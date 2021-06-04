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
 * This is controller class (boundary) for window ExamSettings in Teacher. This
 * class handle all events related to this window. This class connect with
 * client.
 *
 * @author Bat-El Gardin
 * @author Ilan Meikler
 * @version June 2021
 */

public class ExamSettingsWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	@FXML
	private ImageView imgBack;

	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @throws IOException
	 */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ExamSettingsWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Exam Settings");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Change Duration'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void changeDurAction(ActionEvent event) {
		RequestChangeDurationExamTimeController popUp = new RequestChangeDurationExamTimeController();
		try {
			popUp.start(new Stage());
		} catch (Exception e) {
			UsefulMethods.instance().printException(e);
		}
		UsefulMethods.instance().close(event);
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Lock Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void lockExamAction(ActionEvent event) {
		LockExamWindowController popUp = new LockExamWindowController();
		try {
			popUp.start(new Stage());
		} catch (Exception e) {
			UsefulMethods.instance().printException(e);
		}
		UsefulMethods.instance().close(event);
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set images
		Image img = new Image(this.getClass().getResource("examSettings.PNG").toString());
		imgBack.setImage(img);
	}
}
//End of ExamSettingsWindowController class