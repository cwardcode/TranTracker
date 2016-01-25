import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatApplet extends JApplet implements MessageListener{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel south;
    private JList userList;
    private JTextArea display;
    private JTextField input;
    private JOptionPane dialogOptions;
    private JButton send;
    private JButton connect;
    private JButton quit;
    private Socket socket;
    private DefaultListModel model;
    private String serverName;
    private int serverPort;
    private NetworkInterface netInter;
    
    public ChatApplet() {
        south = new JPanel();
        display = new JTextArea();
        input = new JTextField();
        dialogOptions = new JOptionPane("Please enter a username:",JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        connect = new JButton("Connect");
        send = new JButton("Send");
        quit = new JButton("Quit");
        model = new DefaultListModel();
        serverName = "tracker.cwardcode.com";
        serverPort = 1723;
    }

    private class ActionList implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("c")){
                connect(serverName, serverPort);
            } else if (e.getActionCommand().equals("q")) {
                send("/quit");
                model.removeAllElements();
                quit.setEnabled(false);
                send.setEnabled(false);
                connect.setEnabled(true);

            } else if(e.getActionCommand().equals("s")) {
                send(input.getText());
                input.requestFocus();
            }
        }
    }

    public void init() {
        quit.setActionCommand("q");
        quit.addActionListener((new ActionList()));
        connect.setActionCommand("c");
        connect.addActionListener(new ActionList());
        send.setActionCommand("s");
        send.addActionListener(new ActionList());

        Panel keys = new Panel();
        keys.setLayout(new GridLayout(1, 2));
        keys.add(quit);
        keys.add(connect);

        south.setLayout(new BorderLayout());
        south.add("West", keys);
        south.add("Center", input);
        south.add("East", send);

        userList = new JList(model);
        userList.setSelectedIndex(0);
        userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        userList.setLayoutOrientation(JList.VERTICAL_WRAP);

        JScrollPane dispScroll = new JScrollPane(display);
        JScrollPane userScroll = new JScrollPane(userList);
        userScroll.setPreferredSize(new Dimension(75,280));

        setLayout(new BorderLayout());
        add("Center", dispScroll);
        add("South", south);
        add("West", userScroll);

        quit.setEnabled(false);
        send.setEnabled(false);
        this.getRootPane().setDefaultButton(send);
        this.setSize(new Dimension(800, 500));
    }
   
    public void connect(String serverName, int serverPort) {
        model.removeAllElements();
        println("Establishing connection. Please wait ...");
        try {
            socket = new Socket(serverName, serverPort);
            netInter = new NetworkInterface(socket);
            open(getHandle());
            println("Connected");
            send.setEnabled(true);
            connect.setEnabled(false);
            quit.setEnabled(true);
        } catch (UnknownHostException uhe) {
            println("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            println("Unexpected exception: " + ioe.getMessage());
        }
    }

    private String getHandle() {
        dialogOptions.setMessage("Please enter a username");
        String name;
        name = JOptionPane.showInputDialog(null, "What\'s your name?", "Enter your username:", JOptionPane.WARNING_MESSAGE);
        return name;
    }

    private void send(String message) {
            try {
                netInter.sendMessage(message);
                input.setText("");
            } catch (IOException e) {
                println("could not send message");
            }

    }

    public void addToUL(String msg){
        model.addElement(msg);
        userList.validate();
    }

    public void open(String nick) {
        Thread t = new Thread(netInter);
        try {
            t.start();
            netInter.addMessageListener(this);
            println("User client is joining");
            send("/hello " + nick);

        } catch (IllegalThreadStateException e) {
            println("Couldn't start network interface thread in client");
        }
    }

    private void println(String msg) {
        display.append(msg + "\n");
        display.setCaretPosition(display.getDocument().getLength());
    }
    
    @Override
    public void destroy() {
    	send("/quit");
    	super.destroy();
    }

    @Override
    public void messageReceived(String message, MessageSource source) throws IOException {
        if (message.contains("!!!") && message.contains("has joined.")){
            model.removeAllElements();
            send("/who");
        } else if(message.contains("has quit.")) {
            model.removeAllElements();
            send("/who");
        } else if(message.contains("-_user:")){
            addToUL(message.substring(8, message.length()));
        } else {
                println(message);
        } 
    }

    @Override
    public void sourceClosed(MessageSource source) {
        try {
            netInter.close();
            System.exit(0);
        } catch (IOException e) {
            println("could not close");
        }
    }
}