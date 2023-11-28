/* module javaroo.umldiagram {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.jline;
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
    requires org.fusesource.jansi;
    requires org.slf4j;
    requires org.jline;

    opens javaroo.umldiagram to javafx.fxml;
    //exports and opens directives for javaroo.cmd
    exports javaroo.cmd to junit, javafx.graphics;
    opens javaroo.cmd to junit;
    exports javaroo.umldiagram to javafx.graphics;

    // Other exports if necessary
}
