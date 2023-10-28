package javaroo.umldiagram.controller;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javaroo.umldiagram.model.UMLClassModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassTemplateController {
    @FXML
    private Label attributeLabel;

    @FXML
    private AnchorPane attributesContainer;

    @FXML
    private AnchorPane classContainer;


    @FXML
    private AnchorPane classNameContainer;

    @FXML
    private Label classNameLabel;

    @FXML
    private VBox classVbox;

    @FXML
    private Label methodLabel;

    @FXML
    private AnchorPane relContainer;

    @FXML
    private Line separatorLineTwo;

    @FXML
    private Line seperatorLineOne;
    @FXML
    private TextField classNameTextField;


    @FXML
    private TextField att1;

    @FXML
    private TextField att2;

    @FXML
    private TextField att3;

    @FXML
    private TextField att4;

    @FXML
    private TextField att5;

    public boolean isClassNameEditable =false;
    public boolean isAttrEditable =false;
    public boolean getIsClassNameEditable() {
        return isClassNameEditable;
    }
    public void setIsClassNameEditable(boolean value) {
        this.isClassNameEditable = value;
    }
    public boolean getIsAttrEditable() {
        return isAttrEditable;
    }
    public void setIsAttrEditable(boolean value) {
        this.isClassNameEditable = value;
    }







    public void initialize() {
        Insets padding = new Insets(30,0,0,30);
        relContainer.insetsProperty();
        // Add a double-click event handler
        classNameTextField.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                classNameTextField.setEditable(isClassNameEditable);
                classNameTextField.requestFocus(); // Set focus to the text field
            }
        });
    }
    public void onTextFieldEnter() {
        // When the user presses Enter, make the text field non-editable
        classNameTextField.setEditable(true);
        double preferredHeight = attributesContainer.getChildren().size() * 30.0; // Adjust the height per child as needed

    }




    public void fillClassName(KeyEvent keyEvent) {
        System.out.println("key pressed");
    }

    private double labelHeightAttr = 0.0; // Initial label height
    private double labelHeightMehod = 0.0; // Initial label height

    @FXML
    public void addAttributes(String rel, String toClass) {
        System.out.println(rel + " " + toClass);
    }
private double labelHeightMethod  =30;
    @FXML
    public void addRel(ActionEvent actionEvent) {
        // Create a new AnchorPane containing a TextField and a Button
        AnchorPane newAnchorPane = new AnchorPane();
        newAnchorPane.setPrefHeight(36.0);
        newAnchorPane.setPrefWidth(334.0);



        AnchorPane newAnchorPane2 = new AnchorPane();
        newAnchorPane2.setPrefHeight(36.0);
        newAnchorPane2.setPrefWidth(334.0);



        TextField textField = new TextField();
        textField.setPromptText("Enter relation type and destination");
        textField.setPrefHeight(26.0);
        textField.setPrefWidth(334.0);
        textField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;-fx-padding: 5 10 5 10;");

        Button deleteButton = new Button("-");
        deleteButton.setLayoutX(289.0);
        deleteButton.setLayoutY(1.0);
        deleteButton.setMnemonicParsing(false);
        deleteButton.setPrefHeight(24.0);
        deleteButton.setPrefWidth(45.0);
        deleteButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 5 10 5 10;"); // Add an event handler to the delete button if needed
        deleteButton.setStyle("-fx-font-size:20px");
        deleteButton.setOnAction(e -> {
            // Get the parent of deleteButton (which should be newAnchorPane)
            AnchorPane parentAnchorPane = (AnchorPane) deleteButton.getParent();

            // Get the container (e.g., relContainer) and remove the newAnchorPane from it
            if (parentAnchorPane != null && relContainer != null) {
                relContainer.getChildren().remove(parentAnchorPane);
            }
        });

        // Add the TextField and Button to the AnchorPane
        newAnchorPane.getChildren().addAll(textField, deleteButton);

        // Calculate the new AnchorPane's position
        AnchorPane.setTopAnchor(newAnchorPane, labelHeightMehod);
        AnchorPane.setLeftAnchor(newAnchorPane, 2.0);

        // Add the new AnchorPane to the relContainer
        relContainer.getChildren().add(newAnchorPane);

        // Update the height for the next AnchorPane
        labelHeightMehod += 15.0;

        // Update the preferred height of the relContainer (if needed)
        // relContainer.setPrefHeight(relContainer.getPrefHeight() + 30.0);

        // Update the preferred height of the classContainer (if needed)
    }

    @FXML
    private void deleteRel(ActionEvent e) {

    }


    @FXML
    public void saveFile(ActionEvent actionEvent) {
    }
}