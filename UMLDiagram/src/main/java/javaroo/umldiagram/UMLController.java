package javaroo.umldiagram;


import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javaroo.cmd.*;

import java.io.*;
import java.net.URL;
import java.util.*;

public class UMLController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="addClassButton"
    private Button addClassButton; // Value injected by FXMLLoader

    @FXML // fx:id="addRelButton"
    private Button addRelButton; // Value injected by FXMLLoader

    @FXML // fx:id="centerContent"
    private Canvas centerContent; // Value injected by FXMLLoader
    @FXML //
    private ScrollPane centerScrollPane;

    //private double xOffset = 0;
    //private double yOffset = 0;

    @FXML // fx:id="deleteClassButton"
    private Button deleteClassButton; // Value injected by FXMLLoader

    @FXML // fx:id="deleteRelButton"
    private Button deleteRelButton; // Value injected by FXMLLoader

    @FXML // fx:id="renameAttrButton"
    private Button renameAttrButton; // Value injected by FXMLLoader

    @FXML // fx:id="renameClassButton"
    private Button renameClassButton; // Value injected by FXMLLoader

    // List to store the drawn UML classes
    private List<UMLClass> drawnUMLClasses = new ArrayList<>();

    private final ArrayList<UMLRelationships> drawnUMLRelationships = new ArrayList();


    private UMLDiagram diagram = new UMLDiagram();

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

            // Add the new class to the UMLDiagram
           // diagram.addClass(newUMLClass.getName());

            // Update the visual representation
            // Assuming you have methods to handle the visual representation of the UML class
            autoAssignCoordinates(newUMLClass); // This method should set the coordinates for the new class
            drawnUMLClasses.add(newUMLClass); // This should be a collection of UMLClass objects that are currently drawn
            drawUMLClass(newUMLClass); // This method should handle the actual drawing of the class on the canvas or pane

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
            diagram.classExists(umlClass.getName()).addField(fieldName, fieldType, fieldVisibility);
        }
    }

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

    private void drawUMLClass(UMLClass umlClass) {
        double x = umlClass.getX(); // Get the X-coordinate from the UMLClass
        double y = umlClass.getY(); // Get the Y-coordinate from the UMLClass
        double textPadding = 20; // Padding for text inside the box

        GraphicsContext gc = centerContent.getGraphicsContext2D();
        gc.setLineWidth(0.75);

        // Define fonts
        Font classFont = Font.font("Serif", FontWeight.BOLD, 20);
        Font fieldMethodFont = Font.font("Serif", FontWeight.NORMAL, 16);

        // Get the class name, fields, and methods
        String className = umlClass.getName();
        List<UMLFields> fields = diagram.classExists(umlClass.getName()).getFields();
        List<UMLMethods> methods = diagram.classExists(umlClass.getName()).getMethods();

        // Calculate the width and height based on text size
        double boxWidth = 0;
        double boxHeight = 0;

        // Calculate width and initial height based on class name
        Text classText = new Text(className);
        classText.setFont(classFont);
        boxWidth = Math.max(boxWidth, classText.getLayoutBounds().getWidth() + 2 * textPadding);
        boxHeight += classText.getLayoutBounds().getHeight() + 2 * textPadding;

        // Calculate additional height based on fields
        for (UMLFields field : fields) {
            Text fieldText = new Text(formatVisibility(field.getVisibility()) + " " + field.getName() + " : " + field.getType());
            fieldText.setFont(fieldMethodFont);
            boxWidth = Math.max(boxWidth, fieldText.getLayoutBounds().getWidth() + 2 * textPadding);
            boxHeight += fieldText.getLayoutBounds().getHeight() + textPadding;
        }

        // Add separator space if both fields and methods are present
        if (!fields.isEmpty() && !methods.isEmpty()) {
            boxHeight += textPadding;
        }

        // Calculate additional height based on methods
        for (UMLMethods method : methods) {
            Text methodText = new Text(method.getName() + "(" + String.join(", ", method.getParameters()) + ") : " + method.getReturnType());
            methodText.setFont(fieldMethodFont);
            boxWidth = Math.max(boxWidth, methodText.getLayoutBounds().getWidth() + 2 * textPadding);
            boxHeight += methodText.getLayoutBounds().getHeight() + textPadding;
        }

        // Draw the UML class box with a white fill and black outline
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.fillRect(x, y, boxWidth, boxHeight);
        gc.strokeRect(x, y, boxWidth, boxHeight);

        // Draw the class name inside the box
        gc.setFont(classFont);
        gc.setFill(Color.BLACK); // Set the text color to black
        double contentY = y + classText.getLayoutBounds().getHeight() + textPadding;
        gc.fillText(className, x + textPadding, contentY);

        // Draw separator after class name
        contentY += textPadding / 2; // Adjust for separator
        gc.strokeLine(x, contentY, x + boxWidth, contentY);
        contentY += textPadding; // Adjust for space after separator

        // Draw fields
        gc.setFont(fieldMethodFont);
        for (UMLFields field : fields) {
            Text fieldText = new Text(formatVisibility(field.getVisibility()) + " " + field.getName() + " : " + field.getType());
            fieldText.setFont(fieldMethodFont);
            contentY += fieldText.getLayoutBounds().getHeight();
            gc.fillText(fieldText.getText(), x + textPadding, contentY);
            contentY += textPadding; // Add padding after each field
        }

        // Draw separator between fields and methods if necessary
        if (!fields.isEmpty() && !methods.isEmpty()) {
            gc.strokeLine(x, contentY, x + boxWidth, contentY);
            contentY += textPadding; // Add padding after the separator
        }

        // Draw methods
        for (UMLMethods method : methods) {
            Text methodText = new Text(method.getName() + "(" + String.join(", ", method.getParameters()) + ") : " + method.getReturnType());
            methodText.setFont(fieldMethodFont);
            contentY += methodText.getLayoutBounds().getHeight();
            gc.fillText(methodText.getText(), x + textPadding, contentY);
            contentY += textPadding; // Add padding after each method
        }
    }
    // Helper method to format the visibility symbol for fields
    private String formatVisibility(String visibility) {
        switch (visibility.toLowerCase()) {
            case "public":
                return "+";
            case "private":
                return "-";
            default:
                return "?";
        }
    }



    private void autoAssignCoordinates(UMLClass umlClass) {
        double xPadding = 80; // Horizontal padding between classes
        double yPadding = 80; // Vertical padding between classes
        double minX = 100; // Minimum X-coordinate
        double minY = 100; // Minimum Y-coordinate
        double maxX = centerContent.getWidth() - umlClass.getWidth() - xPadding;
        double maxY = centerContent.getHeight() - umlClass.getHeight() - yPadding;

        if (drawnUMLClasses.isEmpty()) {
            // If there are no existing classes, assign a random position within the bounds
            double x = minX + (maxX - minX) * Math.random();
            double y = minY + (maxY - minY) * Math.random();
            umlClass.setPosition(x, y);
            umlClass.addClassWithCoordinates(umlClass.getName(), umlClass.getX(), umlClass.getY());
            return;
        }

        boolean placed = false;
        double x = 0;
        double y = 0;

        while (!placed) {
            boolean collision = false;
            // Randomly select a position within the bounds
            x = minX + (maxX - minX) * Math.random();
            y = minY + (maxY - minY) * Math.random();

            for (UMLClass existingClass : drawnUMLClasses) {
                if (x + umlClass.getWidth() + xPadding > existingClass.getX() &&
                        existingClass.getX() + existingClass.getWidth() + xPadding > x &&
                        y + umlClass.getHeight() + yPadding > existingClass.getY() &&
                        existingClass.getY() + existingClass.getHeight() + yPadding > y) {
                    // There's a collision, classes are too close, choose a new random position
                    collision = true;
                    break;
                }
            }

            if (!collision) {
                // Check if the new class collides with any other class in the drawing
                boolean classCollision = false;
                for (UMLClass existingClass : drawnUMLClasses) {
                    if (x < existingClass.getX() + existingClass.getWidth() + xPadding &&
                            x + umlClass.getWidth() + xPadding > existingClass.getX() &&
                            y < existingClass.getY() + existingClass.getHeight() + yPadding &&
                            y + umlClass.getHeight() + yPadding > existingClass.getY()) {
                        classCollision = true;
                        break;
                    }
                }

                if (!classCollision) {
                    // No collision with other classes, place the class at (x, y)
                    umlClass.setPosition(x, y);
                    placed = true;
                }
            }
        }
    }

    @FXML
    void addRelationshipGui(ActionEvent event) {
        if (drawnUMLClasses.isEmpty()) {
            showAlert("Error", "No UML classes have been created to establish a relationship.");
            return;
        }

        TextInputDialog sourceClassDialog = new TextInputDialog();
        sourceClassDialog.setTitle("Add Relationship");
        sourceClassDialog.setHeaderText("Enter the source class name:");
        sourceClassDialog.setContentText("Source Class:");

        Optional<String> sourceClassResult = sourceClassDialog.showAndWait();
        if (!sourceClassResult.isPresent() || sourceClassResult.get().isEmpty()) {
            showAlert("Error", "Source class name is required.");
            return;
        }

        String sourceClassName = sourceClassResult.get();
        UMLClass sourceClass = findUMLClass(sourceClassName);

        if (sourceClass == null) {
            showAlert("Error", "Source class '" + sourceClassName + "' does not exist.");
            return;
        }

        TextInputDialog destinationClassDialog = new TextInputDialog();
        destinationClassDialog.setTitle("Add Relationship");
        destinationClassDialog.setHeaderText("Enter the destination class name:");
        destinationClassDialog.setContentText("Destination Class:");

        Optional<String> destinationClassResult = destinationClassDialog.showAndWait();
        if (!destinationClassResult.isPresent() || destinationClassResult.get().isEmpty()) {
            showAlert("Error", "Destination class name is required.");
            return;
        }

        String destinationClassName = destinationClassResult.get();
        UMLClass destinationClass = findUMLClass(destinationClassName);

        if (destinationClass == null) {
            showAlert("Error", "Destination class '" + destinationClassName + "' does not exist.");
            return;
        }

        // Check if a relationship already exists between the same classes in either direction
        if (relationshipExists(sourceClass, destinationClass) || relationshipExists(destinationClass, sourceClass)) {
            showAlert("Error", "A relationship already exists between '" + sourceClassName + "' and '" + destinationClassName + "'.");
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
        drawUMLRelationship(sourceClass, destinationClass, selectedRelationshipType);

        // Add the created relationship to the controller's list of relationships
        drawnUMLRelationships.add(relationshipWithCoordinates);

        // Add the created relationship to the diagram's list of relationships
        diagram.addRelationship(sourceClass, destinationClass, relationship.getType());

        // Notify the user of a successful relationship creation
        showAlert("Success", "Relationship created successfully: " + sourceClassName + " " + selectedRelationshipType + " " + destinationClassName);
    }

    private boolean relationshipExists(UMLClass sourceClass, UMLClass destinationClass) {
        for (UMLRelationships relationship : drawnUMLRelationships) {
            if (relationship.getSource().equals(sourceClass) && relationship.getDest().equals(destinationClass)) {
                return true;
            }
        }
        return false;
    }



    private void drawUMLRelationship(UMLClass sourceClass, UMLClass destinationClass, UMLRelationships.RelationshipType relationshipType) {
        GraphicsContext gc = centerContent.getGraphicsContext2D();
        gc.setLineWidth(1.0);

        // Get coordinates of source and destination classes
        double sourceX = sourceClass.getX();
        double sourceY = sourceClass.getY();
        double destX = destinationClass.getX();
        double destY = destinationClass.getY();

        // Calculate the start and end points of the relationship lines
        double startX = sourceX + sourceClass.getWidth() / 2;
        double startY = sourceY + sourceClass.getHeight() / 2;
        double endX = destX + destinationClass.getWidth() / 2;
        double endY = destY + destinationClass.getHeight() / 2;

        // Calculate the triangle points at the end of the line
        double triangleHalfWidth = 20; // Half the width of the triangle
        double triangleHeight = 15; // Height of the triangle

        // Set line colors and styles based on the relationship type
        gc.setStroke(Color.BLACK); // Default color
        gc.setLineDashes(0); // Reset line style

        // Draw the relationship line based on the relationship type
        switch (relationshipType) {
            case AGGREGATION:
                // Vertical line from source to a little below destination
                gc.strokeLine(startX, startY, startX, endY + 20);
                // Horizontal line from startX to endX at the same Y-coordinate
                gc.strokeLine(startX, endY + 20, endX, endY + 20);
                // Draw the diamond shape at the end of the line
                gc.strokePolygon(new double[]{endX - 10, endX, endX + 10, endX}, new double[]{endY + 20, endY, endY + 20, endY + 40}, 4);
                break;

            case INHERITANCE:
                // Calculate the triangle position based on the direction of the relationship
                if (startX < endX) {
                    // Inheritance arrow points right
                    double arrowX = endX;
                    double arrowY = endY;
                    double x1 = arrowX - triangleHalfWidth;
                    double y1 = arrowY - triangleHeight;
                    double x2 = arrowX - triangleHalfWidth;
                    double y2 = arrowY + triangleHeight;
                    double x3 = arrowX;
                    double y3 = arrowY;
                    gc.setFill(Color.WHITE); // Set fill color to white
                    gc.setStroke(Color.BLACK); // Set stroke color to black
                    gc.strokeLine(startX, startY, startX, endY);
                    gc.strokeLine(startX, endY, endX, endY);
                    gc.fillPolygon(new double[]{x1, x2, x3}, new double[]{y1, y2, y3}, 3);
                    gc.strokePolygon(new double[]{x1, x2, x3}, new double[]{y1, y2, y3}, 3);
                } else {
                    // Inheritance arrow points left
                    double arrowX = endX;
                    double arrowY = endY;
                    double x1 = arrowX + triangleHalfWidth;
                    double y1 = arrowY - triangleHeight;
                    double x2 = arrowX + triangleHalfWidth;
                    double y2 = arrowY + triangleHeight;
                    double x3 = arrowX;
                    double y3 = arrowY;
                    gc.setFill(Color.WHITE); // Set fill color to white
                    gc.setStroke(Color.BLACK); // Set stroke color to black
                    gc.strokeLine(startX, startY, startX, endY);
                    gc.strokeLine(startX, endY, endX, endY);
                    gc.fillPolygon(new double[]{x1, x2, x3}, new double[]{y1, y2, y3}, 3);
                    gc.strokePolygon(new double[]{x1, x2, x3}, new double[]{y1, y2, y3}, 3);
                }
                break;


            case REALIZATION:
                // Vertical line from source to the same Y-coordinate as destination
                gc.setLineDashes(5);
                gc.strokeLine(startX, startY, startX, endY);
                // Horizontal dashed line from startX to endX at the same Y-coordinate
                gc.setLineDashes(5); // Set line style to dashed
                gc.strokeLine(startX, endY, endX, endY);

                // Adjust the triangle position based on the direction of the relationship
                if (startX < endX) {
                    // Triangle points right
                    double arrowX = endX;
                    double arrowY = endY;
                    double x1 = arrowX - triangleHalfWidth;
                    double y1 = arrowY - triangleHeight;
                    double x2 = arrowX - triangleHalfWidth;
                    double y2 = arrowY + triangleHeight;
                    double x3 = arrowX;
                    double y3 = arrowY;
                    gc.setFill(Color.WHITE); // Set fill color to white
                    gc.setStroke(Color.BLACK); // Set stroke color to black
                    gc.fillPolygon(new double[]{x1, x2, x3}, new double[]{y1, y2, y3}, 3);
                    gc.strokePolygon(new double[]{x1, x2, x3}, new double[]{y1, y2, y3}, 3);
                } else {
                    // Triangle points left
                    double arrowX = endX;
                    double arrowY = endY;
                    double x1 = arrowX + triangleHalfWidth;
                    double y1 = arrowY - triangleHeight;
                    double x2 = arrowX + triangleHalfWidth;
                    double y2 = arrowY + triangleHeight;
                    double x3 = arrowX;
                    double y3 = arrowY;
                    gc.setFill(Color.WHITE); // Set fill color to white
                    gc.setStroke(Color.BLACK); // Set stroke color to black
                    gc.fillPolygon(new double[]{x1, x2, x3}, new double[]{y1, y2, y3}, 3);
                    gc.strokePolygon(new double[]{x1, x2, x3}, new double[]{y1, y2, y3}, 3);
                }
                break;

            case COMPOSITION:
                // Vertical line from source to a little below destination
                gc.strokeLine(startX, startY, startX, endY + 20);
                // Horizontal line from startX to endX at the same Y-coordinate
                gc.strokeLine(startX, endY + 20, endX, endY + 20);
                // Draw the filled diamond shape at the end of the line
                gc.fillPolygon(new double[]{endX - 10, endX, endX + 10, endX}, new double[]{endY + 20, endY, endY + 20, endY + 40}, 4);
                break;
        }
    }




    private UMLClass findUMLClass(String className) {
        for (UMLClass umlClass : drawnUMLClasses) {
            if (umlClass.getName().equalsIgnoreCase(className)) {
                return umlClass;
            }
        }
        return null; // Class not found
    }




    @FXML
    void closeDiagramGui(ActionEvent event) {
        Platform.exit();
    }

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
                // Remove the class from the diagram
                diagram.removeClass(className);
                // Update the canvas by removing the specified class
                updateCanvasRemoveClass(umlClass.getName());
                showAlert("Success", "Class '" + className + "' has been removed.");
            } else {
                showAlert("Error", "Class '" + className + "' does not exist.");
            }
        }
    }
