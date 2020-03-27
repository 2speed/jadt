package griz.jadt.graph;

import static java.lang.String.format;

import java.util.List;

/**
 * {@link GraphException} is an unchecked exception used for reporting errors during graph operations that cannot be
 * reasonably handled during <i>normal</i> program execution.
 * 
 * @author nichollsmc
 * 
 * @see java.lang.RuntimeException
 */
public class GraphException extends RuntimeException {

    private static final long serialVersionUID = -5575959386655412446L;

    private List<? extends Edge<?>> cycle;

    /**
     * Constructs a new {@code GraphException}.
     */
    public GraphException() {
        super();
    }

    /**
     * Constructs a new {@code GraphException} with the provided detail message.
     * 
     * @param message
     *            - the detail message string
     */
    GraphException(final String message) {
        super(message);
    }

    /**
     * Constructs a new {@code GraphException} with the provided
     * {@code Throwable}.
     * 
     * @param cause
     *            - the {@code Throwable}
     */
    public GraphException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code GraphException} with the provided detail message
     * and cause.
     * 
     * @param message
     *            - the detail message string
     * @param cause
     *            - the {@code Throwable} associated with the detail message
     */
    public GraphException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code GraphException} with the provided cycle.
     * 
     * @param cycle
     *            - a list that represents the cycle
     */
    GraphException(final List<? extends Edge<?>> cycle) {
        super(format("A cycle exists %s", cycle));
        this.cycle = cycle;
    }

    /**
     * @return the cycle
     */
    public List<? extends Edge<?>> getCycle() {
        return cycle;
    }
}
