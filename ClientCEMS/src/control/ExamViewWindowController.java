package control;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ResourceBundle;
import client.ClientUI;
import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.Message;

/**
 * This is controller class (boundary) for window ExamView. This class handle
 * all events related to this window. This class connect with client.
 *
 * @author Sharon Vaknin
 * @author Moran Davidov
 * @version May 2021
 */

public class ExamViewWindowController implements GuiController, Initializable {

	// Instance variables **********************************************

	@FXML
	private TextArea txtExamView;
	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgExam;
	@FXML
	private Label lblExamID;
	@FXML
	private Label lblCourse;
	@FXML
	private Label lblField;
	@FXML
	private Label lblAuthor;
	@FXML
	private Label lblDuration;
    @FXML
    private Label lblType;
	@FXML
	private TextField txtCode;
	@FXML
	private DatePicker dPickDate;
	@FXML
	private Text lblErrCode;
	@FXML
	private Text lblErrDate;

	// Instance methods ************************************************

	/**
	 * Pop this window.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @throws IOException
	 */
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/ExamViewWindow.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Exam View");
		primaryStage.setScene(scene);
		primaryStage.showAndWait();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Update' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void updateAction(ActionEvent event) {
		int lettersNum = 0;
		int digitsNum = 0;
		int codeFlag = 0;
		int dateFlag = 0;
		// check code is valid
		if (txtCode.getText().trim().isEmpty())
			lblErrCode.setText("empty field");

		else if (txtCode.getText().trim().length() != 4)
			lblErrCode.setText("must include 4 characters");

		else if (txtCode.getText().trim().length() == 4) {
			lblErrCode.setText("");
			for (int i = 0; i < txtCode.getText().length(); i++) {
				char ch = txtCode.getText().charAt(i);
				char di = txtCode.getText().charAt(i);

				if (Character.isLetter(ch))
					lettersNum++;

				if (Character.isDigit(di))
					digitsNum++;
			}

			if (digitsNum == 0 || lettersNum == 0)
				lblErrCode.setText("invalid input");

			else if (digitsNum != 0 && lettersNum != 0) {
				Message messageToServer = new Message();
				messageToServer.setMsg(txtCode.getText().trim());
				messageToServer.setOperation("CheckExamCodeIsUnique");
				messageToServer.setControllerName("ExamController");
				boolean isValid = (boolean) ClientUI.client.handleMessageFromClientUI(messageToServer);
				if (isValid == true)
					lblErrCode.setText("code is already exists!");

				else {
					ExamStockForm2Controller.chosenExam.setEcode(txtCode.getText());
					codeFlag = 1;
				}
			}
		}

		// check date is valid
		if (dPickDate.getValue() == null)
			lblErrDate.setText("pick date");
		else {
			lblErrDate.setText("");
			ExamStockForm2Controller.chosenExam.setEdate(dPickDate.getValue());
			dateFlag = 1;
		}
		if (codeFlag == 1 && dateFlag == 1) {
			Message messageToServer = new Message();
			messageToServer.setMsg(ExamStockForm2Controller.chosenExam);
			messageToServer.setOperation("InsertExamToExamToPerformTable");
			messageToServer.setControllerName("ExamController");
			boolean isInsert = (boolean) ClientUI.client.handleMessageFromClientUI(messageToServer);

			if (isInsert == true) {
				// successes pop up
				ExamIsReadyToPerformWindowController popUp = new ExamIsReadyToPerformWindowController();
				try {
					popUp.start(new Stage());
				} catch (Exception e) {
					UsefulMethods.instance().printException(e);
				}
			}

			else {
				UsefulMethods.instance().display("error in update!");
			}
			closeAction(event);
			Navigator.instance().clearHistory("TeacherHomeForm");
		}
	}

	/**
	 * This method shows exam details in the window.
	 */
	private void showExamDetails() {
		lblExamID.setText(ExamStockForm2Controller.chosenExam.getExamID());
		lblField.setText(ExamStockForm2Controller.chosenExam.getFname());
		lblCourse.setText(ExamStockForm2Controller.chosenExam.getCname());
		lblAuthor.setText(ExamStockForm2Controller.chosenExam.getAuthor());
		lblDuration.setText(String.valueOf(ExamStockForm2Controller.chosenExam.getDuration()) + " min");
		txtExamView.setText(ExamStockForm2Controller.chosenExam.allQuestionsForTeacherToString());
		lblType.setText(ExamStockForm2Controller.chosenExam.getEtype().toString());
		dPickDate.setValue(LocalDate.now());
		Callback<DatePicker, DateCell> dayCellFactory = this.getDayCellFactory();
		dPickDate.setDayCellFactory(dayCellFactory);
	}

	/**
	 * This method Factory to create Cell of DatePicker.
	 * 
	 * @return dayCellFactory {@link Callback}.
	 */
	private Callback<DatePicker, DateCell> getDayCellFactory() {
		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						// disable Saturday and days before current day.
						if (item.getDayOfWeek() == DayOfWeek.SATURDAY || item.isBefore(java.time.LocalDate.now())) {
							setDisable(true);
							setStyle("-fx-background-color: #b2b2b2;");
						}
					}
				};
			}
		};
		return dayCellFactory;
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Close' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void closeAction(ActionEvent event) {
		UsefulMethods.instance().close(event);
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set images
		Image img = new Image(this.getClass().getResource("teacherFrame.PNG").toString());
		imgBack.setImage(img);
		Image img1 = new Image(this.getClass().getResource("exam.png").toString());
		imgExam.setImage(img1);
		showExamDetails();
	}
}
//End of ExanViewWindowController class