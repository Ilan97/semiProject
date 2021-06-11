package logic;

/**
 * This is an entity class that save details about each client that is
 * connected/disconnected to the server.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @version May 2021
 */

public class Client {

	// Instance variables **********************************************

	private String ip;
	private String hostName;
	private ClientStatus status;

	// Constructors ****************************************************

	/**
	 * @param ip       The ip.
	 * @param hostName The hostName.
	 * @param status   The status.
	 */
	public Client(String ip, String hostName, ClientStatus status) {
		this.ip = ip;
		this.hostName = hostName;
		this.status = status;
	}

	// Instance methods ************************************************

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip The ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName The hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the status
	 */
	public ClientStatus getStatus() {
		return status;
	}

	/**
	 * @param status The status to set
	 */
	public void setStatus(ClientStatus status) {
		this.status = status;
	}
}
//End of Client class