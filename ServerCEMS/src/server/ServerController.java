package server;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import control.DBconnector;

import control.ExamController;
import logic.Message;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * This class overrides some of the methods in the AbstractServer superclass in
 * order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class ServerController extends AbstractServer {

	// Instance variables **********************************************

	/**
	 * The successes indication of server-client connection.
	 */
	public boolean connectionSuccessfull = false;
	/**
	 * The server's port number.
	 */
	@SuppressWarnings("unused")
	private int port;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the ServerController.
	 *
	 * @param port The port number to connect on.
	 */
	public ServerController(int port) {
		super(port);
		this.port = port;
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client. There are a few
	 * different cases.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		// message received from client is an Message instance.
		Message messageFromClient = (Message) msg;
		// message that returned from controller
		Message messageFromController = new Message();
		/**
		 * Switch case is on controller's name which returning results from DB (execute
		 * queries).
		 */
		switch (messageFromClient.getControllerName()) {
		case "ExamController":
			messageFromController = ExamController.handleRequest(messageFromClient);
			break;

		} // end of switch

		/**
		 * Send the returned message to the client.
		 */
		try {
			client.sendToClient(messageFromController);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method called when the server starts listening for connections. Connect
	 * to the DB.
	 */
	@Override
	protected void serverStarted() {
		DBconnector.connectToDB();
		display("Server listening for connections on port " + getPort());
	}

	/**
	 * This method called when the server stops listening for connections.
	 * Disconnect the DB.
	 */
	@Override
	protected void serverStopped() {
		DBconnector.closeDB();
		display("Server has stopped listening for connections.");
	}

	/**
	 * This method called when the server is closed.
	 */
	@Override
	protected void serverClosed() {
		display("Server closed");
	}

	/**
	 * This method tries connect to clients.
	 */
	public void runServer() {
		try {
			listen(); // Start listening for connections
			connectionSuccessfull = true;
		} catch (Exception ex) {
			display("ERROR - Could not listen for clients!");
		}
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
//End of ServerController class
