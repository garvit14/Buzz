package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class UserListViewCell extends ListCell<People> {
    @FXML
    private ImageView profile;
    @FXML
    private Label name;
    @FXML
    private Label counter;
    @FXML
    private HBox body;
    @FXML
    private FXMLLoader loader;
    @FXML
    private Circle circle;

    @Override
    protected void updateItem(People user,boolean empty){
        super.updateItem(user, empty);
        if(empty || user == null) {  //If empty, skip it
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {   //set message body FXML
                loader = new FXMLLoader(getClass().getResource("../resources/fxml/userlistcell.fxml"));
                loader.setController(this); //and set this class as it's controller
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            name.setText(user.name);
            if(user.counter>0)
                counter.setText(Integer.toString(user.counter));
            else
                counter.setText("");
            Image image = new Image("file:src/resources/images/profilerec.jpg");
            circle.setFill(new ImagePattern(image));

            if(user.isActive)
                circle.setStroke(Color.LIGHTGREEN);
            else
                circle.setStroke(Color.LIGHTGRAY);

            setText(null);
            setGraphic(body);
        }

    }
}
