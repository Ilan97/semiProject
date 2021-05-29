package control;

import java.net.URL;
import java.util.ResourceBundle;

import client.ClientUI;
import gui.Navigator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Exam;
import logic.Message;

/**
 * This is controller class (boundary) for window WriteAnExam (third part) in
 * Teacher. This class handle all events related to this window. This class
 * connect with client.
 *
 * @author Bat-El Gardin
 * @author Ilan Meikler
 * @author Moran Davidov
 * @author Sharon Vaknin
 * @version May 2021
 */

public class WriteAnExamForm3Controller implements GuiController, Initializable {

	// Instance variables **********************************************

	private boolean result;
	private float progress;
	Object monitor = new Object();

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private ImageView imgPencil;
	@FXML
	private Label lblField;
	@FXML
	private Label lblCourse;
	@FXML
	private TextArea questions;
	@FXML
	private Label lblExamID;
	@FXML
	private Label lblDur;
	@FXML
	private Label lblType;
	@FXML
	private Label lblAuthor;
	@FXML
	private ProgressIndicator prog;

	// Inner class ************************************************

//	class MessageThread implements Runnable {
//
//		@Override
//		public void run() {
//			// create new message to the server
//			Message messageToServer = new Message();
//			messageToServer.setMsg(WriteAnExamForm1Controller.Exam);
//			messageToServer.setControllerName("ExamController");
//			messageToServer.setOperation("SaveExam");
//			result = (boolean) ClientUI.client.handleMessageFromClientUI(messageToServer);
//		}
//	}

	class ProgThread implements Runnable {

		@Override
		public void run() {
			synchronized (monitor) {
				for (float i = 0; i <= 1.1; i += 0.1) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					progress = i;
					
					Platform.runLater(() -> {
						System.out.println(progress);
						prog.setProgress(progress);
					});
				}
				monitor.notify();
			}
		}
	}

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
	 * This is FXML event handler. Handles the action of click on 'Save' button.
	 *
	 * @param event The action event.
	 * @throws InterruptedException
	 */
	@FXML
	void saveAction(ActionEvent event) throws InterruptedException {
		//prog.setVisible(true);
//		Thread pt = new Thread(new ProgThread());
//		pt.start();
//		Thread mt = new Thread(new ProgThread());
//		mt.start();
		Message messageToServer = new Message();
		messageToServer.setMsg(WriteAnExamForm1Controller.Exam);
		messageToServer.setControllerName("ExamController");
		messageToServer.setOperation("SaveExam");
		result = (boolean) ClientUI.client.handleMessageFromClientUI(messageToServer);
//		synchronized (monitor) {
//			while (progress < 1)
//				monitor.wait();
//		}

		if (result == true) {
			// successes pop up
			ExamWasCreatedSuccessfullyWindowController popUp = new ExamWasCreatedSuccessfullyWindowController();
			try {
				popUp.start(new Stage());
			} catch (Exception e) {
				System.out.println("Exception: " + e.getMessage());
				e.printStackTrace();
			}
		} else
			// pop up failed
			;
	}

	/**
	 * This method display the exam to the window.
	 */
	private void ShowExam() {
		Exam e = WriteAnExamForm1Controller.Exam;
		lblField.setText(e.getFname());
		lblCourse.setText(e.getCname());
		lblAuthor.setText(e.getAuthor());
		lblDur.setText(String.valueOf(e.getDuration()));
		// set the exam type
		switch (e.getEtype()) {
		case COMPUTERIZED:
			lblType.setText("Computerized");
			break;
		case MANUAL:
			lblType.setText("Manual");
			break;
		}
		// set the Exam ID
		String Cid = GetCid(e.getCname());
		if (!Cid.equals("Course not found"))
			e.setCid(Cid);
		String Fid = GetFid(e.getFname());
		if (!Fid.equals("Field not found"))
			e.setFid(Fid);
		int Eid = GetEid(e.getFname(), e.getCname());
		if (Eid != -1)
			e.setEid(String.format("%02d", Eid + 1));
		else
			display("qid not found");
		e.setExamID(Fid + Cid + String.format("%02d", Eid + 1));
		lblExamID.setText(e.getExamID());
		questions.setText(WriteAnExamForm1Controller.Exam.allQuestionsToString());
	}

	/**
	 * This method request from server to return the eid from DB.
	 *
	 * @param fieldName,courseName from client.
	 * @return return Eid if found in dataBase else return -1
	 */
	private int GetEid(String fieldName, String courseName) {
		int Eid;
		Message messageToServer = new Message();
		messageToServer.setMsg(fieldName + " " + courseName);
		messageToServer.setControllerName("ExamController");
		messageToServer.setOperation("GetEid");
		Eid = (int) ClientUI.client.handleMessageFromClientUI(messageToServer);
		return Eid;
	}

	/**
	 * This method request from server to return the fid from DB.
	 *
	 * @param fieldName from client.
	 * @return return Fid if Field found in dataBase else return "Field not found"
	 */
	private String GetFid(String FieldName) {
		String Fid;
		Message messageToServer = new Message();
		messageToServer.setMsg(FieldName);
		messageToServer.setControllerName("FieldOfStudyController");
		messageToServer.setOperation("GetFieldId");
		Fid = (String) ClientUI.client.handleMessageFromClientUI(messageToServer);
		if (Fid == null)
			return "Field not found";
		return Fid;
	}

	/**
	 * This method request from server to return the cid from DB.
	 *
	 * @param CourseName from client.
	 * @return return Cid if Course found in dataBase else return "Course not found"
	 */
	private String GetCid(String CourseName) {
		String Cid;
		Message messageToServer = new Message();
		messageToServer.setMsg(CourseName);
		messageToServer.setControllerName("CourseController");
		messageToServer.setOperation("GetCourseId");
		Cid = (String) ClientUI.client.handleMessageFromClientUI(messageToServer);
		if (Cid == null)
			return "Course not found";
		return Cid;
	}

	// Menu methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void goHome(ActionEvent event) {
		Navigator.instance().alertPopUp("TeacherHomeForm");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Write Question'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void writeQuestionAction(ActionEvent event) {
		Navigator.instance().alertPopUp("WriteQuestionForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Write an Exam'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void writeExamAction(ActionEvent event) {
		Navigator.instance().alertPopUp("WriteAnExamForm1");
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Get Report'
	 * button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void getReportAction(ActionEvent event) {
		Navigator.instance().alertPopUp("TeacherReportForm1");
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
		Navigator.instance().alertPopUp("ExamStockForm1");
	}

	/**
	 * This method displays a message into the console.
	 *
	 * @param message The string to be displayed.
	 */
	public static void display(String message) {
		System.out.println("> " + message);
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		prog.setVisible(false);
		prog.setProgress(0);
		// set images
		Image img1 = new Image(this.getClass().getResource("frame3WriteAnExam.PNG").toString());
		imgBack.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("pencil.png").toString());
		imgPencil.setImage(img3);
		// preview exam
		ShowExam();
	}
}
//End of WriteAnExamForm3Controller class