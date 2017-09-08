package application;

import com.sun.org.apache.regexp.internal.RE;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {

    public static People user;  //Current User
    private Connection conn;
    public Socket socket;
    public ObjectInputStream objectInputStream;
    public ObjectOutputStream objectOutputStream;
    public boolean isConnected;             //Is the user connected to server
    private MainController mainController;  //Holds reference to mainController instance

    @Override
    public void start(Stage primaryStage) throws Exception{
        isConnected=false;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../resources/fxml/main.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        mainController=fxmlLoader.<MainController>getController();
        mainController.setMain(this);   //Set Main class reference in mainController class
        primaryStage.setTitle("Buzz");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        if(connection("localhost")) {
            isConnected=true;
            ReceivingThread receivingThread = new ReceivingThread(socket,null,mainController.db);
            Thread t = new Thread(receivingThread);
            t.start();
            System.out.println("Connection to sever established");
        }
//        Parent root = FXMLLoader.load(getClass().getResource("../resources/fxml/login.fxml"));
//        primaryStage.setTitle("Buzz");
//        primaryStage.setResizable(false);
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();

    }

    public static void main(String[] args) {
        user=new People("Sumit","sumit","");
        //user=new People("Garvit","garvit","");
        //user=new People("Anubahv","anubhav","");
        launch(args);
    }

    public boolean connection(String ip) throws IOException {
        socket=new Socket(ip,7777);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        Packet packet = new Packet();
        packet.operation="login";
        packet.string1=user.userName;
        SendingThread sendingThread = new SendingThread(objectOutputStream,packet);
        Thread t = new Thread(sendingThread);
        t.start();
        return true;
    }

}