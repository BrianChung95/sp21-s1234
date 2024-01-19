package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>{
    private final Comparator<T> comparator;

    public MaxArrayDeque() {
        super();
        comparator = null;
    }

    public MaxArrayDeque(Comparator<T> c) {
        super();
        comparator = c;
    }

    public T max() {
        if (this.size() == 0) {
            return null;
        }
        T max = get(0);
        for (int i = 1; i < size(); ++i) {
            T element = get(i);
            if (this.comparator.compare(element, max) > 0) {
                max = element;
            }
        }
        return max;
    }

    public T max(Comparator<T> c) {
        if (this.size() == 0) {
            return null;
        }
        T max = get(0);
        for (int i = 1; i < size(); ++i) {
            T element = get(i);
            if (c.compare(element, max) > 0) {
                max = element;
            }
        }
        return max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LinkedListDeque<T> other = (LinkedListDeque<T>) o;

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
