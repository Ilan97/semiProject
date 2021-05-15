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
 * Teacher option. This class handle all events related to this window. This
 * class connect with client.
 *
 * @author Bat-El Gardin
 * @version May 2021
 */

public class PrincipalReportFormTeacherController implements GuiController, Initializable {

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
	private ComboBox<String> teacher;
	@FXML
	private Label lblErrStat;
	@FXML
	private Label lblErrTeacher;

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
	 * This is FXML event handler. Handles the action of click on 'Report by Course'
	 * option in 'Choose Report Type' menu button.
	 * 
	 * @param event The action event.
	 */
	@FXML
	void courseNext(ActionEvent event) {
		Navigator.instance().navigate("PrincipalReportFormCourse");
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
		// set the content in the comboBox 'teacher'
		ArrayList<String> listOfTeacher = null;
		Message messageToServer = new Message();
		messageToServer.setControllerName("UserController");
		messageToServer.setOperation("ShowAllTeachers");
		listOfTeacher = (ArrayList<String>) ClientUI.client.handleMessageFromClientUI(messageToServer);
		teacher.setItems(FXCollections.observableArrayList(listOfTeacher));
	}

}
// End of PrincipalReportFormTeacherController class
