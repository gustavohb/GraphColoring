import edu.uci.ics.jung.graph.util.Pair;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Barrionuevo on 11/4/14.
 */
public class Graph<V, E> {

    // Instance Variables
    protected Map<V, Map<V, E>> vertices; // Map of vertices to adjacency maps of vertices to incident edges
    protected Map<E, Pair<V>> edges;    // Map of edges to incident vertex sets

    // Constructor
    public Graph() {
        vertices = new HashMap<V, Map<V, E>>();
        edges = new HashMap<E, Pair<V>>();
    }

    // Methods
    public Collection<E> getEdges() {
        return Collections.unmodifiableCollection(edges.keySet());
    }

    public Collection<V> getVertices() {
        return Collections.unmodifiableCollection(vertices.keySet());
    }

    public boolean addVertex(V vertex) {
        if (!containsVertex(vertex)) {
            vertices.put(vertex, new HashMap<V, E>());
            return true;
        } else {
            return false;
        }
    }

    public boolean addEdge(E e, V v1, V v2) {
        return addEdge(e, new Pair<V>(v1, v2));
    }

    public boolean addEdge(E edge, Pair<? extends V> endpoints) {
        Pair<V> new_endpoints = getValidatedEndpoints(edge, endpoints);
        if (new_endpoints == null)
            return false;

        V v1 = new_endpoints.getFirst();
        V v2 = new_endpoints.getSecond();

        if (findEdge(v1, v2) != null)
            return false;

        edges.put(edge, new_endpoints);

        if (!vertices.containsKey(v1))
            this.addVertex(v1);

        if (!vertices.containsKey(v2))
            this.addVertex(v2);

        // map v1 to <v2, edge> and vice versa
        vertices.get(v1).put(v2, edge);
        vertices.get(v2).put(v1, edge);

        return true;
    }

    public E findEdge(V v1, V v2) {
        if (!containsVertex(v1) || !containsVertex(v2))
            return null;
        return vertices.get(v1).get(v2);
    }

    protected Pair<V> getValidatedEndpoints(E edge, Pair<? extends V> endpoints) {
        if (edge == null)
            throw new IllegalArgumentException("input edge may not be null");

        if (endpoints == null)
            throw new IllegalArgumentException("endpoints may not be null");

        Pair<V> new_endpoints = new Pair<V>(endpoints.getFirst(), endpoints.getSecond());
        if (containsEdge(edge)) {
            Pair<V> existing_endpoints = getEndpoints(edge);
            if (!existing_endpoints.equals(new_endpoints)) {
                throw new IllegalArgumentException("edge " + edge +
                        " already exists in this graph with endpoints " + existing_endpoints +
                        " and cannot be added with endpoints " + endpoints);
            } else {
                return null;
            }
        }
        return new_endpoints;
    }

    public boolean containsEdge(E edge) {
        return edges.containsKey(edge);
    }


    public Pair<V> getEndpoints(E edge) {
        return edges.get(edge);
    }


    public boolean containsVertex(V vertex) {
        return vertices.containsKey(vertex);
    }

    public Collection<V> getNeighbors(V vertex) {
        if (!containsVertex(vertex))
            return null;
        return Collections.unmodifiableCollection(vertices.get(vertex).keySet());
    }


    public int getNeighborCount(V vertex) {
        if (!containsVertex(vertex))
            throw new IllegalArgumentException(vertex + " is not a vertex in this graph");
        return this.getNeighbors(vertex).size();
    }

    public boolean isNeighbor(V v1, V v2) {
        if (!containsVertex(v1) || !containsVertex(v2))
            return false;
        return this.getNeighbors(v1).contains(v2);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Vertices:\n------------\n");
        for (V v : getVertices()) {
            sb.append(v + ",");
        }

        sb.setLength(sb.length() - 1);
        sb.append("\n\nEdges:\n------------\n");
        for (E e : getEdges()) {
            Pair<V> ep = getEndpoints(e);
            sb.append(e + "[" + ep.getFirst() + "," + ep.getSecond() + "] ");
        }
        sb.append("\n");
        return sb.toString();
    }

}
