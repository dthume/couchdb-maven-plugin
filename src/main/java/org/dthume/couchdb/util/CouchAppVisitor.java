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

import java.util.List;
import java.util.Map;

public interface CouchAppVisitor {
    public enum Status {
        CONTINUE,
        SKIP,
        STOP
    }
    
    Status visitBoolean(String key, boolean value);
    
    Status visitString(String key, String value);
    
    Status visitNumber(String key, Number number);
    
    Status visitArray(String key, List<Object> array);
    
    Status visitObject(String key, Map<String, Object> object);
    
    Status visitNull(String key);
}
