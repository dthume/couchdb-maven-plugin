/**
 * Copyright (C) 2011 David Thomas Hume <dth@dthu.me>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dthume.couchdb.maven;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.reflection.Reflector;
import org.codehaus.plexus.util.reflection.ReflectorException;
import org.dthume.couchdb.repository.CouchAppRepository;
import org.dthume.couchdb.repository.FilesystemCouchAppRepository;
import org.jcouchdb.document.DesignDocument;
import org.jcouchdb.document.View;

import ro.isdc.wro.config.Context;
import ro.isdc.wro.config.jmx.WroConfiguration;
import ro.isdc.wro.model.resource.processor.ResourcePostProcessor;

/**
 * Compress the JavaScript used for view / show etc. functions.
 *
 * @author dth
 *
 * @goal compile-js
 */
public final class CompileJSMojo extends AbstractJSProcessingCouchMojo {

    private ResourcePostProcessor postProcessor;

    private CouchAppRepository inputRepo;
    private CouchAppRepository outputRepo;

    /**
     * Whether or not to compile JavaScript files.
     *
     * @parameter
     *  expression="${couchapp.skipCompilation}"
     *  default-value=false
     */
    private boolean skipCompilation = false;

    /**
     * Whether or not to compile JavaScript files.
     *
     * @parameter expression="${project.build.sourceEncoding}"
     */
    private String sourceEncoding = "UTF-8";

    /**
     * Compiler processor class.
     *
     * @parameter
     *  expression="${couchdb.compilerProcessorClass}"
     *  default-value="ro.isdc.wro.model.resource.processor.impl.js.JSMinProcessor"
     */
    private String processorClass;

    @Override
    protected CouchAppRepository getSourceRepo() {
        return inputRepo;
    }

    @Override
    protected CouchAppRepository getTargetRepo() {
        return outputRepo;
    }

    @Override
    protected void postConstruct() {
        inputRepo = new FilesystemCouchAppRepository(getExpandedDir());
        outputRepo = new FilesystemCouchAppRepository(getCompiledDir());

        initWRO();
    }

    private void initWRO() {
        final Context context = Context.standaloneContext();

        final WroConfiguration config = new WroConfiguration();
        config.setEncoding(sourceEncoding);
        context.setConfig(config);

        Context.set(context);

        initPostProcessor();
    }

    private void initPostProcessor() {
        final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            final Class<?> clazz = Class.forName(processorClass, true, cl);
            final Reflector reflector = new Reflector();
            final Object[] args = new Object[] {};
            postProcessor = (ResourcePostProcessor) reflector.newInstance(
                    clazz, args);
        } catch (ClassNotFoundException e) {
            postProcessor = new ResourcePostProcessor() {
                public void process(final Reader reader, final Writer writer)
                        throws IOException {
                    IOUtils.copy(reader, writer);
                }
            };
        } catch (ReflectorException e) {
            e.printStackTrace();
            throw new RuntimeException(e); // FIXME
        }
    }

    @Override
    protected DesignDocument processInternal(final DesignDocument input)
            throws MojoExecutionException {
        final DesignDocument doc = super.processInternal(input);

        if (!skipCompilation) {
            getLog().debug("Compiling JS in design document: " + doc.getId());

            compileFilters(doc);
            compileViews(doc);
        }

        return doc;
    }

    private void compileViews(final DesignDocument doc) {
        for (Map.Entry<String, View> entry : doc.getViews().entrySet())
            compileView(entry.getValue());
    }

    private void compileView(final View view) {
        view.setMap(compileJS(view.getMap()));
        view.setReduce(compileJS(view.getReduce()));
    }

    private void compileFilters(final DesignDocument doc) {
        final Map<String, String> source = doc.getFilterFunctions();
        final Map<String, String> result = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : source.entrySet())
            result.put(entry.getKey(), compileJS(entry.getValue()));
        doc.setFilterFunctions(result);
    }

    private static final String JS_PREFIX = "var __couchapp_anon_function__=";

    private String compileJS(final String js) {
        if (StringUtils.isBlank(js))
            return js;

        String result = js;

        try {
            final StringReader reader = new StringReader(JS_PREFIX + js);
            final StringWriter writer = new StringWriter();

            postProcessor.process(reader, writer);
            result = writer.toString();
            result = result.substring(result.indexOf("=") + 1);
        } catch (IOException e) {
            e.printStackTrace(); // FIXME
        }

        return result;
    }
}
