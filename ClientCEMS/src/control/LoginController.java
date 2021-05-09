package control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This is controller class (boundary) for window Login. This class handle all
 * events related to this windows. This class connect with client.
 *
 * @author
 * @version May 2021
 */

public class LoginController implements GuiController, Initializable {

	// Instance variables **********************************************

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

	/**
	 * This is FXML event handler. Handles the action of click on 'Log in' button.
	 *
	 * @param event The action event.
	 */
	@FXML
	void login(ActionEvent event) {

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
}
//End of LoginController class