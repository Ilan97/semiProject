package client;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import gui.SearchExamController;

/**
 * This class overrides some of the methods defined in the Application
 * superclass in order to use JavaFX functionalities.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @version April 2021
 */

public class ClientUI extends Application {

	// Instance variables **********************************************

	/**
	 * static variable to save the port's number.
	 */
	final public static int DEFAULT_PORT = 5555;
	/**
	 * static instance for ClientController instance. Will be create only once for
	 * each run.
	 */
	public static ClientController client;

	// Instance methods ************************************************

	/**
	 * The main method of Application class.
	 */
	public static void main(String args[]) throws Exception {
		launch(args);
	}

	/**
	 * Start the client's UI.
	 * 
	 * @param primaryStage The first window that shows.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		try {
			// initialize the instance
			client = new ClientController("localhost", DEFAULT_PORT);
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" + " Terminating client.");
			System.exit(1);
		}
		/**
		 * create and start SearchExam frame.
		 */
		SearchExamController frame = new SearchExamController();
		// close the program
		primaryStage.setOnCloseRequest((event)->{System.exit(0);});
		frame.start(primaryStage);
	}
}
//End of ClientUI class