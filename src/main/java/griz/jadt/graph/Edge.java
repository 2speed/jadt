package griz.jadt.graph;

import java.io.Serializable;

/**
 * Defines the behavior for an unweighted {@code edge} between a pair of vertices.
 * 
 * @param <V> the type of the vertices connected by an {@link Edge}
 * 
 * @author nichollsmc
 */
public interface Edge<V> extends Serializable {

    /**
     * @return the {@code source} (tail) vertex of an {@link Edge}
     */
    V source();

    /**
     * @return the {@code destination} (head) vertex of an {@link Edge}
     */
    V destination();

    /**
     * Returns the endpoint of the {@link Edge} that is different from the specified vertex.
     * 
     * <p>
     * If the edge represents a self-loop, the same vertex passed to this method will be returned
     * 
     * @param vertex one of the vertices of the edge (tail/source or head/destination)
     */
    V other(V vertex);
}
