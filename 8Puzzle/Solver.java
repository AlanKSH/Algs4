import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;


public class Solver {
    private int moveNum = 0;
    private Node goal;
    
    private class Node implements Comparable<Node> {
        private Board board;
        private int moves;
        private int priority;
        private Node previous;
        
        public Node(Board board, int moves, Node previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            this.priority = this.board.manhattan() + this.moves;
        }
        
        public int compareTo(Node that) {
            if (this.priority > that.priority) return 1;
            if (this.priority < that.priority) return -1;
            return 0;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new java.lang.NullPointerException();
        }
        Board initialBoard = initial;
        Board twinBoard = initial.twin();
        MinPQ<Node> pq1 = new MinPQ<Node>();
        MinPQ<Node> pq2 = new MinPQ<Node>();
        Node currentNode1 = new Node(initialBoard, 0, null);
        Node currentNode2 = new Node(twinBoard, 0, null);
        while (!currentNode1.board.isGoal() && !currentNode2.board.isGoal()) {
            for (Board b : currentNode1.board.neighbors()) {
                if (currentNode1.previous != null) {
                    if (currentNode1.previous.board.equals(b)) continue;
                }
                Node newNode1 = new Node(b, currentNode1.moves + 1, currentNode1);
                pq1.insert(newNode1);
            }
            for (Board b : currentNode2.board.neighbors()) {
                if (currentNode2.previous != null) {
                    if (currentNode2.previous.board.equals(b)) continue;
                }
                Node newNode2 = new Node(b, currentNode2.moves + 1, currentNode2);
                pq2.insert(newNode2);
            }
            currentNode1 = pq1.delMin();
            currentNode2 = pq2.delMin();
        }
        if (currentNode1.board.isGoal()) {
            goal = currentNode1;
            moveNum = goal.moves;
        } else {
            goal = null;
            moveNum = -1;
        }
    }
    
    // is initial board solvable?
    public boolean isSolvable() {
        return goal != null;
    }
    
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moveNum;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> s = new Stack<Board>();
        for (Node node = goal; node != null; node = node.previous ) {
            s.push(node.board);
        }
        return s;
    }

    // solve a slider puzzle 
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
