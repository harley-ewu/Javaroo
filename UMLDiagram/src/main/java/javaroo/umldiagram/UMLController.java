package javaroo.umldiagram;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javaroo.cmd.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static javaroo.cmd.UMLMenu.*;
//import static javaroo.umldiagram.UMLDiagramGUI.myLaunch;

public class UMLController {

    private UMLView umlView;

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

    public static UMLController controllerInstance;

    public UMLView getUMLView() {
        return this.umlView;
    }


    // Public method to get the instance
    public static UMLController getInstance() {
        if (controllerInstance == null) {
            controllerInstance = new UMLController();
        }
        return controllerInstance;
    }

    public UMLController() {
        this.umlView = new UMLView(this);
    }


    // List to store the drawn UML classes
    List<UMLClass> drawnUMLClasses = new ArrayList<>();

    // List to store the drawn UML relationships
    final List<UMLRelationships> drawnUMLRelationships = new ArrayList();

    UMLDiagram diagram = new UMLDiagram();

    Map<String, UMLClass> classesMap = UMLDiagram.getClasses();

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

            String className = classNameResult.get().replaceAll("\\s", "").trim();
            // Trim the input to remove any leading or trailing whitespace

            // Check if the class already exists in the UML diagram
            if (diagram.classExists(className) != null) {
                showAlert("Error", "Class '" + className + "' already exists.");
                return;
            }

            // Create the UMLClass object
            UMLClass newUMLClass = new UMLClass(className);

            // Ask the user if they want to add fields and methods
            ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("Add Fields and Methods", "Skip");
            choiceDialog.setTitle("Add Fields and Methods?");
            choiceDialog.setHeaderText("Do you want to add fields and methods to the class?");
            choiceDialog.setContentText("Choose an option:");

            Optional<String> choiceResult = choiceDialog.showAndWait();

            if (!choiceResult.isPresent()) {
                // User pressed "Cancel" in the choice dialog, so do nothing and return
                return;
            }

            // If the user made a choice, proceed with the logic
            if (choiceResult.get().equals("Add Fields and Methods")) {
                diagram.addClass(newUMLClass.getName());
                addFieldsToClass(newUMLClass);
                //addMethodsToClass(newUMLClass);
                showAlert("Success", "Class '" + className + "' added with fields and methods.");
            } else {
                // User chose to skip adding fields and methods or made another choice
                diagram.addClass(newUMLClass.getName());
                showAlert("Success", "Class '" + className + "' added.");
            }

            // Update the visual representation
            umlView.autoAssignCoordinatesGrid(newUMLClass); // This method should set the coordinates for the new class
            drawnUMLClasses.add(newUMLClass); // This should be a collection of UMLClass objects that are currently drawn
            umlView.drawUMLClass(newUMLClass); // This method should handle the actual drawing of the class on the canvas or pane
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
        List<String> fieldTypes = Arrays.asList("String", "int", "double", "char", "void", "float", "boolean");
        AtomicBoolean proceedToAddMethods = new AtomicBoolean(false);

