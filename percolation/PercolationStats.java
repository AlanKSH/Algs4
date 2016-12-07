import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private double[] ratioOfOpen;

    public PercolationStats(int n, int trials){
        if (n <= 0 || trials <= 0)  
            throw new java.lang.IllegalArgumentException();  
        int row = 0;
        int col = 0;
        ratioOfOpen = new double[trials];
        for (int i = 0; i < trials; i++) {
            int count = 0;
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()){
                row = StdRandom.uniform(n) + 1;
                col = StdRandom.uniform(n) + 1;
                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                    count++;
                }
            }
            ratioOfOpen[i] = (count + 0.0)/(n * n);
        }
    } // perform trials independent experiments on an n-by-n grid
 
    public double mean(){
        return StdStats.mean(ratioOfOpen);
    } // sample mean of percolation threshold

    public double stddev(){
        return StdStats.stddev(ratioOfOpen);
    } // sample standard deviation of percolation threshold

    public double confidenceLo(){
        return this.mean() - 1.96 * this.stddev() / Math.sqrt(ratioOfOpen.length);
    } // low endpoint of 95% confidence interval
    public double confidenceHi(){
        return this.mean() + 1.96 * this.stddev() / Math.sqrt(ratioOfOpen.length);
    } // high endpoint of 95% confidence interval

    public static void main(String[] args){
        int n = StdIn.readInt();
        int t = StdIn.readInt();
        Stopwatch timer = new Stopwatch();
        PercolationStats ps = new PercolationStats(n,t);
        double time = timer.elapsedTime();
        System.out.println("time                    = " + time);  
        System.out.println("mean                    = " + ps.mean());  
        System.out.println("stddev                  = " + ps.stddev());  
        System.out.println("95% confidence interval = " + ps.confidenceLo() + ", " + ps.confidenceHi());  
    }  // test client
}