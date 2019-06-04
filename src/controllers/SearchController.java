package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import other.DatabaseHandler;
import sceneManagers.FXMLScene;
import sceneManagers.SceneHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class SearchController {
    @FXML
    ChoiceBox<String> Category;
    @FXML
    ChoiceBox<String> SortBy;
    @FXML
    ChoiceBox<String> Attribute, AttributeVal;
    @FXML
    Slider minimalRating;

    @FXML
    Text name1,name2,name3,name4,name5,name6,name7,name8;
    @FXML
    Text category1,category2,category3,category4,category5,category6,category7,category8;
    @FXML
    Text rating1,rating2,rating3,rating4,rating5,rating6,rating7,rating8;
    @FXML
    Button CP1, CP2, CP3, CP4,CP5, CP6, CP7,CP8;
    @FXML
    Button addProduct;

    DatabaseHandler db;

    SearchResult[]result=new SearchResult[8];

    public void initialize(){
        addProduct.setManaged(SceneHandler.data.ifShop);
        addProduct.setVisible(SceneHandler.data.ifShop);

        result[0]=new SearchResult(name1,category1,rating1,CP1);
        result[1]=new SearchResult(name2,category2,rating2,CP2);
        result[2]=new SearchResult(name3,category3,rating3,CP3);
        result[3]=new SearchResult(name4,category4,rating4,CP4);
        result[4]=new SearchResult(name5,category5,rating5,CP5);
        result[5]=new SearchResult(name6,category6,rating6,CP6);
        result[6]=new SearchResult(name7,category7,rating7,CP7);
        result[7]=new SearchResult(name8,category8,rating8,CP8);

        minimalRating.setMin(0);
        minimalRating.setMax(5.0);
        minimalRating.setValue(0);
        minimalRating.setShowTickLabels(true);
        minimalRating.setShowTickMarks(true);
        minimalRating.setMajorTickUnit(1.0);
     /*   minimalRating.setMinorTickCount();
        minimalRating.setBlockIncrement(10);*/

        AttributeVal.setVisible(false);
        AttributeVal.setManaged(false);

        db=new DatabaseHandler();

        try {
            Category.getItems().addAll(db.getCategories());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void setCat(){
        Attribute.getItems().clear();
        int x=db.getCatId(Category.getValue());
        Attribute.getItems().addAll(db.getAttributes(x));
    }

    @FXML
    void pc1(){
        priceComparison(0);
    }

    @FXML
    void pc2(){
        priceComparison(1);
    }

    @FXML
    void pc3(){
        priceComparison(2);
    }

    @FXML
    void pc4(){
        priceComparison(3);
    }

    @FXML
    void pc5(){
        priceComparison(4);
    }

    @FXML
    void pc6(){
        priceComparison(5);
    }

    @FXML
    void pc7(){
        priceComparison(6);
    }

    @FXML
    void pc8(){
        priceComparison(7);
    }

    void priceComparison(int id){
        SceneHandler.current.changeScene(new PriceCompScene(SceneHandler.curr, SceneHandler.current, "../fxmlFiles/PriceComp.fxml",results.get(id).id));
    }

    static String a[] = {"lowest Price", "highest Price", "highest Rating"};

    @FXML
    void prev(){
        if(pageNum==0){
            return;
        }
        pageNum--;
        fill();
    }

    @FXML
    void next(){
        System.out.println("A");
        if(pageNum*8+8>=results.size()){
            System.out.println("B");
            return;
        }
        pageNum++;
        fill();
    }

    String sort;


    @FXML
    void setSort(){
        sort=SortBy.getValue();
    }

    @FXML
    void fill(){
        /*search();*/
        int i=0;
        for(;i<min(results.size()-pageNum*8,8);i++){                  //terrible as far as computation speed goes, might change later
            result[i].set(results.get(i+pageNum*8).name,"",results.get(i+pageNum*8).rating,5);
        }
        for(;i<8;i++){
            result[i].clear();
        }
    }

    static class Res{
        public String name;
        public int id;
        public float rating;
        public Res(String name, int id, float rating){
            this.name=name;
            this.id=id;
            this.rating=rating;
        }
    }

    List<Res> results = new LinkedList<>();
    int pageNum;

    @FXML
    void setAtt(){
        AttributeVal.getItems().clear();
        AttributeVal.getItems().addAll(db.getAttVal(Attribute.getValue()));
        AttributeVal.setVisible(true);
        AttributeVal.setManaged(true);
    }

    @FXML
    void search(){
        try {
            ResultSet r =db.searchCategories(Category.getValue(),"product_id", (float) minimalRating.getValue(),AttributeVal.getValue(),Attribute.getValue());
            results.clear();
            pageNum=0;
            while(r.next()){
                System.out.println(r.getString(1) + " " + r.getFloat(3));
                results.add(new Res(r.getString(1),r.getInt(2),r.getFloat(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fill();
    }

    @FXML
    void addProduct(){
        SceneHandler.current.changeScene(new FXMLScene(SceneHandler.curr, SceneHandler.current, "../fxmlFiles/AddProduct.fxml"));
    }

    @FXML
    void signout(){
        SceneHandler.current.changeScene(new FXMLScene(SceneHandler.curr, SceneHandler.current, "../fxmlFiles/Login.fxml"));
    }
}
