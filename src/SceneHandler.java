import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneHandler {
    Stage stage;
    static SceneController curr;
    static SceneHandler current;

    SceneHandler(Stage stage){
        this.stage=stage;
        current=this;
    }
    void changeScene(SceneController s){
        stage.setScene(s.getScene());
        curr=s;
        stage.show();
    }
}
