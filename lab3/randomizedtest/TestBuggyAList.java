package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  @Test
  public void testThreeAddThreeRemove() {
      AListNoResizing<Integer> correct = new AListNoResizing<>();
      BuggyAList<Integer> buggy = new BuggyAList<>();

      correct.addLast(4);
      correct.addLast(5);
      correct.addLast(6);

      buggy.addLast(4);
      buggy.addLast(5);
      buggy.addLast(6);

      assertEquals(correct.size(), buggy.size());
      assertEquals(correct.removeLast(), buggy.removeLast());
      assertEquals(correct.removeLast(), buggy.removeLast());
      assertEquals(correct.removeLast(), buggy.removeLast());
  }

  @Test
  public void randomizedTest(){
      AListNoResizing<Integer> L = new AListNoResizing<>();
      BuggyAList<Integer> B = new BuggyAList<>();

      int N = 5000;
      for (int i = 0; i < N; i += 1) {
          int operationNumber = StdRandom.uniform(0, 4);
          if (operationNumber == 0) {
              // addLast
              int randVal = StdRandom.uniform(0, 100);
              L.addLast(randVal);
              B.addLast(randVal);
          } else if (operationNumber == 1) {
              // size
              int sizeL = L.size();
              int sizeB = B.size();
              assertEquals(sizeB, sizeL);
          } else if (operationNumber == 2) {
              if (L.size() > 0) {
                  int lastL = L.removeLast();
                  int lastB = B.removeLast();
                  assertEquals(lastL, lastB);
              }
          } else if (operationNumber == 3) {
              if (L.size() > 0) {
                  int lastL = L.getLast();
                  int lastB = B.getLast();
                  assertEquals(lastL, lastB);
              }
          }
      }
  }
}
