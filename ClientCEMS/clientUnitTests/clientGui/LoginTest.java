package clientGui;

/**
 * This is unitTest class for the client side. These test are checking the functionality of login action.
 *
 * @author Ilan Meikler
 * @author Bat-El Gardin
 * @author Ohad Shamir
 * @version June 2021
 */

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import client.ClientUI;
import client.IClientController;
import control.GuiController;
import control.LoginController;
import gui.Navigator;
import gui.NavigatorInterface;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import logic.Message;
import logic.User;
import logic.UserType;

class LoginTest {

	// Variables ************************************************

	/**
	 * This variable is declared for JavaFX to run.
	 */
	@SuppressWarnings("unused")
	private JFXPanel panel = new JFXPanel();
	/**
	 * The user we are testing.
	 */
	private User user;
	/**
	 * The message that returned from clientController.
	 */
	private Object objToRet;

	// Private classes ************************************************

	/**
	 * This is stub class to the ClientController class. This class handles the
	 * messages that send to the client from the screen.
	 */
	private class ClientControllerStub implements IClientController {

		@Override
		public Object handleMessageFromClientUI(Message msg) {
			if (msg.getOperation().equals("isUserExists"))
				return objToRet;
			return null;
		}

	}

	/**
	 * The screen we want to navigate to.
	 */
	String navigatedScreen = "";

	/**
	 * This is stub class to the Navigator class. This class is navigate between
	 * different pages.
	 */
	private class StubNavigator implements NavigatorInterface {

		@Override
		public GuiController navigate(String destenation) {
			navigatedScreen = destenation;
			return null;
		}

		@Override
		public void back() {

		}

		@Override
		public void clearHistory() {
		}

		@Override
		public void clearHistory(String fxml) {

		}

		@Override
		public void alertPopUp(String string) {

		}

		@Override
		public void next() {

		}

	}

	/**
	 * This is the controller we are testing.
	 */
	LoginController lc = new LoginController();

	/**
	 * This class is set up the variables we are going to test.
	 */
	@BeforeEach
	void setUp() throws Exception {
		// the error label in the window
		lc.lblErr = new Label();
		// set up the user we are testing
		user = new User();
		user.setUsername("ilanM");
		user.setUpassword("ilan1234");
		user.setLogedIN(false);
		lc.txtPassword = new PasswordField();
		lc.txtPassword.setText(user.getUpassword());
		lc.txtUserName = new TextField(user.getUsername());
		// stub classes
		ClientUI.client = new ClientControllerStub();
		Navigator.setNavigator(new StubNavigator());
	}

	// Tests ************************************************

	// checking wrong userName or password that user puts in.
	// input: null.
	// expected: error label: "User Name or Password is incorrect".
	@Test
	void logInWrongUserNameOrPasswordTest() {
		objToRet = null;
		try {
			lc.login(null);
			assertEquals("User Name or Password is incorrect", lc.lblErr.getText());
		} catch (IOException e) {
			fail();
		}
	}

	// checking login with empty fields.
	// input: empty fields.
	// expected: error label: "User Name or Password is missing".
	@Test
	void logInEmptyUserNameTest() {
		lc.txtUserName.setText("");
		try {
			lc.login(null);
			assertEquals("User Name or Password is missing", lc.lblErr.getText());
		} catch (IOException e) {
			fail();
		}
	}

	// checking user that is already logged in.
	// input: user that is logged in.
	// expected: error label: "User already logged in".
	@Test
	void AlreadylogedInTest() {
		objToRet = user;
		user.setLogedIN(true);
		try {
			lc.login(null);
			assertEquals("User already logged in", lc.lblErr.getText());
		} catch (IOException e) {
			fail();
		}
	}

	// checking successful student login.
	// input: student user.
	// expected: navigate to "StudentHomeForm" screen.
	@Test
	void successfullStudentLogInTest() {
		objToRet = user;
		user.setLogedIN(false);
		user.setUserType(UserType.STUDENT);
		try {
			lc.login(null);
			assertEquals("", lc.lblErr.getText());
			assertEquals("StudentHomeForm", navigatedScreen);
		} catch (IOException e) {
			fail();
		}
	}

	// checking successful teacher login.
	// input: teacher user.
	// expected: navigate to "TeacherHomeForm" screen.
	@Test
	void successfullTeacherLogInTest() {
		objToRet = user;
		user.setLogedIN(false);
		user.setUserType(UserType.TEACHER);
		try {
			lc.login(null);
			assertEquals("", lc.lblErr.getText());
			assertEquals("TeacherHomeForm", navigatedScreen);
		} catch (IOException e) {
			fail();
		}
	}

	// checking successful principal login.
	// input: principal user.
	// expected: navigate to "PrincipalHomeForm" screen.
	@Test
	void successfullPrincipalLogInTest() {
		objToRet = user;
		user.setLogedIN(false);
		user.setUserType(UserType.PRINCIPAL);
		try {
			lc.login(null);
			assertEquals("", lc.lblErr.getText());
			assertEquals("PrincipalHomeForm", navigatedScreen);
		} catch (IOException e) {
			fail();
		}
	}
}
//End of LoginTest class