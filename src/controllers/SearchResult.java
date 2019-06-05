package controllers;

import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class SearchResult {
    public Text name,attribute,rating;
    public Button compareP;
    SearchResult(Text name,Text attribute, Text rating, Button compareP){
        this.name=name;
        name.setText("");
        this.attribute=attribute;
        attribute.setText("");
        this.rating=rating;
        rating.setText("");
        this.compareP=compareP;
        compareP.setVisible(false);
        compareP.setManaged(false);
    }

    public void set(String n, String a, float r, float p){
        compareP.setManaged(true);
        compareP.setVisible(true);
        name.setText(n);
        attribute.setText(a);
        rating.setText("rating: "+Float.toString(r));
    }

    public void clear(){
        name.setText("");
        attribute.setText("");
        rating.setText("");
        compareP.setManaged(false);
        compareP.setVisible(false);
    }

}