private void updateCanvasRemoveClass(String className) {
        UMLClass umlClassToRemove = null;

        // Find the specified class in the drawnUMLClasses list
        for (UMLClass umlClass : drawnUMLClasses) {
            if (umlClass.getName().equals(className)) {
                umlClassToRemove = umlClass;
                break;
            }
        }

        if (umlClassToRemove != null) {
            GraphicsContext gc = centerContent.getGraphicsContext2D();

            // Remove the specified class from the drawnUMLClasses list
            drawnUMLClasses.remove(umlClassToRemove);

            // Clear the entire region occupied by the class and its relationships on the canvas
            gc.clearRect(0, 0, centerContent.getWidth(), centerContent.getHeight());

            // Redraw all remaining classes (excluding the removed class)
            for (UMLClass existingClass : drawnUMLClasses) {
                if (!existingClass.getName().equals(className)) {
                    drawUMLClass(existingClass);
                }
            }

            // Remove the class from the diagram
            diagram.removeClass(className);

            // Notify the user of a successful class removal
            showAlert("Success", "Class '" + className + "' has been removed.");
        }
    }

    private void updateCanvas(UMLClass umlClass) {
        GraphicsContext gc = centerContent.getGraphicsContext2D();

        // Clear the entire canvas
        gc.clearRect(0, 0, centerContent.getWidth(), centerContent.getHeight());

        // Redraw all classes except the renamed class
        for (UMLClass existingClass : diagram.getClasses().values()) {
            if (existingClass != umlClass) {
                drawUMLClass(existingClass);
            }
        }

        // Redraw the renamed class with its updated name
        drawUMLClass(umlClass);
    }




    @FXML
    void deleteRelationshipGui(ActionEvent event) {

    }
    @FXML
    void renameAttributeGui(ActionEvent event) {
    }

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
                        updateCanvas(classToRename);

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
                    drawUMLClass(umlClass);
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
        assert addRelButton != null : "fx:id=\"addRelButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert centerContent != null : "fx:id=\"centerContent\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert deleteClassButton != null : "fx:id=\"deleteClassButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert deleteRelButton != null : "fx:id=\"deleteRelButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert renameAttrButton != null : "fx:id=\"renameAttrButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert renameClassButton != null : "fx:id=\"renameClassButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";

    }



}
