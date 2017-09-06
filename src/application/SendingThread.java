package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;

public class SendingThread implements Runnable {
    private Socket clientSocket;
    private Packet sendPacket;
    private ObjectOutputStream serverOutputStream;
    public SendingThread(Socket clientSocket, Packet sendPacket){
        this.clientSocket=clientSocket;
        this.sendPacket=sendPacket;
    }
    @Override
    public void run() {
        try {
            serverOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            serverOutputStream.writeObject(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
