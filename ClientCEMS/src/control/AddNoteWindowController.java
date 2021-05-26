package control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * This is controller class (boundary) for window AddNote in Teacher. This class
 * handle all events related to this window. This class connect with client.
 *
 * @author Bat-El Gardin
 * @author Ilan Meikler
 * @author Moran Davidov
 * @version May 2021
 */

public class AddNoteWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * The notes that are entered
	 */
	public String teachNote = null;
	public String studNote = null;

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private Button btnCancel;
	@FXML
	private TextArea txtTeachNote;
	@FXML
	private TextArea txtStudNote;

	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @return the "real" controller.
	 */
	public Object start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/AddNoteWindow.fxml"));
		Parent root = loader.load();
		AddNoteWindowController cont = loader.getController();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Add Note");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
		return cont;
	}

	/**
	 * This method close the current stage.
	 */
	private void close(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Add' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void addNoteActionButton(ActionEvent event) {
		teachNote = txtTeachNote.getText();
		studNote = txtStudNote.getText();
		close(event);
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Cancel' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void cancelActionButton(ActionEvent event) {
		close(event);
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set images
		Image img = new Image(this.getClass().getResource("teacherFrame.PNG").toString());
		imgBack.setImage(img);
	}

}
//End of QuestionScoreWindowController class