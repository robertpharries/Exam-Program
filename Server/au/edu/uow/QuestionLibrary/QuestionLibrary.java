package au.edu.uow.QuestionLibrary;

import java.util.*;
import java.io.File;
import java.util.Collections.*;

/**
 * Class to contain the questions, read in from file<br>
 * Reads the questions in individually into question objects and<br>
 * adds to the question list contained<br>
 * Also creates lists of randomly selected questions for a quiz
 *
 * @author Robert Harries
 * Subject Code: CSCI213
 * Your Name: Robert Harries
 * Your Id: 4733095
 * Your Unix Login: rph289
 */
public class QuestionLibrary {

	/**
	 * This defines the container for the question library
	 */
	private static List<Question> qLib = new ArrayList<Question>();

	/**
	* Method to build library from given file
	* @param qFile File name for question list
	* @return boolean indicating success
	*/
	public static boolean buildLibrary(String qFile){
		try {
			Scanner fileIn = new Scanner(new File(qFile));
			String inHeader = fileIn.nextLine();
			String input = fileIn.nextLine();

			if(!inHeader.equals("<?xml version=\"1.0\"?>") && !input.equals("<JavaQuestions>")) {
				//if header information incorrect, return failure
				return false;
			}

			input = fileIn.nextLine();

			while(!input.equals("</JavaQuestions>")) {
				if(input.equals("<MQuestion>")) {
					List<String> newQ = new ArrayList<String>();
					List<String> newC = new ArrayList<String>();
					int answer = 0;
					
					//till the end of the question block
					//check what section of the question 
					//being delt with and retrieve information
					//appropiatly
					while(!input.equals("</MQuestion>")) {
						if(input.equals("<question>")) {
							input = fileIn.nextLine();	
							while(!input.equals("</question>")) {
								newQ.add(input);
								input = fileIn.nextLine();
							}
						} else if(input.equals("<answer>")) {
							answer = fileIn.nextInt();
							input = fileIn.nextLine();							
						} else if(input.equals("<choices>")) {
							input = fileIn.nextLine();	
							while(!input.equals("</choices>")) {
								newC.add(input);
								input = fileIn.nextLine();
							}
						}
						input = fileIn.nextLine();
					}

					MultipleChoiceQuestion currQ = new MultipleChoiceQuestion(newQ, newC, answer);

					qLib.add(currQ);
				} else if(input.equals("<TFQuestion>")) {
					List<String> newQ = new ArrayList<String>();
					int answer = 0;
					
					while(!input.equals("</TFQuestion>")) {
						if(input.equals("<question>")) {
							input = fileIn.nextLine();	
							while(!input.equals("</question>")) {
								newQ.add(input);
								input = fileIn.nextLine();
							}
						} else if(input.equals("<answer>")) {
							input = fileIn.nextLine();							
							if(input.equalsIgnoreCase("True")) {
								answer = 1;
							} else {
								answer = 2;
							}
							input = fileIn.nextLine();							
						}
						input = fileIn.nextLine();
					}

					TrueAndFalseQuestion currQ = new TrueAndFalseQuestion(newQ, answer);

					qLib.add(currQ);
				} else {
					//if file format incorrect, return failure
					return false;
				}

				input = fileIn.nextLine();
			}

		} catch(Exception e) {
			//if an exception occurs, return failure
			System.out.println("Exception: \n" + e);
			return false;
		}

		return true;
	}

	/**
	* Makes a quiz list from a selection of questions from the question library
	* Modified for assignment 4, so that if -1 is passed for number of questions
	* The entire list is returned, shuffled.
	* @param noOfQuestions number of questions to be contained in the new quiz
	* @return selection of questions in a list of questions
	*/
	public static List<Question> makeQuiz(int noOfQuestions) {
		List<Question> quizLib;
		Random rand = new Random();

		if(noOfQuestions != -1) {
			//while there are not enough questions in the quiz
			//random a question, if it does not already exist in the 
			//quiz, add it
			quizLib = new ArrayList<Question>();

			while(quizLib.size() < noOfQuestions) {
				int n = rand.nextInt(noOfQuestions);
				if(!quizLib.contains(qLib.get(n))) {
					quizLib.add(qLib.get(n));
				}
			}
		} else {
			quizLib = qLib;
			Collections.shuffle(quizLib, rand);
		}

		return quizLib;
	}
}