package nsu.oop.explorer.backend.model.core.server;

import nsu.oop.explorer.backend.model.events.control.ControlEvent;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Connection {
    class InputStream extends Thread {
        private Socket socket;
        InputStream(Socket socket) {
            this.socket = socket;
            //TODO: set timeout
//            socket.setSoTimeout();
        }

        @Override
        public void run() {
            ObjectInputStream inputStream = null;
            try {
                inputStream = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            while(!isTimeOut) {
                try {
                    ControlEvent msg = (ControlEvent) inputStream.readObject();
                    synchronized (inputMessages) {
                        inputMessages.add(msg);
                    }
                } catch (SocketTimeoutException | EOFException e) {
                    isTimeOut = true;
                    break;
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class OutputStream extends Thread {
        private Socket socket;
        OutputStream(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            ObjectOutputStream outputStream = null;
            try {
                outputStream = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            while(!isTimeOut) {
                try {
                    synchronized (outputMessages) {
                        while(!outputMessages.isEmpty()) {
                            ControlEvent msg = outputMessages.get(0);
                            outputMessages.remove(0);
                            try {
                                outputStream.writeObject(msg);
                            } catch (SocketTimeoutException e) {
                                isTimeOut = true;
                                break;
                            }
                        }

                        outputMessages.wait();
                    }
                } catch (IOException  e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(socket + " is closed");
        }
    }

    private boolean isTimeOut;
    private Socket socket;
    private final List<ControlEvent> inputMessages;
    private final List<ControlEvent> outputMessages;

    private InputStream inputStream;
    private OutputStream outputStream;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        socket.setSoTimeout(10*1000);

        inputMessages = new LinkedList<>();
        outputMessages = new LinkedList<>();

        inputStream = new InputStream(socket);
        outputStream = new OutputStream(socket);
        inputStream.start();
        outputStream.start();

        isTimeOut = false;
    }

    public Connection(String ip, int port) throws IOException {
        this(new Socket(ip, port));
    }

    public void send(ControlEvent event) {
        synchronized (outputMessages) {
            outputMessages.add(event);
            outputMessages.notifyAll();
        }
    }

    public List<ControlEvent> getInput() {
        LinkedList<ControlEvent> list;
        synchronized (inputMessages) {
            list = new LinkedList<>(inputMessages);
            inputMessages.clear();
        }
        return list;
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public boolean isTimeOut() {
        return isTimeOut;
    }

    public void shutdown() {
        try {
            socket.close();
            isTimeOut = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
