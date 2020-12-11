/* *****************************************************************************
 *  File: Board.java
 *  Name:      Clyde James Felix
 *  Net ID:    cjfelix@hawaii.edu
 **************************************************************************** */


import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int[][] tiles;
    private final int dimension;

    /**
     * <p>Create a board from an n-by-n array of tiles</p>
     * <p>tiles[row][col] = tiles at (row, col)</p>
     *
     * @param tiles the 2-d array that represents the tiles
     */
    public Board(int[][] tiles) {
        this.dimension = tiles.length;

        // Defensive copy
        this.tiles = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }


    /**
     * String representation of this board
     *
     * @return a String that represents the current state of this board
     */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder(Integer.toString(dimension) + '\n');
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                out.append(String.format("%2d ", tiles[i][j]));
            }
            out.append('\n');
        }
        return out.toString();
    }

    /**
     * Get the board dimension of this board
     *
     * @return int - board dimension
     */
    public int dimension() {
        return dimension;
    }

    /**
     * <p>Hamming distance between a board and the goal board is the number of
     * tiles in the wrong position</p>
     *
     * @return int - number of tiles out of place
     */
    public int hamming() {
        int cnt = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int rightNumber = linearIndex(i, j);
                if (i == dimension - 1 && j == dimension - 1)
                    rightNumber = 0;
                if (tiles[i][j] != 0 && tiles[i][j] != rightNumber)
                    cnt++;
            }
        }
        return cnt;
    }

    // Get the linear index starts with 1 to dimension^2
    private int linearIndex(int row, int col) {
        if (row >= dimension || col >= dimension)
            throw new IllegalArgumentException("row or col greater than " +
                                                       "dimension");

        return row * dimension + col + 1;
    }

    // Get the row number of linear index starts with 1
    private int rowOf(int index) {
        if (index >= dimension * dimension)
            throw new IllegalArgumentException("Index greater than dimension");
        return (index - 1) / dimension;
    }

    private int colOf(int index) {
        if (index >= dimension * dimension)
            throw new IllegalArgumentException("Index greater than dimension");
        return index - rowOf(index) * dimension - 1;
    }

    /**
     * <p>Manhattan distance between a board and the goal board is the sum
     * of the vertical anf horizontal distances of the tiles from the
     * current positions to their goal positions</p>
     *
     * @return int - the Manhattan distances between current board and the goal
     */
    public int manhattan() {
        int dist = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int rightNumber = linearIndex(i, j);
                if (i == dimension - 1 && j == dimension - 1)
                    rightNumber = 0;

                if (tiles[i][j] != 0 && tiles[i][j] != rightNumber) {
                    int rightRow = rowOf(tiles[i][j]);
                    int rightCol = colOf(tiles[i][j]);
                    dist += Math.abs(i - rightRow) + Math.abs(j - rightCol);
                }
            }
        }
        return dist;
    }

    /**
     * Is this board the goal board
     *
     * @return boolean true if it is the goal board, false otherwise
     */
    public boolean isGoal() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int rightNumber = linearIndex(i, j);
                if (i == dimension - 1 && j == dimension - 1)
                    rightNumber = 0;

                if (tiles[i][j] != rightNumber)
                    return false;
            }
        }
        return true;
    }

    /**
     * <p>Does this board equals another object/board</p>
     * <p>Two boards are equal if they are have the same size and their
     * corresponding tiles are in the same positions</p>
     *
     * @param y Another object/board
     * @return boolean true if equals, false otherwise
     */
    @Override
    public boolean equals(Object y) {
        // Reference equality
        if (this == y) return true;

        // Check for null
        // if (y == null) throw new NullPointerException("Error: NullPointerException");
        if (y == null) return false;

        // Check belong to same class (Use getclass() instead of instanceof)
        if (this.getClass() != y.getClass()) return false;

        // Cast and check fields
        if (((Board) y).dimension != this.dimension) return false;

        return Arrays.deepEquals(this.tiles, ((Board) y).tiles);
    }

    /**
     * Get all neighboring boards, which can be reached within one move from
     * current state
     *
     * @return an iterable containing the neighbors of the board
     */
    public Iterable<Board> neighbors() {
        ArrayList<Board> neigh = new ArrayList<>();

        int i0 = 0, j0 = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tiles[i][j] == 0) {
                    i0 = i;
                    j0 = j;
                    break;
                }
            }
        }

        if (i0 - 1 >= 0) {
            // (i0 - 1, j0)
            Board that = new Board(this.tiles);
            that.slide(i0, j0, i0 - 1, j0);
            neigh.add(that);
        }

        if (j0 - 1 >= 0) {
            // (i0, j0 - 1)
            Board that = new Board(this.tiles);
            that.slide(i0, j0, i0, j0 - 1);
            neigh.add(that);
        }

        if (i0 + 1 < dimension) {
            // (i0 + 1, j0)
            Board that = new Board(this.tiles);
            that.slide(i0, j0, i0 + 1, j0);
            neigh.add(that);
        }

        if (j0 + 1 < dimension) {
            // (i0, j0 + 1)
            Board that = new Board(this.tiles);
            that.slide(i0, j0, i0, j0 + 1);
            neigh.add(that);
        }

        return neigh;
    }


    // Exchange neighbours with the 0 position
    private void slide(int i0, int j0, int i, int j) {
        if (tiles[i0][j0] != 0)
            throw new IllegalArgumentException("The given 0 position is not 0");

        tiles[i0][j0] = tiles[i][j];
        tiles[i][j] = 0;
    }

    private void swap(int i0, int j0, int i1, int j1) {
        int temp = tiles[i0][j0];
        tiles[i0][j0] = tiles[i1][j1];
        tiles[i1][j1] = temp;
    }

    /**
     * <p>Get twin board of this board</p>
     * <p>Twin board is obtained by exchanging any pair of tiles</p>
     *
     * @return a Board object - twin board
     */
    public Board twin() {
        Board twin = new Board(tiles);
        int i0 = 0, j0 = 0;
        int i1 = 0, j1 = 1;
        if (tiles[i0][j0] == 0) {
            i0 = 1;
            j0 = 1;
        }
        else if (tiles[i1][j1] == 0) {
            i1 = 1;
            j1 = 0;
        }
        twin.swap(i0, j0, i1, j1);
        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // Test 1 : hamming()
        System.out.println("hamming(): " + initial.hamming());
        // Test 2 : manhattan()
        System.out.println("manhattan(): " + initial.manhattan());
        // Test 3 : dimension()
        System.out.println("dimension(): " + initial.dimension());
        // Test 4 : toString()
        System.out.println("toString(): \n" + initial.toString());
        // Test 5 :  neighbors()
        System.out.println("neighbors(): \n" + initial.neighbors());
        // Test 7 : twin()
        System.out.println("twin(): \n" + initial.twin());
        // Test 8 : isGoal()
        System.out.println("isGoal():" + initial.isGoal());
        // Test 8 : equals()

        Board r1 = initial;
        Board r2 = initial;
        Board r3 = initial;
        System.out.println(
                "equals():symmetric: \n" + "r1 -> r2: " + r1.equals(r2) + "r2 ->r1: " + r2
                        .equals(r1));
        System.out.println("equals():symmetric: \n" + r1.equals(r1));
        System.out.println(
                "equals():transitive: \n" + "r1 -> r2: " + r1.equals(r2) + "r2 -> r3: " + r2
                        .equals(r3)
                        + "r1 -> r3" + r1.equals(r3));

        try {
            initial.equals(null);
        }
        catch (NullPointerException nullPointerException) {
            System.out.println("equals() causes an exception: NullPointerException.");
        }
    }
}
