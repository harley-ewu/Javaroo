module javaroo.umldiagram {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;



    opens javaroo.umldiagram to javafx.fxml;
    //exports javaroo.umldiagram;
    exports javaroo.umldiagram to javafx.graphics;
    exports javaroo.umldiagram.model; // Export the package
    exports javaroo.umldiagram.controller to javafx.graphics;
    opens javaroo.umldiagram.controller to javafx.fxml;
}


