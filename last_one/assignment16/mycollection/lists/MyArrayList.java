package com.shpp.p2p.cs.ldebryniuk.assignment16.mycollection.lists;

import com.shpp.p2p.cs.ldebryniuk.assignment16.mycollection.MyCollection;

/**
 * class that contains all the elements in an enhanceable array
 */
public class MyArrayList<T> implements MyList<T>, MyCollection {

    private final int MIN_ARR_SIZE = 1;
    private int arrIndexForInsertion = 0;
    private Object[] array = new Object[MIN_ARR_SIZE];

    /**
     * Adds element to the end of the array
     *
     * @param element element that will be added to the array. can be any reference type
     */
    @Override
    public void add(T element) {
        if (arrIndexForInsertion == array.length) { // true if there is no place to fit the new element
            int halfOfArrSize = (int) (Math.ceil(array.length / 2.0));
            Object[] enhancedArray = new Object[array.length + halfOfArrSize];

            // copy existed arr to the enhanced
            for (int i = 0; i < array.length; i++) {
                enhancedArray[i] = array[i];
            }
            array = enhancedArray;
        }

        array[arrIndexForInsertion] = element;
        arrIndexForInsertion++;
    }

    /**
     * Adds element at the specified position (pasteIndex) to the array
     *
     * @param pasteIndex position where new element should be inserted
     * @param element    element that will be added to the array. can be any reference type
     * @throws Exception specifies that index is out of boundaries
     */
    @Override
    public void add(int pasteIndex, T element) throws Exception {
        if (pasteIndex > arrIndexForInsertion || pasteIndex < 0) {
            throw new Exception("Index " + pasteIndex + " is out of array boundaries. Array size: " + array.length);
        } else if (pasteIndex == arrIndexForInsertion) { // add to the end of the array
            add(element);
        } else { // add to the beginning or somewhere in the middle
            Object[] enhancedArray = new Object[arrIndexForInsertion + 1];

            // copy array before pasteIndex
            for (int i = 0; i < pasteIndex; i++) {
                enhancedArray[i] = array[i];
            }

            // add element to the enhanced array
            enhancedArray[pasteIndex] = element;

            // copy array after pasteIndex
            for (int i = (pasteIndex + 1); i <= arrIndexForInsertion; i++) {
                enhancedArray[i] = array[i - 1];
            }

            array = enhancedArray;
            arrIndexForInsertion++;
        }
    }

    /**
     * deletes an element at the specified index (elIndex)
     *
     * @param elIndexToRemove index of an element to remove
     */
    @Override
    public void remove(int elIndexToRemove) {
        Object[] reducedArray = new Object[arrIndexForInsertion - 1];

        int reducedArrayIndex = 0;
        for (int i = 0; i <= reducedArray.length; i++) {
            if (i == elIndexToRemove) continue;

            reducedArray[reducedArrayIndex] = array[i];
            reducedArrayIndex++;
        }

        if (reducedArray.length == 0) { // when we remove the last element length becomes 0 (array is empty)
            reducedArray = new Object[MIN_ARR_SIZE];
        }
        array = reducedArray;

        arrIndexForInsertion--;
    }

    /**
     * retrieves an element at the specified index (elIndex)
     *
     * @param elIndex index of element to retrieve
     * @return the element at the specified index (elIndex)
     */
    @Override
    public T get(int elIndex) {
        return (T) array[elIndex];
    }

    /**
     * changes the value of an element at the specified index (elIndex)
     *
     * @param elIndex index of an element that should be changed
     * @param element new value that should be put at the specified index (elIndex)
     */
    @Override
    public void set(int elIndex, T element) {
        array[elIndex] = element;
    }

    /**
     * returns the size of the list
     *
     * @return the size of the list
     */
    @Override
    public int size() {
        return arrIndexForInsertion;
    }

    /**
     * returns false if array is not empty and true if array is empty
     *
     * @return false if array is not empty and true if array is empty
     */
    @Override
    public boolean isEmpty() {
        return arrIndexForInsertion == 0;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("[");

        if (arrIndexForInsertion > 0) {
            for (int i = 0; i < arrIndexForInsertion - 1; i++) {
                res.append(array[i]).append(", ");
            }
            res.append(array[arrIndexForInsertion - 1]); // last element is neither followed by coma nor space
        }

        res.append("]");
        return res.toString();
    }
}
