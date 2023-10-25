package javaroo.umldiagram;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
    void addClassGui(ActionEvent event) {

    }

    @FXML
    void addRelationshipGui(ActionEvent event) {

    }

    @FXML
    void deleteClassGui(ActionEvent event) {

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

    }

    @FXML
    void saveDiagramGui(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert addClassButton != null : "fx:id=\"addClassButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert addRelButton != null : "fx:id=\"addRelButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert deleteClassButton != null : "fx:id=\"deleteClassButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert deleteRelButton != null : "fx:id=\"deleteRelButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert renameAttrButton != null : "fx:id=\"renameAttrButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";
        assert renameClassButton != null : "fx:id=\"renameClassButton\" was not injected: check your FXML file 'UMLCreator.fxml'.";

    }
 
}