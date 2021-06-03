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
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Exam;
import logic.Message;


/**
 * This is controller class (boundary) for window ExamStock (second part). This
 * class handle all events related to this window. This class connect with
 * client.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Moran Davidov
 * @version May 2021
 */

public class ExamStockForm2Controller implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * static variables.
	 */
	public static Exam chosenExam;
	
	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private ListView<Exam> examsView;

	// Instance methods ************************************************

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
	 * This is FXML event handler. Handles the action of click on 'Close' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void closeAction(ActionEvent event) {
		Navigator.instance().clearHistory("TeacherHomeForm");
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
		// Navigator.instance().navigate(" ");/// ????
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
	 * This is event handler. Handles the action of click on exam to view it
	 *
	 * @param event - the action event.
	 */
	@FXML
	void viewAction(ActionEvent event) {
		if (examsView.getSelectionModel().isEmpty())
			return;
		chosenExam = examsView.getSelectionModel().getSelectedItem();
		ExamViewWindowController examView = new ExamViewWindowController();
		try {
			examView.start(new Stage());
		} catch (IOException e) {}
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		chosenExam = null;
		// set images
		Image img1 = new Image(this.getClass().getResource("frameWriteQuestion2.PNG").toString());
		imgBack.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		// set the exams list in order to the field and course that was chosen
		ArrayList<Exam> listOfExams;
		Exam exam = ExamStockForm1Controller.Exam;
		Message messageToServer = new Message();
		messageToServer.setMsg(exam.getFname() + " " + exam.getCname());
		messageToServer.setControllerName("ExamController");
		messageToServer.setOperation("ShowExamList");
		System.out.println(messageToServer);
		listOfExams = (ArrayList<Exam>) ClientUI.client.handleMessageFromClientUI(messageToServer);
		examsView.setItems(FXCollections.observableArrayList(listOfExams));
	}
}
// End of ExamStockForm2Controller class
