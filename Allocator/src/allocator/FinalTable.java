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
public class FinalTable {
    
    /** The whole final table, headings and payload, will be stored in a two-dimensional ArrayList.
     * Every element of the final table will be internal handled as a String.
     * @since Release (1st July 2018)
     */
    private ArrayList<ArrayList<String>> table;
    /**
     * Each heading in the final table will be separately stored in a one-dimensional ArrayList.
     * Every heading in the ArrayList is a String object.
     * The native order will not be changed.
     * @since Release (1st July 2018)
     */
    private ArrayList<String> headings;
    /**
     * The payload of the final table will also be separately stored, in a two-dimensional ArraList.
     * "Payload" means the final table <u>without</u> headings.
     * Every element of the payload will be internal handled as a String.
     * @since Release (1st July 2018)
     */
    private ArrayList<ArrayList<String>> payload;
    /**
     * The count of rows at the final table, represented by a Integer.
     * The first line with the headings <b>is counted</b> too.
     * @since Release (1st July 2018)
     */
    private int rowCount;
    /**
     * The count of columns at the final table, represented by a Integer.
     * @since Release (1st July 2018)
     */
    private int columnCount;
    
    /**
     * <b>Constructor</b> <p>
     * > Set up the final table as a two-dimensional ArrayList. <p>
     * > Set up the number of rows. <p>
     * > Set up the number of columns. <p>
     * > Seperate the headings. <p>
     * > Seperate the payload. <p>r>
     *
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index > size())
     * @throws NullPointerException if the specified collection is null
     * @throws ClassCastException if the class of an element of this list is incompatible with the specified collection
     * @throws IllegalArgumentException if the endpoint indices are out of order (fromIndex > toInde
     * @throws ConcurrentModificationException if the list is structurally modified at any time after the iterator is created, in any way except through the iterator's own remove or add methods
     * @since Release (1st July 2018)
     */
    public FinalTable() {
        this.table = new ArrayList<>();
        this.rowCount = 0;
        this.columnCount = 0;
        this.headings = new ArrayList<>();
        this.payload = new ArrayList<>();    
    }
    
    /**
     * <b>Setter</b> <p>
     * @param finalTable represented by a two-dimensional String ArrayList
     * @since Release (1st July 2018)
     */
    //confirmation pending
    public void setTable(ArrayList<ArrayList<String>> finalTable) {
        this.table = finalTable;
        this.rowCount = finalTable.size(); //The headings line is counted too!
        this.columnCount = finalTable.get(0).size();
        this.headings = finalTable.get(0);
        this.payload = new ArrayList<>(finalTable);
        this.payload.remove(0); 
    }
    /**
     * <b>Setter</b> <p>
     * Set the number of rows at the current final table
     * @since Release (1st July 2018)
     */
    public void setRowCount(){
        this.rowCount = this.table.size();
    }
    /**
     * <b>Setter</b> <p>
     * Set the number of columns at the current final table
     * @since Release (1st July 2018)
     */
    public void setColumnCount() {
       this.columnCount = this.table.get(0).size();
    }
    /**
     * <b>Setter</b> <p>
     * @param headings represented by a one-dimensional String ArrayList
     * @since Release (1st July 2018)
     */
    //confirmation pending
    public void setHeadings(ArrayList<String> headings) {
        this.table.set(0, headings);
        this.headings = headings;
    }
    /**
     * <b>Setter</b> <p>
     * @param payload represented by a two-dimensional String Array <p>
     * "Payload" is meaning a final table without headings.
     * @since Release (1st July 2018)
     */
    //confirmation pending
    public void setPayload(ArrayList<ArrayList<String>> payload) {
        this.payload = payload;
        for (int row = 1; row < this.rowCount; row++) {
            for (int column = 0; column < this.columnCount; column++) {
                this.table.get(row).set(column, payload.get(row).get(column));
            }
        }
    }
    /**
     * <b>Setter</b> <p>
     * @param column represented by a Integer
     * @param heading represented by a text String
     * @since Release (1st July 2018)
     */
    //confirmation pending
    public void setHeadingAt(int column, String heading) {
        this.table.get(0).set(column, heading);
        this.headings.set(column, heading);
    }
    /**
     * <b>Setter</b> <p>
     * @param x Representing a row
     * @param y Representing a column
     * @param value Represented  by a String
     * @since Release (1st July 2018)
     */
    //confirmation pending
    public void setElementAt(int x, int y, String value) {
        this.table.get(x).set(y, value);
        //If x=0 it will change only a heading of the final table. 
        //Therefore, payload remains untouched.
        this.headings.set(y, value);
        if(x > 0) {this.payload.get(x-1).set(y, value);} 
    }
    /**
     * <b>Setter</b> <p>
     * @param row represented by a Integer
     * @param rowContent represented by a one-dimensional String ArrayList 
     * @since Release (1st July 2018)
     */
    //confirmation pending
    public void setRowAt(int row, ArrayList<String> rowContent) {
        this.table.set(row, rowContent);
        //If x=0 it will change the whole headings of the final table
        if(row == 0) {this.headings = rowContent;}
        if(row > 0) {this.payload.set(row-1, rowContent);}
    }
    /**
     * <b>Setter</b> <p>
     * The columnContent is set up 1:1 in the final table.
     * @param column represented by a Integer
     * @param columnContent represented by a one-dimensional String ArrayList 
     * @since Release (1st July 2018)
     */
    //confirmation pending
    public void setColumnAt(int column, ArrayList<String> columnContent) {
        for (int row = 0; row < rowCount; row++) {
            this.table.get(row).set(column, columnContent.get(row));
        }
    }
    /**
     * <b>Setter</b> <p>
     * Only a existing area can be set up.  <p>
     * The XY-space need as same size as areaContent. 
     * @param areaContent represented by a two-dimensional String ArrayList
     * @param fromX Representing a start row
     * @param fromY Representing a start column
     * @param toX Representing a end row
     * @param toY Representing a end column
     * @since Release (1st July 2018)
     */
    //confirmation pending
    public void setArea(ArrayList<ArrayList<String>> areaContent, int fromX, int fromY, int toX, int toY) {
        int areaRow = 0, areaColumn = 0;
        for (int row = fromX; row <= toX; row++) {
            for (int column = fromY; column <= toY; column++) {
                this.table.get(row).add(column, areaContent.get(areaRow++).get(areaColumn++));
            }
        }
    }
    
