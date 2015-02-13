package address_book;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
    Scene scene;
    @Override
    public void start(Stage primary_stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("address_book_ui.fxml"));
        primary_stage.setTitle("Address Book");
        scene = new Scene(root, 800, 450);
        primary_stage.setScene(scene);

        String css = Main.class.getResource("styles/main.css").toExternalForm();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);
        primary_stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
