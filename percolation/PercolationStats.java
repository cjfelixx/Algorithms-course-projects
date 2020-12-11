/* *****************************************************************************
 *  File Name: PercolationStats.java
 *  Name:      Clyde James Felix
 *  Net ID:    cjfelix@hawaii.edu
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

// import edu.princeton.cs.algs4.StdRandom;
// import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONF95 = 1.96;
    // your code here; define your own instance variables here
    private double[] data;
    private int T;
    private int N;

    public PercolationStats(int n, int trials)
    // perform T independent experiments on an N-by-N grid
    {
        data = new double[trials];
        T = trials;
        N = n;
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("n or trials must > 0");
        // initial you instance variable here

        int openSites = 0;

        for (int t = 1; t <= T; t++) {
            Percolation percolation = new Percolation(N);
            while (!percolation.percolates()) {
                int row = (int) (Math.random() * N) + 1;
                int col = (int) (Math.random() * N) + 1;

                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                    openSites++;
                }
            }
            data[t - 1] = (double) openSites / (N * N);
            openSites = 0;
        }
    }

    public double mean()
    // sample mean of percolation threshold
    {
        double sum = 0;
        for (int i = 0; i < data.length; i++) sum = sum + data[i];
        return sum / T;
    }

    public double stddev()
    // sample standard deviation of percolation threshold
    {
        double sum = 0;
        for (int i = 0; i < data.length; i++)
            sum = sum + Math.pow(data[i] - this.mean(), 2);
        return sum / (T - 1);
    }

    public double confidenceLo()
    // low  endpoint of 95% confidence interval
    {
        return (this.mean() - (CONF95 * this.stddev() / Math.sqrt(T)));
    }

    public double confidenceHi()
    // high endpoint of 95% confidence interval
    {
        return (this.mean() + (CONF95 * this.stddev() / Math.sqrt(T)));
    }

    // test client
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("n or trials must > 0");
        Stopwatch timer = new Stopwatch();
        PercolationStats myPS = new PercolationStats(n, trials);
        StdOut.println("mean = " + myPS.mean());
        StdOut.println("stddev = " + myPS.stddev());
        String conf = "95% confidence interval = [" + myPS.confidenceLo();
        StdOut.println(conf + ", " + myPS.confidenceHi() + "]");
        double stop = timer.elapsedTime();
        System.out.println("Time: " + stop + "seconds: ");
    }
}
