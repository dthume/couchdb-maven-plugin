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
package org.dthume.couchdb.maven;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.dthume.couchdb.repository.CouchAppRepository;
import org.jcouchdb.db.Database;
import org.jcouchdb.db.Server;
import org.jcouchdb.db.ServerImpl;
import org.jcouchdb.document.DesignDocument;
import org.svenson.JSON;

public abstract class AbstractCouchMojo extends AbstractMojo
{
    /**
     * The hostname of the Couch instance
     * 
     * @parameter
     * 	expression="${couchapp.host}"
     * 	default-value = "localhost"
     */
    private String host;
    
    /**
     * The port of the Couch instance
     * 
     * @parameter
     * 	expression = "${couchapp.port}"
     * 	default-value = "5984"
     */
    private int port;
    
    /**
     * ID of the database to work with
     * 
     * @parameter expression="${couchapp.database}"
     */
    private String database;
    
    /**
     * The username to use when communicating with the Couch instance
     * 
     * @parameter expression="${couchapp.username}"
     */
    private String username;
    
    /**
     * The password to use when communicating with the Couch instance
     * 
     * @parameter expression="${couchapp.password}"
     */
    private String password;
    
    /**
     * The directory where couchapp sources are stored
     * 
     * @parameter
     *  expression = "${couchapp.sourceDirectory}"
     * 	default-value = "${basedir}/src/main/couchapp"
     */
    protected File sourceDirectory;
	
    /**
     * @parameter
     *  expression = "${project.build.directory}/${project.build.finalName}"
     */
    protected File webappDirectory;
    
    /**
     * @parameter
     *  expression = "${scripts}"
     *  default-value = "scripts"
     */
    protected String scriptsDirectory;
    
    /**
     * The directory to use for expanded couchapp source files
     * 
     * @parameter
     *  expression = "${couchapp.expandedSourcesDirectory}"
     * 	default-value = "${project.build.directory}/couchapp/expanded"
     */
    protected File expandedSourcesDirectory;
    
    /**
     * The directory to use for compiled couchapp source files
     * 
     * @parameter
     *  expression = "${couchapp.compiledSourcesDirectory}"
     * 	default-value = "${project.build.directory}/couchapp/compiled"
     */
    protected File compiledSourcesDirectory;
    
    /**
     * The directory to use for packaged couchapps
     * 
     * @parameter
     *  expression = "${couchapp.packageDirectory}"
     * 	default-value = "${project.build.directory}/couchapp/packages"
     */
    protected File packageDirectory;
    
    /**
     * The directory to use for couchapps packaged as maven artifacts
     * 
     * @parameter
     *  expression = "${couchapp.artifactsDirectory}"
     * 	default-value = "${project.build.directory}/couchapp/artifacts"
     */
    protected File artifactsDirectory;
    
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
