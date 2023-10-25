package javaroo.umldiagram;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class UMLDiagramGUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UMLDiagramGUI.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        Image appIcon = new Image(Objects.requireNonNull(UMLDiagramGUI.class.getResourceAsStream("icon.png")));
        stage.getIcons().add(appIcon);
        stage.setTitle("UML Diagram Builder");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}