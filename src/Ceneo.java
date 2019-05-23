import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Ceneo extends Application {



    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ceneo");
        SceneHandler sh=new SceneHandler(primaryStage);
       sh.changeScene(new LoginScene(null,sh));
    }



}