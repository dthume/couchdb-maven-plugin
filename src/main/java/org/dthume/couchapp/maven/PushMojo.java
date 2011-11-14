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

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.dthume.couchapp.model.CouchAppRepository;
import org.dthume.couchapp.model.FilesystemCouchAppRepository;
import org.jcouchdb.document.DesignDocument;

/**
 * Goal which pushes to Couch DB
 *
 * @author dth
 * 
 * @execute phase="package"
 * @goal push
 */
public class PushMojo extends AbstractCouchMojo
{
    /**
     * @required
     * @parameter
     *  expression = "${couchapp.packageDirectory}"
     * 	default-value = "${project.build.directory}/couchapp/packages"
     */
    private String packageDirectory;    
    
    private CouchAppRepository inputRepo;
  
    public void execute() throws MojoExecutionException
    {
    	final File dir = new File(packageDirectory);
    	inputRepo = new FilesystemCouchAppRepository(dir);
    	
    	for (final String app : inputRepo.listIds())
    		updateApplication(app);
    }
    
    private CouchAppRepository getInputRepo() { return inputRepo; }
    
    private void updateApplication(final String id)
    throws MojoExecutionException
    {
    	getLog().info("Pushing application: " + id);
    	try
    	{
    		new RequestHandler(id).handle();
    	}
    	catch (IOException e)
    	{
    		throw new MojoExecutionException("Caught IOException while pushing application", e);
    	}

    }
    
    private DesignDocument getOrCreateDesignDocument(final String application)
    {
    	final DesignDocument design = new DesignDocument(application);
		try
		{
			final DesignDocument current =
					getDatabase().getDesignDocument(application);
			design.setRevision(current.getRevision());
		}
		catch (Exception e) {} // FIXME
		
		return design;
    }
    
    private class RequestHandler
    {
    	final DesignDocument design;
    	
    	RequestHandler(String id)
    	{
    		design = getInputRepo().retrieve(id);
    		final DesignDocument current = getOrCreateDesignDocument(id);
    		if (!isBlank(current.getRevision()))
    			design.setRevision(current.getRevision());
    	}
    	
    	void handle() throws IOException
    	{
    		getDatabase().createOrUpdateDocument(design);
    	}    	
    }    
}
