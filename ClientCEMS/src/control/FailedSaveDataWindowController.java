package control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class FailedSaveDataWindowController implements GuiController, Initializable {
	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgX;
	@FXML
	private ImageView imgErr;

	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/FailedSaveDataWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Alert");
		primaryStage.setScene(scene);
		//closing the current window and return to home page
		primaryStage.setOnCloseRequest((event) -> {
			primaryStage.close();
			Navigator.instance().clearHistory("TeacherHomeForm");
		});
		primaryStage.showAndWait();
	}
	
	/**
	 * This is FXML event handler. Handles the action of click on 'Try Again' button.
	 *
	 * @param event The action event.
	 */
    @FXML
    void tryAgainAction(ActionEvent event) {

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
		imgErr.setImage(img2);
	}
}
//End of FailedSaveDataWindowController class