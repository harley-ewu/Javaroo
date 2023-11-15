package javaroo.umldiagram;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javaroo.cmd.*;

import java.util.ArrayList;
import java.util.List;

public class UMLView {
    private final UMLController controller;
    private Canvas centerContent;
    private ScrollPane centerScrollPane;

    public UMLView(UMLController controller) {
        this.controller = controller;
    }

    public void initializeComponents(Canvas centerContent, ScrollPane centerScrollPane) {
        this.centerContent = centerContent;
        this.centerScrollPane = centerScrollPane;
    }

    void drawUMLClass(UMLClass umlClass) {
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
        List<UMLFields> fields = controller.diagram.classExists(umlClass.getName()).getFields();
        List<UMLMethods> methods = controller.diagram.classExists(umlClass.getName()).getMethods();

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
        return switch (visibility.toLowerCase()) {
            case "public" -> "+";
            case "private" -> "-";
            default -> "?";
        };
    }

    // Helper method to check for class collisions, ensures each class is not touching or overlapping
    void autoAssignCoordinates(UMLClass umlClass) {
        double xPadding = 80; // Horizontal padding between classes
        double yPadding = 80; // Vertical padding between classes
        double minX = 100; // Minimum X-coordinate
        double minY = 100; // Minimum Y-coordinate
        double maxX = centerContent.getWidth() - umlClass.getWidth() - xPadding;
        double maxY = centerContent.getHeight() - umlClass.getHeight() - yPadding;

        if (controller.drawnUMLClasses.isEmpty()) {
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

            for (UMLClass existingClass : controller.drawnUMLClasses) {
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
                for (UMLClass existingClass : controller.drawnUMLClasses) {
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

    void updateCanvas(UMLDiagram diagram, UMLClass umlClass) {
        GraphicsContext gc = centerContent.getGraphicsContext2D();

        // Clear the entire canvas
        gc.clearRect(0, 0, centerContent.getWidth(), centerContent.getHeight());

        // Redraw all classes in the specified diagram except the renamed class
        for (UMLClass existingClass : diagram.getClasses().values()) {
            if (existingClass != umlClass) {
                drawUMLClass(existingClass);
            }
        }

        // Redraw the renamed class with its updated name
        drawUMLClass(umlClass);
    }

    void drawUMLRelationship(UMLClass sourceClass, UMLClass destinationClass, UMLRelationships.RelationshipType relationshipType) {
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

    void updateCanvasRemoveClass(String className) {
        UMLClass umlClassToRemove = null;
        List<UMLRelationships> relationshipsToRemove = new ArrayList<>();

        // Find the specified class in the drawnUMLClasses list
        for (UMLClass umlClass : controller.drawnUMLClasses) {
            if (umlClass.getName().equals(className)) {
                umlClassToRemove = umlClass;
                break;
            }
        }

        if (umlClassToRemove != null) {
            GraphicsContext gc = centerContent.getGraphicsContext2D();

            // Identify and remove the relationships involving the deleted class
            for (UMLRelationships relationship : controller.drawnUMLRelationships) {
                if (relationship.getSource() == umlClassToRemove || relationship.getDest() == umlClassToRemove) {
                    relationshipsToRemove.add(relationship);
                }
            }

            // Remove the specified class from the drawnUMLClasses list
            controller.drawnUMLClasses.remove(umlClassToRemove);

            // Clear the entire region occupied by the class and its relationships on the canvas
            gc.clearRect(0, 0, centerContent.getWidth(), centerContent.getHeight());

            // Redraw all remaining classes (excluding the removed class)
            for (UMLClass existingClass : controller.drawnUMLClasses) {
                if (!existingClass.getName().equals(className)) {
                    drawUMLClass(existingClass);
                }
            }

            // Remove the relationships involving the deleted class from the list
            controller.drawnUMLRelationships.removeAll(relationshipsToRemove);

            // Redraw the remaining relationships
            for (UMLRelationships relationship : controller.drawnUMLRelationships) {
                drawExistingRelationships();
            }

            // Remove the class from the diagram
            controller.diagram.removeClass(className);
        }
    }


    // Helper method to updated the contents of the screen
    void updateCanvas(UMLClass umlClass) {
        GraphicsContext gc = centerContent.getGraphicsContext2D();

        // Clear the entire canvas
        gc.clearRect(0, 0, centerContent.getWidth(), centerContent.getHeight());

        // Redraw all classes except the renamed class
        for (UMLClass existingClass : controller.diagram.getClasses().values()) {
            if (existingClass != umlClass) {
                drawUMLClass(existingClass);
            }
        }

        // Redraw the renamed class with its updated name
        drawUMLClass(umlClass);
    }

    private void drawExistingRelationships() {
        GraphicsContext gc = centerContent.getGraphicsContext2D();
        gc.setLineWidth(1.0);

        for (UMLRelationships relationship : controller.drawnUMLRelationships) {
            UMLClass sourceClass = relationship.getSource();
            UMLClass destinationClass = relationship.getDest();
            UMLRelationships.RelationshipType relationshipType = relationship.getType();

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
    }

    @FXML
    void clearRelationshipFromCanvas(ActionEvent event, UMLRelationships relationshipToRemove) {
        // Clear the entire canvas
        GraphicsContext gc = centerContent.getGraphicsContext2D();
        gc.clearRect(0, 0, centerContent.getWidth(), centerContent.getHeight());

        // Redraw all remaining classes
        for (UMLClass umlClass : controller.drawnUMLClasses) {
            drawUMLClass(umlClass);
        }

        // Redraw the remaining relationships, excluding the one to be removed
        for (UMLRelationships relationship : controller.drawnUMLRelationships) {
            if (relationship != relationshipToRemove) {
                drawExistingRelationships();
            }
        }

        // Notify the user of a successful relationship removal
        showAlert("Success", "Relationship deleted.");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void drawUpdatedClass(UMLClass updatedClass) {
        GraphicsContext gc = centerContent.getGraphicsContext2D();
        gc.clearRect(updatedClass.getX(), updatedClass.getY(), updatedClass.getWidth(), updatedClass.getHeight());
        drawUMLClass(updatedClass);
    }




}
