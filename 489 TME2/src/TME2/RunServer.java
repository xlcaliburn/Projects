// RunServer.java - Michael Wong 3101126
// This class maintains an active connection to the mysql server and the clients connect to this class
// and call the respective methods to execute queries on the database.

// Please make sure you run 'start orbd -ORBInitialPort 1050' before you run this.
// To run, add run configurations: -ORBInitialPort 1050 -ORBInitialHost localhost


package TME2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

class ServerImpl extends ServerPOA {
	private ORB orb;
	private static Connection dbConnection;	
	
	// Constructor
	public ServerImpl(Connection in) { 
		super(); 
		dbConnection = in;	
	}
	
	public void setORB(ORB orb_val) {
		orb = orb_val;
	}
	
	// Boolean check for file existence in database
	public boolean findFile(String filename) {
		try {
			// Create a statement and execute the SQL query
			Statement s = dbConnection.createStatement();
			ResultSet results = s.executeQuery("SELECT filename, user from test");
	
			// Iterate through results
			while (results.next()) {
				String file = results.getString("filename");
				String user = results.getString("user");
				if (file.equals(filename)) {
					System.out.println("File Found");
					return true;
				}
			}	
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("File Not Found");		
		return false;
	}

	// Retrieve file owner's ip
	public String getFile(String filename) {
		try {
			System.out.println(filename);
			// Create a statement and execute the SQL query
			Statement s = dbConnection.createStatement();
			ResultSet results = s.executeQuery("SELECT filename, user, port from test");
	
			// Iterate through results
			while (results.next()) {
				String file = results.getString("filename");
				String user = results.getString("user");
				String port = results.getString("port");
				if (file.equals(filename)) {
					return user+":"+port;
				}
			}	
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("Error: File not found.");
		return null;
	}

	// Register the file with the client name onto the database
	public void registerFile(String filename, String clientname, String port) {
		try {
			// Create a statement and execute the SQL query
			Statement s = dbConnection.createStatement();
			String query = "INSERT INTO test (filename,user,port) VALUES (\"" + filename+ "\",\"" 
					+ clientname + "\",\"" + port + "\");";
			s.executeUpdate(query);
			System.out.println("Executed");
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	// Remove a file from the database. A simple check is done to confirm that the owner is the same
	// A more elaborate identity check could be done as a future implementation.
	public void removeFile(String filename, String clientname) {
		try {
			// Create a statement and execute the SQL query
			Statement s = dbConnection.createStatement();
			String query = "SELECT id, filename, user from test";
			ResultSet results = s.executeQuery(query);
	
			// Iterate through results
			while (results.next()) {
				String id = results.getString("id");
				String file = results.getString("filename");
				String user = results.getString("user");
				if (file.equals(filename)) {
					// Check if client name matches registered clientname
					if (user.equals(clientname)) {
						// Remove file from database
						query = "DELETE FROM test where ID = " + id;
						s.executeUpdate(query);
						System.out.println("File deleted");
					}
					else {
						System.out.println("Failure to delete file: You are not the owner.");
					}
					return;
				}
				System.out.println("File not found");
			}		
		} catch (Exception e) {
			System.out.println(e);
		}
	}	
}

public class RunServer {
	// Database login information
	private final static String JDBC_URL = "jdbc:mysql://localhost/comp489";
	private final static String JDBC_USER = "comp489";
	private final static String JDBC_PSW = "abc123";
	
	public static void main(String args[]) {
		try {
			// Connect to database
	    	Class.forName("com.mysql.jdbc.Driver");		
			Connection dbConnection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PSW);
			
			System.out.println("Initializing ORB");
			ORB orb = ORB.init(args, null);
			
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();
			
			ServerImpl serverImpl = new ServerImpl(dbConnection);
			serverImpl.setORB(orb); 
			
			ServerPOATie tie = new ServerPOATie(serverImpl, rootpoa);
			Server href = tie._this(orb);
			
			// Get naming context
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			
			String name = "Server";
			NameComponent path[] = ncRef.to_name( name );
			ncRef.rebind(path, href);
			
			System.out.println("Server started and connected to database");			
			
			for (;;) {
				orb.run();
			}			
    	} catch (Exception e) {
    		System.out.println(e);
    	}
	}
}
