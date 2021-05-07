// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import java.io.IOException;

import gui.SearchExamController;
import logic.Exam;
import logic.Message;
import ocsf.client.AbstractClient;

/**
 * This class overrides some of the methods defined in the AbstractClient
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ClientController extends AbstractClient {

	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	public static boolean awaitResponse = false;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the ClientController.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
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
		awaitResponse = false; // no longer wait for response from server (received message)
		Message messageFromServer = ((Message) msg);
		/**
		 * External switch case is on controller's name which returning results from DB.
		 * Internal switch case is on the operations every controller ask to operate.
		 */
		switch (messageFromServer.getControllerName()) {
		case "ExamController":
			switch (messageFromServer.getOperation()) {
			case "viewTableExam":
				SearchExamController.exam = (Exam) messageFromServer.getMsg();
				break;

			case "updateExamDurationTime":
				SearchExamController.exam.setDuration((int) messageFromServer.getMsg());
				break;
			} // end internal switch case
		} // end external switch case
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param msg The message from the UI.
	 */
	public void handleMessageFromClientUI(Message msg) {
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
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			display("Could not send message to server: Terminating client." + e);
			quit();
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

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
//End of ClientController class