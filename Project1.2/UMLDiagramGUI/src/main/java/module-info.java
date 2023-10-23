module com.example.umldiagramgui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.umldiagramgui to javafx.fxml;
    exports com.example.umldiagramgui;
}