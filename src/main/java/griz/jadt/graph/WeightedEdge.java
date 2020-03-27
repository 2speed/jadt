package griz.jadt.graph;

/**
 * Defines additional behavior for an {@link Edge} between a pair of vertices that have a weighted relationship (i.e.
 * an associated real value).
 * 
 * @param <V> the type of the vertices connected by an {@link WeightedEdge}
 * 
 * @author nichollsmc
 */
public interface WeightedEdge<V> extends Comparable<WeightedEdge<V>> {

    /**
     * @return a {@code double} representing the weight of an edge
     */
    double weight();
}
