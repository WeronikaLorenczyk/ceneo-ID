import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;

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


    static String a[] = {"lowest Price", "highest Price", "highest Rating"};


    String cat;
    String sort;
    String location;
    String attribute;

    @FXML
    void SetCategory(){
        cat=SortBy.getValue();
    }


}
