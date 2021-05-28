package control;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

import client.ClientUI;
import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.ExamOfStudent;
import logic.Message;

/**
 * This is controller class (boundary) for window ManualExam in Student. This
 * class handle all events related to this window. This class connect with
 * client.
 *
 * @author Ilan Meikler
 * @author Bat-El Gardin
 * @author Moran Davidov
 * @version May 2021
 */

public class ManualExamFormController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * The details to upload the exam to DB.
	 */
	public static ExamOfStudent examToUpload;

	/**
	 * The chosen file in bytes.
	 */
	public byte[] fileContent = {};

	/**
	 * The code that is entered.
	 */
	public String code = null;

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private ImageView imgDoc1;
	@FXML
	private ImageView imgDown;
	@FXML
	private ImageView imgDoc2;
	@FXML
	private ImageView imgUp;
	@FXML
	private Button btnSubmit;
	@FXML
	private Button btnUpload;
	@FXML
	private Button btnHome;
	@FXML
	private Button btnComp;
	@FXML
	private Button btnMan;
	@FXML
	private Button btnGrades;
	@FXML
	private Button btnCode;
	@FXML
	private Label lblFileName;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Enter Code'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void enterCodeAction(ActionEvent event) {
		ManualExamCodeWindowController res;
		ManualExamCodeWindowController popUp = new ManualExamCodeWindowController();
		try {
			res = (ManualExamCodeWindowController) popUp.start(new Stage());
			code = res.code;
			// no exam was download
			if (code == null)
				btnUpload.setDisable(true);
			else if (res.chooseDir) {
				// exam was download
				btnUpload.setDisable(false);
				btnHome.setDisable(true);
				btnComp.setDisable(true);
				btnMan.setDisable(true);
				btnGrades.setDisable(true);
				btnCode.setDisable(true);
			}
		} catch (Exception e) {

		}
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Upload' button.
	 *
	 * @param event The action event.
	 * @throws IOException
	 */
	@FXML
	void uploadAction(ActionEvent event) throws IOException {
		lblFileName.setText("");
		// open browser to search for the file to upload
		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		// choose file from browser
		File chosen = fileChooser.showOpenDialog(stage);
		stage.setOnCloseRequest((e) -> {
			stage.close();
		});
		if (chosen != null) {
			lblFileName.setText(chosen.getName());
			// the path of the chosen file
			Path path = chosen.toPath();
			fileContent = Files.readAllBytes(path);
			// calculate the difference between start and end time
			Message messageToServer = new Message();
			messageToServer.setControllerName("StudentController");
			messageToServer.setOperation("StopTimer");
			System.out.println(messageToServer);
			double difference = (double) ClientUI.client.handleMessageFromClientUI(messageToServer);
			examToUpload = new ExamOfStudent(fileContent, code, LoginController.user.getUsername(), difference);
			// file was chosen
			btnSubmit.setDisable(false);
		}

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Submit Exam'
	 * button.
	 *
	 * @param event The action event.
	 * @throws IOException
	 */
	@FXML
	void submitAction(ActionEvent event) throws IOException {
		SubmissionAgreementWindowController agreement = new SubmissionAgreementWindowController();
		agreement.start(new Stage(), "ManualExamCodeWindowController");
	}

	// Menu methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void goHome(ActionEvent event) {
		Navigator.instance().clearHistory("StudentHomeForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Computerized
	 * Exam' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void compExamAction(ActionEvent event) {
		// successes pop up
		ComputerizedExamCodeWindowController popUp = new ComputerizedExamCodeWindowController();
		try {
			popUp.start(new Stage());
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Manual Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void manualExamAction(ActionEvent event) {
		Navigator.instance().navigate("ManualExamForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Grades' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void gradesAction(ActionEvent event) {
		Navigator.instance().navigate("GradesForm");
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// initialize static variables
		examToUpload = null;
		// set images
		Image img = new Image(this.getClass().getResource("studentFrame.PNG").toString());
		imgBack.setImage(img);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("docPage.png").toString());
		imgDoc1.setImage(img3);
		Image img4 = new Image(this.getClass().getResource("download.PNG").toString());
		imgDown.setImage(img4);
		Image img5 = new Image(this.getClass().getResource("docPage.png").toString());
		imgDoc2.setImage(img5);
		Image img6 = new Image(this.getClass().getResource("upload.PNG").toString());
		imgUp.setImage(img6);
		// no file was chosen
		btnSubmit.setDisable(true);
		// no exam was download
		btnUpload.setDisable(true);
	}
}
// End of ManualExamFormController class