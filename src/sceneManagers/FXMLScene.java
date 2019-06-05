package sceneManagers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLScene extends SceneController implements Initializable {
    String fxml;
    Scene ret;
    public FXMLScene(SceneController prev, SceneHandler sh, String fxmlpath) {
        super(prev, sh);
        this.fxml=fxmlpath;
    }

    @Override
    public Scene getScene() {
        if(ret==null) {
            Parent root = null;
            URL c = null;
            try {
                c = getClass().getResource(fxml);
                System.out.println(c);
                root = FXMLLoader.load(c);
                root.getStylesheets().add("fxmlFiles/buttons.css");

            } catch (IOException e) {
                e.printStackTrace();
            }
            ret= new Scene(root, 800, 600);
            System.out.println("ladowane css");
           // String css = this.getClass().getResource("../other/buttons.css").toExternalForm();
            //ret.getStylesheets().add(css);
          // ret.getStylesheets().add("fxmlFiles/buttons.css");
        }
        return ret;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
