import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] openStates;
    private int n;
    private WeightedQuickUnionUF connectedStates;
    private WeightedQuickUnionUF connectedStatesBW;

    public Percolation(int n) { // create n-by-n grid, with all sites blocked
        if (n <= 0)  
            throw new java.lang.IllegalArgumentException();  
        this.n = n;
        openStates = new boolean[n*n];
        for (int i = 0; i < n * n; i++) openStates[i] = false;
        connectedStates = new WeightedQuickUnionUF(n * n + 2);
        connectedStatesBW = new WeightedQuickUnionUF(n * n + 1);
    }    

    public void open(int row, int col) {
        if ( row < 1 || row > n || col < 1 || col > n ) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int r = row - 1;
        int c = col - 1;
        if (!openStates[ r * n + c ]) {
            openStates[ r * n + c ] = true;
            if ( row == 1 ) {
                connectedStates.union(r * n + c, n * n);
                connectedStatesBW.union(r * n + c, n * n);
            }
            if ( row == n ) {
                connectedStates.union(r * n + c, n * n + 1);
            }
            if (( row > 1 ) && this.isOpen(row - 1, col)) { //up
                connectedStates.union(r * n + c, (r - 1) * n + c);
                connectedStatesBW.union(r * n + c, (r - 1) * n + c);
            }
            if (( row < n ) && this.isOpen(row + 1, col)) { //down
                connectedStates.union(r * n + c, (r + 1) * n + c);
                connectedStatesBW.union(r * n + c, (r + 1) * n + c);
            }
            
            if (( col > 1 ) && this.isOpen(row, col - 1)) { //left
                connectedStates.union(r * n + c, r * n + c - 1);
                connectedStatesBW.union(r * n + c, r * n + c - 1);
            }
            if (( col < n ) && this.isOpen(row, col + 1)) { //right
                connectedStates.union(r * n + c, r * n + c + 1);
                connectedStatesBW.union(r * n + c, r * n + c + 1);
            }
        }
    }   // open site (row, col) if it is not open already
 
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int r = row-1;
        int c = col-1;
        return openStates[r * n + c];
    }  // is site (row, col) open?
    
    public boolean isFull(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int r = row-1;
        int c = col-1;
        return connectedStatesBW.connected( r * n + c, n * n);
    }  // is site (row, col) full?
    
    public boolean percolates() {
        return connectedStates.connected(n * n, n * n + 1);
    } // does the system percolates?
    
}