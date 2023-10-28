package javaroo.umldiagram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javaroo.umldiagram.controller.ClassTemplateController;
import javaroo.umldiagram.model.UMLClassModel;
import javaroo.umldiagram.view.ClassDiagram;

public class UMLController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="addClassButton"
    private Button addClassButton; // Value injected by FXMLLoader

    @FXML // fx:id="addRelButton"
    private Button addRelButton; // Value injected by FXMLLoader

    @FXML // fx:id="deleteClassButton"
    private Button deleteClassButton; // Value injected by FXMLLoader

    @FXML // fx:id="deleteRelButton"
    private Button deleteRelButton; // Value injected by FXMLLoader

    @FXML // fx:id="renameAttrButton"
    private Button renameAttrButton; // Value injected by FXMLLoader

    @FXML // fx:id="renameClassButton"
    private Button renameClassButton; // Value injected by FXMLLoader

    @FXML
    private HBox content;

    ClassTemplateController classTemplateController;


    private ClassTemplateController controller;


    @FXML
    void addClassGui(ActionEvent event) {
        try {

            FXMLLoader loader =  new FXMLLoader(getClass().getResource("templates/class-template.fxml"));
            Node node  = loader.load();
            AnchorPane pane = new AnchorPane();
            pane.setPrefHeight(30);
            pane.setPrefWidth(50);
            content.getChildren().add(node);
            content.getChildren().add(pane);
            ClassTemplateController controller = loader.getController();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void addRelationshipGui(ActionEvent event) {
        int numberOfChildren = content.getChildren().size();
        if (numberOfChildren > 1) {

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Input Dialog");
            dialog.setHeaderText("Enter two values");
            dialog.setContentText("Value 1:");

            // Show the input dialog and wait for the user's input
            Optional<String> result1 = dialog.showAndWait();

            // Check if the user provided the first value
            if (result1.isPresent()) {
                dialog.getEditor().clear();  // Clear the editor for the next input
                dialog.setContentText("Value 2:");

                // Show the input dialog again for the second value
                Optional<String> result2 = dialog.showAndWait();

                // Check if the user provided the second value
                if (result2.isPresent()) {

                    String value1 = result1.get();
                    String value2 = result2.get();

                    classTemplateController.addAttributes("relation", "toClass");
//                    classTemplateController.addAttributes(value1, value2);

                }
            }

        }
    }

    @FXML
    void deleteClassGui(ActionEvent event) throws IOException {
        int numberOfChildren = content.getChildren().size();
        System.out.println(numberOfChildren);
        if (numberOfChildren > 0) {
            content.getChildren().remove(numberOfChildren - 1);
            content.getChildren().remove(numberOfChildren - 2);
        }

    }

    @FXML
    void deleteRelationshipGui(ActionEvent event) {

    }

    @FXML
    void loadDiagramGui(ActionEvent event) {

    }

    @FXML
    void renameAttributeGui(ActionEvent event) {
        //        enabled by default

    }

    @FXML
    void renameClassGui(ActionEvent event) {
//        enabled by default


    }

    @FXML
    void saveDiagramGui(ActionEvent event) {















        List<Map<String, Object>> outputList = new ArrayList<>();

        ObservableList<Node> children = content.getChildren();
        for (Node node : children) {
            if (node instanceof AnchorPane) {
                AnchorPane anchorPane = (AnchorPane) node;
                String id = anchorPane.getId();

                if (id != null) {
                    List<String> attributesList = new ArrayList<>();
                    List<String> relationList = new ArrayList<>();
                    String className = null;

                    // Check for nested VBox within the AnchorPane
                    ObservableList<Node> anchorPaneChildren = anchorPane.getChildren();
                    for (Node anchorPaneChild : anchorPaneChildren) {
                        if (anchorPaneChild instanceof VBox) {
                            VBox vbox = (VBox) anchorPaneChild;

                            // Traverse children within the VBox
                            for (Node vboxChild : vbox.getChildren()) {
                                if (vboxChild instanceof AnchorPane) {
                                    AnchorPane nestedAnchorPane = (AnchorPane) vboxChild;
                                    String nestedId = nestedAnchorPane.getId();
                                    if (nestedId != null) {
                                        // Check if the nestedAnchorPane has the ID you're looking for
                                        if ("classNameContainer".equals(nestedId)) {
                                            // Access the TextField (assuming there's only one child)
                                            ObservableList<Node> nestedChildren = nestedAnchorPane.getChildren();
                                            if (!nestedChildren.isEmpty() && nestedChildren.get(0) instanceof TextField) {
                                                TextField textField = (TextField) nestedChildren.get(0);
                                                className = textField.getText();
                                            }
                                        }

                                        if ("attributesContainer".equals(nestedId)) {
                                            // Access the TextField content
                                            for (Node nestedChild : nestedAnchorPane.getChildren()) {
                                                if (nestedChild instanceof TextField) {
                                                    TextField textField = (TextField) nestedChild;
                                                    String textFieldContent = textField.getText();
                                                    attributesList.add(textFieldContent);
                                                }
                                            }
                                        }
                                        if ("relContainer".equals(nestedId)) {
                                            for (Node containerNode : nestedAnchorPane.getChildren()) {
                                                if (containerNode instanceof AnchorPane) {
                                                    AnchorPane anchorPane1 = (AnchorPane) containerNode;
                                                    // Now, you are inside the specific AnchorPane, so you can search for the TextField.
                                                    for (Node fieldNode : anchorPane1.getChildren()) {
                                                        if (fieldNode instanceof TextField) {
                                                            TextField textField = (TextField) fieldNode;
                                                            relationList.add(textField.getText());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Create a map for the data and add it to the output list
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("className", className);
                    dataMap.put("attributes", attributesList);
                    dataMap.put("relationships", relationList);
                    outputList.add(dataMap);
                }
            }
        }


        System.out.println(outputList);


        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Optional: pretty-print the JSON

        // Convert the outputList to a JSON string
        String json = gson.toJson(outputList);

        // Define the file path where you want to save the JSON file
        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filePath = desktopPath + File.separator + timestamp + ".json";

        // Write the JSON string to the file
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(json);
            System.out.println("JSON data saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }





















    }
































    @FXML
    public void cloaseDiagramDui(ActionEvent actionEvent) {
    }







    @FXML
    public // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        FXMLLoader loader  =  new FXMLLoader(getClass().getResource("templates/class-template.fxml"));
        classTemplateController = loader.getController();
        assert addClassButton != null : "fx:id=\"addClassButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert addRelButton != null : "fx:id=\"addRelButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert deleteClassButton != null : "fx:id=\"deleteClassButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert deleteRelButton != null : "fx:id=\"deleteRelButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert renameAttrButton != null : "fx:id=\"renameAttrButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert renameClassButton != null : "fx:id=\"renameClassButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";

    }

}




//                                        if ("attributesContainer".equals(nestedId)) {
//
//                                            System.out.println("some");
//                                            // Access the TextField (assuming there's only one child)
//                                            ObservableList<Node> nestedChildren = nestedAnchorPane.getChildren();
//                                            if (!nestedChildren.isEmpty() && nestedChildren.get(0) instanceof TextField) {
//                                                TextField textField = (TextField) nestedChildren.get(0);
//                                                String textFieldContent = textField.getText();
//                                                System.out.println("Content of TextField: " + textFieldContent);
//                                            }
//                                        }