package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import other.DatabaseHandler;
import sceneManagers.FXMLScene;
import sceneManagers.SceneHandler;


public class LoginController {
    @FXML
    Button logIn;
    @FXML
    TextField loginF;
    @FXML
    PasswordField passwordF;
    @FXML
    Button signUp;
    @FXML
    Text wrong;

    @FXML
    Button logIn1;
    @FXML
    TextField loginF1;
    @FXML
    PasswordField passwordF1;
    @FXML
    Button signUp1;
    @FXML
    Text wrong1;

    public void initialize(){
        wrong.setManaged(false);
        wrong.setVisible(false);
        wrong1.setManaged(false);
        wrong1.setVisible(false);
    }

    @FXML
    void loginCheck(){
        System.out.println("Submit");
        DatabaseHandler dbHandler=new DatabaseHandler();
        String login=loginF.getText();
        String password=passwordF.getText();
        int userId= 0;
        try {
            userId = dbHandler.getUserId(login, password);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println(userId+" "+login+" "+password);
        dbHandler.close();
        if(userId==-1){
            wrong.setManaged(true);
            wrong.setVisible(true);
            loginF.clear();
            passwordF.clear();
        }
        else{
            System.out.println("logged in!");
            System.out.println(userId);
            SceneHandler.data.userId=userId;
            SceneHandler.data.ifShop=false;
            SceneHandler.current.changeScene(new FXMLScene(SceneHandler.curr, SceneHandler.current, "../fxmlFiles/SearchScene.fxml"));
        }
    }

    @FXML
    void signUp(){
        SceneHandler.current.changeScene(new FXMLScene(SceneHandler.curr, SceneHandler.current, "../fxmlFiles/SignUp.fxml"));
    }


    @FXML
    void loginCheckShop(){
        System.out.println("Submit");
        DatabaseHandler dbHandler=new DatabaseHandler();
        String login=loginF1.getText();
        String password=passwordF1.getText();
        int shopId= 0;
        try {
            shopId = dbHandler.getShopId(login,password);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println(shopId+" "+login+" "+password);
        dbHandler.close();
        if(shopId==-1){
            wrong1.setManaged(true);
            wrong1.setVisible(true);
            loginF1.clear();
            passwordF1.clear();
        }
        else{
            System.out.println("logged in!");
            SceneHandler.data.userId=shopId;
            SceneHandler.data.ifShop=true;
            SceneHandler.current.changeScene(new FXMLScene(SceneHandler.curr, SceneHandler.current, "../fxmlFiles/SearchScene.fxml"));
        }
    }

    @FXML
    void signUpShop(){
        SceneHandler.current.changeScene(new FXMLScene(SceneHandler.curr, SceneHandler.current, "../fxmlFiles/SingUpShop.fxml"));
    }
}
