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

import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.maven.plugin.MojoExecutionException;
import org.dthume.couchdb.repository.CouchAppRepository;
import org.jcouchdb.db.Database;
import org.jcouchdb.db.Server;
import org.jcouchdb.db.ServerImpl;
import org.jcouchdb.document.DesignDocument;

/**
 * Base class for couch mojos; defines the base set of configuration
 * properties.
 *
 * @author dth
 */
public abstract class AbstractJSProcessingCouchMojo
    extends AbstractCouchMojo {

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
}
