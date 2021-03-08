import java.io.*;
import java.net.*;

/*
 * Client App upon SMTP
 * Name: Hussain Al Zerjawi
 * Assignment: HW04
 * Due date: 02/29/2020
 */ 
public class client {



	public static void main(String[] args) throws Exception {
		Socket tcpSocket = null;
		PrintWriter socketOut = null;
		BufferedReader socketIn = null;
		String repeat = "Yes";
		long startTime = 0;
		long endTime = 0;
		long RTT = 0;


		BufferedReader systemInput = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please type in the DNS name or IP address of the Server machine.");
		String serverMachine = systemInput.readLine();

		try {
			startTime = System.currentTimeMillis();
			tcpSocket = new Socket(serverMachine, 5010);
			socketOut = new PrintWriter(tcpSocket.getOutputStream(), true);
			socketIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
			String firstResponse = socketIn.readLine();
			System.out.println(firstResponse);
			endTime = System.currentTimeMillis();
			RTT = endTime - startTime; 
			System.out.println("RTT of establishing this TCP connection: " + RTT + " ms");
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + serverMachine);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: "
					+ serverMachine);
			System.exit(1);
		}
		while (repeat.contentEquals("Yes") || repeat.contentEquals("yes")) {
			String emailContent = "";
			String contentLine = "";
			socketOut = new PrintWriter(tcpSocket.getOutputStream(), true);
			socketIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));

			System.out.println("Please type in the sender's email address.");
			String senderEmail = systemInput.readLine();
			System.out.println("Please type in the receiver's email address.");
			String receiverEmail = systemInput.readLine();
			System.out.println("Please type in the subject.");
			String subject = systemInput.readLine();
			System.out.println("Please type in the email content (Single'.' on a line indicates end of content).");
			while(true){
				contentLine = systemInput.readLine();
				if(contentLine.contentEquals(".")){
					emailContent = emailContent + contentLine + "\r\n";
					break;
				}
				emailContent = emailContent + contentLine + "\r\n";
			}
			// Step 4) a)
			startTime = System.currentTimeMillis();
			socketOut.println("HELO " + serverMachine);
			String secondResponse = socketIn.readLine();
			endTime = System.currentTimeMillis();
			RTT = endTime - startTime;
			System.out.println("The RTT query: " + RTT + " ms");
			System.out.println(secondResponse);

			// Step 4) b)
			startTime = System.currentTimeMillis();
			socketOut.println("MAIL FROM: " + senderEmail);
			String thirdResponse = socketIn.readLine();
			endTime = System.currentTimeMillis();
			RTT = endTime - startTime;
			System.out.println("The RTT query: " + RTT + " ms");
			System.out.println(thirdResponse);

			// Step 4) c)
			startTime = System.currentTimeMillis();
			socketOut.println("RCPT TO: " + receiverEmail);
			String fourthResponse = socketIn.readLine();
			endTime = System.currentTimeMillis();
			RTT = endTime - startTime;
			System.out.println("The RTT query: " + RTT + " ms");
			System.out.println(fourthResponse);

			// Step 4) d)
			startTime = System.currentTimeMillis();
			socketOut.println("DATA");
			String fifthResponse = socketIn.readLine();
			endTime = System.currentTimeMillis();
			RTT = endTime - startTime;
			System.out.println("The RTT query: " + RTT + " ms");
			System.out.println(fifthResponse);

			// Step 4) e)
			startTime = System.currentTimeMillis();
			socketOut.println("To: " + receiverEmail + "\r\n" + "From: " + senderEmail + "\r\n" + "Subject: " + subject + "\r\n\r\n" + emailContent);
			String sixthResponse = socketIn.readLine();
			endTime = System.currentTimeMillis();
			RTT = endTime - startTime;
			System.out.println("The RTT query: " + RTT + " ms");
			System.out.println(sixthResponse);


			System.out.println("Would you like to continue? Please type in either yes or no or quit.");
			repeat = systemInput.readLine();

			if (repeat.contentEquals("QUIT") || repeat.contentEquals("quit") || repeat.contentEquals("Quit") || repeat.contentEquals("no") || repeat.contentEquals("No")) {
				socketOut.println("QUIT");
				String seventhResponse = socketIn.readLine();
				System.out.println(seventhResponse);
				socketOut.close();
				socketIn.close();
				systemInput.close();
				tcpSocket.close();
				System.exit(0);
				break;

			}
			else{
				socketOut.println("Yes");
			}
		}
	}
}
