package au.edu.uow.Networking;

import au.edu.uow.QuestionLibrary.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.sql.*;
import java.io.*;
import java.io.BufferedReader;
import java.util.Scanner;
import java.awt.*;

/**
 * Client handler for the server. Listens to an individual<br>
 * client and handles requests appropriatly
 *
 * @author CSCI213, Robert Harries, 4733095, rph289
 */

public class JavaClientHandler implements Runnable {
	private final Socket incoming;
	private final List<Question> quiz;
	private int questionCount = 0;

	/**
	 * Constructor for the handler. 
	 * @param newSoc Socket of connecting client
	 * @param newquiz Shuffled list of the question library
	 */
	public JavaClientHandler(Socket newsoc, List<Question> newquiz) {
		incoming = newsoc;
		quiz = newquiz;
	}

	/**
	 * Executes the thread to listen to the client
	 */
	public void run() {
		String name = ""; 

		try {
			OutputStream outStream = incoming.getOutputStream();     
			PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
			Scanner in = new Scanner(incoming.getInputStream());
			//loop till broken in BYE command
			for(;;) {
				String input = in.nextLine();
				String inputArr[] = input.split(" ");
				//if register, set name and output connected
				if(inputArr[0].equals("REGISTER")) {
					name = inputArr[1];
					System.out.println(name + " registered");
					out.println("OK");
				//if get question, return the next question, serialized
				} else if(input.equals("GET QUESTION")) {
					ObjectOutputStream toClient = new ObjectOutputStream(incoming.getOutputStream());
					toClient.writeObject(quiz.get(questionCount++));
				//if bye, close connection, output disconnected
				} else if(input.equals("BYE")) {
					System.out.println(name + " disconnected");
					incoming.close();	
					break;
				} else {
					out.println("Wrong syntax for command");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}