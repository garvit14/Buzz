package application;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static Map<String,ObjectOutputStream> socketMap;

    public static void  main(String args[]) throws Exception{
        socketMap= new HashMap<>();
        ServerSocket serverSocket = new ServerSocket(7777);
        Connection con = getDatabaseConnection();
        while (true) {
            Socket clientSocket = serverSocket.accept();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("Client connected");
            ReceivingThread receivingThread=new ReceivingThread(clientSocket,con,objectOutputStream);
            Thread receive = new Thread(receivingThread);
            receive.start();
//            SendingThread sendingThread=new SendingThread(clientSocket,con);
//            Thread send = new Thread(sendingThread);
//            send.start();
        }
    }
    private static Connection getDatabaseConnection() throws Exception{
            Connection conn = null;
            try {
                // db parameters
                String url = "jdbc:sqlite:./Databases/BuzzServer.db";
                // create a connection to the database
                conn = DriverManager.getConnection(url);

                System.out.println("Connection to SQLite has been established.");

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (conn != null) {
                        //System.out.println("Connection is closing.");
                        conn.close();
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            return conn;
    }
}
