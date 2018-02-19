package es.ucm.fdi.util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A TreeMap that supports multiple values for the same key, via ArrayLists.
 *
 * Values for the same key will be returned and traversed in order of insertion;
 * that is, newer values with the same key will be stored after any other values
 * with the same key.
 */
public class MultiTreeMap<K, V> extends TreeMap<K, ArrayList<V>> {

    /**
	 * Generated UID
	 */
	private static final long serialVersionUID = -4499248827104580712L;

	public MultiTreeMap() {}

    public MultiTreeMap(Comparator<K> comparator) {
        super(comparator);
    }

    /**
     * Adds a value at the end of the list of values for the specified key.
     * @param key to add the value under
     * @param value to add
     */
    public void putValue(K key, V value) {
        if ( ! containsKey(key)) {
            put(key, new ArrayList<>());
        }
        get(key).add(value);
    }

    /**
     * Removes the first occurrence of a value from the list found at
     * a given key. Efficiency is O(size-of-that-list)
     * @param key to look into
     * @param value within the list found at that key to remove. The first
     *              element that is equals to this one will be removed.
     * @return true if removed, false if not found
     */
    public boolean removeValue(K key, V value) {
        if ( ! containsKey(key)) {
            return false;
        }
        return get(key).remove(value);
    }

    /**
     * Returns the total number of values stored in this multimap
     */
    public long sizeOfValues() {
        long total = 0;
        for (List<V> l : values()) {
            total += l.size();
        }
        return total;
    }

    /**
     * Iterates through all internal values
     * (not the arraylists themselves), first by key order,
     * and within each bucket, by insertion order.
     */
    private class InnerIterator implements Iterator<V> {

        private Iterator<ArrayList<V>> arrayIterator;
        private Iterator<V> valueIterator;
        private boolean finished = false;
        private V nextElement;

        private InnerIterator() {
            arrayIterator = values().iterator();
            advance();
        }

        private void advance() {
            if (valueIterator == null || ! valueIterator.hasNext()) {
                if (arrayIterator.hasNext()) {
                    valueIterator = arrayIterator.next().iterator();
                    if (valueIterator.hasNext()) {
                        nextElement = valueIterator.next();
                    }
                } else {
                    finished = true;
                }
            } else {
                nextElement = valueIterator.next();
            }
        }

        @Override
        public boolean hasNext() {
            return !finished;
        }

        @Override
        public V next() {
            V current = nextElement;
            advance();
            return current;
        }
    }

    /**
     * Allows iteration by base values.
     * @return iterable values, ordered by key and then by order-of-insertion
     */
    public Iterable<V> innerValues() {
        return () -> new InnerIterator();
    }
}
