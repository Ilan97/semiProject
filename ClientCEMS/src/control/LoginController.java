package control;

import java.awt.event.InputMethodEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ClientUI;
import gui.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Message;
import logic.User;
import logic.UserType;

/**
 * This is controller class (boundary) for window Login. This class handle all
 * events related to this windows. This class connect with client.
 *
 * @author
 * @version May 2021
 */

public class LoginController implements GuiController, Initializable {

	// Instance variables **********************************************
	public static User user = null;

	/**
	 * FXML variables.
	 */
	@FXML
	private ImageView imgLogin;
	@FXML
	private TextField txtUserName;
	@FXML
	private Button btnLogin;
	@FXML
	private PasswordField txtPassword; // maybe its better to change to textArea (?)
	@FXML
	private Label lblPassErr;
	@FXML
	private ImageView imgCopyRights;
	@FXML
	private ImageView imgLogo;
	@FXML
	private Label lblUserErr;

	// Instance methods ************************************************

	@FXML
	void inputPass(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) 
			btnLogin.fire();
	}

	// txtPassword.setOnKeyPressed(event -> if(event.getCode() == KeyCode.ENTER);

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
		// check that user put an id
		if (userName.trim().isEmpty()) {
			lblUserErr.setText("Please enter User name");
			if (password.trim().isEmpty())
				lblPassErr.setText("Please enter password");
			else
				lblPassErr.setText("");
		}
		// trim isn't empty
		else if (password.trim().isEmpty()) {
			lblUserErr.setText("");
			lblPassErr.setText("Please enter password");
		} else {
			lblPassErr.setText("");
			// create new Message object with the request
			messageToServer.setMsg(userName + " " + password);
			messageToServer.setOperation("isUserExists");
			messageToServer.setControllerName("UserController");
			user = (User) ClientUI.client.handleMessageFromClientUI(messageToServer);

		}
		// user isn't exists in DB
		if (user == null) {// create pop up alert
			Alert a = new Alert(AlertType.INFORMATION);
			a.setTitle("CEMS");
			a.setResizable(true);
			a.setHeaderText("User name or password is incorrect");
			Label label = new Label();
			label.setPrefSize(100, 100);
			BackgroundFill bgFill = new BackgroundFill(Color.RED, new CornerRadii(60), null);
			Background bg = new Background(bgFill);
			label.setPadding(new Insets(10, 10, 10, 10));
			label.setBackground(bg);
			a.setGraphic(label);
			a.showAndWait();
		}

		else if (user.isLogedIN() == true) {
			Alert a = new Alert(AlertType.INFORMATION);
			a.setTitle("CEMS");
			a.setResizable(true);
			a.setHeaderText("User name already logged in !");
			Label label = new Label();
			label.setPrefSize(100, 100);
			BackgroundFill bgFill = new BackgroundFill(Color.RED, new CornerRadii(20), null);
			Background bg = new Background(bgFill);
			label.setPadding(new Insets(10, 10, 10, 10));
			label.setBackground(bg);
			a.setGraphic(label);
			a.showAndWait();
		} else {// in case when user logged in successfully
				// * need to add - update to DB for connection status//

			System.out.println("before update");
			messageToServer.setMsg(userName);
			messageToServer.setOperation("updateConnectionStatus");
			messageToServer.setControllerName("UserController");
			Boolean temp = (Boolean) ClientUI.client.handleMessageFromClientUI(messageToServer);
			System.out.println("after update");

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
			}
		}

	}

	/**
	 * This method called to initialize a controller after its root element has been
	 * completely processed (after load method).
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Image img1 = new Image(this.getClass().getResource("loginForm.PNG").toString());
		imgLogin.setImage(img1);
		Image img2 = new Image(this.getClass().getResource("logo.png").toString());
		imgLogo.setImage(img2);
		Image img3 = new Image(this.getClass().getResource("copyright.png").toString());
		imgCopyRights.setImage(img3);
	}

	/**
	 * The client's first window and this window's first method. load and show this
	 * window.
	 *
	 * @param primaryStage The stage for window's scene.
	 */
	public void start(Stage primaryStage) throws Exception {
		Pane p = new Pane();
		Navigator.init(p);
		primaryStage.setScene(new Scene(p));
		Navigator.instance().navigate("LoginForm");
		primaryStage.setTitle("CEMS");
		// close the client
		primaryStage.setOnCloseRequest((event) -> {
			try {
				ClientUI.client.closeConnection();
			} catch (IOException ex) {
				System.out.println("Fail to close client!");
			}
			System.exit(0);
		});
		primaryStage.show();
	}
}

//End of LoginController class