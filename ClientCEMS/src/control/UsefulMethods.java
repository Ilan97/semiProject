package control;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * (Singleton) class for using useful method that we use a lot.
 *
 * @author Bat-El Gardin
 * @version June 2021
 */

public class UsefulMethods implements UsefulMethodsInterface {

	private static UsefulMethodsInterface instance = null;

	/**
	 * (Singleton) get an instance of UsefulMethods.
	 * 
	 * @return UsefulMethods - the instance of this class.
	 */
	public static UsefulMethodsInterface instance() {
		if (instance == null)
			instance = new UsefulMethods();
		return instance;
	}

	@Override
	public void close(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	@Override
	public void display(String message) {
		System.out.println("> " + message);
	}

	@Override
	public void printExeption(Exception e) {
		display("Exception: " + e.getMessage());
		e.printStackTrace();
	}
}
//End of UsefulMethods class