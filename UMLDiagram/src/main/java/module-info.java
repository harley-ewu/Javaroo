module javaroo.umldiagram.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires javafx.base;

    opens javaroo.cmd to javafx.fxml, javafx.graphics;
    opens javaroo.umldiagram.controller to javafx.fxml, javafx.graphics;

    exports javaroo.cmd;
    exports javaroo.umldiagram.gui;
    exports javaroo.umldiagram.controller;
    opens javaroo.umldiagram.gui to javafx.fxml, javafx.graphics;
}




