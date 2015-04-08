

import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * Created by Barrionuevo on 11/4/14.
 */


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class GraphJung<V, E> implements UndirectedGraph<V, E> {

    private final Graph<V, E> graph;
    protected Map<V, Map<V, E>> vertices; // Map of vertices to adjacency maps of vertices to incident edges
    protected Map<E, Pair<V>> edges;    // Map of edges to incident vertex sets

    public GraphJung(final Graph<V, E> graph) {
        this.graph = graph;
        this.vertices = graph.vertices;
        this.edges = graph.edges;
    }

    private void convertEdges() {

    }

    public Graph<V, E> getBaseGraph() {
        return this.graph;
    }

    public boolean addEdge(E edge, Collection<? extends V> vertices) {
        return addEdge(edge, vertices, this.getDefaultEdgeType());
    }

    @SuppressWarnings("unchecked")
    public boolean addEdge(E edge, Collection<? extends V> vertices, EdgeType edgeType) {
        if (vertices == null)
            throw new IllegalArgumentException("'vertices' parameter must not be null");
        if (vertices.size() == 2)
            return addEdge(edge,
                    vertices instanceof Pair ? (Pair<V>) vertices : new Pair<V>(vertices),
                    edgeType);
        else if (vertices.size() == 1) {
            V vertex = vertices.iterator().next();
            return addEdge(edge, new Pair<V>(vertex, vertex), edgeType);
        } else
            throw new IllegalArgumentException("Graph objects connect 1 or 2 vertices; vertices arg has " + vertices.size());
    }

    public boolean addEdge(E e, V v1, V v2) {
        return addEdge(e, v1, v2, this.getDefaultEdgeType());
    }

    public boolean addEdge(E e, V v1, V v2, EdgeType edge_type) {
        return addEdge(e, new Pair<V>(v1, v2));
    }


    public boolean addEdge(E edge, Pair<? extends V> endpoints) {
        return addEdge(edge, endpoints);
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

    public int inDegree(V vertex) {
        return this.getInEdges(vertex).size();
    }

    public int outDegree(V vertex) {
        return this.getOutEdges(vertex).size();
    }

    public boolean isPredecessor(V v1, V v2) {
        return this.getPredecessors(v1).contains(v2);
    }

    public boolean isSuccessor(V v1, V v2) {
        return this.getSuccessors(v1).contains(v2);
    }

    public int getPredecessorCount(V vertex) {
        return this.getPredecessors(vertex).size();
    }

    public int getSuccessorCount(V vertex) {
        return this.getSuccessors(vertex).size();
    }

    public boolean isNeighbor(V v1, V v2) {
        if (!containsVertex(v1) || !containsVertex(v2))
            return false;
        return this.getNeighbors(v1).contains(v2);
    }

    public boolean isIncident(V vertex, E edge) {
        if (!containsVertex(vertex) || !containsEdge(edge))
            return false;
        return this.getIncidentEdges(vertex).contains(edge);
    }

    public int getNeighborCount(V vertex) {
        if (!containsVertex(vertex))
            throw new IllegalArgumentException(vertex + " is not a vertex in this graph");
        return this.getNeighbors(vertex).size();
    }

    public int degree(V vertex) {
        if (!containsVertex(vertex))
            throw new IllegalArgumentException(vertex + " is not a vertex in this graph");
        return this.getIncidentEdges(vertex).size();
    }

    public int getIncidentCount(E edge) {
        Pair<V> incident = this.getEndpoints(edge);
        if (incident == null)
            return 0;
        if (incident.getFirst() == incident.getSecond())
            return 1;
        else
            return 2;
    }

    public EdgeType getEdgeType(E e) {
        return null;
    }

    public EdgeType getDefaultEdgeType() {
        return null;
    }

    public Collection<E> getEdges(EdgeType edgeType) {
        return null;
    }

    public int getEdgeCount(EdgeType edgeType) {
        return 0;
    }

    public V getOpposite(V vertex, E edge) {
        Pair<V> incident = this.getEndpoints(edge);
        V first = incident.getFirst();
        V second = incident.getSecond();
        if (vertex.equals(first))
            return second;
        else if (vertex.equals(second))
            return first;
        else
            throw new IllegalArgumentException(vertex + " is not incident to " + edge + " in this graph");
    }

    public Collection<V> getIncidentVertices(E edge) {
        Pair<V> endpoints = this.getEndpoints(edge);
        Collection<V> incident = new ArrayList<V>();
        incident.add(endpoints.getFirst());
        incident.add(endpoints.getSecond());

        return Collections.unmodifiableCollection(incident);
    }

    public boolean addEdge(E edge, Pair<? extends V> endpoints, EdgeType edgeType) {
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

        vertices.get(v1).put(v2, edge);
        vertices.get(v2).put(v1, edge);

        return true;
    }

    public Collection<E> getInEdges(V vertex) {
        return this.getIncidentEdges(vertex);
    }

    public Collection<E> getOutEdges(V vertex) {
        return this.getIncidentEdges(vertex);
    }

    public Collection<V> getPredecessors(V vertex) {
        return this.getNeighbors(vertex);
    }

    public Collection<V> getSuccessors(V vertex) {
        return this.getNeighbors(vertex);
    }

    public E findEdge(V v1, V v2) {
        if (!containsVertex(v1) || !containsVertex(v2))
            return null;
        return vertices.get(v1).get(v2);
    }

    public Collection<E> findEdgeSet(V v1, V v2) {
        if (!containsVertex(v1) || !containsVertex(v2))
            return null;
        ArrayList<E> edge_collection = new ArrayList<E>(1);

        E e = findEdge(v1, v2);
        if (e == null)
            return edge_collection;
        edge_collection.add(e);
        return edge_collection;
    }

    public Pair<V> getEndpoints(E edge) {
        return edges.get(edge);
    }

    public V getSource(E directed_edge) {
        return null;
    }

    public V getDest(E directed_edge) {
        return null;
    }

    public boolean isSource(V vertex, E edge) {
        return false;
    }

    public boolean isDest(V vertex, E edge) {
        return false;
    }

    public Collection<E> getEdges() {
        return Collections.unmodifiableCollection(edges.keySet());
    }

    public Collection<V> getVertices() {
        return Collections.unmodifiableCollection(vertices.keySet());
    }

    public boolean containsVertex(V vertex) {
        return vertices.containsKey(vertex);
    }

    public boolean containsEdge(E edge) {
        return edges.containsKey(edge);
    }

    public int getEdgeCount() {
        return edges.size();
    }

    public int getVertexCount() {
        return vertices.size();
    }

    public Collection<V> getNeighbors(V vertex) {
        if (!containsVertex(vertex))
            return null;
        return Collections.unmodifiableCollection(vertices.get(vertex).keySet());
    }

    public Collection<E> getIncidentEdges(V vertex) {
        if (!containsVertex(vertex))
            return null;
        return Collections.unmodifiableCollection(vertices.get(vertex).values());
    }

    public boolean addVertex(V vertex) {
        if (vertex == null) {
            return false;
        }
        if (!containsVertex(vertex)) {
            vertices.put(vertex, new HashMap<V, E>());
            return true;
        } else {
            return false;
        }
    }

    public boolean removeVertex(V vertex) {
        if (!containsVertex(vertex))
            return false;

        for (E edge : new ArrayList<E>(vertices.get(vertex).values()))
            removeEdge(edge);

        vertices.remove(vertex);
        return true;
    }

    public boolean removeEdge(E edge) {
        if (!containsEdge(edge))
            return false;

        Pair<V> endpoints = getEndpoints(edge);
        V v1 = endpoints.getFirst();
        V v2 = endpoints.getSecond();

        // remove incident vertices from each others' adjacency maps
        vertices.get(v1).remove(v2);
        vertices.get(v2).remove(v1);

        edges.remove(edge);
        return true;
    }

}
