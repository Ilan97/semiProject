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
import javafx.scene.control.CheckBox;
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
 * @author Sharon Vaknin
 * @author Ilan Meikler
 * @author Moran Davidov
 * @version June 2021
 */

public class PrincipalReportForm1Controller implements GuiController, Initializable {

	// Instance variables **********************************************

	@FXML
	private ImageView imgBack1;
	@FXML
	private ImageView imgRep;
	@FXML
	private ImageView imgLogo;
	@FXML
	private CheckBox clickAvg;
	@FXML
	private CheckBox clickMed;
	@FXML
	private CheckBox clickHist;
	@FXML
	private Label lblStar;
	@FXML
	private Label lblStatistics;
	@FXML
	private Label lblStar2;
	@FXML
	private Label lblErrStat;
	@FXML
	private Label lblErrOption;
	@FXML
	private Label lblErrType;
	@FXML
	private Label lblErrData;
	@FXML
	private ComboBox<String> typeOptions;
	@FXML
	private ComboBox<String> chooseType;

	// Instance methods ************************************************

	/**
	 * This method clear error label.
	 */
	private void clearErrLbl(Label err) {
		err.setText("");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'next' button.
	 *
	 * @param event The action event.
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void next(ActionEvent event) {
		clearErrLbl(lblErrData);
		// handle missing fields
		if (chooseType.getSelectionModel().isEmpty()) {
			if (chooseType.getSelectionModel().isEmpty())
				lblErrType.setText("choose type");
			// type chosen
			else
				clearErrLbl(lblErrType);
		} else {

			if (typeOptions.getSelectionModel().isEmpty()
					|| (!clickAvg.isSelected() && !clickMed.isSelected() && !clickHist.isSelected())) {
				// type not chosen
				if (chooseType.getSelectionModel().isEmpty())
					lblErrType.setText("choose type");
				// type chosen
				else
					clearErrLbl(lblErrType);

				// option not chosen
				if (typeOptions.getSelectionModel().isEmpty()) {
					switch (chooseType.getSelectionModel().getSelectedItem()) {
					case "Teacher":
						lblErrOption.setText("choose teacher");
						break;

					case "Student":
						lblErrOption.setText("choose student");
						break;

					case "Course":
						lblErrOption.setText("choose course");
						break;
					}
				}
				// option chosen
				else
					clearErrLbl(lblErrOption);

				// no one of the statistics was chosen
				if (!clickAvg.isSelected() && !clickMed.isSelected() && !clickHist.isSelected())
					lblErrStat.setText("choose statistics");
				// at least one statistics chosen
				else
					clearErrLbl(lblErrStat);
				// all fields are chosen
			} else {
				Message messageToServer = new Message();
				ArrayList<Integer> list = new ArrayList<Integer>();
				// clear all labels
				clearErrLbl(lblErrType);
				clearErrLbl(lblErrOption);
				clearErrLbl(lblErrStat);
				// by the chosen type, set the message
				switch (chooseType.getSelectionModel().getSelectedItem()) {
				case "Teacher":
					messageToServer.setMsg(typeOptions.getSelectionModel().getSelectedItem()); // teacher's name
					messageToServer.setControllerName("TeacherController");
					messageToServer.setOperation("GetGradeList");
					list = (ArrayList<Integer>) ClientUI.client.handleMessageFromClientUI(messageToServer);
					break;

				case "Student":
					messageToServer.setMsg(typeOptions.getSelectionModel().getSelectedItem()); // student's name
					messageToServer.setControllerName("StudentController");
					messageToServer.setOperation("GetGradeList");
					list = (ArrayList<Integer>) ClientUI.client.handleMessageFromClientUI(messageToServer);
					break;

				case "Course":
					messageToServer.setMsg(typeOptions.getSelectionModel().getSelectedItem()); // course's name
					messageToServer.setControllerName("CourseController");
					messageToServer.setOperation("GetGradeList");
					list = (ArrayList<Integer>) ClientUI.client.handleMessageFromClientUI(messageToServer);

					break;
				}
				if (list != null) {
					PrincipalReportForm2Controller cont = (PrincipalReportForm2Controller) Navigator.instance()
							.navigate("PrincipalReportForm2");
					// set the report
					cont.setReport(list, clickHist.isSelected(), clickAvg.isSelected(), clickMed.isSelected());
				} else
					lblErrData.setText("there is no data to show!");
			}
		}
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Choose Type'
	 * comboBox.
	 * 
	 * @param event The action event.
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void chooseTypeAction(ActionEvent event) {
		Message messageToServer = new Message();
		ArrayList<String> listOfOptions = null;
		lblStar.setVisible(true);
		typeOptions.setVisible(true);
		// by the chosen type, show the options
		switch (chooseType.getSelectionModel().getSelectedItem()) {
		case "Teacher":
			typeOptions.setPromptText("Choose Teacher");
			messageToServer.setControllerName("UserController");
			messageToServer.setOperation("ShowAllTeachers");
			listOfOptions = (ArrayList<String>) ClientUI.client.handleMessageFromClientUI(messageToServer);
			break;

		case "Student":
			typeOptions.setPromptText("Choose Student");
			messageToServer.setControllerName("UserController");
			messageToServer.setOperation("ShowAllStudents");
			listOfOptions = (ArrayList<String>) ClientUI.client.handleMessageFromClientUI(messageToServer);
			break;

		case "Course":
			typeOptions.setPromptText("Choose Course");
			messageToServer.setControllerName("CourseController");
			messageToServer.setOperation("ShowAllCourses");
			listOfOptions = (ArrayList<String>) ClientUI.client.handleMessageFromClientUI(messageToServer);

			break;
		}
		typeOptions.setItems(FXCollections.observableArrayList(listOfOptions));
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Choose Option'
	 * comboBox.
	 * 
	 * @param event The action event.
	 */
	@FXML
	void chooseOption(ActionEvent event) {
		lblStatistics.setVisible(true);
		lblStar2.setVisible(true);
		clickAvg.setVisible(true);
		clickMed.setVisible(true);
		clickHist.setVisible(true);
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
		// hide unwanted variables
		lblStatistics.setVisible(false);
		lblStar2.setVisible(false);
		clickAvg.setVisible(false);
		clickMed.setVisible(false);
		clickHist.setVisible(false);
		lblStar.setVisible(false);
		typeOptions.setVisible(false);
		// set images
		Image img = new Image(this.getClass().getResource("frame1PrincipalReport.PNG").toString());
		imgBack1.setImage(img);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("report.png").toString());
		imgRep.setImage(img3);
		// set the content in the comboBox 'choose type'
		ArrayList<String> listOfTypes = new ArrayList<>();
		listOfTypes.add("Teacher");
		listOfTypes.add("Student");
		listOfTypes.add("Course");
		chooseType.setItems(FXCollections.observableArrayList(listOfTypes));
	}
}
// End of PrincipalReportFormCourseController class