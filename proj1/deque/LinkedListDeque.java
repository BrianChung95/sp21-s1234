package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private class ListNode {
        private T _item;
        private ListNode _next;
        private ListNode _prev;

        public ListNode(T item, ListNode prev, ListNode next) {
            _item = item;
            _prev = prev;
            _next = next;
        }
    }

    private ListNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new ListNode(null, null, null);
        sentinel._prev = sentinel;
        sentinel._next = sentinel;
        size = 0;
    }

    /**
     * Add item to the front of the LinkedListDeque.
     * @param item The Item to be added.
     */
    public void addFirst(T item) {
        ListNode newNode = new ListNode(item, sentinel, sentinel._next);
        sentinel._next._prev = newNode;
        sentinel._next = newNode;
        ++size;
    }

    /**
     * Add item to the end of the LinkedListDeque.
     * @param item The item to be added
     */
    public void addLast(T item) {
        ListNode newNode = new ListNode(item, sentinel._prev, sentinel);
        sentinel._prev._next = newNode;
        sentinel._prev = newNode;
        ++size;
    }

    /**
     * Return the size of the LinkedListDeque.
     * @return size of the LinkedListDeque
     */
    public int size() {
        return size;
    }

    /**
     * Print out the LinkedListDeque
     */
    public void printDeque() {
        ListNode current = sentinel._next;
        while (current != sentinel) {
            System.out.print(current._item.toString() + " ");
            current = current._next;
        }
        System.out.println();
    }

    /**
     * Remove the first item from the LinkedListDeque and return it.
     * @return The first item of the LinkedListDeque
     */
    public T removeFirst() {
        if (size == 0){
            return null;
        }
        T item = sentinel._next._item;
        sentinel._next = sentinel._next._next;
        sentinel._next._prev = sentinel;
        --size;
        return item;
    }

    /**
     * Remove the last item of the LinkedListDeque and return it.
     * @return The last item of LinkedListDeque
     */
    public T removeLast() {
        if (size == 0){
            return null;
        }
        T item = sentinel._prev._item;
        sentinel._prev = sentinel._prev._prev;
        sentinel._prev._next = sentinel;
        --size;
        return item;
    }

    /**
     * Get the item at a specific position
     * @param index The position of the item
     * @return The item at index
     */

    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        ListNode current = sentinel;
        while (index >= 0) {
            current = current._next;
            --index;
        }
        return current._item;
    }

    /**
     * Helper method for getRecursive.
     * @param node A ListNode
     * @param index the index of the item
     * @return The item at index
     */
    private T getRecursive(ListNode node, int index) {
        if (index == 0) {
            return node._next._item;
        }

        return getRecursive(node._next, index - 1);
    }

    /**
     * Recursive implementation of get() method.
     * @param index the index of the item
     * @return The item at index
     */
    public T getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        }

        return getRecursive(sentinel, index);
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private int pos;
        public LinkedListDequeIterator() {
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
     * Return an iterator for LinkedListDeque
     * @return Iterator for LinkedListDeque
     */
    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    /**
     * Check if this LinkedListDeque equals the other LinkedListDeque. Return true if they have the same
     * size and each of the item at the same index is the same.
     * @param o The item to compare with.
     * @return True if equal, false if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof LinkedListDeque other){
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
        return false;
    }
}
