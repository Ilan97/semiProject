package control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * This is controller class (boundary) for window ServerCrushed. This class
 * handle all events related to this windows. This class connect with client.
 *
 * @author Bat-El Gardin
 * @version May 2021
 */

public class ServerCrushedController implements Initializable {

	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgCrash;
	@FXML
	private ImageView imgErr;
	@FXML
	private Button btnApprove;

	// Instance methods ************************************************

	/**
	 * The server's first window and this window's first method. load and show this
	 * window.
	 *
	 * @param primaryStage The stage for window's scene.
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerCrushedWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Server Crush!");
		primaryStage.setScene(scene);
		// close the program
		primaryStage.setOnCloseRequest((event) -> {
			try {
				ClientUI.client.closeConnection();
			} catch (IOException ex) {
				System.out.println("Fail to close client!");
				System.out.println("Exception: " + ex.getMessage());
				ex.printStackTrace();
			}
			System.exit(0);
		});
		primaryStage.show();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'ok' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void approveActionButton(ActionEvent event) {
		try {
			ClientUI.client.closeConnection();
		} catch (IOException ex) {
			System.out.println("Fail to close client!");
			System.out.println("Exception: " + ex.getMessage());
			ex.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set images
		Image img = new Image(this.getClass().getResource("serverCrushed.PNG").toString());
		imgCrash.setImage(img);
		Image img2 = new Image(this.getClass().getResource("error.png").toString());
		imgErr.setImage(img2);
	}

}
//End of ServerCrushedController class