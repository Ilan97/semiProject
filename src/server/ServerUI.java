package server;

/**
 * This class overrides some of the methods defined in the Application
 * superclass in order to use JavaFX functionalities.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Ohad Shamir
 * @version May 2021
 */

import gui.ServerPortController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ServerUI extends Application {

	// Instance methods ************************************************

	/**
	 * The main method of Application class.
	 */
	public static void main(String args[]) throws Exception {
		launch(args);
	}

	/**
	 * Start the server's UI. Create and start ServerPort frame.
	 * 
	 * @param primaryStage The first window that shows.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		ServerPortController frame = new ServerPortController(); // create PortFrame
		frame.start(primaryStage);
	}
}
//End of ServerUI class