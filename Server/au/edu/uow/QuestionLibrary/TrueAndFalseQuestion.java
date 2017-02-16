package au.edu.uow.QuestionLibrary;

import java.util.*;

/**
 * Implements a true or false question based on the question interface<br>
 *       
 * @author Robert Harries
 * Subject Code: CSCI213
 * Your Name: Robert Harries
 * Your Id: 4733095
 * Your Unix Login: rph289
 */

public class TrueAndFalseQuestion implements Question {

	/**
	 * String list to hold the question text
	 */
	private List<String> question;
	/**
	 * String list to hold the choice text
	 */
	private List<String> choices;
	/**
	 * String list to hold the answer number
	 */
	private int answer;

	/**
	 * Constructer for class<br>
	 * Choices are set to True and False
	 * @param #newQ Question text
	 * @param #newA answer number
	 * @see #getQuestion()
	 * @see #getChoices()
	 * @see #compareAnswer(int)
	 */
	public TrueAndFalseQuestion(List<String> newQ, int newA) {
		question = newQ;
		choices = Arrays.asList("True", "False");
		answer = newA;
	}

	public List<String> getQuestion() {
		return question;
	}

	public List<String> getChoices() {
		return choices;
	}

	public boolean compareAnswer(int ans) {
		return (ans == answer);
	}
}
