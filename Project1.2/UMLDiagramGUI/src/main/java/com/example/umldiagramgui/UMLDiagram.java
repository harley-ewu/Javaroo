package com.example.umldiagramgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class UMLDiagram extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            stage.setTitle("UML Diagram Builder");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}