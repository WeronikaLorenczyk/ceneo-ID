package sceneManagers;

import javafx.stage.Stage;

public class SceneHandler {
    Stage stage;
    public static SceneController curr;
    public static SceneHandler current;

    public SceneHandler(Stage stage){
        this.stage=stage;
        current=this;
    }
    public void changeScene(SceneController s){
        stage.setScene(s.getScene());
        curr=s;
        stage.show();
    }
}