        while (true) {
            TextInputDialog fieldNameDialog = new TextInputDialog();
            fieldNameDialog.setTitle("Add Field");
            fieldNameDialog.setHeaderText("Enter field details:");
            fieldNameDialog.setContentText("Field Name:");

            // Remove the default 'Cancel' button and keep the 'OK' button
            fieldNameDialog.getDialogPane().getButtonTypes().retainAll(ButtonType.OK);

            // Custom button for 'Proceed to add methods'
            ButtonType proceedToAddMethodsButtonType = new ButtonType("Proceed to add methods", ButtonBar.ButtonData.CANCEL_CLOSE);
            fieldNameDialog.getDialogPane().getButtonTypes().add(proceedToAddMethodsButtonType);

            // Handling 'Proceed to add methods' button
            Node proceedButton = fieldNameDialog.getDialogPane().lookupButton(proceedToAddMethodsButtonType);
            proceedButton.addEventFilter(ActionEvent.ACTION, event -> {
                proceedToAddMethods.set(true);
                event.consume();
                fieldNameDialog.close();
            });

            // Custom button for 'Clear Text'
            ButtonType clearTextButtonType = new ButtonType("Clear Text", ButtonBar.ButtonData.OTHER);
            fieldNameDialog.getDialogPane().getButtonTypes().add(clearTextButtonType);

            // Handling 'Clear Text' button
            Node clearTextButton = fieldNameDialog.getDialogPane().lookupButton(clearTextButtonType);
            clearTextButton.addEventFilter(ActionEvent.ACTION, event -> {
                fieldNameDialog.getEditor().clear();
                event.consume();
            });

            // Show the dialog and wait for response
            Optional<String> fieldNameResult = fieldNameDialog.showAndWait();
            if (proceedToAddMethods.get() || !fieldNameResult.isPresent() || fieldNameResult.get().isEmpty()) {
                break;
            }

            String fieldName = fieldNameResult.get().replaceAll("\\s+", "").trim();

            // Create a choice dialog for selecting field type
            ChoiceDialog<String> fieldTypeDialog = new ChoiceDialog<>("String", fieldTypes);
            fieldTypeDialog.setTitle("Field Type");
            fieldTypeDialog.setHeaderText("Select Field Type:");
            fieldTypeDialog.setContentText("Field Type:");

            Optional<String> fieldTypeResult = fieldTypeDialog.showAndWait();
            if (!fieldTypeResult.isPresent()) {
                continue;
            }
            String fieldType = fieldTypeResult.get();

            // Create a new ChoiceDialog for field visibility
            ChoiceDialog<String> visibilityDialog = new ChoiceDialog<>("public", Arrays.asList("public", "private"));
            visibilityDialog.setTitle("Field Visibility");
            visibilityDialog.setHeaderText("Select Field Visibility:");
            visibilityDialog.setContentText("Visibility:");

            Optional<String> fieldVisibilityResult = visibilityDialog.showAndWait();
            if (!fieldVisibilityResult.isPresent()) {
                continue;
            }
            String fieldVisibility = fieldVisibilityResult.get();

            // Add the field to the class
            umlClass.addField(fieldName, fieldType, fieldVisibility);
            diagram.classExists(umlClass.getName()).addField(fieldName, fieldType, fieldVisibility);
        }

