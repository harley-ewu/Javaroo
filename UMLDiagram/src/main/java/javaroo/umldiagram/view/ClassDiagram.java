package javaroo.umldiagram.view;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javaroo.umldiagram.controller.ClassTemplateController;

public class ClassDiagram extends AnchorPane {

    private ClassTemplateController classTemplateController;

    public ClassDiagram(int id) {

        setPrefHeight(356.0);
        setPrefWidth(353.0);
        setStyle("-fx-border-color: #0d1117; -fx-background-radius: 10; -fx-border-radius: 10;");
        // Create VBox
        VBox classVbox = new VBox();
        classVbox.setLayoutX(1.0);
        classVbox.setPrefHeight(334.0);
        classVbox.setPrefWidth(305.0);

        // Create AnchorPane for classNameContainer
        AnchorPane classNameContainer = new AnchorPane();
        classNameContainer.setPrefHeight(58.0);
        classNameContainer.setPrefWidth(332.0);

        // Create TextField for classNameTextField
        TextField classNameTextField = new TextField();
        classNameTextField.setPrefHeight(50.0);
        classNameTextField.setPrefWidth(348.0);
//        classNameTextField.setPromptText("Class Name");
        classNameTextField.setText("Class Name");
        classNameTextField.setStyle("-fx-font-family: Poppins; -fx-font-size: 20; -fx-font-weight: BOLD; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: transparent; -fx-background-color: transparent; -fx-alignment: center;");
        classNameContainer.getChildren().add(classNameTextField);

        // Create AnchorPane for attributesContainer
        AnchorPane attributesContainer = new AnchorPane();
        attributesContainer.setPrefHeight(290.0);
        attributesContainer.setPrefWidth(362.0);

        // Create Line for separatorLineTwo
        Line separatorLineTwo = new Line();
        separatorLineTwo.setEndX(194.0);
        separatorLineTwo.setEndY(6.0);
        separatorLineTwo.setLayoutX(166.0);
        separatorLineTwo.setLayoutY(-6.0);
        separatorLineTwo.setStartX(-165.0);
        separatorLineTwo.setStartY(2.0);

        // Create Button for addAttributesBtn
        Button addAttributesBtn = new Button("+");

        addAttributesBtn.setLayoutX(311.0);
        addAttributesBtn.setLayoutY(239.0);
        addAttributesBtn.setMnemonicParsing(false);
        addAttributesBtn.setStyle("-fx-background-color: transparent;");
        // Create Label for attributesLabel
        Label attributesLabel = new Label("Attributes");
        attributesLabel.setLayoutX(148.0);
        attributesLabel.setLayoutY(6.0);
        // Add children to attributesContainer
        attributesContainer.getChildren().addAll(separatorLineTwo, addAttributesBtn, attributesLabel);

        // Add children to classVbox
        classVbox.getChildren().addAll(classNameContainer, attributesContainer);

        // Add classVbox to this ClassDiagram
        getChildren().add(classVbox);
    }
}
