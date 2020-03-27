package griz.jadt.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Extends the functionality of {@link HashMap} for performing operations on map entry values of type {@link Set}.
 * 
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of values contained by a mapped {@link Set}
 * 
 * @author nichollsmc
 */
public class SetMap<K, V> extends HashMap<K, Set<V>> implements Map<K, Set<V>> {

    private static final long serialVersionUID = -1995089577844733677L;

    public boolean put0(final K key, final V value) {
        var values = get(key);
        if (values == null) {
            values = new HashSet<>();
            put(key, values);
        }

        return values.add(value);
    }

    @Override
    public boolean remove(final Object key, final Object value) {
        var values = get(key);
        if (values != null) {
            return values.remove(value);
        }

        return false;
    }
}
