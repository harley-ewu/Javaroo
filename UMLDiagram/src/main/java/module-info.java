module javaroo.umldiagram {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.jline;

    opens javaroo.umldiagram to javafx.fxml;
    //exports javaroo.umldiagram;
    exports javaroo.umldiagram to javafx.graphics;
}