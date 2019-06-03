import javafx.application.Application;
import javafx.stage.Stage;
import sceneManagers.FXMLScene;
import sceneManagers.SceneHandler;


public class Ceneo extends Application {



    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ceneo");
        SceneHandler sh=new SceneHandler(primaryStage);
       //sh.changeScene(new LoginScene(null,sh));
        SceneHandler.current.changeScene(new FXMLScene(SceneHandler.curr, SceneHandler.current, "../fxmlFiles/Login.fxml"));
    }



}