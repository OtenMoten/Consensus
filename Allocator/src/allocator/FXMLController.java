/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allocator;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
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
import javafx.util.Callback;

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
    
    /**
     * <b>Constructor</b> <p>
     * @since Release (1st July 2018)
     */

    private void selectUserData() {
        
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
            //display(selectedHeadings);
            
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
    
    @FXML
    private void readInAll() {
        
        ArrayList<String> list = new ArrayList<>(5);
        list.add("1");list.add("2");list.add("3");list.add("4");list.add("5");
        this.finalTable.addRow(list);
        
        for (int row = 0; row < this.finalTable.getRowCount(); row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                System.out.print(this.finalTable.getTable().get(row).get(column) + " \t");
            } System.out.println();
        }
        
        selectUserData();
        
    }
    
    private void display(ArrayList<String> selectedHeadings) {
        //save it into controller object data model array
        controller.setLayoutTable(arrayLayoutTable); 

        btnImport.setDisable(true);
        
        String[] layoutTableHeadings = arrayLayoutTable;
        
        int columnCount = controller.getDataTable()[0].length;
        int counter = 0;
        
        //Button headings for layout table
        for(int i = 0; i < layoutTableHeadings.length; i++) {
            
            layoutTableHeadings[i] = controller.getHeadingsLayoutTable()[i];
            
            Button layoutHeader = new Button(layoutTableHeadings[i]);
            layoutHeader.setTextAlignment(TextAlignment.CENTER);
            layoutHeader.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
            layoutHeader.setFocusTraversable(false);

            layoutHeader.setOnDragOver((DragEvent event) -> {
            /* data is dragged over the target
            * accept it only if it is not dragged from the same node
            * and if it has a string data
            */
            if (event.getGestureSource() != layoutHeader && event.getDragboard().hasString()) {
                    
                /* allow for moving */
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
            });
                
            layoutHeader.setOnDragEntered((DragEvent event) -> {
            // the drag-and-drop gesture entered the target
            // show to the user that it is an actual gesture target
            if (event.getGestureSource() != layoutHeader && event.getDragboard().hasString()) {
                layoutHeader.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 14));
            }
            event.consume();
            });
                
            layoutHeader.setOnDragExited((DragEvent event) -> {
            // mouse moved away, remove the graphical cues
            layoutHeader.setText(layoutHeader.getText());
            layoutHeader.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
            event.consume();
            });

            layoutHeader.setOnDragDropped((DragEvent event) -> {
            // data dropped
            // if there is a string data on dragboard, read it and use it
            Dragboard db = event.getDragboard();
            overwriteDataTableHeading(db.getString(), layoutHeader.getText());
            boolean success = false;
            if (db.hasString()) {
                layoutHeader.setText(layoutHeader.getText() + " \n[" + db.getString() + "]");
                success = true;
            }
            table.getColumns().clear();
            for(int k=0 ; k < controller.getDataTable()[0].length; k++){
                //We are using non property style for making dynamic table
                final int j = k;                
                TableColumn col = new TableColumn(controller.getDataTable()[0][k]);
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {                                                                                              
                        return new SimpleStringProperty(param.getValue().get(j).toString());                        
                    }                    
                });
                table.getColumns().addAll(col); 
            }
            
            // let the source know whether the string was successfully
            // transferred and used
            event.setDropCompleted(success);
            layoutHeader.setTextFill(Color.DEEPSKYBLUE);
            event.consume();
            });
            
            //hboxLayoutTable.getChildren().add(layoutHeader);
            
            switch (counter) {
                
                case 0:
                    vboxSplitPane1.getChildren().add(layoutHeader);
                    counter++;
                    break;
                    
                case 1:
                    vboxSplitPane2.getChildren().add(layoutHeader);
                    counter++;
                    break;
                    
                case 2:
                    vboxSplitPane3.getChildren().add(layoutHeader);
                    counter++;
                    break;
                    
                case 3:
                    vboxSplitPane4.getChildren().add(layoutHeader);
                    counter++;
                    break;
                    
                case 4:
                    vboxSplitPane5.getChildren().add(layoutHeader);
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
        controller.setLayoutTable(layoutTableHeadings);

        //Button headings for imported data table
        for (int j = 0; j < columnCount; j++) {

            Button dataTableHeader = new Button(controller.getDataTable()[0][j]);
            if("Author".equals(dataTableHeader.getText()) || 
                    "Year".equals(dataTableHeader.getText())  || 
                    "Title".equals(dataTableHeader.getText()) || 
                    "Pages".equals(dataTableHeader.getText()) || 
                    "Journal".equals(dataTableHeader.getText()))
            {dataTableHeader.setDisable(true);
            }
            dataTableHeader.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
            dataTableHeader.setFocusTraversable(false);
            
            dataTableHeader.setOnDragDetected((MouseEvent event) -> {
            // drag was detected, start a drag-and-drop gesture
            // allow any transfer mode
            Dragboard db = dataTableHeader.startDragAndDrop(TransferMode.ANY);
                
            // Put a string on a dragboard
            ClipboardContent content = new ClipboardContent();
            content.putString(dataTableHeader.getText());
            db.setContent(content);
            //dataTableHeader.setTextFill(Color.DEEPSKYBLUE);
            //dataTableHeader.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 14));
            event.consume();
            });
                
            dataTableHeader.setOnDragDone((DragEvent event) -> {
            // the drag and drop gesture ended
            // if the data was successfully moved, clear it
            if (event.getTransferMode() == TransferMode.MOVE) {
                dataTableHeader.setTextFill(Color.GREY);
                dataTableHeader.setFont(Font.font("Verdana", FontWeight.LIGHT, 12));
                dataTableHeader.setDisable(true);
            }
            event.consume();
            });
            
            hboxDataTable.getChildren().add(dataTableHeader);
        }
        //printTableView();
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
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.fileReader = new FileReader("userDataTable.xls", "metadata.xml", "layout.csv");
        this.userDataTable = new UserDataTable(this.fileReader.readInUserDataTableHSSF());
        this.metaDataTable = new MetaDataTable(this.fileReader.readInMetaDataTable());
        this.layout = new Layout(this.fileReader.readInLayout(";"));
        (this.finalTable = new FinalTable()).setHeadings(this.layout.getHeadings());
       
        System.out.println("initialize done :-)");
    }
    
}