        if (proceedToAddMethods.get()) {
            // Code for adding methods goes here
            addMethodsToClass(umlClass);
        }
    }


    // Utility method to clear a text field
    private void clearTextField(TextField textField) {
        Platform.runLater(() -> textField.setText(""));
    }





    // Helper method to add methods to the class
    private void addMethodsToClass(UMLClass umlClass) {
        while (true) {
            TextInputDialog methodDialog = new TextInputDialog();
            methodDialog.setTitle("Add Method");
            methodDialog.setHeaderText("Enter method details (or press 'Finish' to end):");
            methodDialog.setContentText("Method Name:");

            // Remove default button types and add 'OK' and custom buttons
            methodDialog.getDialogPane().getButtonTypes().clear();
            methodDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

            // Custom 'Finish' button to end method addition
            ButtonType finishButtonType = new ButtonType("Finish", ButtonBar.ButtonData.CANCEL_CLOSE);
            methodDialog.getDialogPane().getButtonTypes().add(finishButtonType);

            // Custom 'Clear Text' button to clear the method name
            ButtonType clearTextButtonType = new ButtonType("Clear Text", ButtonBar.ButtonData.OTHER);
            methodDialog.getDialogPane().getButtonTypes().add(clearTextButtonType);

            // Event filter for 'Clear Text' button
            Node clearTextButton = methodDialog.getDialogPane().lookupButton(clearTextButtonType);
            clearTextButton.addEventFilter(ActionEvent.ACTION, event -> {
                methodDialog.getEditor().clear();
                event.consume();
            });

            Optional<String> methodNameResult = methodDialog.showAndWait();
            if (!methodNameResult.isPresent() || methodNameResult.get().isEmpty() ||
                    finishButtonType.equals(methodDialog.getResult())) {
                // User finished adding methods or clicked 'Finish'
                break;
            }
            String methodName = methodNameResult.get().replaceAll("\\s", "").trim(); // Trim the input

            // Create a custom dialog for selecting the method return type
            ChoiceDialog<String> returnTypeDialog = new ChoiceDialog<>("String", "int", "double", "char", "void", "float", "boolean");
            returnTypeDialog.setTitle("Select Method Return Type");
            returnTypeDialog.setHeaderText("Choose the method return type:");
            returnTypeDialog.setContentText("Method Return Type:");

            Optional<String> methodTypeResult = returnTypeDialog.showAndWait();
            if (!methodTypeResult.isPresent() || methodTypeResult.get().isEmpty()) {
                showAlert("Error", "Method return type is required.");
                continue;
            }
            String methodType = methodTypeResult.get().trim(); // Selected return type

            // Clear the input text before showing the next dialog
            methodDialog.getEditor().clear();

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
            umlClass.addMethod(methodName, methodType, paramsList);
            diagram.classExists(umlClass.getName()).addMethod(methodName, methodType, paramsList);
        }
    }
  
    @FXML
    public void refresh(){

        drawnUMLClasses.clear();
        drawnUMLRelationships.clear();

        for (UMLClass umlClass : diagram.getClasses().values()) {
            drawnUMLClasses.add(umlClass);
            umlView.autoAssignCoordinatesGrid(umlClass);// Add to list
            umlView.updateCanvas(diagram, umlClass);      // Draw the class
        }

//        for (UMLClass umlClass : classesMap.values()) {
//            drawnUMLClasses.add(umlClass);
//        }
//        for (UMLClass umlClass : drawnUMLClasses) {
//            umlView.autoAssignCoordinatesGrid(umlClass);
//            umlView.updateCanvas(diagram, umlClass);
//
//        }

        for (UMLRelationships relationship : diagram.getRelationships()) {
            drawnUMLRelationships.add(relationship);
            umlView.drawUMLRelationship(relationship.getSource(), relationship.getDest(), relationship.getType());
        }

        // Draw the existing relationships.
        umlView.drawExistingRelationships();
    }
  
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

        // Define the list of available field types
        List<String> fieldTypes = Arrays.asList("String", "int", "double", "char", "void", "float", "boolean");

        // Now proceed with adding fields to the selected class using the diagram
        while (true) {
            TextInputDialog fieldNameDialog = new TextInputDialog();
            fieldNameDialog.setTitle("Add Field");
            fieldNameDialog.setHeaderText("Enter field name (or press 'Finish' to end):");
            fieldNameDialog.setContentText("Field Name:");

            // Remove default button types and add 'OK' and custom buttons
            fieldNameDialog.getDialogPane().getButtonTypes().clear();
            fieldNameDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

            // Custom 'Finish' button to end field addition
            ButtonType finishButtonType = new ButtonType("Finish", ButtonBar.ButtonData.CANCEL_CLOSE);
            fieldNameDialog.getDialogPane().getButtonTypes().add(finishButtonType);

            // Custom 'Clear Text' button to clear the field name
            ButtonType clearTextButtonType = new ButtonType("Clear Text", ButtonBar.ButtonData.OTHER);
            fieldNameDialog.getDialogPane().getButtonTypes().add(clearTextButtonType);

            // Handling 'Clear Text' button
            Node clearTextButton = fieldNameDialog.getDialogPane().lookupButton(clearTextButtonType);
            clearTextButton.addEventFilter(ActionEvent.ACTION, event1 -> {
                fieldNameDialog.getEditor().setText(""); // Clear the text
                event1.consume(); // Prevent the dialog from closing
            });

            Optional<String> fieldNameResult = fieldNameDialog.showAndWait();
            if (!fieldNameResult.isPresent() || fieldNameResult.get().isEmpty() ||
                    finishButtonType.equals(fieldNameDialog.getResult())) {
                // User finished adding fields or clicked 'Finish'
                break;
            }
            String fieldName = fieldNameResult.get().replaceAll("\\s+", "").trim(); // Trim the input

            // Create a choice dialog for selecting field type
            ChoiceDialog<String> fieldTypeDialog = new ChoiceDialog<>("String", fieldTypes);
            fieldTypeDialog.setTitle("Field Type");
            fieldTypeDialog.setHeaderText("Select Field Type:");
            fieldTypeDialog.setContentText("Field Type:");

            Optional<String> fieldTypeResult = fieldTypeDialog.showAndWait();
            if (!fieldTypeResult.isPresent()) {
                // User canceled field type selection
                continue; // Go back to entering field details
            }
            String fieldType = fieldTypeResult.get();

            // Create a choice dialog for selecting field visibility (public or private)
            ChoiceDialog<String> visibilityDialog = new ChoiceDialog<>("public", "public", "private");
            visibilityDialog.setTitle("Field Visibility");
            visibilityDialog.setHeaderText("Select Field Visibility:");
            visibilityDialog.setContentText("Visibility:");

            Optional<String> fieldVisibilityResult = visibilityDialog.showAndWait();
            if (!fieldVisibilityResult.isPresent()) {
                // User canceled field visibility selection
                continue; // Go back to entering field details
            }
            String fieldVisibility = fieldVisibilityResult.get();

            // Create the new field and add it to the selected class using the diagram
            selectedClass.addField(fieldName, fieldType, fieldVisibility);
            diagram.classExists(selectedClass.getName()).addField(fieldName, fieldType, fieldVisibility);

            // Redraw the updated class on the canvas
            //umlView.autoAssignCoordinatesGrid(selectedClass);
            umlView.drawUpdatedClass(selectedClass);
            //umlView.drawUpdatedClass(selectedClass);
            umlView.drawExistingRelationships();
        }
    }

    // To be implemented
    @FXML
    void addClassMethodGui(ActionEvent event) {
        // Check if there are no classes in the list
        if (drawnUMLClasses.isEmpty()) {
            showAlert("Error", "No classes found. Please add a class first.");
            return;
        }

        ButtonType finishButtonType = new ButtonType("Finish", ButtonBar.ButtonData.CANCEL_CLOSE);


        // Create a choice dialog for selecting the class
        ChoiceDialog<String> classDialog = new ChoiceDialog<>(
                drawnUMLClasses.get(0).getName(),
                drawnUMLClasses.stream().map(UMLClass::getName).collect(Collectors.toList()));
        classDialog.setTitle("Select Class");
        classDialog.setHeaderText("Choose the class to add methods:");
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

        // Now proceed with adding methods to the selected class
        while (true) {
            TextInputDialog methodDialog = new TextInputDialog();
            methodDialog.setTitle("Add Method");
            methodDialog.setHeaderText("Enter method details (or press 'Finish' to end):");
            methodDialog.setContentText("Method Name:");

            // Remove default button types and add 'OK' and custom buttons
            methodDialog.getDialogPane().getButtonTypes().clear();
            methodDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, finishButtonType);

            // Custom 'Clear Text' button to clear the method name
            ButtonType clearTextButtonType = new ButtonType("Clear Text", ButtonBar.ButtonData.OTHER);
            methodDialog.getDialogPane().getButtonTypes().add(clearTextButtonType);

            // Event filter for 'Clear Text' button
            Node clearTextButton = methodDialog.getDialogPane().lookupButton(clearTextButtonType);
            clearTextButton.addEventFilter(ActionEvent.ACTION, event1 -> {
                methodDialog.getEditor().clear();
                event1.consume(); // Prevent the dialog from closing
            });

            Optional<String> methodNameResult = methodDialog.showAndWait();
            if (!methodNameResult.isPresent() || methodNameResult.get().isEmpty() ||
                    finishButtonType.equals(methodDialog.getResult())) {
                // User finished adding methods or clicked 'Finish'
                break;
            }
            String methodName = methodNameResult.get().replaceAll("\\s+", "").trim(); // Trim the input

            // Choice dialog for method return type
            ChoiceDialog<String> returnTypeDialog = new ChoiceDialog<>("String", "int", "double", "char", "void", "float", "boolean");
            returnTypeDialog.setTitle("Select Method Return Type");
            returnTypeDialog.setHeaderText("Choose the method return type:");
            returnTypeDialog.setContentText("Method Return Type:");

            Optional<String> methodTypeResult = returnTypeDialog.showAndWait();
            if (!methodTypeResult.isPresent() || methodTypeResult.get().isEmpty()) {
                showAlert("Error", "Method return type is required.");
                continue;
            }
            String methodType = methodTypeResult.get().trim(); // Selected return type

            // Clear the input text before showing the next dialog
            methodDialog.getEditor().clear();

            // Dialog for entering method parameters
            methodDialog.setContentText("Method Parameters (e.g., int param1, String param2):");
            Optional<String> methodParamsResult = methodDialog.showAndWait();
            String methodParams = methodParamsResult.orElse("");

            // Split the parameters string into an ArrayList<String>
            ArrayList<String> paramsList = new ArrayList<>();
            if (!methodParams.isEmpty()) {
                String[] paramsArray = methodParams.split(",\\s*");
                Collections.addAll(paramsList, paramsArray);
            }

            // Add the method to the selected class and update the diagram
            selectedClass.addMethod(methodName, methodType, paramsList);
            diagram.classExists(selectedClass.getName()).addMethod(methodName, methodType, paramsList);

            // Redraw the updated class on the canvas
            umlView.drawUpdatedClass(selectedClass);
            umlView.drawExistingRelationships();
        }
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
                    diagram.undoRemoveClass(className);

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
                diagram.removeRelationship(selectedRelationship.getSource().getName(), selectedRelationship.getDest().getName());

                // Clear the relationship from the canvas
                umlView.clearRelationshipFromCanvas(selectedRelationship);

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
        umlView.drawExistingRelationships();
    }



    // Unfinished
    @FXML
    void deleteClassMethodGui(ActionEvent event) {
        // Check if there are no classes in the list
        if (drawnUMLClasses.isEmpty()) {
            showAlert("Error", "No classes found. Please add a class first.");
            return;
        }

        // Create a choice dialog for selecting the class
        ChoiceDialog<String> classDialog = new ChoiceDialog<>(
                drawnUMLClasses.get(0).getName(),
                drawnUMLClasses.stream().map(UMLClass::getName).collect(Collectors.toList()));
        classDialog.setTitle("Select Class");
        classDialog.setHeaderText("Choose the class to delete methods from:");
        classDialog.setContentText("Class:");

        Optional<String> classNameResult = classDialog.showAndWait();
        if (!classNameResult.isPresent()) {
            return; // User canceled class selection
        }
        String className = classNameResult.get();

        // Find the selected class using the findUMLClass method
        UMLClass selectedClass = findUMLClass(className);
        if (selectedClass == null) {
            showAlert("Error", "Class not found.");
            return;
        }

        // Check if there are methods to delete
        if (selectedClass.getMethods() == null || selectedClass.getMethods().isEmpty()) {
            showAlert("Error", "No methods available to delete in the selected class.");
            return;
        }

        List<UMLMethods> methods = selectedClass.getMethods();

        ChoiceDialog<UMLMethods> methodDialog = new ChoiceDialog<>(methods.get(0), methods);
        methodDialog.setTitle("Delete Method");
        methodDialog.setHeaderText("Select the method to delete:");
        methodDialog.setContentText("Method:");

        Optional<UMLMethods> selectedMethodResult = methodDialog.showAndWait();
        if (!selectedMethodResult.isPresent()) {
            return; // User canceled
        }

        UMLMethods selectedMethod = selectedMethodResult.get();

        // Display method details before deletion
        showMethodDetails(selectedMethod);

        // Ask for confirmation before deleting the method
        Alert confirmDeletion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDeletion.setTitle("Confirm Deletion");
        confirmDeletion.setHeaderText("Confirm Deletion");
        confirmDeletion.setContentText("Do you really want to delete this method?");
        Optional<ButtonType> result = confirmDeletion.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            selectedClass.removeMethod(selectedMethod.getName()); // Use the method name for deletion
            umlView.updateCanvas(diagram, selectedClass);
            umlView.drawExistingRelationships();
            showAlert("Info", "Method deleted successfully.");
        } else {
            // User canceled the deletion
            showAlert("Info", "Deletion canceled. The method was not deleted.");
        }
    }

    // Show method details using an Alert
    private void showMethodDetails(UMLMethods method) {
        String methodDetails = "Method Name: " + method.getName() + "\n" +
                "Return Type: " + method.getReturnType() + "\n" +
                "Parameters: " + method.getParameters().toString();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Method Details");
        alert.setHeaderText(null);
        alert.setContentText(methodDetails);
        alert.showAndWait();
    }

    // Unfinished
    @FXML
    void renameClassFieldGui(ActionEvent event) {
        // Check if there are no classes in the list
        if (drawnUMLClasses.isEmpty()) {
            showAlert("Error", "No classes found. Please add a class first.");
            return;
        }

        // Step 1: Select a class
        ChoiceDialog<String> classDialog = new ChoiceDialog<>(
                drawnUMLClasses.get(0).getName(),
                drawnUMLClasses.stream().map(UMLClass::getName).collect(Collectors.toList()));
        classDialog.setTitle("Select Class");
        classDialog.setHeaderText("Select the class of the field to rename:");
        classDialog.setContentText("Class:");

        Optional<String> classNameResult = classDialog.showAndWait();
        if (!classNameResult.isPresent()) {
            return; // User canceled class selection
        }
        String className = classNameResult.get();
        UMLClass selectedClass = findUMLClass(className);
        if (selectedClass == null) {
            showAlert("Error", "Class not found.");
            return;
        }

        // Step 2: Select a field
        ChoiceDialog<String> fieldDialog = new ChoiceDialog<>(
                selectedClass.getFields().get(0).getName(),
                selectedClass.getFields().stream().map(UMLFields::getName).collect(Collectors.toList()));
        fieldDialog.setTitle("Select Field");
        fieldDialog.setHeaderText("Select the field to rename:");
        fieldDialog.setContentText("Field:");

        Optional<String> fieldNameResult = fieldDialog.showAndWait();
        if (!fieldNameResult.isPresent()) {
            return; // User canceled field selection
        }
        String fieldName = fieldNameResult.get();

        // Step 3: Enter new name
        TextInputDialog newNameDialog = new TextInputDialog();
        newNameDialog.setTitle("Rename Field");
        newNameDialog.setHeaderText("Enter the New Field Name");
        newNameDialog.setContentText("New Field Name:");

        Optional<String> newNameResult = newNameDialog.showAndWait();
        newNameResult.ifPresent(newName -> {
            // Step 4: Rename the field
            selectedClass.renameField(fieldName, newName);
            showAlert("Success", "Field renamed from '" + fieldName + "' to '" + newName + "'");
            // Update the view
            umlView.drawUpdatedClass(selectedClass);
        });
    }


    // Unfinished
    @FXML
    void renameClassMethodGui(ActionEvent event) {
        // Check if there are no classes in the list
        if (drawnUMLClasses.isEmpty()) {
            showAlert("Error", "No classes found. Please add a class first.");
            return;
        }

        // Step 1: Select a class
        ChoiceDialog<String> classDialog = new ChoiceDialog<>(
                drawnUMLClasses.get(0).getName(),
                drawnUMLClasses.stream().map(UMLClass::getName).collect(Collectors.toList()));
        classDialog.setTitle("Select Class");
        classDialog.setHeaderText("Select the class of the method to rename:");
        classDialog.setContentText("Class:");

        Optional<String> classNameResult = classDialog.showAndWait();
        if (!classNameResult.isPresent()) {
            return; // User canceled class selection
        }
        String className = classNameResult.get();
        UMLClass selectedClass = findUMLClass(className);
        if (selectedClass == null) {
            showAlert("Error", "Class not found.");
            return;
        }

        // Step 2: Select a method
        ChoiceDialog<UMLMethods> methodDialog = new ChoiceDialog<>(
                selectedClass.getMethods().isEmpty() ? null : selectedClass.getMethods().get(0),
                selectedClass.getMethods());
        methodDialog.setTitle("Select Method");
        methodDialog.setHeaderText("Select the method to rename:");
        methodDialog.setContentText("Method:");

        Optional<UMLMethods> selectedMethodResult = methodDialog.showAndWait();
        if (!selectedMethodResult.isPresent()) {
            return; // User canceled method selection
        }

        UMLMethods selectedMethod = selectedMethodResult.get();

        // Step 3: Display method details, including parameters and types
        StringBuilder methodDetails = new StringBuilder();
        methodDetails.append("Method Name: ").append(selectedMethod.getName()).append("\n");
        methodDetails.append("Return Type: ").append(selectedMethod.getReturnType()).append("\n");
        methodDetails.append("Parameters:\n");
        for (String parameter : selectedMethod.getParameters()) {
            methodDetails.append(" - ").append(parameter).append("\n");
        }

        Alert methodDetailsAlert = new Alert(Alert.AlertType.INFORMATION);
        methodDetailsAlert.setTitle("Method Details");
        methodDetailsAlert.setHeaderText("Method Details");
        methodDetailsAlert.setContentText(methodDetails.toString());
        methodDetailsAlert.showAndWait();

        // Step 4: Ask for confirmation before renaming the method
        Alert confirmRename = new Alert(Alert.AlertType.CONFIRMATION);
        confirmRename.setTitle("Confirm Rename");
        confirmRename.setHeaderText("Confirm Rename");
        confirmRename.setContentText("Do you really want to rename this method?");
        Optional<ButtonType> result = confirmRename.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Step 5: Enter new name
            TextInputDialog newNameDialog = new TextInputDialog();
            newNameDialog.setTitle("Rename Method");
            newNameDialog.setHeaderText("Enter the New Method Name");
            newNameDialog.setContentText("New Method Name:");

            Optional<String> newNameResult = newNameDialog.showAndWait();
            newNameResult.ifPresent(newName -> {
                // Step 6: Rename the method
                selectedClass.renameMethod(selectedMethod.getName(), newName);
                showAlert("Success", "Method renamed to: " + newName);
                // Update the view
                umlView.drawUpdatedClass(selectedClass);
            });
        } else {
            // User canceled the rename
            showAlert("Info", "Rename canceled. The method was not renamed.");
        }
    }
    


    // Unfinished
    @FXML
    void changeRelTypeGui(ActionEvent event) {
        if (drawnUMLRelationships.isEmpty()) {
            showAlert("Error", "No relationships have been created to change.");
            return;
        }

        // Step 1: Select a relationship
        ChoiceDialog<UMLRelationships> relationshipDialog = new ChoiceDialog<>(null, new ArrayList<>(drawnUMLRelationships));
        relationshipDialog.setTitle("Change Relationship Type");
        relationshipDialog.setHeaderText("Select the relationship to change:");
        relationshipDialog.setContentText("Relationship:");

        Optional<UMLRelationships> relationshipResult = relationshipDialog.showAndWait();
        if (!relationshipResult.isPresent()) {
            return; // User canceled relationship selection
        }

        UMLRelationships selectedRelationship = relationshipResult.get();

        // Step 2: Choose new relationship type
        ChoiceDialog<UMLRelationships.RelationshipType> relationshipTypeDialog = new ChoiceDialog<>(selectedRelationship.getType(), UMLRelationships.RelationshipType.values());
        relationshipTypeDialog.setTitle("Change Relationship Type");
        relationshipTypeDialog.setHeaderText("Select the new type of relationship:");
        relationshipTypeDialog.setContentText("New Relationship Type:");

        Optional<UMLRelationships.RelationshipType> newTypeResult = relationshipTypeDialog.showAndWait();
        if (!newTypeResult.isPresent()) {
            return; // User canceled new type selection
        }

        UMLRelationships.RelationshipType newType = newTypeResult.get();

        // Step 3: Update the relationship type
        umlView.updateRelationType(selectedRelationship.getSource(), selectedRelationship.getDest(), newType);

        showAlert("Success", "Relationship type changed successfully.");
    }



    // Unifinished
    @FXML
    void renameClassGui(ActionEvent event) {
        // Check if there are no classes in the list
        if (drawnUMLClasses.isEmpty()) {
            showAlert("Error", "No classes found. Please add a class first.");
            return;
        }

        // Create a choice dialog for selecting the class to rename
        ChoiceDialog<String> classDialog = new ChoiceDialog<>(
                drawnUMLClasses.get(0).getName(),
                drawnUMLClasses.stream().map(UMLClass::getName).collect(Collectors.toList()));
        classDialog.setTitle("Select Class");
        classDialog.setHeaderText("Choose the class to rename:");
        classDialog.setContentText("Class:");

        Optional<String> classNameResult = classDialog.showAndWait();
        if (!classNameResult.isPresent()) {
            return; // User canceled class selection
        }
        String className = classNameResult.get();

        // Find the selected class using the findUMLClass method
        UMLClass selectedClass = findUMLClass(className);
        if (selectedClass == null) {
            showAlert("Error", "Class not found.");
            return;
        }

        // TextInputDialog to get the new class name
        TextInputDialog newNameDialog = new TextInputDialog();
        newNameDialog.setTitle("Rename Class");
        newNameDialog.setHeaderText("Enter the New Class Name");
        newNameDialog.setContentText("New Class Name:");

        Optional<String> newNameResult = newNameDialog.showAndWait();
        newNameResult.ifPresent(newName -> {
            // Use the UMLDiagram instance to rename the class
            diagram.renameClass(className, newName);

            // Update the name in the drawnUMLClasses list
            selectedClass.setName(newName);

            // Update the view with the new class name
            umlView.drawUpdatedClass(selectedClass);
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
        dialog.setHeaderText("Load UML Diagram");
        dialog.setContentText("Enter the file path or name:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(filePath -> {
            try {
                UMLSaveLoad saveLoad = new UMLSaveLoad(diagram);
                saveLoad.loadData(filePath);

                GraphicsContext gc = centerContent.getGraphicsContext2D();
                gc.clearRect(0, 0, centerContent.getWidth(), centerContent.getHeight());

                drawnUMLClasses.clear();
                drawnUMLRelationships.clear();

                // Assign coordinates to each UMLClass using autoAssignCoordinatesGrid
                for (UMLClass umlClass : diagram.getClasses().values()) {
                     // Assign coordinates
                    drawnUMLClasses.add(umlClass);
                    umlView.autoAssignCoordinatesGrid(umlClass);
                    umlView.updateCanvas(diagram, umlClass);      // Draw the class
                }

                for (UMLRelationships relationship : diagram.getRelationships()) {
                    drawnUMLRelationships.add(relationship);
                    umlView.drawUMLRelationship(relationship.getSource(), relationship.getDest(), relationship.getType());
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load the diagram: " + e.getMessage());
            }
        });
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

        this.umlView = new UMLView(this);
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

    public void restartCLI(ActionEvent actionEvent) {
        CView = true;
        Platform.exit();
        //myLaunch(UMLDiagramGUI.class);
    }

    public void zoomIn(ActionEvent actionEvent) {
        umlView.zoomIn();
    }

    public void zoomOut(ActionEvent actionEvent) {
        umlView.zoomOut();
    }
}
