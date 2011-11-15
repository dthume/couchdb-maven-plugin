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
import static org.dthume.couchapp.model.CouchAppConstants.toId;

import org.dthume.couchapp.model.CouchAppRepository;
import org.dthume.couchapp.model.CouchDBCouchAppRepository;
import org.dthume.couchapp.model.SingleFilePerCouchAppRepository;
import org.jcouchdb.document.DesignDocument;

/**
 * Goal which pushes one or more couch apps to Couch DB
 *
 * @author dth
 * 
 * @execute phase="package"
 * @goal push
 */
public class PushMojo extends AbstractCouchMojo
{
    private CouchAppRepository inputRepo;
    
    private CouchAppRepository outputRepo;
  
    protected void postConstruct()
    {
    	inputRepo = new SingleFilePerCouchAppRepository(packageDirectory);
    	outputRepo = new CouchDBCouchAppRepository(getDatabase());	
    }
        
    @Override
	protected CouchAppRepository getSourceRepo() { return inputRepo; }

	@Override
	protected CouchAppRepository getTargetRepo() { return outputRepo; }
	
	@Override
	protected DesignDocument processInternal(DesignDocument design) {
		final DesignDocument current =
				getOrCreateDesignDocument(toId(design));
		
		if (!isBlank(current.getRevision()))
			design.setRevision(current.getRevision());
		
		return design;
	}
    
    private DesignDocument getOrCreateDesignDocument(final String application)
    {
    	final DesignDocument design = new DesignDocument(application);
		try
		{
			final DesignDocument current =
					outputRepo.retrieve(application);
			design.setRevision(current.getRevision());
		}
		catch (Exception e) {} // FIXME
		
		return design;
    }    
}
