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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.dthume.couchdb.repository.SingleFilePerCouchAppRepository;

/**
 * Attaches packaged couchapps to the current project.
 *
 * @author dth
 *
 * @goal attach-artifacts
 * @requiresProject true
 */
public class AttachArtifactsMojo extends AbstractCouchMojo {
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @component
     * @readonly
     */
    private MavenProjectHelper projectHelper;

    private SingleFilePerCouchAppRepository inputRepo;

    protected void postConstruct() {
        inputRepo = new SingleFilePerCouchAppRepository(getArtifactDir());
    }

    public void execute() throws MojoExecutionException {
        postConstruct();

        for (final String app : inputRepo.listIds())
            attachArtifact(app);
    }

    private void attachArtifact(final String id) {
        final File file = inputRepo.getFile(id);
        projectHelper.attachArtifact(project, "couchapp", id, file);
    }
}
