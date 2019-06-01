import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class PrceCompController {

    @FXML
    Text loc1,loc2,loc3,loc4,name1,name2,name3,name4,disc1,disc2,disc3,disc4,price1,price2,price3,price4;

    PriceCompResult []display = new PriceCompResult[4];
    List<Ret> rets = new LinkedList<>();

    int curr;

    DatabaseHandler databaseHandler;

    public void initialize(){
        display[0]=new PriceCompResult(name1,loc1,disc1,price1);
        display[1]=new PriceCompResult(name2,loc2,disc2,price2);
        display[2]=new PriceCompResult(name3,loc3,disc3,price3);
        display[3]=new PriceCompResult(name4,loc4,disc4,price4);
        for(int i=0;i<4;i++){
            display[i].clear();
        }
        databaseHandler = new DatabaseHandler();
        try {
            ResultSet rs=databaseHandler.priceComp(PriceCompScene.id);
            while (rs.next()){
                rets.add(new Ret(rs.getString(1),rs.getString(2),0.0f,rs.getFloat(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fill();
    }

    void fill(){
        int i=0;
        for(;i<min(rets.size()-curr*4,4);i++){
            display[i].set(rets.get(i+curr*4));
        }
        for(;i<4;i++){
            display[i].clear();
        }
    }

    static class Ret{
        public String name;
        public String loc;
        public float disc;
        public float price;

        Ret(String name, String loc, float disc, float price){
            this.name=name;
            this.loc=loc;
            this.disc=disc;
            this.price=price;
        }
    }

    @FXML
    void back(){
        SceneHandler.current.changeScene(SceneHandler.curr.prev);
    }
}
