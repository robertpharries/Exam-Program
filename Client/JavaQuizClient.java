import au.edu.uow.QuestionLibrary.*;
import au.edu.uow.ClientGUI.*;
import au.edu.uow.Networking.*;

import java.util.*;
import java.util.List;
import java.io.*;
import java.io.BufferedReader;
import java.awt.*;

/**
 * Main file for the Client Quiz task. Reads the properites from config file<br>
 * and starts the client with the appropriate settings
 *
 * @author CSCI213, Robert Harries, 4733095, rph289
 */

public class JavaQuizClient {

	/**
	 * This is the entry point of the application
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		try {

			Properties props;
			Dimension dim;
			String lnf;
			int qNum;

			QuizClientGUIFrame ui;

			//get properties from file
			props = new Properties();
			//stream to read config
			FileInputStream in = new FileInputStream("JavaQuizGUI.conf");
			props.load(in);
			in.close();
			//set variables from config
			int hor = Integer.parseInt(props.getProperty("SizeHorizontal"));
			int ver = Integer.parseInt(props.getProperty("SizeVertical"));
			dim = new Dimension(hor,ver);
			lnf = props.getProperty("LookAndFeel");
			qNum = Integer.parseInt(props.getProperty("NumberOfQuestions"));
			
			//build window
			ui = new QuizClientGUIFrame(dim, lnf, qNum);
			ui.startQuiz();
		} catch(Exception e) {
			//if an exception occurs, return failure
			System.out.println("Exception: \n" + e);
		}
	}
}