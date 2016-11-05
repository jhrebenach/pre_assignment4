import java.io.*;
import java.net.*;

public class Server {
	
	static Integer port = 12321;
	static DatagramSocket socket;
	static ServerSocket serverSocket;
//	static String clientName;
	static DatagramSocket socketUDP;
	
	public static void main(String... args) {
		

		// if a port is specified (optional), else default is port 10000
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} 
		
		try {
			// listen on specified port
			serverSocket = new ServerSocket(port);
			
			
			for (int i = 0; i < 5; i++) {
				if (i != 3) {
					int receiveWindow = windowSize(i);
					acceptBytesTCP(10_000_000, true, receiveWindow);
				}
			}
			
			for (int i = 0; i < 5; i++) {
				if (i != 3) {
					int receiveWindow = windowSize(i);
					acceptBytesTCP(10_000_000, false, receiveWindow);
				}
			}
			
//			for (int i = 0; i < 5; i++) {
//				if (i != 3) {
//					for (int j = 0; j < 5; j++) {
//						if (j != 3) {
//							System.out.println(windowSize(i));
//							acceptBytesTCP(10_000_000, windowSize(i), windowSize(j));	
//						}
//					}
//				}
//			}
			serverSocket.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void acceptBytesTCP(int length, boolean receiveWindow, int windowSize) {

		try {
			// initialize socket to client and input/output streams
			Socket clientSocket = serverSocket.accept();
			if (receiveWindow) {
				clientSocket.setReceiveBufferSize(windowSize);
			}
			else {
				clientSocket.setSendBufferSize(windowSize);
			}
			
			InputStream fromClient = clientSocket.getInputStream();
		    
		    
			// accept sent bytes and return them
			byte[] reply = new byte[10_000_000];

			while ( fromClient.read(reply) != -1) {
//				toClient.write(reply, 0, bytes_read);
//				toClient.flush();
			}
			
			// close the socket
			clientSocket.close();
	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int windowSize(int i) {
		int window = 10;
		for (int mult = i; mult >=0; mult--) {
			window *= 10;
		}
		return window;
	}

}
