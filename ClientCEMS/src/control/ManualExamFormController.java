package control;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;
import client.ClientUI;
import gui.Navigator;
import javafx.application.Platform;
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

	static final int MIN = 60*1000;
	static final int SEC = 1000;
	/**
	 * The {@link ExamOfStudent} to upload to DB.
	 */
	public static ExamOfStudent examToUpload;
	public boolean flagForTimer = true;
	public static int SecTimer = 0, MinTimer = 0,HourTimer = 0;
	/**
	 * The chosen file in bytes.
	 */
	public byte[] fileContent = {};

	/**
	 * The code that is entered.
	 */
	public String code = null;

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
    @FXML
    private Label timerLabel;
    @FXML
    private Label titleOfTimer;
    @FXML
    private ImageView imgTimer;



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
				Image img = new Image(this.getClass().getResource("timer.png").toString());
				imgTimer.setImage(img);
				titleOfTimer.setText("Timer: ");
				new Thread(() -> { //Thread for Timer
					try {
						while (flagForTimer) {
							Thread.sleep(SEC);
							if (SecTimer == 59) {
								MinTimer += 1;
								SecTimer = 0;
							} else
								SecTimer += 1;
							if(MinTimer == 60) {
								HourTimer +=1;
								MinTimer = 0;
							}
							Platform.runLater(() -> {
								timerLabel.setText(String.format("%02d:%02d:%02d",HourTimer ,MinTimer, SecTimer));
							});

						}
					} catch (Exception e) {
						UsefulMethods.instance().printException(e);
					}
				}).start();
				
				//thread for Checks if exam time is over or changed.
				//At the end of the time closes the exam automatically
				new Thread(() -> { //Thread 
					try {
						
						double startDuration;
						double currDuration;
						String ExamStatus;
						String Exam_eCode = code;
						Message messageToServer = new Message();
						//request for startDuration
						messageToServer.setControllerName("ExamController");
						messageToServer.setOperation("GetExamDuration");
						messageToServer.setMsg(Exam_eCode);
						startDuration = (double) ClientUI.client.handleMessageFromClientUI(messageToServer);
						
						while (flagForTimer) {
					
							Thread.sleep(MIN);
							//request for currDuration
							messageToServer.setControllerName("ExamController");
							messageToServer.setOperation("GetExamDuration");
							messageToServer.setMsg(Exam_eCode);
							currDuration = (double) ClientUI.client.handleMessageFromClientUI(messageToServer);
							//check if A change was made during the exam
							if (currDuration != startDuration );
							//TODO POPUP PAY ATTENTION exam duration has changed ! 
							//request for eStatus of the exam
							messageToServer.setControllerName("ExamController");
							messageToServer.setOperation("GetExamStatus");
							messageToServer.setMsg(Exam_eCode);
							ExamStatus = (String) ClientUI.client.handleMessageFromClientUI(messageToServer);
					
							if( (HourTimer*60 + MinTimer) + 10 == currDuration) {
								Platform.runLater(() -> {
									// successes pop up
									AlertTimeIsRunningOutWindowController pop = new AlertTimeIsRunningOutWindowController();
									try {
										pop.start(new Stage());
									} catch (Exception e) {
										UsefulMethods.instance().printException(e);
									}
								});
							}
								
								//check if time is up or status is "locked"
							if( ((HourTimer*60 + MinTimer) >= currDuration || ExamStatus.equals("locked")) ) {
								Platform.runLater(() -> {
									examToUpload = new ExamOfStudent(null, code, LoginController.user.getUsername(), -1);
									btnSubmit.fire();
									messageToServer.setMsg(examToUpload);
									messageToServer.setControllerName("StudentController");
									messageToServer.setOperation("SubmitExam");
									ClientUI.client.handleMessageFromClientUI(messageToServer);
									StudentDidNotMakeItWindowController pop = new StudentDidNotMakeItWindowController();
									try {
										pop.start(new Stage());
									} catch (IOException e) {UsefulMethods.instance().printException(e);}
									
									Navigator.instance().clearHistory("StudentHomeForm");
								});		
								flagForTimer = false;
							}
						}
					} catch (Exception e) {
						UsefulMethods.instance().printException(e);
					}
				}).start();
				
			}
		} catch (Exception e) {
			UsefulMethods.instance().printException(e);
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
			double difference = (double) ClientUI.client.handleMessageFromClientUI(messageToServer);
			messageToServer.setControllerName("ExamController");
			messageToServer.setOperation("GetExamDuration");
			messageToServer.setMsg(code);
			double duration = (double) ClientUI.client.handleMessageFromClientUI(messageToServer);
			// in case the student did not submit the test on time
			if (difference > duration)
				difference = -1;
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
			UsefulMethods.instance().printException(e);
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