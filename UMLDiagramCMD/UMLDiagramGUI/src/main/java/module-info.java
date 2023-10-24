module javaroo.gui {
    requires javafx.controls;
    requires javafx.fxml;


    opens javaroo.gui to javafx.fxml;
    exports javaroo.gui;
}