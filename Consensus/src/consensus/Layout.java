/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consensus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

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
     * The name of the column where the generated metadata should be.
     * @since Release (1st July 2018)
     */
    private final String citationColumn;
    /**
     * A list of specified headings from the layout which are criteria for the selection of empty columns while the allocation. <p>
     * If these headings are no filled with userdata in the final table then the user will be encouraged to select it from ceckboxes. <p>
     * This list will be the source for which .CSV-files will be selected for the checkboxes content.
     * @since Release (1st July 2018)
     */
    private final ArrayList<String> comboBoxes;

    /**
     * <b>Constructor</b> <p>
     * Set up the layout as a one-dimensional ArrayList. <p>
     * Set up the number of columns. <p>
     *
     * @param layout represented by a one-dimensional String ArrayList
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index > size())
     * @throws NullPointerException if the specified collection is null
     * @throws ClassCastException if the class of an element of this list is incompatible with the specified collection
     * @throws IllegalArgumentException if the endpoint indices are out of order (fromIndex > toInde
     * @throws ConcurrentModificationException if the list is structurally modified at any time after the iterator is created, in any way except through the iterator's own remove or add methods
     * @since Release (1st July 2018)
     */
    public Layout(ArrayList<ArrayList<String>> layout) {
        this.headings  = layout.get(0);
        this.columnCount = layout.get(0).size();
        this.citationColumn = layout.get(1).get(0);
        this.comboBoxes = layout.get(2);
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
     * @return The heading of the citation column from the layout
     */
    public String getCitationColumn() {
        return this.citationColumn;
    }
    /**
     * <b>Getter</b> <p>
     * The native order was not changed. It's a final value.
     * @since Release (1st July 2018)
     * @return The headings from the layout which should be displayed as a checkbox.
     */
    public ArrayList<String> getComboBoxes() {
        return this.comboBoxes;
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
    /**
     * <b>Getter</b> <p>
     * @param comboBoxString represented by a String
     * @since Release (1st July 2018)
     * @return A list in form of a String ArrayList with the payload of the .CSV-files
     */
    public ArrayList<String> getComboBoxPayload(String comboBoxString) {
        BufferedReader bufferedReader;
        String splitter = ";";
        ArrayList<String> comboBoxPayload = new ArrayList<>();
  
        try {
            bufferedReader = new BufferedReader(new java.io.FileReader("\\\\gruppende\\IV2.2\\Int\\WRMG\\Table_Extractor\\Layouts\\checkboxes\\" + comboBoxString + ".csv"));
            Iterator<String> iterator = bufferedReader.lines().iterator();
            comboBoxPayload.addAll(Arrays.asList(iterator.next().split(splitter)));
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Layout.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return comboBoxPayload;
    }

}