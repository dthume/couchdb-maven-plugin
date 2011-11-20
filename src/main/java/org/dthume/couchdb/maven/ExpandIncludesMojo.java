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

import static org.apache.commons.io.FileUtils.readFileToString;

import static org.dthume.couchdb.model.CouchAppConstants.toId;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.MojoExecutionException;
import org.dthume.couchdb.repository.CouchAppRepository;
import org.dthume.couchdb.repository.FilesystemCouchAppRepository;
import org.jcouchdb.document.DesignDocument;
import org.jcouchdb.document.View;

/**
 * Expands include (!code [FILE]) comments in couch app files
 *
 * @author dth
 * 
 * @goal expand-includes
 */
public class ExpandIncludesMojo extends AbstractCouchMojo
{
    private CouchAppRepository inputRepo;
    private CouchAppRepository outputRepo;
    
    protected void postConstruct()
    {
    	inputRepo =
    		new FilesystemCouchAppRepository(sourceDirectory);
    	outputRepo =
    		new FilesystemCouchAppRepository(getExpandedDir());
    }
    
    @Override
	protected CouchAppRepository getSourceRepo() { return inputRepo; }
    
	@Override
	protected CouchAppRepository getTargetRepo() { return outputRepo; }

	@Override
    protected DesignDocument processInternal(final DesignDocument doc)
    	throws MojoExecutionException
    {
		getLog().info("Expanding includes for app: " + toId(doc));
    	try
    	{
    		expandAppIncludes(doc);
    		return doc;
    	}
    	catch (IOException e)
    	{
    		throw new MojoExecutionException("Caught IOException while expanding includes", e);
    	}
    }
    
    private void expandAppIncludes(final DesignDocument doc)
    	throws IOException
    {
    	for (final Map.Entry<String, View> entry : doc.getViews().entrySet())
    		expandIncludeReferences(entry.getValue());
    }
    
    private void expandIncludeReferences(final View view)
    	throws IOException
    {
    	view.setMap(expandIncludeReferences(view.getMap()));
    	view.setReduce(expandIncludeReferences(view.getReduce()));
    }
    
    private final static Pattern INCLUDES_PATTERN =
        	Pattern.compile("^\\s*//\\s*!code\\s+([^\\s]+)\\s*$",
        			Pattern.MULTILINE);
    
    private String expandIncludeReferences(final String js)
    	throws IOException {
    	if (null == js) return js;
    	
		final StringBuffer sb = new StringBuffer();
		
		final Matcher matcher = INCLUDES_PATTERN.matcher(js);
		while (matcher.find()) {
			final String replacement =
				expandIncludeReferences(readInclude(matcher.group(1)));
			matcher.appendReplacement(sb, replacement);
			sb.append("\n");
		}
		matcher.appendTail(sb);
		
		return sb.toString();
    }
    
    private String readInclude(String include) throws IOException {
    	final File base = new File(webappDirectory, scriptsDirectory);
		return readFileToString(new File(base, include));
	}
}
