package griz.jadt.graph;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class provides a skeletal implementation of the {@link Graph} interface.
 * <p>
 * Subclasses of this implementation will be implicitly {@code undirected}, that is, calls to {@link #isDirected()}
 * will always return false. The {@code isDirected()} method should be overridden and return a value of {@code true} to
 * be considered as a directed graph.
 * 
 * @param <V> the type of vertices contained within the {@link Graph}
 * @param <E> the type of the {@link Edge}s between vertices
 * 
 * @author nichollsmc
 * 
 * @see Graph
 */
public abstract class AbstractGraph<V, E extends Edge<V>> implements Graph<V, E> {

    private Map<V, DepthFirstMetaModel<E>> depthFirstForest;
    private Deque<V>                       topologicalOrder;
    private Deque<E>                       directedCycle;

    /**
     * Default no-arg constructor. (For invocation by subclass constructors, typically implicit.)
     */
    AbstractGraph() {}

    @Override
    public boolean hasCycle() {
        depthFirstUpdate();

        return directedCycle != null;
    }

    @Override
    public Optional<List<E>> getCycle() {
        depthFirstUpdate();

        return Optional.ofNullable(directedCycle)
                .map(LinkedList::new);
    }

    @Override
    public List<V> topologicalOrder() {
        if (!isDirected()) {
            throw new GraphException("Graph is undirected, no topological order available");
        }

        depthFirstUpdate();
        if (directedCycle == null) {
            return new LinkedList<>(topologicalOrder);
        }

        throw new GraphException(new LinkedList<>(directedCycle));
    }

    private void depthFirstUpdate() {
        if (topologicalOrder == null) {
            topologicalOrder = new ArrayDeque<>();
            depthFirstForest = new HashMap<>(numVertices());
            visitDepthFirst();
        }
    }

    private void visitDepthFirst() {
        for (V vertex : this) {
            if (!depthFirstForest.containsKey(vertex)) {
                visitDepthFirst(vertex, createDfmm(vertex, null));
            }
        }

        depthFirstForest = null;
    }

    private void visitDepthFirst(final V vertex, final DepthFirstMetaModel<E> dfmm) {
        for (final var outEdges = edgesFrom(vertex); outEdges.hasNext();) {
            final var edge = outEdges.next();
            if (edge != dfmm.edgeFromParent) {
                final var neighbor = neighbor(vertex, edge);
                if (!depthFirstForest.containsKey(neighbor)) {
                    visitDepthFirst(neighbor, createDfmm(neighbor, edge));
                } else if (directedCycle == null) {
                    checkForCycle(vertex, edge);
                }
            }
        }

        dfmm.finished();
        topologicalOrder.addFirst(vertex);
    }

    private V neighbor(final V vertex, final E edge) {
        if (edge.source() == vertex) {
            return edge.destination();
        } else {
            return edge.source();
        }
    }

    private void checkForCycle(final V source, final E edge) {
        var destination = edge.destination();
        final var dfmm = depthFirstForest.get(destination);
        if (!dfmm.isFinished()) {
            directedCycle = new ArrayDeque<>(numVertices());
            while (source != destination) {
                final var nextEdge = depthFirstForest.get(destination).edgeFromParent;
                if (nextEdge == null) {
                    break;
                }
                directedCycle.addFirst(nextEdge);
                destination = neighbor(destination, nextEdge);
            }

            directedCycle.addLast(edge);
        }
    }

    /**
     * Internal wrapper that holds the information associated with each vertex during depth-first search operations.
     * 
     * @param <E> type of the parent edge
     */
    static class DepthFirstMetaModel<E> {

        private final E edgeFromParent;
        private boolean finished;

        /**
         * Creates a vertex meta-model used during depth-first search.
         * 
         * @param edgeFromParent the parent edge of this vertex meta-model
         */
        DepthFirstMetaModel(E edgeFromParent) {
            this.edgeFromParent = edgeFromParent;
            this.finished = false;
        }

        /**
         * @return the parent edge of this vertex meta-model
         */
        public E edgeFromParent() {
            return edgeFromParent;
        }

        /**
         * @return true if this vertex meta-model has been removed from the depth-first forest
         */
        boolean isFinished() {
            return finished;
        }

        /**
         * Marks this vertex meta-model as removed from depth-first forest.
         */
        void finished() {
            finished = true;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append(this.getClass().getSimpleName())
                    .append("{edgeFromParent: ")
                    .append(edgeFromParent)
                    .append(", depth-first complete: ")
                    .append(isFinished() ? "yes" : "no")
                    .append('}')
                    .toString();
        }
    }

    /**
     * Creates a {@link DepthFirstMetaModel} for the provided vertex and parent edge.
     * 
     * @param vertex the vertex to be wrapped by a meta-model instance
     * @param edgeFromParent the parent edge of the vertex, can be {@code null}
     * @return a new {@code DepthFirstMetaModel} instance which wraps the provided vertex
     */
    private DepthFirstMetaModel<E> createDfmm(final V vertex, final E edgeFromParent) {
        final var dfmm = new DepthFirstMetaModel<>(edgeFromParent);
        depthFirstForest.put(vertex, dfmm);

        return dfmm;
    }
    
    @Override
    public String toString() {
        final var result = new StringBuilder();
        for (V vertex : this) {
            result.append(vertex).append(", outgoing edges:");
            var iterator = edgesFrom(vertex);
            if(iterator.hasNext()) {
                while (iterator.hasNext()) {
                    result.append(" ").append(iterator.next());
                }
            } else {
                result.append(" none");
            }

            result.append('\n');
        }
        
        return result.toString();
    }
}
