package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;

public class SendingThread implements Runnable {
    private Packet packet;
    private ObjectOutputStream objectOutputStream;
    public SendingThread(ObjectOutputStream objectOutputStream, Packet packet){
        this.objectOutputStream=objectOutputStream;
        this.packet=packet;
    }

    @Override
    public void run() {
        try {
            if(objectOutputStream==null)
                System.out.println("Maa ki");
            objectOutputStream.writeObject(packet);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
