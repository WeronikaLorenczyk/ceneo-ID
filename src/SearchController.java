import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchController {
    @FXML
    ChoiceBox<String> Category;
    @FXML
    ChoiceBox<String> SortBy;
    @FXML
    ChoiceBox<String> Location;
    @FXML
    ChoiceBox<String> Attribute;
    @FXML
    Slider minimalRating;

    @FXML
    Text name1,name2,name3,name4,name5,name6,name7,name8;
    @FXML
    Text category1,category2,category3,category4,category5,category6,category7,category8;
    @FXML
    Text price1,price2,price3,price4,price5,price6,price7,price8;
    @FXML
    Text rating1,rating2,rating3,rating4,rating5,rating6,rating7,rating8;

    DatabaseHandler db;

    SearchResult []result=new SearchResult[8];

    public void initialize(){
        result[0]=new SearchResult(name1,category1,price1,rating1);
        result[1]=new SearchResult(name2,category2,price2,rating2);
        result[2]=new SearchResult(name3,category3,price3,rating3);
        result[3]=new SearchResult(name4,category4,price4,rating4);
        result[4]=new SearchResult(name5,category5,price5,rating5);
        result[5]=new SearchResult(name6,category6,price6,rating6);
        result[6]=new SearchResult(name7,category7,price7,rating7);
        result[7]=new SearchResult(name8,category8,price8,rating8);

        db=new DatabaseHandler();

        try {
            Category.getItems().addAll(db.getCategories());
            Location.getItems().addAll(db.getLocations());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    static String a[] = {"lowest Price", "highest Price", "highest Rating"};


    String cat;
    String sort;
    String location;
    String attribute;

    @FXML
    void SetCategory(){
        sort=SortBy.getValue();
    }

    @FXML
    void search(){
        try {
            ResultSet r =db.search(1,"Krakow","product_id", "1",0.0f); //only some of the cryteria work
            while(r.next()){
                System.out.println(r.getString(1) + " " + r.getFloat(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
