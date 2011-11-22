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
package org.dthume.couchdb.util;

import static org.dthume.couchdb.util.CouchAppVisitor.Status.CONTINUE;
import static org.dthume.couchdb.util.CouchAppVisitor.Status.SKIP;
import static org.dthume.couchdb.util.CouchAppVisitor.Status.STOP;

import java.util.Map;
import java.util.Stack;

public class CouchAppWalker {
    private void walk(Object item, final CouchAppVisitor visitor) {
        final Stack<Object> stack = new Stack<Object>();
        
        String key = null;
        CouchAppVisitor.Status status = CONTINUE;

        do
        {
            if (item instanceof Map) {
                status = visitor.visitObject(key, (Map<String, Object>)item);
                if (CONTINUE.equals(status)) {
                    stack.push(item);
                    
                }
            }
        }        
        while(!STOP.equals(status));
    }
}
