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

import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.jcouchdb.db.Database;
import org.jcouchdb.db.Server;
import org.jcouchdb.db.ServerImpl;

/**
 * Base class for couch mojos; defines the base set of configuration
 * properties.
 *
 * @author dth
 */
public abstract class AbstractOnlineCouchMojo extends AbstractCouchMojo {

    /**
     * The hostname of the Couch instance.
     *
     * @parameter expression="${couchapp.host}" default-value = "localhost"
     */
    private String host;

    /**
     * The port of the Couch instance.
     *
     * @parameter
     *  expression = "${couchapp.port}"
     *  default-value = "5984"
     */
    private int port;

    /**
     * ID of the database to work with.
     *
     * @parameter expression="${couchapp.database}"
     */
    private String database;

    /**
     * The username to use when communicating with the Couch instance.
     *
     * @parameter expression="${couchapp.username}"
     */
    private String username;

    /**
     * The password to use when communicating with the Couch instance.
     *
     * @parameter expression="${couchapp.password}"
     */
    private String password;

    protected Database getDatabase() {
        return new Database(getServer(), database);
    }

    protected Server getServer() {
        final ServerImpl server = new ServerImpl(host, port);
        if (!StringUtils.isBlank(username)) {
            final Credentials creds =
                    new UsernamePasswordCredentials(username, password);
            server.setCredentials(AuthScope.ANY, creds);
        }
        return server;
    }
}
