import java.io.IOException;

/**
 * This class is an interface that is to be implemented by
 * classes that want to be able to be notified of incoming
 * messages.
 *
 * @author Ben Dana
 * @author Chris Ward
 * @version 11.9.12
 */
public abstract interface MessageListener {

    /**
     * This method is designed to be implemented so
     * to receive a message and originating source.
     *
     * @param message would be message to be received.
     * @param source is MessageSource that it came from.
     * @throws IOException if input/output error occurs.
     */
	public void messageReceived(String message, MessageSource source) throws
                                                                    IOException;

	/**
     * This method is designed to shut down the Message Source passed.
     *
     * @param source is MessageSource to be shut down.
     * @throws IOException if error with input/output.
     */
	public void sourceClosed(MessageSource source) throws IOException;
}
