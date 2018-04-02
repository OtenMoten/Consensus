/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allocator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author Kevin Ossenbrueck
 */
public class FileReader {
    
    /** The path to the .xls/.xlsx file where the userdata table is within.
     * @since Release (1st July 2018)
    */
    private final String pathUserDataTable;
    /** The path to the .XML file where the metadata is within.
     * @since Release (1st July 2018)
    */
    private final String pathMetaDataTable;
    /** The path to the CSV file where the layout is within.
     * @since Release (1st July 2018)
    */
    private final String pathLayout;
    
    /**
     * <b>Constructor</b> <br>
     * > Set up the paths to the files.
     *
     * @param pathToUserDataTable represents a String to a Excel file
     * @param pathToMetaData represents a String to a XML file
     * @param pathToLayout represents a String to a CSV file
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index > size())
     * @throws NullPointerException if the specified collection is null
     * @throws ClassCastException if the class of an element of this list is incompatible with the specified collection
     * @throws IllegalArgumentException if the endpoint indices are out of order (fromIndex > toInde
     * @throws ConcurrentModificationException if the list is structurally modified at any time after the iterator is created, in any way except through the iterator's own remove or add methods
     * @since Release (1st July 2018)
    */
    public FileReader(String pathToUserDataTable, String pathToMetaData, String pathToLayout) {
        this.pathUserDataTable = pathToUserDataTable;
        this.pathMetaDataTable = pathToMetaData;
        this.pathLayout = pathToLayout;
    }

    /**
     * <b>Getter</b> <br>
     * @since Release (1st July 2018)
     * @return Path to the Excel file where the userdata table is within
    */
    public String getPathUserDataTable() {
        return this.pathUserDataTable;
    }
    
    /**
     * HSSF for .xls files >= Excel 97 & <= Excel 2005
     * @param pathToUserDataTable represented by a String
     * @return The userdata Table from the Excel file as a two-dimensional String ArrayList
    */
    public ArrayList<ArrayList<String>> readInUserDataTable (String pathToUserDataTable/*, char floatingPointChar*/) {
        File excelFile = new File(pathToUserDataTable);
        //char floatingPoint = floatingPointChar; //Can be either a "." or a "," //Error handling in GUI
        HSSFWorkbook workbook;
        HSSFSheet spreadsheet;
        Iterator< Row > rowIterator;
        Iterator< Cell > cellIterator;
        HSSFRow row;
        Cell cell;
        ArrayList<ArrayList<String>> userDataTable = new ArrayList<>();
        int numberOfRows;
        int numberOfColumns;
        int rowNumber = 0;
        int cellNumber = 0;
        Alert alert = new Alert(Alert.AlertType.WARNING);
        
        try (FileInputStream excelFileInputStream = new FileInputStream(excelFile)) {
            workbook = new HSSFWorkbook(excelFileInputStream); //Create a virtual copy of the Excel file
            
            spreadsheet = workbook.getSheetAt(0); //Create a virtual copy of the first spreadsheet in the Excel file
            numberOfRows = spreadsheet.getPhysicalNumberOfRows();
            numberOfColumns = spreadsheet.getRow(0).getLastCellNum();
            userDataTable = new ArrayList<>(numberOfRows);
            
            //Create a virtual copy of each row within the spreadsheet and put it in a interator object called "rowIterator" 
            //rowIterator is needed for pointing on each row in a spreadsheet
            rowIterator = spreadsheet.iterator(); 

            //Execute the loop until hasNext() returns "false
            while (rowIterator.hasNext()) {
                userDataTable.add(new ArrayList<>());
                //"next()" puts the next row in "rowIterator"
                //every time the while-loop was executed
                row = (HSSFRow) rowIterator.next();
                
                //The following for-loop is needed to eliminate/uncover all "BLANK-cells" and fill them with Strings.
                //This loop is executed before the second while-loop below starts,
                //else "cellIterator" will ignore the BLANK-cells.
                for (int column = 0; column < numberOfColumns; column++) {
                    if(row.getCell(column, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) == null) { //Identify BLANK-cells
                        row.createCell(column, CellType.STRING); //Transform a BLANK-cell into a String-cell
                        row.getCell(column).setCellValue("BLANK"); //Set the text of the cell
                    }
                }
                
                cellIterator = row.cellIterator(); 
                while(cellIterator.hasNext()) { //Execute the loop until hasNext() returns "false"
                    
                    cell = cellIterator.next();
                    
                    CellType cellType = cell.getCellTypeEnum();
                    switch (cellType) {
                        case STRING:
                            userDataTable.get(rowNumber).add(cell.getStringCellValue());
                            break;
                            
                        case _NONE:
                            System.out.println("case NONE");
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue("NONE");
                            alert.setTitle("WARNING!");
                            alert.setHeaderText("CellType '_NONE' discoverd."); 
                            alert.setContentText("Cell will formated as String and text set to 'NONE'.");
                            alert.show();
                            break;
                            
                        case ERROR:
                            System.out.println("case ERROR");
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue("ERROR");
                            alert.setTitle("WARNING!");
                            alert.setHeaderText("CellType 'ERROR' discoverd."); 
                            alert.setContentText("Cell will formated as String and text set to 'ERROR'.");
                            alert.show();
                            break;
                            
                        case NUMERIC:
                            //if(floatingPoint == 44) //44 is ASCII ","
                            //if(floatingPoint == 46) {//46 is ASCII "."
                            userDataTable.get(rowNumber).add(String.valueOf(cell.getNumericCellValue()));
                            break;
                            
                        default:
                            System.out.println("DEFAULT");
                            alert.setTitle("WARNING!");
                            alert.setHeaderText("Internal error occured - [DEFAULT switch case region in readInUserDataTable()]");
                            alert.setContentText("Contact the creator of this program.");
                            alert.show();
                            break;
                    }
                }
                rowNumber++;
            }
            //Close the underlying file of the FileInputStream
            excelFileInputStream.close();
        } 
        catch (IOException ex) {
            Logger.getLogger(FileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userDataTable;
    }
    
}