    /**
     * <b>Getter</b> <p>
     * @since Release (1st July 2018)
     * @return Headings and payload
     */
    public ArrayList<ArrayList<String>> getTable() {
        return this.table;
    }
    /**
     * <b>Getter</b> <p>
     * The headings line is <u>included</u>.
     * @since Release (1st July 2018)
     * @return Number of rows at the final table
     */
    public int getRowCount(){
        return this.rowCount;
    }
    /**
     * <b>Getter</b> <p>
     * @since Release (1st July 2018)
     * @return The number of columns at the final table
     */
    public int getColumnCount() {
        return this.columnCount;
    }
    /**
     * <b>Getter</b> <p>
     * @since Release (1st July 2018)
     * @return The headings from the userdata table
     */
    public ArrayList<String> getHeadings() {
        return this.headings;
    }
    /**
     * <b>Getter</b> <p>
     * @since Release (1st July 2018)
     * @return The final table without headings
     */
    public ArrayList<ArrayList<String>> getPayload() {
        return this.payload;
    }
    /**
     * <b>Getter</b> <p>
     * @param x Representing a row
     * @param y Representing a column
     * @since Release (1st July 2018)
     * @return A element at specified coodinates
     */
    public String getElementAt(int x, int y) {
        return table.get(x).get(y);
    }
    /**
     * <b>Getter</b> <p>
     * @param column represented by a Integer
     * @since Release (1st July 2018)
     * @return A heading by a specified column from the final table
     */
    public String getHeadingAt(int column) {
        return headings.get(column);
    }
    /**
     * <b>Getter</b> <p>
     * @param row represented by a Integer
     * @since Release (1st July 2018)
     * @return All elements within a sepcified row from the final table
     */
    public ArrayList<String> getRowAt(int row) {
        return table.get(row);
    }
    /**
     * <b>Getter</b> <p>
     * @param column represented by a Integer
     * @since Release (1st July 2018)
     * @return All elements within a sepcified column from the final table
     */
    public ArrayList<String> getColumnAt(int column) {
        ArrayList<String> columnElements = new ArrayList<>(rowCount);
        for (int row = 0; row < rowCount; row++) {
            columnElements.add(table.get(row).get(column));
        }
        return columnElements;
    }
    /**
     * <b>Getter</b> <p>
     * @param heading represented by a String
     * @return The ID of the column from the final table, -1 if heading was not found
     * @since Release (1st July 2018)
     */
    public int getColumnID(String heading) {
        int columnID = -1;
        for (int column = 0; column < this.columnCount; column++) {
            if(this.headings.get(column).equals(heading)) {columnID = column; break;}
        }
        return columnID;
    }
    /**
     * <b>Getter</b> <p>
     * @param fromX Representing a start row
     * @param fromY Representing a start column
     * @param toX Representing a end row
     * @param toY Representing a end column
     * @since Release (1st July 2018)
     * @return All elements within a sepcified area from the final table
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
