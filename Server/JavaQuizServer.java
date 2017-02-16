import au.edu.uow.QuestionLibrary.*;
import au.edu.uow.Networking.*;

import java.net.*;
import java.util.*;
import java.util.List;
import java.io.*;
import java.io.BufferedReader;
import java.awt.*;

/**
 * Main driver for the Quiz Server. Loads question library <br>
 * and sends the questions to the client. Has threading to<br>
 * allow multiple clients different quizes using the same<br>
 * question set
 *
 * @author CSCI213, Robert Harries, 4733095, rph289
 */

public class JavaQuizServer {

	private static int port = 40213; 
	private static String hostname = "";
	private static String hostIP = "";

	private QuestionLibrary quiz;

	private static Properties props;
	private static String qFileName;

	/**
	 * This is the entry point of the application
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		try {
			//check proper args usage
			if (args.length > 1){
				System.out.println("Usage: java JavaQuizServer &serverHostname&");
				System.exit(0);
				if(args.length == 1) {
					port = Integer.parseInt(args[0]);
				}
			}

			//get properties from file
			props = new Properties();
			FileInputStream in = new FileInputStream("JavaQuizGUI.conf");
			props.load(in);
			in.close();
			qFileName = props.getProperty("QuestionFile");

			//build questionlibrary
			if(!QuestionLibrary.buildLibrary(qFileName)) {
				System.out.println("question file doesnt exist");
				System.exit(0);
			}

			//get host information
			hostname = InetAddress.getLocalHost().getHostName();
			hostIP = InetAddress.getLocalHost().getHostAddress();

			System.out.println("JavaQuizServer listening at: " + port);

			//create new socket for server
			ServerSocket serverSocket = new ServerSocket(port);

			//run all new connections in new thread
			for(;;) {
				List<Question> quiz = QuestionLibrary.makeQuiz(-1);
				Socket incoming = serverSocket.accept();
				Runnable clientH = new JavaClientHandler(incoming, quiz);
				new Thread(clientH).start();
			}

		} catch(Exception e) {
			//if an exception occurs, return failure
			System.out.println("Exception: \n" + e);
		}
	}
}