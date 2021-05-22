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

public class ComputerizedExamEnterIDWindowController implements GuiController, Initializable {
	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
    @FXML
    private ImageView imgBack;
    @FXML
    private ImageView imgGoodLuck;
    @FXML
    private TextField txtID;
    @FXML
    private Label lblErrCode;
	
	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ComputerizedExamEnterIDWindow.fxml"));
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
	 * This is FXML event handler. Handles the action of click on 'Start' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void startAction(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide();
		Navigator.instance().clearHistory("ComputerizedExamFormStart");
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
