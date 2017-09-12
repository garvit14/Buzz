package application;

public class OnlineStatusThread implements Runnable{

    private MainController controller;
    private Main main;

    public OnlineStatusThread(MainController controller,Main main){
        this.controller=controller;
        this.main=main;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.currentThread().sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Packet packet = new Packet();
            packet.operation="onlinerequest";
            packet.string1=Main.user.userName;
            packet.peopleList.addAll(controller.peopleList);
            SendingThread sendingThread = new SendingThread(main.objectOutputStream,packet);
            Thread t = new Thread(sendingThread);
            t.start();
        }
    }
}
