package griz.jadt.graph;

import static java.util.Collections.emptyIterator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * Directed {@link Graph} implementation.
 * <p>
 * The edges between the vertices contained within an {@code DirectedGraph} can either be {@code weighted} or
 * {@code unweighted}, with at most one edge between a source (u) vertex and destination (v) vertex (a.k.a an
 * ordered pair).
 * <p>
 * Calls to the {@link #isDirected()} method will always return {@code true}.
 * 
 * @param <V> the type of vertices contained within a {@link Graph}
 * @param <E> the type of {@link Edge}s between vertices
 * 
 * @author nichollsmc
 */
public class DirectedGraph<V, E extends Edge<V>> extends AbstractGraph<V, E> implements Graph<V, E> {

    private final Set<V>       vertices = new HashSet<>();
    private final SetMap<V, E> outEdges = new SetMap<>();
    private final SetMap<V, E> inEdges  = new SetMap<>();
    
    /**
     * Constructs a new {@link DirectedGraph}.
     */
    public DirectedGraph() {
        super();
    }
    
    @Override
    public final boolean isDirected() {
        return true;
    }

    @Override
    public void addEdge(final E edge) {
        Optional.ofNullable(edge).ifPresent(e -> addEdge(e.source(), e));
    }

    private void addEdge(final V vertex, final E edge) {
        addVertex(vertex);
        addVertex(edge.destination());
        outEdges.put0(vertex, edge);
        inEdges.put0(edge.destination(), edge);
    }

    @Override
    public boolean addVertex(final V vertex) {
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
            
            return true;
        }

        return false;
    }

    @Override
    public E getEdge(final V source, final V destination) {
        if (outEdges.containsKey(source)) {
            for (final var edgesFrom = edgesFrom(source); edgesFrom.hasNext();) {
                final var nextEdge = edgesFrom.next();
                if (nextEdge.destination() == destination && nextEdge.source() == source) {
                    return nextEdge;
                }
            }
        }

        return null;
    }

    @Override
    public boolean removeEdge(final E edge) {
        if (removeEdge(edge.source(), edge)) {
            return containsVertex(edge.destination());
        }

        return false;
    }

    private boolean removeEdge(final V vertex, final E edge) {
        if (outEdges.get(vertex).remove(edge)) {
            inEdges.get(edge.destination()).remove(edge);

            return true;
        }

        return false;
    }

    @Override
    public boolean removeVertex(final V vertex) {
        if (containsVertex(vertex)) {
            for (final var edgesFrom = edgesFrom(vertex); edgesFrom.hasNext();) {
                final var edge = edgesFrom.next();
                inEdges.entrySet().stream()
                    .filter(n -> n.getValue().contains(edge))
                    .forEach(n -> inEdges.remove(n.getKey(), edge));
            }

            for (final var edgesTo = edgesTo(vertex); edgesTo.hasNext();) {
                final var edge = edgesTo.next();
                outEdges.entrySet().stream()
                        .filter(n -> n.getValue().contains(edge))
                        .forEach(n -> outEdges.remove(n.getKey(), edge));
            }

            outEdges.remove(vertex);
            inEdges.remove(vertex);
            vertices.remove(vertex);

            return true;
        }

        return false;
    }

    @Override
    public int numVertices() {
        return vertices.size();
    }

    @Override
    public boolean containsEdge(final V source, final V destination) {
        return getEdge(source, destination) != null;
    }

    @Override
    public boolean containsVertex(final V vertex) {
        return vertices.contains(vertex);
    }

    @Override
    public Iterator<E> edgesFrom(final V vertex) {
        if (outEdges.containsKey(vertex)) {
            return new ImmutableIterator<>(outEdges.get(vertex).iterator());
        }

        return emptyIterator();
    }

    @Override
    public Iterator<E> edgesTo(final V vertex) {
        if (inEdges.containsKey(vertex)) {
            return new ImmutableIterator<>(inEdges.get(vertex).iterator());
        }

        return emptyIterator();
    }

    @Override
    public Iterator<V> iterator() {
        return new ImmutableIterator<>(vertices.iterator());
    }
}
