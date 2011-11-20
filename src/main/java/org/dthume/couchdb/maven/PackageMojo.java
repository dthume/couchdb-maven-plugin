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

import org.dthume.couchdb.repository.CouchAppRepository;
import org.dthume.couchdb.repository.FilesystemCouchAppRepository;
import org.dthume.couchdb.repository.SingleFilePerCouchAppRepository;

/**
 * Packages a couchapp in preparation for deployment to couch db or an
 * artifact repository.
 *
 * @author dth
 * 
 * @goal package
 */
public class PackageMojo extends AbstractCouchMojo
{
    private CouchAppRepository inputRepo;
    private SingleFilePerCouchAppRepository outputRepo;
    
    @Override
	protected CouchAppRepository getSourceRepo() { return inputRepo; }

	@Override
	protected CouchAppRepository getTargetRepo() { return outputRepo; }

	@Override
	protected void postConstruct()
    {
    	inputRepo =
    		new FilesystemCouchAppRepository(getCompiledDir());
    	outputRepo =
        	new SingleFilePerCouchAppRepository(getPackagedDir());
    }
}
