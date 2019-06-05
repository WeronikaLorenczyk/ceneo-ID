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
    ChoiceBox<String> category,attribute;
    @FXML
    TextField name,description,attValue;
    @FXML
    Button back,add,addAttribute;
    @FXML
    Text wrong,added,wrong1,added1;

    int productId=-1,categoryId;

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

        wrong1.setManaged(false);
        wrong1.setVisible(false);
        added1.setManaged(false);
        added1.setVisible(false);

    }

    @FXML
    void add() throws SQLException {

        wrong.setManaged(false);
        wrong.setVisible(false);
        added.setManaged(false);
        added.setVisible(false);

        int done=db.addProduct(name.getText(),description.getText(),category.getValue());
        productId=done;
        categoryId=db.getCatId(category.getValue());
        if(done != -1)
            pass();
        else{
            didntPass();
        }
    }
    void didntPass(){
        wrong.setManaged(true);
        wrong.setVisible(true);
        productId=-1;
        attribute.getItems().remove(0,attribute.getItems().size());
    }

    void pass(){
        added.setManaged(true);
        added.setVisible(true);
        attribute.getItems().addAll(db.getAttributes(categoryId));

    }

    @FXML
    void addAtt(){
        wrong1.setManaged(false);
        wrong1.setVisible(false);
        added1.setManaged(false);
        added1.setVisible(false);

        boolean done=db.addAttribute(attribute.getValue(),attValue.getText(),productId);
        if(done){
            added1.setManaged(true);
            added1.setVisible(true);
        }
        else{
            wrong1.setManaged(true);
            wrong1.setVisible(true);
        }
    }


    @FXML
    void back(){
        SceneHandler.current.changeScene(SceneHandler.curr.prev);
    }
}
