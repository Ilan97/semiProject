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
 * This is controller class (boundary) for window ViewExams (second part).
 * This class handle all events related to this window. This class connect with
 * client.
 *
 * @author Sharon Vaknin
 * @version June 2021
 */

public class ViewExamsForm2Controller  implements GuiController, Initializable {

	// Instance variables **********************************************
	
	public static Exam chosenExam;
	
    @FXML
    private ImageView imgBack;
    @FXML
    private ImageView imgLogo;
    @FXML
    private ImageView imgClock;
    @FXML
    private ListView<Exam> examsList;

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
	 * This is event handler. Handles the action of click on exam to view it.
	 *
	 * @param event - the action event.
	 */
    @FXML
    void viewExams(ActionEvent event) {
		if (examsList.getSelectionModel().isEmpty())
			return;
		chosenExam = examsList.getSelectionModel().getSelectedItem();
		PrincipalViewExamWindowController examView = new PrincipalViewExamWindowController();
		try {
			examView.start(new Stage());
		} catch (IOException e) {
			UsefulMethods.instance().printException(e);
		}
    }

	// Menu methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void goHome(ActionEvent event) {
		Navigator.instance().clearHistory("PrincipalHomeForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Get Report'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void getReportAction(ActionEvent event) {
		Navigator.instance().navigate("PrincipalReportForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'View Request'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void viewRequestsAction(ActionEvent event) {
		Navigator.instance().navigate("PrincipalViewRequestForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'View Questions'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void viewQuestionsAction(ActionEvent event) {
		Navigator.instance().navigate("ViewQuestionsForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'View Exams'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void viewExamsAction(ActionEvent event) {
		Navigator.instance().navigate("ViewExamsForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'View Grades'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void viewGradesAction(ActionEvent event) {
		Navigator.instance().navigate("ViewGradesForm");
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
		Image img1 = new Image(this.getClass().getResource("frame2PrincipalReport.PNG").toString());
		imgBack.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		// set the exams list in order to the field and course that was chosen
		ArrayList<Exam> listOfExams;
		Exam exam = ViewExamsForm1Controller.exam;
		Message messageToServer = new Message();
		messageToServer.setMsg(exam.getFname() + " " + exam.getCname());
		messageToServer.setControllerName("ExamController");
		messageToServer.setOperation("ShowExamList");
		listOfExams = (ArrayList<Exam>) ClientUI.client.handleMessageFromClientUI(messageToServer);
		examsList.setItems(FXCollections.observableArrayList(listOfExams));
	}
}
// End of ViewExamsForm2Controller class