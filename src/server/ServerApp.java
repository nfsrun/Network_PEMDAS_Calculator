/*
Kevin Tran
Assignment 4 - UDPMathServer - ServerApp
November 1, 2018
William Mortl
 */

package server;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.EventQueue;
import java.awt.Font;

/**
 * GUI class for UDPMathServer. Utilizes a button and a text field to create a 
 * better experience for receiving user than by conventional console use. 
 * @author Kevin Tran
 */
public class ServerApp {
	
	private JFrame frame;
	private JTextArea console;
	
	//Moved Thread code to ServerApp 
	private Thread serverThread;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerApp window = new ServerApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServerApp() {
		
		try{
			initialize();
			UDPMathServer server = new UDPMathServer(console);
			serverThread = new Thread(server);
			serverThread.start();
		}catch (Exception e){
            console.append("\n\nMath server error: " + e.toString());
        }
		console.append("\n\nMath server running... click on quit to quit...");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
		frame.setBounds(100, 100, 450, 300);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				serverThread.interrupt();
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		frame.getContentPane().add(btnQuit, BorderLayout.SOUTH);
		
		console = new JTextArea();
		console.setTabSize(4);
		console.setWrapStyleWord(true);
		console.setLineWrap(true);
		console.setText("UDPCalculator - Server \nServer always responds back to client via port #: 1605");
		console.setBackground(Color.BLACK);
		console.setForeground(Color.GREEN);
		console.setFont(new Font("Consolas", Font.PLAIN, 12));
		console.setEditable(false);
		JScrollPane consolePane = new JScrollPane();
		
		//Horizontal scroll bar not needed due to console's word wrap. 
		consolePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		consolePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		consolePane.setViewportView(console);
		consolePane.setBackground(Color.BLACK);
		frame.getContentPane().add(consolePane, BorderLayout.CENTER);
	}

}
