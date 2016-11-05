import java.io.*;
import java.net.*;

public class Client {
	
	static Integer port = 12321;
	static DatagramSocket socket;
	static Socket serverSocketTCP;
	static String serverName;

	public static void main(String... args) {
		
		// read server name, catch if there is no specified server
		try {
			serverName = args[0];
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// if a port is specified (optional), else default is port 10000
		if (args.length > 1) {
			port = Integer.parseInt(args[1]);
		} 
		
		try {
			// let server get setup, in case someone starts both at almost the same time
			Thread.sleep(3000);
			
			//results stored in format results[receiveSize][sendSize]
			
			for (int i = 0; i < 5; i++) {
				if (i != 3) {
					int receiveWindow = windowSize(i);
					long timeTaken = sendBytesTCP(10_000_000, true, receiveWindow);
					System.out.println("Time to send 10MB with receive window " + receiveWindow 
							+ ": " + timeTaken + "ms");
				}
			}
			
			for (int i = 0; i < 5; i++) {
				if (i != 3) {
					int sendWindow = windowSize(i);
					long timeTaken = sendBytesTCP(10_000_000, false, sendWindow);
					System.out.println("Time to send 10MB with send window " + sendWindow 
							+ ": " + timeTaken + "ms");
				}
			}
//
//			for (int i = 0; i < 5; i++) {
//				if (i != 3) {
//					int receiveWindow = windowSize(i);
//					for (int j = 0; j < 5; j++) {
//						if (j != 3) {
//							int sendWindow = windowSize(j);
//							double timeInSeconds = (double)results[i][j] / 1000.0;
//							System.out.println("Time to send 10MB with receive window " + receiveWindow 
//									+ " and send window " + sendWindow + ": " + timeInSeconds + " seconds");
//						}
//					}
//				}
//			}
//			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static long sendBytesTCP(int length, boolean receiveWindow, int windowSize) {
		
		// measure elapsed time of process
		long startTime = System.currentTimeMillis();
		Socket serverSocketTCP;
		
		try {
			
			// open our socket in exterior try/catch (so we will certainly close it if opened)
			serverSocketTCP = new Socket(serverName, port);
			if (receiveWindow) {
				serverSocketTCP.setReceiveBufferSize(windowSize);
			}
			else {
				serverSocketTCP.setSendBufferSize(windowSize);
			}
			
			// establish connection to server and send length bytes
			try {
				byte[] byteArray = new byte[length];
				final OutputStream toClient = serverSocketTCP.getOutputStream();
			    final DataOutputStream dos = new DataOutputStream(toClient);
			    
			    dos.writeInt(byteArray.length);
			    dos.write(byteArray);


			} catch (Exception e) {
		    	e.printStackTrace();
		    }
		    
			// since we always must close our socket, use separate try/catch
		    serverSocketTCP.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();;
		
		return endTime - startTime;
	}
	
	public static int windowSize(int i) {
		int window = 10;
		for (int mult = i; mult >=0; mult--) {
			window *= 10;
		}
		return window;
	}
	
}
