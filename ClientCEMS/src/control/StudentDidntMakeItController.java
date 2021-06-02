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
 * This is controller class (boundary) for window StudentDidntMakeIt in Student.
 * This class handle all events related to this windows. This class connect with
 * client.
 *
 * @author Ohad Shamir
 * @author Bat-El Gardin
 * @version May 2021
 */

public class StudentDidntMakeItController implements GuiController, Initializable {

	// Instance variables **********************************************

	@FXML
	private ImageView imgCrash;
	@FXML
	private ImageView imgErr;

	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @throws IOException
	 */
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/StudentDidntMakeIt.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Are you Sure?");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Ok' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void ok(ActionEvent event) {
		UsefulMethods.instance().close(event);
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set images
		Image img = new Image(this.getClass().getResource("serverCrushed.PNG").toString());
		imgCrash.setImage(img);
		Image img2 = new Image(this.getClass().getResource("error.png").toString());
		imgErr.setImage(img2);
	}
}
//End of SubmissionAgreementWindowController class