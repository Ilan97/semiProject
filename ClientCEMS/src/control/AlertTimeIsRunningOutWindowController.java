package control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * This is controller class (boundary) for window AlertTimeIsRunningOut. This
 * class handle all events related to this window. This class connect with
 * client.
 *
 * @author Sharon Vaknin
 * @version June 2021
 */

public class AlertTimeIsRunningOutWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	@FXML
	private ImageView imgCrash;
	@FXML
	private ImageView imgTimer;

	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @throws IOException if an I/O error occurs when opening.
	 */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/AlertTimeIsRunningOutWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Alert");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Ok' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void okAction(ActionEvent event) {
		UsefulMethods.instance().close(event);
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set images
		Image img1 = new Image(this.getClass().getResource("serverCrushed.PNG").toString());
		imgCrash.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("timer.png").toString());
		imgTimer.setImage(img2);
	}
}
// End of AlertTimeIsRunningOutWindowController class