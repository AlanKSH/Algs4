import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Stack;


public class KdTree {
    private PointNode root;
    private int pointCount;

    public KdTree() 
     {
        root = null;
        pointCount = 0;
    }

    private void checkNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
    }

    public boolean isEmpty() 
     {
        return root == null;
    }

    public int size() 
     {
        return pointCount;
    }

    public void insert(Point2D p) 
     {
        checkNull(p);

        if (root == null) {
            root = new PointNode(p, null, false);
            pointCount++;
        } else {
            PointNode current = root;

            while (true) {
              if (current.value.compareTo(p) == 0) {
                return;
              }
                if (current.isAtLeftSideOfNode(p)) {
                    if (current.left == null) {
                        current.left = new PointNode(p, current, true);
                        pointCount++;
                        break;
                    } else {
                        current = current.left;
                    }
                } else {
                    if (current.right == null) {
                        current.right = new PointNode(p, current, false);
                        pointCount++;
                        break;
                    } else {
                        current = current.right;
                    }
                }
            }
        }
    }

    public boolean contains(Point2D p) 
     {
        checkNull(p);

        PointNode current = root;

        while (current != null) {
            if (current.value.compareTo(p) == 0) {
                return true;
            } else if (current.isAtLeftSideOfNode(p)) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return false;
    }

    public void draw() 
     {
        if (root != null) {
            root.draw(null, false);
        }
    }

    private void addToStack(Stack<Point2D> stack, PointNode current, RectHV rect) {
        
        if (!rect.intersects(current.rect)) {
            return;
        }

        if ((current.value.x() >= rect.xmin()) &&
                (current.value.x() <= rect.xmax()) &&
                (current.value.y() >= rect.ymin()) &&
                (current.value.y() <= rect.ymax())) {
            stack.push(current.value);
        }

        if (current.left != null) {
            addToStack(stack, current.left, rect);
        }

        if (current.right != null) {
            addToStack(stack, current.right, rect);
        }
    }

    private Point2D searchNode(Point2D toSearch, PointNode current,
        Point2D nearestPoint) {
        if (nearestPoint == null) {
            nearestPoint = current.value;
        }

        double distance = nearestPoint.distanceSquaredTo(toSearch);
        double newdistance = current.value.distanceSquaredTo(toSearch);

        if ((distance >= current.rect.distanceSquaredTo(toSearch)) ||
                (distance >= newdistance)) {
            if (distance > newdistance) {
                nearestPoint = current.value;
            }

            if ((current.isH && (toSearch.y() > current.value.y())) ||
                    (!current.isH && (toSearch.x() < current.value.x()))) {
                if (current.left != null) {
                    nearestPoint = searchNode(toSearch, current.left,
                            nearestPoint);
                }

                if (current.right != null) {
                    nearestPoint = searchNode(toSearch, current.right,
                            nearestPoint);
                }
            } else {
                if (current.right != null) {
                    nearestPoint = searchNode(toSearch, current.right,
                            nearestPoint);
                }

                if (current.left != null) {
                    nearestPoint = searchNode(toSearch, current.left,
                            nearestPoint);
                }
            }
        }

        return nearestPoint;
    }

    public Iterable<Point2D> range(RectHV rect) 
     {
        checkNull(rect);

        Stack<Point2D> stack = new Stack<Point2D>();
        if (root != null)
          addToStack(stack, root, rect);

        return stack;
    }

    public Point2D nearest(Point2D p) 
     {
        checkNull(p);

        Point2D point = null;
        if (root != null)
          point = searchNode(p, root, point);

        return point;
    }

    public static void main(String[] args) 
     {
        String filename = args[0];
        In in = new In(filename);

        KdTree kdtree = new KdTree();

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

        kdtree.draw();
    }

    private static class PointNode {
        private PointNode left;
        private PointNode right;
        private RectHV rect;
        private Point2D value;
        private boolean isH;

        public PointNode(Point2D p, PointNode parent, boolean isLeftUp) {
            value = p;
            left = null;
            right = null;
            if (parent == null) {
                isH = false;
                rect = new RectHV(0, 0, 1, 1);
            } else {
                isH = !parent.isH;

                if (isH) {
                    if (isLeftUp) {
                        rect = new RectHV(parent.rect.xmin(),
                                parent.rect.ymin(), parent.value.x(),
                                parent.rect.ymax());
                    } else {
                        rect = new RectHV(parent.value.x(), parent.rect.ymin(),
                                parent.rect.xmax(), parent.rect.ymax());
                    }
                } else {
                    if (isLeftUp) {
                        rect = new RectHV(parent.rect.xmin(), parent.value.y(),
                                parent.rect.xmax(), parent.rect.ymax());
                    } else {
                        rect = new RectHV(parent.rect.xmin(),
                                parent.rect.ymin(), parent.rect.xmax(),
                                parent.value.y());
                    }
                }
            }
        }

        public void draw(PointNode parent, boolean isLeftUp) {
            StdOut.println(isLeftUp ? "leftup" : "rightdown");
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            value.draw();
            StdDraw.setPenRadius();

            if (parent == null) {
                StdDraw.setPenColor(StdDraw.RED);
                new Point2D(value.x(), 1).drawTo(new Point2D(value.x(), 0));
            } else {
                StdOut.println(value);
                StdOut.println(parent.rect);

                if (parent.isH) {
                    StdDraw.setPenColor(StdDraw.RED);
                    new Point2D(value.x(), rect.ymin()).drawTo(new Point2D(
                            value.x(), rect.ymax()));
                } else {
                    StdDraw.setPenColor(StdDraw.BLUE);
                    new Point2D(rect.xmax(), value.y()).drawTo(new Point2D(
                            rect.xmin(), value.y()));
                }
            }

            StdDraw.pause(100);

            if (left != null) {
                left.draw(this, true);
            }

            if (right != null) {
                right.draw(this, false);
            }
        }

        public boolean isAtLeftSideOfNode(Point2D p) {
            if (isH) {
                return p.y() > value.y();
            } else {
                return p.x() < value.x();
            }
        }
    }
}