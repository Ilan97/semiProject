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
 * @author
 * @version May 2021
 */

public class PrincipalViewRequestFormController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * The {@link Request} that principal was chosen.
	 */
	public static Request chosenReq;

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
		requestsList.setItems(FXCollections.observableArrayList(listOfRequests));
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
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		btnRefresh.fire();
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