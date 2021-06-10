package control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.ClientUI;
import gui.Navigator;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Message;
import logic.Request;

/**
 * This is controller class (boundary) for window PrincipalViewRequest. This
 * class handle all events related to this window. This class connect with
 * client.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @version June 2021
 */

public class PrincipalViewRequestFormController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * The {@link Request} that principal was chosen.
	 */
	public static Request chosenReq;
	/**
	 * The indication if the thread still alive.
	 */
	public boolean flag = true;

	@FXML
	private ImageView imgBack;
	@FXML
	private ImageView imgLogo;
	@FXML
	private ImageView imgClock;
	@FXML
	private ListView<Request> requestsList;
	@FXML
	private Button btnRefresh;

	// Instance methods ************************************************

	@SuppressWarnings("unchecked")
	/**
	 * This is FXML event handler. Handles the action of click on 'Refresh' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void refreshAction(ActionEvent event) {
		ArrayList<Request> listOfRequests;
		Message messageToServer = new Message();
		messageToServer.setControllerName("PrincipalController");
		messageToServer.setOperation("GetAllRequests");
		listOfRequests = (ArrayList<Request>) ClientUI.client.handleMessageFromClientUI(messageToServer);
		if (listOfRequests != null) {
			requestsList.setVisible(true);
			requestsList.setItems(FXCollections.observableArrayList(listOfRequests));
		} else
			requestsList.setVisible(false);
	}

	// Menu methods ************************************************

	/**
	 * This is FXML event handler. Handles the action of click on 'Home' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void goHome(ActionEvent event) {
		flag = false;
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
		flag = false;
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
		flag = false;
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
		flag = false;
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
		flag = false;
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
		flag = false;
		Navigator.instance().navigate("ViewGradesForm");
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		requestsList.setVisible(false);
		// refresh the requests list every minute
		new Thread(() -> {
			while (flag) {
				try {
					Thread.sleep(1000);
					Platform.runLater(() -> {
						btnRefresh.fire();
					});
				} catch (Exception e) {
					UsefulMethods.instance().printException(e);
				}
			}
		}).start();
		chosenReq = null;
		// set images
		Image img = new Image(this.getClass().getResource("principalFrame.PNG").toString());
		imgBack.setImage(img);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("clock.png").toString());
		imgClock.setImage(img3);
		// show the details of chosen request (clicked)
		requestsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Request>() {
			@Override
			public void changed(ObservableValue<? extends Request> observable, Request oldValue, Request newValue) {
				// request chosen
				if (newValue != null) {
					Platform.runLater(() -> {
						if (requestsList.getSelectionModel().isEmpty())
							return;
						else {
							chosenReq = requestsList.getSelectionModel().getSelectedItem();
							ApproveDurationWindowController approve = new ApproveDurationWindowController();
							try {
								approve.start(new Stage());
								requestsList.getSelectionModel().clearSelection();
							} catch (Exception e) {
								UsefulMethods.instance().printException(e);
							}
						}
					});
				}
			}
		});
	}
}
// End of PrincipalViewRequestFormController class