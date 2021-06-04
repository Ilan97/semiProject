package logic;

import java.io.Serializable;

/**
 * This is an entity class that save details about each user that is created.
 * Implements Serializable to save the state of User object and re-create it as
 * needed.
 *
 * @author Moran Davidov
 * @version May 2021
 */

@SuppressWarnings("serial")
public class User implements Serializable {

	// Instance variables **********************************************

	private String username;
	private String upassword;
	private String firstName;
	private String lastName;
	private String uid;
	private String email;
	private boolean isLogedIN;
	private UserType userType;

	// Constructors ****************************************************

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public User() {

	}

	// Instance methods ************************************************

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username The username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the upassword
	 */
	public String getUpassword() {
		return upassword;
	}

	/**
	 * @param upassword The upassword to set
	 */
	public void setUpassword(String upassword) {
		this.upassword = upassword;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName The firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName The lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid The uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email The email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return if logged in or not
	 */
	public boolean isLogedIN() {
		return isLogedIN;
	}

	/**
	 * @param isLogedIN The isLogedIN to set
	 */
	public void setLogedIN(boolean isLogedIN) {
		this.isLogedIN = isLogedIN;
	}

	/**
	 * @return result The hashCode of User object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + (isLogedIN ? 1231 : 1237);
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		result = prime * result + ((upassword == null) ? 0 : upassword.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	/**
	 * @return true If object is this User, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (isLogedIN != other.isLogedIN)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		if (upassword == null) {
			if (other.upassword != null)
				return false;
		} else if (!upassword.equals(other.upassword))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
//End of User class