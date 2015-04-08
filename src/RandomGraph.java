

import org.apache.commons.collections15.Factory;

/**
 * This class allows to generate random undirected graphs.
 * Created by Barrionuevo on 11/4/14.
 */
public class RandomGraph {

    public static <V, E> Graph<V, E> generateUndirectedGraph(Factory<Graph<V, E>> graphFactory, Factory<V> vertexFactory, Factory<E> edgeFactory, int n, double p) {
        Graph<V, E> graph = graphFactory.create();

        for (int i = 0; i < n; i++)
            graph.addVertex(vertexFactory.create());
        V[] varray = (V[]) graph.getVertices().toArray();

        for (int i = 0; i < varray.length; i++)
            for (int j = i; j < varray.length; j++) {
                if (Math.random() <= p) {
                    if (varray[i] != varray[j] && !graph.isNeighbor(varray[i], varray[j]))
                        graph.addEdge(edgeFactory.create(), varray[i], varray[j]);
                }
                if (graph.getNeighborCount(varray[i]) == 0) {
                    if (i > 0)
                        graph.addEdge(edgeFactory.create(), varray[i], varray[i - 1]);
                    else if (varray.length != 1)
                        graph.addEdge(edgeFactory.create(), varray[i], varray[varray.length - 1]);
                }
            }

        for (int i = 0; i < varray.length; i++)
            if (graph.getNeighborCount(varray[i]) < 2) {
                if (i > 0)
                    graph.addEdge(edgeFactory.create(), varray[i], varray[i - 1]);
                else if (varray.length != 1)
                    graph.addEdge(edgeFactory.create(), varray[i], varray[varray.length - 1]);
            }

        return graph;
    }
}
