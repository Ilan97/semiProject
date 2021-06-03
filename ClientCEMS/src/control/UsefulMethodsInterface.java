package control;

import javafx.event.ActionEvent;

public interface UsefulMethodsInterface {
	/**
	 * This method close the current stage.
	 * 
	 * @param event The action event that we want to close the stage because of it.
	 */
	void close(ActionEvent event);

	/**
	 * This method displays a message into the console.
	 *
	 * @param message The string to be displayed.
	 */
	void display(String message);

	/**
	 * This method displays the exception.
	 *
	 * @param excpetion The Exception.
	 */
	void printException(Exception e);
}