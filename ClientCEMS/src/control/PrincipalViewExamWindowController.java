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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * This is controller class (boundary) for window PrincipalViewExam. This class
 * handle all events related to this window. This class connect with client.
 *
 * @author Sharon Vaknin
 * @author Moran Davidov
 * @version May 2021
 */

public class PrincipalViewExamWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	@FXML
	private TextArea txtExamView;
	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgExam;
	@FXML
	private Label lblExamID;
	@FXML
	private Label lblCourse;
	@FXML
	private Label lblField;
	@FXML
	private Label lblAuthor;
	@FXML
	private Label lblDuration;
	@FXML
	private Label lblType;
	@FXML
	private TextField txtCode;
	@FXML
	private DatePicker dPickDate;

	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @throws IOException
	 */
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/PrincipalViewExamWindow.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("View Exam");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
	}

	/**
	 * This method shows exam details in the window.
	 */
	private void showExamDetails() {
		lblExamID.setText(ViewExamsForm2Controller.chosenExam.getExamID());
		lblField.setText(ViewExamsForm2Controller.chosenExam.getFname());
		lblCourse.setText(ViewExamsForm2Controller.chosenExam.getCname());
		lblAuthor.setText(ViewExamsForm2Controller.chosenExam.getAuthor());
		lblDuration.setText(String.valueOf(ViewExamsForm2Controller.chosenExam.getDuration()) + " min");
		txtExamView.setText(ViewExamsForm2Controller.chosenExam.allQuestionsForTeacherToString());
		lblType.setText(ViewExamsForm2Controller.chosenExam.getEtype().toString());
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Close' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void closeAction(ActionEvent event) {
		UsefulMethods.instance().close(event);
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set images
		Image img = new Image(this.getClass().getResource("principalFrame.PNG").toString());
		imgBack.setImage(img);
		Image img1 = new Image(this.getClass().getResource("exam.png").toString());
		imgExam.setImage(img1);
		showExamDetails();
	}
}
//End of PrincipalViewExamWindowController class