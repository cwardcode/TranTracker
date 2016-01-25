import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is an abstract class that is extended 
 * by the Network Interface class
 *
 * @author Ben Dana
 * @author Chris Ward
 * @version 9.11.12
 */
public abstract class MessageSource {

    /* This field contains a list of messageListeners */
	private List<MessageListener> messageListeners;

    /**
     * This is the constructor that initialized the list field.
     */
	public MessageSource() {
		this.messageListeners = new ArrayList<MessageListener>();
	}

    /**
     * This method adds listeners to the list.
     *
     * @param listener is a MessageListener to be added.
     */
	public void addMessageListener(MessageListener listener) {
		messageListeners.add(listener);
	}

    /**
     * This method removed listeners from the list.
     *
     * @param listener is a MessageListener to be removed.
     */
	public void removeMessageListener(MessageListener listener) {
		messageListeners.remove(listener);
	}

    /**
     * This method notifies the list of listeners of a Receipt.
     *
     * @param message to notify the list of.
     * @throws RuntimeException if listener cannot take message.
     * @throws IOException if incorrect output/input
     */
	protected void notifyReceipt(String message) throws IOException{
		for (MessageListener listener :
				new ArrayList<MessageListener>(messageListeners)) {
			// We wrap this in a try/catch block so that just in case
			// one of our observers screws up, we don't want to stop
			// notifying other observers.
			try {
				listener.messageReceived(message, this);
			} catch (RuntimeException ex) {
				ex.printStackTrace();
			}
		}
	}

    /**
     * This method closes down the list, sends the shutdown message
     * to all listeners.
     *
     * @throws IOException if incorrect output/input.
     * @throws RuntimeException if listener won't shut down.
     */
	protected void closeMessageSource() throws IOException {
		// Here we need to iterate over a *copy* of our messageListeners list.
		// The reason is because if the listener's 'sourceClosed' method
		// removes that listener from this subject, we'd get a
		// ConcurrentModificationException if we were iterating over the
		// original list.
		for (MessageListener listener :	new ArrayList<MessageListener>(messageListeners)) {
			try {
				listener.sourceClosed(this);
			} catch (RuntimeException ex) {
				// Ignore any exceptions encountered when trying to close
				// a source. There's a similar rationale here as we had
				// with ignoring exceptions when we tried to close streams.
			}
		}
		messageListeners.clear();
	}
}
