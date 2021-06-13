package nsu.oop.explorer.backend.tester;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class DoesObjectInputStreamLocks {
    /*
    Conclusion: calling ObjectInputStream.readObject() locks a stream
    */
    class Sender extends Thread {
        Socket socket;
        PrintStream out;
        Sender(PrintStream out) throws IOException {
            socket = new Socket("localhost", port);
            this.out = out;
        }

        @Override
        public void run() {
            out.println("Sender is there!");

            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            out.println("Time's out! Gonna send!");

            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(Integer.valueOf(10));
                objectOutputStream.close();
                out.println("Object is sent!");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    final int port = 25565;
    ServerSocket serverSocket;
    Sender sender;

    public DoesObjectInputStreamLocks() throws IOException {
        serverSocket = new ServerSocket(port);
        sender = new Sender(System.out);

    }

    void run() throws IOException {
        sender.start();
        System.out.println("Server is active!!!");
        Socket socket = serverSocket.accept();
        System.out.println("There is someone!!!");
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        try {
            Integer testObject = (Integer)objectInputStream.readObject();
            System.out.println("Received an object!!!");
            System.out.println(testObject);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            DoesObjectInputStreamLocks tester = new DoesObjectInputStreamLocks();
            tester.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
