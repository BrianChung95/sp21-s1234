package deque;

public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    @SuppressWarnings("unchecked")
    public ArrayDeque() {
        items = (T []) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }

    @SuppressWarnings("unchecked")
    private void resize(int cap) {
        T[] a = (T[]) new Object[cap];

        int oldStart = (nextFirst + 1) % items.length;
        int oldEnd = (nextLast - 1 + items.length) % items.length;
        if (oldStart < oldEnd){
            System.arraycopy(items, oldStart, a, 0, size);
        } else {
            System.arraycopy(items, oldStart, a, 0, items.length - oldStart);
            System.arraycopy(items, 0, a, items.length - oldStart, nextLast);
        }

        items = a;
        nextFirst = items.length - 1;
        nextLast = size;
    }

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

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int oldFirst = (nextFirst + 1) % items.length;
        int oldLast = (nextLast - 1 + items.length) % items.length;
        if (oldFirst <= oldLast) {
            for(int i = oldFirst; i <= oldLast; ++i) {
                System.out.print(items[i].toString() + " ");
            }
        } else {
            for(int i = oldFirst; i < items.length; ++i) {
                System.out.print(items[i].toString() + " ");
            }
            for(int i = 0; i < oldLast; ++i) {
                System.out.print(items[i].toString() + " ");
            }
        }
        System.out.println();
    }

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

    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        int i = index + nextFirst + 1;

        return items[i % (items.length)];
    }

    public static void main(String[] args) {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addLast(0);   // Deque: [0]
        deque.addLast(1);   // Deque: [0, 1]
        deque.addFirst(2);

        System.out.println(deque.get(2));
    }
}
