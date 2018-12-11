/*
Kevin Tran
Assignment 4 - UDPMathServer - UDP Calculator
November 1, 2018
William Mortl
 */

package server;

import java.net.*;
import javax.swing.JTextArea;

//References: 
//Omit nulls from Byte String: 
//https://stackoverflow.com/questions/42574130/how-to-convert-a-byte-array-to-a-string-in-java-and-skip-all-null-bytes

/**
 * UDPMathServer provided by William Mortl. Modified to use in conjunction with
 * the GUI ServerApp. 
 * @author William Mortl, Kevin Tran
 */
class UDPMathServer implements Runnable 
{
	//The UDP MathServer is always at port 1604. 
    private static final int Port = 1604;
    
    //The JTextArea reference variable to the console in ServerApp GUI. 
    private JTextArea console;
    
    /**
     * Constructor that would take in a JTextArea reference as the GUI console. 
     * @param console: JTextArea reference to GUI console output. 
     */
    public UDPMathServer(JTextArea console)
    {
        this.console = console;
    }

    /**
     * Thread's run method that has been overridden to run a threaded job in 
     * keeping itself ready for receiving UDP Packets through the 
     * DatagramSocket, calculating, the answer, and sending an answer response back. 
     */
    public void run() 
    {
        DatagramSocket ds = null;
        try
        {
            ds = new DatagramSocket(Port);
        }
        catch (Exception e)
        {
        	printLog("Couldn't create datagram socket: " +
                    e.toString());
        }
        while(true)
        {
            DatagramPacket dp = null;
            DatagramPacket reply = null;
            try
            {
                dp = new DatagramPacket(new byte[1514], 1514);
                ds.receive(dp);
                
                //minmum position for a null byte inferring a command with 
                //two-one digit ints
                int lastNull = 1514;
                byte[] byteString = dp.getData();
                while(byteString[lastNull-1] == 0 && lastNull-1 < byteString.length) {
                	lastNull--;
                }
                String message = new String(dp.getData(), 0, lastNull, "UTF-8");
                printLog("Received via UDP: " + message);
                
                // answer the math problem and print answer with only 8 decimal points
                String mathAnswer = String.format("%.8f", this.handleMathMessage(message));
                
                printLog("The math answer to the message was : " + mathAnswer);
                reply = new DatagramPacket(mathAnswer.getBytes(), 
                		mathAnswer.length(), dp.getAddress(), 1605);
                ds.send(reply);
                Thread.yield();
            }
            catch (Exception e)
            {
            	printLog("Failed to receive UDP packet, " +
                        "general exception: " + e.toString());
                break;
            }
        }
    }
	
    /**
     * Appends a given message to the GUI log or console window. 
     * @param message: string to append a message to log. 
     */
	private void printLog(String message) {
    	if(this.console != null) {
    		this.console.append(message + "\n\n");
    	}else 
    		System.out.println(message);
    }
    
    /**
     * Method takes equation that it was given and processes it so that it 
     * comes out with an answer. Currently will only take addition, division, 
     * subtraction, multiplication, and exponential requests. 
     * @param message: String of verified equation. 
     * @return String of answer will be returned. 
     */
    private Double handleMathMessage(String message)
    {
        String[] parts = message.split(" ");
        double one = Double.parseDouble(parts[1]),
                two = Double.parseDouble(parts[2]);
        switch(parts[0]){
            case("a"):
                return one + two;
            case("+"):
                return one + two;
            case("m"):
                return one * two;
            case("*"):
                return one * two;
            case("s"):
                return one - two;
            case("-"):
                return one - two;
            case("d"):
                return one / two;
            case("/"):
                return one / two;
            case("e"):
                return Math.pow(one, two);
            case("^"):
            	return Math.pow(one, two);
        }
         return Double.NaN;
    }
}