/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consensus;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 *
 * @author Kevin
 */
public class Layout {
    
    /**
     * Each heading of the layout will be separately stored in a one-dimensional ArrayList.
     * Every heading in the ArrayList is a String object.
     * The native order will not be changed.
     * @since Release (1st July 2018)
     */
    private final ArrayList<String> headings;
    /**
     * The count of columns at the layout, represented by a Integer.
     * @since Release (1st July 2018)
     */
    private final int columnCount;
    
    /**
     * <b>Constructor</b> <p>
     * > Set up the layout as a one-dimensional ArrayList. <p>
     * > Set up the number of columns. <p>
     *
     * @param layout one-dimensional String ArrayList
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index > size())
     * @throws NullPointerException if the specified collection is null
     * @throws ClassCastException if the class of an element of this list is incompatible with the specified collection
     * @throws IllegalArgumentException if the endpoint indices are out of order (fromIndex > toInde
     * @throws ConcurrentModificationException if the list is structurally modified at any time after the iterator is created, in any way except through the iterator's own remove or add methods
     * @since Release (1st July 2018)
     */
    public Layout(ArrayList<String> layout) {
        this.headings  = layout;
        this.columnCount = layout.size();
    }

    /**
     * <b>Getter</b> <p>
     * The native order was not changed. It's a final value.
     * @since Release (1st July 2018)
     *  @return The headings from the layout
     */
    public ArrayList<String> getHeadings() {
        return this.headings;
    }
    /**
     * <b>Getter</b> <p>
     * @since Release (1st July 2018)
     * @return The number of columns at the layout
     */
    public int getColumnCount() {
        return this.columnCount;
    }
    /**
     * <b>Getter</b> <p>
     * @param heading represented by a String
     * @since Release (1st July 2018)
     * @return The column ID where the heading was found.
     */
    public int getColumnIDby(String heading) {
        int columnID = -1;
        for (int column = 0; column < this.columnCount; column++) {
            if(this.headings.get(column).equals(heading)) {
                columnID = column;
                break;
            }
        }
        if(columnID == -1) {
            System.err.println("Heading was not found in the layout.");
            System.out.println("Heading was not found in the layout.");
        } 
        return columnID;
    }
    /**
     * <b>Getter</b> <p>
     * @param column representing a Integer
     * @since Release (1st July 2018)
     * @return A heading by a specified column from the layout
     */
    public String getHeadingAt(int column) {
        return this.headings.get(column);
    }
    /**
     * <b>Getter</b> <p>
     * @param from represents the start by a columnID as Intger
     * @param to represents the end by a columnID as Intger
     * @since Release (1st July 2018)
     * @return All elements within a sepcified range from the layout
     */
    public ArrayList<String> getHeadingFromTo(int from, int to) {
        ArrayList<String> listofHeadings = new ArrayList<>((to - from) + 1);
        if(from <= to) {
            for (int column = from; column <= to; column++) {
                listofHeadings.add(this.headings.get(column));
            }
        } else {return null;}
        return listofHeadings;
    }

}