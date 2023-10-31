package javaroo.umldiagram.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javaroo.cmd.UMLClass;
import javaroo.cmd.UMLDiagram;
import javaroo.cmd.UMLRelationships;
import javaroo.cmd.UMLSaveLoad;

import java.util.Optional;

public class UMLController {

    @FXML
    private Button addClassButton;

    @FXML
    private Button addRelButton;

    @FXML
    private VBox attributes;

    @FXML
    private Label className;

    @FXML
    private Button deleteClassButton;

    @FXML
    private Button deleteRelButton;

    @FXML
    private VBox methods;

    @FXML
    private Button renameAttrButton;

    @FXML
    private Button renameClassButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox umlClass;

    @FXML
    void addClassGui(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Class");
        dialog.setHeaderText("Enter the name for the new class:");
        dialog.setContentText("Class Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(className -> {
            try {
                UMLClass.addClass(className);
                // Display success alert to the user
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Class '" + className + "' successfully added!");
                successAlert.showAndWait();
            } catch (Exception e) {
                // Handle any exceptions or issues
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Add Class Error");
                errorAlert.setContentText("There was an error while adding the class. Please try again.");
                errorAlert.showAndWait();
            }
        });
    }

    @FXML
    void addRelationshipGui(ActionEvent event) {
        // Prompt for source class name
        TextInputDialog sourceDialog = new TextInputDialog();
        sourceDialog.setTitle("Add Relationship");
        sourceDialog.setHeaderText("Enter the source class name:");
        sourceDialog.setContentText("Source Class Name:");

        Optional<String> sourceResult = sourceDialog.showAndWait();

        if (!sourceResult.isPresent()) {
            return; // Exit if the user cancels
        }
        String sourceClassName = sourceResult.get();

        // Prompt for destination class name
        TextInputDialog destinationDialog = new TextInputDialog();
        destinationDialog.setTitle("Add Relationship");
        destinationDialog.setHeaderText("Enter the destination class name:");
        destinationDialog.setContentText("Destination Class Name:");

        Optional<String> destinationResult = destinationDialog.showAndWait();

        if (!destinationResult.isPresent()) {
            return; // Exit if the user cancels
        }
        String destinationClassName = destinationResult.get();

        // Existing logic
        UMLClass source = UMLDiagram.classExists(sourceClassName);
        UMLClass destination = UMLDiagram.classExists(destinationClassName);
        if (source != null && destination != null) {
            UMLRelationships.addRelationship(source, destination);

            // Success Alert
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Relationship successfully established between " + sourceClassName + " and " + destinationClassName + "!");
            successAlert.showAndWait();
        } else {
            // Failure Alert
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Relationship Error");
            errorAlert.setContentText("One or both classes do not exist.");
            errorAlert.showAndWait();
        }
    }



    @FXML
    void cloaseDiagramDui(ActionEvent event) {

    }

    @FXML
    void deleteClassGui(ActionEvent event) {

    }

    @FXML
    void deleteRelationshipGui(ActionEvent event) {

    }

    @FXML
    void loadDiagramGui(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Load Diagram");
        dialog.setHeaderText("Enter the filename for the diagram:");
        dialog.setContentText("Filename:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            try {
                // Use the provided name as the filename
                UMLSaveLoad.loadData(name);

                // Display success alert to the user
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Diagram loaded successfully from " + name + "!");
                successAlert.showAndWait();
            } catch (Exception e) {
                // Handle any exceptions during the save process
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Load Error");
                errorAlert.setContentText("There was an error while loading the diagram. Please try again.");
                errorAlert.showAndWait();
            }
        });
    }


    @FXML
    void renameAttributeGui(ActionEvent event) {

    }

    @FXML
    void renameClassGui(ActionEvent event) {

    }

    @FXML
    public void saveDiagramGui(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save Diagram");
        dialog.setHeaderText("Enter the filename for the diagram:");
        dialog.setContentText("Filename:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            try {
                // Use the provided name as the filename
                UMLSaveLoad.saveData(name);

                // Display success alert to the user
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Diagram successfully saved as " + name + "!");
                successAlert.showAndWait();
            } catch (Exception e) {
                // Handle any exceptions during the save process
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Save Error");
                errorAlert.setContentText("There was an error while saving the diagram. Please try again.");
                errorAlert.showAndWait();
            }
        });
    }

}

