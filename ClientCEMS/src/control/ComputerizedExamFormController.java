package control;

import java.io.IOException;
import java.net.URL;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import client.ClientUI;
import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.ExamOfStudent;
import logic.Message;
import logic.Question;

/**
 * This is controller class (boundary) for window ComputerizedExam in Student.
 * This class handle all events related to this window. This class connect with
 * client.
 *
 * @author Bat-El Gardin
 * @author Ilan Meikler
 * @version May 2021
 */

public class ComputerizedExamFormController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * The {@link ExamOfStudent} to upload to DB.
	 */
	public static ExamOfStudent examToSubmit;
	/**
	 * The size of the qArray (number of questions in exam).
	 */
	public static int qSize;
	/**
	 * The number of the current question (1,2,....).
	 */
	private int curQuestion = 0;
	/**
	 * To navigate between questions.
	 */
	private Pane qArray[];
	/**
	 * Array of controllers of type {@link ComputerizedExamInnerFormController}.
	 */
	private ComputerizedExamInnerFormController contArray[];

	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private Button btnHome;
	@FXML
	private Button btnComp;
	@FXML
	private Button btnMan;
	@FXML
	private Button btnGrades;
	@FXML
	private Button btnBack;
	@FXML
	private Button btnNext;
	@FXML
	private Button btnFinish;
	@FXML
	private Pane innerPane;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'back' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void back(ActionEvent event) {
		if (curQuestion == (qSize - 1)) {
			btnNext.setVisible(true);
			btnFinish.setVisible(false);
		}
		curQuestion--;
		if (curQuestion == 0) {
			btnBack.setVisible(false);
		}
		innerPane.getChildren().clear();
		innerPane.getChildren().add(qArray[curQuestion]);
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'next' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void next(ActionEvent event) {
		if (curQuestion == 0) {
			btnBack.setVisible(true);
		}
		curQuestion++;
		if (curQuestion == (qSize - 1)) {
			btnNext.setVisible(false);
			btnFinish.setVisible(true);
		}
		innerPane.getChildren().clear();
		innerPane.getChildren().add(qArray[curQuestion]);
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Finish' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void finishAction(ActionEvent event) {
		String ans = "";
		int score = 0;
		// get the final answers and calculate the score.
		for (ComputerizedExamInnerFormController cont : contArray) {
			int studentAns = cont.getAnswerOfStudent();
			ans += studentAns;
			if (studentAns == 0)
				score += cont.getScore();
		}
		// calculate the difference between start and end time
		Message messageToServer = new Message();
		messageToServer.setControllerName("StudentController");
		messageToServer.setOperation("StopTimer");
		double difference = (double) ClientUI.client.handleMessageFromClientUI(messageToServer);
		messageToServer.setControllerName("ExamController");
		messageToServer.setOperation("GetExamDuration");
		messageToServer.setMsg(ComputerizedExamCodeWindowController.code);
		double duration = (double) ClientUI.client.handleMessageFromClientUI(messageToServer);
		// in case the student did not submit the test on time
		if (difference > duration)
			difference = -1;
		examToSubmit = new ExamOfStudent(ComputerizedExamCodeWindowController.code, LoginController.user.getUsername(),
				difference, score, ans);
		// agreement pop up
		SubmissionAgreementWindowController agreement = new SubmissionAgreementWindowController();
		try {
			agreement.start(new Stage(), "ComputerizedExamInnerFormController");
		} catch (IOException e) {
			UsefulMethods.instance().printExeption(e);
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
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		// buttons not available
		btnHome.setDisable(true);
		btnComp.setDisable(true);
		btnMan.setDisable(true);
		btnGrades.setDisable(true);
		btnBack.setVisible(false);
		btnFinish.setVisible(false);
		// set the controllers array
		qSize = ComputerizedExamCodeWindowController.compExam.getQuestionsInExam().size();
		if (qSize == 1) {
			btnFinish.setVisible(true);
			btnNext.setVisible(false);
		}
		qArray = new Pane[qSize];
		contArray = new ComputerizedExamInnerFormController[qSize];
		int cnt = 0;
		for (Entry<Question, Integer> q : ComputerizedExamCodeWindowController.compExam.getQuestionsInExam()
				.entrySet()) {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Navigator.class.getResource("ComputerizedExamInnerForm.fxml"));
				qArray[cnt] = loader.load();
				contArray[cnt] = loader.getController();
				contArray[cnt].setQuestion(q.getKey(), cnt + 1, q.getValue());
			} catch (IOException e) {
				UsefulMethods.instance().printExeption(e);
			}
			cnt++;
		}
		innerPane.getChildren().clear();
		innerPane.getChildren().add(qArray[0]);
	}
}
//End of ComputerizedExamFormController class