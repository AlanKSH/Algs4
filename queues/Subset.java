import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Subset {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> r = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            r.enqueue(StdIn.readString());
        }

        for (String x:r) {
            if (k == 0) { break; }
            StdOut.println(x);
            k--;
        }
    }
}