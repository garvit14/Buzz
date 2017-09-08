package application;

import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

//this class also conatins server database methods

public class ReceivingThread implements Runnable {
    private Socket clientSocket;
    private Connection conn;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private LocalDB db;
    public ReceivingThread(Socket clientSocket, Connection conn,LocalDB db){
        this.clientSocket=clientSocket;
        this.conn=conn;
        this.db=db;
        try {
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ReceivingThread(Socket clientSocket, Connection conn,ObjectOutputStream objectOutputStream){
        this.clientSocket=clientSocket;
        this.conn=conn;
        this.db=null;
        this.objectOutputStream=objectOutputStream;
        try {
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void run() {
        try {

            while (true){   //reads any input from the client till apocalypse
                Packet p = (Packet)objectInputStream.readObject();
                System.out.println("Packet received");
                if(p.operation.equals("login")){

                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    Server.socketMap.put(p.string1,objectOutputStream);


                }else if(p.operation.equals("send")){

                    p.operation="receive";
                    if(Server.socketMap.get(p.list.get(0).receiver)!=null) {
                        SendingThread sendingThread = new SendingThread(Server.socketMap.get(p.list.get(0).receiver), p);
                        Thread t=new Thread(sendingThread);
                        t.start();
                    }else{
                        Statement stmt = null;
                        try {
                            String url = "jdbc:sqlite:./Databases/BuzzServer.db";
                            conn = DriverManager.getConnection(url);
                            stmt = conn.createStatement();
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String query="insert into Messages (sender,receiver,message,time) values('"+p.list.get(0).sender+"','"+p.list.get(0).receiver+"','"+p.list.get(0).text+"','"+df.format(p.list.get(0).date)+"')";
                        System.out.println(query);
                            stmt.executeUpdate(query);
                        } catch (SQLException e) {
                            System.out.println("Cannot store message to database");
                            e.printStackTrace();
                        }
                    }

                }else if(p.operation.equals("receive")){
                    for(int i=0;i<p.list.size();i++) {
                        final int temp=i;
                        Platform.runLater(() -> {
                            db.receiveMessage(p.list.get(temp));
                        });
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
