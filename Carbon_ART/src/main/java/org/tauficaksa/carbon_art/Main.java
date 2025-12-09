package org.tauficaksa.carbon_art;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) {
        BrowserUI ui = new BrowserUI();
        ui.start(stage);
    }
}