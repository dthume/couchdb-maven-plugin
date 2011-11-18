/**
 * Copyright (C) 2011 David Thomas Hume <dth at dthu.me>
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
package org.dthume.couchapp.maven;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.dthume.couchapp.repository.CouchAppRepository;
import org.dthume.couchapp.repository.FilesystemCouchAppRepository;
import org.jcouchdb.document.DesignDocument;
import org.jcouchdb.document.View;

import ro.isdc.wro.config.Context;
import ro.isdc.wro.config.jmx.WroConfiguration;
import ro.isdc.wro.extensions.processor.js.GoogleClosureCompressorProcessor;
import ro.isdc.wro.model.resource.processor.ResourcePostProcessor;

/**
 * Compress the JavaScript use for view / show etc. functions.
 *
 * @author dth
 * 
 * @goal compile-js
 */
public class CompileJSMojo extends AbstractCouchMojo
{
    private final ResourcePostProcessor postProcessor =
    	new GoogleClosureCompressorProcessor();

    private CouchAppRepository inputRepo;
    private CouchAppRepository outputRepo;
        
    /**
     * Whether or not to compile JavaScript files
     * 
     * @parameter
     * 	expression = "${couchapp.skipCompilation}"
     *  default-value = false
     */
    protected boolean skipCompilation = false;
    
    @Override
	protected CouchAppRepository getSourceRepo() { return inputRepo; }

	@Override
	protected CouchAppRepository getTargetRepo() { return outputRepo; }

	@Override
	protected void postConstruct()
    {
    	inputRepo =
    		new FilesystemCouchAppRepository(expandedSourcesDirectory);
    	outputRepo =
        	new FilesystemCouchAppRepository(compiledSourcesDirectory);
    	
    	initWRO();
    }
	
	private void initWRO()
	{
		final Context context = Context.standaloneContext();
		
		final WroConfiguration config = new WroConfiguration();
		config.setEncoding("UTF-8"); // FIXME - use build encoding
		context.setConfig(config);
		
		Context.set(context);
	}

	@Override
	protected DesignDocument processInternal(DesignDocument doc)
			throws MojoExecutionException
	{
		doc = super.processInternal(doc);
		
		if (!skipCompilation)
		{
			getLog().debug("Compiling JS in design document: " + doc.getId());
			
			for (Map.Entry<String, View> entry : doc.getViews().entrySet())
				compressView(entry.getValue());
		}
		
		return doc;
	}
	
	private void compressView(View view)
	{
		view.setMap(compressJS(view.getMap()));
		view.setReduce(compressJS(view.getReduce()));
	}
	
	private final static String JS_PREFIX =
		"var __couchapp_anon_function__=";
	
	private String compressJS(String js)
	{
		if (StringUtils.isBlank(js)) return js;
		
		String result = js;
		
		try
		{
			final StringReader reader = new StringReader(JS_PREFIX + js);
			final StringWriter writer = new StringWriter();
			postProcessor.process(reader, writer);
			result = writer.toString().substring(JS_PREFIX.length());
		} catch (IOException e)
		{
			e.printStackTrace(); // FIXME
		}
		
		return result;
	}
}
