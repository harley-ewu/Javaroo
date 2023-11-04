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
import javafx.stage.Stage;
import javaroo.cmd.*;

import java.io.FileNotFoundException;
import java.io.IOException;
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
            addFieldsToClass(newUMLClass);

            // Prompt the user for methods
            addMethodsToClass(newUMLClass);

            // Add the new class to the UMLDiagram
            diagram.addClass(newUMLClass.getName());

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
            umlClass.addField(fieldName, fieldType, fieldVisibility);
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
            umlClass.addMethod(methodName, methodType, paramsList);
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
        List<UMLFields> fields = umlClass.getFields();
        List<UMLMethods> methods = umlClass.getMethods();

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

        // Create the UMLRelationship object with the selected relationship type
        UMLRelationships relationship = new UMLRelationships(sourceClass, destinationClass, selectedRelationshipType);

        // Now, draw the relationship on the canvas based on the type
        drawUMLRelationship(sourceClass, destinationClass, selectedRelationshipType);

        // Notify the user of a successful relationship creation
        showAlert("Success", "Relationship created successfully: " + sourceClassName + " " + selectedRelationshipType + " " + destinationClassName);
    }

    private void drawUMLRelationship(UMLClass sourceClass, UMLClass destinationClass, UMLRelationships.RelationshipType type) {
        GraphicsContext gc = centerContent.getGraphicsContext2D();

        // Calculate the center coordinates of the source and destination classes
        double sourceX = sourceClass.getX() + sourceClass.getWidth() / 2;
        double sourceY = sourceClass.getY() + sourceClass.getHeight() / 2;
        double destX = destinationClass.getX() + destinationClass.getWidth() / 2;
        double destY = destinationClass.getY() + destinationClass.getHeight() / 2;

        // Set line attributes for different relationship types
        gc.setLineWidth(2); // Set the line width as needed

        double offsetSourceX, offsetSourceY, offsetDestX, offsetDestY;

        // Determine whether to draw the relationship vertically or horizontally based on the shorter distance
        double dx = Math.abs(destX - sourceX);
        double dy = Math.abs(destY - sourceY);

        if (dx < dy) {
            // Draw vertically
            offsetSourceX = sourceX;
            offsetSourceY = sourceY + sourceClass.getHeight() / 2;
            offsetDestX = destX;
            offsetDestY = destinationClass.getY() + destinationClass.getHeight() / 2;
        } else {
            // Draw horizontally
            offsetSourceX = sourceX + sourceClass.getWidth() / 2;
            offsetSourceY = sourceY;
            offsetDestX = destinationClass.getX() + destinationClass.getWidth() / 2;
            offsetDestY = destY;
        }

        // Check for collisions with existing relationships
        for (UMLRelationships relationship : diagram.getRelationships()) {
            if (relationship.getSource() == sourceClass || relationship.getDest() == sourceClass ||
                    relationship.getSource() == destinationClass || relationship.getDest() == destinationClass) {
                // Skip relationships involving the same source or destination class
                continue;
            }

            double collisionX = 0;
            double collisionY = 0;

            if (lineSegmentsIntersect(offsetSourceX, offsetSourceY, offsetDestX, offsetDestY,
                    relationship.getStartX(), relationship.getStartY(), relationship.getEndX(), relationship.getEndY(),
                    collisionX, collisionY)) {
                // A collision has been detected, adjust the offset points
                offsetSourceX = collisionX;
                offsetSourceY = collisionY;
            }
        }

        switch (type) {
            case AGGREGATION:
                gc.setStroke(Color.BLACK);
                gc.strokeLine(offsetSourceX, offsetSourceY, offsetDestX, offsetDestY);
                // Draw a diamond arrow symbol at the destination class
                drawDiamondArrow(gc, offsetDestX, offsetDestY);
                break;

            case COMPOSITION:
                gc.setStroke(Color.BLACK);
                gc.strokeLine(offsetSourceX, offsetSourceY, offsetDestX, offsetDestY);
                // Draw a filled diamond symbol at the destination class
                drawFilledDiamond(gc, offsetDestX, offsetDestY);
                break;

            case REALIZATION:
                gc.setStroke(Color.BLACK);
                gc.setLineDashes(5, 5); // Dotted line for inheritance
                gc.strokeLine(offsetSourceX, offsetSourceY, offsetDestX, offsetDestY);
                gc.setLineDashes(); // Reset line dashes
                break;

            case INHERITANCE:
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(1); // Adjust line width as needed
                // Draw an open arrow symbol at the destination class
                drawOpenArrow(gc, offsetDestX, offsetDestY);
                break;

            default:
                // Handle any other cases if needed
        }
    }


    private boolean lineSegmentsIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double intersectionX, double intersectionY) {
        // Calculate the direction vectors of the two line segments
        double dx1 = x2 - x1;
        double dy1 = y2 - y1;
        double dx2 = x4 - x3;
        double dy2 = y4 - y3;

        // Calculate the determinant of the direction vectors
        double determinant = dx1 * dy2 - dx2 * dy1;

        if (determinant == 0) {
            // The lines are parallel, no intersection
            return false;
        }

        // Calculate the parameters for the parametric equations of the lines
        double t1 = ((x3 - x1) * dy2 - (y3 - y1) * dx2) / determinant;
        double t2 = ((x3 - x1) * dy1 - (y3 - y1) * dx1) / determinant;

        // Check if the intersection point is within the line segments
        if (t1 >= 0 && t1 <= 1 && t2 >= 0 && t2 <= 1) {
            intersectionX = x1 + t1 * dx1;
            intersectionY = y1 + t1 * dy1;
            return true;
        }

        // No intersection
        return false;
    }

    private void adjustCoordinatesToAvoidCollision(UMLRelationships existingRelationship, UMLRelationships newRelationship) {
        // Calculate the angle between the existing and new relationships
        double angle = Math.atan2(newRelationship.getEndY() - newRelationship.getStartY(), newRelationship.getEndX() - newRelationship.getStartX());

        // Define an offset to move the new relationship away from the existing relationship
        double offset = 20; // Adjust this value based on your preferences

        // Calculate the new coordinates for the end point of the new relationship
        double newX = newRelationship.getEndX() + offset * Math.cos(angle);
        double newY = newRelationship.getEndY() + offset * Math.sin(angle);

        // Update the coordinates of the new relationship to avoid collision
        newRelationship.setEndX(newX);
        newRelationship.setEndY(newY);
    }

    private boolean relationshipIntersects(UMLRelationships relationship1, UMLRelationships relationship2) {
        // Check if the two relationships intersect by comparing their bounding boxes
        double x1Start = relationship1.getStartX();
        double y1Start = relationship1.getStartY();
        double x1End = relationship1.getEndX();
        double y1End = relationship1.getEndY();

        double x2Start = relationship2.getStartX();
        double y2Start = relationship2.getStartY();
        double x2End = relationship2.getEndX();
        double y2End = relationship2.getEndY();

        // Check for intersection between the bounding boxes of the relationships
        return doLineSegmentsIntersect(x1Start, y1Start, x1End, y1End, x2Start, y2Start, x2End, y2End);
    }

    private boolean doLineSegmentsIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        // Calculate the direction vectors of the two line segments
        double dx1 = x2 - x1;
        double dy1 = y2 - y1;
        double dx2 = x4 - x3;
        double dy2 = y4 - y3;

        // Calculate the determinants
        double determinant = dx1 * dy2 - dx2 * dy1;

        if (determinant == 0) {
            // The lines are parallel, and we consider them non-intersecting
            return false;
        } else {
            // Calculate the parameters (u, v) for the intersection point
            double u = ((x3 - x1) * dy2 - (y3 - y1) * dx2) / determinant;
            double v = ((x3 - x1) * dy1 - (y3 - y1) * dx1) / determinant;

            // Check if the intersection point is within both line segments
            return (u >= 0 && u <= 1) && (v >= 0 && v <= 1);
        }
    }




    private void drawDiamondArrow(GraphicsContext gc, double x, double y) {
        // Customize the drawing of a diamond arrow as needed
        // Example:
        gc.setFill(Color.BLACK);
        gc.fillPolygon(new double[]{x - 10, x, x + 10, x},
                new double[]{y, y - 10, y, y + 10}, 4);
    }

    private void drawFilledDiamond(GraphicsContext gc, double x, double y) {
        // Customize the drawing of a filled diamond as needed
        // Example:
        gc.setFill(Color.BLACK);
        gc.fillPolygon(new double[]{x - 10, x, x + 10, x},
                new double[]{y, y - 10, y, y + 10}, 4);
    }

    private void drawOpenArrow(GraphicsContext gc, double x, double y) {
        // Customize the drawing of an open arrow as needed
        // Example:
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2); // Adjust line width as needed
        gc.strokeLine(x - 10, y - 10, x, y);
        gc.strokeLine(x, y, x + 10, y + 10);
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
                // Update the canvas
                updateCanvas(umlClass);
                showAlert("Success", "Class '" + className + "' has been removed.");
            } else {
                showAlert("Error", "Class '" + className + "' does not exist.");
            }
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
    void loadDiagramGui(ActionEvent event) {

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