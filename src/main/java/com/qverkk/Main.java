package com.qverkk;

import com.qverkk.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

/**
 * @author Qverkk
 * @project DocumentConverter
 * @date 8/4/2019
 **/

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {

        URL resource = getClass().getClassLoader().getResource("ui/MainUi.fxml");
        if (resource == null) {
            System.out.println("Resource was null");
            return;
        }

        FXMLLoader loader = new FXMLLoader(resource);
        loader.setController(new MainController());
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();
    }
}
