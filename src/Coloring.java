import java.util.*;

import org.apache.commons.collections15.Factory;

/**
 * Created by Barrionuevo on 11/4/14.
 */
public class Coloring<V, E> {
    // Instance Variables
    private Factory<Graph<V, E>> graphFactory;
    private Graph<V, E> graph;

    // Constructor
    public Coloring(Factory<Graph<V, E>> factory) {
        graphFactory = factory;
    }

    // Methods
    public Set<Set<V>> graphColoring(Graph<V, E> g) {
        Map<Integer, Set<V>> coloredGroups = backtrackGraphColoring(g);
        Set<Set<V>> cs = new HashSet<Set<V>>();
        for (int scolor : coloredGroups.keySet()) {
            cs.add(coloredGroups.get(scolor));
        }
        return cs;
    }

    public Map<Integer, Set<V>> backtrackGraphColoring(Graph<V, E> inGraph) {
        this.graph = copyGraph(inGraph, this.graphFactory);
        Map<Integer, Set<V>> coloredSets = new HashMap<Integer, Set<V>>();
        Iterator<V> iter = inGraph.getVertices().iterator();
        return m_coloring(iter, coloredSets);
    }

    public Map<Integer, Set<V>> m_coloring(Iterator<V> iter, Map<Integer, Set<V>> coloredSets) {
        if (!iter.hasNext()) {
            return coloredSets;
        } else {
            V v = iter.next();
            for (int color = 1; ; color++) {
                if (promising(v, color, coloredSets)) {
                    if (coloredSets.get(color) == null)
                        coloredSets.put(color, new LinkedHashSet<V>());
                    coloredSets.get(color).add(v);
                    return m_coloring(iter, coloredSets);
                }
            }
        }
    }

    public boolean promising(V vertex, int color, Map<Integer, Set<V>> coloredSets) {
        boolean flag = true;
        Collection<V> neighbors = graph.getNeighbors(vertex);
        for (Iterator<V> innerIter = neighbors.iterator(); innerIter.hasNext(); ) {
            V n = innerIter.next();
            if (coloredSets.get(color) != null && coloredSets.get(color).contains(n)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static <V, E> Graph<V, E> copyGraph(Graph<V, E> g, Factory<Graph<V, E>> f) {
        Graph<V, E> graph = f.create();
        for (V x : g.getVertices()) {
            graph.addVertex(x);
        }
        for (E e : g.getEdges()) {
            graph.addEdge(e, g.getEndpoints(e).getFirst(), g.getEndpoints(e).getSecond());
        }
        return graph;
    }
}
