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
package org.dthume.couchapp.maven.util;

import java.io.File;
import java.io.FilenameFilter;

public class IOUtil
{
	private IOUtil() {}
	
	public static Iterable<File> iterateDirectories(final File dir)
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
}
