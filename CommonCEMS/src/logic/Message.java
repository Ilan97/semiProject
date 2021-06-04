package logic;

import java.io.Serializable;

/**
 * This is an entity class that save details about each Message that
 * send/received between client-server-controller. Implements Serializable to
 * save the state of Message object and re-create it as needed.
 *
 * @author Bat-El Gardin
 * @version May 2021
 */

@SuppressWarnings("serial")
public class Message implements Serializable {

	// Instance variables **********************************************

	private Object msg;
	private String operation;
	private String controllerName;

	// Constructors ****************************************************

	public Message() {
	}

	// Instance methods ************************************************

	/**
	 * @return the msg
	 */
	public Object getMsg() {
		return msg;
	}

	/**
	 * @param msg The msg to set
	 */
	public void setMsg(Object msg) {
		this.msg = msg;
	}

	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation The operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * @return the controllerName
	 */
	public String getControllerName() {
		return controllerName;
	}

	/**
	 * @param operation The operation to set
	 */
	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}

	@Override
	public String toString() {
		return "Message [msg=" + msg + ", operation=" + operation + ", controllerName=" + controllerName + "]";
	}
}
//End of Message class