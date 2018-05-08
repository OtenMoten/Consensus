/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consensus;

import java.util.Arrays;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author Kevin
 */
public class Consensus extends Application {
    
    private static String[] arguments;
    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);
    
    @Override
    public void start(Stage rootStage) throws Exception {
        
        /*for(String arg:arguments) {
            System.out.println(arg);
        }*/

        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI.fxml"));
        BorderPane borderPane = loader.load();
        Scene rootScene = new Scene(borderPane);

        rootStage.setScene(rootScene);
        rootStage.show();
        
        FXMLController controller = loader.getController();
        controller.initialize(arguments[0] + "\\", arguments[1] + "\\");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {    
        arguments = args[0].split("%");
        launch(args);
    }
    
}
