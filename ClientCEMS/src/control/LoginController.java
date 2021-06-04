package control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import client.ClientUI;
import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Message;
import logic.User;

/**
 * This is controller class (boundary) for window Login. This is the first
 * system's window. This class handle all events related to this windows. This
 * class connect with client.
 *
 * @author Bat-El Gardin
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @version May 2021
 */

public class LoginController implements GuiController, Initializable {

	// Instance variables **********************************************

	/**
	 * static instance for {@link User} object. Will be create only once for each
	 * run. the object initialize by the info that return from DB.
	 */
	public static User user;

	@FXML
	private ImageView imgLogin;
	@FXML
	private ImageView imgCopyRights;
	@FXML
	private ImageView imgLogo;
	@FXML
	private TextField txtUserName;
	@FXML
	private Button btnLogin;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private Label lblErr;

	// Instance methods ************************************************

	/**
	 * The client's first window and this window's first method. load and show this
	 * window. in each press on X button (in all primary windows), the user is
	 * logged out and return to 'log in' screen.
	 *
	 * @param primaryStage The stage for window's scene.
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		Pane p = new Pane();
		Navigator.init(p);
		primaryStage.setScene(new Scene(p));
		Navigator.instance().navigate("LoginForm");
		primaryStage.setTitle("CEMS");
		// when press on X button
		primaryStage.setOnCloseRequest((event) -> {
			// log out and return to 'log in' window
			if (user != null) {
				Message messageToServer = new Message();
				messageToServer.setMsg(user.getUsername());
				messageToServer.setOperation("updateConnectionStatus");
				messageToServer.setControllerName("UserController");
				ClientUI.client.handleMessageFromClientUI(messageToServer);
				user = null;
			}
			Navigator.instance().clearHistory();
			// marks this event as consumed. This stops its further propagation.
			event.consume();
		});
		primaryStage.show();
	}

	/**
	 * This is FXML event handler. Handles the action of press on enter key.
	 *
	 * @param event The action event.
	 */
	@FXML
	void inputPass(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER))
			btnLogin.fire();
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Log in' button.
	 *
	 * @param event The action event.
	 * @throws IOException
	 */
	@FXML
	void login(ActionEvent event) throws IOException {
		Message messageToServer = new Message();
		String userName = txtUserName.getText();
		String password = txtPassword.getText();
		// check that user put details
		if (userName.trim().isEmpty() || password.trim().isEmpty())
			lblErr.setText("User Name or Password is missing");
		else {
			lblErr.setText("");
			// create new Message object with the request
			messageToServer.setMsg(userName + " " + password);
			messageToServer.setOperation("isUserExists");
			messageToServer.setControllerName("UserController");
			// the result User instance
			user = (User) ClientUI.client.handleMessageFromClientUI(messageToServer);
			// user isn't exists in DB
			if (user == null) { // create pop up alert
				lblErr.setText("User Name or Password is incorrect");
			}
			// user already log in
			else if (user.isLogedIN() == true) { // create pop up alert
				lblErr.setText("User already logged in");
			} else { // user logged in successfully
				messageToServer.setMsg(userName);
				messageToServer.setOperation("updateConnectionStatus");
				messageToServer.setControllerName("UserController");
				// update the user's connection status
				ClientUI.client.handleMessageFromClientUI(messageToServer);
				// navigate user to the right home page, by his permission
				switch (user.getUserType()) {
				case STUDENT:
					Navigator.instance().navigate("StudentHomeForm");
					break;
				case TEACHER:
					Navigator.instance().navigate("TeacherHomeForm");
					break;
				case PRINCIPAL:
					Navigator.instance().navigate("PrincipalHomeForm");
					break;
				} // end switch case
			}
		}
	}

	/**
	 * This is FXML event handler. Handles the action of click on 'Exit' button.
	 *
	 * @param event The action event.
	 * @throws IOException
	 */
	@FXML
	void exit(ActionEvent event) throws IOException {
		// close the client
		try {
			ClientUI.client.closeConnection();
		} catch (IOException e) {
			UsefulMethods.instance().display("Fail to close client!");
			UsefulMethods.instance().printException(e);
		}
		System.exit(0);
	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set images
		Image img1 = new Image(this.getClass().getResource("loginForm.PNG").toString());
		imgLogin.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("copyright.png").toString());
		imgCopyRights.setImage(img3);
	}
}
//End of LoginController class