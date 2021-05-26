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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * This is controller class (boundary) for window ComputerizedExamCode in
 * Student. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author Sharon Vaknin
 * @author Bat-El Gardin
 * @version May 2021
 */

public class ComputerizedExamCodeWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private TextField txtCode;
	@FXML
	private Label lblErrCode;
	
	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ComputerizedExamCodeWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Verify");
		primaryStage.setScene(scene);
		// closing the current window and return to home page
		primaryStage.setOnCloseRequest((event) -> {
			primaryStage.close();
			Navigator.instance().clearHistory("StudentHomeForm");
		});
		primaryStage.show();
	}
	
	/**
	 * This is FXML event handler. Handles the action of click on 'Next' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	 void nextAction(ActionEvent event){
		// successes pop up
		ComputerizedExamEnterIDWindowController popUp = new ComputerizedExamEnterIDWindowController();
		try {
			((Node)event.getSource()).getScene().getWindow().hide();
			popUp.start(new Stage());
		
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		
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
//End of ComputerizedExamCodeWindowController class