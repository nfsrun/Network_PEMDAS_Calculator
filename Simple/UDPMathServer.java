/*
Kevin Tran
Assignment 4 - UDPMathServer - UDP Calculator
November 1, 2018
William Mortl
 */

import java.io.*;
import java.net.*;

//UDPMathServer

/**
 * UDPMathServer provided by William Mortl. Modified to use in conjunction with
 * the GUI ServerApp.
 * @author William Mortl, Kevin Tran
 */
class UDPMathServer implements Runnable 
{
    private static final int Port = 1604;
    private boolean running = false;

    // main entry point for the application
    public static void main(String args[]) 
    {
        try 
        {
            // startup
            UDPMathServer server = new UDPMathServer();
            Thread serverThread = new Thread(server);
            serverThread.start();

            // wait until finished
            System.out.println("Math server running, press enter to quit...");
            Console cons = System.console(); 
            String enterString = cons.readLine();

            // shutdown gracefully
            server.running = false;
            serverThread.interrupt();
        } 
        catch (Exception e) 
        {
            System.err.println("Math server error: " + e.toString());
        }
    }

    // constructor
    public UDPMathServer()
    {
        this.running = true;
    }

    // this is the server thread
    public void run() 
    {
        DatagramSocket ds = null;
        try
        {
            ds = new DatagramSocket(Port);
        }
        catch (Exception e)
        {
            System.err.println("Couldn't create datagram socket: " +
                    e.toString());
        }

        while(this.running == true)
        {
            DatagramPacket dp = null;
            try
            {
                dp = new DatagramPacket(new byte[1024], 1024);
                ds.receive(dp);
                String message = new String(dp.getData());
                
                // answer the math problem and print answer
                int mathAnswer = this.handleMathMessage(message);
                System.out.println("Received via UDP: " + message);
                System.out.println("The math answer to the message was : " +
                        Integer.toString(mathAnswer));
                Thread.yield();
            }
            catch (Exception e)
            {
                System.err.println("Failed to receive UDP packet, " +
                        "general exception: " + e.toString());
                this.running = false;
                break;
            }
        }
    }

    /**
     * Helper method takes equation that it was given and processes it so that
     * it comes out with an answer. Currently will only take addition,
     * division, subtraction, and multiplication requests.
     * @param message: String of verified equation.
     * @return String of answer will be returned.
     */
    private int handleMathMessage(String message)
    {
        String[] parts = message.split(" ");
        int one = Integer.parseInt(parts[1]),
                two = Integer.parseInt(parts[2]);
        switch(parts[0]){
            case("a"):
                return one + two;
            case("m"):
                return one * two;
            case("s"):
                return one - two;
            case("d"):
                return one / two;
        }
         return -1;
    }
}