package com.shpp.p2p.cs.ldebryniuk.assignment16;

public class MyArrayList<T> {

    private int ARRAY_CAPACITY = 4;
    /**
     * Indicates index after the last element in array
     */
    private int indexForInsertion = 0;
    private Object[] array = new Object[ARRAY_CAPACITY];

    public void add(T element) {
        if (indexForInsertion >= ARRAY_CAPACITY) {
            ARRAY_CAPACITY += (ARRAY_CAPACITY / 2);
            Object[] enhancedArray = new Object[ARRAY_CAPACITY];
            for (int i = 0; i < array.length; i++) {
                enhancedArray[i] = array[i];
            }
            array = enhancedArray;
        }
        array[indexForInsertion] = element;
        indexForInsertion++;
    }

    public void add(int pasteIndex, T element) {
        if (pasteIndex > indexForInsertion || pasteIndex < 0) {
            System.err.printf("Index %s is out of array boundaries. List size is %d", pasteIndex, indexForInsertion);
            System.exit(1);
        } else if (pasteIndex == indexForInsertion) {
            array[indexForInsertion] = element;
        } else {
            Object[] enhancedArray = new Object[indexForInsertion + 1];
            int i;
            // copy array before pasteIndex
            for (i = 0; i < pasteIndex; i++) {
                enhancedArray[i] = array[i];
            }
            enhancedArray[pasteIndex] = element;
            // copy array after pasteIndex
            int j = 1;
            for (; i < indexForInsertion; j++) {
                enhancedArray[pasteIndex + j] = array[i];
                i++;
            }
            array = enhancedArray;
        }
        indexForInsertion++;
    }

    public void remove(int elIndexToRemove) {
        Object[] reducedArray = new Object[indexForInsertion - 1];
        int j = 0;
        for (int i = 0; i <= reducedArray.length; i++) {
            if (i == elIndexToRemove) continue;
            reducedArray[j] = array[i];
            j++;
        }
        array = reducedArray;

        indexForInsertion--;
    }

    public T get (int elIndex) {
        return (T) array[elIndex];
    }

    public void set (int elIndex, T element) {
        array[elIndex] = element;
    }

    public int size() {
        return indexForInsertion;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("[");

        if (indexForInsertion > 0) {
            for (int i = 0; i < indexForInsertion - 1; i++) {
                res.append(array[i]).append(", ");
            }
            res.append(array[indexForInsertion - 1]); // last element is neither followed by coma nor space
        }

        res.append("]");
        return res.toString();
    }
}
