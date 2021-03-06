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

import java.util.Collection;

import org.dthume.couchdb.model.CouchAppConstants;
import org.jcouchdb.db.Database;
import org.jcouchdb.document.DesignDocument;

public class CouchDBCouchAppRepository implements CouchAppRepository {

    private Database database;

    public CouchDBCouchAppRepository(final Database database) {
        this.database = database;
    }

    public Collection<String> listIds() {
        return java.util.Collections.emptyList();
    }

    public DesignDocument create(DesignDocument app) {
        return update(app);
    }

    public DesignDocument retrieve(String id) {
        return database.getDesignDocument(id);
    }

    public DesignDocument update(DesignDocument app) {
        database.createOrUpdateDocument(app);
        return database.getDesignDocument(CouchAppConstants.toId(app));
    }

    public boolean delete(DesignDocument app) {
        database.delete(app);
        return true;
    }
}
