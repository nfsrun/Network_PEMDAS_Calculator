/*
Kevin Tran
Assignment 4 - UDPMathClient - ClientApp
November 1, 2018
William Mortl
 */

//References: 
//GUI Designer: Eclipse WindowBuilder Add-On
//https://www.eclipse.org/windowbuilder/
//Scroll Bar Pane On Console: 
//https://stackoverflow.com/questions/1052473/scrollbars-in-jtextarea
//https://www.youtube.com/watch?v=tcs-Sz1WPuU
//Add Enter ActionListener to JTextField
//https://stackoverflow.com/questions/4419667/detect-enter-press-in-jtextfield

package client;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.FlowLayout;

/**
 * GUI class for UDPMathClient. Utilizes various textboxes, tab panes, buttons 
 * and text fields to create a better experience for user than by conventional  
 * console use. 
 * @author Kevin Tran
 */
public class ClientApp {

	private JFrame frame;
	private JTextArea console;
	private JTextField ip;
	private JTextField input;
	private JTextPane answer;
	private UDPMathClient client;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientApp window = new ClientApp();
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
	public ClientApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 501, 453);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("486px:grow"),},
			new RowSpec[] {
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("15px"),
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("61px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel welcome = new JLabel("Welcome to UDP Calculator. UDP Calculator Client always sends to port #: 1604. ");
		frame.getContentPane().add(welcome, "1, 2, center, top");
		welcome.setFont(new Font("Lucida Sans", Font.PLAIN, 12));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, "1, 4, 1, 9, fill, fill");
		
		JPanel connectionPanel = new JPanel();
		tabbedPane.addTab("Connection", null, connectionPanel, null);
		
		JLabel ipLabel = new JLabel("IP: ");
		connectionPanel.add(ipLabel);
		
		ip = new JTextField();
		ip.setToolTipText("Please only enter IP in the following format: 0-255.0-255.0-255.0-255. ");
		connectionPanel.add(ip);
		ip.setText("127.0.0.1");
		ip.setColumns(12);
		
		JButton btnResetToThis = new JButton("Reset to computer / Localhost");
		btnResetToThis.addActionListener(new ActionListener() {
			//Reverts the IP field back to this computer (localhost)
			public void actionPerformed(ActionEvent arg0) {
				ip.setText("127.0.0.1");
			}
		});
		connectionPanel.add(btnResetToThis);
		
		console = new JTextArea();
		console.setBounds(0, 1, 454, 123);
		console.setTabSize(4);
		console.setWrapStyleWord(true);
		console.setLineWrap(true);
		console.setFont(new Font("Consolas", Font.PLAIN, 12));
		console.setBackground(Color.BLACK);
		console.setForeground(Color.GREEN);
		console.setEditable(false);
		JScrollPane consolePanel = new JScrollPane();
		
		//Horizontal scroll bar not needed due to console's word wrap. 
		consolePanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		consolePanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		consolePanel.setViewportView(console);
		consolePanel.setBackground(Color.BLACK);
		tabbedPane.addTab("Console Log", null, consolePanel, null);
		
		JPanel helpPanel = new JPanel();
		tabbedPane.addTab("Input Help", null, helpPanel, "");
		helpPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblExponent = new JLabel("^ (E)xponent");
		helpPanel.add(lblExponent);
		
		JLabel lbladdition = new JLabel("+ (A)ddition");
		helpPanel.add(lbladdition);
		
		JLabel lblmultiplication = new JLabel("* (M)ultiplication");
		helpPanel.add(lblmultiplication);
		
		JLabel lblsubtraction = new JLabel("- (S)ubtraction");
		helpPanel.add(lblsubtraction);
		
		JLabel lbldivision = new JLabel("/ (D)ivision");
		helpPanel.add(lbldivision);
		
		JLabel lblExampleOfAn = new JLabel("Example of an equation: 2 + 3 => a 2 3");
		helpPanel.add(lblExampleOfAn);
		
		JLabel lblExampleOfAnother = new JLabel("Example of another equation: 2.26565 ^ 658.265 => ^ 2.26565 658.265");
		helpPanel.add(lblExampleOfAnother);
		
		JLabel lblInput = new JLabel("Input");
		frame.getContentPane().add(lblInput, "1, 14, center, top");
		
		input = new JTextField();
		input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				send();
			}
		});
		input.setToolTipText("Please enter an equation string. \r\n0-9, /, *, -, +, ^, (, ,), and \" \" are only allowed. ");
		
		frame.getContentPane().add(input, "1, 16, 1, 3, fill, fill");
		
		JLabel lblOutput = new JLabel("Output:");
		frame.getContentPane().add(lblOutput, "1, 20, center, top");
		
		answer = new JTextPane();
		answer.setFont(new Font("Arial", Font.PLAIN, 14));
		frame.getContentPane().add(answer, "1, 22, fill, fill");
		
		JButton calculate = new JButton("Send Calculation");
		//Sends the equation string to calculating server when Calculation button is pressed
		calculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				send();
			}
		});
		frame.getContentPane().add(calculate, "1, 24");
		
		JButton clear = new JButton("Clear Input");
		//Clears the input string when pressed
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				input.setText("");
			}
		});
		frame.getContentPane().add(clear, "1, 26");
		JButton quit = new JButton("Quit");
		//Quit Application when quit is clicked. 
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				client.close();
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		frame.getContentPane().add(quit, "1, 28");
		client = new UDPMathClient(answer, console);
	}
	
	/**
	 * Method to send information to the actual UDPMathServer object. 
	 * Prevents redundancy that would occur between two different events: 
	 * 1. Clicking on the calculate button & 
	 * 2. Hitting enter in the input form. 
	 */
	private void send() {
		//Checks if valid IP address by the following regular expression
		if(ip.getText().matches("^(((1?[0-9]{2})|(2[0-4][0-9])|(25[0-5])|[0-9])\\.){3}((1?[0-9]{2})|(2[0-4][0-9])|(25[0-5])|[0-9])$")) 
			client.sendAndReceive(input.getText(), ip.getText());
		else {
			console.append("You have entered an invalid IP. ");
			answer.setForeground(Color.RED);
			answer.setText("Error: Invalid IP Value");
		}
	}
}
