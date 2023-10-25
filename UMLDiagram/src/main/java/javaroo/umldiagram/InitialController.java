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


import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

public class InitialController {
    @FXML
    public void cmd(ActionEvent actionEvent) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String[] command;

            // Get the current working directory
            String currentWorkingDirectory = System.getProperty("user.dir");

            String gradleCommand = "run"; // This will run Main1 by default
            // Uncomment the next line if you want to run Main2 instead
             gradleCommand = "runMain2";

            if (os.contains("win")) {
                // Windows: Start a new CMD window that executes gradlew
                command = new String[]{"cmd.exe", "/c", "start", "cmd.exe", "/K", currentWorkingDirectory + "\\gradlew.bat " + gradleCommand + " --console=plain"};
            } else if (os.contains("nix") || os.contains("nux")) {
                // Linux: Start a new gnome-terminal window that executes gradlew (for GNOME)
                command = new String[]{"gnome-terminal", "--", "/bin/sh", "-c", currentWorkingDirectory + "/gradlew " + gradleCommand + " --console=plain; bash"};
            } else if (os.contains("mac")) {
                // macOS: Start a new Terminal window that executes gradlew
                command = new String[]{"osascript", "-e", String.format("tell app \"Terminal\" to do script \"cd %s; ./gradlew %s --console=plain\"", currentWorkingDirectory, gradleCommand)};
            } else {
                throw new IOException("Unsupported operating system");
            }

            new ProcessBuilder(command)
                    .inheritIO()
                    .start();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void newScene(ActionEvent event) {
        try {
            // Load the new scene from the FXML file
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));

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