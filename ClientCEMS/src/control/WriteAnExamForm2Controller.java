package control;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.ClientUI;
import gui.Navigator;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.Exam;
import logic.Message;
import logic.Question;

/**
 * This is controller class (boundary) for window WriteAnExam (second part) in
 * Teacher. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author Bat-El Gardin
 * @author Ilan Meikler
 * @author Moran Davidov
 * @author Sharon Vaknin
 * @author Ohad Shamir
 * @version May 2021
 */

public class WriteAnExamForm2Controller implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * This variable help us to check that the total score in this exam is exactly
	 * 100
	 */
	public static int sum;

	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private ImageView imgPencil;
	@FXML
	private ListView<Question> quesList;
	@FXML
	private ListView<Question> chosenList;
	@FXML
	private Label lblErr;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'next' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void next(ActionEvent event) {
		if (sum < 100)
			lblErr.setText(("total score is under 100!"));
		else
			Navigator.instance().navigate("WriteAnExamForm3");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'back' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void back(ActionEvent event) {
		Navigator.instance().back();
	}

	/**
	 * This is FXML event handler. Handles the action of click on the button to add
	 * questions to exam.
	 *
	 * @param event The action event.
	 */
	@FXML
	void add(MouseEvent event) {
		QuestionScoreWindowController returned = null;
		if (quesList.getSelectionModel().isEmpty())
			return;
		Question newValue = quesList.getSelectionModel().getSelectedItem();
		quesList.getSelectionModel().clearSelection();
		QuestionScoreWindowController scoreWindow = new QuestionScoreWindowController();
		try {
			returned = (QuestionScoreWindowController) scoreWindow.start(new Stage());
		} catch (IOException e) {
			UsefulMethods.instance().printException(e);
		}
		// check if score is over 100
		sum += returned.score;
		if (sum > 100) {
			lblErr.setText("total score is over 100!");
			sum -= returned.score;
		}
		// user entered on "X"
		else if (returned.score == 0)
			return;
		else {
			lblErr.setText("");
			quesList.getItems().remove(newValue);
			chosenList.getItems().add(newValue);
			// add the question to the exam
			newValue.setTeacherNote(returned.teachNote);
			newValue.setStudentNote(returned.studNote);
			WriteAnExamForm1Controller.Exam.getQuestionsInExam().put(newValue, returned.score);
			if (sum == 100) {
				lblErr.setText("total score is 100!");
				quesList.setDisable(true);
			}
		}
	}

	/**
	 * This is FXML event handler. Handles the action of click on the button to
	 * remove questions from exam.
	 *
	 * @param event The action event.
	 */
	@FXML
	void remove(MouseEvent event) {
		if (chosenList.getSelectionModel().isEmpty())
			return;
		lblErr.setText("");
		quesList.setDisable(false);
		Question newValue = chosenList.getSelectionModel().getSelectedItem();
		chosenList.getSelectionModel().clearSelection();
		chosenList.getItems().remove(newValue);
		// delete from hash map (in Exam object)
		quesList.getItems().add(newValue);
		sum -= WriteAnExamForm1Controller.Exam.getQuestionsInExam().get(newValue);
		WriteAnExamForm1Controller.Exam.getQuestionsInExam().remove(newValue);
	}

	// Menu methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void goHome(ActionEvent event) {
		Navigator.instance().alertPopUp("TeacherHomeForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Write Question'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void writeQuestionAction(ActionEvent event) {
		Navigator.instance().alertPopUp("WriteQuestionForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Write an Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void writeExamAction(ActionEvent event) {
		Navigator.instance().alertPopUp("WriteAnExamForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Get Report'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void getReportAction(ActionEvent event) {
		Navigator.instance().alertPopUp("TeacherReportForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Check Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void checkExamAction(ActionEvent event) {
		/* TODO Navigator.instance().navigate("checkExamForm"); */
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Exam Stock'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void examSearchAction(ActionEvent event) {
		Navigator.instance().alertPopUp("ExamStockForm1");
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sum = 0;
		// set images
		Image img1 = new Image(this.getClass().getResource("frame2WriteAnExam.PNG").toString());
		imgBack.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("pencil.png").toString());
		imgPencil.setImage(img3);
		// set the questions list in order to the field and course that was chosen
		ArrayList<Question> listOfQuestions;
		Exam exam = WriteAnExamForm1Controller.Exam;
		Message messageToServer = new Message();
		messageToServer.setMsg(exam.getFname() + " " + exam.getCname());
		messageToServer.setControllerName("QuestionController");
		messageToServer.setOperation("ShowQuestionList");
		listOfQuestions = (ArrayList<Question>) ClientUI.client.handleMessageFromClientUI(messageToServer);
		quesList.setItems(FXCollections.observableArrayList(listOfQuestions));
	}
}
//End of WriteAnExamForm2Controller class