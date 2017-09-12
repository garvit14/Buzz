package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class MainController {

    @FXML
    private ListView<People> peopleListView;
    @FXML
    private ListView<Message> messageListView;
    @FXML
    private TextField sendInput;
    @FXML
    private Button sendButton;
    @FXML
    private AnchorPane profilePane;
    @FXML
    private AnchorPane messagePane;
    @FXML
    private SplitPane splitPane;

    public ObservableList<People> peopleList;
    public ObservableList<Message> messageList;

    public People currentlyOpenUser;
    public LocalDB db;
    private Main main;

    @FXML
    public void initialize() {
        System.out.println("Initialize");
        currentlyOpenUser=null;
        db=new LocalDB(this);
        peopleList= FXCollections.observableArrayList();
        try {
            db.setUsers();
        } catch (SQLException e) {
            System.out.println("SQL error in setting users");
            e.printStackTrace();
        }
        peopleListView.setItems(peopleList);
        peopleListView.setCellFactory(peopleListView -> new UserListViewCell());

        peopleListView.setOnMouseClicked(mouseEvent -> {
            System.out.println("Button Clicked");
            try {
                currentlyOpenUser=peopleListView.getSelectionModel().getSelectedItem();
                System.out.println(peopleListView.getSelectionModel().getSelectedItem().userName);
                db.updateAllMessages(peopleListView.getSelectionModel().getSelectedItem(),messageList);
            } catch (SQLException e) {
                System.out.println("Local Database Error");
                e.printStackTrace();
            }
        });

        messageList=FXCollections.observableArrayList();
        messageListView.setItems(messageList);
        messageListView.setCellFactory(messageListView -> new MessageListViewCell());

        //removing the message pane and adding the profile pane
        System.out.println("At the removing area "+profilePane);
        splitPane.getItems().remove(1);
        splitPane.getItems().add(1,profilePane);
    }

    @FXML
    public void send(ActionEvent event){
        if(sendInput.getText().equals(""))
            return;
        try {
            Message message = new Message(sendInput.getText(),Main.user.userName,currentlyOpenUser.userName,new Date());
            db.sendMessage(message);
            sendInput.setText("");
            messageList.clear();
            db.updateAllMessages(currentlyOpenUser,messageList);
        } catch (SQLException e) {
            System.out.println("ResultSet error in send");
            e.printStackTrace();
        }
    }

    @FXML
    public void pressedEnter(KeyEvent keyEvent){
        if(keyEvent.getCode()== KeyCode.ENTER)
            send(null);
    }

    public void receiveMessage(Message message){
        db.storeMessage(message);
        if(!message.sender.equals(currentlyOpenUser.userName)){
            for(People p:peopleList){
                if(p.userName.equals(message.sender)){
                    p.counter++;
                    break;
                }
            }
        }
        if(currentlyOpenUser!=null && message.sender.equals(currentlyOpenUser.userName))
            try {
                db.updateAllMessages(currentlyOpenUser,messageList);
            } catch (SQLException e) {
                System.out.println("SQL error while updating message list");
                e.printStackTrace();
            }
    }

    public void updateStatus(List<People> list){
        peopleList.clear();
        peopleList.addAll(list);
    }

    public void setMain(Main main){
        this.main=main;
        db.setMain(main);
        //Has to do it in here because main is null before this
        OnlineStatusThread onlineStatusThread = new OnlineStatusThread(this,main);
        Thread t = new Thread(onlineStatusThread);
        t.start();
    }

}
