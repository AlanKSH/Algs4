import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private Node first = null;
    private Node last = null;
    private int n = 0;
    
    private class Node {
        private Item item;
        private Node before;
        private Node next;
    } 
    
    // construct an empty deque
    public Deque() { 
        first = new Node();
        last = new Node();
        first = last;
    }
    
    // is the deque empty?
    public boolean isEmpty() { return (n == 0); }
    
    // return the number of tiems on the deque
    public int size() { return n; }
    
    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException();
        }
        if (isEmpty()) {
            first.item = item;
        } else {
            Node oldFirst = first;
            first = new Node();
            first.item = item;
            first.next = oldFirst;
            first.before = null;
            oldFirst.before = first;
        }
        n++;
    }
    
    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException();
        }
        if (isEmpty()) {
            last.item = item;
        } else {
            Node oldLast = last;
            last = new Node();
            last.item = item;
            last.before = oldLast;
            oldLast.next = last;
            last.next = null;
        }
        n++;
    }
    
    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Item item = first.item;
        if (n == 1) {
            first.item = null;
        } else {
            Node oldFirst = first;
            first = first.next;
            oldFirst.next = null;
            first.before = null;
        }
        n--;
        return item;
    }
    
    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Item item = last.item;
        if (n == 1) {
            last.item = null;
        } else {
            Node oldLast = last;
            last = last.before;
            oldLast.before = null;
            last.next = null;
        }
        n--;
        return item;
    }
    
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new FrontToEndIterator();
    }
    
    private class FrontToEndIterator implements Iterator<Item> {
        private Node current = first;
        
        public boolean hasNext() { return current != null; }
        
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
        
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    
    
    public static void main(String[] args) {
    }
}