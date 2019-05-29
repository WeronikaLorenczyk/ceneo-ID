import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class LoginController {
    @FXML
    Button logIn;
    @FXML
    TextField loginF;
    @FXML
    PasswordField passwordF;

   //Nwm jak rozwiazać stąd zmienić scenę

    @FXML
    void loginCheck(){
        System.out.println("Submit");
        DatabaseHandler dbHandler=new DatabaseHandler();
        String login=loginF.getText();
        String password=passwordF.getText();
        int userId=dbHandler.getUserId(login, password);
        System.out.println(userId+" "+login+" "+password);
        dbHandler.close();
        if(userId==-1){
            Text textWrongLog = new Text("Wrong login or password");
            System.out.println("Wrong login or password");
            loginF.clear();
            passwordF.clear();
        }
        else{
            System.out.println("logged in!");
            SceneHandler.current.changeScene(new FXMLScene(SceneHandler.curr,SceneHandler.current,"SearchScene.fxml"));
            // ceneo.customerId=userId;
            // ceneo.changeScene(new SearchScene().getSearchScene());
        }
    }
}
