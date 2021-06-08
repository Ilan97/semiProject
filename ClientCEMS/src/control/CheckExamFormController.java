package control;

import java.net.URL;
import java.util.ResourceBundle;

import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This is controller class (boundary) for window CheckExam in Student.
 * This class handle all events related to this window. This class connect with
 * client.
 *
 * @author Sharon Vaknin
 * @version May 2021
 */

public class CheckExamFormController implements GuiController, Initializable {
    @FXML
    private ImageView imgBack;

    @FXML
    private ImageView imgLogo;

//	/**
//	 * This method check that there is no selected values in the form.
//	 *
//	 * @return true if form isn't empty, false otherwise.
//	 */
//	private boolean formIsNotEmpty() {
//		if (!field.getSelectionModel().isEmpty() || !questionCon.getText().trim().isEmpty()
//				|| !instructions.getText().trim().isEmpty() || !rightAns.getText().trim().isEmpty()
//				|| !wrongAns1.getText().trim().isEmpty() || !wrongAns2.getText().trim().isEmpty()
//				|| !wrongAns3.getText().trim().isEmpty())
//			return true;
//		return false;
//	}
//
//	// Menu methods ************************************************
//
//	/**
//	 * This is FXML event handler. Handles the action of click on 'Home' button.
//	 *
//	 * @param event The action event.
//	 */
//	@FXML
//	void goHome(ActionEvent event) {
//		if (formIsNotEmpty())
//			Navigator.instance().alertPopUp("TeacherHomeForm");
//		else
//			Navigator.instance().navigate("TeacherHomeForm");
//	}
//
//	/**
//	 * This is FXML event handler. Handles the action of click on 'Write Question'.
//	 * button.
//	 *
//	 * @param event The action event.
//	 */
//	@FXML
//	void writeQuestionAction(ActionEvent event) {
//		if (formIsNotEmpty())
//			Navigator.instance().alertPopUp("WriteQuestionForm1");
//		else
//			Navigator.instance().navigate("WriteQuestionForm1");
//	}
//
//	/**
//	 * This is FXML event handler. Handles the action of click on 'Write an Exam'
//	 * button.
//	 *
//	 * @param event The action event.
//	 */
//	@FXML
//	void writeExamAction(ActionEvent event) {
//		if (formIsNotEmpty())
//			Navigator.instance().alertPopUp("WriteAnExamForm1");
//		else
//			Navigator.instance().navigate("WriteAnExamForm1");
//	}
//
//	/**
//	 * This is FXML event handler. Handles the action of click on 'Get Report'
//	 * button.
//	 *
//	 * @param event The action event.
//	 */
//	@FXML
//	void getReportAction(ActionEvent event) {
//		if (formIsNotEmpty())
//			Navigator.instance().alertPopUp("TeacherReportForm1");
//		else
//			Navigator.instance().navigate("TeacherReportForm1");
//	}
//
//	/**
//	 * This is FXML event handler. Handles the action of click on 'Check Exam'
//	 * button.
//	 *
//	 * @param event The action event.
//	 */
//	@FXML
//	void checkExamAction(ActionEvent event) {
//		/* TODO Navigator.instance().navigate("checkExamForm"); */
//	}
//
//	/**
//	 * This is FXML event handler. Handles the action of click on 'Exam Stock'
//	 * button.
//	 *
//	 * @param event The action event.
//	 */
//	@FXML
//	void examSearchAction(ActionEvent event) {
//		if (formIsNotEmpty())
//			Navigator.instance().alertPopUp("ExamStockForm1");
//		else
//			Navigator.instance().navigate("ExamStockForm1");
//	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set images
		Image img = new Image(this.getClass().getResource("teacherFrame.PNG").toString());
		imgBack.setImage(img);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
	}
}
//End of checkExamFormController class