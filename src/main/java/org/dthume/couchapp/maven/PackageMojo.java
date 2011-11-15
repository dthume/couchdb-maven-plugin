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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.dthume.couchapp.model.CouchAppRepository;
import org.dthume.couchapp.model.FilesystemCouchAppRepository;
import org.dthume.couchapp.model.SingleFilePerCouchAppRepository;

/**
 * Goal which packages a couchapp in preparation for couch db
 *
 * @author dth
 * 
 * @goal package
 */
public class PackageMojo extends AbstractCouchMojo
{
    /**
     * @required
     * @parameter
     *  expression = "${couchapp.expandedSourcesDirectory}"
     * 	default-value = "${project.build.directory}/couchapp/expanded"
     */
    private File expandedSourcesDirectory;
    
    /**
     * @required
     * @parameter
     *  expression = "${couchapp.packageDirectory}"
     * 	default-value = "${project.build.directory}/couchapp/packages"
     */
    private File packageDirectory;
    
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;
    
    /**
     * @component
     * @readonly
     */
    private MavenProjectHelper projectHelper;
    
    private CouchAppRepository inputRepo;
    private SingleFilePerCouchAppRepository outputRepo;
  
    private void postConstruct()
    {
    	inputRepo =
    		new FilesystemCouchAppRepository(expandedSourcesDirectory);
    	outputRepo =
        	new SingleFilePerCouchAppRepository(packageDirectory);
    }
    
    public void execute() throws MojoExecutionException
    {
    	postConstruct();
    	
    	for (final String app : inputRepo.listIds())
    	{
    		updateApplication(app);
    		attachArtifact(app);
    	}
    }
    
    private void updateApplication(final String id)
    throws MojoExecutionException
    {
    	getLog().info("Packaging application: " + id);
    	outputRepo.update(inputRepo.retrieve(id));
    }
    
    private void attachArtifact(final String id)
    {
    	final File file = outputRepo.getFile(id);
    	projectHelper.attachArtifact(project, "couchapp", id, file);
    }
}
