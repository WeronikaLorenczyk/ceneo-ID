package controllers;

import controllers.PrceCompController;
import javafx.scene.text.Text;

public class PriceCompResult {
    Text name,location,discount,price;
    PriceCompResult(Text name, Text location, Text discount, Text price){
        this.name=name;
        this.discount=discount;
        this.location=location;
        this.price=price;
    }

    void set(PrceCompController.Ret x){
        name.setText(x.name);
        //discount.setText(Float.toString(x.disc));
        location.setText(x.loc);
        price.setText(Float.toString(x.price)+"zł");
    }

    void clear(){
        name.setText("");
        discount.setText("");
        location.setText("");
        price.setText("");
    }
}
