/*
Copyright 2008 WebAtlas
Authors : Mathieu Bastian, Mathieu Jacomy, Julian Bilcke
Website : http://www.gephi.org

This file is part of Gephi.

Gephi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Gephi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Gephi.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.gephi.project.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.gephi.project.api.Project;
import org.gephi.project.api.Projects;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Mathieu Bastian
 */
public class ProjectsImpl implements Projects, Lookup.Provider, Serializable {

    //Project
    private List<Project> projects = new ArrayList<Project>();
    private ProjectImpl currentProject;
    //Lookup
    private transient InstanceContent ic;
    private transient AbstractLookup lookup;

    public ProjectsImpl() {
        ic = new InstanceContent();
        lookup = new AbstractLookup(ic);
    }

    public void addProject(Project project) {
        if (!projects.contains(project)) {
            projects.add(project);
            ic.add(project);
        }
    }

    public void removeProject(Project project) {
        projects.remove(project);
        ic.remove(project);
    }

    public List<Project> getProjects() {
        return projects;
    }

    public Lookup getLookup() {
        if (lookup == null) {
            ic = new InstanceContent();
            lookup = new AbstractLookup(ic);
        }
        return lookup;
    }

    public void reinitLookup() {
        ic.set(projects, null);
    }

    public ProjectImpl getCurrentProject() {
        return currentProject;
    }

    public boolean hasCurrentProject() {
        return currentProject != null;
    }

    public void setCurrentProject(ProjectImpl currentProject) {
        this.currentProject = currentProject;
    }

    public void closeCurrentProject() {
        this.currentProject = null;
    }
}