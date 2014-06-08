package com.cwardcode.trackerapp;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintStream;
import java.io.IOException;

/**
 * This class is designed to be the network interface for 
 * either client or server.
 *
 * @author Ben Dana
 * @author Chris Ward.
 * @version 9.11.12
 */
public class NetworkInterface extends MessageSource implements Runnable {

    /* This socket is the network connection field */
    Socket socket;

    /* This is the field that stores a pointer to the BufferedReader */
    BufferedReader in;

    /* This is the PrintStream field */
    PrintStream out;

    /**
     * This is the constructor that takes a Socket parameter
     * and creates a BufferedReader to read from it.
     *
     * @param newSocket is the socket connection.
     * @throws IOException if input/output error.
     */
    public NetworkInterface(Socket newSocket) throws IOException {
        this.socket = newSocket;
        this.in = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
        this.out = new PrintStream(this.socket.getOutputStream());
    }

    /**
     * This method is meant to send a message through the 
     * connection.
     *
     * @param message is the message to be sent through.
     * @throws IOException if input/output error.
     */
    public void sendMessage(String message) throws IOException {
        this.out.println(message);
    }

    /**
     * This method checks to see if the socket is still open.
     * 
     * @return true socket is connected.
     */
    public boolean isConnected() {
        return this.socket.isConnected();
    }

    /**
     * This method attempts to close the network interface
     * gracefully.
     *
     * @throws IOException if things aren't graceful.
     */
    public void close() throws IOException {
        this.in.close();
        this.out.close();
        this.socket.close();
    }

    /**
     * This method is implemented from Runnable interface
     * and is the centerpiece for this class's threading.
     */
    public void run() {
        String message;
        try {
            while (this.isConnected()) {
                message = this.in.readLine();
                if(message != null) {
                    super.notifyReceipt(message);
                }
            }         
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
