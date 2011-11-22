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

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.svenson.JSON;

/**
 * Standalone goal which gets a document from Couch DB
 *
 * @author dth
 *
 * @goal get-document
 * @requiresProject false
 * @requiresDirectInvocation true
 */
public class GetDocumentMojo extends AbstractOnlineCouchMojo
{
    /**
     * @parameter expression="${couchapp.documentId}"
     * @required
     */
    private String documentId;
    
    /**
     * @parameter expression="${couchapp.file}" default-value="-"
     * @required
     */
    private String outputFile;
	
    public void execute() throws MojoExecutionException
    {
    	try
    	{
    		executeInternal();
    	}
    	catch (IOException e)
    	{
    		throw new MojoExecutionException("Caught IOException during mojo execution", e);
    	}
    }
    
    private void executeInternal() throws IOException
    {
    	final PrintStream output = getOutputStream();
    	final Object document = getDocument();
    	
    	writeJSON(document, output);
    }
    
    private Object getDocument()
    {
    	return getDatabase().getDocument(Map.class, documentId);
    }
    
    private PrintStream getOutputStream() throws IOException
    {
    	return "-".equals(outputFile) ?
    			System.out : new PrintStream(outputFile);
    }
    
    private void writeJSON(Object document, PrintStream output)
    {
    	output.println(
        	JSON.formatJSON(
        		JSON.defaultJSON().forValue(document)
        	)
        );
    }
}
