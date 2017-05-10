import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;


public class PointSET {
	private SET<Point2D> pointSet;
	// construct an empty set of points
	public PointSET() {
		pointSet = new SET<Point2D>();
	}

	// is the set empty?
	public boolean isEmpty() {
		return size() == 0;
	}

	// number of points in the set
	public int size() {
		return pointSet.size();
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (!contains(p)) pointSet.add(p);
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		return pointSet.contains(p);
	}

	// draw all points to standard draw
	public void draw() {
		for (Point2D p: pointSet) {
			p.draw();
		}
	}

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		Stack<Point2D> s = new Stack<Point2D>();
		for (Point2D p: pointSet) {
			if (p.x() >= rect.xmin() && p.x() <= rect.xmax() && p.y() >= rect.ymin() && p.y() <= rect.ymax()) {
				s.push(p);
			}
		}
		return s;
	}
	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (pointSet.isEmpty()) {
			return null;
		}
		Point2D minDisP = new Point2D(0.0, 0.0);
		Double minDis = 3.0;
		for (Point2D t: pointSet) {
			if (minDis > p.distanceTo(t)) {
				minDis = p.distanceTo(t);
				minDisP = t;
			}
		}
		return minDisP;
	}
	// unit testing of the methods
	public static void main(String[] args) {
		PointSET set = new PointSET();
		set.insert(new Point2D(0.3, 0.4));
		StdOut.println(set.size());
		StdOut.println(set.contains(new Point2D(0.3, 0.4)));
		set.insert(new Point2D(0.5, 0.5));
		StdOut.println(set.size());
		set.draw();
		Stack<Point2D> s = (Stack) set.range(new RectHV(0.4, 0.4, 0.6, 0.6));
		for (Point2D p: s) {
			StdOut.println(p.x());
			StdOut.println(p.y());
		}
		Point2D d = set.nearest(new Point2D(0.8, 0.8));
		StdOut.println(d.x());
		StdOut.println(d.y());
	}
}