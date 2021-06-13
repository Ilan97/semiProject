package serverDB;

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

	User user;
	String userName, password;

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

	@Test
	void logInWrongUserNameOrPasswordTest() {
		Message msg = new Message();
		// wrong userName
		msg.setMsg("ilanA ilan1234");
		msg.setOperation("isUserExists");
		assertNull(UserController.handleRequest(msg).getMsg());
		// wrong password
		msg = new Message();
		msg.setMsg("ilanM ilan123");
		msg.setOperation("isUserExists");
		assertNull(UserController.handleRequest(msg).getMsg());
	}

	@Test
	void successfullStudentLogInTest() {
		Message msg = new Message();
		msg.setMsg("ilanM ilan1234");
		msg.setOperation("isUserExists");
		assertEquals(user, (User) (UserController.handleRequest(msg).getMsg()));
	}
}