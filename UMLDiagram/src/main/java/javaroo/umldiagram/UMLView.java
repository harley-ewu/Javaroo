package javaroo.umldiagram;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javaroo.cmd.*;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.List;


public class UMLView {
    private final UMLController controller;
    private Canvas centerContent;
    private ScrollPane centerScrollPane;

    private GraphicsContext gc;


    public UMLView(UMLController controller) {
        this.controller = controller;
    }

    public void initializeComponents(Canvas centerContent, ScrollPane centerScrollPane) {
        this.centerContent = centerContent;
        this.centerScrollPane = centerScrollPane;

    }



    void drawUMLClass(UMLClass umlClass) {
        double zoomLevel = 0.5;
        GraphicsContext gc = centerContent.getGraphicsContext2D();

        double boxWidth = 150 * zoomLevel;
        double boxHeight = 100 * zoomLevel;
        double textPadding = 20 * zoomLevel;

// Scale line width
        gc.setLineWidth(0.75 * zoomLevel);
        //gc.setLineWidth(0.75);

        // Define fonts
        Font classFont = Font.font("Serif", FontWeight.BOLD, 20);
        Font fieldMethodFont = Font.font("Serif", FontWeight.NORMAL, 16);

        // Get the class name, fields, and methods
        String className = umlClass.getName();
        List<UMLFields> fields = umlClass.getFields();
        List<UMLMethods> methods = umlClass.getMethods();

        // Initial box dimensions
//        double boxWidth = 150;
//        double boxHeight = 100;
//        double textPadding = 20;

        // Calculate box dimensions
        Text tempText = new Text();
        tempText.setFont(classFont);
        tempText.setText(className);
        boxWidth = Math.max(boxWidth, tempText.getLayoutBounds().getWidth() + 2 * textPadding);
        boxHeight += tempText.getLayoutBounds().getHeight() + textPadding;

        tempText.setFont(fieldMethodFont);
        for (UMLFields field : fields) {
            tempText.setText(field.getName() + " : " + field.getType());
            boxWidth = Math.max(boxWidth, tempText.getLayoutBounds().getWidth() + 2 * textPadding);
            boxHeight += tempText.getLayoutBounds().getHeight() + textPadding;
        }

        if (!fields.isEmpty() && !methods.isEmpty()) {
            boxHeight += textPadding;
        }

        for (UMLMethods method : methods) {
            tempText.setText(method.getName() + "(" + String.join(", ", method.getParameters()) + ") : " + method.getReturnType());
            boxWidth = Math.max(boxWidth, tempText.getLayoutBounds().getWidth() + 2 * textPadding);
            boxHeight += tempText.getLayoutBounds().getHeight() + textPadding;
        }

//         Draw the UML class box
        double x = umlClass.getX();
        double y = umlClass.getY();
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.fillRect(x, y, boxWidth, boxHeight);
        gc.strokeRect(x, y, boxWidth, boxHeight);

        // Draw class name
        gc.setFont(classFont);
        gc.setFill(Color.BLACK);
        double contentY = y + tempText.getLayoutBounds().getHeight() + textPadding;
        gc.fillText(className, x + textPadding, contentY);

        // Draw a line separator between class name and fields
        gc.strokeLine(x, contentY + textPadding / 2, x + boxWidth, contentY + textPadding / 2);
        contentY += textPadding;

        // Draw fields and methods
        gc.setFont(fieldMethodFont);
        for (UMLFields field : fields) {
            contentY += tempText.getLayoutBounds().getHeight() + textPadding;
            gc.fillText(formatVisibility(field.getVisibility()) + " " + field.getName() + " : " + field.getType(), x + textPadding, contentY);

        }

        if (!fields.isEmpty() && !methods.isEmpty()) {
            contentY += textPadding;
            gc.strokeLine(x, contentY, x + boxWidth, contentY);
            contentY += textPadding;
        }

        for (UMLMethods method : methods) {
            contentY += tempText.getLayoutBounds().getHeight() + textPadding;
            gc.fillText(method.getName() + "(" + String.join(", ", method.getParameters()) + ") : " + method.getReturnType(), x + textPadding, contentY);
        }


        centerOnClass(umlClass, centerScrollPane);
    }


    // Helper method to format the visibility symbol for fields
    private String formatVisibility(String visibility) {
        return switch (visibility.toLowerCase()) {
            case "public" -> "+";
            case "private" -> "-";
            default -> "?";
        };
    }

//

