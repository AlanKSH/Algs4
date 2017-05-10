import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.Arrays;

public class BruteCollinearPoints {
    private Point[] a;
	private int n = 0;
	private Node last;

	private class Node {
		private LineSegment value;
		private Node prev;
	}

	public BruteCollinearPoints(Point[] points) {
		if (points == null) {
			throw new java.lang.NullPointerException();
		}
		int pl = points.length;
		a = new Point[pl];
		for (int i = 0; i < pl; i++) {
			if (points[i] == null) {
				throw new NullPointerException();
			}
			a[i] = points[i];
			for (int j = i + 1; j < pl; j++) {
				if (points[i].compareTo(points[j]) == 0) {
					throw new java.lang.IllegalArgumentException();
				}
			}
		}
		last = new Node();
		Arrays.sort(a);

		double slope1;
		double slope2;
		double slope3;

		for (int p = 0; p < pl; p++) {
			for (int q = p + 1; q < pl; q++) {
				slope1 = a[p].slopeTo(a[q]);
				for (int r = q + 1; r < pl; r++) {
					slope2 = a[p].slopeTo(a[r]);
					for (int s = r + 1; s < pl; s++) {
						slope3 = a[p].slopeTo(a[s]);
						if (slope1 == slope2 && slope1 == slope3) {
							if (last == null) {
								last.value = new LineSegment(a[p], a[s]);
							} else {
								Node newNode = new Node();
								newNode.value = new LineSegment(a[p], a[s]);
								newNode.prev = last;
								last = newNode;
							}
							n++;
						}
					}
				}
			}
		}
	} // finds all line segments containing 4 points

	public int numberOfSegments() {
		return n;
	} // the number of line segments

	public LineSegment[] segments() {
		LineSegment[] lines = new LineSegment[n];
		Node current = last;

		for (int i = 0; i < n; i++ ) {
			lines[i] = current.value;
			current = current.prev;
		}

		return lines;
	} // the line segments

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
	}
}