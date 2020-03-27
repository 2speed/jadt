package griz.jadt.graph;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Defines the behavior for a graph {@code G = (V,E)}, where {@code V} is a set of vertices and {@code E} is a set of
 * edges between the vertices.
 * 
 * @param <V> the type of vertices contained within a {@link Graph}
 * @param <E> the type of the {@link Edge}s between vertices
 * 
 * @author nichollsmc
 */
public interface Graph<V, E extends Edge<V>> extends Iterable<V> {

    /**
     * Returns a boolean indicating whether the graph is directed or not.
     * 
     * @return {@code true} if the graph is directed, {@code false} if not
     */
    default boolean isDirected()  {
        return false;
    }

    /**
     * Adds an edge to the graph.
     * <p>
     * If a {@code Graph} does not allow multi-edges, this method requires that there is not already an edge from the
     * source to the destination.
     * 
     * @param edge the edge to be added to the graph
     */
    void addEdge(E edge);

    /**
     * Returns an edge in the graph from the {@code source} to {@code destination} vertex.
     * 
     * @param source the source vertex
     * @param destination the destination vertex
     * @return an edge in the graph from the source to destination vertex, null if no edge exists for the vertex pair
     */
    E getEdge(V source, V destination);

    /**
     * Adds the provided vertex to the graph, returning a boolean indicating whether the vertex was added successfully
     * or not.
     * 
     * @param vertex the vertex to be added to the graph
     * @return {@code true} if the vertex was added successfully, {@code false} if not
     */
    boolean addVertex(V vertex);

    /**
     * Removes an edge from the graph, returning a boolean indicating whether the edge was removed successfully or not.
     * 
     * @param edge the edge to be removed from the graph
     * @return {@code true} if the edge was removed successfully, {@code false} if not
     */
    boolean removeEdge(E edge);

    /**
     * Removes a vertex from the graph, returning a boolean indicating whether the vertex was removed successfully or
     * not.
     * <p>
     * <b>Note: Removing a vertex implicitly removes all of its associated edges.</b>
     * 
     * @param vertex the vertex to be removed from the graph
     * @return {@code true} if the vertex was removed successfully, {@code false} if not
     */
    boolean removeVertex(V vertex);

    /**
     * Returns a boolean indicating whether an edge exists between the provided {@code source} and {@code destination}
     * vertex pair.
     * 
     * @param source the source vertex
     * @param destination the destination vertex
     * @return {@code true} if an edge exists between a source/destination vertex pair, {@code false} if not
     */
    boolean containsEdge(V source, V destination);

    /**
     * Returns a boolean indicating whether the provided vertex exists within the graph or not.
     * 
     * @param vertex the vertex to check
     * @return {@code true} if the graph contains the provided vertex, {@code false} if not
     */
    boolean containsVertex(V vertex);

    /**
     * @return the number of vertices within the graph
     */
    int numVertices();

    /**
     * Returns an iterator over the out-bound edges from the provided vertex.
     * 
     * @param vertex the vertex for the associated edges
     * @return an iterator over the outgoing edges from the provided vertex
     */
    Iterator<E> edgesFrom(V vertex);

    /**
     * Returns an iterator over the in-bound edges to the provided vertex.
     * 
     * @param vertex the vertex for the associated edges
     * @return an iterator over the outgoing edges from the provided vertex
     */
    Iterator<E> edgesTo(V vertex);

    /**
     * Returns a boolean indicating whether the graph has a cycle or not.
     * 
     * @return {@code true} if the graph has a cycle, {@code false} if no cycle exists
     */
    boolean hasCycle();

    /**
     * Returns an {@link Optional} that contains the list of edges within a cycle of the graph, in the order they appear
     * in the cycle.
     * <p>
     * The {@link #hasCycle()} method can be used to determine if the graph contains a cycle.
     * 
     * @return an {@link Optional} that contains the list of edges within a cycle of the graph
     */
    Optional<List<E>> getCycle();

    /**
     * Returns a list of the vertices of a directed graph in topological (edge directed) order.
     * <p>
     * A {@link GraphException} will be thrown when a graph is undirected, or if there is a directed cycle.
     * 
     * @return a list of the vertices contained within a directed graph in topological order
     * @throws GraphException if a cycle is detected while calculating the topological order of the graph
     */
    List<V> topologicalOrder() throws GraphException;
}
