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
package org.dthume.couchdb.repository;

import static org.apache.commons.io.FileUtils.listFiles;
import static org.dthume.couchdb.model.CouchAppConstants.toId;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.jcouchdb.document.DesignDocument;
import org.svenson.JSON;
import org.svenson.JSONParser;

public class SingleFilePerCouchAppRepository
	implements CouchAppRepository {
	
	public final static String DEFAULT_EXTENSION = ".couchapp";
	
	private final File baseDir;
	private final String[] extensions = new String[] { "couchapp" };
	
	public SingleFilePerCouchAppRepository(File baseDir) {
		this.baseDir = baseDir;
	}

	public Collection<String> listIds() {
		final Collection<File> files = listFiles(baseDir, extensions, false); 
		final Collection<String> names = new java.util.ArrayList<String>();
		for (final File file : files)
			names.add(toAppId(file));
		return names;
	}
	
	private String toAppId(final File file)
	{
		final String name = file.getName();
		return name.substring(0, name.lastIndexOf('.'));
	}

	public DesignDocument create(DesignDocument app) {
		return update(app);
	}
	
	public File getFile(String id) {
		return new File(baseDir, id + DEFAULT_EXTENSION);
	}

	public DesignDocument retrieve(String id) {
		try {
			return read(id);
		}
		catch (IOException e) {
			throw new IllegalArgumentException("error reading app with id: " + id, e);
		}
	}

	public DesignDocument update(DesignDocument app) {
		try {
			write(app);
		}
		catch (IOException e) {
			throw new IllegalArgumentException("error writing app with id: " + toId(app), e);
		}
		return app;
	}

	public boolean delete(DesignDocument app) {
		final String name = toId(app) + DEFAULT_EXTENSION;
		return new File(baseDir, name).delete();
	}
	
	private JSON getJSON() { return JSON.defaultJSON(); }
	
	private JSONParser getJSONParser() {
		return JSONParser.defaultJSONParser();
	}
	
	protected DesignDocument read(final String id)
		throws IOException {
		final String json = IOUtils.toString(getInputStream(id));
		final DesignDocument doc =
				getJSONParser().parse(DesignDocument.class, json);
		doc.setId("_design/" + id);
		return doc;
	}
	
	protected void write(final DesignDocument app)
		throws IOException {
		baseDir.mkdirs();
		final OutputStream out = getOutputStream(toId(app));
		final Writer writer = new java.io.OutputStreamWriter(out);
		try
		{
			getJSON().writeJSONToWriter(app, writer);
		}
		finally
		{
			if (null != writer) IOUtils.closeQuietly(writer);
		}
	}
	
	protected OutputStream getOutputStream(final String id)
		throws IOException
	{
		return new FileOutputStream(new File(baseDir, id + DEFAULT_EXTENSION));
	}
	
	protected InputStream getInputStream(final String id)
			throws IOException
	{
		return new FileInputStream(new File(baseDir, id + DEFAULT_EXTENSION));
	}
}
