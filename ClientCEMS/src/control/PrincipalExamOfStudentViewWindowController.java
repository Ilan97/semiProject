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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Exam;
import logic.Message;
import logic.Question;

/**
 * This is controller class (boundary) for window show exam of student for
 * principal. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author Sharon Vaknin
 * @version June 2021
 */

public class PrincipalExamOfStudentViewWindowController implements GuiController, Initializable {

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
	 * Array of controllers of type {@link ViewExamOfStudentInnerFormController}.
	 */
	private ViewExamOfStudentInnerFormController contArray[];

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private Pane paneQuestions;
	@FXML
	private Button btnBack;
	@FXML
	private Button btnNext;
	@FXML
	private TextArea txtNote;
	@FXML
	private Label lblNote;
	@FXML
	private Label lblGrade;
	@FXML
	private Label lblGradeNum;

	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @return the "real" controller.
	 * @throws IOException if an I/O error occurs when opening.
	 */
	public Object start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/PrincipalExamOfStudentViewWindow.fxml"));
		Parent root = loader.load();
		PrincipalExamOfStudentViewWindowController cont = loader.getController();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Exam View");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
		return cont;
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'back' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void back(ActionEvent event) {
		if (curQuestion == (qSize - 1)) {
			// last question
			btnNext.setVisible(true);
		}
		curQuestion--;
		if (curQuestion == 0) {
			// first question
			btnBack.setVisible(false);
			// show wanted variables
			if (!ViewGradesFormController.chosenExam.getTeachNote().equals("")) {
				lblNote.setVisible(true);
				txtNote.setVisible(true);
			} else {
				lblNote.setVisible(false);
				txtNote.setVisible(false);
			}
			lblGrade.setVisible(true);
			lblGradeNum.setVisible(true);
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
		// hide unwanted variables
		lblNote.setVisible(false);
		txtNote.setVisible(false);
		lblGrade.setVisible(false);
		lblGradeNum.setVisible(false);

		if (curQuestion == 0) {
			// first question
			btnBack.setVisible(true);
		}
		curQuestion++;
		if (curQuestion == (qSize - 1)) {
			// last question
			btnNext.setVisible(false);
		}
		paneQuestions.getChildren().clear();
		paneQuestions.getChildren().add(qArray[curQuestion]);
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Close' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void closeAction(ActionEvent event) {
		close(event);
	}

	/**
	 * This method close the current stage.
	 * 
	 * @param event The action event.
	 */
	private void close(ActionEvent event) {
		Node source = (Node) event.getSource();
		Stage stage = (Stage) source.getScene().getWindow();
		stage.close();
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		exam = null;
		// show wanted variables
		if (!ViewGradesFormController.chosenExam.getTeachNote().equals("")) {
			lblNote.setVisible(true);
			txtNote.setVisible(true);
		} else {
			lblNote.setVisible(false);
			txtNote.setVisible(false);
		}
		lblGrade.setVisible(true);
		lblGradeNum.setVisible(true);
		// set the teacher note
		txtNote.setText(ViewGradesFormController.chosenExam.getTeachNote());
		// set the student's grade
		lblGradeNum.setText(ViewGradesFormController.chosenExam.getGrade() + "");
		// set images
		Image img = new Image(this.getClass().getResource("principalFrame.PNG").toString());
		imgBack.setImage(img);
		Message messageToServer = new Message();
		messageToServer.setMsg(ViewGradesFormController.chosenExam.getCode() + " computerized");
		messageToServer.setOperation("FindExamOfStudent");
		messageToServer.setControllerName("ExamController");
		exam = (Exam) ClientUI.client.handleMessageFromClientUI(messageToServer);
		if (exam != null) {
			qSize = exam.getQuestionsInExam().size();
			qArray = new Pane[qSize];
			contArray = new ViewExamOfStudentInnerFormController[qSize];
			int cnt = 0;
			String[] answers = ViewGradesFormController.chosenExam.getAnswers().split("");
			for (Entry<Question, Integer> q : exam.getQuestionsInExam().entrySet()) {
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(Navigator.class.getResource("ViewExamOfStudentInnerForm.fxml"));
					qArray[cnt] = loader.load();
					contArray[cnt] = loader.getController();
					contArray[cnt].setQuestion(q.getKey(), cnt + 1, q.getValue(), answers[cnt]);
				} catch (IOException e) {

				}
				cnt++;
			}
			paneQuestions.getChildren().clear();
			paneQuestions.getChildren().add(qArray[0]);
			if (qSize == 1) {
				btnNext.setVisible(false);
			}

			btnBack.setVisible(false);
		}
	}
}
// End of PrincipalExamOfStudentViewWindowController class