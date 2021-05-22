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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * This is controller class (boundary) for window QuestionScore in Teacher. This
 * class handle all events related to this window. This class connect with
 * client.
 *
 * @author Bat-El Gardin
 * @author Ilan Meikler
 * @author Moran Davidov
 * @version May 2021
 */

public class QuestionScoreWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * The score that is entered.
	 */
	public int score = 0;

	/**
	 * The notes that are entered (from addNote window)
	 */
	public String teachNote = null;
	public String studNote = null;

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private Button btnSaveScore;
	@FXML
	private TextField txtScore;
	@FXML
	private Label lblErr;
    @FXML
    private Label lblRange;

	// Instance methods ************************************************

	/**
	 * @return the score from window.
	 */
	public String getScore() {
		return txtScore.getText();
	}

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @return the "real" controller.
	 */
	public Object start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/QuestionScoreWindow.fxml"));
		Parent root = loader.load();
		QuestionScoreWindowController cont = loader.getController();
		Scene scene = new Scene(root);
		primaryStage.setOnCloseRequest((e) -> {
			cont.btnSaveScore.fire();
			e.consume();
		});
		primaryStage.setTitle("Add Score");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
		return cont;
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Add' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void addNoteActionButton(ActionEvent event) {
		AddNoteWindowController returned = null;
		AddNoteWindowController noteWindow = new AddNoteWindowController();
		try {
			returned = (AddNoteWindowController) noteWindow.start(new Stage());
			teachNote = returned.teachNote;
			studNote = returned.studNote;
		} catch (IOException e) {
		}
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Save' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void saveScoreActionButton(ActionEvent event) {
		// score is missing
		if (getScore().trim().isEmpty())
			lblErr.setText("enter score");
		// score was entered
		else {
			lblErr.setText("");
			if (!isValid())
				lblErr.setText("invalid score");
			else {
				score = Integer.parseInt(getScore());
				close(event);
			}
		}
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
	 * This method check if score is valid
	 */
	private boolean isValid() {
		String scoreString = getScore();
		try {
			int score = Integer.parseInt(scoreString);
			// check if score is valid number
			if (score < 1 || score > 100)
				return false;
		} catch (NumberFormatException e) {
			return false;
		}
		return true;

	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set images
		Image img = new Image(this.getClass().getResource("teacherFrame.PNG").toString());
		imgBack.setImage(img);
		// show the range that can be put
		if (WriteAnExamForm2Controller.sum == 0)
			lblRange.setText("(range: 1-100)");
		else if (WriteAnExamForm2Controller.sum == 99)
			lblRange.setText("(range: 1)");
		else
			lblRange.setText("(range: 1-" + (100-WriteAnExamForm2Controller.sum) + ")");
	}

}
//End of QuestionScoreWindowController class