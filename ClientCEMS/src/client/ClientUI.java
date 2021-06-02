package client;

import java.io.IOException;

import control.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This class overrides some of the methods defined in the Application
 * superclass in order to use JavaFX functionalities.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @author Moran Davidov
 * @version April 2021
 */

public class ClientUI extends Application {

	// Instance variables **********************************************

	/**
	 * Static variable to save the port's number.
	 */
	final public static int DEFAULT_PORT = 5555;
	/**
	 * Static instance for {@link ClientController} instance. Will be create only
	 * once for each run.
	 */
	public static ClientController client;

	// Instance methods ************************************************

	/**
	 * The main method of Application class.
	 * 
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		launch(args);
	}

	/**
	 * Start the client's UI.
	 * 
	 * @param primaryStage The first window that shows.
	 * @throws Exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			// initialize the instance
			client = new ClientController("localhost", DEFAULT_PORT);
		} catch (IOException exception) {
			display("Error: Can't setup connection!" + " Terminating client.");
			System.exit(1);
		}
		// create and start Login frame.
		LoginController frame = new LoginController();
		frame.start(primaryStage);
	}

	/**
	 * This method displays a message into the console.
	 *
	 * @param message The string to be displayed.
	 */
	public static void display(String message) {
		System.out.println("> " + message);
	}
}
//End of ClientUI class