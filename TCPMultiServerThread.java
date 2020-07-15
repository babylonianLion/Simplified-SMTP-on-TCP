/*
 * Server App upon SMTP
 * A thread is started to handle every client TCP connection to this server
 * Name: Hussain Al Zerjawi
 * Assignment: HW04
 * Due date: 02/29/2020
 */ 

import java.net.*;
import java.io.*;


public class TCPMultiServerThread extends Thread {
	private Socket clientTCPSocket = null;

	public TCPMultiServerThread(Socket socket) {
		super("TCPMultiServerThread");
		clientTCPSocket = socket;
	}


	public void run() {
		try{
			PrintWriter cSocketOut = new PrintWriter(clientTCPSocket.getOutputStream(), true);
			BufferedReader cSocketIn = new BufferedReader(new InputStreamReader(clientTCPSocket.getInputStream()));
			cSocketOut.println("220 cs3700a.msudenver.edu");
		}catch (IOException e) {
			e.printStackTrace();
		}
		outer :while(true){
			try {
				PrintWriter cSocketOut = new PrintWriter(clientTCPSocket.getOutputStream(), true);
				BufferedReader cSocketIn = new BufferedReader(new InputStreamReader(clientTCPSocket.getInputStream()));
				int line = 0;
				String fromClient;
				String[] request = new String[1000];


				cSocketIn.mark(1000);
				if(cSocketIn.readLine() == "QUIT"){
					cSocketOut.println("221 <cs3700a.msudenver.edu> closing connection");
					cSocketOut.close();
					cSocketIn.close();
					clientTCPSocket.close();
					//System.exit(0);
					break;
				}
				cSocketIn.reset();
				// Part 3) a) and b)
				String HELO;
				while(true){
					HELO = cSocketIn.readLine();
					if(HELO.contains("HELO")){
						System.out.println(HELO);
						cSocketOut.println("250 cs3700a.msudenver.edu Hello cs3700b.msudenver.edu");
						break;
					}
					else{
						String noHELO = "503 5.5.2 Send hello first";
						cSocketOut.println(noHELO);
					}
				}

				// Part 3) c) and d)
				String FROM;
				while(true){
					FROM = cSocketIn.readLine();
					if(FROM.contains("MAIL FROM")){
						System.out.println(FROM);
						cSocketOut.println("250 2.1.0 Sender OK");
						break;
					}
					else{
						String noFROM = "503 5.5.2 Need mail command";
						cSocketOut.println(noFROM);
					}
				}

				// Part 3) e) and f)
				String RCPT;
				while(true){
					RCPT = cSocketIn.readLine();
					if(RCPT.contains("RCPT TO")){
						System.out.println(RCPT);
						cSocketOut.println("250 2.1.5 Recipient OK");
						break;
					}
					else{
						String noRCPT = "503 5.5.2 Need rcpt command";
						cSocketOut.println(noRCPT);
					}
				}

				// Part 3) g) and h)
				String DATA;
				while(true){
					DATA = cSocketIn.readLine();
					if(DATA.contains("DATA")){
						System.out.println(DATA);
						cSocketOut.println("354 Start mail input; end with <CRLF>.<CRLF>");
						break;
					}
					else{
						String noDATA = "503 5.5.2 Need data command";
						cSocketOut.println(noDATA);
					}
				}

				cSocketIn.mark(1000);
				while ((fromClient = cSocketIn.readLine()) != null) {

					if (fromClient.equals(".")) {
						System.out.print("\r\n");
						cSocketOut.println("250 Message received and to be delivered");
						break;
					} else {
						System.out.println(fromClient);
						request[line] = fromClient;
					}
					line++;
				}
				cSocketIn.reset();
				// Part 4 and 5)
				String QUIT;
				while((QUIT = cSocketIn.readLine()) != null){
					if(QUIT.contains("QUIT")){
						cSocketOut.println("221 <cs3700a.msudenver.edu> closing connection");
						cSocketOut.close();
						cSocketIn.close();
						clientTCPSocket.close();
						break outer;
						//System.exit(0);
					}
					else if (QUIT.contains("Yes")){
						break;
					}
				}

			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}