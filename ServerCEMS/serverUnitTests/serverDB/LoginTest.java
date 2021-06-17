package serverDB;

/**
 * This is unitTest class for the server side. These test are checking the functionality of login action.
 *
 * @author Ilan Meikler
 * @author Bat-El Gardin
 * @author Ohad Shamir
 * @version June 2021
 */

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import control.DBconnector;
import control.UserController;
import logic.Message;
import logic.User;
import logic.UserType;

class LoginTest {

	// Variables ************************************************

	/**
	 * This is the user that returned from the userController.
	 */
	User user;
	/**
	 * This is the userName we are testing.
	 */
	String userName;
	/**
	 * This is the user's password we are testing..
	 */
	String password;

	/**
	 * This class is set up the variables we are going to test.
	 */
	@BeforeEach
	void setUp() throws Exception {
		DBconnector.connectToDB();
		user = new User();
		user.setUsername("ilanM");
		user.setUpassword("ilan1234");
		user.setEmail("ilan.meikler@gmail.com");
		user.setFirstName("Ilan");
		user.setLastName("Meikler");
		user.setUid("316599430");
		user.setUserType(UserType.STUDENT);
		user.setLogedIN(false);
	}

	// Tests ************************************************

	// checking wrong userName that user puts in.
	// input: wrong userName.
	// expected: null.
	@Test
	void logInWrongUserNameTest() {
		Message msg = new Message();
		// wrong userName
		msg.setMsg("ilanA ilan1234");
		msg.setOperation("isUserExists");
		assertNull(UserController.handleRequest(msg).getMsg());
	}

	// checking wrong password that user puts in.
	// input: wrong password.
	// expected: null.
	@Test
	void logInWrongPasswordTest() {
		Message msg = new Message();
		// wrong password
		msg = new Message();
		msg.setMsg("ilanM ilan123");
		msg.setOperation("isUserExists");
		assertNull(UserController.handleRequest(msg).getMsg());
	}

	// checking successful student login.
	// input: existing user.
	// expected: object user that returns from the userController.
	@Test
	void successfullStudentLogInTest() {
		Message msg = new Message();
		msg.setMsg("ilanM ilan1234");
		msg.setOperation("isUserExists");
		assertEquals(user, (User) (UserController.handleRequest(msg).getMsg()));
	}
}
//End of LoginTest class