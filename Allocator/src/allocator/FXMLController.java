/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allocator;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 *
 * @author Kevin Ossenbrueck
 */
public class FXMLController implements Initializable {
    
    private UserDataTable userDataTable;
    private MetaDataTable metaDataTable;
    private FinalTable finalTable;
    private Layout layout;
    private FileReader fileReader;
    
    /**
     * <b>Constructor</b> <p>
     * @since Release (1st July 2018)
     */

    @FXML
    public void readInAll() {
        
        
        
        
        testFinalTable();
        
    }
    
    private void testUserDataTable() {
        System.out.println("UserDataTable Class Test");
        
        System.out.println("Native userdata table");
        for (int row = 0; row < this.userDataTable.getRowCount(); row++) {
            for (int column = 0; column < this.userDataTable.getColumnCount(); column++) {
                if(row == 0) {
                    System.out.print(this.userDataTable.getTable().get(row).get(column) + " \t ");
                } else {
                    System.out.print(this.userDataTable.getTable().get(row).get(column) + " \t\t ");
                } 
            } System.out.println();
        }
        System.out.println();
        
        System.out.println("Getting a area");
        for (int row = 0; row < this.userDataTable.getArea(1, 1, 3, 4).size(); row++) {
            for (int column = 0; column < this.userDataTable.getArea(1, 1, 3, 4).get(0).size(); column++) {
                System.out.print(this.userDataTable.getArea(1, 1, 3, 4).get(row).get(column) + " \t\t ");
            } System.out.println();
        }
        System.out.println();
        
        System.out.println("Getting a row");
        System.out.println(this.userDataTable.getRowAt(0));
        System.out.println("Getting a column");
        System.out.println(this.userDataTable.getColumnAt(0));
        System.out.println("Getting the number of rows");
        System.out.println(this.userDataTable.getRowCount());
        System.out.println("Getting the number of columns");
        System.out.println(this.userDataTable.getColumnCount());
        System.out.println();
        
        System.out.println("Getting a column ID by heading");
        System.out.println(this.userDataTable.getColumnIDbyHeading("Heading 5"));
        System.out.println();
        
        System.out.println("Getting a element at X,Y");
        System.out.println(this.userDataTable.getElementAt(2, 3));
        System.out.println();
        
        System.out.println("Getting a heading at sepcified column ID");
        System.out.println(this.userDataTable.getHeadingAt(1));
        System.out.println();
        
        System.out.println("Getting all headings");
        System.out.println(this.userDataTable.getHeadings());
        System.out.println();
        
        System.out.println("Getting the payload of the userdata table");
        for (int row = 0; row < this.userDataTable.getRowCount()-1; row++) {
            for (int column = 0; column < this.userDataTable.getColumnCount(); column++) {
                System.out.println(this.userDataTable.getPayload().get(row).get(column));  
            } System.out.println();
        }
    }
    
    private void testMetaDataTable () {
        System.out.println("MetaDataTable Class Test");
        
        System.out.println("Native metadata table");
        for (int row = 0; row < this.metaDataTable.getRowCount(); row++) {
            for (int column = 0; column < this.metaDataTable.getColumnCount(); column++) {
                if(row == 0) {
                    System.out.print(this.metaDataTable.getTable().get(row).get(column) + " \t " + "| | |");
                } else {
                    System.out.print(this.metaDataTable.getTable().get(row).get(column) + " \t " + "| | |");
                } 
            } System.out.println();
        }
        System.out.println();
        
        System.out.println("Getting a area");
        for (int row = 0; row < this.metaDataTable.getArea(0, 2, 1, 4).size(); row++) {
            for (int column = 0; column < this.metaDataTable.getArea(0, 2, 1, 4).get(0).size(); column++) {
                System.out.print(this.metaDataTable.getArea(0, 2, 1, 4).get(row).get(column) + " \t\t ");
            } System.out.println();
        }
        System.out.println();
        
        System.out.println("Getting a row");
        System.out.println(this.metaDataTable.getRowAt(0));
        System.out.println("Getting a column");
        System.out.println(this.metaDataTable.getColumnAt(0));
        System.out.println("Getting the number of rows");
        System.out.println(this.metaDataTable.getRowCount());
        System.out.println("Getting the number of columns");
        System.out.println(this.metaDataTable.getColumnCount());
        System.out.println();
        
        System.out.println("Getting a column ID by heading");
        System.out.println(this.metaDataTable.getColumnIDbyHeading("contributors"));
        System.out.println();
        
        System.out.println("Getting a element at X,Y");
        System.out.println(this.metaDataTable.getElementAt(1, 2));
        System.out.println();
        
        System.out.println("Getting a heading at sepcified column ID");
        System.out.println(this.metaDataTable.getHeadingAt(2));
        System.out.println();
        
        System.out.println("Getting all headings");
        System.out.println(this.metaDataTable.getHeadings());
        System.out.println();
        
        System.out.println("Getting the payload of the userdata table");
        System.out.println(this.metaDataTable.getPayload());
    }

