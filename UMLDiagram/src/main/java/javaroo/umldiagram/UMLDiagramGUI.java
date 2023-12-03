package javaroo.umldiagram;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import static javaroo.umldiagram.UMLController.controllerInstance;

public class UMLDiagramGUI extends Application {
    private static final CountDownLatch launchLatch = new CountDownLatch(1);
    private static UMLDiagramGUI instance;
    private Stage primaryStage;
    //private UMLController controller;

    public static boolean isGUIRunning = false;

    public static UMLController getController() {
        return controllerInstance;
    }


    @Override
    public void start(Stage stage) throws IOException {
        instance = this;
        this.primaryStage = stage;

        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UMLCreator.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600); // Adjust size as needed

        // Get the controller
        controllerInstance = fxmlLoader.getController();

        // Further setup, e.g., passing dependencies to the controller
        // controller.setDependency(...);

        // Setup the stage
        Image appIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png")));
        stage.getIcons().add(appIcon);
        stage.setTitle("UML Diagram Builder");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.runLater(stage::hide));

        stage.setOnCloseRequest(event -> {
            showGUI(false);
        });

        // Do not show the stage immediately
//         stage.show();

        launchLatch.countDown();
    }

    public static void waitForInitialization() {
        try {
            launchLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static UMLDiagramGUI getInstance() {
        return instance;
    }

    public void showGUI(boolean show) {
        Platform.runLater(() -> {
            if (show) {
                primaryStage.show();
            } else {
                primaryStage.hide();
            }
        });
    }

    // Main method for launching the JavaFX application
    public static void main(String[] args) {
        launch(args);
    }
}
