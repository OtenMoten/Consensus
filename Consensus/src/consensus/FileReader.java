/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consensus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
     * <b>Constructor</b> <p>
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
     * <b>Getter</b> <p>
     * @since Release (1st July 2018)
     * @return Path to the Excel file where the userdata table is within
     */
    public String getPathUserDataTable() {
        return this.pathUserDataTable;
    }
    /**
     * <b>Getter</b> <p>
     * @since Release (1st July 2018)
     * @return Path to the XML file where the metadata is within
     */
    public String getPathMetaDataTable() {
        return this.pathMetaDataTable;
    }
    /**
     * <b>Getter</b> <p>
     * @since Release (1st July 2018)
     * @return Path to the CSV file where the layout is within
     */
    public String getPathLayout() {
        return this.pathLayout;
    }
    
    /**
     * HSSF for .xls files >= Excel 97 & <= Excel 2005
     * @return The userdata Table from the Excel file as a two-dimensional String ArrayList
     */
    public ArrayList<ArrayList<String>> readInUserDataTableHSSF (/*, char floatingPointChar*/) {
        File excelFile = new File(this.pathUserDataTable);
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
        Alert alert = new Alert(Alert.AlertType.WARNING);
        
        try (FileInputStream excelFileInputStream = new FileInputStream(excelFile)) {
            workbook = new HSSFWorkbook(excelFileInputStream); //Create a virtual copy of the Excel file
            
            int lastSheetInWorkbook = workbook.getNumberOfSheets() - 1;
            spreadsheet = workbook.getSheetAt(lastSheetInWorkbook); //Create a virtual copy of the first spreadsheet in the Excel file
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
                        row.getCell(column).setCellValue(""); //Set the text of the cell
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
    /**
     * XSSF for .xls files > Excel 2005
     * @return The userdata table from the Excel file as a two-dimensional String ArrayList
     */
    public ArrayList<ArrayList<String>> readInUserDataTableXSSF (/*, char floatingPointChar*/) {
        File excelFile = new File(this.pathUserDataTable);
        ArrayList<ArrayList<String>> userDataTable = new ArrayList<>();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        //char floatingPoint = floatingPointChar; //Can be either a "." or a "," //Error handling in GUI
        XSSFWorkbook workbook;
        XSSFSheet spreadsheet;
        Iterator< Row > rowIterator;
        Iterator< Cell > cellIterator;
        XSSFRow row;
        Cell cell;
        int numberOfRows;
        int numberOfColumns;
        int rowNumber = 0;
        
        try (FileInputStream excelFileInputStream = new FileInputStream(excelFile)) {
            workbook = new XSSFWorkbook(excelFileInputStream); //Create a virtual copy of the Excel file
            
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
                row = (XSSFRow) rowIterator.next();
                
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
    /**
     * Transform the metadata within an XML file into a two-dimensional table.
     * @return The metadata from the XML file represented by two-dimensional String ArrayList
     */
    public ArrayList<ArrayList<String>> readInMetaDataTable() {
        ArrayList<ArrayList<String>> metaDataTable = new ArrayList<>();
        File xmlFile = new File(this.pathMetaDataTable);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        
        try {
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getChildNodes();
            
            for (int row = 0; row < 2; row++) {
                metaDataTable.add(new ArrayList<>());
                for (int column = 0; column < nodeList.getLength(); column++) {
                    Element elementOfNode = (Element) nodeList.item(column);
                    if(row == 0) {
                        metaDataTable.get(row).add(elementOfNode.getNodeName());
                    } else {
                        metaDataTable.get(row).add(elementOfNode.getTextContent());
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {}
        return metaDataTable;
    }
    /**
     * The seperator between each heading in the CSV file is configurable. <p>
     * @param csvSplitBy represented by a String
     * @return The layout and it's attributes as a two-dimensional String ArrayList. 0 = headings, 1 = citation column, 2 = checkboxes. 
     */
    public ArrayList<ArrayList<String>> readInLayout (String csvSplitBy) {
        BufferedReader bufferedReader;
        ArrayList<String> headings = new ArrayList<>(); // first row in the .CSV-files are read as a whole String
        ArrayList<String> citationColumn = new ArrayList<>();
        ArrayList<String> checkboxes = new ArrayList<>();
        String splitter = csvSplitBy;
        ArrayList<ArrayList<String>> layout = new ArrayList<>();
        
        try {
            bufferedReader = new BufferedReader(new java.io.FileReader(this.pathLayout));
            Iterator<String> iterator = bufferedReader.lines().iterator();
            // add headings
            headings.addAll(Arrays.asList(iterator.next().split(splitter)));
            layout.add(headings);
            // add citation column
            citationColumn.add(iterator.next());
            layout.add(citationColumn);
            // add checkboxes
            checkboxes.addAll(Arrays.asList(iterator.next().split(splitter)));
            layout.add(checkboxes);
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(FileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return layout;
    }
        
}   