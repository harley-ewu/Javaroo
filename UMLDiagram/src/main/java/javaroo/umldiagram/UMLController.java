package javaroo.umldiagram;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javaroo.cmd.*;
import java.util.*;
import java.util.stream.Collectors;

public class UMLController {

    private final UMLView umlView;

    @FXML
    private Button addClassButton;

    @FXML
    private Button addFieldButton;

    @FXML
    private Button addMethodButton;

    @FXML
    private Button addRelButton;

    @FXML
    private Canvas centerContent;

    @FXML
    private ScrollPane centerScrollPane;

    @FXML
    private Button changeRelButton;

    @FXML
    private Button deleteClassButton;

    @FXML
    private Button deleteFieldButton;

    @FXML
    private Button deleteMethodButton;

    @FXML
    private Button deleteRelButton;

    @FXML
    private Button renameClassButton;

    @FXML
    private Button renameFieldButton;

    @FXML
    private Button renameMethodButton;

    public UMLController() {
        this.umlView = new UMLView(this);
    }


    // List to store the drawn UML classes
    List<UMLClass> drawnUMLClasses = new ArrayList<>();

    // List to store the drawn UML relationships
    final List<UMLRelationships> drawnUMLRelationships = new ArrayList();

    UMLDiagram diagram = new UMLDiagram();

