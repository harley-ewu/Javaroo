package javaroo.gui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.nio.file.Paths;

public class MainController {

    @FXML
    protected void cmd(ActionEvent event) {
        try {
            // Detect the operating system from system properties
            String os = System.getProperty("os.name").toLowerCase();

            // Command to execute, will be set based on the operating system
            String[] command;

            // Getting the directory where the Java program is currently running
            String currentWorkingDirectory = System.getProperty("user.dir");

            // This is the target JAR file you want to execute; make sure to provide the correct name
            // Assuming the JAR file is in the current working directory
            String targetJarFile = "/build/libs/Project1.2-1.0-SNAPSHOT.jar";

            // Construct the absolute path to the JAR file, and escape the path to handle spaces
            String absoluteJarFilePath = Paths.get(currentWorkingDirectory, targetJarFile).toString().replace(" ", "\\ ");

            if (os.contains("win")) {
                // Windows
                // Quotes are used for the path to handle spaces on Windows
                command = new String[]{"cmd.exe", "/c", "start", "cmd.exe", "/K", "cd \"" + currentWorkingDirectory + "\" && java -jar \"" + absoluteJarFilePath.replace("\\ ", " ") + "\""};
            } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                // Unix or Linux
                // Quotes are not necessary here because we're escaping spaces
                command = new String[]{"/bin/bash", "-c", "cd \"" + currentWorkingDirectory + "\"; java -jar " + absoluteJarFilePath};
            } else if (os.contains("mac")) {
                // macOS
                // Use quotes for the AppleScript command to handle spaces
                String script = String.format("tell application \"Terminal\" to do script \"cd '%s'; java -jar '%s'\"", currentWorkingDirectory, absoluteJarFilePath.replace("\\ ", " "));
                command = new String[]{"osascript", "-e", script};
            } else {
                throw new IOException("Unsupported operating system");
            }

            // Execute the command
            new ProcessBuilder(command).inheritIO().start();
            //System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}