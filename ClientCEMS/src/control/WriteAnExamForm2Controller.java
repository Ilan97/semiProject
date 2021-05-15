package control;

import java.net.URL;
import java.util.ResourceBundle;

import gui.Navigator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.Question;

/**
 * This is controller class (boundary) for window WriteAnExam (second part) in
 * Teacher. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author Moran Davidov
 * @author Bat-El Gardin
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
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set images
		Image img1 = new Image(this.getClass().getResource("frame2WriteAnExam.PNG").toString());
		imgBack.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("pencil.png").toString());
		imgPencil.setImage(img3);
		// set quesList (questions from DB) listView
		quesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Question>() {
			@Override
			public void changed(ObservableValue<? extends Question> observable, Question oldValue, Question newValue) {
				if (newValue != null) {
					quesList.getItems().remove(newValue);
					// add to test
					chosenList.getItems().add(newValue);
				}

			}
		});
		// set chosenList (chosen questions) listView
		chosenList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Question>() {
			@Override
			public void changed(ObservableValue<? extends Question> observable, Question oldValue, Question newValue) {
				if (newValue != null) {
					chosenList.getItems().remove(newValue);
					// delete from hash map
					quesList.getItems().add(newValue);
				}
			}
		});
		// Lambda expression
//		chosenList.getSelectionModel().selectedItemProperty().addListener((obs,o,n)->{
//			chosenList.getItems().remove(n);
//			//delete from hash map		
//			quesList.getItems().add(n);
//		});
	}
}
//End of WriteAnExamForm2Controller class