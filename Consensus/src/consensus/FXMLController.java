/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consensus;

import java.nio.file.Files;
import java.nio.file.Paths;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
    ArrayList<String> globalSelectedHeadings = new ArrayList<>();
    ArrayList<String> globalNonSelectedHeadings = new ArrayList<>();
    @FXML private VBox splitPaneVBoxOne; @FXML private VBox splitPaneVBoxTwo; @FXML private VBox splitPaneVBoxThree; @FXML private VBox splitPaneVBoxFour; @FXML private VBox splitPaneVBoxFife;
    @FXML private HBox splitPaneHBox;
    @FXML private TableView tableView;
    @FXML private ListView listView;
    @FXML private Button btnImport;
    @FXML private Button btnExport;

    /**
     * <b> Operation </b> <p>
     * 
     * Export the FinalTable-object into a Excel file.
     */
    @FXML // mapped to "Export"-button in the main GUI
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

    /**
     * <b> Operation </b> <p>
     * Creating a GUI to select which layout headings should be displayed as Drag&Drop element. <p>
     * Select the headings of the final table which should be filled with payload from the userdata table. <p>
     * @since Release (1st July 2018)
     */
    @FXML // mapped to 'Import'-button in the main GUI
    private void selectLayoutHeadings() {
        Stage selectingStage = new Stage();
        selectingStage.initModality(Modality.APPLICATION_MODAL);
        
        HBox hboxContainer = new HBox();
        hboxContainer.setSpacing(10);
        
        VBox vboxLeft = new VBox(); VBox vboxMid = new VBox(); VBox vboxRight = new VBox();
        vboxLeft.setSpacing(10); vboxMid.setSpacing(10); vboxRight.setSpacing(10);
        
        hboxContainer.getChildren().addAll(vboxLeft, vboxMid, vboxRight);
        Scene scene = new Scene(hboxContainer);
        
        selectingStage.setTitle("Selecting headings from Layout");
        selectingStage.setWidth(900);
        selectingStage.setHeight(350);
        
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
                        this.globalSelectedHeadings.add(((CheckBox) vboxLeft.getChildren().get(i)).getText());
                    } else {this.globalNonSelectedHeadings.add(((CheckBox) vboxLeft.getChildren().get(i)).getText());}
                }
            }
            for (int i = 0; i < vboxMid.getChildren().size(); i++) {
                if(vboxMid.getChildren().get(i) instanceof Button) {/*do nothing*/} 
                else { //get the text of the heading
                    if(((CheckBox) vboxMid.getChildren().get(i)).isSelected() == true) {
                        selectedHeadings.add(((CheckBox) vboxMid.getChildren().get(i)).getText());
                        this.globalSelectedHeadings.add(((CheckBox) vboxMid.getChildren().get(i)).getText());
                    } else {this.globalNonSelectedHeadings.add(((CheckBox) vboxMid.getChildren().get(i)).getText());}
                }
            }
            for (int i = 0; i < vboxRight.getChildren().size(); i++) {
                if(vboxRight.getChildren().get(i) instanceof Button) {/*do nothing*/} 
                else { //get the text of the heading
                    if(((CheckBox) vboxRight.getChildren().get(i)).isSelected() == true) {
                        selectedHeadings.add(((CheckBox) vboxRight.getChildren().get(i)).getText());
                        this.globalSelectedHeadings.add(((CheckBox) vboxRight.getChildren().get(i)).getText());
                    } else {this.globalNonSelectedHeadings.add(((CheckBox) vboxRight.getChildren().get(i)).getText());}
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
        btnImport.setDisable(true);
        // only for showcase
    }
    
    private void hilfsMethode(ArrayList<String> labels, ArrayList<String> values) {

        for (int i = 0; i < labels.size(); i++) {
            ArrayList<String> list = new ArrayList<>();
            for (int finalTableRowCount = 0; finalTableRowCount < this.finalTable.getRowCount(); finalTableRowCount++) {
                if(finalTableRowCount == 0) {list.add(labels.get(i));}
                else {list.add(values.get(i));}
            } 
            System.out.println("Listenlänge: " + list.size());
            this.finalTable.setColumnAt(labels.get(i), list);
        }     
    }
    
    /**
     * <b> GUI-Operation </b> <p>
     * The user will be encouraged to allocate the metadata to the final table. <p>
     */
    @FXML // mapped to 'Next'-button in the main GUI
    private void allocateMetaData() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        
        HBox hboxContainer = new HBox();
        hboxContainer.setSpacing(10);
        
        VBox vboxLeft = new VBox(); vboxLeft.setSpacing(35); vboxLeft.setAlignment(Pos.CENTER);
        VBox vboxMid = new VBox(); vboxMid.setSpacing(25); vboxMid.setAlignment(Pos.CENTER);
        VBox vboxRight = new VBox(); vboxRight.setSpacing(25); vboxRight.setAlignment(Pos.CENTER);
        
        
        Button btnAccept = new Button("Accept"); btnAccept.setPrefSize(100, 50);
        Button btnReset = new Button("Reset"); btnReset.setPrefSize(100, 50);
        vboxRight.getChildren().addAll(btnAccept, btnReset);
        
        
        btnAccept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent action) {
                ArrayList<String> labelsOfTextField = new ArrayList<>();
                ArrayList<String> valuesOfTextField = new ArrayList<>();
                for(Node children : vboxLeft.getChildren()) {
                    if(children instanceof Label) {
                        labelsOfTextField.add(((Label) children).getText());
                    }
                }
                
                for (Node children : vboxMid.getChildren()) {
                    if(children instanceof TextField) {
                        valuesOfTextField.add(((TextField) children).getText());
                    }
                }
                hilfsMethode(labelsOfTextField, valuesOfTextField);
            }
        });
        
        hboxContainer.getChildren().addAll(vboxLeft, vboxMid, vboxRight);
        Scene scene = new Scene(hboxContainer);
        
        stage.setTitle("Allocate the meta data");
        stage.setWidth(550);
        stage.setHeight(550);
        
        this.globalNonSelectedHeadings.remove(7);
        
        for (String globalNonSelectedHeading : this.globalNonSelectedHeadings) {
            Label labelGlobalNonSelectedHeading = new Label(globalNonSelectedHeading);
            labelGlobalNonSelectedHeading.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 12));
            
            TextField textfieldGlobalNonSelectedHeading = new TextField(globalNonSelectedHeading);
            
            vboxLeft.getChildren().add(labelGlobalNonSelectedHeading);
            vboxMid.getChildren().add(textfieldGlobalNonSelectedHeading);
        }
        
        stage.setScene(scene);
        stage.show();
        this.btnExport.setDisable(false);
    }
    
    /**
     * Fill in the VBoxes in the SplitPane with the selected headings from 'selectUserData()'. <p>
     * Furthermore, the Drag&Drop functionality are given to the displayed elements 
     * ('selected Headings' = partial Layout-object headings and the headings from the userDataTable-object).
     * @param selectedHeadings represented by a one-dimensional String ArrayList
     */
    private void display(ArrayList<String> selectedHeadings) {
        this.globalSelectedHeadings = selectedHeadings;
        // only for show-case
        // add manually the metadata
        ArrayList<String> dummy = new ArrayList<>();
        for (int row = 0; row < this.userDataTable.getRowCount(); row++) {
            dummy.add("Boulard et al. 2018");
        } dummy.set(0, "Literature Citation");
        this.finalTable.setColumnAt(this.finalTable.getColumnCount() - 1, dummy);
        
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
                // rembember: final table and layout has the same order of headings
                // get the payload from a specified column in the userdata table
                ArrayList<String> finalTableColumn = new ArrayList<>(this.userDataTable.getPayloadColumnAt(db.getString()));
                // add the, via Drag&Drop, allocated heading to the payload column on top
                finalTableColumn.add(0, currentHeading);
                // put the column with userdata payload and heading from layout at a specified column in the final table
                this.finalTable.setColumnAt(this.layout.getColumnIDby(currentHeading), finalTableColumn);
                // let the source know whether the string was successfully
                // transferred and used
                boolean success = false;
                if(db.hasString()) {success = true;}
                // Create a new text on the layout heading button
                btnHeading.setText(btnHeading.getText() + "\n" + "[" + db.getString() + "]");
                // Update the tableView-object
                this.printTableView();
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
    }
    
    /**
     * <b> GUI-Operation </b> <p>
     * The user will be encouraged to select the folder and enter the file name where the final table Excel file should be saved. <p>
     * @return The path to the final table Excel file
     */
    private String outputFileDialog() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        JFrame parentJFrame = new JFrame("Save as ..");
        parentJFrame.setSize(450,300);     
        parentJFrame.getContentPane().add(fileChooser);
        parentJFrame.setVisible(true);
        
        int userSelection = fileChooser.showSaveDialog(parentJFrame);
        File fileToSave = new File("");
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            fileToSave = fileChooser.getSelectedFile();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
            parentJFrame.setVisible(false);
            parentJFrame.dispose();
            return fileToSave.getAbsolutePath();
        }
        parentJFrame.setVisible(false);
        parentJFrame.dispose();
        return fileToSave.getAbsolutePath();
    }
    
    /**
     * <b> GUI-Operation </b> <p>
     * This function manage the tableView-object. <p>
     * Source is the current finalTable-object.
     */
    private void printTableView() {
        
        this.tableView.getColumns().clear();
        this.tableView.getItems().clear();
        
        ObservableList<ObservableList> dataList = FXCollections.observableArrayList();

        for(int column = 0 ; column < this.finalTable.getColumnCount(); column++){
            //We are using non-property style for making dynamic table
            final int columnCopy = column;                
            TableColumn tableColumn = new TableColumn(this.finalTable.getHeadings().get(column));
            tableColumn.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                        return new SimpleStringProperty(param.getValue().get(columnCopy).toString());                        
                    }                    
            });
            this.tableView.getColumns().addAll(tableColumn); 
        }
        
        for (int row = 1; row < this.finalTable.getRowCount(); row++) {
            ObservableList<String> rowElements = FXCollections.observableArrayList();
            for(int column = 0 ; column < this.finalTable.getColumnCount(); column++){
                rowElements.add(this.finalTable.getTable().get(row).get(column));
            }
            dataList.add(rowElements);
        }
        this.tableView.setItems(dataList);  
    }
    
    /**
     * <b> GUI-Operation </b> <p>
     * Manage the list items at the right site of the GUI. <p>
     * These list items source is a folder with several .csv-files.
     */
    private void loadListView() {
        ObservableList<String> elementList = FXCollections.observableArrayList();
        
        try {
            Files.walk(Paths.get("\\\\gruppende\\IV2.2\\Int\\WRMG\\Table_Extractor\\Layouts\\")).filter(Files::isRegularFile).forEach(filePath ->{
                String name = "\\\\gruppende\\IV2.2\\Int\\WRMG\\Table_Extractor\\Layouts\\" + filePath.getFileName().toString();
                if(name.startsWith("\\\\gruppende\\IV2.2\\Int\\WRMG\\Table_Extractor\\Layouts\\LO")) {
                    elementList.add(filePath.getFileName().toString());
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error in 'loadListView()'");
            System.err.println("Error in 'loadListView()'");
        }
        listView.setItems(elementList);
        listView.refresh();
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.fileReader = new FileReader("C:\\Test_Extraktor\\Internal\\Boulard_et_al._2018__polare_AM.xls", "C:\\Test_Extraktor\\Internal\\metadata.xml", "\\\\gruppende\\IV2.2\\Int\\WRMG\\Table_Extractor\\Layouts\\LO_Arne.csv");
        this.userDataTable = new UserDataTable(this.fileReader.readInUserDataTableHSSF());
        this.metaDataTable = new MetaDataTable(this.fileReader.readInMetaDataTable());
        this.layout = new Layout(this.fileReader.readInLayout(";"));
        (this.finalTable = new FinalTable()).setHeadings(this.layout.getHeadings());
        this.finalTable.addRow();
        
        loadListView();

        System.out.println("initialize done :-)");
    }
    
}