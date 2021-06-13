package clientGui;

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
	@SuppressWarnings("unused")
	private JFXPanel panel = new JFXPanel();// for javafx to run
	private User user;
	private Object objToRet;

	private class ClientControllerStudentStub implements IClientController {

		@Override
		public Object handleMessageFromClientUI(Message msg) {
			if (msg.getOperation().equals("isUserExists"))
				return objToRet;
			return null;
		}

	}

	String navigatedScreen = "";

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

	LoginController lc = new LoginController();

	@BeforeEach
	void setUp() throws Exception {
		lc.lblErr = new Label();

		user = new User();
		user.setUsername("ilanM");
		user.setUpassword("ilan1234");
		user.setLogedIN(false);
		lc.txtPassword = new PasswordField();
		lc.txtPassword.setText(user.getUpassword());
		lc.txtUserName = new TextField(user.getUsername());
		ClientUI.client = new ClientControllerStudentStub();
		Navigator.setNavigator(new StubNavigator());
	}

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