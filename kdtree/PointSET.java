/* *****************************************************************************
 *  File:      PointSET.java
 *  Name:      Clyde James Felix
 *  Net ID:    cjfelix@hawaii.edu
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> set;


    public PointSET()
    // construct an empty set of points
    // Treeset library was used because it is stated that the set will be sorted.
    {
        set = new TreeSet<Point2D>();
    }

    public boolean isEmpty()
    // is the set empty?
    // Approach: Check the length of the Treeset
    {
        if (set == null) throw new IllegalArgumentException("calls null argument");
        return set.size() == 0;
    }

    public int size()
    // number of points in the set
    // Approach: Call the size of the Treeset
    {
        if (set == null) throw new IllegalArgumentException("calls null argument");
        return set.size();
    }

    public void insert(Point2D p)
    // add the point to the set (if it is not already in the set)
    // Approach: Use the Treeset add function to add the point
    {
        if (p == null) throw new IllegalArgumentException("calls null argument");
        if (set == null) throw new IllegalArgumentException("calls null argument");
        set.add(p);
    }

    public boolean contains(Point2D p)
    // Does the set contain point p?
    // Approach: Use the Treeset contain function
    {
        if (p == null) throw new IllegalArgumentException("calls null argument");
        if (set == null) throw new IllegalArgumentException("calls null argument");
        return set.contains(p);
    }

    public void draw()
    // Show all point to standard show
    // Approach: Point2D draw() function
    {
        if (set == null) throw new IllegalArgumentException("calls null argument");
        for (Point2D points : set)
            points.draw();
    }

    public Iterable<Point2D> range(RectHV rect)
    // All [points that are inside the rectangle (or on the boundary)
    // Approach:
    //      1) initialize a data structure to store points using a queue
    //      2) Iterate through all the points.
    //      3) Add the points in the queue if they are in the rect using rect.contains()
    {
        if (rect == null) throw new IllegalArgumentException("calls null argument");
        if (set == null) throw new IllegalArgumentException("calls null argument");
        Queue<Point2D> store = new Queue<Point2D>();

        for (Point2D points : set) {
            if (rect.contains(points)) {
                store.enqueue(points);
            }
        }

        return store;
    }

    public Point2D nearest(Point2D p)
    // A nearest neighbor in the set to point p; null id the set is empty
    // Approach:
    //      1) Corner case: return null if set is empty
    //      2) Iterate through all the points.
    //      3) Check for the closest point to p.
    {
        if (set == null) throw new IllegalArgumentException("calls null argument");
        if (p == null) throw new IllegalArgumentException("calls null argument");
        if (isEmpty()) return null;

        Point2D nearestPoint = null;
        for (Point2D points : set) {
            if (nearestPoint == null || p.distanceSquaredTo(points) < p
                    .distanceSquaredTo(nearestPoint)) {
                nearestPoint = points;
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args)
    // Unit testing of the methods (optional)
    {
        // Test 1.1,1.2: insert n random points; check size(), isEmpty() and contains() after each insertion.
        // * 5 random points in a 1-by-1 grid
        // * 50 random points in a 8-by-8 grid
        // * 100 random points in a 16-by-16 grid
        // * 1000 random points in a 128-by-128 grid

        PointSET t1 = new PointSET();
        PointSET t2 = new PointSET();
        PointSET t3 = new PointSET();
        PointSET t4 = new PointSET();
        for (int i = 0; i < 10; i++) {
            double x = Math.random() * 1;
            double y = Math.random() * 1;

            t1.insert(new Point2D(x / 1, y / 1));
        }
        for (int i = 0; i < 50; i++) {
            double x = Math.random() * 8;
            double y = Math.random() * 8;

            t2.insert(new Point2D(x / 8, y / 8));
        }
        for (int i = 0; i < 100; i++) {
            double x = Math.random() * 16;
            double y = Math.random() * 16;

            t3.insert(new Point2D(x / 16, y / 16));
        }
        for (int i = 0; i < 1000; i++) {
            double x = Math.random() * 128;
            double y = Math.random() * 128;

            t4.insert(new Point2D(x / 128, y / 128));
        }

        System.out.println("t1 size() : " + t1.size());
        System.out.println("t1 is Empty() : " + t1.isEmpty());
        System.out.println("t1 contains() : " + t1.contains(new Point2D(1, 1)));
        System.out.println("t2 size() : " + t2.size());
        System.out.println("t2 is Empty() : " + t2.isEmpty());
        System.out.println("t2 contains() : " + t2.contains(new Point2D(1, 1)));
        System.out.println("t3 size() : " + t3.size());
        System.out.println("t3 is Empty() : " + t3.isEmpty());
        System.out.println("t3 contains() : " + t3.contains(new Point2D(1, 1)));
        System.out.println("t4 size() : " + t4.size());
        System.out.println("t4 is Empty() : " + t4.isEmpty());
        System.out.println("t4 contains() : " + t4.contains(new Point2D(1, 1)));


        // Test 1.2: insert n random points; check contains() with random query points
        // * 5 random points in a 1-by-1 grid
        // * 50 random points in a 4-by-4 grid
        // * 100 random points in a 8-by-8 grid
        // * 1000 random points in a 128-by-128 grid

        // Test 1.3: insert random points; check nearest() with random query points.
        //     * 10 random points in a 4-by-4 grid
        //     * 20 random points in a 16-by-16 grid * 100 random points in a 32-by-32 grid

        // Test 1.4: insert random points; check range() with random query rectangles
        //     * 2 random points and random rectangles in a 2-by-2 grid
        //     * 10 random points and random rectangles in a 4-by-4 grid
        //     * 20 random points and random rectangles in a 8-by-8 grid
        //     * 100 random points and random rectangles in a 16-by-16 grid

        // Test 1.5: call methods before inserting any points * size() and isEmpty()
        //     * contains()
        //     * nearest()
        //     * range()

        // Test 1.6: call methods with null argument * insert()
        //     * contains()
        //     * range()
        //     * nearest()

        // Test 1.7: check intermixed sequence of calls to insert(), isEmpty(), size(), contains(), range(), and nearest() with probabilities (p1, p2, p3, p4, p5, p6, p7), respectively
        //     * 10000 calls with random points in a 1-by-1 grid and probabilities (0.3, 0.1, 0.1, 0.1, 0.2, 0.2)
    }
}
