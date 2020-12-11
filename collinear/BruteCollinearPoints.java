/* *****************************************************************************
 *  File: Points
 *  Name: Clyde James Felix
 *  Email:cjfelix@hawaii.edu
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private LineSegment[] segments;
    private int numSegments;

    public BruteCollinearPoints(Point[] points) // finds all line segments containing 4 points
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

        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int m = k + 1; m < points.length; m++) {
                        double slopePQ = points[i].slopeTo(points[j]);
                        // StdOut.println("origin i: " + points[i].toString());
                        // StdOut.println("j: " + points[j].toString());
                        // StdOut.println("k: " + points[k].toString());
                        // StdOut.println("m: " + points[m].toString());
                        // StdOut.println("i -> j " + points[i].slopeTo(points[j]));
                        // StdOut.println("j -> k " + points[j].slopeTo(points[k]));
                        // StdOut.println("k -> m " + points[k].slopeTo(points[m]));

                        if (slopePQ == points[j].slopeTo(points[k])
                                && points[j].slopeTo(points[k]) == points[k].slopeTo(points[m])) {
                            segmentsIdx.add(new LineSegment(points[i], points[m]));
                        }
                    }
                }
            }
        }
        segments = segmentsIdx.toArray(new LineSegment[0]);
    }

    public int numberOfSegments() // the number of line segments
    {
        return segments.length;
    }

    public LineSegment[] segments() // the line segments
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        // TEST CASES

        // Test 9
        StdOut.println("Number of Segments: " + collinear.numSegments);
        // Test 10
        try {
            BruteCollinearPoints test10a = new BruteCollinearPoints(null);
        }
        catch (NullPointerException nullPointerException) {
            StdOut.println("BruteCollinearPoints() causes an exception: NullPointerException.");
        }
        Point[] testPoints = points.clone();
        testPoints[0] = null;
        try {
            BruteCollinearPoints test10b = new BruteCollinearPoints(testPoints);
        }
        catch (NullPointerException nullPointerException) {
            StdOut.println("null point(s) causes an exception: NullPointerException.");
        }

        // Test 11
        testPoints[0] = testPoints[1];
        try {
            BruteCollinearPoints test11 = new BruteCollinearPoints(testPoints);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            StdOut.println("Duplicate points");
        }
    }
}
