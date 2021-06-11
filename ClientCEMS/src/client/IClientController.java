package client;

import logic.Message;

public interface IClientController {

	/**
	 * This method handles all data coming from the UI.
	 *
	 * @param msg {@link Message} The message from the UI.
	 * @return message The result from server
	 * 
	 */
	Object handleMessageFromClientUI(Message msg);


}