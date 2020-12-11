/* *****************************************************************************
 * File Name: Percolation.java
 * Name: Clyde James Felix
 * NetID: cjfelix@hawaii.edu
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int totalOpen; // remember total num of open sites

    private int[][] grid;
    private int myN;
    private int empty;
    private int close;
    private WeightedQuickUnionUF set;

    public Percolation(int n)
    // creates n-by-n grid, with all sites initially blocked
    {
        if (n <= 0) throw new IllegalArgumentException("n too small");

        myN = n;
        grid = new int[myN][myN];
        empty = 1;
        close = 0;
        totalOpen = this.numberOfOpenSites();
        set = new WeightedQuickUnionUF((myN + 1) * (myN + 1));


    }

    public boolean isOpen(int row, int col)
    // is the site (row, col) open?
    {
        // StdOut.println("Row: " + Integer.toString(row) + " Col: " + Integer.toString(col)
        //                        + " N: " + Integer.toString(myN));
        if (row <= 0 || row > myN || col <= 0 || col > myN) throw new IndexOutOfBoundsException();

        if (grid[row - 1][col - 1] == empty) return true;
        return false;
    }

    public boolean isFull(int row, int col)
    // is site (row i, column j) full?
    {
        if (row <= 0 || row > myN || col <= 0 || col > myN) throw new IndexOutOfBoundsException();
        if (row == 1 && isOpen(row, col)) return true;
        for (int i = 1; i <= myN; i++) {
            if (set.find(i) == set.find(myN * (row - 1) + col) && isOpen(row, col)) {
                return true;
            }
        }
        return false;
    }

    public void open(int row, int col)
    // opens the site (row, col) if it is not open already
    {
        if (row <= 0 || row > myN || col <= 0 || col > myN) throw new IndexOutOfBoundsException();

        if (grid[row - 1][col - 1] == close) grid[row - 1][col - 1] = empty;
        totalOpen++;

        if (myN > 1) {
            if (row == 1 && col == 1) {
                if (isOpen(row, col + 1))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col + 1);
                if (isOpen(row + 1, col))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col + myN);
            }
            else if (row == 1 && col == myN) {
                if (isOpen(row, col - 1))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col - 1);
                if (isOpen(row + 1, col))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col + myN);
            }
            else if (row == myN && col == 1) {
                if (isOpen(row, col + 1))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col + 1);
                if (isOpen(row - 1, col))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col - myN);
            }
            else if (row == myN && col == myN) {
                if (isOpen(row, col - 1))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col - 1);
                if (isOpen(row - 1, col))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col - myN);
            }
            else if (row == 1 && (col > 1 || col < myN)) {
                if (isOpen(row, col + 1))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col + 1);
                if (isOpen(row, col - 1))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col - 1);
                if (isOpen(row + 1, col))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col + myN);
            }
            else if (row == myN && (col > 1 || col < myN)) {
                if (isOpen(row, col + 1))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col + 1);
                if (isOpen(row, col - 1))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col - 1);
                if (isOpen(row - 1, col))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col - myN);
            }
            else if ((row > 1 || row < myN) && col == 1) {
                if (isOpen(row, col + 1))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col + 1);
                if (isOpen(row + 1, col))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col + myN);
                if (isOpen(row - 1, col))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col - myN);
            }
            else if ((row > 1 || row < myN) && col == myN) {
                if (isOpen(row, col - 1))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col - 1);
                if (isOpen(row + 1, col))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col + myN);
                if (isOpen(row - 1, col))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col - myN);
            }
            else {
                if (isOpen(row, col + 1))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col + 1);
                if (isOpen(row, col - 1))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col - 1);
                if (isOpen(row + 1, col))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col + myN);
                if (isOpen(row - 1, col))
                    set.union(myN * (row - 1) + col, myN * (row - 1) + col - myN);
            }
        }
    }

    public int numberOfOpenSites()
    // returns the number of open sites
    {
        return totalOpen;
    }

    public boolean percolates()
    // does the system percolate?
    {
        for (int i = 1; i <= myN; i++) {
            for (int j = myN * (myN - 1) + 1; j <= myN * myN; j++) {
                if (set.find(i) == set.find(j)) return true;
            }
        }
        return false;
    }

    // test client
    public static void main(String[] args) {
        // Test Group (1): opening random sites with the following n's until it percolates
        // int[] nRandom = { 3, 5, 10, 20, 50, 250, 500, 1000, 2000 };
        int[] nRandom = { 1 };
        for (int myi = 0; myi < nRandom.length; myi++) {
            int counter = 0;
            Percolation myPerRandomSites = new Percolation(nRandom[myi]);
            while (!myPerRandomSites.percolates())
            // generate a random site (row, col) to open
            {
                int row = (int) (Math.random() * nRandom[myi] + 1);
                int col = (int) (Math.random() * nRandom[myi] + 1);
                StdOut.println("Row: " + Integer.toString(row) + " Col: " + Integer.toString(col));
                if (!myPerRandomSites.isOpen(row, col)) {
                    myPerRandomSites.open(row, col);
                    counter++;
                }
            }
            StdOut.println("n = " + nRandom[myi] + " : Percolated after opening " + counter
                                   + " random sites");
        }

        // Test Group (2): catch exceptions
        Percolation myPerException = new Percolation(10);
        int[][] invalidSites = {
                { -1, 5 }, { 11, 5 }, { 0, 5 }, { 5, -1 },
                { Integer.MIN_VALUE, Integer.MIN_VALUE }, { Integer.MAX_VALUE, Integer.MAX_VALUE }
        };

        for (int myi = 0; myi < 2 * invalidSites.length; myi = myi + 3) {
            try {
                myPerException.open(invalidSites[myi][0], invalidSites[myi][1]);
            }
            catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                System.out.println("open() causes an exception: IndexOutOfBoundsException.");
            }
            try {
                myPerException.isOpen(invalidSites[myi][0], invalidSites[myi][1]);
            }
            catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                System.out.println("isOpen() causes an exception: IndexOutOfBoundsException.");
            }
            try {
                myPerException.isFull(invalidSites[myi][0], invalidSites[myi][1]);
            }
            catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                System.out.println("isFull() causes an exception: IndexOutOfBoundsException.");
            }
        }

        // Test Group (3): invalid argument
        int[] myN = { -10, -1, 0 };
        for (int i = 0; i < myN.length; i++)
            try {
                StdOut.println("N: " + Integer.toString(myN[i]));

                Percolation p = new Percolation(myN[i]);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                System.out.println("IllegalArgumentException ");
            }
    }
}

