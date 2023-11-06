package javaroo.umldiagram;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javaroo.cmd.UMLMenu;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

public class InitialController {
    @FXML
    public void cmd(ActionEvent actionEvent) {
        try {
            // Start the runMain2 task as a separate process
            runMain2Method();

            // After starting the task, switch to the new scene
            switchScene(actionEvent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runMain2Method() {
        Thread cliThread = new Thread(() -> {
            try {
                UMLMenu.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // Set the thread as a daemon thread.
        cliThread.setDaemon(true);
        cliThread.start();
    }


    private void switchScene(ActionEvent actionEvent) throws IOException {
        // Load the new scene from the FXML file
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UMLCreator.fxml")));
        // Create the scene with the loaded parent
        Scene newScene = new Scene(root);
        // Assuming the event source is a Node, get the current stage
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        // Set the new scene to the current stage
        currentStage.setScene(newScene);
    }
    @FXML
    void newScene(ActionEvent event) {
        try {
//            System.out.println(getClass().getResource("UMLCreator.fxml"));

            // Load the new scene from the FXML file
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UMLCreator.fxml")));
            // Create the scene with the loaded parent
            Scene newScene = new Scene(root);
            // Assuming the event source is a Node, get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            // Set the new scene to the current stage
            currentStage.setScene(newScene);
        } catch (IOException e) {
            // Handle exception, e.g., print stack trace or show an alert
            e.printStackTrace();
        }
    }
}