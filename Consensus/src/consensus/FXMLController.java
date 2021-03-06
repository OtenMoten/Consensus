/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consensus;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
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
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
    private String currentPDF = "";
    private String currentXML = "";
    private String currentLayout = "";
    private String pathInternalFolder;
    private String pathLayoutFolder;
    @FXML
    private VBox splitPaneVBoxOne;
    @FXML
    private VBox splitPaneVBoxTwo;
    @FXML
    private VBox splitPaneVBoxThree;
    @FXML
    private VBox splitPaneVBoxFour;
    @FXML
    private VBox splitPaneVBoxFife;
    @FXML
    private HBox splitPaneHBox;
    @FXML
    private TableView tableView;
    @FXML
    private ListView listView;
    @FXML
    private Button btnImport;
    @FXML
    private Button btnExport;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnSeparate;

    /**
     * <b> Operation </b>
     * <p>
     *
     * Export the FinalTable-object into a Excel file.
     */
    @FXML // mapped to "Export"-button in the main GUI
    private void printFinalTable() {
        String finaTablePath = outputFileDialog();

        //Create blank workbook
        HSSFWorkbook finalWorkbook = new HSSFWorkbook();
        //Create a blank sheet
        HSSFSheet finalSpreadsheet = finalWorkbook.createSheet(finaTablePath.split("\\\\")[finaTablePath.split("\\\\").length - 1]);
        //Create row object
        HSSFRow finalRow;
        //This data needs to be written (Object[])
        Map< Integer, String[]> empinfo = new TreeMap<>();
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
        Set< Integer> keyid = empinfo.keySet();
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
            alert.setTitle("Information");
            alert.setHeaderText("Export finished");
            alert.setContentText(finaTablePath);
            alert.show();

            // Put the while-loop in a new task because else the GUI is not touchable durring the while-loop
            Runnable task = () -> {
                try {
                    while (alert.isShowing()) {
                        Thread.sleep(100);
                    }
                    Platform.exit();
                    System.exit(0);

                } catch (InterruptedException ex) {
                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            };
            new Thread(task).start();

        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Maybe your Excel file is open ..");
            System.out.println("Maybe your Excel file is open ..");
        }
    }

    /**
     * <b> Operation </b>
     * <p>
     * Creating a GUI to select which layout headings should be displayed as
     * Drag&Drop element.
     * <p>
     * Select the headings of the final table which should be filled with
     * payload from the userdata table.
     * <p>
     * @since Release (1st July 2018)
     */
    @FXML // mapped to 'Import'-button in the main GUI
    private void selectLayoutHeadings() {

        this.currentLayout = this.pathLayoutFolder + this.listView.getSelectionModel().getSelectedItem();
        this.listView.setDisable(true);
        this.fileReader = new FileReader(this.currentPDF, this.currentXML, this.currentLayout);
        this.userDataTable = new UserDataTable(this.fileReader.readInUserDataTableHSSF());
        this.metaDataTable = new MetaDataTable(this.fileReader.readInMetaDataTable());
        this.layout = new Layout(this.pathLayoutFolder + "checkboxes\\", this.fileReader.readInLayout(";"));
        (this.finalTable = new FinalTable()).setHeadings(this.layout.getHeadings());
        this.finalTable.addRow();

        Stage selectingStage = new Stage();
        selectingStage.initModality(Modality.APPLICATION_MODAL);

        HBox hboxContainer = new HBox();
        hboxContainer.setSpacing(10);

        VBox vboxLeft = new VBox();
        VBox vboxMid = new VBox();
        VBox vboxRight = new VBox();
        vboxLeft.setSpacing(10);
        vboxMid.setSpacing(10);
        vboxRight.setSpacing(10);

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
                if (vboxLeft.getChildren().get(i) instanceof Button) {/*do nothing*/
                } else //get the text of the heading
                 if (((CheckBox) vboxLeft.getChildren().get(i)).isSelected() == true) {
                        selectedHeadings.add(((CheckBox) vboxLeft.getChildren().get(i)).getText());
                        this.globalSelectedHeadings.add(((CheckBox) vboxLeft.getChildren().get(i)).getText());
                    } else {
                        this.globalNonSelectedHeadings.add(((CheckBox) vboxLeft.getChildren().get(i)).getText());
                    }
            }
            for (int i = 0; i < vboxMid.getChildren().size(); i++) {
                if (vboxMid.getChildren().get(i) instanceof Button) {/*do nothing*/
                } else //get the text of the heading
                 if (((CheckBox) vboxMid.getChildren().get(i)).isSelected() == true) {
                        selectedHeadings.add(((CheckBox) vboxMid.getChildren().get(i)).getText());
                        this.globalSelectedHeadings.add(((CheckBox) vboxMid.getChildren().get(i)).getText());
                    } else {
                        this.globalNonSelectedHeadings.add(((CheckBox) vboxMid.getChildren().get(i)).getText());
                    }
            }
            for (int i = 0; i < vboxRight.getChildren().size(); i++) {
                if (vboxRight.getChildren().get(i) instanceof Button) {/*do nothing*/
                } else //get the text of the heading
                 if (((CheckBox) vboxRight.getChildren().get(i)).isSelected() == true) {
                        selectedHeadings.add(((CheckBox) vboxRight.getChildren().get(i)).getText());
                        this.globalSelectedHeadings.add(((CheckBox) vboxRight.getChildren().get(i)).getText());
                    } else {
                        this.globalNonSelectedHeadings.add(((CheckBox) vboxRight.getChildren().get(i)).getText());
                    }
            }
            //if the Accept-Button is clicked then the selected headings will be displayed in the main GUI
            //next step is to apply the headings from the layout to the headings from the userdata table
            display(selectedHeadings);
            this.btnNext.setDisable(false);
            selectingStage.close();
        });

        // Appending metadata to the citation column in the final table
        String citationHeading = this.layout.getCitationColumn(); // replace through global mapped layout variable
        ArrayList<String> citationList = new ArrayList<>();
        citationList.add(citationHeading);
        for (int i = 1; i < this.userDataTable.getRowCount(); i++) {
            //citationList.add(this.metaDataTable.getRowAt(1).toString());
            citationList.add("Boulard et al. 2018");
        }
        this.finalTable.setColumnAt(citationHeading, citationList);

        final ArrayList<String> headings = this.layout.getHeadings();
        int counter = 1;

        for (int i = 0; i < headings.size(); i++) {

            if (counter == 4) {
                counter = 1;
            }
            final CheckBox checkBox = new CheckBox(headings.get(i));

            //final Tooltip tooltip = new Tooltip("$ tooltip");
            //tooltip.setFont(new Font("Arial", 16));
            checkBox.setStyle("-fx-border-color: lightblue; " + "-fx-font-size: 20;" + "-fx-border-insets: -5; " + "-fx-border-radius: 5;" + "-fx-border-style: dotted;" + "-fx-border-width: 2;");
            //cb.setTooltip(tooltip);

            switch (counter) {
                case 1:
                    if (!checkBox.getText().equals(citationHeading)) {
                        vboxLeft.getChildren().add(checkBox);
                    } // remove the citation checkbox
                    counter++;
                    if (i + 1 == headings.size()) {
                        vboxRight.getChildren().add(btnAccept);
                    }
                    break;
                case 2:
                    if (!checkBox.getText().equals(citationHeading)) {
                        vboxMid.getChildren().add(checkBox);
                    } // remove the citation checkbox
                    counter++;
                    if (i + 1 == headings.size()) {
                        vboxRight.getChildren().add(btnAccept);
                    }
                    break;
                case 3:
                    if (!checkBox.getText().equals(citationHeading)) {
                        vboxRight.getChildren().add(checkBox);
                    } // remove the citation checkbox
                    counter++;
                    if (i + 1 == headings.size()) {
                        vboxRight.getChildren().add(btnAccept);
                    }
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
    }

    /**
     * <b> Operation </b>
     * <p>
     * Separates '<' and '>'.
     * <p>
     * Put it in a new column beside.
     *
     * @since Release (1st July 2018)
     */
    @FXML // mapped to 'Separate < >'-button in the main GUI
    private void separateGreaterAndLess() {
        ArrayList<ArrayList<String>> copyOfFinalTable = this.finalTable.getTable();
        int iSaveColumn = -1;
        boolean greater = false;
        boolean less = false;

        for (int row = 0; row < this.finalTable.getRowCount(); row++) {
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                if (copyOfFinalTable.get(row).get(column).contains("<") || copyOfFinalTable.get(row).get(column).contains(">")) {
                    iSaveColumn = column;
                    break;
                }
            }
            if (iSaveColumn != -1) {
                break;
            }
        }
        this.finalTable.addColumn(iSaveColumn, "Detection");

        if (iSaveColumn != -1) {
            for (int row = 0; row < this.finalTable.getRowCount(); row++) {
                if (copyOfFinalTable.get(row).get(iSaveColumn + 1).contains("<")) {
                    if (copyOfFinalTable.get(row).get(iSaveColumn + 1).contains(">")) {
                        this.finalTable.setElementAt(row, iSaveColumn, ">");
                        this.finalTable.setElementAt(row, iSaveColumn + 1, copyOfFinalTable.get(row).get(iSaveColumn).replace(">", ""));
                    }
                    this.finalTable.setElementAt(row, iSaveColumn, "<");
                    this.finalTable.setElementAt(row, iSaveColumn + 1, copyOfFinalTable.get(row).get(iSaveColumn + 1).replace("<", ""));
                } else if (row > 0) {
                    this.finalTable.setElementAt(row, iSaveColumn, "=");
                }
            }
        }
        this.btnSeparate.setDisable(true);
        printTableView();
    }

    /**
     * <b> GUI-Operation </b>
     * <p>
     * The user will be encouraged to allocate the metadata to the final table.
     * <p>
     */
    @FXML // mapped to 'Next'-button in the main GUI
    private void allocateMetaData() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        HBox hboxContainer = new HBox();
        hboxContainer.setSpacing(10);

        VBox vboxLeft = new VBox();
        vboxLeft.setSpacing(35);
        vboxLeft.setAlignment(Pos.CENTER);
        VBox vboxMid = new VBox();
        vboxMid.setSpacing(25);
        vboxMid.setAlignment(Pos.CENTER);
        VBox vboxRight = new VBox();
        vboxRight.setSpacing(25);
        vboxRight.setAlignment(Pos.CENTER);

        Button btnAccept = new Button("Accept");
        btnAccept.setPrefSize(100, 50);
        Button btnReset = new Button("Reset");
        btnReset.setPrefSize(100, 50);
        vboxRight.getChildren().addAll(btnAccept, btnReset);

        btnAccept.setOnAction((ActionEvent action) -> {
            ArrayList<String> labelsOfTextField = new ArrayList<>();
            ArrayList<String> valuesOfTextField = new ArrayList<>();
            vboxLeft.getChildren().stream().filter((children) -> (children instanceof Label)).forEach((Node children) -> {
                labelsOfTextField.add(((Label) children).getText());
            });

            vboxMid.getChildren().stream().filter((children) -> (children instanceof TextField)).forEach((Node children) -> {
                valuesOfTextField.add(((TextField) children).getText());
            });
            vboxMid.getChildren().stream().filter((children) -> (children instanceof ComboBox)).forEach((Node children) -> {
                valuesOfTextField.add(((ComboBox) children).getSelectionModel().getSelectedItem().toString());

            });
            fillSpaces(labelsOfTextField, valuesOfTextField);
            printTableView();
            this.btnSeparate.setDisable(false);
            stage.close();
        });

        hboxContainer.getChildren().addAll(vboxLeft, vboxMid, vboxRight);
        Scene scene = new Scene(hboxContainer);

        stage.setTitle("Allocate the meta data");
        stage.setWidth(550);
        stage.setHeight(550);

        this.globalNonSelectedHeadings.stream().forEach((String globalNonSelectedHeading) -> {
            String textString = "- Enter value here -";
            Label labelGlobalNonSelectedHeading = new Label(globalNonSelectedHeading);
            labelGlobalNonSelectedHeading.setFont(Font.font("Verdana", FontWeight.THIN, 12));

            final TextField textfieldGlobalNonSelectedHeading = new TextField();
            ObservableList<String> options = FXCollections.observableArrayList();
            final ComboBox comboBox = new ComboBox(options);
            Boolean trigger = false; // default is false => TextField

            for (String comboboxString : this.layout.getComboBoxes()) {
                if (comboboxString.equals(globalNonSelectedHeading)) {
                    options.addAll(this.layout.getComboBoxPayload(comboboxString)); // add files to the ComboBox
                    trigger = true;
                } else {
                    textfieldGlobalNonSelectedHeading.setText(textString);
                }
            }

            // Remove the placeholder-text in the TextField when clicked in
            textfieldGlobalNonSelectedHeading.setOnMouseClicked((MouseEvent event) -> {
                if (textfieldGlobalNonSelectedHeading.getText().equals(textString)) {
                    textfieldGlobalNonSelectedHeading.setText("");
                }
            });

            if (trigger == true) { // if true => checkbox will added
                vboxMid.getChildren().add(comboBox);
            } else { // if false =>
                vboxMid.getChildren().add(textfieldGlobalNonSelectedHeading);
            }
            vboxLeft.getChildren().add(labelGlobalNonSelectedHeading);
        });
        stage.setScene(scene);
        stage.show();
    }

    /**
     * <b> GUI-Operation </b>
     * <p>
     * Fill in the empty (non-allocated) columns with user-entered values.
     *
     * @param labels the headings of the non-allocated columns represented by a
     * one-dimensional String ArrayList
     * @param values the payload of the non-allocated columns represented by a
     * one-dimensional String ArrayList
     */
    private void fillSpaces(ArrayList<String> labels, ArrayList<String> values) {
        this.btnExport.setDisable(false);
        this.btnNext.setDisable(true);

        for (int i = 0; i < labels.size(); i++) {
            ArrayList<String> list = new ArrayList<>();
            for (int finalTableRowCount = 0; finalTableRowCount < this.finalTable.getRowCount(); finalTableRowCount++) {
                if (finalTableRowCount == 0) {
                    list.add(labels.get(i));
                } else {
                    list.add(values.get(i));
                }
            }
            this.finalTable.setColumnAt(labels.get(i), list);
        }
    }

    /**
     * <b> GUI-Operation </b>
     * <p>
     * Fill in the VBoxes in the SplitPane with the selected headings from
     * 'selectUserData()'.
     * <p>
     * Furthermore, the Drag&Drop functionality are given to the displayed
     * elements ('selected Headings' = partial Layout-object headings and the
     * headings from the userDataTable-object).
     *
     * @param selectedHeadings represented by a one-dimensional String ArrayList
     */
    private void display(ArrayList<String> selectedHeadings) {

        int columnCount = selectedHeadings.size();
        int counter = 0;

        for (int column = 0; column < columnCount; column++) {

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
                if (event.getGestureSource() != btnHeading && event.getDragboard().hasString()) {
                    /* allow for moving */ event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            btnHeading.setOnDragEntered((DragEvent event) -> {
                // the drag-and-drop gesture entered the target
                // show to the user that it is an actual gesture target
                if (event.getGestureSource() != btnHeading && event.getDragboard().hasString()) {
                    btnHeading.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 14));
                }
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
                if (db.hasString()) {
                    success = true;
                }
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
     * <b> GUI-Operation </b>
     * <p>
     * The user will be encouraged to select the folder and enter the file name
     * where the final table Excel file should be saved.
     * <p>
     * @return The path to the final table Excel file
     */
    private String outputFileDialog() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        JFrame parentJFrame = new JFrame("Save as ..");
        parentJFrame.setSize(450, 300);
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
     * <b> GUI-Operation </b>
     * <p>
     * This function manage the tableView-object.
     * <p>
     * Source is the current finalTable-object.
     */
    private void printTableView() {

        this.tableView.getColumns().clear();
        this.tableView.getItems().clear();

        ObservableList<ObservableList> dataList = FXCollections.observableArrayList();

        for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
            //We are using non-property style for making dynamic table
            final int columnCopy = column;
            TableColumn tableColumn = new TableColumn(this.finalTable.getHeadings().get(column));
            tableColumn.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(columnCopy).toString());
                }
            });
            this.tableView.getColumns().addAll(tableColumn);
        }

        for (int row = 1; row < this.finalTable.getRowCount(); row++) {
            ObservableList<String> rowElements = FXCollections.observableArrayList();
            for (int column = 0; column < this.finalTable.getColumnCount(); column++) {
                rowElements.add(this.finalTable.getTable().get(row).get(column));
            }
            dataList.add(rowElements);
        }
        this.tableView.setItems(dataList);
    }

    /**
     * <b> GUI-Operation </b>
     * <p>
     * Manage the list items at the right site of the GUI.
     * <p>
     * These list items source is a folder with several .csv-files.
     */
    private void loadListView() {
        ObservableList<String> elementList = FXCollections.observableArrayList();

        try {
            Files.walk(Paths.get(this.pathLayoutFolder)).filter(Files::isRegularFile).forEach(filePath -> {
                String name = this.pathLayoutFolder + filePath.getFileName().toString();
                System.out.println(name);
                if (name.startsWith("LO")) {
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

    /**
     * <b> GUI-Operation </b>
     * <p>
     * This function disable the 'Import'-button in the main GUI.
     * <p>
     */
    private void disableImportButton() {
        this.btnImport.setDisable(false);
    }

    private void testUserDataTable() {
        System.out.println("UserDataTable Class Test");

        System.out.println("Native userdata table");
        for (int row = 0; row < this.userDataTable.getRowCount(); row++) {
            for (int column = 0; column < this.userDataTable.getColumnCount(); column++) {
                if (row == 0) {
                    System.out.print(this.userDataTable.getTable().get(row).get(column) + " \t ");
                } else {
                    System.out.print(this.userDataTable.getTable().get(row).get(column) + " \t\t ");
                }
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("Getting a area");
        for (int row = 0; row < this.userDataTable.getArea(1, 1, 3, 4).size(); row++) {
            for (int column = 0; column < this.userDataTable.getArea(1, 1, 3, 4).get(0).size(); column++) {
                System.out.print(this.userDataTable.getArea(1, 1, 3, 4).get(row).get(column) + " \t\t ");
            }
            System.out.println();
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
        for (int row = 0; row < this.userDataTable.getRowCount() - 1; row++) {
            for (int column = 0; column < this.userDataTable.getColumnCount(); column++) {
                System.out.println(this.userDataTable.getPayload().get(row).get(column));
            }
            System.out.println();
        }
    }

    private void testMetaDataTable() {
        System.out.println("MetaDataTable Class Test");

        System.out.println("Native metadata table");
        for (int row = 0; row < this.metaDataTable.getRowCount(); row++) {
            for (int column = 0; column < this.metaDataTable.getColumnCount(); column++) {
                if (row == 0) {
                    System.out.print(this.metaDataTable.getTable().get(row).get(column) + " \t " + "| | |");
                } else {
                    System.out.print(this.metaDataTable.getTable().get(row).get(column) + " \t " + "| | |");
                }
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("Getting a area");
        for (int row = 0; row < this.metaDataTable.getArea(0, 2, 1, 4).size(); row++) {
            for (int column = 0; column < this.metaDataTable.getArea(0, 2, 1, 4).get(0).size(); column++) {
                System.out.print(this.metaDataTable.getArea(0, 2, 1, 4).get(row).get(column) + " \t\t ");
            }
            System.out.println();
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

    public void initialize(String pathLayoutFolder, String pathInternalFolder) {
        this.pathInternalFolder = pathInternalFolder;
        this.pathLayoutFolder = pathLayoutFolder;

        ObservableList<String> elementList = FXCollections.observableArrayList();
        try {

            Files.walk(Paths.get(this.pathInternalFolder)).filter(Files::isRegularFile).forEach(filePath -> {
                if (filePath.getFileName().toString().endsWith(".xls")) { // Only one file with '.xls' ending should be in the internal folder.
                    this.currentPDF = this.pathInternalFolder + filePath.getFileName().toString();
                    alert.setTitle("Test!");
                    alert.setHeaderText("");
                    alert.setContentText(this.currentPDF);
                    alert.show();
                    System.out.println(filePath.getFileName().toString());
                }

                if (filePath.getFileName().toString().endsWith(".xml")) { // Only one file with '.xml' ending should be in the internal folder.
                    this.currentXML = this.pathInternalFolder + filePath.getFileName().toString();
                    alert.setTitle("Test!");
                    alert.setHeaderText("");
                    alert.setContentText(this.currentXML);
                    alert.show();
                    System.out.println(filePath.getFileName().toString());
                }
                
            });
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error in 'initialize()'");
            System.err.println("Error in 'initialize()'");
        }

        try {
            Files.walk(Paths.get(this.pathLayoutFolder)).filter(Files::isRegularFile).forEach(filePath -> {
                String name = this.pathLayoutFolder + filePath.getFileName().toString();
                if (filePath.getFileName().toString().startsWith("Layout_")) {
                    elementList.add(filePath.getFileName().toString());
                }

            });
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error in 'loadListView()'");
            System.err.println("Error in 'loadListView()'");
        }

        this.listView.setItems(elementList);
        this.listView.refresh();

        this.listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                disableImportButton();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
