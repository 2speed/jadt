package griz.jadt.graph;

import static java.lang.Double.isNaN;
import static java.lang.String.format;
import static java.util.Objects.hash;

/**
 * A basic implementation of a {@link WeightedEdge}.
 * 
 * @param <V> the type of the vertices connected by a {@link SimpleWeightedEdge}
 * 
 * @author nichollsmc
 */
public class SimpleWeightedEdge<V> extends SimpleEdge<V> implements WeightedEdge<V>, Edge<V> {

    private static final long serialVersionUID = -7673712533715893212L;

    private double weight;

    /**
     * Constructor for creating a {@link SimpleWeightedEdge} with the provided {@code source} (tail) and
     * {@code destination} (head) vertices, and the associated weight.
     * 
     * @param source the {@code source} vertex
     * @param destination the {@code destination} vertex
     * @param weight the weighted value
     */
    public SimpleWeightedEdge(final V source, final V destination, final double weight) {
        super(source, destination);
        if (isNaN(weight)) {
            throw new IllegalArgumentException("Weight is not a number.");
        }
        this.weight = weight;
    }

    @Override
    public double weight() {
        return weight;
    }

    @Override
    public int compareTo(final WeightedEdge<V> weightedEdge) {
        if (weight < weightedEdge.weight()) {
            return -1;
        } else if (weight > weightedEdge.weight()) {
            return 1;
        }

        return 0;
    }

    @Override
    public int hashCode() {
        return hash(super.hashCode(), weight());
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }

        if (object instanceof WeightedEdge) {
            WeightedEdge<?> weightedEdge = (WeightedEdge<?>) object;

            return super.equals(weightedEdge) && weight() == weightedEdge.weight();
        }

        return false;
    }

    @Override
    public String toString() {
        return format(super.toString() + " @ %.5f", weight());
    }
}
