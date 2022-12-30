package edu.ieu.tr.cvm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.xml.crypto.Data;

public class App extends Application {
    @Override
    public void start(final Stage primaryStage) throws IOException, SQLException, ClassNotFoundException {
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/app-view.fxml")), 640, 480));
        primaryStage.setTitle("cvm");
        primaryStage.show();
    }

    public static void main(final String[] args) {
        launch();
    }
}
