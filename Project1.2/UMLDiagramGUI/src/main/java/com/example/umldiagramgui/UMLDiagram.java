package com.example.umldiagramgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class UMLDiagram extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main.fxml")));
            stage.setTitle("UML Diagram Builder");
            Scene scene = new Scene(root, 300, 200);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("CSS.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();

            //stage.setScene(new Scene(root, 300, 200));
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}