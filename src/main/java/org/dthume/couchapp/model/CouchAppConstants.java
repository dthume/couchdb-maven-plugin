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
package org.dthume.couchapp.model;

import org.jcouchdb.document.DesignDocument;

public class CouchAppConstants
{
	private CouchAppConstants() { }
	
	public final static String MAP_FILE = "map.js";
	public final static String REDUCE_FILE = "reduce.js";
	
	public final static String VIEWS_FILE = "views";
	
	public static String toId(DesignDocument doc)
	{
		final String id = doc.getId();
		return id.substring(id.indexOf("/") + 1);
	}
}
