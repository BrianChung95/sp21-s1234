package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();

        addTimes(1000, 15, times, Ns);
        printTimingTable(Ns, times, Ns);
    }

    public static void addTimes(int start, int times, AList<Double> timesRes, AList<Integer> nRes) {
        for(int i = 0; i < times; ++i) {
            int j = 0;
            nRes.addLast(start * (int)Math.pow(2.0, i));
            AList<Integer> a = new AList<>();
            Stopwatch sw = new Stopwatch();
            while (j < start * Math.pow(2.0, i)){
                a.addLast(10);
                ++j;
            }
            timesRes.addLast(sw.elapsedTime());
        }

    }
}
