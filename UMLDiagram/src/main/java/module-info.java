/* module javaroo.umldiagram {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires junit;


    opens javaroo.umldiagram to javafx.fxml;
    //exports javaroo.umldiagram;
    exports javaroo.umldiagram to javafx.graphics;
}


 */


module javaroo.umldiagram {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires junit;

    opens javaroo.umldiagram to javafx.fxml;
    // Added exports and opens directives for javaroo.cmd
    exports javaroo.cmd to junit, javafx.graphics;
    opens javaroo.cmd to junit;

    // Other exports if necessary
}
