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
        Database database= Database.getInstance();
        database.open();
        HashMap<String,Integer> hashMap = new HashMap<>();
        hashMap.put("asd",12);
        hashMap.put("asj",12);
        hashMap.put("asd",12);
        Cv cv= new Cv(1,"a",18,1999,"b","c","d","e","f","g",new HashMap<>(),hashMap,new HashMap<>(),new ArrayList<>());
        database.insert(cv);
        database.close();
        primaryStage.show();
    }

    public static void main(final String[] args) {
        launch();
    }
}
