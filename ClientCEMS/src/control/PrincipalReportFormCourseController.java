package control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientUI;
import gui.Navigator;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.Message;

/**
 * This is controller class (boundary) for window PrincipalReport (first part)
 * Course option. This class handle all events related to this window. This
 * class connect with client.
 *
 * @author Bat-El Gardin
 * @version May 2021
 */

public class PrincipalReportFormCourseController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack1;
	@FXML
	private ImageView imgRep;
	@FXML
	private ImageView imgLogo;
	@FXML
	private ComboBox<String> course;
	@FXML
	private Label lblErrStat;
	@FXML
	private Label lblErrCourse;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'next' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void next(ActionEvent event) {
		Navigator.instance().navigate("PrincipalReportForm2");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Report by
	 * Student' option in 'Choose Report Type' menu button.
	 * 
	 * @param event The action event.
	 */
	@FXML
	void studentNext(ActionEvent event) {
		Navigator.instance().navigate("PrincipalReportFormStudent");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Report by
	 * Teacher' option in 'Choose Report Type' menu button.
	 * 
	 * @param event The action event.
	 */
	@FXML
	void teacherNext(ActionEvent event) {
		Navigator.instance().navigate("PrincipalReportFormTeacher");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Average' check
	 * box.
	 *
	 * @param event The action event.
	 */
	@FXML
	void chooseAvgAction(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Median' check
	 * box.
	 *
	 * @param event The action event.
	 */
	@FXML
	void chooseMedAction(ActionEvent event) {

	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Histogram
	 * Distribution' check box.
	 *
	 * @param event The action event.
	 */
	@FXML
	void chooseHistAction(ActionEvent event) {

	}

	// Menu methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void goHome(ActionEvent event) {
		Navigator.instance().alertPopUp("PrincipalHomeForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Get Report'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void getReportAction(ActionEvent event) {
		Navigator.instance().alertPopUp("PrincipalReportForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'View Request'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void viewRequestsAction(ActionEvent event) {
		Navigator.instance().alertPopUp("PrincipalViewRequestForm");
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set images
		Image img = new Image(this.getClass().getResource("frame1PrincipalReport.PNG").toString());
		imgBack1.setImage(img);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("report.png").toString());
		imgRep.setImage(img3);
		// set the content in the comboBox 'course'
		ArrayList<String> listOfCourse = null;
		Message messageToServer = new Message();
		messageToServer.setControllerName("CourseController");
		messageToServer.setOperation("ShowAllCourses");
		listOfCourse = (ArrayList<String>) ClientUI.client.handleMessageFromClientUI(messageToServer);
		course.setItems(FXCollections.observableArrayList(listOfCourse));
	}

}
// End of PrincipalReportFormCourseController class