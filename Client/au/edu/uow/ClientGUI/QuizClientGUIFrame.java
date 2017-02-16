package au.edu.uow.ClientGUI;

import java.util.*;
import java.util.List;
import au.edu.uow.QuestionLibrary.*;
import au.edu.uow.Networking.*;
import java.sql.*;
import java.io.*;
import java.io.BufferedReader;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * This program implements the QuestionDB interface and <br>
 * gives functionality required to load database, fill <br>
 * database with questions and other interactions required <br>
 * for the database
 *
 * @author CSCI213, Robert Harries, 4733095, rph289
 */

public class QuizClientGUIFrame extends JFrame {

	//object for student
	Student student;
	//j objects
	JPanel cardList;
	JFrame mainFrame;
	JTextField nameField;
	int quizNum;
	List<Question> quiz;
	int pageCount = 0;
	JLabel statusLabel;
	//the default for the server
	String currentServer = "localhost:40213";

	NetworkClientCon conn;
	
	/**
	 * This is the entry point of the application
	 * @param size Size of the frame window
	 * @param lnf Look and Feel setting
	 */
	public QuizClientGUIFrame(Dimension size, String lnf, int qnum) {
		//call jframe constructor
		mainFrame = new JFrame("JavaQuizGUI");	
		try {
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		        public void run() {
		            if(conn != null) {
		            	conn.closeCon();
		            }
		        }
		    }, "Shutdown-thread"));

		    quizNum = qnum;

			//set frame properties
			mainFrame.setLayout(new BorderLayout());
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.setPreferredSize(size);
			mainFrame.setLocationRelativeTo(null);
			mainFrame.setResizable(true);									//should be resizeable?
			UIManager.setLookAndFeel(lnf);
		} catch(Exception e) {
			//if an exception occurs, return failure
			System.out.println("Exception: \n" + e);
			e.printStackTrace();
		}
	}

	/**
	 * Starts the quiz.
	 * @param newQuiz Selection of questions for the quiz
	 * @see showMain()
	 */
	public void startQuiz() {
		showMain();
	}

	/**
	 * Builds and adds the menu bar and the status bar for the window
	 * @see showMain()
	 */
	private void buildToolBar() {
		//create menubar and add menus to it
		JMenuBar menuBar = new JMenuBar();
        JMenu conMenu = new JMenu("Connection");
        menuBar.add(conMenu);
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        //add  items to menus
        final JMenuItem connectAct = new JMenuItem("Connect");
        final JMenuItem disconnectAct = new JMenuItem("Disconnect");
        disconnectAct.setEnabled(false);
        JMenuItem setAct = new JMenuItem("Set Server");
        JMenuItem closeAct = new JMenuItem("Exit");
        conMenu.add(connectAct);
        conMenu.add(disconnectAct);
        conMenu.add(new JSeparator());
        conMenu.add(setAct);
        conMenu.add(new JSeparator());
        conMenu.add(closeAct);

        JMenuItem aboutAct = new JMenuItem("About");
        helpMenu.add(aboutAct);
        //connect event
        connectAct.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent arg0) {
            	if(student == null && !nameField.getText().equals("")) {
		    		conn = new NetworkClientCon();
		    		//connect, if connection succeeds
		    		if(conn.connect(currentServer)) {
		    			statusLabel.setText("Connected to " + currentServer);
						connectAct.setEnabled(false);
            			disconnectAct.setEnabled(true);
		    		} else {
            			//if no server found dont start and show message
            			JOptionPane.showMessageDialog(null, "No server on that path", "Message", JOptionPane.INFORMATION_MESSAGE);
            			return;
		    		}
					//create student object
					student = new Student();
					student.setName(nameField.getText());
		    		conn.registerName(student.getName());
		    		makePages();
					//call next card in list
					CardLayout cardLayout = (CardLayout) cardList.getLayout();
					cardLayout.next(cardList);
				} else {
            		JOptionPane.showMessageDialog(null, "Please enter your name first", "Message", JOptionPane.INFORMATION_MESSAGE);
				}
            }
        } );
		//disconnect event
        disconnectAct.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent arg0) {
            	if(conn != null) {
            		conn.closeCon();
            		conn = null;
		    		statusLabel.setText("Disconnected");
            	}
            }
        } );

        setAct.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent arg0) {
               currentServer = JOptionPane.showInputDialog(null, "Server:Port", currentServer);
            }
        } );

        closeAct.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent arg0) {
                System.exit(JFrame.NORMAL);
            }
        } );

        aboutAct.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent arg0) {
            	JOptionPane.showMessageDialog(null, "Java Quiz Client v6.9\nBased on Java sockets\nby Robert Harries", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        } );

        mainFrame.setJMenuBar(menuBar);

        //create and show status bar
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));

		statusLabel = new JLabel("Connect to the server first.");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);

		statusPanel.add(statusLabel);

		mainFrame.add(statusPanel, BorderLayout.SOUTH);
	}

	/**
	 * Makes the initial page for the program
	 * @see showMain()
	 */
	private void makeFirst() {
		cardList = new JPanel(new CardLayout());

		JPanel page = new JPanel(new BorderLayout());

		JLabel title = new JLabel("Java Quiz", SwingConstants.CENTER);
		title.setFont(title.getFont().deriveFont(32f));
		title.setForeground(Color.BLUE);
		JLabel created = new JLabel("Created by:", SwingConstants.CENTER);
		JLabel name = new JLabel("Robert Harries", SwingConstants.CENTER);

		//create panel for puting text in the middle of the screen,
		JPanel panelCenter = new JPanel(new GridBagLayout());
		panelCenter.setBackground(Color.CYAN);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panelCenter.add(title, gbc);
		gbc.gridy++;
		panelCenter.add(created, gbc);
		gbc.gridy++;
		panelCenter.add(name, gbc);

		JPanel panelInput = new JPanel();
		panelInput.setLayout(new FlowLayout());
		JLabel nameLabel = new JLabel("Your Name:");
		panelInput.add(nameLabel);
		nameField = new JTextField(20);
		panelInput.add(nameField);

		page.add(panelCenter, BorderLayout.CENTER);
		page.add(panelInput, BorderLayout.SOUTH);

		cardList.add(page);
	}

	/**
	 * Builds and adds all the windows for the questions
	 * @see showMain()
	 */
	private void makePages() {
		//get questions from server based on client number of questions
		Question newq;
		quiz = new ArrayList<Question>();
		for (int i = 0; i < quizNum; i++) {
			newq = conn.getQuestion();
			quiz.add(newq);
		}

		//create page for each item in quiz
		for(int i = 0; i < quiz.size();  i++) {
			//create new card
			JPanel card = new JPanel();
			//split panel for q and a
			JPanel panelSplit = new JPanel(new GridLayout(2,1));
			//panel for q half
			JPanel panelQuest = new JPanel(new GridBagLayout());
			panelQuest.setBackground(Color.WHITE);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.fill = GridBagConstraints.BOTH;
       		gbc.insets = new Insets(15, 5, 0, 0);
			gbc.fill = GridBagConstraints.HORIZONTAL;

			//get question, create label in q half for each line
			final Question curQ = quiz.get(i);
			List<String> qList = curQ.getQuestion();
			for(String curStr : qList) {
				panelQuest.add(new JLabel(curStr), gbc);
				gbc.gridy++;
			}

			//create panel for answer
			JPanel panelAns = new JPanel(new BorderLayout());
			//gridbag for answer list
			JPanel panelChoice = new JPanel(new GridBagLayout());
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			//button group for radio buttons, create one for each choice
			final ButtonGroup group = new ButtonGroup();
			List<String> cList = curQ.getChoices();
			for(String curStr : cList) {
				JRadioButton tempb = new JRadioButton(curStr);
				tempb.setMnemonic(KeyEvent.VK_B);
				tempb.setSelected(false);
				group.add(tempb);
				panelChoice.add(tempb, gbc);
				gbc.gridy++;
			}
			
			//create button for next			
			JPanel bpanel = new JPanel();
			JButton nextButton = new JButton("Next");
			nextButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//loop through radio buttons, record score
					int i = 1;
					for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
			            AbstractButton button = buttons.nextElement();

			            if (button.isSelected()) {
			               	student.recordScore(curQ.compareAnswer(i));
			               	pageCount++;
			               	//if end of quiz
			               	if(pageCount == quiz.size()) {
			               		if(conn != null) {
				            		conn.closeCon();
				            		conn = null;
						    		statusLabel.setText("Disconnected");
				            	}
								makeLast();
			               	}
		               		CardLayout cardLayout = (CardLayout) cardList.getLayout();
							cardLayout.next(cardList);
			            } else {
			            	i++;
			            }
			        }
				}		
			});
			//add panels
			bpanel.add(nextButton);

			panelAns.add(panelChoice, BorderLayout.CENTER);
			panelAns.add(bpanel, BorderLayout.PAGE_END);

			panelSplit.add(panelQuest);
			panelSplit.add(panelAns);
			cardList.add(panelSplit);
		}
	}

	/**
	 * Builds and adds the results page
	 * @see showMain()
	 */
	private void makeLast() {
		//create text from result
		JLabel result = new JLabel("Result of " + student.getName() + ": " + student.getScore() + " out of " + quiz.size(), SwingConstants.CENTER);

		//set text for center of screen
		JPanel panelLast = new JPanel(new BorderLayout());
		JPanel panelBag = new JPanel();
		panelBag.setBackground(Color.CYAN);
		panelBag.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panelBag.add(result, gbc);

		//add in
		panelLast.add(panelBag, BorderLayout.CENTER);
		
		cardList.add(panelLast);
	}

	/**
	 * Calls functions to build the program, shows the application window
	 * @see startQuiz()
	 */
	private void showMain() {
		buildToolBar();
		//make first page
		makeFirst();
		//add pages to frame
		mainFrame.add(cardList, BorderLayout.CENTER);	

		mainFrame.pack();
		
		mainFrame.setVisible(true);
	}
}