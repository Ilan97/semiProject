package control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * This is controller class (boundary) for window ExamIsReadyToPerform. This
 * class handle all events related to this window. This class connect with
 * client.
 *
 * @author Sharon Vaknin
 * @author Moran Davidov
 * @version May 2021
 */

public class ExamIsReadyToPerformWindowController implements GuiController, Initializable {
	
	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgOk;

	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ExamIsReadyToPerformWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Success!");
		primaryStage.setScene(scene);
		//closing the current window and return to home page
		primaryStage.setOnCloseRequest((event) -> {
			primaryStage.close();
			Navigator.instance().clearHistory("TeacherHomeForm");
		});
		primaryStage.show();
	}
	
	/**
	 * This is FXML event handler. Handles the action of click on 'Ok' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void okAction(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide();
		Navigator.instance().clearHistory("TeacherHomeForm");
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set images
		Image img1 = new Image(this.getClass().getResource("teacherFrame.PNG").toString());
		imgBack.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("ok.png").toString());
		imgOk.setImage(img2);
	}

}
//End of ExamIsReadyToPerformWindowController class