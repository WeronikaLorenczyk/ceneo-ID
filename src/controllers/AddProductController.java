package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import other.DatabaseHandler;
import sceneManagers.SceneHandler;

import java.sql.SQLException;

public class AddProductController {

    @FXML
    ChoiceBox<String> category;
    @FXML
    TextField name,description;
    @FXML
    Button back,add;
    @FXML
    Text wrong,added;

    DatabaseHandler db;
    public void initialize(){
        db=new DatabaseHandler();

        try {
            category.getItems().addAll(db.getCategories());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        wrong.setManaged(false);
        wrong.setVisible(false);
        added.setManaged(false);
        added.setVisible(false);
    }

    @FXML
    void add() throws SQLException {

        wrong.setManaged(false);
        wrong.setVisible(false);
        added.setManaged(false);
        added.setVisible(false);

        boolean done=db.addProduct(name.getText(),description.getText(),category.getValue());
        if(done)
            pass();
        else{
            didntPass();
        }
    }
    void didntPass(){
        wrong.setManaged(true);
        wrong.setVisible(true);
    }

    void pass(){
        added.setManaged(true);
        added.setVisible(true);
    }


    @FXML
    void back(){
        SceneHandler.current.changeScene(SceneHandler.curr.prev);
    }
}
