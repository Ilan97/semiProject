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
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.Message;

/**
 * This is controller class (boundary) for window PrincipalReport (first part).
 * This class handle all events related to this window. This class connect with
 * client.
 *
 * @author Bat-El Gardin
 * @version May 2021
 */

public class PrincipalReportForm1Controller implements GuiController, Initializable {

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
	private ComboBox<String> reportType;
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
		if(formIsNotEmpty()) {   //check if any type of report selected
			lblErr.setText("");
			switch(reportType.getSelectionModel().getSelectedItem()) {
				case "Report By Student":
					Navigator.instance().navigate("PrincipalReportFormStudent");
					break;
				case "Report By Course":
					Navigator.instance().navigate("PrincipalReportFormCourse");
					break;
					
				case "Report By Teacher":
					Navigator.instance().navigate("PrincipalReportFormTeacher");
					break;
				}
			}
		else   //if not write error label
			lblErr.setText("Choose type first");
	}

	// Menu methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void goHome(ActionEvent event) {
		if (formIsNotEmpty())
			Navigator.instance().alertPopUp("PrincipalHomeForm");
		else
			Navigator.instance().navigate("PrincipalHomeForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Get Report'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void getReportAction(ActionEvent event) {
		if (formIsNotEmpty())
			Navigator.instance().alertPopUp("PrincipalReportForm1");
		else
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
		if (formIsNotEmpty())
			Navigator.instance().alertPopUp("PrincipalViewRequestForm");
		else
			Navigator.instance().navigate("PrincipalViewRequestForm");
	}
	
	/**
	 * This method check that there is no selected values in the form
	 *
	 * @return boolean result.
	 */
	private boolean formIsNotEmpty() {
		return !reportType.getSelectionModel().isEmpty();
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set images
		Image img = new Image(this.getClass().getResource("frame1PrincipalReport.PNG").toString());
		imgBack1.setImage(img);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("report.png").toString());
		imgRep.setImage(img3);
		
		ArrayList<String> reportTypes = new ArrayList<>();
		reportTypes.add("Report By Student");
		reportTypes.add("Report By Course");
		reportTypes.add("Report By Teacher");
		reportType.setItems(FXCollections.observableArrayList(reportTypes));
	}
}
//End of PrincipalReportForm1Controller class