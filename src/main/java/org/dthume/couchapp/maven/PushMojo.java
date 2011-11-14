/*
 * Copyright 2011 David Thomas Hume
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dthume.couchapp.maven;

import static org.apache.commons.io.FileUtils.getFile;
import static org.apache.commons.io.FileUtils.listFiles;
import static org.apache.commons.io.FileUtils.iterateFiles;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.filefilter.FileFilterUtils.directoryFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.trueFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.falseFileFilter;

import static org.dthume.couchapp.maven.CouchAppConstants.MAP_FILE;
import static org.dthume.couchapp.maven.CouchAppConstants.REDUCE_FILE;
import static org.dthume.couchapp.maven.CouchAppConstants.VIEWS_FILE;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.MojoExecutionException;
import org.jcouchdb.document.DesignDocument;
import org.jcouchdb.document.View;

/**
 * Goal which pushes to Couch DB
 *
 * @author dth
 *
 * @goal push
 */
public class PushMojo extends AbstractCouchMojo
{
    /**
     * @required
     * @parameter
     *  expression = "${couchapp.sourceDirectory}"
     * 	default-value = "${basedir}/src/main/couchapp"
     */
    private String sourceDirectory;
    
    /**
     * @required
     * @parameter
     *  expression = "${couchapp.libraryDirectory}"
     * 	default-value = "${basedir}/src/main/javascript"
     */
    private String javascriptLibraryDirectory;
	
    /**
     * @required
     * @parameter expression = "${couchapp}"
     */
    private String application;
    
    public void execute() throws MojoExecutionException
    {
    	try
    	{
    		new RequestHandler(application).handle();
    	}
    	catch (IOException e)
    	{
    		throw new MojoExecutionException("Caught IOException while pushing application", e);
    	}
    }
    
    private File getApplicationDirectory()
    {
    	return new File(sourceDirectory, application);
    }
    
    private DesignDocument getOrCreateDesignDocument()
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
    
    private static Iterable<File> iterateDirectories(final File dir)
    {
    	final String[] names = dir.list(new FilenameFilter()
    	{
			public boolean accept(File dir, String name)
			{
				return new File(dir, name).isDirectory();
			}
    	});
    	
    	final java.util.List<File> directories =
    		new java.util.ArrayList<File>(names.length);
    	
    	for (final String name : names)
    		directories.add(new File(dir, name));
    	
    	return directories;
    }
    
    private static void ensureExists(File file, String template)
    {
    	if (!file.exists())
    	{
    		final String msg = String.format(template, file.getName());
    		throw new IllegalArgumentException(msg);
    	}
    }
    
    private class RequestHandler
    {
    	final File baseDir;
    	final DesignDocument design;
    	
    	RequestHandler(String name)
    	{
    		baseDir = getApplicationDirectory();
    		design = getOrCreateDesignDocument();
    	}
    	
    	void handle() throws IOException
    	{
    		addViews();
    		
    		getDatabase().createOrUpdateDocument(design);
    	}
    	
    	void addViews() throws IOException
    	{
    		final File viewDir = getViewsDir();
    		
    		getLog().debug("Loading views from: " + viewDir);
    		
    		for (final File f : iterateDirectories(getViewsDir()))
    			addView(f);
    	}
    	
    	File getViewsDir()
    	{
    		return new File(baseDir, VIEWS_FILE);
    	}
    	
    	void addView(final File viewDir) throws IOException
    	{
    		getLog().debug("Adding view: " + viewDir.getName());
    		final View view = new View();
    		
    		final File map = getFile(viewDir, MAP_FILE);
    		ensureExists(map, "view map function (%s) must exist");
    		view.setMap(readAndExpand(map));
    		
    		final File reduce = getFile(viewDir, REDUCE_FILE);
    		if (reduce.exists())
    		{
    			view.setReduce(readAndExpand(reduce));
    		}
    		
    		design.addView(viewDir.getName(), view);
    	}
    	
    	String readAndExpand(File file) throws IOException
    	{
    		return expandIncludes(readFileToString(file));
    	}
    	
    	String expandIncludes(String json) throws IOException
    	{
    		final StringBuffer sb = new StringBuffer();
    		final Matcher matcher = INCLUDES_PATTERN.matcher(json);
    		while (matcher.find())
    		{
    			final String replacement =
    				expandIncludes(readInclude(matcher.group(1)));
    			matcher.appendReplacement(sb, replacement);
    			sb.append("\n");
    		}
    		matcher.appendTail(sb);
    		return sb.toString();
    	}
    	
    	String readInclude(String include) throws IOException
    	{
    		return readFileToString(new File(javascriptLibraryDirectory, include));
    	}
    }
    
    private final static Pattern INCLUDES_PATTERN =
    	Pattern.compile("^\\s*//\\s*!code\\s+([^\\s]+)\\s*$", Pattern.MULTILINE);
}
