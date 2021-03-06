package logic;

import java.io.Serializable;

/**
 * This is an enum class that defines the type of user. Implements Serializable
 * to save the state of UserType object and re-create it as needed.
 *
 * @author Moran Davidov
 * @version May 2021
 */
public enum UserType implements Serializable {
	STUDENT, TEACHER, PRINCIPAL;
}
//End of UserType class