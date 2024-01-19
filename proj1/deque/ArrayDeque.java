package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    /**
     * Create an empty ArrayDeque
     */
    public ArrayDeque() {
        items = (T []) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }

    /**
     * Helper method. Resize the underlying Array to a new size
     * @param cap The new size of the underlying Array
     */
    private void resize(int cap) {
        T[] a = (T[]) new Object[cap];

        int oldStart = (nextFirst + 1) % items.length;
        int oldEnd = (nextLast - 1 + items.length) % items.length;
        if (oldStart < oldEnd) {
            System.arraycopy(items, oldStart, a, 0, size);
        } else {
            System.arraycopy(items, oldStart, a, 0, items.length - oldStart);
            System.arraycopy(items, 0, a, items.length - oldStart, nextLast);
        }

        items = a;
        nextFirst = items.length - 1;
        nextLast = size;
    }

    /**
     * Add an itemt to the front of the ArrayDeque.
     * @param item Item to be added
     */
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = item;
        ++size;
        if (nextFirst == 0) {
            nextFirst = items.length - 1;
        } else {
            --nextFirst;
        }
    }

    /**
     * Add an item to the end of the ArrayDeque
     * @param item Item to be added
     */
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = item;
        ++size;
        if (nextLast == items.length - 1) {
            nextLast = 0;
        } else {
            ++nextLast;
        }
    }

    /**
     * return the size of the ArrayDeque
     * @return The size of the ArrayDeque
     */
    public int size() {
        return size;
    }

    /**
     * Print the Deque.
     */
    public void printDeque() {
        int oldFirst = (nextFirst + 1) % items.length;
        int oldLast = (nextLast - 1 + items.length) % items.length;
        if (oldFirst <= oldLast) {
            for (int i = oldFirst; i <= oldLast; ++i) {
                System.out.print(items[i].toString() + " ");
            }
        } else {
            for (int i = oldFirst; i < items.length; ++i) {
                System.out.print(items[i].toString() + " ");
            }
            for (int i = 0; i < oldLast; ++i) {
                System.out.print(items[i].toString() + " ");
            }
        }
        System.out.println();
    }

    /**
     * Remove the first item of the ArrayDeque and return it.
     * @return The first item of the ArrayDeque
     */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        if (nextFirst == items.length - 1) {
            nextFirst = 0;
        } else {
            ++nextFirst;
        }

        T removed = items[nextFirst];
        items[nextFirst] = null;

        if (--size < items.length / 4 && items.length > 8) {
            resize(items.length / 2);
        }
        return removed;
    }

    /**
     * Remove the last item of the ArrayDeque and Return it.
     * @return The last item of the ArrayDeque
     */
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        if (nextLast == 0) {
            nextLast = items.length - 1;
        } else {
            --nextLast;
        }

        T removed = items[nextLast];
        items[nextLast] = null;
        if (--size < items.length / 4 && items.length > 8) {
            resize(items.length / 2);
        }
        return removed;
    }

    /**
     * Get the item from the ArrayDeque at a specific position and return it.
     * @param index The position of the item in the ArrayDeque
     * @return The item at position index
     */
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        int i = index + nextFirst + 1;

        return items[i % (items.length)];
    }

    /**
     * Iterator class for ArrayDeque
     */
    private class ArrayDequeIterator implements Iterator<T> {
        private int pos;

        ArrayDequeIterator() {
            pos = 0;
        }
        @Override
        public boolean hasNext() {
            return pos < size;
        }

        @Override
        public T next() {
            T returnItem = get(pos);
            pos += 1;
            return returnItem;
        }
    }

    /**
     * Return an iterator for ArrayDeque class
     * @return An iterator
     */
    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    /**
     * Check if 2 ArrayDeque equal to each other. They have to be in the same size,
     * and each of the item should be equal.
     * @param o Item to be compared with
     * @return True if 2 ArrayDeque are equal, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deque)) {
            return false;
        }

        Deque<T> other = (Deque<T>) o;

        if (this.size() != other.size()) {
            return false;
        }
        for (int i = 0; i < size(); ++i) {
            if (!this.get(i).equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }
}
