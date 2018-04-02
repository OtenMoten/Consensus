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
    
    public final ArrayList<String> headings;
    
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
    
    
}
