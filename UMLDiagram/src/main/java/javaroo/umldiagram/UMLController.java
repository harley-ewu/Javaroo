package javaroo.umldiagram;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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
import javaroo.cmd.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class UMLController {

//    @FXML // ResourceBundle that was given to the FXMLLoader
//    private ResourceBundle resources;
//
//    @FXML // URL location of the FXML file that was given to the FXMLLoader
//    private URL location;
//
//    @FXML // fx:id="addClassButton"
//    private Button addClassButton; // Value injected by FXMLLoader
//
//    @FXML // fx:id="addRelButton"
//    private Button addRelButton; // Value injected by FXMLLoader
//
//    @FXML // fx:id="centerContent"
//    private Canvas centerContent; // Value injected by FXMLLoader
//    @FXML //
//    private ScrollPane centerScrollPane;
//
//    //private double xOffset = 0;
//    //private double yOffset = 0;
//
//    @FXML // fx:id="deleteClassButton"
//    private Button deleteClassButton; // Value injected by FXMLLoader
//
//    @FXML // fx:id="deleteRelButton"
//    private Button deleteRelButton; // Value injected by FXMLLoader
//
//    @FXML // fx:id="renameAttrButton"
//    private Button renameAttrButton; // Value injected by FXMLLoader
//
//    @FXML // fx:id="renameClassButton"
//    private Button renameClassButton; // Value injected by FXMLLoader
//
//    // List to store the drawn UML classes
//    private List<UMLClass> drawnUMLClasses = new ArrayList<>();
//
//    @FXML
//    void addClassGui(ActionEvent event) {
//        TextInputDialog classNameDialog = new TextInputDialog();
//        classNameDialog.setTitle("Add UML Class");
//        classNameDialog.setHeaderText("Enter the class name:");
//        classNameDialog.setContentText("Class Name:");
//
//        Optional<String> classNameResult = classNameDialog.showAndWait();
//        if (!classNameResult.isPresent() || classNameResult.get().isEmpty()) {
//            // User canceled the input or provided an empty string for the class name
//            return;
//        }
//
//        String className = classNameResult.get();
//
//        UMLClass existingClass = UMLClass.getClass(className);
//
//        if (existingClass != null) {
//            showAlert("Error", "Class '" + className + "' already exists.");
//            return;
//        }
//
//        // Prompt the user for attributes in a loop
//        ArrayList<String> attributesList = new ArrayList<>();
//        while (true) {
//            TextInputDialog attributeDialog = new TextInputDialog();
//            attributeDialog.setTitle("Add Attribute");
//            attributeDialog.setHeaderText("Enter an attribute for the class (or press Cancel to finish):");
//            attributeDialog.setContentText("Attribute:");
//
//            Optional<String> attributeResult = attributeDialog.showAndWait();
//            if (!attributeResult.isPresent()) {
//                // User finished adding attributes
//                break;
//            }
//
//            String attribute = attributeResult.get();
//
//            if (attribute.isEmpty()) {
//                // User entered an empty attribute, which is not allowed
//                showAlert("Error", "Attribute cannot be empty.");
//                continue;
//            }
//
//            if (attributesList.contains(attribute)) {
//                showAlert("Error", "Attribute '" + attribute + "' already exists.");
//                continue;
//            }
//
//            attributesList.add(attribute);
//        }
//
//        // Create the UMLClass with coordinates and add attributes
//        UMLClass newUMLClass = new UMLClass(className, 0, 0);
//        if (newUMLClass != null) {
//            for (String attribute : attributesList) {
//                UMLAttributes.addAttribute(newUMLClass, attribute);
//            }
//
//            // Update the visual representation
//
//            autoAssignCoordinates(newUMLClass);
//            drawnUMLClasses.add(newUMLClass);
//
//            drawUMLClass(newUMLClass);
//
//            showClassCoordinates(newUMLClass);
//
//            showAlert("Success", "Class '" + className + "' added with attributes.");
//        }
//    }
//
//
//    private void showClassCoordinates(UMLClass umlClass) {
//        double x = umlClass.getX();
//        double y = umlClass.getY();
//
//        String message = "Class '" + umlClass.getName() + "' created at X: " + x + ", Y: " + y;
//        showAlert("Class Created", message);
//    }
//
//    private void drawUMLClass(UMLClass umlClass) {
//        double x = umlClass.getX(); // Get the X-coordinate from the UMLClass
//        double y = umlClass.getY(); // Get the Y-coordinate from the UMLClass
//        double xPadding = 40; // Increased horizontal padding between classes
//        double textPadding = 20; // Increased padding for text inside the box
//
//        GraphicsContext gc = centerContent.getGraphicsContext2D();
//
//        gc.setLineWidth(0.75);
//
//        // Get the class name and attributes
//        String className = umlClass.getName();
//        List<UMLAttributes> attributes = umlClass.getAttributes();
//
//        // Calculate the width and height based on text size
//        double boxWidth = 0;
//        double boxHeight = 0;
//
//        // Calculate width based on class name
//        Text classText = new Text(className);
//        classText.setFont(Font.font("Inter", FontWeight.NORMAL, FontPosture.REGULAR, 12));
//        boxWidth = Math.max(boxWidth, classText.getLayoutBounds().getWidth() + 2 * textPadding);
//
//        // Calculate width based on attributes
//        for (UMLAttributes attribute : attributes) {
//            Text attrText = new Text("- " + attribute.getName());
//            attrText.setFont(Font.font("Inter", FontWeight.NORMAL, FontPosture.REGULAR, 12));
//            boxWidth = Math.max(boxWidth, attrText.getLayoutBounds().getWidth() + 2 * textPadding);
//            boxHeight += attrText.getLayoutBounds().getHeight() + textPadding;
//        }
//
//        boxHeight += textPadding; // Add padding for class name
//
//        // Draw the UML class box with a white fill and black outline
//        gc.setFill(Color.WHITE);
//        gc.setStroke(Color.BLACK);
//        gc.fillRect(x, y, boxWidth, boxHeight);
//        gc.strokeRect(x, y, boxWidth, boxHeight);
//
//        // Draw the class name inside the box
//        gc.strokeText(className, x + textPadding, y + textPadding);
//
//        double attributeY = y + textPadding + classText.getLayoutBounds().getHeight();
//        for (UMLAttributes attribute : attributes) {
//            // Draw each attribute inside the box
//            gc.strokeText("- " + attribute.getName(), x + textPadding, attributeY);
//            attributeY += textPadding + classText.getLayoutBounds().getHeight();
//        }
//    }
//
//    private void autoAssignCoordinates(UMLClass umlClass) {
//        double xPadding = 80; // Horizontal padding between classes
//        double yPadding = 80; // Vertical padding between classes
//        double minX = 100; // Minimum X-coordinate
//        double minY = 100; // Minimum Y-coordinate
//        double maxX = centerContent.getWidth() - umlClass.getWidth() - xPadding;
//        double maxY = centerContent.getHeight() - umlClass.getHeight() - yPadding;
//
//        if (drawnUMLClasses.isEmpty()) {
//            // If there are no existing classes, assign a random position within the bounds
//            double x = minX + (maxX - minX) * Math.random();
//            double y = minY + (maxY - minY) * Math.random();
//            umlClass.setPosition(x, y);
//            umlClass.addClassWithCoordinates(umlClass.getName(), umlClass.getX(), umlClass.getY());
//            return;
//        }
//
//        boolean placed = false;
//        double x = 0;
//        double y = 0;
//
//        while (!placed) {
//            boolean collision = false;
//            // Randomly select a position within the bounds
//            x = minX + (maxX - minX) * Math.random();
//            y = minY + (maxY - minY) * Math.random();
//
//            for (UMLClass existingClass : drawnUMLClasses) {
//                if (x + umlClass.getWidth() + xPadding > existingClass.getX() &&
//                        existingClass.getX() + existingClass.getWidth() + xPadding > x &&
//                        y + umlClass.getHeight() + yPadding > existingClass.getY() &&
//                        existingClass.getY() + existingClass.getHeight() + yPadding > y) {
//                    // There's a collision, classes are too close, choose a new random position
//                    collision = true;
//                    break;
//                }
//            }
//
//            if (!collision) {
//                // Check if the new class collides with any other class in the drawing
//                boolean classCollision = false;
//                for (UMLClass existingClass : drawnUMLClasses) {
//                    if (x < existingClass.getX() + existingClass.getWidth() + xPadding &&
//                            x + umlClass.getWidth() + xPadding > existingClass.getX() &&
//                            y < existingClass.getY() + existingClass.getHeight() + yPadding &&
//                            y + umlClass.getHeight() + yPadding > existingClass.getY()) {
//                        classCollision = true;
//                        break;
//                    }
//                }
//
//                if (!classCollision) {
//                    // No collision with other classes, place the class at (x, y)
//                    umlClass.setPosition(x, y);
//                    placed = true;
//                }
//            }
//        }
//    }
//
//    @FXML
//    void addRelationshipGui(ActionEvent event) {
//        if (drawnUMLClasses.isEmpty()) {
//            showAlert("Error", "No UML classes have been created to establish a relationship.");
//            return;
//        }
//
//        TextInputDialog sourceClassDialog = new TextInputDialog();
//        sourceClassDialog.setTitle("Add Relationship");
//        sourceClassDialog.setHeaderText("Enter the source class name:");
//        sourceClassDialog.setContentText("Source Class:");
//
//        Optional<String> sourceClassResult = sourceClassDialog.showAndWait();
//        if (!sourceClassResult.isPresent() || sourceClassResult.get().isEmpty()) {
//            showAlert("Error", "Source class name is required.");
//            return;
//        }
//
//        String sourceClassName = sourceClassResult.get();
//        UMLClass sourceClass = findUMLClass(sourceClassName);
//
//        if (sourceClass == null) {
//            showAlert("Error", "Source class '" + sourceClassName + "' does not exist.");
//            return;
//        }
//
//        TextInputDialog destinationClassDialog = new TextInputDialog();
//        destinationClassDialog.setTitle("Add Relationship");
//        destinationClassDialog.setHeaderText("Enter the destination class name:");
//        destinationClassDialog.setContentText("Destination Class:");
//
//        Optional<String> destinationClassResult = destinationClassDialog.showAndWait();
//        if (!destinationClassResult.isPresent() || destinationClassResult.get().isEmpty()) {
//            showAlert("Error", "Destination class name is required.");
//            return;
//        }
//
//        String destinationClassName = destinationClassResult.get();
//        UMLClass destinationClass = findUMLClass(destinationClassName);
//
//        if (destinationClass == null) {
//            showAlert("Error", "Destination class '" + destinationClassName + "' does not exist.");
//            return;
//        }
//
//        // Both source and destination classes exist, so draw the arrow between them.
//        drawUMLRelationship(sourceClass, destinationClass);
//    }
//
//    private UMLClass findUMLClass(String className) {
//        for (UMLClass umlClass : drawnUMLClasses) {
//            if (umlClass.getName().equalsIgnoreCase(className)) {
//                return umlClass;
//            }
//        }
//        return null; // Class not found
//    }
//
//
//    private void showRelationshipCoordinates(UMLClass sourceClass, UMLClass destinationClass, double startX, double startY, double endX, double endY) {
//        String message = "Relationship line created from '" + sourceClass.getName() + "' (X: " + startX + ", Y: " + startY + ") to '" + destinationClass.getName() + "' (X: " + endX + ", Y: " + endY + ")";
//        showAlert("Relationship Created", message);
//    }
//
//    private void drawDiamondArrow(GraphicsContext gc, double x, double y, double size) {
//        double halfSize = size * 0.5;
//        double x1 = x - halfSize;
//        double y1 = y;
//        double x2 = x;
//        double y2 = y - halfSize;
//        double x3 = x + halfSize;
//        double y3 = y;
//        double x4 = x;
//        double y4 = y + halfSize;
//
//        gc.setFill(Color.WHITE);
//        gc.setStroke(Color.BLACK); // Set the stroke color to black
//        gc.setLineWidth(2); // Adjust the stroke width as needed
//
//        gc.fillPolygon(new double[]{x1, x2, x3, x4}, new double[]{y1, y2, y3, y4}, 4);
//        gc.strokePolygon(new double[]{x1, x2, x3, x4}, new double[]{y1, y2, y3, y4}, 4);
//    }
//
//
//    private void drawUMLRelationship(UMLClass sourceClass, UMLClass destinationClass) {
//        GraphicsContext gc = centerContent.getGraphicsContext2D();
//
//        // Calculate the center coordinates of the source and destination classes
//        double sourceX = sourceClass.getX() + sourceClass.getWidth() / 2;
//        double sourceY = sourceClass.getY() + sourceClass.getHeight() / 2;
//        double destX = destinationClass.getX() + destinationClass.getWidth() / 2;
//        double destY = destinationClass.getY() + destinationClass.getHeight() / 2;
//
//        // Determine the direction of the relationship (vertical or horizontal)
//        boolean isVertical = Math.abs(sourceX - destX) < Math.abs(sourceY - destY);
//
//        if (isVertical) {
//            // Draw a vertical line from source to destination
//            gc.setStroke(Color.BLACK);
//            gc.setLineWidth(1.5);
//            gc.strokeLine(sourceX, sourceY, sourceX, destY);
//
//            // Draw a horizontal line from the vertical line to the destination
//            gc.strokeLine(sourceX, destY, destX, destY);
//        } else {
//            // Draw a horizontal line from source to destination
//            gc.setStroke(Color.BLACK);
//            gc.setLineWidth(1.5);
//            gc.strokeLine(sourceX, sourceY, destX, sourceY);
//
//            // Draw a vertical line from the horizontal line to the destination
//            gc.strokeLine(destX, sourceY, destX, destY);
//        }
//
//        // Draw a diamond-shaped arrow at the end of the line
//        double arrowSize = 15; // Adjust the size of the diamond arrow as needed
//        if (isVertical) {
//            drawDiamondArrow(gc, destX, destY, arrowSize);
//        } else {
//            drawDiamondArrow(gc, destX, destY, arrowSize);
//        }
//
//        // Show the relationship coordinates to the user
//        showRelationshipCoordinates(sourceClass, destinationClass, sourceX, sourceY, destX, destY);
//    }
//
//
//
//
//    @FXML
//    void closeDiagramGui(ActionEvent event) {
//
//    }
//
//    @FXML
//    void deleteClassGui(ActionEvent event) {
//
//    }
//
//    @FXML
//    void deleteRelationshipGui(ActionEvent event) {
//
//    }
//
//    @FXML
//    void loadDiagramGui(ActionEvent event) {
//
//    }
//
//    @FXML
//    void renameAttributeGui(ActionEvent event) {
//
//    }
//
//    @FXML
//    void renameClassGui(ActionEvent event) {
//
//    }
//
//    @FXML
//    void saveDiagramGui(ActionEvent event) {
//
//    }
//
//    private void showAlert(String title, String content) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(content);
//        alert.showAndWait();
//    }
//
//
//
//    @FXML // This method is called by the FXMLLoader when initialization is complete
//    void initialize() {
//        assert addClassButton != null : "fx:id=\"addClassButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
//        assert addRelButton != null : "fx:id=\"addRelButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
//        assert centerContent != null : "fx:id=\"centerContent\" was not injected: check your FXML file 'UMLCreator.fxml'.";
//        assert deleteClassButton != null : "fx:id=\"deleteClassButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
//        assert deleteRelButton != null : "fx:id=\"deleteRelButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
//        assert renameAttrButton != null : "fx:id=\"renameAttrButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
//        assert renameClassButton != null : "fx:id=\"renameClassButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
//
//    }
//


}
