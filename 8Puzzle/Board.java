import java.util.Stack;
import java.lang.Math;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Board {
    private int[][] a;
    private int n; //array length
    private int p,q; //positon where is blank 
    
    // construct a board from an n-by-n array of blocks
    public Board(int[][] blocks) {
        n = blocks.length;
        a = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = blocks[i][j];
                if (a[i][j] == 0) { p = i; q = j;}
            }
        }
    }
    
    // board dimension n
    public int dimension() {
        return n;
    }
    
    // number of blocks out of place
    public int hamming() {
        int hammingNum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i][j] == 0) continue;
                if (a[i][j] != i * n + j + 1) hammingNum++;
            }
        }
        return hammingNum;
    }
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manhattanNum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i][j] == 0) continue;
                manhattanNum += Math.abs((a[i][j] - 1) / n - i) + Math.abs((a[i][j]-1) % n - j);
            }
        }
        return manhattanNum;
    }
    // is this board the goal board?
    public boolean isGoal() {
        return (manhattan() == 0);
    }
    // a board that is obtained by exchanging any pair of blocks
    // 忘记考虑2＊2
    public Board twin() {
        int[][] b = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                b[i][j] = a[i][j];
            }
        }
        if (p == 0 && q == 0) {
            int temp = b[0][1];
            b[0][1] = b[1][0];
            b[1][0] = temp;
        } else {
            int temp = b[0][0];
            if (p == 0 && q == 1) {
                b[0][0] = b[1][0];
                b[1][0] = temp;
            } else {
                b[0][0] = b[0][1];
                b[0][1] = temp;
            }
        }
        Board twin = new Board(b);
        return twin;
    }
    // does this board equal y?
    // 没有考虑维数不同的情况
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.a[i][j] != that.a[i][j]) return false;;
            }
        }
        return true;
    }
    // all neighboring boards
    public Iterable<Board> neighbors() {
        int temp;
        Stack<Board> s = new Stack<Board>();
        if (q != 0) {
            temp = a[p][q-1];
            a[p][q-1] = a[p][q];
            a[p][q] = temp;
            s.push(new Board(a));
            temp = a[p][q-1];
            a[p][q-1] = a[p][q];
            a[p][q] = temp;
        }
        if (p != 0) {
            temp = a[p-1][q];
            a[p-1][q] = a[p][q];
            a[p][q] = temp;
            s.push(new Board(a));
            temp = a[p-1][q];
            a[p-1][q] = a[p][q];
            a[p][q] = temp;
        }
        if (q != n - 1) {
            temp = a[p][q+1];
            a[p][q+1] = a[p][q];
            a[p][q] = temp;
            s.push(new Board(a));
            temp = a[p][q+1];
            a[p][q+1] = a[p][q];
            a[p][q] = temp;
        }
        if (p != n - 1) {
            temp = a[p+1][q];
            a[p+1][q] = a[p][q];
            a[p][q] = temp;
            s.push(new Board(a));
            temp = a[p+1][q];
            a[p+1][q] = a[p][q];
            a[p][q] = temp;
        }
        return s;
    }
    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", a[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    // unit test
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        int[][] goalarray = {{1,2,3},{4,5,6},{7,8,0}};
        Board initial = new Board(blocks);
        Board goal = new Board(goalarray);
        StdOut.println(initial.equals(goal));
        StdOut.println(initial);
        StdOut.println(initial.hamming());
        StdOut.println(initial.isGoal());
        StdOut.println(initial.twin());
        for (Board b : initial.neighbors() ) {
            StdOut.println(b);
        }
    }
}