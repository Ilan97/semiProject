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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * This is controller class (boundary) for window ManualExamCode in Student.
 * This class handle all events related to this window. This class connect with
 * client.
 *
 * @author
 * @version May 2021
 */

public class ManualExamCodeWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private TextField txtCode;
	@FXML
	private Label lblErr;
	
	// Instance methods ************************************************
	
	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ManualExamCodeWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Enter Code");
		primaryStage.setScene(scene);
		//closing the current window
		primaryStage.setOnCloseRequest((event) -> {
			primaryStage.close();
		});
		primaryStage.show();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Download' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void download(ActionEvent event) {
		
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
	}

}
//End of ManualExamCodeWindowController class