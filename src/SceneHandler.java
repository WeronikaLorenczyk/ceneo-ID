import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneHandler {
    Stage stage;

    SceneHandler(Stage stage){
        this.stage=stage;
    }
    void changeScene(SceneController s){
        stage.setScene(s.getScene());
        stage.show();
    }
}