    void autoAssignCoordinatesGrid(UMLClass umlClass) {
        int totalClasses = controller.drawnUMLClasses.size() + 1;
        final int MAX_CLASSES_PER_ROW = 4;
        int cols = Math.min(MAX_CLASSES_PER_ROW, totalClasses);

        double maxClassWidth = 0;
        double maxClassHeight = 0;
        for (UMLClass cls : controller.drawnUMLClasses) {
            maxClassWidth = Math.max(maxClassWidth, cls.getWidth());
            maxClassHeight = Math.max(maxClassHeight, cls.getHeight());
        }

        double gridWidth = Math.max(centerContent.getWidth() / cols, maxClassWidth);
        double gridHeight = Math.max(centerContent.getHeight() / cols, maxClassHeight);

        // Spacing reduction for all rows, including the first
        double verticalSpacingReduction = 0.4; // Adjust this value as needed

        if (!controller.drawnUMLClasses.contains(umlClass)) {
            controller.drawnUMLClasses.add(umlClass);
        }
        int index = controller.drawnUMLClasses.indexOf(umlClass);

        double x, y;

        if (index == 0) {
            x = (centerContent.getWidth() - umlClass.getWidth()) / 2;
            y = 0;
        } else {
            index--;
            int row = index / MAX_CLASSES_PER_ROW;
            int col = index % MAX_CLASSES_PER_ROW;

            double cellX = col * gridWidth;
            double cellY = row * gridHeight;

            x = cellX + (gridWidth - umlClass.getWidth()) / 2;
            y = cellY + (gridHeight - umlClass.getHeight()) / 2;

            // Reduce the y-coordinate for all rows
            y -= verticalSpacingReduction * row;
        }
        if(y==0){y+= 60;}
        umlClass.setPosition(x, y);
    }

    private double zoomScale = 1.0;

    public void zoomIn() {
        zoomScale *= 1.1; // Increase scale by 10%
        applyZoom();
    }

    public void zoomOut() {
        zoomScale *= 0.9; // Decrease scale by 10%
        applyZoom();
    }

