package deque;

public class LinkedListDeque<T> {
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

    public void addFirst(T item) {
        ListNode newNode = new ListNode(item, sentinel, sentinel._next);
        sentinel._next._prev = newNode;
        sentinel._next = newNode;
        ++size;
    }

    public void addLast(T item) {
        ListNode newNode = new ListNode(item, sentinel._prev, sentinel);
        sentinel._prev._next = newNode;
        sentinel._prev = newNode;
        ++size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        ListNode current = sentinel._next;
        while (current != sentinel) {
            System.out.print(current._item.toString() + " ");
            current = current._next;
        }
        System.out.println();
    }

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

    private T getRecursive(ListNode node, int index) {
        if (index == 0) {
            return node._next._item;
        }

        return getRecursive(node._next, index - 1);
    }
    public T getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        }

        return getRecursive(sentinel, index);
    }
}
