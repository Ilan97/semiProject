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
 * This is controller class (boundary) for window FailedSaveData. This class
 * handle all events related to this window. This class connect with client.
 *
 * @author Sharon Vaknin
 * @author Moran Davidov
 * @version May 2021
 */

public class FailWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgX;

	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @throws IOException
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/FailedSaveDataWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Alert");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Try Again'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void tryAgainAction(ActionEvent event) {
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
		imgBack.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("x.png").toString());
		imgX.setImage(img2);
	}
}
//End of FailedSaveDataWindowController class