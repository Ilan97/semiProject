package control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.Question;

/**
 * This is controller class (boundary) for window ViewQuestions (second part).
 * This class handle all events related to this window. This class connect with
 * client.
 *
 * @author Sharon Vaknin
 * @version June 2021
 */

public class ViewQuestionsForm2Controller implements GuiController, Initializable {

	// Instance variables **********************************************

	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private TextArea txtQuestions;
	@FXML
	private ImageView imgClock;

	// Instance methods ************************************************

	/**
	 * This is methods create a String of all questions.
	 *
	 * @param questions - a list of all questions of this course and field.
	 * @return String of all questions
	 */
	public String showQuestions(ArrayList<Question> questions) {
		String QuestionView = "";
		for (Question q : questions) {
			QuestionView += q.getAuthor() + "\n";
			QuestionView += q.getContent() + "\n";
			QuestionView += q.getInstructions() + "\n";
			QuestionView += "1. " + q.getRightAnswer() + " (right answer)" + "\n";
			QuestionView += "2. " + q.getWrongAnswer1() + "\n";
			QuestionView += "3. " + q.getWrongAnswer2() + "\n";
			QuestionView += "4. " + q.getWrongAnswer3() + "\n";
			QuestionView += "__________________________";
			QuestionView += "\n\n";
		}
		return QuestionView;
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
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set images
		Image img = new Image(this.getClass().getResource("frame2PrincipalReport.PNG").toString());
		imgBack.setImage(img);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		txtQuestions.setText(showQuestions(ViewQuestionsForm1Controller.listOfQuestions));
	}
}
//End of ViewQuestionsForm2Controller class