    private void testLayout() {
        System.out.println("Layout Class Test");
        
        System.out.println("Getting the headings");
        System.out.println(this.layout.getHeadings());
  
        System.out.println("Getting the number of headings");
        System.out.println(this.layout.getColumnCount());
  
        System.out.println("Getting a heading from a specified column");
        for (int column = 0; column < this.layout.getColumnCount(); column++) {
            System.out.println(this.layout.getHeadingAt(column));
        }
        
        System.out.println("Getting headings in a specified range");
        System.out.println(this.layout.getHeadingFromTo(2, 1));
    
    }
    
    private void testFinalTable() {
        System.out.println("FinalTable Class Test");
        
        /*System.out.println("Setting a final table");
        this.finalTable.setTable(this.userDataTable.getTable());
        for (int row = 0; row < this.finalTable.getRowCount(); row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                System.out.print(this.finalTable.getTable().get(row).get(column) + " ");
            } System.out.println();
        } System.out.println();
       
        System.out.println("Setting a area in a final table");
        this.finalTable.setArea(this.metaDataTable.getArea(0, 0, 1, 4), 1, 0, 2, 4);
        
        for (int row = 0; row < this.finalTable.getRowCount(); row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                System.out.print(this.finalTable.getTable().get(row).get(column) + " ");
            } System.out.println();
        } System.out.println();

        System.out.println("Setting a specified column in a final table");        
        this.finalTable.setColumnAt(3, new ArrayList<>(Arrays.asList(new String[]{"A", "B", "C", "D", "E"})));
        for (int row = 0; row < this.finalTable.getRowCount(); row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                System.out.print(this.finalTable.getTable().get(row).get(column) + " ");
            } System.out.println();
        } System.out.println();
        
        System.out.println("Checking Headings");
        for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
            System.out.print(this.finalTable.getHeadings().get(column) + " ");
        } System.out.println();
        System.out.println();
        
        System.out.println("Checking Payload");
        for (int row = 0; row < this.finalTable.getRowCount() - 1; row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                System.out.print(this.finalTable.getPayload().get(row).get(column) + " ");
            } System.out.println();
        } System.out.println();
                
        System.out.println("Setting a specified row in a final table");
        this.finalTable.setRowAt(1, new ArrayList<>(Arrays.asList(new String[]{"A", "B", "C", "D", "E"})));
        for (int row = 0; row < this.finalTable.getRowCount(); row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                System.out.print(this.finalTable.getTable().get(row).get(column) + " ");
            } System.out.println();
        } System.out.println();
        
        System.out.println("Checking Headings");
        for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
            System.out.print(this.finalTable.getHeadings().get(column) + " ");
        } System.out.println();
        System.out.println();
        
        System.out.println("Checking Payload");
        for (int row = 0; row < this.finalTable.getRowCount() - 1; row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                System.out.print(this.finalTable.getPayload().get(row).get(column) + " ");
            } System.out.println();
        } System.out.println();
   
        System.out.println("Setting a specified element in the final table");
        this.finalTable.setElementAt(3, 2, "ELEMENT AT");
        for (int row = 0; row < this.finalTable.getRowCount(); row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                System.out.print(this.finalTable.getTable().get(row).get(column) + " ");
            } System.out.println();
        } System.out.println();
        
        System.out.println("Checking Headings");
        for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
            System.out.print(this.finalTable.getHeadings().get(column) + " ");
        } System.out.println();
        System.out.println();
        
        System.out.println("Checking Payload");
        for (int row = 0; row < this.finalTable.getRowCount() - 1; row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                System.out.print(this.finalTable.getPayload().get(row).get(column) + " ");
            } System.out.println();
        } System.out.println();
        
        System.out.println("Setting a specified heading in the final table");
        this.finalTable.setHeadingAt(2, "SET HEADING AT");
        for (int row = 0; row < this.finalTable.getRowCount(); row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                System.out.print(this.finalTable.getTable().get(row).get(column) + " ");
            } System.out.println();
        } System.out.println();
        
        System.out.println("Checking Headings");
        for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
            System.out.print(this.finalTable.getHeadings().get(column) + " ");
        } System.out.println();
        System.out.println();
        
        System.out.println("Setting a specified payload in the final table");
        this.finalTable.setPayload(this.fileReader.readInUserDataTableHSSF());
        for (int row = 0; row < this.finalTable.getRowCount(); row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                System.out.print(this.finalTable.getTable().get(row).get(column) + " ");
            } System.out.println();
        } System.out.println();
        
        System.out.println("Checking Headings");
        for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
            System.out.print(this.finalTable.getHeadings().get(column) + " ");
        } System.out.println();
        System.out.println();
        
        System.out.println("Checking Payload");
        for (int row = 0; row < this.finalTable.getRowCount() - 1; row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                System.out.print(this.finalTable.getPayload().get(row).get(column) + " ");
            } System.out.println();
        } System.out.println();
        
        System.out.println("Setting specified headings in the final table");
        this.finalTable.setHeadings(new ArrayList<>(Arrays.asList(new String[]{"V", "W", "X", "Y", "Z"})));
        for (int row = 0; row < this.finalTable.getRowCount(); row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                System.out.print(this.finalTable.getTable().get(row).get(column) + " ");
            } System.out.println();
        } System.out.println();
        
        System.out.println("Checking Headings");
        for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
            System.out.print(this.finalTable.getHeadings().get(column) + " ");
        } System.out.println();
        System.out.println();
        
        System.out.println("Checking Payload");
        for (int row = 0; row < this.finalTable.getRowCount() - 1; row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                System.out.print(this.finalTable.getPayload().get(row).get(column) + " ");
            } System.out.println();
        } System.out.println();
        
        System.out.println("");
        System.out.println("Checking");
        System.out.println("Getter");
        
        System.out.println("Getting the final table");
        for (int row = 0; row < this.finalTable.getRowCount(); row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                System.out.print(this.finalTable.getTable().get(row).get(column) + " ");
            } System.out.println();
        } System.out.println();
        
        System.out.println("getArea");
        System.out.println(this.finalTable.getArea(0, 0, 2, 4));
        
        System.out.println("getColumnAt");
        System.out.println(this.finalTable.getColumnAt(2));
        
        System.out.println("getColumnIDbyHeading");
        System.out.println(this.finalTable.getColumnIDby("Y"));
        
        System.out.println("getElementAt");
        System.out.println(this.finalTable.getElementAt(3, 2));
        
        System.out.println("getHeadingAt");
        System.out.println(this.finalTable.getHeadingAt(3));
        
        System.out.println("getHeadings");
        System.out.println(this.finalTable.getHeadings());
        
        System.out.println("getPayload");
        System.out.println(this.finalTable.getPayload());
        
        System.out.println("getRowAt");
        System.out.println(this.finalTable.getRowAt(2));*/
        
        System.out.println("****************** CHECK OPERATIONS ******************");
        
        System.out.println("Setting a final table");
        
        this.finalTable = new FinalTable();
        
        /*this.finalTable.addRow();
        this.finalTable.addRow();
        this.finalTable.addColumn();
        this.finalTable.addColumn();
        
        for (int row = 0; row < this.finalTable.getRowCount(); row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                System.out.print(this.finalTable.getTable().get(row).get(column));
            } System.out.println();
        }
        
        for (int row = 0; row < this.finalTable.getPayload().size(); row++) {
            for (int column = 0; column < this.finalTable.getPayload().get(0).size(); column++) {
                System.out.print(this.finalTable.getPayload().get(row).get(column));
            } System.out.println();
        }
        
        for (int column = 0; column < this.finalTable.getHeadings().size(); column++) {
            System.out.println(this.finalTable.getHeadings().get(column));
        } */
        
        this.finalTable.addColumn(heading);

        /*
        this.finalTable.addColumn(0);
        
        this.finalTable.addColumn(heading, payload);
        
        this.finalTable.addColumn(0, heading);
        
        this.finalTable.addColumn(0, heading, payload); */
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize done :-)");
        this.fileReader = new FileReader("userDataTable.xls", "metadata.xml", "layout.csv");
        this.userDataTable = new UserDataTable(this.fileReader.readInUserDataTableHSSF());
        this.metaDataTable = new MetaDataTable(this.fileReader.readInMetaDataTable());
        this.finalTable = new FinalTable();
        this.layout = new Layout(this.fileReader.readInLayout(";"));
    }
    
}