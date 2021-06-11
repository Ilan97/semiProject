package client;

import java.io.IOException;
import control.ServerCrushedController;
import javafx.application.Platform;
import javafx.stage.Stage;
import logic.Message;
import ocsf.client.AbstractClient;

/**
 * This class overrides some of the methods defined in the AbstractClient
 * superclass in order to give more functionality to the client.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @author Moran Davidov
 * @version May 2021
 */
public class ClientController extends AbstractClient implements IClientController {

	// Instance variables **********************************************

	/**
	 * The message that returns from server
	 */
	private Object message;
	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	public static boolean awaitResponse = false;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the ClientController.
	 *
	 * @param host The server to connect to.
	 * @param port The port number to connect on.
	 * @throws IOException if an I/O error occurs when opening.
	 */
	public ClientController(String host, int port) throws IOException {
		super(host, port);
		openConnection(); // check connection between client and server 5555
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		display("HandleMessageFromServer");
		Message messageFromServer = ((Message) msg);
		message = messageFromServer.getMsg();
		awaitResponse = false; // no longer wait for response from server (received message)
	}

	/**
	 * This method handles all data coming from the UI.
	 *
	 * @param msg {@link Message} The message from the UI.
	 * @return message The result from server
	 * 
	 */
	@Override
	public Object handleMessageFromClientUI(Message msg) {
		try {
			openConnection();
			awaitResponse = true;
			display("Message recieved from clientController: " + msg.toString());
			sendToServer(msg);
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}

			Object ret = message;
			message = null;
			return ret;
		} catch (IOException e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
			display("Could not send message to server: Terminating client." + e);
			quit();
		}
		return null;
	}

	/**
	 * This method called when the client is close connection with server.
	 */
	@Override
	protected void connectionClosed() {
		display("Client disconnected");
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * This method called when client connection closed.
	 */
	@Override
	protected void connectionEstablished() {
		Thread con = Thread.currentThread();
		new Thread(() -> {
			// the loop is staying while there is connection to the server
			while (con.isAlive()) {
				try {
					con.join();
				} catch (InterruptedException e) {
					System.out.println("Exception: " + e.getMessage());
					e.printStackTrace();
				}
			}
			// run the specified Runnable on the JavaFX Application Thread at some
			// unspecified time in the future (when server is suddenly disconnected)
			Platform.runLater(() -> {
				// pop up window - will close the client on exit
				ServerCrushedController serverCrushedController = new ServerCrushedController();
				try {
					serverCrushedController.start(new Stage());
				} catch (Exception e) {
					System.out.println("Exception: " + e.getMessage());
					e.printStackTrace();
				}
			});
		}).start();
	}

	/**
	 * This method displays a message into the console.
	 *
	 * @param message The string to be displayed.
	 */
	public void display(String message) {
		System.out.println("> " + message);
	}
	
	
}
//End of ClientController class