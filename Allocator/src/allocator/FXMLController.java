/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allocator;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author Kevin Ossenbrueck
 */
public class FXMLController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        
        ArrayList<ArrayList<String>> myTable = new ArrayList<>(0);
        int counter = 0;
        for (int row = 0; row < 3; row++) {
            myTable.add(new ArrayList<>());
            for (int column = 0; column < 10; column++) {
               myTable.get(row).add(String.valueOf(++counter));
            }
        }
        
        UserDataTable userDataTable = new UserDataTable(myTable);
        
        System.out.println("*** The Table ***");
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 10; column++) {
               System.out.print(userDataTable.getUserDataTable().get(row).get(column) + " ");
            }
            System.out.println();
        }
        
        System.out.println("*** The Headings ***");
        for (int column = 0; column < 10; column++) {
            System.out.print(userDataTable.getUserDataHeadings().get(column) + " ");
        } System.out.println("");
        
        System.out.println("*** Payload ***");
        for (int row = 0; row < 2; row++) {
            for (int column = 0; column < 10; column++) {
               System.out.print(userDataTable.getUserDataTable().get(row).get(column) + " ");
            }
            System.out.println();
        }
        
        System.out.println("*** getColumnAt() ***");
        for (int element = 0; element < userDataTable.getUserDataTableRowCount(); element++) {
            System.out.print(userDataTable.getColumnAt(9).get(element) + " ");
        } System.out.println();
        
        System.out.println("*** getArea() ***");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(userDataTable.getArea(0, 0, 2, 2).get(i).get(j) + " ");
            }
            System.out.println();
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
