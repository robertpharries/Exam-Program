package au.edu.uow.Networking;

import au.edu.uow.QuestionLibrary.*;
import java.util.*;
import java.util.List;
import java.io.*;
import java.io.BufferedReader;
import java.net.*;

/**
 * Network connection class. Used for communication to and from the<br>
 * quiz server
 *
 * @author CSCI213, Robert Harries, 4733095, rph289
 */

public class NetworkClientCon {
	static Socket socket = null;

	public NetworkClientCon() {
		
	}

	/**
	 * Connects socket to the server
	 * @param serverHost Server path
	 * @return boolean Returns success
	 */
	public boolean connect(String serverHost) {
		String hostArr[] = serverHost.split(":");
		try {
			socket = new Socket(hostArr[0], Integer.parseInt(hostArr[1]));
		} catch (Exception e) {
			System.out.println("Make sure the server is running and try again");
			return false;
		}
		return true;
	}

	/**
	 * Registers username with the server, recieves OK from server
	 * @param name Name to register
	 * @return boolean Returns success
	 */
	public boolean registerName(String name) {
		try {
			if(socket == null) {
				return false;
			}
			Scanner in = new Scanner(socket.getInputStream());
			OutputStream outStream = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
			
			out.println("REGISTER " + name);

			String input = in.nextLine();

			if(input.equals("OK")) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Retrieves next question from the server
	 * @return Question NextQuestion
	 */
	public Question getQuestion() {
		try {
			if(socket == null) {
				return null;
			}
			Scanner in = new Scanner(socket.getInputStream());
			OutputStream outStream = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);

			out.println("GET QUESTION");

			ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
			Object object = inObj.readObject();

			Question question = (Question) object;

			return question;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found");
		}
		return null;
	}

	/**
	 * Closes the connection to the server
	 * @see #connect
	 */
	public void closeCon() {
		try {
			if(socket == null) {
				return;
			}
			Scanner in = new Scanner(socket.getInputStream());
			OutputStream outStream = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);

			out.println("BYE");

			in.close();
			out.close();
			socket.close();
			socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns if connected
	 * @return boolean Returns connection status
	 */
	public boolean isCon() {
		return(socket != null);
	}
}