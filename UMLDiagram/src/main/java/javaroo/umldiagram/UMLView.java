package javaroo.umldiagram;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
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
        List<UMLFields> fields = umlClass.getFields();
        List<UMLMethods> methods = umlClass.getMethods();

        // Calculate the width and height based on text size
        double boxWidth = 0;
        double boxHeight = 0;

        // Constants for minimum and maximum box sizes
        final double MIN_BOX_WIDTH = 150;
        final double MAX_BOX_WIDTH = 300;
        final double MIN_BOX_HEIGHT = 100;
        final double MAX_BOX_HEIGHT = 200;

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

        // Adjust the box width and height to fit within the standardized sizes
        boxWidth = Math.min(Math.max(boxWidth, MIN_BOX_WIDTH), MAX_BOX_WIDTH);
        boxHeight = Math.min(Math.max(boxHeight, MIN_BOX_HEIGHT), MAX_BOX_HEIGHT);

        // Draw the UML class box with a white fill and black outline
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.fillRect(x, y, boxWidth, boxHeight);
        gc.strokeRect(x, y, boxWidth, boxHeight);

        // Draw the class name, fields, and methods inside the box
        // ... (rest of the drawing code remains the same) ...

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


        //makeClassDraggable(umlClass);
        //toggleClassDetails(umlClass);
        //adjustZoomAndCenter();
        // Adjust the view to center on the class
        centerOnClass(umlClass, centerScrollPane);
        // ... (rest of your existing code) ...
    }

    // Helper method to format the visibility symbol for fields
    private String formatVisibility(String visibility) {
        return switch (visibility.toLowerCase()) {
            case "public" -> "+";
            case "private" -> "-";
            default -> "?";
        };
    }

    void autoAssignCoordinatesGrid(UMLClass umlClass) {
        int totalClasses = controller.drawnUMLClasses.size() + 1;
        int cols = (int) Math.ceil(Math.sqrt(totalClasses));
        double gridWidth = centerContent.getWidth() / cols;
        double gridHeight = centerContent.getHeight() / cols;

        int index = controller.drawnUMLClasses.indexOf(umlClass);
        if (index == -1) {
            index = totalClasses - 1; // Position for the new class
        }

        // Calculate the top-left corner of the grid cell
        double cellX = (index % cols) * gridWidth;
        double cellY = (index / cols) * gridHeight;

        // Center the UMLClass in its grid cell
        double x = cellX + (gridWidth - umlClass.getWidth()) / 2;
        double y = cellY + (gridHeight - umlClass.getHeight()) / 2;

        // Adjust for overall centering in the ScrollPane
        double totalWidth = cols * gridWidth;
        double totalHeight = (int) Math.ceil((double) totalClasses / cols) * gridHeight;
        double offsetX = (centerContent.getWidth() - totalWidth) / 2;
        double offsetY = (centerContent.getHeight() - totalHeight) / 2;

        umlClass.setPosition(x + offsetX, y + offsetY);
    }

    void updateCanvas(UMLDiagram diagram, UMLClass umlClass) {
        GraphicsContext gc = centerContent.getGraphicsContext2D();

        // Clear the entire canvas
        gc.clearRect(0, 0, centerContent.getWidth(), centerContent.getHeight());

        // Redraw all classes in the specified diagram except the updated class
        for (UMLClass existingClass : diagram.getClasses().values()) {
            if (!existingClass.getName().equals(umlClass.getName())) {
                drawUMLClass(existingClass);
            }
        }

        // Redraw the updated class
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

    public void adjustViewAfterDrawing(UMLClass newUmlClass) {
        // Calculate the bounding box of all UML diagrams
        Rectangle2D boundingBox = calculateBoundingBox();

        // Adjust zoom and focus
        adjustZoomAndFocus(newUmlClass, boundingBox);
    }

    private Rectangle2D calculateBoundingBox() {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = 0;
        double maxY = 0;

        for (UMLClass umlClass : controller.drawnUMLClasses) {
            minX = Math.min(minX, umlClass.getX());
            minY = Math.min(minY, umlClass.getY());
            maxX = Math.max(maxX, umlClass.getX() + umlClass.getWidth());
            maxY = Math.max(maxY, umlClass.getY() + umlClass.getHeight());
        }

        return new Rectangle2D(minX, minY, maxX - minX, maxY - minY);
    }

    private void adjustZoomAndFocus(UMLClass umlClass, Rectangle2D boundingBox) {
        GraphicsContext gc = centerContent.getGraphicsContext2D();

        // Calculate zoom factor (this is a simplified example)
        double zoomFactor = Math.min(centerContent.getWidth() / boundingBox.getWidth(),
                centerContent.getHeight() / boundingBox.getHeight());

        // Apply zoom factor to graphics context
        gc.scale(zoomFactor, zoomFactor);

        // Focus on the newly added UML class
        centerContent.setTranslateX(-umlClass.getX() * zoomFactor);
        centerContent.setTranslateY(-umlClass.getY() * zoomFactor);
    }

    public void drawUpdatedClass(UMLClass updatedClass) {
        GraphicsContext gc = centerContent.getGraphicsContext2D();
        gc.clearRect(updatedClass.getX(), updatedClass.getY(), updatedClass.getWidth(), updatedClass.getHeight());
        drawUMLClass(updatedClass);
    }


    private void centerOnClass(UMLClass umlClass, ScrollPane scrollPane) {
        // Calculate the center position of the class
        double centerX = umlClass.getX() + umlClass.getWidth() / 2.0;
        double centerY = umlClass.getY() + umlClass.getHeight() / 2.0;

        // Assuming you have a reference to the container (like ScrollPane) holding your classes
        // Calculate the position to scroll to
        double hvalue = centerX / scrollPane.getContent().getBoundsInLocal().getWidth();
        double vvalue = centerY / scrollPane.getContent().getBoundsInLocal().getHeight();

        // Adjust the scrollPane's hvalue and vvalue to center on the new class
        scrollPane.setHvalue(hvalue);
        scrollPane.setVvalue(vvalue);
    }
}
