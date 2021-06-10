package control;

import java.io.IOException;
import java.net.URL;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import client.ClientUI;
import gui.Navigator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.ExamOfStudent;
import logic.Message;
import logic.Question;

/**
 * This is controller class (boundary) for window ComputerizedExam in Student.
 * This class handle all events related to this window. This class connect with
 * client.
 *
 * @author Bat-El Gardin
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @version May 2021
 */

public class ComputerizedExamFormController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * The time for the thread to refresh (one minute).
	 */
	static final int MIN = 60 * 1000;
	/**
	 * One second.
	 */
	static final int SEC = 1000;
	/**
	 * The {@link ExamOfStudent} to upload to DB.
	 */
	public static ExamOfStudent examToSubmit;
	/**
	 * The current duration of the exam (if was changed by principal).
	 */
	public static double currDuration;
	/**
	 * The origin duration of the exam.
	 */
	public static double startDuration;
	/**
	 * Variables for Timer
	 */
	public boolean flagForTimer = true;
	/**
	 * Variables for Timer
	 */
	public static int SecTimer = 0, MinTimer = 0, HourTimer = 0;
	/**
	 * The size of the qArray (number of questions in exam).
	 */
	public static int qSize;
	/**
	 * The number of the current question (1,2,....).
	 */
	private int curQuestion = 0;
	/**
	 * To navigate between questions.
	 */
	private Pane qArray[];
	/**
	 * Array of controllers of type {@link ComputerizedExamInnerFormController}.
	 */
	private ComputerizedExamInnerFormController contArray[];
	/**
	 * Pop up that asks the student if he sure he want to submit his exam.
	 */
	private SubmissionAgreementWindowController agreement = new SubmissionAgreementWindowController();
	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private Button btnHome;
	@FXML
	private Button btnComp;
	@FXML
	private Button btnMan;
	@FXML
	private Button btnGrades;
	@FXML
	private Button btnBack;
	@FXML
	private Button btnNext;
	@FXML
	private Button btnFinish;
	@FXML
	private Pane innerPane;
	@FXML
	private Label timerLabel;
	@FXML
	private ImageView imgTimer;

	// Instance methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'back' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void back(ActionEvent event) {
		if (curQuestion == (qSize - 1)) {
			btnNext.setVisible(true);
			btnFinish.setVisible(false);
		}
		curQuestion--;
		if (curQuestion == 0) {
			btnBack.setVisible(false);
		}
		innerPane.getChildren().clear();
		innerPane.getChildren().add(qArray[curQuestion]);
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'next' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void next(ActionEvent event) {
		if (curQuestion == 0) {
			btnBack.setVisible(true);
		}
		curQuestion++;
		if (curQuestion == (qSize - 1)) {
			btnNext.setVisible(false);
			btnFinish.setVisible(true);
		}
		innerPane.getChildren().clear();
		innerPane.getChildren().add(qArray[curQuestion]);
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Finish' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void finishAction(ActionEvent event) {
		String ans = "";
		int score = 0;
		// get the final answers and calculate the score.
		for (ComputerizedExamInnerFormController cont : contArray) {
			int studentAns = cont.getAnswerOfStudent();
			ans += studentAns;
			if (studentAns == 0)
				score += cont.getScore();
		}
		// calculate the difference between start and end time
		Message messageToServer = new Message();
		messageToServer.setControllerName("StudentController");
		messageToServer.setOperation("StopTimer");
		double difference = (double) ClientUI.client.handleMessageFromClientUI(messageToServer);
		messageToServer.setControllerName("ExamController");
		messageToServer.setOperation("GetExamDuration");
		messageToServer.setMsg(ComputerizedExamCodeWindowController.code);
		double duration = (double) ClientUI.client.handleMessageFromClientUI(messageToServer);
		// in case the student did not submit the test on time
		if (difference > duration) {
			difference = -1;
			examToSubmit = new ExamOfStudent(ComputerizedExamCodeWindowController.code,
					LoginController.user.getUsername(), difference, 0, null);
		} else
			examToSubmit = new ExamOfStudent(ComputerizedExamCodeWindowController.code,
					LoginController.user.getUsername(), difference, score, ans);

		flagForTimer = false;// end of thread Timer
		// agreement pop up
		try {
			agreement.start(new Stage(), "ComputerizedExamInnerFormController");
		} catch (IOException e) {
			UsefulMethods.instance().printException(e);
		}

	}

	/**
	 * This method end exam of student if he didn't make it on time
	 */
	void forcedFinishActionTimeOut() {
		Message messageToServer = new Message();
		examToSubmit = new ExamOfStudent(ComputerizedExamCodeWindowController.code, LoginController.user.getUsername(),
				-1, 0, null);
		try {
			messageToServer.setMsg(ComputerizedExamFormController.examToSubmit);
			messageToServer.setControllerName("StudentController");
			messageToServer.setOperation("SubmitExam");
			ClientUI.client.handleMessageFromClientUI(messageToServer);
			StudentDidNotMakeItWindowController pop = new StudentDidNotMakeItWindowController();
			pop.start(new Stage());
		} catch (IOException e) {
			UsefulMethods.instance().printException(e);
		}
	}

	/**
	 * This method end exam of student if the exam has locked
	 */
	void forcedFinishActionCouseLocked() {
		Message messageToServer = new Message();
		examToSubmit = new ExamOfStudent(ComputerizedExamCodeWindowController.code, LoginController.user.getUsername(),
				-1, 0, null);
		try {
			messageToServer.setMsg(ComputerizedExamFormController.examToSubmit);
			messageToServer.setControllerName("StudentController");
			messageToServer.setOperation("SubmitExam");
			ClientUI.client.handleMessageFromClientUI(messageToServer);
			AlertExamLockedWindowController popUp = new AlertExamLockedWindowController();
			popUp.start(new Stage());
		} catch (Exception e) {
			UsefulMethods.instance().printException(e);
		}
	}

	/**
	 * This method called to update timer for client as on server
	 * 
	 * @param eCode of the exam
	 */
	public void updateCurrTimerForClient(String ecode) {
		int temp;
		Message messageToServer = new Message();
		messageToServer.setControllerName("ExamController");
		messageToServer.setOperation("CheckTimeOfExam");
		messageToServer.setMsg(ecode);
		synchronized (this) {
			temp = (int) ClientUI.client.handleMessageFromClientUI(messageToServer);
		}
		MinTimer = temp;
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set images
		Image img = new Image(this.getClass().getResource("studentFrame.PNG").toString());
		imgBack.setImage(img);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("timer.png").toString());
		imgTimer.setImage(img3);
		// buttons not available
		btnHome.setDisable(true);
		btnComp.setDisable(true);
		btnMan.setDisable(true);
		btnGrades.setDisable(true);
		btnBack.setVisible(false);
		btnFinish.setVisible(false);
		// set the controllers array
		qSize = ComputerizedExamCodeWindowController.compExam.getQuestionsInExam().size();
		if (qSize == 1) {
			btnFinish.setVisible(true);
			btnNext.setVisible(false);
		}
		qArray = new Pane[qSize];
		contArray = new ComputerizedExamInnerFormController[qSize];
		int cnt = 0;
		for (Entry<Question, Integer> q : ComputerizedExamCodeWindowController.compExam.getQuestionsInExam()
				.entrySet()) {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Navigator.class.getResource("ComputerizedExamInnerForm.fxml"));
				qArray[cnt] = loader.load();
				contArray[cnt] = loader.getController();
				contArray[cnt].setQuestion(q.getKey(), cnt + 1, q.getValue());
			} catch (IOException e) {
				UsefulMethods.instance().printException(e);
			}
			cnt++;
		}
		innerPane.getChildren().clear();
		innerPane.getChildren().add(qArray[0]);
		// update the countPerformers in data base to +1
		Message messageToServer = new Message();
		messageToServer.setMsg(ComputerizedExamCodeWindowController.code);
		messageToServer.setOperation("increaseCounter");
		messageToServer.setControllerName("ExamController");
		synchronized (this) {
			ClientUI.client.handleMessageFromClientUI(messageToServer);
		}
		// get curr timer from server
		updateCurrTimerForClient(ComputerizedExamCodeWindowController.code);

		new Thread(() -> {
			try {
				while (flagForTimer) {
					Thread.sleep(SEC);
					if (SecTimer == 59) {
						MinTimer += 1;
						SecTimer = 0;
					} else
						SecTimer += 1;
					if (MinTimer == 60) {
						HourTimer += 1;
						MinTimer = 0;
					}
					Platform.runLater(() -> {
						timerLabel.setText(String.format("%02d:%02d:%02d", HourTimer, MinTimer, SecTimer));
					});

				}
			} catch (Exception e) {
				UsefulMethods.instance().printException(e);
			}
		}).start();

		// Thread for catch delay in time
		new Thread(() -> {
			boolean flag = true;
			Message messageToServerThread2 = new Message();
			messageToServerThread2.setControllerName("ExamController");
			messageToServerThread2.setOperation("CheckTimeOfExam");
			messageToServerThread2.setMsg(ComputerizedExamCodeWindowController.code);
			while (flag) {
				try {
					Thread.sleep(SEC);
					synchronized (this) {
						int min = (int) ClientUI.client.handleMessageFromClientUI(messageToServerThread2);

						if (min != MinTimer) {
							MinTimer = min;
							SecTimer = 0;
							flag = false;
							// break;
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		// Checks if exam time is over or changed.
		// At the end of the time closes the exam automatically
		new Thread(() -> {
			try {
				String ExamStatus;
				String Exam_eCode = ComputerizedExamCodeWindowController.code;
				Message messageToServerThread3 = new Message();
				// request for startDuration
				messageToServerThread3.setControllerName("ExamController");
				messageToServerThread3.setOperation("GetExamDuration");
				messageToServerThread3.setMsg(ComputerizedExamCodeWindowController.code);
				synchronized (this) {
					startDuration = (double) ClientUI.client.handleMessageFromClientUI(messageToServerThread3);
				}
				while (flagForTimer) {
					Thread.sleep(MIN);
					// request for currDuration
					messageToServerThread3.setControllerName("ExamController");
					messageToServerThread3.setOperation("GetExamDuration");
					messageToServerThread3.setMsg(Exam_eCode);
					synchronized (this) {
						currDuration = (double) ClientUI.client.handleMessageFromClientUI(messageToServerThread3);
					}
					// check if A change was made during the exam
					if (currDuration != startDuration) {
						// pop up
						Platform.runLater(() -> {
							AlertExamDurationChangedWindowController popUp = new AlertExamDurationChangedWindowController();
							try {
								popUp.start(new Stage());
							} catch (Exception e) {
								UsefulMethods.instance().printException(e);
							}
						});
					}
					if ((HourTimer * 60 + MinTimer) + 10 == currDuration) {
						// pop up
						Platform.runLater(() -> {
							AlertTimeIsRunningOutWindowController popUp = new AlertTimeIsRunningOutWindowController();
							try {
								popUp.start(new Stage());
							} catch (Exception e) {
								UsefulMethods.instance().printException(e);
							}
						});
					}
					// request for eStatus of the exam
					messageToServerThread3.setControllerName("ExamController");
					messageToServerThread3.setOperation("GetExamStatus");
					messageToServerThread3.setMsg(Exam_eCode);
					synchronized (this) {
						ExamStatus = (String) ClientUI.client.handleMessageFromClientUI(messageToServerThread3);
					}
					// check if time is up or status is "locked"
					if ((HourTimer * 60 + MinTimer) >= currDuration || ExamStatus.equals("locked")) {
						if (ExamStatus.equals("locked")) {
							flagForTimer = false; // end of thread Timer
							Platform.runLater(() -> {
								forcedFinishActionCouseLocked();
								Navigator.instance().clearHistory("StudentHomeForm");
							});
						} else {
							flagForTimer = false; // end of thread Timer
							Platform.runLater(() -> {
								forcedFinishActionTimeOut();
								Navigator.instance().clearHistory("StudentHomeForm");
							});
						}
					}
					startDuration = currDuration;
				}
			} catch (Exception e) {
				UsefulMethods.instance().printException(e);
			}
		}).start();
	}
}
//End of ComputerizedExamFormController class