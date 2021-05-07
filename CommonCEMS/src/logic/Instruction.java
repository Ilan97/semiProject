package logic;

import java.io.Serializable;

/**
 * This is an entity class that save details about each instruction that is
 * created. Implements Serilizable to save the state of Instruction object and
 * re-create it as needed.
 *
 * @author Moran Davidov
 * @version May 2021
 */

@SuppressWarnings("serial")
public class Instruction implements Serializable {

	// Instance variables **********************************************
		private String qid;
		private String eid;
		private String fid;
		private String cid;
		private String instructionMsg;
	
	// Constructors ****************************************************
		public Instruction() {
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
		 * @return the eid
		 */
		public String getEid() {
			return eid;
		}

		/**
		 * @param eid The eid to set
		 */
		public void setEid(String eid) {
			this.eid = eid;
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
		 * @return the instructionMsg
		 */
		public String getInstructionMsg() {
			return instructionMsg;
		}

		/**
		 * @param instructionMsg The instructionMsg to set
		 */
		public void setInstructionMsg(String instructionMsg) {
			this.instructionMsg = instructionMsg;
		}

		/**
		 * @return result The hashCode of Instruction object
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((cid == null) ? 0 : cid.hashCode());
			result = prime * result + ((eid == null) ? 0 : eid.hashCode());
			result = prime * result + ((fid == null) ? 0 : fid.hashCode());
			result = prime * result + ((instructionMsg == null) ? 0 : instructionMsg.hashCode());
			result = prime * result + ((qid == null) ? 0 : qid.hashCode());
			return result;
		}

		/**
		 * @return true If object is this Instruction, false otherwise
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Instruction other = (Instruction) obj;
			if (cid == null) {
				if (other.cid != null)
					return false;
			} else if (!cid.equals(other.cid))
				return false;
			if (eid == null) {
				if (other.eid != null)
					return false;
			} else if (!eid.equals(other.eid))
				return false;
			if (fid == null) {
				if (other.fid != null)
					return false;
			} else if (!fid.equals(other.fid))
				return false;
			if (instructionMsg == null) {
				if (other.instructionMsg != null)
					return false;
			} else if (!instructionMsg.equals(other.instructionMsg))
				return false;
			if (qid == null) {
				if (other.qid != null)
					return false;
			} else if (!qid.equals(other.qid))
				return false;
			return true;
		}
		
		
}
//End of Instruction class