package griz.jadt.graph;

import java.util.Iterator;

/**
 * An immutable {@link Iterator}.
 * 
 * @param <E> the type to iterate over
 * 
 * @author nichollsmc
 */
class ImmutableIterator<E> implements Iterator<E> {

    private final Iterator<? extends E> iterator;

    /**
     * Default no-arg constructor.
     */
    public ImmutableIterator() {
        this(null);
    }

    /**
     * Creates a new {@link ImmutableIterator} with the provided {@link Interator} masked that will be masked from
     * clients.
     * 
     * @param iterator the internal {@code Iterator}
     */
    ImmutableIterator(Iterator<? extends E> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        if (iterator != null) {
            return iterator.hasNext();
        }

        return false;
    }

    @Override
    public E next() {
        if (iterator != null) {
            return iterator.next();
        }

        return null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Removal of elements is not supported for this iterator type");
    }
}
