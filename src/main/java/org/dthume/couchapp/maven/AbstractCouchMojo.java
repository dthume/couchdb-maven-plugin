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

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.dthume.couchapp.model.CouchAppRepository;
import org.jcouchdb.db.Database;
import org.jcouchdb.db.Server;
import org.jcouchdb.db.ServerImpl;
import org.jcouchdb.document.DesignDocument;
import org.svenson.JSON;

public abstract class AbstractCouchMojo extends AbstractMojo
{
    /**
     * @parameter expression="${couchapp.host}"
     * @required
     */
    private String host;
    
    /**
     * @parameter
     * 	expression = "${couchapp.port}"
     * 	default-value = "5984" 
     * @required
     */
    private int port;
    
    /**
     * @parameter expression="${couchapp.database}"
     * @required
     */
    private String database;
    
    /**
     * @parameter expression="${couchapp.username}"
     */
    private String username;
    
    /**
     * @parameter expression="${couchapp.password}"
     */
    private String password;
    
    /**
     * @required
     * @parameter
     *  expression = "${couchapp.sourceDirectory}"
     * 	default-value = "${basedir}/src/main/couchapp"
     */
    protected File sourceDirectory;
	
    /**
     * @required
     * @parameter
     *  expression = "${project.build.directory}/${project.build.finalName}"
     */
    protected File webappDirectory;
    
    /**
     * @required
     * @parameter
     *  expression = "${scripts}"
     *  default-value = "scripts"
     */
    protected String scriptsDirectory;
    
    /**
     * @required
     * @parameter
     *  expression = "${couchapp.expandedSourcesDirectory}"
     * 	default-value = "${project.build.directory}/couchapp/expanded"
     */
    protected File expandedSourcesDirectory;
    
    /**
     * @required
     * @parameter
     *  expression = "${couchapp.packageDirectory}"
     * 	default-value = "${project.build.directory}/couchapp/packages"
     */
    protected File packageDirectory;
    
    public void execute() throws MojoExecutionException
    {
    	postConstruct();
    	final CouchAppRepository inputRepo = getSourceRepo();
    	final CouchAppRepository outputRepo = getTargetRepo();
    	for (final String id : inputRepo.listIds())
    	{
    		DesignDocument app = inputRepo.retrieve(id);
    		app = processInternal(app);
    		outputRepo.update(app);
    	}
    }
    
    protected void postConstruct() {}
    
    protected CouchAppRepository getSourceRepo() {
    	return null;
    }
    
    protected CouchAppRepository getTargetRepo() {
    	return null;
    }
    
    protected DesignDocument processInternal(DesignDocument doc)
    	throws MojoExecutionException {
    	return doc;
    }
    
    protected Database getDatabase()
    {
    	return new Database(getServer(), database);
    }
    
    protected Server getServer()
    {
    	final ServerImpl server = new ServerImpl(host, port);
    	if (!StringUtils.isBlank(username))
    	{
    		final Credentials creds = 
    				new UsernamePasswordCredentials(username, password);
    		server.setCredentials(AuthScope.ANY, creds);
    	}
    	return server;
    }
    
    protected JSON getJSON()
    {
    	return JSON.defaultJSON();
    }
}
