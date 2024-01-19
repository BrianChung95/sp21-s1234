package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MaxArrayDequeTest {
    public static class valueComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    }

    @Test
    public void addIsEmptySizeTest() {
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(new valueComparator());
        for (int i = 0; i < 500; ++i) {
            mad.addLast(i);
        }
        for (int i = 500; i > 0; --i) {
            mad.addFirst(i);
        }
        assertEquals((Integer)500, mad.max());
    }


    @Test
    public void addIsEmptySizeTest2() {
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>();
        for (int i = 0; i < 500; ++i) {
            mad.addLast(i);
        }
        for (int i = 1500; i > 0; --i) {
            mad.addFirst(i);
        }
        assertEquals((Integer)1500, mad.max(new valueComparator()));
    }
}
