package edu.wcu.trackerapp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
/**
 * An Android implementation of our Shuttle Speak Chat client. 
 * Connects to the server through an AsyncTask, then prompts a user
 * to enter a username, and then allows the user to chat with others.
 * 
 * @author Chris Ward
 * @version 15/03/2014
 *
 */
public class Chat extends Activity implements OnClickListener, MessageListener {

	/** The map menu button. */
	private Button map;
	/** The key menu button */
	private Button key;
	/** The help menu button. */
	private Button help;
	/** The about menu button. */
	private Button about;
	/** socket that allows us to connect to server */
	private Socket socket;
	/** NetworkInterface allows us to establish a client with the server */
	private NetworkInterface netInter;
	/** The address of the server in which to connect */
	private final String SERVER = "tracker.cwardcode.com";
	/** The port of the server in which the service is running */
	private final int PORT = 2014;
	/** The scroll view that allws the messages to scroll down the screen. */
	private ScrollView scroller;
	/** TextView which displays messages. */
	private TextView messages;
	/** Allows user to enter a message. */
	private EditText input;
	/** Button used to send message to server. */
	@SuppressWarnings("unused")
	private Button sendButton;

	/**
	 * Allows us to connect to the chat server
	 * 
	 * @author Chris Ward
	 * @version 15/03/2014
	 * 
	 */
	private class ConnectToNetwork extends AsyncTask<String, Void, String> {

		@Override
		/**
		 * Runs in background asynchroniously.
		 * @param arg0 array of arguments - Unused. 
		 * @return null
		 */
		protected String doInBackground(String... arg0) {
			connect(SERVER, PORT);
			return null;
		}

	}

	/**
	 * Creates our "Enter username" dialog.
	 * 
	 * @author Chris Ward
	 * @version 15/03/2014
	 */
	public class HandleDialogFragment extends DialogFragment {
		@Override
		/**
		 * Creates the username prompt.
		 * @param savedInstanceState
		 * @return the created dialog
		 */
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View content = inflater.inflate(R.layout.dialoglayout, null);
			// Allow us to pass username to connect method
			final EditText user = (EditText) content
					.findViewById(R.id.username);
			// Inflate and set the layout for the dialog
			// Pass null as the parent view because its going in the dialog
			// layout
			builder.setView(content)
					// Add action buttons
					.setPositiveButton(R.string.signin,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									if (user.getText().toString().isEmpty()) {
										Toast.makeText(getApplicationContext(),
												"Invalid username entered!",
												Toast.LENGTH_SHORT).show();
										Intent i = new Intent(
												getApplicationContext(),
												Chat.class);
										i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(i);
									} else {
										open(user.getText().toString());
									}
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									HandleDialogFragment.this.getDialog()
											.cancel();
									finish();
								}
							});
			return builder.create();
		}
	}

	/**
	 * Returns to the proper screen when a specified menu button is pressed.
	 * 
	 * @param v
	 *            the view that was pressed.
	 */
	@Override
	public void onClick(View v) {
		Button button = (Button) v;

		if (button.equals(map)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.Map.class);
			next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(next);
		} else if (button.equals(help)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.Chat.class);
			next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(next);
		} else if (button.equals(key)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.Key.class);
			next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(next);
		} else if (button.equals(about)) {
			Intent next = new Intent(this, edu.wcu.trackerapp.About.class);
			next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(next);
		}

	}

	/**
	 * Initializes the activity.
	 * 
	 * @param savedInstanceState
	 *            saved data from a previous run, if any.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle("TranTracker");
		setContentView(R.layout.activity_chat);

		map = (Button) findViewById(R.id.chatMapButton);
		key = (Button) findViewById(R.id.chatKeyButton);
		about = (Button) findViewById(R.id.chatAboutButton);
		help = (Button) findViewById(R.id.chatHelpButton);
		messages = (TextView) findViewById(R.id.outputTextView);
		sendButton = (Button) findViewById(R.id.sendBtn);
		input = (EditText) findViewById(R.id.inputEditText);
		scroller = (ScrollView) findViewById(R.id.chatScroll);

		map.setOnClickListener(this);
		key.setOnClickListener(this);
		about.setOnClickListener(this);
		help.setOnClickListener(this);

		new ConnectToNetwork().execute("");
	}

	/**
	 * Connects to the server
	 * 
	 * @param serverName
	 *            URL of server
	 * @param serverPort
	 *            Port on server in which to connect
	 */
	public void connect(String serverName, int serverPort) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				messages.setText("");
				messages.setText("Establishing connection. Please wait ...");
			}
		});
		try {
			socket = new Socket(serverName, serverPort);
			// Establish client with server
			netInter = new NetworkInterface(socket);
			// prompt for username
			DialogFragment dialog = new HandleDialogFragment();
			dialog.setCancelable(false);
			dialog.show(getFragmentManager(), "username");
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					messages.setText("Connected");
				}
			});
		} catch (final UnknownHostException uhe) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					messages.setText("Host unknown: " + uhe.getMessage());
				}
			});
		} catch (final IOException ioe) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					messages.setText("Unexpected exception: "
							+ ioe.getMessage());
				}
			});
		}
	}

	/**
	 * Opens communication from client to server.
	 * 
	 * @param nick
	 *            nickname of user
	 */
	public void open(String nick) {
		Thread t = new Thread(netInter);
		try {
			t.start();
			netInter.addMessageListener(this);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					messages.append("User client is joining");
				}
			});
			send("/hello " + nick);

		} catch (IllegalThreadStateException e) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					messages.append("Couldn't start network interface "
							+ "thread in client");
				}
			});
		}
	}

	/**
	 * Sends message when the 'send' button is pressed.
	 * 
	 * @param view
	 *            the view from which the method is called.
	 */
	public void onSendClicked(View view) {
		send(input.getText().toString());
	}

	/**
	 * Sends user's message to server.
	 * 
	 * @param message
	 *            string sent to server
	 */
	private void send(String message) {
		try {
			netInter.sendMessage(message);
			input.setText("");
		} catch (IOException e) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					messages.append("could not send message");
				}
			});
		} catch (NullPointerException ex) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					messages.append("Could not connect to network.");
				}
			});
		}
	}

	@Override
	/**
	 * Called when activity is deconstructed.
	 * Ensures user is disconnected from server.
	 */
	public void onDestroy() {
		super.onDestroy();
		send("/quit");
	}

	@Override
	/**
	 * Notifies application when a new message is received. Posts message
	 * to the messages TextView.
	 * @param message Message which was sent.
	 * @param source User who sent message.
	 */
	public void messageReceived(final String message, MessageSource source)
			throws IOException {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				messages.append("\n" + message);
			}
		});
		scroller.fullScroll(ScrollView.FOCUS_DOWN);
	}

	@Override
	/**
	 * Closes network interface on exit.
	 */
	public void sourceClosed(MessageSource source) {
		try {
			netInter.close();
			System.exit(0);
		} catch (IOException e) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					messages.append("\n Could not Close");
				}
			});
		}
	}

}
