import javafx.scene.Scene;

public abstract class SceneController {

     String fxmlPath;
     SceneController prev;
     SceneHandler sh;

    SceneController(SceneController prev, SceneHandler sh){
        this.prev=prev;
        this.sh=sh;
    }

    abstract Scene getScene();
}
