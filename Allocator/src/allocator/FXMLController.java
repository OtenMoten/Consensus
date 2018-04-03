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
    
    @FXML
    private void readInDataAll() {

        this.fileReader = new FileReader("userDataTable.xls", "metadata.xml", "layout.csv");
        
        this.userDataTable = new UserDataTable(fileReader.readInUserDataTableHSSF());
        this.metaDataTable = new MetaDataTable(fileReader.readInMetaDataTable());
        this.fileReader.readInMetaDataTable();
        this.layout = new Layout(fileReader.readInLayout(";"));
        
        System.out.println("USER DATA TABLE");
        for (int row = 0; row < this.userDataTable.getRowCount(); row++) {
            for (int column = 0; column < this.userDataTable.getColumnCount(); column++) {
                System.out.print(this.userDataTable.getElementAt(row, column) + " \t ");
            }
            System.out.println();
        }
        
        System.out.println("LAYOUT");
        for (int column = 0; column < this.layout.columnCount; column++) {
            System.out.print(this.layout.getHeadingAt(column) + " ");
        }
        
        System.out.println();
        System.out.println("META DATA TABLE PAYLOAD");
        for (int i = 0; i < this.metaDataTable.getColumnCount(); i++) {
                System.out.println(this.metaDataTable.getPayload().get(i));
        }
        System.out.println("META DATA TABLE HEADINGS");
        for (int i = 0; i < this.metaDataTable.getColumnCount(); i++) {
                System.out.println(this.metaDataTable.getHeadings().get(i));
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
