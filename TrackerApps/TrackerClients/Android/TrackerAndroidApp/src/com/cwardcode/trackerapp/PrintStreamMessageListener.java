package com.cwardcode.trackerapp;
import java.io.PrintStream;

/**
 * This class handles the streaming of output into 
 * the PrintStream of the client.
 *
 * @author Ben Dana
 * @author Chris Ward
 * @version 11.09.12
 */
public class PrintStreamMessageListener implements MessageListener {
    /* This field is the reference to the PrintStream */
    private PrintStream out;
    /**
     * This is the constructor that takes the input stream
     * and passes it to the field.
     *
     * @param out is the PrintStream for the client.
     */
   public PrintStreamMessageListener(PrintStream out) {
      this.out = out; /* is clients System.out */
   }
    /**
     * This method received a message and the source and prints 
     * out the message.
     *
     * @param message contains the message.
     * @param source is depreciated.
     */
   public void messageReceived(String message, MessageSource source) {
      out.println(message);
   }
    /**
     * This method is depreciated.
     *
     * @param source is depreciated.
     */
   public void sourceClosed(MessageSource source) {
      //if (source instanceof NetworkInterface) {
      //   source.close();
      //}
   }
}

