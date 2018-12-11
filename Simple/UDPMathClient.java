/*
Kevin Tran
Assignment 4 - UDPMathClient - UDP Calculator
November 1, 2018
William Mortl
 */

import java.net.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * UDPMathClient provided by William Mortl. Modified to use in conjunction with
 * the GUI ClientApp.
 * @author William Mortl, Kevin Tran
 */
class UDPMathClient 
{
    private static final int Port = 1604;

    // main entry point for the application
    public static void main(String args[]) 
    {
        Scanner s = new Scanner(System.in);
        String input = "";
        try 
        {
            boolean shouldQuit = false;
            while(!shouldQuit)
            {
                System.out.println("Please enter your choice: (a)dd, " +
                        "(s)ubtract, (m)ultiply, (d)ivide, (q)uit");
                input = s.next();
                if (input.equals("q"))
                        shouldQuit = true;
                else if(input.equals("a") | input.equals("d") |
                        input.equals("s") | input.equals("m"))
                        input += numbers(s);
                else {
                    System.out.println("Please input command again.");
                    input = s.next();
                }
                DatagramSocket ds = null;
                DatagramPacket dp = null;
                try
                {
                    //creation of a new datagram socket using the input string
                    InetAddress localIpAddress = InetAddress.getLocalHost();
                    ds = new DatagramSocket();
                    //ensures that input string sent will be less than 1024
                    //bytes for personal protocol consistency.
                    if(input.length()>1024)
                        throw new IllegalArgumentException("This string is " +
                                "too complex");
                    dp = new DatagramPacket(input.getBytes(), input.length(),
                            localIpAddress, Port);
                    ds.send(dp);
                }
                catch (Exception e)
                {
                    System.err.println("Failed to create socket and send " +
                            "packet: " + e.toString());
                    shouldQuit = true;
                    break;
                }
            }
        } 
        catch (Exception e) 
        {
            System.err.println("Math client error: " + e.toString());
        }
    }

    /**
     * Method assists in getting two double values in for network processing.
     * @param s: a given aribitrary scanner that is used to make
     * @return String of formatted double values.
     */
    private static String numbers(Scanner s) {
        System.out.println("Please enter two numbers for math operation. ");
        Integer input;
        String change = " ";
        try {
            for(int i=0; i<2; i++)
                while ((input = s.nextInt()) != null){
                    change += input + " ";
                    input = null;
                    break;
                }
        }catch(InputMismatchException ime) {
            System.out.println("Wrong input type, please input number again. ");
        }
        return change;
    }
}