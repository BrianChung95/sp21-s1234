package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        AList<Integer> Ns = new AList<>();
        AList<Integer> ops = new AList<>();
        AList<Double> times = new AList<>();

        addTimesSl(1000, 8, times, Ns, ops);
        printTimingTable(Ns, times, ops);
    }

    public static void addTimesSl(int start, int times, AList<Double> timesRes, AList<Integer> nRes, AList<Integer> ops) {
        for(int i = 0; i < times; ++i) {
            ops.addLast(10000);
            int j = 0;
            nRes.addLast(start * (int)Math.pow(2.0, i));
            SLList<Integer> a = new SLList<>();

            while (j < start * Math.pow(2.0, i)){
                a.addLast(10);
                ++j;
            }
            Stopwatch sw = new Stopwatch();
            int k = 0;
            while (k < 10000) {
                a.getLast();
                ++k;
            }

            timesRes.addLast(sw.elapsedTime());
        }

    }
}
