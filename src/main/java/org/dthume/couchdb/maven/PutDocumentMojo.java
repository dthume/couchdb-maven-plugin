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

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.svenson.JSONParser;

/**
 * Standalone goal which puts a document into Couch DB
 * 
 * @author dth
 * 
 * @goal put-document
 * @requiresProject false
 * @requiresDirectInvocation true
 */
public class PutDocumentMojo extends AbstractOnlineCouchMojo {
    /**
     * The id of the document
     * 
     * @parameter expression="${couchapp.documentId}"
     * @required
     */
    private String documentId;

    /**
     * The file to put.
     * 
     * @parameter expression="${couchapp.file}"
     */
    private File inputFile;

    public void execute() throws MojoExecutionException {
        try {
            executeInternal();
        } catch (IOException e) {
            throw new MojoExecutionException(
                    "Caught IOException during mojo execution", e);
        }
    }

    private void executeInternal() throws IOException {
        final Map<String, Object> existing = getDocument();
        final Map<String, Object> source = getSource();
        
        if (null != existing) {
            source.put("_revision", existing.get("_revision"));
        }
        
        source.put("_id", documentId);
        
        getDatabase().createOrUpdateDocument(source);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getDocument() {
        try {
            final Object document =
                getDatabase().getDocument(Map.class, documentId); 
            return (Map<String, Object>)document;
        }
        catch (RuntimeException e) {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    private Map<String, Object> getSource() throws IOException {
        final String data = IOUtils.toString(inputFile.toURI());
        return JSONParser.defaultJSONParser().parse(Map.class, data);
    }
}
