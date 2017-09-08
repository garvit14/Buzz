package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.sql.SQLException;

public class MainController {

    @FXML
    private ListView<People> peopleListView;
    @FXML
    private ListView<Message> messageListView;
    @FXML
    private TextField sendInput;
    @FXML
    private Button sendButton;

    public ObservableList<People> peopleList;
    public ObservableList<Message> messageList;

    public People currentlyOpenUser;
    public LocalDB db;
    private Main main;

    @FXML
    public void initialize() {
        System.out.println("Initialize");
        db=new LocalDB(this);
        peopleList= FXCollections.observableArrayList();
        try {
            db.setUsers();
        } catch (SQLException e) {
            System.out.println("SQL error in setting users");
            e.printStackTrace();
        }
        peopleListView.setOnMouseClicked(mouseEvent -> {
            System.out.println("Button Clicked");
            try {
                currentlyOpenUser=peopleListView.getSelectionModel().getSelectedItem();
                db.updateAllMessages(peopleListView.getSelectionModel().getSelectedItem());
            } catch (SQLException e) {
                System.out.println("Local Database Error");
                e.printStackTrace();
            }
            //temp.setText(list.getSelectionModel().getSelectedItem().name);
        });
        peopleListView.setItems(peopleList);
        messageList=FXCollections.observableArrayList();
        messageListView.setItems(messageList);
        messageListView.setCellFactory(messageListView -> new MessageListViewCell());
    }

    @FXML
    public void send(ActionEvent event){
        if(sendInput.getText().equals(""))
            return;
        try {
            db.sendMessage(currentlyOpenUser,sendInput.getText());
        } catch (SQLException e) {
            System.out.println("ResultSet error in send");
            e.printStackTrace();
        }
    }

    public void setMain(Main main){
        this.main=main;
        db.setMain(main);
    }

}
