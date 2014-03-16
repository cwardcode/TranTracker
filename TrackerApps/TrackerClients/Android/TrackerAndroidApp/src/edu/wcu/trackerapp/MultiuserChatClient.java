package edu.wcu.trackerapp;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * A class that encompasses the main parts of the client
 * side of the application.
 *
 * @author Ben Dana
 * @author Chris Ward
 * @version 11/9/12
 */
public class MultiuserChatClient extends MessageSource 
                                                    implements MessageListener {

    /* This field holds the InetAddress reference. */
    private InetAddress host;

    /* This field holds the port number. */
    private int port;

    /* This field holds the string nickname. */
    private String nickname;

    /* This field holds the MessageListener reference. */
    private MessageListener listener;

    /* This field holds the socket reference. */
    private Socket clientSock;

    /* This field holds the printstream reference. */
    private PrintStream stream;

    /* This field holds the NetworkInterface class reference. */   
    private NetworkInterface netInter;

    /** 
     * This is the constructor of the class that takes the parameters 
     * and initializes the class.
     *
     * @param hostname holds the string hostname to connect to.
     * @param port holds the port int to connect the socket to.
     * @param nickname is the string to give the server as a username.
     * @param out is the printstream from the client's prompt.
     */
    public MultiuserChatClient(String hostname, int port, String nickname,
            PrintStream out) {
        try {
            this.host = InetAddress.getByName(hostname);
        } catch(UnknownHostException e) {
            System.out.println("Host could not be found!");
        }
        this.port = port;
        this.nickname = nickname;
        stream = out;
    }

    /**
     * This class creates the socket as well as the 
     * PrintStreamMessageListener, NetworkInterface, 
     * and thread.
     */
    public void connect() throws IOException {
        try {
            clientSock = new Socket(host, port);
        } catch (SocketException e) {
            System.out.println("Could not connect to given socket and port");
        }
        listener = new PrintStreamMessageListener(stream);
        netInter = new NetworkInterface(clientSock);
        Thread t = new Thread(netInter);
        try {
            t.start();
            netInter.addMessageListener(this);
            stream.println("User client is joining");
            send("/hello " + nickname);
        } catch (IllegalThreadStateException e) {
            stream.println("Couldn't start network interface thread in client");
        }
    }

    /**
     * This class sends a string message through the 
     * NetworkInterface.
     *
     * @param message is the string to be sent through
     */
    public void send(String message) {
        try {
            netInter.sendMessage(message);
        } catch (IOException e) {
            stream.println("could not send message");
        }
    }

    /**
     * This method is responsible for receiving messages
     * and passing htem onto the listener.
     *
     * @param message is the string that was received.
     * @param source is the network interface it originated from.
     */
    public void messageReceived(String message, MessageSource source)
                                                            throws IOException {
        listener.messageReceived(message, source); /* what to do with source? */

    }

    /**
     * This method closes the network interface.
     *
     * @param source is depreciated.
     */
    public void sourceClosed(MessageSource source) {
        try {
            netInter.close();
            System.exit(0);
        } catch (IOException e) {
            stream.println("could not close");
        }
    }

    /**
     * This method is an accessor method that
     * returns a pointer to the NetworkInterface.
     * 
     * @return netInter is a pointer to the field.
     */
    public NetworkInterface getNetworkInterface() {
        return netInter;
    }
}

