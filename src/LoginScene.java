import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginScene extends SceneController implements Initializable {

    LoginScene(SceneController prev,SceneHandler sh){
       super(prev,sh);
       fxmlPath="/sample/sample.fxml";
    }

    @Override
    Scene getScene(){

        //mialam problemy z scenBuilderem wiec zostawilam tak jak bylo ale to ma sie ladowac z tych fxml
        // powinien byc ten kod zakomentowany zamiast calej reszty + obsluga przyciskow z tej scene



        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(fxmlPath));
            //System.out.println(root.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Scene(root, 800, 600);
/*
        Text text1 = new Text("Login");
        Text text2 = new Text("Password");

        TextField textFieldLogin = new TextField();
        PasswordField textFieldPassword = new PasswordField();

        Button buttonSubmit = new Button("Submit");


        GridPane gridPane = new GridPane();
        gridPane.setMinSize(800, 600);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        //Setting the Grid alignment
        gridPane.setAlignment(Pos.CENTER);

        //Arranging all the nodes in the grid
        gridPane.add(text1, 0, 0);
        gridPane.add(textFieldLogin, 1, 0);
        gridPane.add(text2, 0, 1);
        gridPane.add(textFieldPassword, 1, 1);
        gridPane.add(buttonSubmit, 0, 2);

        buttonSubmit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Submit");
                DatabaseHandler dbHandler=new DatabaseHandler();
                String login=textFieldLogin.getText();
                String password=textFieldPassword.getText();
                int userId=dbHandler.getUserId(login, password);
                System.out.println(userId+" "+login+" "+password);
                dbHandler.close();
                if(userId==-1){
                    Text textWrongLog = new Text("Wrong login or password");
                    gridPane.add(textWrongLog,1,2);
                    textFieldLogin.clear();
                    textFieldPassword.clear();
                }
                else{
                   // ceneo.customerId=userId;
                       // ceneo.changeScene(new SearchScene().getSearchScene());
                    sh.changeScene(new SearchScene(null,sh));
                }
            }
        });

        //Creating a scene object
        Scene scene = new Scene(gridPane);
        return scene;*/
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
