package control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientUI;
import gui.Navigator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.Exam;
import logic.Message;
import logic.Question;

/**
 * This is controller class (boundary) for window WriteAnExam (second part) in
 * Teacher. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @version May 2021
 */

public class WriteAnExamForm2Controller implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private ListView<Question> quesList;
	@FXML
	private ListView<Question> chosenList;
	@FXML
	private Label lblError;
	@FXML
	private ImageView imgPencil;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'next' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void next(ActionEvent event) {
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

	// Menu methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void goHome(ActionEvent event) {
		Navigator.instance().clearHistory("TeacherHomeForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Write Question'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void writeQuestionAction(ActionEvent event) {
		Navigator.instance().navigate("WriteQuestionForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Write an Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void writeExamAction(ActionEvent event) {
		Navigator.instance().navigate("WriteAnExamForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Get Report'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void getReportAction(ActionEvent event) {
		Navigator.instance().navigate("TeacherReportForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Change Exam
	 * Duration' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void changeDurAction(ActionEvent event) {
		Navigator.instance().navigate("RequestChangeExamDurationTimeWindow");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Check Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void checkExamAction(ActionEvent event) {
		// Navigator.instance().navigate(" ");///????
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Exam Stock'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void examSearchAction(ActionEvent event) {
		Navigator.instance().navigate("ExamStockForm1");
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
		System.out.println(messageToServer);
		listOfQuestions = (ArrayList<Question>) ClientUI.client.handleMessageFromClientUI(messageToServer);
		quesList.setItems(FXCollections.observableArrayList(listOfQuestions));
		// handle the action of press on question in quesList
		quesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Question>() {
			@Override
			public void changed(ObservableValue<? extends Question> observable, Question oldValue, Question newValue) {
				// question chosen
				if (newValue != null) {
					quesList.getItems().remove(newValue);
					// add to hash map (in Exam object)
					chosenList.getItems().add(newValue);
					WriteAnExamForm1Controller.Exam.getQuestionsInExam().put(newValue, 0);
				}
			}
		});
		// handle the action of press on question in chosenList
		chosenList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Question>() {
			@Override
			public void changed(ObservableValue<? extends Question> observable, Question oldValue, Question newValue) {
				// question removed from exam
				if (newValue != null) {
					chosenList.getItems().remove(newValue);
					// delete from hash map (in Exam object)
					quesList.getItems().add(newValue);
					WriteAnExamForm1Controller.Exam.getQuestionsInExam().remove(newValue);
				}
			}
		});
		
	}
}
//End of WriteAnExamForm2Controller class