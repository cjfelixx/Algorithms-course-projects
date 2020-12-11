/* *****************************************************************************
 *  File:      KdTree.java
 *  Name:      Clyde James Felix
 *  Net ID:    cjfelix@hawaii.edu
 *  Main approach:
 *      I referred to the BST algorithm from algs4 because the code will be
 *      "somewhat" the same.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private int size;
    private Node root;

    private class Node {
        private Point2D p;
        private RectHV r;
        private Node left, right;

        private Node(Point2D p, RectHV r) {
            this.p = p;
            this.r = r;
            this.left = null;
            this.right = null;
        }
    }

    public KdTree()
    // construct an empty set of points
    // Approach: Initialize the root node with size of 0.
    {
        root = null;
        size = 0;
    }

    public boolean isEmpty()
    // is the set empty?
    // Approach: Return false if the size is 0.
    {
        return size() == 0;
    }

    public int size()
    // number of points in the set
    // Approach: Return the size of the tree.
    {
        return size;
    }

    public void insert(Point2D p)
    // add the point to the set (if it is not already in the set)
    // Approach:
    //        1) Make a new function insert that recursively compares nodes argument and the nodes in the tree
    //           and inserts a Node class as child of a parent node.
    //        2) For duplicates, the algorithm overwrites the current Node with the argument. It will be the same node.
    //        3) Have a boolean flag {@code cmp} that handles if x's or y's are to be compared.
    //        4) In this function, {@code size} of tree is tracked.
    {
        //    Corner case:
        if (p == null) throw new IllegalArgumentException("calls null argument");

        root = insert(root, p, new RectHV(0, 0, 1, 1), true);
    }

    private Node insert(Node node, Point2D p, RectHV r, boolean cmp) {
        // This function is to insert node according to the rules of Kr Tree insert

        // If the function reaches an empty node, insert the node.
        if (node == null) {
            size++;
            return new Node(p, r);
        }

        // For existing points, return Node
        else if (node.p.x() == p.x() && node.p.y() == p.y()) return node;
        else if (cmp)
        // Compares x's
        {
            if (p.x() < node.p.x())
                node.left = insert(node.left, p, new RectHV(0, 0, node.p.x(), 1), !cmp);
            else node.right = insert(node.right, p, new RectHV(node.p.x(), 0, 1, 1), !cmp);

        }
        else if (!cmp)
        // Compares y's
        {
            if (p.y() < node.p.y())
                node.left = insert(node.left, p, new RectHV(0, 0, 1, node.p.y()), !cmp);
            else node.right = insert(node.right, p, new RectHV(0, node.p.y(), 1, 1), !cmp);
        }
        return node;
    }

    public boolean contains(Point2D p)
    // Does the set contain point p?
    // Approach: Traverse the tree and check if there is a node with point
    {
        return contains(root, p, true);
    }

    private boolean contains(Node node, Point2D p, boolean cmp) {
        // If the node is empty, then return false
        if (node == null) return false;

            // Return true if the nodes are in the node
        else if (node.p.x() == p.x() && node.p.y() == p.y()) return true;
        else {
            if (cmp) {
                if (p.x() < node.p.x()) return contains(node.left, p, !cmp);
                else return contains(node.right, p, !cmp);
            }
            else {
                if (p.y() < node.p.y()) return contains(node.left, p, !cmp);
                else return contains(node.right, p, !cmp);
            }
        }
    }

    public void draw()
    // Show all point to standard show
    // Approach: Iterate to all of the points available and draw point with its line
    {
        // Corner case
        draw(root, true);
    }

    private void draw(Node node, boolean line) {

        // Draw the point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.05);
        node.p.draw();

        // If {@code line} is true, then draw a vertical line, otherwise draw a horizontal line
        if (line) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.01);
            StdDraw.line(node.p.x(), node.r.ymin(), node.p.x(), node.r.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(node.r.xmin(), node.p.y(), node.r.xmax(), node.p.y());
        }

        // Recursively do this to all the nodes
        draw(node.left, !line);
        draw(node.right, !line);
    }


    public Iterable<Point2D> range(RectHV rect)
    // All points that are inside the rectangle (or on the boundary)
    // Approach: 1) Make a range function that traverses in the tree
    //           2) Store the values in a Queue
    {
        //    Corner case:
        if (rect == null) throw new IllegalArgumentException("calls null argument");
        Queue<Point2D> points = new Queue<Point2D>();
        return range(root, rect, points);
    }

    private Queue<Point2D> range(Node node, RectHV r, Queue<Point2D> points) {

        // End the recursive if no nodes are left and query r does not intersect with node's r.
        if (node == null || !r.intersects(node.r)) return points;

        // Check if points can be inside the query r. If it does, store it in the Queue.
        if (r.contains(node.p)) points.enqueue(node.p);

        // Check through all of the nodes
        range(node.left, r, points);
        range(node.right, r, points);

        return points;
    }

    public Point2D nearest(Point2D p)
    // A nearest neighbor in the set to point p; null id the set is empty
    // Approach: Make a private function nearest that traverses in the tree
    {
        //    Corner case:
        if (p == null) throw new IllegalArgumentException("calls null argument");
        if (isEmpty()) return null;

        // Make the root the closest neighbor. Then traverse through the tree using a private function
        Node nearestPoint = root;

        // return the closest points
        return nearest(root, nearestPoint, p, true).p;
    }

    private Node nearest(Node node, Node nearestNode, Point2D p, boolean cmp) {

        // Return the nearest node if there is no more node to compare to anymore.
        if (node == null) return nearestNode;

        // Determine the current closest distance with the distance in question
        double currMinDistance = p.distanceSquaredTo(nearestNode.p);
        double currDistance = p.distanceSquaredTo(node.p);

        // return the current nearest node if distance to the node's r if greater current nearest node distance
        if (node.r.distanceSquaredTo(p) >= currMinDistance) return nearestNode;

        boolean isCoordLessThan = false;
        if (cmp) {
            isCoordLessThan = p.x() < node.p.x();
        }
        else {
            isCoordLessThan = p.y() < node.p.y();
        }

        if (isCoordLessThan) {
            nearestNode = nearest(node.left, nearestNode, p, !cmp);
            nearestNode = nearest(node.right, nearestNode, p, !cmp);
        }
        else {
            nearestNode = nearest(node.right, nearestNode, p, !cmp);
            nearestNode = nearest(node.left, nearestNode, p, !cmp);
        }

        return nearestNode;
    }

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        // Test 2.1a: insert points from file; check size() and isEmpty() after each insertion
        // * input0.txt
        // * input1.txt
        // * input5.txt
        // * input10.txt
        System.out.println("Size of the tree: " + kdtree.size());
        System.out.println("isEmpty() of the tree: " + kdtree.isEmpty());

        // Test 2.1b: insert non-degenerate points; check size() and isEmpty() after each
        // * 1 random non-degenerate points in a 1-by-1 grid
        // * 5 random non-degenerate points in a 8-by-8 grid
        // * 10 random non-degenerate points in a 16-by-16 grid
        // * 50 random non-degenerate points in a 128-by-128 grid
        // * 500 random non-degenerate points in a 1024-by-1024 grid
        System.out.println("Test 2.1b");
        KdTree t1 = new KdTree();
        KdTree t2 = new KdTree();
        KdTree t3 = new KdTree();
        KdTree t4 = new KdTree();
        KdTree t5 = new KdTree();
        t1.insert(new Point2D(0.1, 0.1));

        for (int i = 0; i < 5; i++) {
            double x = Math.random() * 8;
            double y = 0;
            t2.insert(new Point2D(x / 8, y / 8));
        }

        for (int i = 0; i < 10; i++) {
            double x = Math.random() * 16;
            double y = 0.01;
            t3.insert(new Point2D(x / 16, y / 16));
        }

        for (int i = 0; i < 50; i++) {
            double x = Math.random() * 128;
            double y = 0.05;
            t4.insert(new Point2D(x / 128, y / 128));
        }

        for (int i = 0; i < 500; i++) {
            double x = Math.random() * 1024;
            double y = 0.01;
            t5.insert(new Point2D(x / 1024, y / 1024));
        }
        System.out.println("Size of the tree: " + t1.size());
        System.out.println("isEmpty() of the tree: " + t1.isEmpty());

        System.out.println("Size of the tree: " + t2.size());
        System.out.println("isEmpty() of the tree: " + t2.isEmpty());

        System.out.println("Size of the tree: " + t3.size());
        System.out.println("isEmpty() of the tree: " + t3.isEmpty());

        System.out.println("Size of the tree: " + t4.size());
        System.out.println("isEmpty() of the tree: " + t4.isEmpty());

        System.out.println("Size of the tree: " + t5.size());
        System.out.println("isEmpty() of the tree: " + t5.isEmpty());

        // Test 2.1c: insert distinct points; check size() and isEmpty() after each insertion
        // * 1 random distinct points in a 1-by-1 grid
        // * 10 random distinct points in a 8-by-8 grid
        // * 20 random distinct points in a 16-by-16 grid
        System.out.println("Test 2.1c");
        KdTree t6 = new KdTree();
        KdTree t7 = new KdTree();
        KdTree t8 = new KdTree();

        t6.insert(new Point2D(0.1, 0.1));

        for (int i = 0; i < 10; i++) {
            double x = Math.random() * 8;
            double y = Math.random() * 8;
            while (x == y) {
                y = Math.random() * 8;
            }
            t7.insert(new Point2D(x / 8, y / 8));
        }

        for (int i = 0; i < 20; i++) {
            double x = Math.random() * 16;
            double y = Math.random() * 16;
            while (x == y) {
                y = Math.random() * 16;
            }
            t8.insert(new Point2D(x / 16, y / 16));
        }


        System.out.println("Size of the tree: " + t6.size());
        System.out.println("isEmpty() of the tree: " + t6.isEmpty());

        System.out.println("Size of the tree: " + t7.size());
        System.out.println("isEmpty() of the tree: " + t7.isEmpty());

        System.out.println("Size of the tree: " + t8.size());
        System.out.println("isEmpty() of the tree: " + t8.isEmpty());


        // Test 2.1d: insert general points; check size() and isEmpty() after each insertion.
        // * 5 random general points in a 1-by-1 grid
        // * 10 random general points in a 4-by-4 grid
        // * 50 random general points in a 8-by-8 grid
        System.out.println("Test 2.1d");
        KdTree t9 = new KdTree();
        KdTree t10 = new KdTree();
        KdTree t11 = new KdTree();

        for (int i = 0; i < 5; i++) {
            double x = Math.random() * 1;
            double y = Math.random() * 1;

            t9.insert(new Point2D(x / 8, y / 8));
        }
        for (int i = 0; i < 10; i++) {
            double x = Math.random() * 4;
            double y = Math.random() * 4;

            t10.insert(new Point2D(x / 4, y / 4));
        }

        for (int i = 0; i < 50; i++) {
            double x = Math.random() * 8;
            double y = Math.random() * 8;

            t11.insert(new Point2D(x / 8, y / 8));
        }
        // Test 2.2a: insert points from file; check contains() with random query points
        // * input0.txt
        // * input1.txt
        // * input5.txt
        // * input10.txt
        System.out.println("Test 2.2a");
        double x = Math.random();
        double y = Math.random();
        System.out.println("contains() : " + kdtree.contains(new Point2D(1, 1)));
        // Test 2.2b: insert non-degenerate points; check contains() with random query points
        // * 1 random non-degenerate points in a 1-by-1 grid
        // * 5 random non -degenerate points in a 8 - by - 8 grid
        // * 10 random non -degenerate points in a 16 - by - 16 grid
        // * 20 random non -degenerate points in a 32 - by - 32 grid
        // * 500 random non -degenerate points in a 1024 - by - 1024 grid
        System.out.println("contains() : " + t1.contains(new Point2D(1, 1)));
        System.out.println("contains() : " + t2.contains(new Point2D(1, 1)));
        System.out.println("contains() : " + t3.contains(new Point2D(1, 1)));
        System.out.println("contains() : " + t4.contains(new Point2D(1, 1)));
        System.out.println("contains() : " + t5.contains(new Point2D(1, 1)));

        System.out.println("Test 2.2b");
        // Test 2.2c: insert distinct points; check contains() with random query points
        // * 1 random distinct points in a 1-by-1 grid
        // * 10 random distinct points in a 4 - by - 4 grid
        // * 20 random distinct points in a 8 - by - 8 grid
        // * 10000 random distinct points in a 128 - by - 128 grid

        KdTree t13 = new KdTree();
        KdTree t14 = new KdTree();
        for (int i = 0; i < 20; i++) {
            double x13 = Math.random() * 8;
            double y13 = Math.random() * 8;
            while (x13 == y13) {
                y13 = Math.random() * 8;
            }
            t13.insert(new Point2D(x13 / 8, y13 / 8));
        }
        for (int i = 0; i < 10000; i++) {
            double x14 = Math.random() * 128;
            double y14 = Math.random() * 128;
            while (x14 == y14) {
                y14 = Math.random() * 128;
            }
            t14.insert(new Point2D(x14 / 128, y14 / 128));
        }
        System.out.println("contains() : " + t1.contains(new Point2D(1, 1)));
        System.out.println("contains() : " + t10.contains(new Point2D(1, 1)));
        System.out.println("contains() : " + t14.contains(new Point2D(1, 1)));

        System.out.println("Test 2.2c");
        // Test 2.2d: insert general points; check contains() with random query points
        // * 10000 random general points in a 1-by-1 grid
        // * 10000 random general points in a 16 - by - 16 grid
        // * 10000 random general points in a 128 - by - 128 grid
        KdTree t15 = new KdTree();
        KdTree t16 = new KdTree();
        for (int i = 0; i < 10000; i++) {
            double x15 = Math.random() * 8;
            double y15 = Math.random() * 8;
            t15.insert(new Point2D(x15 / 8, y15 / 8));
        }
        for (int i = 0; i < 10000; i++) {
            double x16 = Math.random() * 8;
            double y16 = Math.random() * 8;
            t16.insert(new Point2D(x16 / 8, y16 / 8));
        }
        System.out.println("contains() : " + t15.contains(new Point2D(1, 1)));
        System.out.println("contains() : " + t16.contains(new Point2D(1, 1)));
        System.out.println("contains() : " + t14.contains(new Point2D(1, 1)));
        System.out.println("Test 2.2d");
        // Test 2.3a: insert points from file; check range() with random query rectangles
        // * input0.txt
        // * input1.txt * input5.txt
        // * input10.txt
        System.out.println("Test 2.3a");
        System.out.println("range() : " + kdtree.range(new RectHV(0, 0, 1, 1)));
        // Test 2.3b: insert non-degenerate points; check range() with random query rectangles
        // * 1 random non -degenerate points and random rectangles in a 2 - by - 2 grid
        // * 5 random non -degenerate points and random rectangles in a 8 - by - 8 grid
        // * 10 random non -degenerate points and random rectangles in a 16 - by - 16 grid
        // * 20 random non -degenerate points and random rectangles in a 32 - by - 32 grid
        // * 500 random non -degenerate points and random rectangles in a 1024 - by - 1024 grid
        System.out.println("Test 2.3b");
        System.out.println("range() : " + t1.range(new RectHV(0, 0, 1, 1)));
        System.out.println("range() : " + t2.range(new RectHV(0, 0, 1, 1)));
        System.out.println("range() : " + t3.range(new RectHV(0, 0, 1, 1)));
        System.out.println("range() : " + t4.range(new RectHV(0, 0, 1, 1)));
        System.out.println("range() : " + t5.range(new RectHV(0, 0, 1, 1)));

        // Test 2.3c: insert distinct points; check range() with random query rectangles
        // * 2 random distinct points and random rectangles in a 2-by-2 grid
        // * 10 random distinct points and random rectangles in a 4 - by - 4 grid
        // * 20 random distinct points and random rectangles in a 8 - by - 8 grid
        // * 100 random distinct points and random rectangles in a 16 - by - 16 grid
        // * 1000 random distinct points and random rectangles in a 64 - by - 64 grid
        KdTree t17 = new KdTree();
        KdTree t18 = new KdTree();
        KdTree t19 = new KdTree();
        KdTree t20 = new KdTree();
        KdTree t21 = new KdTree();
        for (int i = 0; i < 2; i++) {
            double x17 = Math.random() * 2;
            double y17 = Math.random() * 2;
            while (x17 == y17) {
                y17 = Math.random() * 2;
            }
            t17.insert(new Point2D(x17 / 2, y17 / 2));
        }
        for (int i = 0; i < 10; i++) {
            double x18 = Math.random() * 4;
            double y18 = Math.random() * 4;
            while (x18 == y18) {
                y18 = Math.random() * 4;
            }
            t18.insert(new Point2D(x18 / 4, y18 / 4));
        }
        for (int i = 0; i < 20; i++) {
            double x19 = Math.random() * 8;
            double y19 = Math.random() * 8;
            while (x19 == y19) {
                y19 = Math.random() * 8;
            }
            t19.insert(new Point2D(x19 / 8, y19 / 8));
        }
        for (int i = 0; i < 100; i++) {
            double x20 = Math.random() * 16;
            double y20 = Math.random() * 16;
            while (x20 == y20) {
                y20 = Math.random() * 16;
            }
            t20.insert(new Point2D(x20 / 16, y20 / 16));
        }
        for (int i = 0; i < 1000; i++) {
            double x21 = Math.random() * 64;
            double y21 = Math.random() * 64;
            while (x21 == y21) {
                y21 = Math.random() * 64;
            }
            t21.insert(new Point2D(x21 / 64, y21 / 64));
        }
        System.out.println("range() : " + t17.range(new RectHV(0, 0, 1, 1)));
        System.out.println("range() : " + t18.range(new RectHV(0, 0, 1, 1)));
        System.out.println("range() : " + t19.range(new RectHV(0, 0, 1, 1)));
        System.out.println("range() : " + t20.range(new RectHV(0, 0, 1, 1)));
        System.out.println("range() : " + t21.range(new RectHV(0, 0, 1, 1)));

        System.out.println("Test 2.3c");
        // Test 2.3d: insert general points; check range() with random query rectangles
        // * 5000 random general points and random rectangles in a 16-by-16 grid
        // * 5000 random general points and random rectangles in a 128 - by - 128 grid
        KdTree t22 = new KdTree();
        KdTree t23 = new KdTree();
        for (int i = 0; i < 5000; i++) {
            x = Math.random() * 16;
            y = Math.random() * 16;
            t22.insert(new Point2D(x / 16, y / 16));
        }
        for (int i = 0; i < 5000; i++) {
            x = Math.random() * 128;
            y = Math.random() * 128;
            t21.insert(new Point2D(x / 128, y / 128));
        }
        System.out.println("range() : " + t22.range(new RectHV(0, 0, 1, 1)));
        System.out.println("range() : " + t23.range(new RectHV(0, 0, 1, 1)));
        // System.out.println("Test 2.3d");
        // Test 2.3e: insert random points; check range() with tiny rectangles enclosing each point
        // * 5 tiny rectangles and 5 general points in a 2 - by - 2 grid
        // * 10 tiny rectangles and 10 general points in a 4 - by - 4 grid
        // * 20 tiny rectangles and 20 general points in a 8 - by - 8 grid
        // * 5000 tiny rectangles and 5000 general points in a 65536 - by - 65536 grid
        KdTree t24 = new KdTree();
        KdTree t25 = new KdTree();
        KdTree t26 = new KdTree();
        KdTree t27 = new KdTree();

        for (int i = 0; i < 5000; i++) {
            x = Math.random() * 128;
            y = Math.random() * 128;
            t21.insert(new Point2D(x / 128, y / 128));
        }

        // System.out.println("Test 2.3e");
        // Test 2.4a: insert points from file; check range() with random query rectangles and check traversal of kd-tree
        // * input5.txt
        // * input10.txt
        // System.out.println("Test 2.4a");
        // Test 2.4b: insert non-degenerate points; check range() with random query rectangles and check traversal of kd-tree
        // * 3 random non -degenerate points and 1000 random rectangles in a 4 - by - 4 grid
        // * 6 random non -degenerate points and 1000 random rectangles in a 8 - by - 8 grid
        // * 10 random non -degenerate points and 1000 random rectangles in a 16 - by - 16 grid
        // System.out.println("Test 2.4b");
        // Test 2.5a: insert points from file; check nearest() with random query points
        // * input0.txt
        // * input1.txt
        // * input5.txt
        // * input10.txt
        // System.out.println("Test 2.5a");
        // Test 2.5b: insert non-degenerate points; check nearest() with random query points
        // * 5 random non-degenerate points in a 8-by-8 grid
        // * 10 random non-degenerate points in a 16-by-16 grid
        // * 20 random non-degenerate points in a 32-by-32 grid
        // * 30 random non-degenerate points in a 64-by-64 grid
        // System.out.println("Test 2.5b");
        // Test 2.5c: insert distinct points; check nearest() with random query points * 10 random distinct points in a 4-by-4 grid
        // * 15 random distinct points in a 8 - by - 8 grid
        // * 20 random distinct points in a 16 - by - 16 grid
        // System.out.println("Test 2.5c");
        // Test 2.5d:insert general points; check nearest () with random query points
        // *10000 random general points in a 16 - by - 16 grid
        // System.out.println("Test 2.5d");
        // Test 2.6a:insert points from file; check nearest () with random query points and check traversal of kd - tree
        // * input5.txt
        // * input10.txt
        // System.out.println("Test 2.6a");
        // Test 2.6 b:insert non -degenerate points;check nearest () with random query points and check traversal of kd - tree
        // * 5 random non -degenerate points in a 8 - by - 8 grid
        // * 10 random non -degenerate points in a 16 - by - 16 grid
        // * 20 random non -degenerate points in a 32 - by - 32 grid
        // * 30 random non -degenerate points in a 64 - by - 64 grid
        // * 50 random non -degenerate points in a 128 - by - 128 grid
        // System.out.println("Test 2.6b");
        // Test 2.7:check with no points.We should see exceptions.
        // *size() and isEmpty ()
        // *contains()
        // * nearest()
        // * range()
        // System.out.println("Test 2.7");
        // Test 2.8:check that the specified exception is thrown with null arguments * argument to insert () is null
        // * argument to contains () is null
        // * argument to range () is null
        // * argument to nearest () is null
        // Test 2.9 a: check intermixed sequence of calls to insert(), isEmpty(), size(), contains(), range(), and nearest() with probabilities (p1, p2, p3, p4, p5, p6),respectively
        // *20000 calls with non - degenerate points in a 1 - by - 1 grid and probabilities(0.3, 0.05, 0.05, 0.2, 0.2, 0.2)
        // System.out.println("Test 2.9a");
        // Test 2.9 b:check intermixed sequence of calls to insert(), isEmpty(), size(), contains(), range(), and nearest() with probabilities (p1, p2, p3, p4, p5, p6),respectively
        // * 20000 calls with distinct points in a 1 - by - 1 grid
        // System.out.println("Test 2.9b");
        // Test 2.9 c:check intermixed sequence of calls to insert(), isEmpty(), size(), contains(), range(), and nearest() with probabilities (p1, p2, p3, p4, p5, p6),respectively
        // * 20000 calls with general points in a 1 - by - 1 grid and probabilities(0.3, 0.05, 0.05, 0.2, 0.2, 0.2)
        // System.out.println("Test 2.9c");
    }
}
