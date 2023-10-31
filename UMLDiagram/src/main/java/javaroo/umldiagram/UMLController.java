package javaroo.umldiagram;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javaroo.cmd.*;

import java.util.ArrayList;
import java.util.List;
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
    private List<UMLClass> diagramClasses = new ArrayList<>();


    public void setClassName(Label className) {
        this.className = className;
    }
    @FXML
    void addClassGui(ActionEvent event) {
        TextInputDialog classDialog = new TextInputDialog();
        classDialog.setTitle("Add Class");
        classDialog.setHeaderText("Enter the name for the new class:");
        classDialog.setContentText("Class Name:");

        Optional<String> classResult = classDialog.showAndWait();
        classResult.ifPresent(className -> {
            if (diagramClasses.stream().anyMatch(c -> c.getName().equalsIgnoreCase(className))) {
                showAlert("Duplicate Class Error", "A class with the name '" + className + "' already exists.");
                return;
            }

            UMLClass newUMLClass = new UMLClass(className);
            diagramClasses.add(newUMLClass); // Add the new class to the model

            // Now, ask for attributes to add to this new class.
            while (true) {
                TextInputDialog attributeDialog = new TextInputDialog();
                attributeDialog.setTitle("Add Attribute");
                attributeDialog.setHeaderText("Enter an attribute name for " + className + " (or leave blank and press OK to finish):");
                attributeDialog.setContentText("Attribute Name:");

                Optional<String> attributeResult = attributeDialog.showAndWait();
                if (!attributeResult.isPresent() || attributeResult.get().isEmpty()) {
                    break; // Exit the loop if the user presses cancel or submits an empty name
                }

                String attributeName = attributeResult.get();
                if (newUMLClass.getAttributes().stream().noneMatch(a -> a.getName().equalsIgnoreCase(attributeName))) {
                    UMLAttributes.addAttribute(newUMLClass, attributeName); // Use the static method to add the attribute
                } else {
                    showAlert("Duplicate Attribute Error", "An attribute with the name '" + attributeName + "' already exists in class '" + className + "'.");
                }
            }

            updateDiagramView(); // Update the diagram view
            showAlert("Success", "Class '" + className + "' successfully added with its attributes!");
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



    private HBox umlClassVBox = new HBox(); // A VBox to hold the UMLClass visual representation.

// ...

    private void updateDiagramView() {
        // Clear existing diagram representation
        umlClassVBox.getChildren().clear();

        umlClassVBox.setSpacing(20);

        // Loop through all classes in the diagram and create their visual representation
        for (UMLClass umlClass : diagramClasses) {
            VBox classVBox = createClassVBox(umlClass);
            umlClassVBox.getChildren().add(classVBox);
        }

        // Set the updated diagram in the scroll pane
        scrollPane.setContent(umlClassVBox);
    }

    // This helper method creates a VBox for a UMLClass instance
    private VBox createClassVBox(UMLClass umlClass) {
        VBox classVBox = new VBox();
        classVBox.setPadding(new Insets(5));
        classVBox.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");

        Label classNameLabel = new Label(umlClass.getName());
        classNameLabel.setStyle("-fx-font-weight: bold;");
        classVBox.getChildren().add(classNameLabel);

        VBox attributesVBox = new VBox();
        for (UMLAttributes attribute : umlClass.getAttributes()) {
            Label attributeLabel = new Label("- " + attribute.toString()); // Assuming UMLAttributes has a proper toString() method
            attributesVBox.getChildren().add(attributeLabel);
        }
        classVBox.getChildren().add(attributesVBox);

        // Repeat similar logic for methods if required

        return classVBox;
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

