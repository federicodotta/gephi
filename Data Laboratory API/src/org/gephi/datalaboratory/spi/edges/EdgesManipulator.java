
/*
Copyright 2008-2010 Gephi
Authors : Eduardo Ramos <eduramiba@gmail.com>
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
package org.gephi.datalaboratory.spi.edges;

import org.gephi.datalaboratory.spi.GraphElementsManipulator;
import org.gephi.graph.api.Edge;

/**
 * GraphElementsManipulator for edges.
 * @see GraphElementsManipulator
 * @author Eduardo Ramos <eduramiba@gmail.com>
 */
public interface EdgesManipulator extends GraphElementsManipulator{
    /**
     * Prepare edges for this action.
     * @param edges All selected edges to operate
     * @param clickedEdge The right clicked edge of all edges
     */
    void setup(Edge[] edges, Edge clickedEdge);
}