    private void applyZoom() {
        centerContent.setScaleX(zoomScale);
        centerContent.setScaleY(zoomScale);
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
        double startX = sourceX + 10;
        double startY = sourceY;
        double endX = destX + 10;
        double endY = destY - 40;

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
                endY -= 13;
                if(startY<=endY) {
                    gc.strokeLine(startX, startY, startX, startY-20);
                    gc.strokeLine(startX, startY - 20, endX, startY - 20);
                    gc.strokeLine(endX, startY - 20, endX, endY + 20);
                }
                else{
                    gc.strokeLine(startX, startY, startX, endY+15);
                    gc.strokeLine(startX, endY+15, endX, endY+15);
                }
                // Draw the diamond shape at the end of the line
                gc.strokePolygon(new double[]{endX - 10, endX, endX + 10, endX}, new double[]{endY + 37, endY+17, endY + 37, endY + 57}, 4);
                break;

            case INHERITANCE:
                gc.setFill(Color.WHITE); // Set fill color to white
                gc.setStroke(Color.BLACK); // Set stroke color to black
                endY -= 13;
                if(startY<=endY) {
                    gc.strokeLine(startX, startY, startX, startY-20);
                    gc.strokeLine(startX, startY - 20, endX, startY - 20);
                    gc.strokeLine(endX, startY - 20, endX, endY + 40);
                }
                else{
                    gc.strokeLine(startX, startY, startX, endY+15);
                    gc.strokeLine(startX, endY+15, endX, endY+15);
                    gc.strokeLine(endX, endY+15, endX, endY + 40);
                }
                gc.fillPolygon(new double[]{endX-10, endX, endX+10}, new double[]{endY+37, endY+57, endY+37}, 3);
                gc.strokePolygon(new double[]{endX-10, endX, endX+10}, new double[]{endY+37, endY+57, endY+37}, 3);
                break;

            case REALIZATION:
                // Vertical line from source to the same Y-coordinate as destination
                gc.setLineDashes(5);
                // Horizontal dashed line from startX to endX at the same Y-coordinate
                gc.setFill(Color.WHITE); // Set fill color to white
                gc.setStroke(Color.BLACK); // Set stroke color to black
                endY -= 13;
                if(startY<=endY) {
                    gc.strokeLine(startX, startY, startX, startY-20);
                    gc.strokeLine(startX, startY - 20, endX, startY - 20);
                    gc.strokeLine(endX, startY - 20, endX, endY + 40);
                }
                else{
                    gc.strokeLine(startX, startY, startX, endY+15);
                    gc.strokeLine(startX, endY+15, endX, endY+15);
                    gc.strokeLine(endX, endY+15, endX, endY + 40);
                }
                gc.fillPolygon(new double[]{endX-10, endX, endX+10}, new double[]{endY+37, endY+57, endY+37}, 3);
                gc.strokePolygon(new double[]{endX-10, endX, endX+10}, new double[]{endY+37, endY+57, endY+37}, 3);
                gc.setLineDashes(0);
                break;

            case COMPOSITION:
                // Vertical line from source to a little below destination
                endY -= 13;
                if(startY<=endY) {
                    gc.strokeLine(startX, startY, startX, startY-20);
                    gc.strokeLine(startX, startY - 20, endX, startY - 20);
                    gc.strokeLine(endX, startY - 20, endX, endY + 20);
                }
                else{
                    gc.strokeLine(startX, startY, startX, endY+15);
                    gc.strokeLine(startX, endY+15, endX, endY+15);
                }
                // Draw the diamond shape at the end of the line
                gc.fillPolygon(new double[]{endX - 10, endX, endX + 10, endX}, new double[]{endY + 37, endY+17, endY + 37, endY + 57}, 4);
                break;
        }
    }


    public void updateRelationType(UMLClass sourceClass, UMLClass destinationClass, UMLRelationships.RelationshipType newType) {
        UMLRelationships relationshipToUpdate = null;

        // Find the relationship in your collection
        for (UMLRelationships relationship : controller.drawnUMLRelationships) {
            if (relationship.getSource().equals(sourceClass) && relationship.getDest().equals(destinationClass)) {
                relationshipToUpdate = relationship;
                break;
            }
        }

        if (relationshipToUpdate != null) {
            // Remove the old representation of the relationship from the canvas
            clearRelationshipFromCanvas(relationshipToUpdate);

            // Update the relationship type
            relationshipToUpdate.setType(newType);

            // Redraw the updated relationship
            drawUMLRelationship(sourceClass, destinationClass, newType);
        } else {
            // Handle the case where the relationship is not found
            showAlert("Error", "Relationship not found.");
        }
    }


    public void updateCanvasRemoveClass(String className) {
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
                    autoAssignCoordinatesGrid(existingClass);
                    drawUMLClass(existingClass);
                }
            }

            // Remove the relationships involving the deleted class from the list
            controller.drawnUMLRelationships.removeAll(relationshipsToRemove);

            // Redraw the remaining relationships
            for (UMLRelationships relationship : controller.drawnUMLRelationships) {
                drawExistingRelationships();
            }
        }
    }

    void drawExistingRelationships() {
        GraphicsContext gc = centerContent.getGraphicsContext2D();
        gc.setLineWidth(1.0);

        for (UMLRelationships relationship : controller.drawnUMLRelationships) {
            drawUMLRelationship(relationship.getSource(), relationship.getDest(), relationship.getType());
        }
    }

    @FXML
    void clearRelationshipFromCanvas(UMLRelationships relationshipToRemove) {
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

        // Clear the specific area
        gc.clearRect(updatedClass.getX(), updatedClass.getY(), updatedClass.getWidth(), updatedClass.getHeight());

        // Redraw the class
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

    public void clearCanvas() {
        GraphicsContext gc = centerContent.getGraphicsContext2D();
        gc.clearRect(0, 0, centerContent.getWidth(), centerContent.getHeight());
    }

    public void refresh(){

        controller.drawnUMLClasses.clear();
        controller.drawnUMLRelationships.clear();


        controller.drawnUMLClasses.addAll(controller.classesMap.values());

        if(controller.drawnUMLClasses.isEmpty()){
            clearCanvas();
        }
        for (UMLClass umlClass : controller.drawnUMLClasses) {
           autoAssignCoordinatesGrid(umlClass);
            updateCanvas(controller.diagram, umlClass);

        }

        for (UMLRelationships relationship : controller.diagram.getRelationships()) {
            controller.drawnUMLRelationships.add(relationship);
            drawUMLRelationship(relationship.getSource(), relationship.getDest(), relationship.getType());
        }

        // Draw the existing relationships.
        drawExistingRelationships();
    }
}
