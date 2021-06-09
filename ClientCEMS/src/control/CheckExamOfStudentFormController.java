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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Exam;
import logic.Message;
import logic.Question;

/**
 * This is controller class (boundary) for window CheckExamOfStudent in Teacher.
 * This class handle all events related to this window. This class connect with
 * client.
 *
 * @author Bat-El Gardin
 * @version June 2021
 */

public class CheckExamOfStudentFormController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * The exam of the student.
	 */
	public static Exam exam;
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
	 * Array of controllers of type {@link CheckExamOfStudentInnerFormController}.
	 */
	private CheckExamOfStudentInnerFormController contArray[];

	@FXML
	private Pane paneQuestions;
	@FXML
	private ImageView imgBack;
	@FXML
	private Button btnBack;
	@FXML
	private Button btnNext;
	@FXML
	private Button btnSave;
	@FXML
	private Label lblGrade;
	@FXML
	private TextField txtGrade;
	@FXML
	private Label lblStar;
	@FXML
	private TextArea txtNote;
	@FXML
	private Label lblNote;
	@FXML
	private Label lblErrGrade;

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @throws IOException
	 */
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/CheckExamOfStudentForm.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Check Exam");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'back' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void back(ActionEvent event) {
		// hide unwanted variables
		lblGrade.setVisible(false);
		lblStar.setVisible(false);
		txtGrade.setVisible(false);
		btnSave.setVisible(false);
		lblNote.setVisible(false);
		txtNote.setVisible(false);
		if (curQuestion == (qSize - 1)) {
			btnNext.setVisible(true);
		}
		curQuestion--;
		if (curQuestion == 0) {
			btnBack.setVisible(false);
		}
		paneQuestions.getChildren().clear();
		paneQuestions.getChildren().add(qArray[curQuestion]);
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
			// last question
			btnNext.setVisible(false);
			lblGrade.setVisible(true);
			lblStar.setVisible(true);
			txtGrade.setVisible(true);
			btnSave.setVisible(true);
			lblNote.setVisible(true);
			txtNote.setVisible(true);
		}
		paneQuestions.getChildren().clear();
		paneQuestions.getChildren().add(qArray[curQuestion]);
	}

	/**
	 * @return the score from window.
	 */
	private String getGrade() {
		return txtGrade.getText();
	}

	/**
	 * This method check if score is valid
	 * 
	 * @return true if valid, false otherwise.
	 */
	private boolean isValid() {
		String scoreString = getGrade();
		try {
			int grade = Integer.parseInt(scoreString);
			// check if score is valid number
			if (grade < 0 || grade > 100)
				return false;
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Save' button.
	 *
	 * @param event The action event.
	 * @throws InterruptedException
	 */
	@FXML
	void saveAction(ActionEvent event) throws InterruptedException {
		if (getGrade().trim().isEmpty())
			lblErrGrade.setText("enter grade");
		else if (!isValid())
			lblErrGrade.setText("invalid grade");
		// grade is valid
		else {
			CheckExamFormController.chosenExam.setGrade(Integer.parseInt(getGrade()));
			if (!(txtNote.getText().trim().isEmpty()))
				CheckExamFormController.chosenExam.setTeachNote(txtNote.getText());
			// update exam of student
			Message messageToServer = new Message();
			messageToServer.setMsg(CheckExamFormController.chosenExam);
			messageToServer.setOperation("UpdateExam");
			messageToServer.setControllerName("StudentController");
			boolean isUpdate = (boolean) ClientUI.client.handleMessageFromClientUI(messageToServer);
			if (isUpdate) {
				// successes pop up
				TheChangesHasUpdatedSuccessfullyController popUp = new TheChangesHasUpdatedSuccessfullyController();
				try {
					popUp.start(new Stage());
				} catch (Exception e) {
					UsefulMethods.instance().printException(e);
				}
			} else {
				// fail pop up
				FailWindowController popUp = new FailWindowController();
				try {
					popUp.start(new Stage());
				} catch (Exception e) {
					UsefulMethods.instance().printException(e);
				}
			}
			UsefulMethods.instance().close(event);
		}
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// hide unwanted variables
		lblGrade.setVisible(false);
		lblStar.setVisible(false);
		txtGrade.setVisible(false);
		btnBack.setVisible(false);
		btnSave.setVisible(false);
		lblNote.setVisible(false);
		txtNote.setVisible(false);
		// set the grade
		txtGrade.setText(CheckExamFormController.chosenExam.getGrade() + "");
		// set images
		Image img = new Image(this.getClass().getResource("teacherFrame.PNG").toString());
		imgBack.setImage(img);
		// get the exam of that student
		Message messageToServer = new Message();
		messageToServer.setMsg(CheckExamFormController.chosenExam.getCode() + " computerized");
		messageToServer.setOperation("FindExamOfStudent");
		messageToServer.setControllerName("ExamController");
		exam = (Exam) ClientUI.client.handleMessageFromClientUI(messageToServer);
		if (exam != null) {
			qSize = exam.getQuestionsInExam().size();
			qArray = new Pane[qSize];
			contArray = new CheckExamOfStudentInnerFormController[qSize];
			int cnt = 0;
			String[] answers = CheckExamFormController.chosenExam.getAnswers().split("");
			for (Entry<Question, Integer> q : exam.getQuestionsInExam().entrySet()) {
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(Navigator.class.getResource("CheckExamOfStudentInnerForm.fxml"));
					qArray[cnt] = loader.load();
					contArray[cnt] = loader.getController();
					contArray[cnt].setQuestion(q.getKey(), cnt + 1, q.getValue(), answers[cnt]);
				} catch (IOException e) {
					UsefulMethods.instance().printException(e);
				}
				cnt++;
			}
			paneQuestions.getChildren().clear();
			paneQuestions.getChildren().add(qArray[0]);
			if (qSize == 1) {
				btnNext.setVisible(false);
				lblGrade.setVisible(true);
				lblStar.setVisible(true);
				txtGrade.setVisible(true);
				btnSave.setVisible(true);
				lblNote.setVisible(true);
				txtNote.setVisible(true);
			}

			btnBack.setVisible(false);
		}
	}
}
//End of CheckExamOfStudentFormController class