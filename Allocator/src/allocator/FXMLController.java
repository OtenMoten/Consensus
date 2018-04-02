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

        FileReader fileReader = new FileReader("userDataTable.xls", "", "layout.csv");
        
        ArrayList<ArrayList<String>> userDataTable = fileReader.readInUserDataTable();
        ArrayList<String> layout = fileReader.readInLayout(";");
        
        System.out.println("USER DATA TABLE");
        for (int row = 0; row < userDataTable.size(); row++) {
            for (int column = 0; column < userDataTable.get(0).size(); column++) {
                System.out.print(userDataTable.get(row).get(column) + "\t");
            }
            System.out.println();
        }
        
        System.out.println("LAYOUT");
        for (int column = 0; column < layout.size(); column++) {
            System.out.print(layout.get(column) + " ");
        }

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
