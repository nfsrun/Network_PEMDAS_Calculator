/*
Kevin Tran
Assignment 4 - UDPMathClient - UDP Calculator
November 1, 2018
William Mortl
 */

//References: 
//Primitive data sizes: 
//https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html
//Double Max/Min info: 
//https://docs.oracle.com/javase/7/docs/api/java/lang/Double.html

package client;

import java.awt.Color;
import java.io.IOException;
import java.net.*;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

/**
 * UDPMathClient provided by William Mortl. Modified to use in conjunction with
 * the GUI ClientApp. 
 * @author William Mortl, Kevin Tran
 */
class UDPMathClient 
{
	
	//Client now has own port; port 1605
    private static final int Port = 1605;
    
    private DatagramSocket ds;
    
    //References to two text areas of the GUI are now referred in client. 
    private JTextPane answer;
    private JTextArea console;
    
    /**
     * Default Constructor. 
     */
    public UDPMathClient() {
    	try {
			ds = new DatagramSocket(Port);
			
			//A timeout is defined in DatagramSocket for receiving 
			//rule purposes. 
			ds.setSoTimeout(2000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Constructor for two items. 
     * @param answer: maps this object to a referred answer text pane. 
     * @param console: maps this object to a referred console text area. 
     */
    public UDPMathClient(JTextPane answer, JTextArea console) {
    	this();
    	this.answer = answer;
    	this.console = console;
    }
    
    /**
     * Closes socket if socket is in use and not null. 
     */
    public void close() {
    	if(ds != null)
    		ds.close();
    }
    
    /**
     * Method prepares the whole packet by packing the message into a 
     * DatagramPacket and sends it to the specific IP address that has been 
     * given. Prior to all of this, it will make sure that the equation length 
     * is reasonable, that the actual destination is valid and is existent, 
     * the socket can be created, and that the answer packet will be received 
     * within a reasonable amount of time. 
     * @param equation: String of the math equation given. 
     * @param ip: String input of verified IP Address. 
     */
    public void sendAndReceive(String equation, String ip) {
        try
        {
        	
            DatagramPacket dp = null;
            verifyMathString(equation);
            
            //Total Packet Message Size limit is set to 1 operation character, 
            //2 spaces, and two double values. Each double value (at it's worst
            //and longest string form), can be 0.1*10^-374 characters, so a 
            //total of 377 characters are possible in each double value. 
            //377 characters / double value * 2 double values is 754 
            //characters. This leads a grand total of 757 bytes as the maximum 
            //value taken in a message. 
            if(equation.length()>757) {
            	printLog("This string is too long. ");
            	printError("This string is too long. ");
            }
            //makes sure commands are lower-cased for user design purposes. 
            dp = new DatagramPacket(equation.toLowerCase().getBytes(), equation.length(),
                    InetAddress.getByName(ip), 1604);
            ds.send(dp);
            printLog("Sent packet: " + equation);
            
            //This is essentially the same number as discussed above but 
            //without various characters and with only one double value. This 
            //means we can get at max 754 bytes of string data for a double. 
            DatagramPacket reply = new DatagramPacket(new byte[757], 757);
        	ds.receive(reply);
        	
        	//minmum position for a null byte inferring a command with 
            //two-one digit ints
            byte[] byteString = reply.getData();
            int lastNull = byteString.length;
            while(byteString[lastNull-1] == 0 && lastNull-1 < byteString.length) {
            	lastNull--;
            }
        	
            String answer = new String(reply.getData(), 0, lastNull, "UTF-8");
        	if(answer!=null) {
        		printLog("Received packet: " + answer);
        		printAnswer(answer);
        	}
        		
        } catch (SocketTimeoutException ste) {
        	printLog("Failed to receive answer packet within 2000 ms. ");
        	printError("Error: Answer Packet Not Received.");
        } catch (UnknownHostException uhe){
        	printLog("Host does not exist or you have entered an invalid value for IP. ");
        	printError("Error: Non-Existant Host or Invalid IP Value");
        } catch (IllegalArgumentException iae){
        	printLog("Math Equation is not valid. ");
        	printError("Math equation is not valid. ");
        } catch (IOException e){
        	printLog("Failed to create socket and send " +
                    "packet: " + e.toString());
        } 
    }
    
    /**
     * Method checks whether a given equation string is legal or not. 
     * @param equation: Given string of equation. 
     * @throws IllegalArgumentException: Throws exception if illegal equation 
     * string was read. 
     */
    private void verifyMathString(String equation) throws IllegalArgumentException {
		if(!equation.matches("^(a|\\+|s|-|d|\\/|m|\\*|e|\\^)(\\s([0-9]*\\.?[0-9]+)){2}$"))
			throw new IllegalArgumentException("Invalid Equation String. ");
	}

    /**
     * Appends a given message to the GUI log or console window. 
     * @param message: string to append a message to log. 
     */
	private void printLog(String message) {
    	if(this.console != null) {
    		this.console.append(message + "\n\n");
    	}else {
    		System.out.println(message);
    	}
    }
    
    /**
     * Overwrites the GUI answer field or writes to the console window with an 
     * error message. 
     * @param message: string to append a message to log. 
     */
	private void printError(String message) {
		answer.setForeground(Color.RED);
    	answer.setText(message);
	}
	
	/**
     * Overwrites the GUI answer field or writes to the console window with an 
     * answer. 
     * @param message: string to append a message to log. 
     */
    private void printAnswer(String message) {
    	if(this.answer != null) {
    		answer.setForeground(Color.BLACK);
    		this.answer.setText(message);
    	}else {
    		System.out.println(message);
    	}
    }
}