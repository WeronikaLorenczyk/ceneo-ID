package sceneManagers;

import javafx.scene.Scene;

public abstract class SceneController {

    public String fxmlPath;
    public SceneController prev;
    public SceneHandler sh;

    public SceneController(SceneController prev, SceneHandler sh){
        this.prev=prev;
        this.sh=sh;
    }

    public abstract Scene getScene();
}
