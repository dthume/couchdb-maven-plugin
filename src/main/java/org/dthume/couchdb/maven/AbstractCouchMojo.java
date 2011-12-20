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

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.dthume.couchdb.repository.CouchAppRepository;
import org.jcouchdb.document.DesignDocument;
import org.svenson.JSON;

/**
 * Base class for couch mojos; defines the base set of configuration
 * properties.
 *
 * @author dth
 */
public abstract class AbstractCouchMojo extends AbstractMojo {
    /**
     * The directory where couchapp sources are stored.
     *
     * @parameter
     *  expression = "${couchapp.sourceDirectory}"
     *  default-value = "${basedir}/src/main/couchapp"
     */
    private File sourceDirectory;

    /**
     * The directory where couchapps are built to.
     *
     * @parameter default-value = "${project.build.directory}/couchapp"
     */
    private File targetDirectory;

    /**
     * The directory where web application files are built to.
     *
     * @parameter
     *  expression = "${project.build.directory}/${project.build.finalName}"
     */
    private File webappDirectory;

    /**
     * The directory where JavaScript files are placed, relative to the
     * {@code webappDirectory}.
     *
     * @parameter
     *  expression = "${scripts}"
     *  default-value = "scripts"
     */
    private String scriptsDirectory;

    public void execute() throws MojoExecutionException {
        postConstruct();
        final CouchAppRepository inputRepo = getSourceRepo();
        final CouchAppRepository outputRepo = getTargetRepo();
        for (final String id : inputRepo.listIds()) {
            DesignDocument app = inputRepo.retrieve(id);
            app = processInternal(app);
            outputRepo.update(app);
        }
    }

    protected void postConstruct() { }

    protected CouchAppRepository getSourceRepo() { return null; }

    protected CouchAppRepository getTargetRepo() { return null; }

    protected DesignDocument processInternal(DesignDocument doc)
            throws MojoExecutionException {
        return doc;
    }

    protected final File getTargetDir(final String relative) {
        return new File(targetDirectory, relative);
    }

    protected final File getArtifactDir() { return getTargetDir("artifacts"); }
    protected final File getCompiledDir() { return getTargetDir("compiled"); }
    protected final File getExpandedDir() { return getTargetDir("expanded"); }
    protected final File getSourceDir() { return sourceDirectory; }
    protected final File getPackagedDir() { return getTargetDir("packaged"); }
    protected final File getScriptsDir() {
        return new File(webappDirectory, scriptsDirectory);
    }

    protected JSON getJSON() { return JSON.defaultJSON(); }
}
