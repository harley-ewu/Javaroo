module javaroo.umldiagram {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires javafx.base;

    opens javaroo.cmd to javafx.fxml, javafx.graphics;
    opens javaroo.umldiagram.controller to javafx.fxml, javafx.graphics;

    exports javaroo.cmd;
    exports javaroo.umldiagram.controller;
    exports javaroo.umldiagram;
    opens javaroo.umldiagram to javafx.fxml, javafx.graphics;
}




