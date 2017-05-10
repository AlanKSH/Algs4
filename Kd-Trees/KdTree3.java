import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
	private Node root;

	private static class Node {
		private Point2D p;           // sorted by key
        private RectHV rect;
        private int size;
        private Node lb; 
        private Node rt;  // left and right subtrees

        public Node(Point2D p, int size, RectHV rect) {
            this.p = p;
            this.size = size;
            this.rect = rect;
        }
	}

	// construct an empty set of points
	public KdTree() {
	}

	// is the set empty?
	public boolean isEmpty() {
		return size() == 0;
	}

	// number of points in the set
	public int size() {
		return size(root);
	}

	private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null) {
            throw new java.lang.NullPointerException();
        }
        if (contains(p)) return;
		root = put(root, p, true);
	}

	private Node put(Node x, Point2D p, boolean isEven) {
		if (x == null) {
			if (isEven) return new Node(p, 1, new RectHV(0, 0, p.x(), 1));
			else        return new Node(p, 1, new RectHV(0, 0, 1, p.y()));
		}
		if (isEven) {
			if   (p.x() < x.p.x())  x.lb = put(x.lb, p, !isEven);
			else 					x.rt = put(x.rt, p, !isEven);
		} else {
			if   (p.y() < x.p.y())  x.lb = put(x.lb, p, !isEven);
			else 					x.rt = put(x.rt, p, !isEven);
		}
		x.size = size(x.lb) + size(x.rt) + 1;
		return x;
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null) {
            throw new java.lang.NullPointerException();
        }
		return search(root, p, true);
	}

	private boolean search(Node x, Point2D p, boolean isEven) {
		if (x == null) return false;
		if (isEven) {
			if      (p.compareTo(x.p) == 0) return true;
			else if (p.x() < x.p.x())  		return search(x.lb, p, !isEven);
			else                       		return search(x.rt, p, !isEven);
		} else {
			if      (p.compareTo(x.p) == 0) return true;
			else if (p.y() < x.p.y())  		return search(x.lb, p, !isEven);
			else                       		return search(x.rt, p, !isEven);
		}
	} 

	// draw all points to standard draw
	public void draw() {
		draw(root, true, true, null);
	}

	private void draw(Node x, boolean isEven, boolean isLeft, Point2D fP) {
		StdDraw.setPenRadius(0.01);
		StdDraw.setPenColor(StdDraw.BLACK);
		x.p.draw();
		StdDraw.setPenRadius();
		Point2D formerPoint = fP;
		if (formerPoint == null) formerPoint = new Point2D(0.0, 1.0);
		if (isEven) {
			StdDraw.setPenColor(StdDraw.RED);
			if (isLeft) StdDraw.line(x.p.x(), 0.0, x.p.x(), formerPoint.y());
			else        StdDraw.line(x.p.x(), formerPoint.y(), x.p.x(), 1.0);
		} else {
			StdDraw.setPenColor(StdDraw.BLUE);
			if (isLeft) StdDraw.line(0.0, x.p.y(), formerPoint.x(), x.p.y());
			else        StdDraw.line(formerPoint.x(), x.p.y(), 1.0, x.p.y());
		}
		if (x.lb != null) draw(x.lb, !isEven, true, x.p);
		if (x.rt != null) draw(x.rt, !isEven, false, x.p);
	}

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) {
            throw new java.lang.NullPointerException();
        }
		Stack<Point2D> s = new Stack<Point2D>();
		range(root, rect, s);
		return s;
	}

	private void range(Node x, RectHV rect, Stack<Point2D> s) {
		if (rect.intersects(x.rect)) {
			if (x.p.x() >= rect.xmin() && x.p.x() <= rect.xmax() && x.p.y() >= rect.ymin() && x.p.y() <= rect.ymax()) {
				s.push(x.p);
			}
		}
		if (x.lb != null) range(x.lb, rect, s);
		if (x.rt != null) range(x.rt, rect, s);
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (p == null) {
            throw new java.lang.NullPointerException();
        }
		Point2D nearestPoint = new Point2D(2.0, 2.0);
		nearestPoint = nearestSearch(root, p, nearestPoint);
		return nearestPoint;
	}

	private Point2D nearestSearch(Node x, Point2D p, Point2D nearestPoint) {
		if (p.distanceTo(x.p) < p.distanceTo(nearestPoint)) {
			nearestPoint = x.p;
		}
		if (x.lb != null) {
			if (x.lb.rect.distanceTo(p) <= p.distanceTo(nearestPoint)) {
				nearestPoint = nearestSearch(x.lb, p, nearestPoint);
				if (x.rt != null) nearestPoint = nearestSearch(x.rt, p, nearestPoint);
			} else if (x.rt != null) {
				nearestPoint = nearestSearch(x.rt, p, nearestPoint);
				nearestPoint = nearestSearch(x.lb, p, nearestPoint);
			}
		} else {
			if (x.rt != null) {
				nearestPoint = nearestSearch(x.rt, p, nearestPoint);
			}
		}
		return nearestPoint;
	}

	// unit testing of the methods
	public static void main(String[] args) {
		KdTree kdTree = new KdTree();
		kdTree.insert(new Point2D(0.3, 0.4));
		kdTree.insert(new Point2D(0.5, 0.5));
		kdTree.insert(new Point2D(0.7, 0.8));
		kdTree.insert(new Point2D(0.9, 0.9));
		StdOut.println(kdTree.size());
		StdOut.println(kdTree.contains(new Point2D(0.5, 0.5)));
		kdTree.draw();
		StdOut.println(kdTree.nearest(new Point2D(0.9, 0.9)));
		Stack<Point2D> s = (Stack) kdTree.range(new RectHV(0.1, 0.1, 0.6, 0.6));
		for (Point2D p: s) {
			StdOut.println(p);
			// StdOut.println(p.y());
		}
	}
}