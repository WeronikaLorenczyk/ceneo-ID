import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Ceneo extends Application {

    Scene scene;
    Stage stage;
    int customerId;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage=primaryStage;
        primaryStage.setTitle("Ceneo");
        scene=new loginScene().getLoginScene(this);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    void changeScene(Scene s){
        scene=s;
        stage.setScene(s);
        stage.show();
    }

}