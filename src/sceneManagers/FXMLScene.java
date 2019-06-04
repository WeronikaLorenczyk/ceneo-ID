package sceneManagers;
import controllers.LoginController;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLScene extends SceneController implements Initializable {
    String fxml;
    public FXMLScene(SceneController prev, SceneHandler sh, String fxmlpath) {
        super(prev, sh);
        this.fxml=fxmlpath;
    }

    @Override
    public Scene getScene() {
        Parent root = null;
        URL c=null;
        try {
           c =getClass().getResource(fxml);
            System.out.println(c);
            root = FXMLLoader.load(c);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Scene(root, 800, 600);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}