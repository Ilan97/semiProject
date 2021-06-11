package clientUnitTests;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.ClientUI;
import client.IClientController;
import control.GuiController;
import control.LoginController;
import control.PrincipalReportForm1Controller;
import gui.Navigator;
import gui.NavigatorInterface;
import javafx.collections.FXCollections;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import logic.Message;
import logic.User;
import logic.UserType;



class UnitTests {
	private JFXPanel panel = new JFXPanel();//for javafx to run
	private User user;
	 
	private Object objToRet,objToRet2;
private class ClientControllerStudentStub implements IClientController{

	@Override
	public Object handleMessageFromClientUI(Message msg) {
		if( msg.getOperation().equals("isUserExists"))
			return objToRet;
		return null;
	}
	
}

private class ClientControllerPrincipalStub implements IClientController{

	@Override
	public Object handleMessageFromClientUI(Message msg) {
		if( msg.getOperation().equals("GetGradeList"))
			return objToRet2;
		return null;
	}
	
}

 String navigatedScreen = "";
private class StubNavigator implements NavigatorInterface{

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
	PrincipalReportForm1Controller lc2 = new PrincipalReportForm1Controller();
	
	@BeforeEach
	void setUp() throws Exception {
		lc.lblErr = new Label();
		
	    user= new User();
		user.setUsername("ilanM");   
		user.setUpassword("ilan1234"); 
		user.setLogedIN(false);	
		lc.txtPassword = new PasswordField();
		lc.txtPassword.setText(user.getUpassword());
		lc.txtUserName = new TextField(user.getUsername());
		ClientUI.client = new ClientControllerStudentStub();
		Navigator.setNavigator(new StubNavigator());
		
		
		
		lc2.chooseType  = new ComboBox<String>();
		ArrayList<String> listOfTypes = new ArrayList<>();
		listOfTypes.add("Teacher");
		listOfTypes.add("Student");
		listOfTypes.add("Course");
		lc2.chooseType.setItems(FXCollections.observableArrayList(listOfTypes));
		lc2.typeOptions = new ComboBox<String>();
		
		
	}
	
	@Test
	void logInWrongUserNameOrPasswordtest() {
		objToRet = null;
		try {
			lc.login(null);
			assertEquals("User Name or Password is incorrect", lc.lblErr.getText());
		} catch (IOException e) {
			fail();
		}
	}
	
	@Test
	void logInEmptyUserNametest() {
		lc.txtUserName.setText("");
		try {
			lc.login(null);
			assertEquals("User Name or Password is missing", lc.lblErr.getText());
		} catch (IOException e) {
			fail();
		}
	}
	
	@Test
	void AlreadylogedIntest() {
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
	void successfullStudentLogIntest() {
		objToRet = user;
		user.setLogedIN(false);
		user.setUserType(UserType.STUDENT);
		try {
			lc.login(null);
			assertEquals("", lc.lblErr.getText());
			assertEquals("StudentHomeForm",navigatedScreen);
		} catch (IOException e) {
			fail();
		}
	}
	
	@Test
	void test() {
		objToRet2 = user;
		lc2.chooseType.getSelectionModel().clearSelection();
		
	//	lc2.chooseType.SelectedIndex = lc2.chooseType.FindString("");

		lc2.next(null);
		assertEquals("", lc.lblErr.getText());
		assertEquals("StudentHomeForm",navigatedScreen);
	}
}