    // Prompts the user to enter class name, fields, and methods
    @FXML
    void addClassGui(ActionEvent event) {
        try {
            TextInputDialog classNameDialog = new TextInputDialog();
            classNameDialog.setTitle("Add UML Class");
            classNameDialog.setHeaderText("Enter the class name:");
            classNameDialog.setContentText("Class Name:");

            Optional<String> classNameResult = classNameDialog.showAndWait();
            if (!classNameResult.isPresent() || classNameResult.get().isEmpty()) {
                // User canceled the input or provided an empty string for the class name
                return;
            }

            String className = classNameResult.get().trim(); // Trim the input to remove any leading or trailing whitespace

            // Check if the class already exists in the UML diagram
            if (diagram.classExists(className) != null) {
                showAlert("Error", "Class '" + className + "' already exists.");
                return;
            }

            // Create the UMLClass object
            UMLClass newUMLClass = new UMLClass(className);

            // Prompt the user for fields
            diagram.addClass(newUMLClass.getName());
            addFieldsToClass(newUMLClass);

            // Prompt the user for methods
            addMethodsToClass(newUMLClass);


            // Update the visual representation
            umlView.autoAssignCoordinatesGrid(newUMLClass); // This method should set the coordinates for the new class
            drawnUMLClasses.add(newUMLClass); // This should be a collection of UMLClass objects that are currently drawn
            umlView.drawUMLClass(newUMLClass);// This method should handle the actual drawing of the class on the canvas or pane

            showAlert("Success", "Class '" + className + "' added with fields and methods.");
        } catch (Exception e) {
            // Log the stack trace along with the message
            logException(e);
            // Optionally, show an error dialog to the user
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    private void logException(Exception e) {
        Throwable cause = e.getCause();
        if (cause != null) {
            // Log the cause
            System.err.println("An error occurred: " + cause);
            cause.printStackTrace();
        } else {
            // Log the original exception if cause is null
            System.err.println("An error occurred: " + e);
            e.printStackTrace();
        }
    }

    // Helper method to add fields to the class
    private void addFieldsToClass(UMLClass umlClass) {
        while (true) {
            TextInputDialog fieldDialog = new TextInputDialog();
            fieldDialog.setTitle("Add Field");
            fieldDialog.setHeaderText("Enter field details (or press Cancel to finish):");
            fieldDialog.setContentText("Field Name:");

            Optional<String> fieldNameResult = fieldDialog.showAndWait();
            if (!fieldNameResult.isPresent() || fieldNameResult.get().isEmpty()) {
                // User finished adding fields or canceled
                break;
            }
            String fieldName = fieldNameResult.get();

            fieldDialog.setContentText("Field Type:");
            Optional<String> fieldTypeResult = fieldDialog.showAndWait();
            if (!fieldTypeResult.isPresent() || fieldTypeResult.get().isEmpty()) {
                showAlert("Error", "Field type is required.");
                continue;
            }
            String fieldType = fieldTypeResult.get();

            fieldDialog.setContentText("Field Visibility (public or private):");
            Optional<String> fieldVisibilityResult = fieldDialog.showAndWait();
            if (!fieldVisibilityResult.isPresent() || fieldVisibilityResult.get().isEmpty()) {
                showAlert("Error", "Field visibility is required.");
                continue;
            }
            String fieldVisibility = fieldVisibilityResult.get();

            // Add the field to the class
            umlClass.addField(fieldName, fieldType, fieldVisibility);
        }
    }

    // Helper method to add methods to the class
    private void addMethodsToClass(UMLClass umlClass) {
        while (true) {
            TextInputDialog methodDialog = new TextInputDialog();
            methodDialog.setTitle("Add Method");
            methodDialog.setHeaderText("Enter method details (or press Cancel to finish):");
            methodDialog.setContentText("Method Name:");

            Optional<String> methodNameResult = methodDialog.showAndWait();
            if (!methodNameResult.isPresent() || methodNameResult.get().isEmpty()) {
                // User finished adding methods or canceled
                break;
            }
            String methodName = methodNameResult.get().trim(); // Trim the input

            methodDialog.setContentText("Method Return Type:");
            Optional<String> methodTypeResult = methodDialog.showAndWait();
            if (!methodTypeResult.isPresent() || methodTypeResult.get().isEmpty()) {
                showAlert("Error", "Method return type is required.");
                continue;
            }
            String methodType = methodTypeResult.get().trim(); // Trim the input

            methodDialog.setContentText("Method Parameters (e.g., int param1, String param2):");
            Optional<String> methodParamsResult = methodDialog.showAndWait();
            String methodParams = methodParamsResult.orElse("");

            // Split the parameters string into an ArrayList<String>
            ArrayList<String> paramsList = new ArrayList<>();
            if (!methodParams.isEmpty()) {
                String[] paramsArray = methodParams.split(",\\s*");
                Collections.addAll(paramsList, paramsArray);
            }

            // Add the method to the class
            diagram.classExists(umlClass.getName()).addMethod(methodName, methodType, paramsList);
        }
    }

    // Helper method to draw the created class and its contents on the gui in random spot
    // To be implemented
    @FXML
    void addClassFieldGui(ActionEvent event) {
        // Check if there are no classes in the list
        if (drawnUMLClasses.isEmpty()) {
            showAlert("Error", "No classes found. Please add a class first.");
            return;
        }

        // Create a choice dialog for selecting the class
        ChoiceDialog<String> classDialog = new ChoiceDialog<>(drawnUMLClasses.get(0).getName(), drawnUMLClasses.stream().map(UMLClass::getName).collect(Collectors.toList()));
        classDialog.setTitle("Select Class");
        classDialog.setHeaderText("Choose the class to add fields:");
        classDialog.setContentText("Class:");

        Optional<String> classNameResult = classDialog.showAndWait();
        if (!classNameResult.isPresent()) {
            // User canceled class selection
            return;
        }
        String className = classNameResult.get();

        // Find the selected class using the findUMLClass method
        UMLClass selectedClass = findUMLClass(className);

        if (selectedClass == null) {
            showAlert("Error", "Class not found."); // Handle the case where the class is not found
            return;
        }

        // Now proceed with adding fields to the selected class using the diagram
        while (true) {
            TextInputDialog fieldDialog = new TextInputDialog();
            fieldDialog.setTitle("Add Field");
            fieldDialog.setHeaderText("Enter field details (or press Cancel to finish):");
            fieldDialog.setContentText("Field Name:");

            Optional<String> fieldNameResult = fieldDialog.showAndWait();
            if (!fieldNameResult.isPresent() || fieldNameResult.get().isEmpty()) {
                // User finished adding fields or canceled
                break;
            }
            String fieldName = fieldNameResult.get();

            fieldDialog.setContentText("Field Type:");
            Optional<String> fieldTypeResult = fieldDialog.showAndWait();
            if (!fieldTypeResult.isPresent() || fieldTypeResult.get().isEmpty()) {
                showAlert("Error", "Field type is required.");
                continue;
            }
            String fieldType = fieldTypeResult.get();

            fieldDialog.setContentText("Field Visibility (public or private):");
            Optional<String> fieldVisibilityResult = fieldDialog.showAndWait();
            if (!fieldVisibilityResult.isPresent() || fieldVisibilityResult.get().isEmpty()) {
                showAlert("Error", "Field visibility is required.");
                continue;
            }
            String fieldVisibility = fieldVisibilityResult.get().toLowerCase(); // Convert to lowercase for case-insensitive comparison

            // Validate field visibility
            if (!fieldVisibility.equals("public") && !fieldVisibility.equals("private")) {
                showAlert("Error", "Invalid field visibility. Use 'public' or 'private'.");
                continue;
            }

            // Create the new field and add it to the selected class using the diagram
            selectedClass.addField(fieldName, fieldType, fieldVisibility);

            // Redraw the updated class on the canvas
            umlView.updateCanvas(selectedClass);
        }
    }

    // To be implemented
    @FXML
    void addClassMethodGui(ActionEvent event) {

    }

    @FXML
    void addRelationshipGui(ActionEvent event) {
        if (drawnUMLClasses.isEmpty()) {
            showAlert("Error", "No UML classes have been created to establish a relationship.");
            return;
        }

        // Create a ChoiceDialog to select the source class
        ChoiceDialog<UMLClass> sourceClassDialog = new ChoiceDialog<>(null, drawnUMLClasses);
        sourceClassDialog.setTitle("Add Relationship");
        sourceClassDialog.setHeaderText("Select the source class:");
        sourceClassDialog.setContentText("Source Class:");

        Optional<UMLClass> sourceClassResult = sourceClassDialog.showAndWait();
        if (!sourceClassResult.isPresent()) {
            return; // The user canceled the source class selection
        }

        UMLClass sourceClass = sourceClassResult.get();

        // Create a ChoiceDialog to select the destination class
        ChoiceDialog<UMLClass> destinationClassDialog = new ChoiceDialog<>(null, drawnUMLClasses);
        destinationClassDialog.setTitle("Add Relationship");
        destinationClassDialog.setHeaderText("Select the destination class:");
        destinationClassDialog.setContentText("Destination Class:");

        Optional<UMLClass> destinationClassResult = destinationClassDialog.showAndWait();
        if (!destinationClassResult.isPresent()) {
            return; // The user canceled the destination class selection
        }

        UMLClass destinationClass = destinationClassResult.get();

        if (destinationClass == null) {
            showAlert("Error", "Destination class '" + destinationClass.getName() + "' does not exist.");
            return;
        }

        if (sourceClass.equals(destinationClass)) {
            showAlert("Error", "Source and destination classes cannot be the same.");
            return;
        }

        // Check if a relationship already exists between the same classes in either direction
        if (relationshipExists(sourceClass, destinationClass) || relationshipExists(destinationClass, sourceClass)) {
            showAlert("Error", "A relationship already exists between '" + sourceClass.getName() + "' and '" + destinationClass.getName() + "'.");
            return;
        }

        // Prompt the user to select the type of relationship
        ChoiceDialog<UMLRelationships.RelationshipType> relationshipTypeDialog = new ChoiceDialog<>(UMLRelationships.RelationshipType.AGGREGATION);
        relationshipTypeDialog.setTitle("Add Relationship");
        relationshipTypeDialog.setHeaderText("Select the type of relationship:");
        relationshipTypeDialog.setContentText("Relationship Type:");

        // Populate the choice dialog with the available relationship types
        relationshipTypeDialog.getItems().addAll(UMLRelationships.RelationshipType.values());

        Optional<UMLRelationships.RelationshipType> relationshipTypeResult = relationshipTypeDialog.showAndWait();
        if (!relationshipTypeResult.isPresent()) {
            // The user canceled the relationship type selection
            return;
        }

        UMLRelationships.RelationshipType selectedRelationshipType = relationshipTypeResult.get();

        double startX = 0;
        double startY = 0;
        double endX = 0;
        double endY = 0;

        // Create the UMLRelationship object with the selected relationship type
        UMLRelationships relationship = new UMLRelationships(sourceClass, destinationClass, selectedRelationshipType);

        // Use the same information but for creating the relationship on the GUI
        UMLRelationships relationshipWithCoordinates = new UMLRelationships(sourceClass, destinationClass, selectedRelationshipType, startX, startY, endX, endY);

        // Now, draw the relationship on the canvas based on the type
        umlView.drawUMLRelationship(sourceClass, destinationClass, selectedRelationshipType);

        // Add the created relationship to the controller's list of relationships
        drawnUMLRelationships.add(relationshipWithCoordinates);

        // Add the created relationship to the diagram's list of relationships
        diagram.addRelationship(sourceClass, destinationClass, relationship.getType());

        // Notify the user of a successful relationship creation
        showAlert("Success", "Relationship created successfully: " + sourceClass.getName() + " " + selectedRelationshipType + " " + destinationClass.getName());
    }

    // Helper method for addRelationshipGui which checks for existing relationships
    private boolean relationshipExists(UMLClass sourceClass, UMLClass destinationClass) {
        for (UMLRelationships relationship : drawnUMLRelationships) {
            if (relationship.getSource().equals(sourceClass) && relationship.getDest().equals(destinationClass)) {
                return true;
            }
        }
        return false;
    }

    // Helper method to check for the existing class
    private UMLClass findUMLClass(String className) {
        for (UMLClass umlClass : drawnUMLClasses) {
            if (umlClass.getName().equalsIgnoreCase(className)) {
                return umlClass;
            }
        }
        return null; // Class not found
    }


    // Allows the user to close the program from the file dropdown tab
    @FXML
    void closeDiagramGui(ActionEvent event) {
        Platform.exit();
    }

    // Method to allow user to delete a class that is not in a relationship
    @FXML
    void deleteClassGui(ActionEvent event) {
        // Get all class names from the UML diagram
        List<String> classNames = new ArrayList<>(diagram.getClasses().keySet());

        // Create a new ChoiceDialog instance with the list of class names
        ChoiceDialog<String> dialog = new ChoiceDialog<>(null, classNames);
        dialog.setTitle("Delete Class");
        dialog.setHeaderText("Select a Class to Delete");
        dialog.setContentText("Class:");

        // Show the dialog and capture the result
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String className = result.get();
            UMLClass umlClass = diagram.classExists(className);

            if (umlClass != null) {
                // Check if the class is part of any relationship
                if (isClassInRelationship(umlClass)) {
                    showAlert("Error", "Class '" + className + "' is part of a relationship and cannot be deleted.");
                } else {
                    // Remove the class from the diagram
                    diagram.removeClass(className);

                    // Update the canvas by removing the specified class
                    umlView.updateCanvasRemoveClass(umlClass.getName());
                    showAlert("Success", "Class '" + className + "' has been removed.");
                }
            } else {
                showAlert("Error", "Class '" + className + "' does not exist.");
            }
        }
    }

    // Checks if the chosen class is in a relationship
    private boolean isClassInRelationship(UMLClass umlClass) {
        for (UMLRelationships relationship : drawnUMLRelationships) {
            if (relationship.getSource().getName().equals(umlClass.getName()) || relationship.getDest().getName().equals(umlClass.getName())) {
                return true;
            }
        }
        return false;
    }

    // Helper method to redraw the canvas to remove the selected class

    // Method to allow the user to delete any chosen existing relationship
    @FXML
    void deleteRelationshipGui(ActionEvent event) {
        if (drawnUMLRelationships.isEmpty()) {
            showAlert("Error", "No relationships have been created to delete.");
            return;
        }

        // Create a ChoiceDialog to select a relationship
        ChoiceDialog<String> dialog = new ChoiceDialog<>(null);
        dialog.setTitle("Delete Relationship");
        dialog.setHeaderText("Select a Relationship to Delete");
        dialog.setContentText("Relationship:");

        // Populate the dialog with the relationship labels
        for (UMLRelationships relationship : drawnUMLRelationships) {
            String relationshipLabel = relationship.getType() + " from " + relationship.getSource().getName() + " to " + relationship.getDest().getName();
            dialog.getItems().add(relationshipLabel);
        }

        // Show the dialog and capture the result
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(selectedRelationshipLabel -> {
            // Find the selected relationship
            UMLRelationships selectedRelationship = findRelationshipByLabel(selectedRelationshipLabel);

            if (selectedRelationship != null) {
                // Remove the relationship from the list
                drawnUMLRelationships.remove(selectedRelationship);

                // Clear the relationship from the canvas
                umlView.clearRelationshipFromCanvas(event, selectedRelationship);

                showAlert("Success", "Relationship has been removed: " + selectedRelationshipLabel);
            } else {
                showAlert("Error", "Failed to find the selected relationship.");
            }
        });
    }

    // Helper method to list the relationships for the user to choose
    private UMLRelationships findRelationshipByLabel(String label) {
        for (UMLRelationships relationship : drawnUMLRelationships) {
            String relationshipLabel = relationship.getType() + " from " + relationship.getSource().getName() + " to " + relationship.getDest().getName();
            if (relationshipLabel.equals(label)) {
                return relationship;
            }
        }
        return null; // Relationship not found
    }


    // Helper meth
    // Unfinished
    @FXML
    void deleteClassFieldGui(ActionEvent event) {
        if (drawnUMLClasses.isEmpty()) {
            showAlert("Error", "No classes to edit. Please add a class first.");
            return;
        }

        // Select Class
        ChoiceDialog<String> classDialog = new ChoiceDialog<>(drawnUMLClasses.get(0).getName(),
                drawnUMLClasses.stream().map(UMLClass::getName).collect(Collectors.toList()));
        classDialog.setTitle("Select Class");
        classDialog.setHeaderText("Choose the class to delete fields from:");
        classDialog.setContentText("Class:");
        Optional<String> classNameResult = classDialog.showAndWait();
        if (!classNameResult.isPresent()) {
            return; // User canceled
        }
        String className = classNameResult.get();

        UMLClass selectedClass = findUMLClass(className);
        if (selectedClass == null) {
            showAlert("Error", "Class not found.");
            return;
        }

        // Select Field to Delete
        List<String> fieldNames = selectedClass.getFields() != null
                ? selectedClass.getFields().stream().map(UMLFields::getName).collect(Collectors.toList())
                : Collections.emptyList();

        if (fieldNames.isEmpty()) {
            showAlert("Error", "No fields available to delete in the selected class.");
            return;
        }

        ChoiceDialog<String> fieldDialog = new ChoiceDialog<>(fieldNames.get(0), fieldNames);
        fieldDialog.setTitle("Delete Field");
        fieldDialog.setHeaderText("Select the field to delete:");
        fieldDialog.setContentText("Field:");
        Optional<String> fieldNameResult = fieldDialog.showAndWait();
        if (!fieldNameResult.isPresent()) {
            return; // User canceled
        }
        String fieldName = fieldNameResult.get();
        selectedClass.removeField(fieldName);

        // Update GUI
        umlView.updateCanvas(diagram,selectedClass);
    }



    // Unfinished
    @FXML
    void deleteClassMethodGui(ActionEvent event) {

    }

    // Unfinished
    @FXML
    void renameClassFieldGui(ActionEvent event) {

    }

    // Unfinished
    @FXML
    void renameClassMethodGui(ActionEvent event) {

    }

    // Unfinished
    @FXML
    void changeRelTypeGui(ActionEvent event) {

    }

    // Unifinished
    @FXML
    void renameClassGui(ActionEvent event) {
        // Get all class names from the UML diagram
        List<String> classNames = new ArrayList<>(diagram.getClasses().keySet());

        // Create a new ChoiceDialog instance with the list of class names for the user to select which class to rename
        ChoiceDialog<String> dialog = new ChoiceDialog<>(null, classNames);
        dialog.setTitle("Rename Class");
        dialog.setHeaderText("Select a Class to Rename");
        dialog.setContentText("Class:");

        // Show the dialog and capture the result
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(oldClassName -> {
            // Prompt the user for the new class name
            TextInputDialog renameDialog = new TextInputDialog(oldClassName);
            renameDialog.setTitle("Rename Class");
            renameDialog.setHeaderText("Renaming Class: " + oldClassName);
            renameDialog.setContentText("Enter new class name:");

            Optional<String> newClassNameResult = renameDialog.showAndWait();
            newClassNameResult.ifPresent(newClassName -> {
                if (!newClassName.trim().isEmpty() && !newClassName.equals(oldClassName)) {
                    // Check if the new class name already exists
                    if (diagram.classExists(newClassName) == null) {
                        // Find the UMLClass object to be renamed
                        UMLClass classToRename = diagram.classExists(oldClassName);

                        // Rename the UMLClass with the new name
                        classToRename.setName(newClassName);

                        // Redraw the UMLClass on the canvas with the updated name and maintain its position
                        umlView.updateCanvas(classToRename);

                        showAlert("Success", "Class '" + oldClassName + "' renamed to '" + newClassName + "'.");
                    } else {
                        showAlert("Error", "A class with the name '" + newClassName + "' already exists.");
                    }
                } else {
                    showAlert("Error", "Invalid class name.");
                }
            });
        });
    }




    // Save diagram option found in the file drop down menu allowing the user to save diagram and its contents
    @FXML
    void saveDiagramGui(ActionEvent event) {
        // Create a TextInputDialog to get the file name
        TextInputDialog saveDialog = new TextInputDialog();
        saveDialog.setTitle("Save Diagram");
        saveDialog.setHeaderText("Enter a file name to save the diagram:");
        saveDialog.setContentText("File Name:");

        // Show the dialog and capture the result
        Optional<String> fileNameResult = saveDialog.showAndWait();

        if (fileNameResult.isPresent() && !fileNameResult.get().isEmpty()) {
            String fileName = fileNameResult.get();

            // Create an instance of UMLSaveLoad
            UMLSaveLoad saveLoad = new UMLSaveLoad(diagram);

            // Call the saveData method of UMLSaveLoad to save the diagram
            saveLoad.saveData(fileName);

            showAlert("Success", "Diagram saved successfully.");
        } else {
            showAlert("Error", "Please enter a valid file name.");
        }
    }

    // not fully implemented
    @FXML
    void loadDiagramGui(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Load Diagram");
        dialog.setHeaderText("Enter the file path (without '.json' extension):");
        dialog.setContentText("File Path:");

        Optional<String> result = dialog.showAndWait();

        // Check if the user entered a file path
        if (result.isPresent()) {
            String filePath = result.get(); // Add the ".json" extension

            // Attempt to load data from the JSON file
            try {
                UMLSaveLoad saveLoad = new UMLSaveLoad(diagram); // You might need to pass a diagram instance here
                saveLoad.loadData(filePath); // Update the 'diagram' variable with the loaded data

                // Assuming you have access to 'gc' and 'centerContent'
                GraphicsContext gc = centerContent.getGraphicsContext2D();

                // Clear the canvas
                gc.clearRect(0, 0, centerContent.getWidth(), centerContent.getHeight());

                // Draw the UML classes from the loaded diagram
                for (UMLClass umlClass : diagram.getClasses().values()) {
                    umlView.drawUMLClass(umlClass);
                }

                // Display a success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Load Successful");
                alert.setHeaderText(null);
                alert.setContentText("Diagram loaded successfully.");
                alert.showAndWait();
            } catch (Exception e) {
                // Handle other exceptions
                showErrorDialog("Error loading diagram: " + e.getMessage());
            }
        }
    }



    private void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert addClassButton != null : "fx:id=\"addClassButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert addFieldButton != null : "fx:id=\"addFieldButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert addMethodButton != null : "fx:id=\"addMethodButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert addRelButton != null : "fx:id=\"addRelButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert centerContent != null : "fx:id=\"centerContent\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert centerScrollPane != null : "fx:id=\"centerScrollPane\" was not injected: check your FXML file 'UMLCreator.fxml'.";

        umlView.initializeComponents(centerContent, centerScrollPane);

        assert changeRelButton != null : "fx:id=\"changeRelButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert deleteClassButton != null : "fx:id=\"deleteClassButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert deleteFieldButton != null : "fx:id=\"deleteFieldButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert deleteMethodButton != null : "fx:id=\"deleteMethodButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert deleteRelButton != null : "fx:id=\"deleteRelButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert renameClassButton != null : "fx:id=\"renameClassButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert renameFieldButton != null : "fx:id=\"renameFieldButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert renameMethodButton != null : "fx:id=\"renameMethodButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";

    }

}
