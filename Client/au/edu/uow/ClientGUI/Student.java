package au.edu.uow.ClientGUI;

import java.util.*;

/**
 * Student class, holds name of student and their current score
 * 
 * @author Robert Harries
 * Subject Code: CSCI213
 * Your Name: Robert Harries
 * Your Id: 4733095
 * Your Unix Login: rph289
 */
public class Student {

	/**
	* String containing student name
	*/
	private String studentName;
	/**
	* Int containing total current score
	*/
	private int totalScore;

	/**
	* Constructor to initilize the variables in the object
	* @see #setName()
	* @see #recordScore()
	*/
	public Student() {
		studentName = "";
		totalScore = 0;
	}

	/**
	* Method to set the student name. Can only set if unset
	* @param name The new name for the student
	* @see #getName()
	*/
	public void setName(String name) {
		if(studentName.equals("")) {
			studentName = name;
		}
	}

	/**
	* Method to set the student name. Can only set if unset
	* @return student name as string
	*/
	public String getName() {
		return studentName;
	}

	/**
	* Method to record student score. 
	* @param isCorrect Boolean result indicating question answered correctly
	*/
	public void recordScore(boolean isCorrect) {
		if(isCorrect) {
			totalScore++;
		}
	}

	/**
	* Method to return the students current score
	* @return Students score as int
	*/
	public int getScore() {
		return totalScore;
	}
}