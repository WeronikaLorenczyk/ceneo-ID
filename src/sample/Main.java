package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;

public class Main extends Application {

    private final String url =" jdbc:postgresql://localhost/postgres";
    private final String user= "postgres";
    private final String password="postgres";

    Connection connection;

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*Class.forName("org.postgresql.Driver");
        try{
            connection= DriverManager.getConnection(url,user,password);
            System.out.println("WORKS!");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }*/
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Ceneło");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
