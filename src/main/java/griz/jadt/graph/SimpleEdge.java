package griz.jadt.graph;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * A basic implementation of an {@link Edge}.
 * <p>
 * A {@code SimpleEdge} represents an {@code unweighted} relationship between a vertex pair. For weighted relationships
 * use the sub-type {@link SimpleWeightedEdge}.
 * 
 * @param <V> the type of the vertices connected by the {@link SimpleEdge}
 * 
 * @author nichollsmc
 */
public class SimpleEdge<V> implements Edge<V> {

    private static final long serialVersionUID = -8203844004707822523L;

    private V source;
    private V destination;

    /**
     * Constructor for creating a {@link SimpleEdge} with the provided {@code source} (tail) and {@code destination}
     * (head) vertices.
     * 
     * @param source the {@code source} vertex
     * @param destination the {@code destination} vertex
     */
    public SimpleEdge(final V source, final V destination) {
        this.source = requireNonNull(source, "Source vertex cannot be null.");
        this.destination = requireNonNull(destination, "Destination vertex cannot be null.");
    }

    @Override
    public V source() {
        return source;
    }

    @Override
    public V destination() {
        return destination;
    }

    @Override
    public V other(final V vertex) {
        if (vertex == source) {
            return source;
        } else if (vertex == destination) {
            return destination;
        }

        throw new IllegalArgumentException("Vertex is not connected by this edge: " + vertex);
    }

    @Override
    public int hashCode() {
        return hash(source(), destination());
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }

        if (object instanceof Edge) {
            Edge<?> edge = (Edge<?>) object;

            return Objects.equals(source(), edge.source())
                    && Objects.equals(destination(), edge.destination());
        }

        return false;
    }

    @Override
    public String toString() {
        return format("%s { %s -> %s }", getClass().getSimpleName(), source(), destination());
    }
}
