import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


//basic, general use client for network programming.
public class MovieClient {
	public static void main(String[] args) {
		 System.out.println("Echo Client");
		 try {
			 System.out.println("Waiting for connection.....");
			 InetAddress localAddress = InetAddress.getLocalHost();
			 try {
				 	Socket clientSocket = new Socket(localAddress, 6000);
				 	BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				 	PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				 	System.out.println("Connected to server");
				 	Scanner scanner = new Scanner(System.in);
				 	while (true) {
				 		//wait for response
				 		while(!br.ready());
				 
				 		//collects output from server
				 		String output = "";
				 		while(br.ready()){
				 			output += (br.readLine() + "\n");
				 		}
				 		System.out.println(output);
			
				 		//collects input and quits if needed
				 		String inputLine = scanner.nextLine();
				 		if ("quit".equalsIgnoreCase(inputLine))
				 			break;
				 		out.println(inputLine);
				 	}
				 	scanner.close();
				 	clientSocket.close();
			 } 
			 catch (IOException ex) {
				 System.out.println(ex.getMessage());
			 }
		 } 
		 catch (IOException ex) {
			 System.out.println(ex.getMessage());
		 }
	}
}