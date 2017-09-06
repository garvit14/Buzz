package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//this class also conatins server database methods
public class ReceivingThread implements Runnable {
    private Socket clientSocket;
    private Connection conn;
    private ObjectInputStream serverInputStream;
    public ReceivingThread(Socket clientSocket, Connection conn){
        this.clientSocket=clientSocket;
        this.conn=conn;
    }
    @Override
    public void run() {
        try {
            serverInputStream = new ObjectInputStream(clientSocket.getInputStream());
            while (true){//reads any input from the client till apocalypse
                Packet receivedPacket = (Packet)serverInputStream.readObject();
                if(receivedPacket.operation == "login"){
                    boolean loginResult = login(receivedPacket);
                    if(loginResult)
                        Server.socketMap.put(clientSocket,receivedPacket.string1);
                        Packet sendPacket = new Packet();;
                        sendPacket.result=loginResult;
                        SendingThread sendingThread = new SendingThread(clientSocket, sendPacket);
                        Thread send = new Thread(sendingThread);
                        send.start();
                }
                if(receivedPacket.operation == "message"){

                }

                if(receivedPacket.operation == "send"){

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Database methods
    public boolean login(Packet p) throws SQLException {
        String username = p.string1;
        String password = p.string2;
        String query = "Select * from User where username="+username+" and password="+password;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if(rs.next())
            return true;
        else
            return false;
    }
}
