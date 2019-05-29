import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLScene extends SceneController implements Initializable {
    String fxml;
    FXMLScene(SceneController prev, SceneHandler sh, String fxmlpath) {
        super(prev, sh);
        this.fxml=fxmlpath;
    }

    @Override
    Scene getScene() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(fxml));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Scene(root, 800, 600);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
