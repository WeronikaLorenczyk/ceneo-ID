package sceneManagers;

import javafx.stage.Stage;

public class SceneHandler {
    Stage stage;
    public static SceneController curr;
    public static SceneHandler current;
    public static Data data=null;

    public SceneHandler(Stage stage){
        this.stage=stage;
        current=this;
        if(data==null)
            data=new Data();
    }
    public void changeScene(SceneController s){
        stage.setScene(s.getScene());
        curr=s;
        stage.show();
    }


    public class Data{
        public int userId=-1;
        public boolean ifShop=true;
    }

}

