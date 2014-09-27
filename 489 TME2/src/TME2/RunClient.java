// RunClient.java - Michael Wong 3101126
// This class connects to the server class in order to make changes to the database. This class has a thread that listens 
// on a port for incoming connections and can also connect to other clients to download files. Each client starts with a random 
// port so that multiple versions of this class can be run and listen on different ports at the same time. A more complex 
// method of checking for free ports can be used but Math.random is sufficient for this testing purposes. 

// Please make sure you run 'start orbd -ORBInitialPort 1050' in addition to the runserver class before running this.
// To run, add run configurations: -ORBInitialPort 1050 -ORBInitialHost localhost

package TME2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class RunClient {
	public static void main(String[] args) {
			
		try {
			ORB orb = ORB.init(args, null);
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			Server serverobj = (Server) ServerHelper.narrow(ncRef.resolve_str("Server"));	
			
			String clientName = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Client ID: " + clientName);
			
			// Set port to random for testing with 2 clients on same ip 
			int port = (int) Math.round(8000+Math.random()*100);
			System.out.println("Port: "+port);
			
			new RunClient().socketThread(port);
			
			Scanner c;
			// Get user input until user specifies to exit
			for (;;) {
				c = new Scanner(System.in);
				System.out.println("[1] - Register a file");
				System.out.println("[2] - Unregister a file");
				System.out.println("[3] - Search for a file");
				System.out.println("[4] - Exit");
				String a = c.nextLine();
				if (a.equals("4")) {
					System.out.println("Exiting");
					break;
				}
				else if (a.equals("1")) {
					System.out.println("File to add: ");
					Scanner c1 = new Scanner(System.in);
					String a1 = c1.nextLine();
					serverobj.registerFile(a1, clientName, Integer.toString(port));
					System.out.println("File added");
				}
				else if (a.equals("2")) {
					System.out.println("File to delete: ");
					Scanner c1 = new Scanner(System.in);
					String a1 = c1.nextLine();
					serverobj.removeFile(a1, clientName);
					System.out.println("File deleted");
				}
				else if (a.equals("3")) {
					System.out.println("File to search for: ");
					Scanner c1 = new Scanner(System.in);
					String a1 = c1.nextLine();
					if (serverobj.findFile(a1)) {
						System.out.println("File Found. Do you want to download this file? (y/n)");
						a = c1.nextLine();
						if (a.equals("y")){
							String seed = serverobj.getFile(a1);
							System.out.println(seed);
							
							// Get target ip and port
							String[] address = seed.split(":"); 
							String ip = address[0];
							int portNumber = Integer.parseInt(address[1]);

					        try {
					        	// Create new socket
					            Socket proxySocket = new Socket(ip, portNumber);
					            	
					            PrintWriter out = new PrintWriter(proxySocket.getOutputStream(), true);
					            BufferedReader in = new BufferedReader( new InputStreamReader(proxySocket.getInputStream()));
					            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
					            out.println(a1);
					            
					            String fromServer;
					            while ((fromServer = in.readLine()) != null) {
					                // Read from server
					                System.out.println("Server: " + fromServer);
					                 
					            }
					        } catch (Exception e) {
					            System.out.println(e);
					        }
						}
					}
					else {
						System.out.println("File not found");
					}
				}
				System.out.println("\n");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void socketThread (final int port) {
        final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);

        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(port);
                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        clientProcessingPool.submit(new ClientTask(clientSocket));
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        };
        Thread serverThread = new Thread(serverTask);
        serverThread.start();
    }
	
	// Listen on socket and run protocol when client connects
    private class ClientTask implements Runnable {

        private final Socket clientSocket;
        private ClientTask(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() { 
        	System.out.println("Got a client!");
        	try {
        		// Receive filename from client
        		DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
        		BufferedReader d = new BufferedReader(new InputStreamReader(dis));
        		String fileName = d.readLine();

        		// Run protocol to read in the file to buffer
                System.out.println(fileName);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);          
                Protocol p = new Protocol();
                String output = p.readFile(fileName);
                System.out.println(output);
                
                // Send file contents to socket
                out.println(output);

            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}

class Protocol {	
	// Read file into buffer
	String readFile(String file) {
		System.out.println("Reading file: "+file);
	    String theOutput = "";
	    try {
			FileReader in = new FileReader(file);
		    BufferedReader br = new BufferedReader(in);
		    
		    String line;

		    while ((line = br.readLine()) != null) {
		        theOutput = theOutput + line;
		    }
		    in.close();
	    } catch (Exception e) {
	    	System.out.println(e);
	    }
	    return theOutput;
	}
}