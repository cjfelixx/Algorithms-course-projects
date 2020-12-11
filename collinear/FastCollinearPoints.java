/* *****************************************************************************
 *  File:FastCollinearPoints
 *  Name: Clyde James Felix
 *  Email: cjfelix@hawaii.edu
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class FastCollinearPoints {

    private LineSegment[] segments;
    private int numSegments;

    public FastCollinearPoints(Point[] points)
    // finds all line segments containing 4 or more points
    {

        if (points == null) throw new NullPointerException();
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new NullPointerException();
        }
        // Checking Duplicates
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0)
                throw new IllegalArgumentException("Duplicate found");
        }
        ArrayList<LineSegment> segmentsIdx = new ArrayList<LineSegment>();
        Arrays.sort(points);

        for (int i = 0; i < points.length; i++) {

            Point p = points[i];
            Arrays.sort(points, p.slopeOrder());
            int q = 1;
            while (q < points.length) {
                LinkedList<Point> pointList = new LinkedList<>();
                do {
                    pointList.add(points[q++]);
                } while (q < points.length && p.slopeTo(points[q]) == p.slopeTo(points[q]));

                if (pointList.size() >= 3 && p.compareTo(pointList.peek()) == -1) {
                    segmentsIdx.add(new LineSegment(p, pointList.removeLast()));
                }
            }
        }
        segments = segmentsIdx.toArray(new LineSegment[0]);

    }

    public int numberOfSegments()
    // the number of line segments
    {
        return segments.length;
    }

    public LineSegment[] segments()
    // the line segments
    {
        return segments;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }

        StdDraw.show();

        // TEST Cases
        // Test 18
        System.out.print(collinear.numberOfSegments() + " segments\n");

        // Test 19
        try {
            FastCollinearPoints test19a = new FastCollinearPoints(null);
        }
        catch (NullPointerException nullPointerException) {
            StdOut.println("BruteCollinearPoints() causes an exception: NullPointerException.");
        }
        Point[] testPoints = points.clone();
        testPoints[0] = null;
        try {
            FastCollinearPoints test19b = new FastCollinearPoints(testPoints);
        }
        catch (NullPointerException nullPointerException) {
            StdOut.println("null point(s) causes an exception: NullPointerException.");
        }
        // Test 20
        testPoints[0] = testPoints[1];
        try {
            FastCollinearPoints test20 = new FastCollinearPoints(testPoints);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            StdOut.println("Duplicate points");
        }
    }
}
