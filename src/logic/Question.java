package logic;

import java.io.Serializable;

/**
 * This is an entity class that save details about each question that is
 * created. Implements Serilizable to save the state of Question object and
 * re-create it as needed.
 *
 * @author Bat-El Gardin
 * @author Sharon Vaknin
 * @author Moran Davidov
 * @author Ilan Meikler
 * @author Ohad Shamir
 * @version April 2021
 */

@SuppressWarnings("serial")
public class Question implements Serializable {

	// Instance variables **********************************************

	private String qid;
	private String fid;
	private String cid;
	private String questionID;
	private String author;
	private String instructions;
	private String rightAnswer;
	private String wrongAnswer1;
	private String wrongAnswer2;
	private String wrongAnswer3;

	// Constructors ****************************************************

	public Question() {
	}

	// Instance methods ************************************************

	/**
	 * @return the qid
	 */
	public String getQid() {
		return qid;
	}

	/**
	 * @param qid The qid to set
	 */
	public void setQid(String qid) {
		this.qid = qid;
	}

	/**
	 * @return the fid
	 */
	public String getFid() {
		return fid;
	}

	/**
	 * @param fid The fid to set
	 */
	public void setFid(String fid) {
		this.fid = fid;
	}

	/**
	 * @return the cid
	 */
	public String getCid() {
		return cid;
	}

	/**
	 * @param cid The cid to set
	 */
	public void setCid(String cid) {
		this.cid = cid;
	}

	/**
	 * @return the questionID
	 */
	public String getQuestionID() {
		return questionID;
	}

	/**
	 * @param questionID The questionID to set
	 */
	public void setQuestionID(String questionID) {
		this.questionID = questionID;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author The author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the instructions
	 */
	public String getInstructions() {
		return instructions;
	}

	/**
	 * @param instructions The instructions to set
	 */
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	/**
	 * @return the rightAnswer
	 */
	public String getRightAnswer() {
		return rightAnswer;
	}

	/**
	 * @param rightAnswer The rightAnswer to set
	 */
	public void setRightAnswer(String rightAnswer) {
		this.rightAnswer = rightAnswer;
	}

	/**
	 * @return the wrongAnswer1
	 */
	public String getWrongAnswer1() {
		return wrongAnswer1;
	}

	/**
	 * @param wrongAnswer1 The wrongAnswer1 to set
	 */
	public void setWrongAnswer1(String wrongAnswer1) {
		this.wrongAnswer1 = wrongAnswer1;
	}

	/**
	 * @return the wrongAnswer2
	 */
	public String getWrongAnswer2() {
		return wrongAnswer2;
	}

	/**
	 * @param wrongAnswer2 The wrongAnswer2 to set
	 */
	public void setWrongAnswer2(String wrongAnswer2) {
		this.wrongAnswer2 = wrongAnswer2;
	}

	/**
	 * @return the wrongAnswer3
	 */
	public String getWrongAnswer3() {
		return wrongAnswer3;
	}

	/**
	 * @param wrongAnswer3 The wrongAnswer3 to set
	 */
	public void setWrongAnswer3(String wrongAnswer3) {
		this.wrongAnswer3 = wrongAnswer3;
	}

	/**
	 * @return result The hashCode of Question object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((cid == null) ? 0 : cid.hashCode());
		result = prime * result + ((fid == null) ? 0 : fid.hashCode());
		result = prime * result + ((instructions == null) ? 0 : instructions.hashCode());
		result = prime * result + ((qid == null) ? 0 : qid.hashCode());
		result = prime * result + ((questionID == null) ? 0 : questionID.hashCode());
		result = prime * result + ((rightAnswer == null) ? 0 : rightAnswer.hashCode());
		result = prime * result + ((wrongAnswer1 == null) ? 0 : wrongAnswer1.hashCode());
		result = prime * result + ((wrongAnswer2 == null) ? 0 : wrongAnswer2.hashCode());
		result = prime * result + ((wrongAnswer3 == null) ? 0 : wrongAnswer3.hashCode());
		return result;
	}

	/**
	 * @return true If object is this Question, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (cid == null) {
			if (other.cid != null)
				return false;
		} else if (!cid.equals(other.cid))
			return false;
		if (fid == null) {
			if (other.fid != null)
				return false;
		} else if (!fid.equals(other.fid))
			return false;
		if (instructions == null) {
			if (other.instructions != null)
				return false;
		} else if (!instructions.equals(other.instructions))
			return false;
		if (qid == null) {
			if (other.qid != null)
				return false;
		} else if (!qid.equals(other.qid))
			return false;
		if (questionID == null) {
			if (other.questionID != null)
				return false;
		} else if (!questionID.equals(other.questionID))
			return false;
		if (rightAnswer == null) {
			if (other.rightAnswer != null)
				return false;
		} else if (!rightAnswer.equals(other.rightAnswer))
			return false;
		if (wrongAnswer1 == null) {
			if (other.wrongAnswer1 != null)
				return false;
		} else if (!wrongAnswer1.equals(other.wrongAnswer1))
			return false;
		if (wrongAnswer2 == null) {
			if (other.wrongAnswer2 != null)
				return false;
		} else if (!wrongAnswer2.equals(other.wrongAnswer2))
			return false;
		if (wrongAnswer3 == null) {
			if (other.wrongAnswer3 != null)
				return false;
		} else if (!wrongAnswer3.equals(other.wrongAnswer3))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Question num." + qid + "\n" + instructions + "\n" + rightAnswer + "\n" + wrongAnswer1 + "\n"
				+ wrongAnswer2 + "\n" + wrongAnswer3;
	}
}
//End of Question class