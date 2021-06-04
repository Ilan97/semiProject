package logic;

import java.io.Serializable;

/**
 * This is an entity class that save details about each exam that is created.
 * Implements Serializable to save the state of ExamFile object and re-create it
 * as needed.
 *
 * @author Bat-El Gardin
 * @version May 2021
 */

@SuppressWarnings("serial")
public class ExamFile implements Serializable {

	// Instance variables **********************************************

	private byte[] content;
	private String filename;

	// Constructors ****************************************************

	/**
	 * @param content
	 * @param filename
	 */
	public ExamFile(byte[] content, String filename) {
		this.content = content;
		this.filename = filename;
	}

	// Instance methods ************************************************

	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

}
//End of ExamFile class