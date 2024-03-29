package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import other.DatabaseHandler;
import other.Hasher;
import sceneManagers.SceneHandler;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

public class SignUpController {
    @FXML
    TextField login, locationn;
    @FXML
    PasswordField password, repeatPassword;
    @FXML
    CheckBox age;
    @FXML
    Button confirm;
    @FXML
    Button back;
    @FXML
    Text wrong;

    public void initialize(){
        wrong.setManaged(false);
        wrong.setVisible(false);
    }


    @FXML
    void sign() throws InvalidKeySpecException, NoSuchAlgorithmException, SQLException {
        if(password.getText().compareTo(repeatPassword.getText())!=0){
            didntPass();
            return;
        }
        String passwordT=password.getText();
        String hashedPassword=null;
        try {
            hashedPassword= Hasher.generateStoringPasswordHash(passwordT);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        System.out.println(hashedPassword);
        DatabaseHandler databaseHandler = new DatabaseHandler();
        boolean done=databaseHandler.addUser(login.getText(),hashedPassword,locationn.getText(),age.isSelected());
        if(done)
            back();
        else{
            didntPass();
        }
    }

    @FXML
    void back(){
        SceneHandler.current.changeScene(SceneHandler.curr.prev);
    }

    private void didntPass(){
        System.out.println("something is wrong!");
        wrong.setManaged(true);
        wrong.setVisible(true);
    }
}
