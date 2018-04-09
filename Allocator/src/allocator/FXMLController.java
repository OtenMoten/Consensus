/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allocator;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 *
 * @author Kevin Ossenbrueck
 */
public class FXMLController implements Initializable {
    
    UserDataTable userDataTable;
    MetaDataTable metaDataTable;
    Layout layout;
    FileReader fileReader;
    
    /**
     * <b>Constructor</b> <p>
     * @since Release (1st July 2018)
     */

    @FXML
    public void readInAll() {
        
        this.fileReader = new FileReader("userDataTable.xls", "metadata.xml", "layout.csv");
        
        this.userDataTable = new UserDataTable(this.fileReader.readInUserDataTableHSSF());
        this.metaDataTable = new MetaDataTable(this.fileReader.readInMetaDataTable());
        this.fileReader.readInMetaDataTable();
        this.layout = new Layout(this.fileReader.readInLayout(";"));
        
        System.out.println("UserDataTable Class Test");
        
        System.out.println("Native table");
        for (int row = 0; row < userDataTable.getRowCount(); row++) {
            for (int column = 0; column < userDataTable.getColumnCount(); column++) {
                if(row == 0) {
                    System.out.print(userDataTable.getTable().get(row).get(column) + " \t ");
                } else {
                    System.out.print(userDataTable.getTable().get(row).get(column) + " \t\t ");
                } 
            } System.out.println();
        }
        
        System.out.println("Getting a area");
        for (int row = 0; row < userDataTable.getArea(1, 1, 3, 4).size(); row++) {
            for (int column = 0; column < userDataTable.getArea(1, 1, 3, 4).get(0).size(); column++) {
                System.out.print(userDataTable.getArea(1, 1, 3, 4).get(row).get(column) + " \t\t ");
            } System.out.println();
        }
        
        System.out.println("Getting a row");
        System.out.println(userDataTable.getRowAt(0));
        System.out.println("Getting a column");
        System.out.println(userDataTable.getColumnAt(0));
        System.out.println("Getting the number of rows");
        System.out.println(userDataTable.getRowCount());
        System.out.println("Getting the number of columns");
        System.out.println(userDataTable.getColumnCount());
        
        System.out.println("Getting a column ID by heading");
        System.out.println(userDataTable.getColumnIDbyHeading("Heading 5"));
        
        System.out.println("Getting a element at X,Y");
        System.out.println(userDataTable.getElementAt(2, 3));
        
        System.out.println("Getting a heading at sepcified column ID");
        System.out.println(userDataTable.getHeadingAt(1));
        
        System.out.println("Getting all headings");
        System.out.println(userDataTable.getHeadings());
        
        for (int row = 0; row < userDataTable.getRowCount()-1; row++) {
            for (int column = 0; column < userDataTable.getColumnCount(); column++) {
                System.out.println(userDataTable.getPayload().get(row).get(column));  
            } System.out.println();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
}