import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;
    private int n;
    private int r;
    
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        n = 0;
    } // construct an empty randomized queue
     
    public boolean isEmpty() {
        return n == 0;
    } // is the queue empty?
    
    public int size() {
        return n;
    } // return the number of items on the queue
    
    private void resize(int capacity) {
        assert capacity >= 0;
        
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }
    
    public void enqueue(Item item) {
        if (n == a.length) resize(2 * a.length);
        a[n++] = item;
    } // add the item
    
    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        r = StdRandom.uniform(n);
        Item item = a[r];
        n--;

        if (r < n) {
            a[r] = a[n];
        }
        a[n] = null;
        
        if (n > 0 && n == a.length/4) resize(a.length/2);
        return item;  
    } // remove and return a random item
    
    public Item sample() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        r = StdRandom.uniform(n);
        Item item = a[r];
        return item;
    } // return (but do not remove) a random item
    
    public Iterator<Item> iterator() {
        return new RandomizedIterator();
    } // return an independent iterator over items in random order
    
    private class RandomizedIterator implements Iterator<Item> {
        private int k = 0; // the number of left items
        private Item[] temp;
        
        public RandomizedIterator() {
            temp = (Item[]) new Object[n];
            for (int i = 0; i < n; i++) {
                temp[i] = a[i];
            }
            StdRandom.shuffle(temp);
        }

        public boolean hasNext() { return k < n; }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return temp[k++];
        }
    }
    
    public static void main(String[] args) {
    } // unit testing
}