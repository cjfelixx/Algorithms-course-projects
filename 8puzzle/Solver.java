/* *****************************************************************************
 *  File:      Solver.java
 *  Name:      Clyde James Felix
 *  Net ID:    cjfelix@hawaii.edu
 **************************************************************************** */


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class Solver {

    // Add a search node class that stores the board, moves, and the previous search node.
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private int priority;
        private SearchNode prevNode;

        public SearchNode(Board board, int moves, SearchNode prevNode) {
            this.board = board;
            this.moves = moves;
            this.prevNode = prevNode;
            priority = moves + board.manhattan();
        }

        public int compareTo(SearchNode that) {
            return (this.priority - that.priority);
        }
    }

    private MinPQ<SearchNode> pq;
    private MinPQ<SearchNode> pqTwin;
    private Board initial;
    private Board goal;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        // Corner case
        if (initial == null) throw new NullPointerException();

        this.initial = initial;
        int n = initial.dimension();
        pq = new MinPQ<SearchNode>();
        pqTwin = new MinPQ<SearchNode>();

        // Set up the goal board
        int[][] goalGrid = new int[n][n];
        int tiles = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                goalGrid[i][j] = tiles;
                tiles++;
            }
        }
        goalGrid[n - 1][n - 1] = 0;
        goal = new Board(goalGrid);
        this.initial = initial;


        // Insert the initial search board
        SearchNode node;
        SearchNode nodeTwin;

        pq.insert(new SearchNode(initial, 0, null));
        pqTwin.insert(new SearchNode(initial.twin(), 0, null));

        // Start the process of A* algorithm. Iterate until the board reaches the endgame.
        while (!pq.min().board.isGoal() && !pqTwin.min().board.isGoal()) {
            node = pq.min();
            nodeTwin = pqTwin.min();

            
            pq.delMin();
            pqTwin.delMin();

            for (Board neighbor : node.board.neighbors()) {
                if (node.moves == 0) {
                    pq.insert(new SearchNode(neighbor, node.moves + 1, node));
                }
                else if (!neighbor.equals(node.prevNode.board)) {
                    pq.insert(new SearchNode(neighbor, node.moves + 1, node));
                }
            }
            // Twin
            for (Board neighbor : nodeTwin.board.neighbors()) {
                if (nodeTwin.moves == 0) {
                    pqTwin.insert(new SearchNode(neighbor, nodeTwin.moves + 1, nodeTwin));
                }
                else if (!neighbor.equals(nodeTwin.prevNode.board)) {
                    pqTwin.insert(new SearchNode(neighbor, nodeTwin.moves + 1, nodeTwin));
                }
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        // Approach: Ensure that one of puzzle instances leads to the goal board.
        if (pq.min().board.equals(goal)) return true;
        if (pqTwin.min().board.equals(goal)) return false;
        return false;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        // Approach: Return the move it takes to reach the end goal from the A* process
        if (!isSolvable()) return -1;
        return pq.min().moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        // Apply corner case
        if (!isSolvable()) return null;

        // Use a stack tool to stack the sequences it took for the previous board to reach the end goal.
        Stack<Board> sequence = new Stack<Board>();

        SearchNode currentNode = pq.min();
        while (currentNode.prevNode != null) {
            sequence.push(currentNode.board);
            currentNode = currentNode.prevNode;
        }

        //    Since the initial board was removes from pq, push the initial board in the stack
        sequence.push(initial);
        return sequence;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        // solve the puzzle
        Stopwatch timer = new Stopwatch();
        Solver solver = new Solver(initial);
        System.out.println("Time: " + timer.elapsedTime() + "seconds: ");
        // print solution to standard output
        //    Test 1: moves() & solution()
        //    Test 5: isSolvable()
        if (!solver.isSolvable()) StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
        //    Test 10: NullPointerException()
        try {
            Solver nullSolver = new Solver(null);
        }
        catch (NullPointerException nullPointerException) {
            System.out.println("Solver() causes an exception: NullPointerException.");
        }
    }
}
