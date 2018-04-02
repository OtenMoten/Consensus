/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allocator;

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
    public final ArrayList<String> headings;
    /**
     * The count of columns at the layout, represented by a Integer.
     * @since Release (1st July 2018)
    */
    public final int columnCount;
    
    /**
     * <b>Constructor</b> <br>
     * > Set up the layout as a one-dimensional ArrayList. <br>
     * > Set up the number of columns. <br>
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
     * <b>Getter</b> <br>
     * The native order was not changed. It's a final value.
     * @since Release (1st July 2018)
     *  @return The headings from the layout
    */
    public ArrayList<String> getHeadings() {
        return this.headings;
    }
    /**
     * <b>Getter</b> <br>
     * @since Release (1st July 2018)
     * @return The number of columns at the layout
    */
    public int getColumnCount() {
        return this.columnCount;
    }
    /**
     * <b>Getter</b> <br>
     * @param column representing a Integer
     * @since Release (1st July 2018)
     * @return A heading by a specified column from the layout
    */
    public String getHeadingAt(int column) {
        return this.headings.get(column);
    }
    
    public ArrayList<String> getHeadingFromTo(int from, int to) {
        ArrayList<String> listofHeadings = new ArrayList<>();
        for (int column = from; column <= to; column++) {
            listofHeadings.add(this.headings.get(column));
        }
        return listofHeadings;
    }

}