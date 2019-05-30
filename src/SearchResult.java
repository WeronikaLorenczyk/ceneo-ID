import javafx.scene.text.Text;

public class SearchResult {
    public Text name,attribute,rating,price;
    SearchResult(Text name,Text attribute, Text rating, Text price){
        this.name=name;
        name.setText("");
        this.attribute=attribute;
        attribute.setText("");
        this.rating=rating;
        rating.setText("");
        this.price=price;
        price.setText("");
    }

    public void set(String n, String a, float r, float p){
        name.setText(n);
        attribute.setText(a);
        rating.setText(Float.toString(r));
        price.setText(Float.toString(p));
    }

}
