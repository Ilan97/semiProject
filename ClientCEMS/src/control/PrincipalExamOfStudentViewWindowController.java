package control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Map.Entry;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Exam;
import logic.ExamOfStudent;
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

	public static Exam exam;
	/**
	 * The details to upload the exam to DB.
	 */
	public static ExamOfStudent examToSubmit;
	/**
	 * private variables.
	 */
	public static int qSize;
	private int curQuestion = 0;
	/**
	 * To navigate between questions.
	 */
	private Pane qArray[];
	private ViewExamOfStudentInnerFormController contArray[];

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private Pane paneQuestions;
	@FXML
	private Label lblNumQuestion;
	@FXML
	private Label lblScore;
	@FXML
	private Label lblWrong;
	@FXML
	private Label lblCorrect;
	@FXML
	private Button btnBack;
	@FXML
	private Button btnNext;

	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @return the "real" controller.
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
			if (qSize == 1)
				btnNext.setVisible(false);

			btnBack.setVisible(false);
		}
	}
}
// End of PrincipalExamOfStudentViewWindowController class