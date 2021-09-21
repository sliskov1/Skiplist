// Starter code for Project 2: skip lists
// Do not rename the class, or change names/signatures of methods that are declared to be public.

// change to your netid
//package svl180002;

import java.util.*;

public class SkipList<T extends Comparable<? super T>> {
	static final int PossibleLevels = 33;
    Entry<T> head, tail;
    int size;
    int levels;
    Entry<T>[] pred;

    static class Entry<E> {
        E element;
        Entry<E>[] next;

        public Entry(E x, int lev) {
            element = x;
            next = new Entry[lev];
            // add more code as needed
        }

        public E getElement() {
            return element;
        }
        public int level() {
            return next.length;
        }
    }


    // Constructor
    public SkipList() {
        head = new Entry<>(null, PossibleLevels);
        tail = new Entry<>(null, PossibleLevels);
        size = 0;
        pred = new Entry[PossibleLevels];

        for (int i = 0; i < PossibleLevels; i++)
          head.next[i] = tail;
    }

    public T findHelper(T x) {
        Entry<T> p = head;
        for(int i = p.level() - 1; i >= 0; i--) {
            Entry<T> nextEntry = (Entry<T>) p.next[i];
            while(nextEntry != tail && nextEntry.element.compareTo(x) < 0) {
                p = nextEntry;
                nextEntry = nextEntry.next[i];
            }
            pred[i] = p;
        }
        return (T) p.next[0].element;
    }

    // Add x to list. If x already exists, reject it. Returns true if new node is
    // added to list
    public boolean add(T x) {
        if (contains(x)) {
            return false;
        } 
        else {
            int lvl = chooseLevel();
            Entry<T> entry = new Entry<T>(x, lvl);
            for (int i = 0; i < lvl; i++) {
                entry.next[i] = pred[i].next[i];
                pred[i].next[i] = entry;
            }
            size++;
        }
        return true;
    }

    // Find smallest element that is greater or equal to x
    public T ceiling(T x) {
        findHelper(x);
        if(pred[0].next[0] == x) {
            return x;
        }
        else {
            return pred[0].next[0].element;
        }
    }

    // Does list contain x?
    public boolean contains(T x) {
        findHelper(x);
        if(pred[0].next[0] != tail) {
            return pred[0].next[0].element.compareTo(x) == 0;
        }
        return false;
    }

    // Return first element of list
    public T first() {
        return (T) head.next[0].element;
    }

    // Find largest element that is less than or equal to x
    public T floor(T x) {
        findHelper(x);
        if(pred[0].next[0] == tail || pred[0].next[0].element.compareTo(x) != 0) {
            return pred[0].element;
        }
        else {
            return x;
        }
    }

    

    // Return element at index n of list. First element is at index 0.
    public T get(int n) {
        if(n < 0) {
            return null;
        }
        Entry<T> p = head;
        for(int k = -1; k < n; k++) {
            p = p.next[0];
        }
        return p.element;
    }

    // Is the list empty?
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        } else {
            return false;
        }
    }

    // Iterate through the elements of list in sorted order
    public Iterator  iterator() {
        return new SkipListIterator();
    }

    private class SkipListIterator<E> implements Iterator {
        private Entry<T> nextNode;

        public SkipListIterator() {
            nextNode = head.next[0];
        }

        public boolean hasNext() {
            return nextNode != tail;
        }

        public E next() {
            if (nextNode != tail) {
                E obj = (E)nextNode.element;
                nextNode = nextNode.next[0];
                return obj;
            } else {
                throw new NoSuchElementException("No next element");
            }
        }
        /** unsupported method */
      public void remove()
      {
         throw new UnsupportedOperationException
                   ("remove not supported");
      }   
    }

    // Return last element of list
    public T last() {
        if(size == 0)
            return null;
        
        Entry<T> p = head;
        for(int currentLevel = PossibleLevels - 1; currentLevel >= 0; currentLevel--) {
            while(p.next[currentLevel] != tail) {
                p = (Entry<T>) p.next[currentLevel];
            }
        }
	return p.element;
    }

    // Remove x from list.  Removed element is returned. Return null if x not in list
    public T remove(T x) {
        if(!contains(x)) {
            return null;
        }
        Entry<T> entry = pred[0].next[0];
        for(int i = 0; i < entry.level(); i++) {
            pred[i].next[i] = entry.next[i];
        }
        size--;
        return entry.element;
    }

    // Return the number of elements in the list
    public int size() {
	return size;
    }

    public int chooseLevel() {
        Random r = new Random();
        int lvl = 1 + Integer.numberOfTrailingZeros(r.nextInt());
        return Math.min(lvl, PossibleLevels);
    }
    
    
}
    

