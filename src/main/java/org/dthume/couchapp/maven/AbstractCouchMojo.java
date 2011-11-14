package org.dthume.couchapp.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.jcouchdb.db.Database;
import org.jcouchdb.db.Server;
import org.jcouchdb.db.ServerImpl;

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
    
    protected Database getDatabase()
    {
    	return new Database(getServer(), database);
    }
    
    protected Server getServer()
    {
    	final ServerImpl server = new ServerImpl(host, port);
    	
    	return server;
    }
}
