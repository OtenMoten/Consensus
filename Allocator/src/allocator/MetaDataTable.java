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
public class MetaDataTable {
    
    /** The whole metadata table, headings and payload, will be stored in a two-dimensional ArrayList.
     * Every element of the metadata table will be internal handled as a String.
     * @since Release (1st July 2018)
    */
    private final ArrayList<ArrayList<String>> table;
    /**
     * Each heading in the metadata table will be separately stored in a one-dimensional ArrayList.
     * Every heading in the ArrayList is a String object.
     * The native order will not be changed.
     * @since Release (1st July 2018)
    */
    private final ArrayList<String> headings;
    /**
     * The payload of the metadata table will also be separately stored, in a two-dimensional ArraList.
     * "Payload" means the metadata table <u>without</u> headings.
     * Every element of the payload will be internal handled as a String.
     * @since Release (1st July 2018)
    */
    private final ArrayList<ArrayList<String>> payload;
    /**
     * The count of rows at the metadata table, represented by a Integer.
     * The first line with the headings <b>is counted</b> too.
     * @since Release (1st July 2018)
    */
    private final int rowCount;
    /**
     * The count of columns at the metadata table, represented by a Integer.
     * @since Release (1st July 2018)
    */
    private final int columnCount;
    
    /**
     * <b>Constructor</b> <br>
     * > Set up the metadata table as a two-dimensional ArrayList. <br>
     * > Set up the number of rows. <br>
     * > Set up the number of columns. <br>
     * > Seperate the headings. <br>
     * > Seperate the payload. <br>r>
     *
     * @param metaDataTable two-dimensional String ArrayList
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index > size())
     * @throws NullPointerException if the specified collection is null
     * @throws ClassCastException if the class of an element of this list is incompatible with the specified collection
     * @throws IllegalArgumentException if the endpoint indices are out of order (fromIndex > toInde
     * @throws ConcurrentModificationException if the list is structurally modified at any time after the iterator is created, in any way except through the iterator's own remove or add methods
     * @since Release (1st July 2018)
    */
    public MetaDataTable(ArrayList<ArrayList<String>> metaDataTable) {
        this.table = metaDataTable;
        this.rowCount = metaDataTable.size();
        this.columnCount = metaDataTable.get(0).size();
        this.headings = metaDataTable.get(0);
        this.payload = new ArrayList<>(metaDataTable);
        this.payload.remove(0);       
    }
    
    /**
     * <b>Getter</b> <br>
     * @since Release (1st July 2018)
     * @return Headings and payload
    */
    public ArrayList<ArrayList<String>> getMetaDataTable() {
        return this.table;
    }
    /**
     * <b>Getter</b> <br>
     * The headings line is <u>included</u>.
     * @since Release (1st July 2018)
     * @return Number of rows at the metadata table
    */
    public int getMetaDataTableRowCount(){
        return this.rowCount;
    }
    /**
     * <b>Getter</b> <br>
     * @since Release (1st July 2018)
     * @return The number of columns at the metadata table
    */
    public int getMetaDataTableColumnCount() {
        return this.columnCount;
    }
    /**
     * <b>Getter</b> <br>
     * The native order is was not changed. It's a final value.
     * @since Release (1st July 2018)
     *  @return The headings from the metadata table
    */
    public ArrayList<String> getMetaDataHeadings() {
        return this.headings;
    }
    /**
     * <b>Getter</b> <br>
     * @since Release (1st July 2018)
     * @return The metadata table without headings.
    */
    public ArrayList<ArrayList<String>> getMetaDataPayload() {
        return this.payload;
    }
    /**
     * <b>Getter</b> <br>
     * @param x Representing a row
     * @param y Representing a column
     * @since Release (1st July 2018)
     * @return A element at specified coodinates
    */
    public String getElementAt(int x, int y) {
        return this.table.get(x).get(y);
    }
    /**
     * <b>Getter</b> <br>
     * @param column representing a Integer
     * @since Release (1st July 2018)
     * @return A heading by a specified column from the metadata.
    */
    public String getHeadingAt(int column) {
        return headings.get(column);
    }
    /**
     * <b>Getter</b> <br>
     * @param row representing a Integer
     * @since Release (1st July 2018)
     * @return All elements within a sepcified row from the metadata table
    */
    public ArrayList<String> getRowAt(int row) {
        return table.get(row);
    }
    /**
     * <b>Getter</b> <br>
     * @param column representing a Integer
     * @since Release (1st July 2018)
     * @return All elements within a sepcified column from the metadata table
    */
    public ArrayList<String> getColumnAt(int column) {
        ArrayList<String> columnElements = new ArrayList<>(rowCount);
        for (int row = 0; row < rowCount; row++) {
            columnElements.add(table.get(row).get(column));
        }
        return columnElements;
    }
    /**
     * <b>Getter</b> <br>
     * @param fromX Representing a start row
     * @param fromY Representing a start column
     * @param toX Representing a end row
     * @param toY Representing a end column
     * @since Release (1st July 2018)
     * @return All elements within a sepcified area from the metadata table
    */
    public ArrayList<ArrayList<String>> getArea(int fromX, int fromY, int toX, int toY) {
        ArrayList<ArrayList<String>> areaElements = new ArrayList<>(0);
        for (int row = fromX; row <= toX; row++) {
            areaElements.add(new ArrayList<>(0));
            for (int column = fromY; column <= toY; column++) {
                areaElements.get(areaElements.size()-1).add(this.table.get(row).get(column));
            }
        }
        return areaElements;
    }  
}