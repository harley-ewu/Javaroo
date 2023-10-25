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
            // Detect the operating system from system properties
            String os = System.getProperty("os.name").toLowerCase();

            // Command to execute, will be set based on the operating system
            String[] command;

            // Getting the directory where the Java program is currently running
            String currentWorkingDirectory = System.getProperty("user.dir");

            // Define the gradlew path based on the current working directory
            String gradlewPath = Paths.get(currentWorkingDirectory, "gradlew").toString();

            // Here's where we define which Gradle task to run. In this example, I'm defaulting to "run"
            // which typically runs the main method in a project's main class.
            String gradleCommand = "run"; // This will run Main1 by default
            // Uncomment the next line if you want to run Main2 instead
            gradleCommand = "runMain2";

            if (os.contains("win")) {
                // Windows
                command = new String[]{"cmd.exe", "/c", "cd \"" + currentWorkingDirectory + "\" && " + gradlewPath + " " + gradleCommand + " --console=plain"};
            } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                // Unix or Linux
                command = new String[]{"/bin/bash", "-c", "cd '" + currentWorkingDirectory + "'; '" + gradlewPath + "' " + gradleCommand + " --console=plain"};
            } else if (os.contains("mac")) {
                // macOS
                // Note: This assumes that Terminal will be used to run the command. Adjustments may be needed for other terminal applications.
                String script = String.format("tell application \"Terminal\" to do script \"cd '%s'; '%s' %s --console=plain\"", currentWorkingDirectory, gradlewPath, gradleCommand);
                command = new String[]{"osascript", "-e", script};
            } else {
                throw new IOException("Unsupported operating system");
            }

            // Print the command for debugging purposes
            System.out.println("Executing: " + String.join(" ", command));

            // Execute the command
            new ProcessBuilder(command).inheritIO().start();


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