package logic;

import java.io.Serializable;

/**
 * This is an enum class that defines the type of instruction.
 * Implements Serilizable to save the state of ExamStatus object and re-create
 * it as needed.
 *
 * @author Moran Davidov
 * @version May 2021
 */
public enum InstructionType implements Serializable{
	FORTEACHER, FORSTUDENT;
}
//End of InstructionType class
