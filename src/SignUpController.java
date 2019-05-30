import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

public class SignUpController {
    @FXML
    TextField login;
    @FXML
    PasswordField password, repeatPassword;
    @FXML
    ChoiceBox<String> locationn;
    @FXML
    CheckBox age;
    @FXML
    Button confirm;
    @FXML
    Button back;

    @FXML
    void sign() throws InvalidKeySpecException, NoSuchAlgorithmException, SQLException {
        if(password.getText().compareTo(repeatPassword.getText())!=0){
            didntPass();
            return;
        }
        String passwordT=password.getText();
        String hashedPassword=null;
        try {
            hashedPassword=Hasher.generateStorngPasswordHash(passwordT);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        System.out.println(hashedPassword);
        //System.out.println(Hasher.validatePassword(passwordT,hashedPassword));
        //System.out.println(Hasher.validatePassword("b",hashedPassword));
        DatabaseHandler databaseHandler = new DatabaseHandler();
        System.out.println(databaseHandler.addUser(login.getText(),hashedPassword,"Krakow"));
        back();
    }

    @FXML
    void back(){
        SceneHandler.current.changeScene(SceneHandler.curr.prev);
    }

    private void didntPass(){
        System.out.println("something is wrong!");
    }
}
