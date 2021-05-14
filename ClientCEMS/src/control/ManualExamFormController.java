package control;

import java.net.URL;
import java.util.ResourceBundle;

import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * This is controller class (boundary) for window ManualExam in Student. This
 * class handle all events related to this window. This class connect with
 * client.
 *
 * @author
 * @version May 2021
 */

public class ManualExamFormController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private ImageView imgDoc1;
	@FXML
	private ImageView imgDown;
	@FXML
	private ImageView imgDoc2;
	@FXML
	private ImageView imgUp;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Enter Code'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void enterCodeAction(ActionEvent event) {
		ManualExamCodeWindowController popUp = new ManualExamCodeWindowController();
		try {
			popUp.start(new Stage());
		} catch (Exception e) {
			
		}
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Submit Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void submitAction(ActionEvent event) {

	}

	// Menu methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void goHome(ActionEvent event) {
		Navigator.instance().clearHistory("StudentHomeForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Computerized
	 * Exam' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void compExamAction(ActionEvent event) {
		/*//successes pop up
		ComputerizedExamCodeWindowController popUp = new ComputerizedExamCodeWindowController();
		try {
			popUp.start(new Stage());
		} catch (Exception e) {
			
		}*/
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Manual Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void manualExamAction(ActionEvent event) {
		Navigator.instance().navigate("ManualExamForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Grades' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void gradesAction(ActionEvent event) {
		Navigator.instance().navigate("GradesForm");
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image img = new Image(this.getClass().getResource("studentFrame.PNG").toString());
		imgBack.setImage(img);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("docPage.png").toString());
		imgDoc1.setImage(img3);
		Image img4 = new Image(this.getClass().getResource("download.PNG").toString());
		imgDown.setImage(img4);
		Image img5 = new Image(this.getClass().getResource("docPage.png").toString());
		imgDoc2.setImage(img5);
		Image img6 = new Image(this.getClass().getResource("upload.PNG").toString());
		imgUp.setImage(img6);
	}

}
// End of ManualExamFormController class
