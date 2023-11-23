package javaroo.umldiagram;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Objects;

import static javaroo.cmd.UMLMenu.GView;

public class UMLDiagramGUI extends Application {
    public static boolean first = true;
    public static UMLDiagramGUI x;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UMLDiagramGUI.class.getResource("UMLCreator.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Image appIcon = new Image(Objects.requireNonNull(UMLDiagramGUI.class.getResourceAsStream("icon.png")));
        stage.getIcons().add(appIcon);
        stage.setTitle("UML Diagram Builder");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                GView = false;
            }
        });
    }
    private static boolean javaFxLaunched = false;
    public static void myLaunch(Class<? extends Application> applicationClass) {
        if(GView){
            System.out.println("GUI already open");
        }
        else {
            GView = true;
            if (!javaFxLaunched) { // First time
                Platform.setImplicitExit(false);
                new Thread(() -> Application.launch(applicationClass)).start();
                javaFxLaunched = true;
            } else { // Next times
                Platform.runLater(() -> {
                    try {
                        Application application = applicationClass.newInstance();
                        Stage primaryStage = new Stage();
                        application.start(primaryStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
