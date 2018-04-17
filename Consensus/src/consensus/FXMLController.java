/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consensus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

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
    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);
    @FXML private VBox splitPaneVBoxOne; @FXML private VBox splitPaneVBoxTwo; @FXML private VBox splitPaneVBoxThree; @FXML private VBox splitPaneVBoxFour; @FXML private VBox splitPaneVBoxFife;
    @FXML private HBox splitPaneHBox;

    @FXML
    private void printFinalTable() {
        System.out.println("Actual userdata table: ");
        for (int row = 0; row < this.userDataTable.getRowCount(); row++) {
            for (int column = 0; column < this.userDataTable.getColumnCount(); column++) {
                System.out.print(this.userDataTable.getTable().get(row).get(column) + " \t ");
            } System.out.println();
        }
        
        System.out.println("Actual final table: ");
        for (int row = 0; row < this.finalTable.getRowCount(); row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                System.out.print(this.finalTable.getTable().get(row).get(column) + " \t ");
            } System.out.println();
        }
        
        String finaTablePath = outputFileDialog();
       
        //Create blank workbook
        HSSFWorkbook finalWorkbook = new HSSFWorkbook();
        //Create a blank sheet
        HSSFSheet finalSpreadsheet = finalWorkbook.createSheet(finaTablePath.split("\\\\")[finaTablePath.split("\\\\").length - 1]);
        //Create row object
        HSSFRow finalRow;
        //This data needs to be written (Object[])
        Map < Integer, String[] > empinfo = new TreeMap <>();
        String[] tmpArray;
        
        //read out the finalTable-object and put each row into the map
        for (int row = 0; row < this.finalTable.getRowCount(); row++) {
            
            //'tmpArray' needs for every row a new instance
            //each key in 'empinfo' holds different instances of tmpArray
            //because else every key would have the same value in 'tmpArray'
            tmpArray = new String[this.finalTable.getColumnCount()];
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                tmpArray[column] = this.finalTable.getTable().get(row).get(column);
            }
            empinfo.put(row, tmpArray);
        }
      
        //Iterate over data and write to sheet
        Set < Integer > keyid = empinfo.keySet();
        int rowid = 0;

        for (Integer key : keyid) {
            
            finalRow = finalSpreadsheet.createRow(rowid++);
            String[] strArr = empinfo.get(key);
         
            int cellid = 0;
            for (String str : strArr) {
                Cell finalCell = finalRow.createCell(cellid++);
                finalCell.setCellValue(str);
            }
        }

        try ( //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File(finaTablePath))) {
            finalWorkbook.write(out);
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Maybe your Excel file is open ..");
            System.out.println("Maybe your Excel file is open ..");
        }
        alert.setTitle("Information");
        alert.setHeaderText("Export finished");
        alert.setContentText(finaTablePath);
        alert.show();     
    }
    
    @FXML //mapped to 'Import'-button in the main GUI
    private void startAllocation() {
        for (int row = 0; row < this.finalTable.getRowCount(); row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                System.out.print(this.finalTable.getTable().get(row).get(column) + " \t ");
            } System.out.println();
        }
        selectLayoutHeadings();
    }
    
    /**
     * Fill in the VBoxes in the SplitPane with the selected headings from 'selectUserData()'. <p>
     * Furthermore, the Drag&Drop functionality are given to the displayed elements 
     * ('selected Headings' = partial Layout-object headings and the headings from the userDataTable-object).
     * @param selectedHeadings represented by a one-dimensional String ArrayList
     */
    private void display(ArrayList<String> selectedHeadings) {
        int columnCount = selectedHeadings.size();
        int counter = 0;

        for(int column = 0; column < columnCount; column++) {
            
            String currentHeading = selectedHeadings.get(column);
            Button btnHeading = new Button(selectedHeadings.get(column));
            btnHeading.setTextAlignment(TextAlignment.CENTER);
            btnHeading.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
            btnHeading.setFocusTraversable(false);

            btnHeading.setOnDragOver((DragEvent event) -> {
                /* data is dragged over the target
                * accept it only if it is not dragged from the same node
                * and if it has a string data
                */
                if (event.getGestureSource() != btnHeading && event.getDragboard().hasString()) { /* allow for moving */ event.acceptTransferModes(TransferMode.MOVE);}
                event.consume();
            });
                
            btnHeading.setOnDragEntered((DragEvent event) -> {
                // the drag-and-drop gesture entered the target
                // show to the user that it is an actual gesture target
                if (event.getGestureSource() != btnHeading && event.getDragboard().hasString()) {btnHeading.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 14));}
                event.consume();
            });
                
            btnHeading.setOnDragExited((DragEvent event) -> {
                // mouse moved away, remove the graphical cues
                btnHeading.setText(btnHeading.getText());
                btnHeading.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
                event.consume();
            });

            btnHeading.setOnDragDropped((DragEvent event) -> {
                // data dropped
                // if there is a string data on dragboard, read it and use it
                Dragboard db = event.getDragboard();
                //rembember: final table and layout has the same order of headings
                //get the payload from a specified column in the userdata table
                ArrayList<String> finalTableColumn = new ArrayList<>(this.userDataTable.getPayloadColumnAt(db.getString()));
                //add the, via Drag&Drop, allocated heading to the payload column on top
                finalTableColumn.add(0, currentHeading);
                //put the column with userdata payload and heading from layout at a specified column in the final table
                this.finalTable.setColumnAt(this.layout.getColumnIDby(currentHeading), finalTableColumn);
                // let the source know whether the string was successfully
                // transferred and used
                boolean success = false;
                if(db.hasString()) {success = true;}
                event.setDropCompleted(success);
                btnHeading.setTextFill(Color.DEEPSKYBLUE);
                event.consume();
            });
            
            switch (counter) {
                
                case 0:
                    splitPaneVBoxOne.getChildren().add(btnHeading);
                    counter++;
                    break;
                    
                case 1:
                    splitPaneVBoxTwo.getChildren().add(btnHeading);
                    counter++;
                    break;
                    
                case 2:
                    splitPaneVBoxThree.getChildren().add(btnHeading);
                    counter++;
                    break;
                    
                case 3:
                    splitPaneVBoxFour.getChildren().add(btnHeading);
                    counter++;
                    break;
                    
                case 4:
                    splitPaneVBoxFife.getChildren().add(btnHeading);
                    counter = 0;
                    break;
                    
                default:
                    System.out.println("DEFAULT");
                    alert.setTitle("WARNING!");
                    alert.setHeaderText("Fatal system error orccured- [DEFAULT switch case region in importHeadings()]");
                    alert.setContentText("Contact the creator of this program.");
                    alert.show();
            } 
        }

        //Headings from the userdata table in form of Buttons
        for (int column = 0; column < this.userDataTable.getColumnCount(); column++) {

            Button userdataTableHeading = new Button(this.userDataTable.getTable().get(0).get(column));
            userdataTableHeading.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
            userdataTableHeading.setFocusTraversable(false);
            
            userdataTableHeading.setOnDragDetected((MouseEvent event) -> {
            // drag was detected, start a drag-and-drop gesture
            // allow any transfer mode
            Dragboard db = userdataTableHeading.startDragAndDrop(TransferMode.ANY);
                
            // Put a String on a dragboard
            ClipboardContent content = new ClipboardContent();
            content.putString(userdataTableHeading.getText());
            db.setContent(content);
            //dataTableHeader.setTextFill(Color.DEEPSKYBLUE);
            //dataTableHeader.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 14));
            event.consume();
            });
                
            userdataTableHeading.setOnDragDone((DragEvent event) -> {
            // the drag and drop gesture ended
            // if the data was successfully moved, clear it
            if (event.getTransferMode() == TransferMode.MOVE) {
                userdataTableHeading.setTextFill(Color.GREY);
                userdataTableHeading.setFont(Font.font("Verdana", FontWeight.LIGHT, 12));
                userdataTableHeading.setDisable(true);
            }
            event.consume();
            });
            
            splitPaneHBox.getChildren().add(userdataTableHeading);
        }
        //printTableView();
    }
    
    /**
     * <b> Operation </b> <p>
     * Creating a GUI to select which layout headings should be displayed as Drag&Drop element
     * @since Release (1st July 2018)
     */
    private void selectLayoutHeadings() {
        //select the headings of the final table which should be filled with payload from the userdata table
        Stage selectingStage = new Stage();
        
        HBox hboxContainer = new HBox();
        hboxContainer.setSpacing(10);
        
        VBox vboxLeft = new VBox(); VBox vboxMid = new VBox(); VBox vboxRight = new VBox();
        vboxLeft.setSpacing(10); vboxMid.setSpacing(10); vboxRight.setSpacing(10);
        
        hboxContainer.getChildren().addAll(vboxLeft, vboxMid, vboxRight);
        Scene scene = new Scene(hboxContainer);
        
        selectingStage.setTitle("Selecting headings from Layout");
        selectingStage.setWidth(900);
        selectingStage.setHeight(550);
        
        ArrayList<String> selectedHeadings = new ArrayList<>();
        
        Button btnAccept = new Button("Accept");
        btnAccept.setPrefSize(100, 30);
        btnAccept.setMinSize(100, 30);
        btnAccept.setMaxSize(100, 30);
        btnAccept.autosize();
        btnAccept.setTextAlignment(TextAlignment.CENTER);
        btnAccept.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
        btnAccept.setFocusTraversable(false);
        btnAccept.setOnAction((ActionEvent event) -> {
            for (int i = 0; i < vboxLeft.getChildren().size(); i++) {
                if(vboxLeft.getChildren().get(i) instanceof Button) {/*do nothing*/} 
                else { //get the text of the heading
                    if(((CheckBox) vboxLeft.getChildren().get(i)).isSelected() == true) {
                        selectedHeadings.add(((CheckBox) vboxLeft.getChildren().get(i)).getText());
                    }
                }
            }
            for (int i = 0; i < vboxMid.getChildren().size(); i++) {
                if(vboxMid.getChildren().get(i) instanceof Button) {/*do nothing*/} 
                else { //get the text of the heading
                    if(((CheckBox) vboxMid.getChildren().get(i)).isSelected() == true) {
                        selectedHeadings.add(((CheckBox) vboxMid.getChildren().get(i)).getText());
                    }
                }
            }
            for (int i = 0; i < vboxRight.getChildren().size(); i++) {
                if(vboxRight.getChildren().get(i) instanceof Button) {/*do nothing*/} 
                else { //get the text of the heading
                    if(((CheckBox) vboxRight.getChildren().get(i)).isSelected() == true) {
                        selectedHeadings.add(((CheckBox) vboxRight.getChildren().get(i)).getText());
                    }
                }
            }
            
            //if the Accept-Button is clicked then the selected headings will be displayed in the main GUI
            //next step is to apply the headings from the layout to the headings from the userdata table
            display(selectedHeadings);
            
            selectingStage.close();
        });
        
        final ArrayList<String> headings = this.layout.getHeadings();
        int counter = 1;
        
        for (int i = 0; i < headings.size(); i++) {
            
            if(counter == 4) {counter = 1;}
            
            final CheckBox checkBox = new CheckBox(headings.get(i));
            
            //final Tooltip tooltip = new Tooltip("$ tooltip");
            //tooltip.setFont(new Font("Arial", 16));
            checkBox.setStyle("-fx-border-color: lightblue; " + "-fx-font-size: 20;" + "-fx-border-insets: -5; " + "-fx-border-radius: 5;" + "-fx-border-style: dotted;" + "-fx-border-width: 2;");
            //cb.setTooltip(tooltip);
            
            switch(counter) {
                case 1:
                    vboxLeft.getChildren().add(checkBox);
                    counter++;
                    if(i+1 == headings.size()) {vboxLeft.getChildren().add(btnAccept);}
                    break;
                case 2:
                    vboxMid.getChildren().add(checkBox);
                    counter++;
                    if(i+1 == headings.size()) {vboxMid.getChildren().add(btnAccept);}
                    break;
                case 3:
                    vboxRight.getChildren().add(checkBox);
                    counter++;
                    if(i+1 == headings.size()) {vboxRight.getChildren().add(btnAccept);}
                    break;
                default:
                    alert.setTitle("INFORMATION!");
                    alert.setHeaderText("Internal error occured - switch-case in test()");
                    alert.setContentText("Contact the creator of this program.");
                    alert.show();
            }
        }
        selectingStage.setScene(scene);
        selectingStage.show();
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
        System.out.println(this.userDataTable.getColumnIDby("Heading 5"));
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
    }
    
    /**
     * <b> GUI-Operation </b> <p>
     * The user will be encouraged to select the folder and enter the file name where the final table Excel file should be saved. <p>
     * @return The path to the final table Excel file
     */
    private String outputFileDialog() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        // Erzeugung eines neuen Frames mit dem Titel "Dateiauswahl"
        JFrame parentJFrame = new JFrame("Save as ..");
        // Wir setzen die Breite auf 450 und die Höhe 300 pixel
        parentJFrame.setSize(450,300);
        // Hole den ContentPane und füge diesem unseren JFileChooser hinzu      
        parentJFrame.getContentPane().add(fileChooser);
        // Wir lassen unseren Frame anzeigen
        parentJFrame.setVisible(true);
        
        int userSelection = fileChooser.showSaveDialog(parentJFrame);
        String finalTablePath = "";
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            finalTablePath = fileToSave.getAbsolutePath();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
        }
        return finalTablePath;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.fileReader = new FileReader("C:\\Test_Extraktor\\Internal\\Boulard_et_al._2018__polare_AM.xls", "C:\\Test_Extraktor\\Internal\\metadata.xml", "\\\\gruppende\\IV2.2\\Int\\WRMG\\Table_Extractor\\Layouts\\LO_Arne.csv");
        this.userDataTable = new UserDataTable(this.fileReader.readInUserDataTableHSSF());
        this.metaDataTable = new MetaDataTable(this.fileReader.readInMetaDataTable());
        this.layout = new Layout(this.fileReader.readInLayout(";"));
        (this.finalTable = new FinalTable()).setHeadings(this.layout.getHeadings());
        this.finalTable.addRow();

        System.out.println("initialize done :-)");
    }
    
}