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
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * This is controller class (boundary) for window ManualExamDownloaded in
 * Student. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author Bat-El Gardin
 * @author Moran Davidov
 * @version May 2021
 */

public class ManualExamDownloadedWindowController implements GuiController, Initializable {
	// Instance variables **********************************************

	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgGoodLuck;
	@FXML
	private Button btnStart;

	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @throws IOException if an I/O error occurs when opening.
	 */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ManualExamDownloadedWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Download success!");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
	}

	/**
	 * This is FXML event handler. Handles the action of press on enter key.
	 *
	 * @param event The action event.
	 */
	@FXML
	void inputPass(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER))
			btnStart.fire();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Start' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void startAction(ActionEvent event) {
		UsefulMethods.instance().close(event);
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set images
		Image img = new Image(this.getClass().getResource("studentFrame.PNG").toString());
		imgBack.setImage(img);
		Image img1 = new Image(this.getClass().getResource("smile.png").toString());
		imgGoodLuck.setImage(img1);
	}
}
//End of ComputerizedExamEnterIDWindowController class