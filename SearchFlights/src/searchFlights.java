// Search Flights Assignment - Michael Wong 
// To run through command line, simply compile with javac searchFlights.java
// then run with 4 parameters: java searchFlights -o abc -d xyz

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;


public class searchFlights {
	public static void main (String args[]) {
		final String originflag = "-o";
		final String destinationflag = "-d";
		final String inFile1 = "Provider1.txt";
		final String inFile2 = "Provider2.txt";
		final String inFile3 = "Provider3.txt";
		
		// Input argument length check
		if (args.length != 4) {
			 throw new IllegalArgumentException("Not a valid argument");
		}
		
		// Check for origin flag
		if (!args[0].equals(originflag)) {
			 throw new IllegalArgumentException("Missing origin flag");
		}
		
		// Check for destination flag
		if (!args[2].equals(destinationflag)) {
			 throw new IllegalArgumentException("Missing destination flag");
		}
		
		// Additional checks for arrival and destination can be done here
		
		String inputOrigin = args[1];
		String inputDestination = args[3];
		
		try {
			// Create a tree set to sort the objects; this data structure does not allow for duplicates on insert
			TreeSet<Flights> flightArray = new TreeSet<Flights>();
			
			// For simplicity, the textfiles are placed in a loop with the assumption that there are no "|" or "-" 
			// in the input text files. Additional checks could be done depending on the situation or could be simply 
			// broken down into three separate loops for the files. 
			String[] inputFiles = new String[]{inFile1, inFile2, inFile3};
			for (String i : inputFiles) {
				BufferedReader reader = new BufferedReader(new FileReader(i));
				String line = reader.readLine(); // Ignore first line of textfile, a check can be done here
				while ((line = reader.readLine()) != null) {
					String lineModified = line.replaceAll("-", "/"); // Replace all hyphens from Provider2
					lineModified = lineModified.replaceAll("\\|", ","); // Replace all straight lines from Provider3
				    String[] tempSplit = lineModified.split(",");
				    
				    // Format entries and create a 'Flight' object
				    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					Date depDate = formatter.parse(tempSplit[1]);
					Date arrDate = formatter.parse(tempSplit[3]);
					int price = Integer.parseInt(tempSplit[4].replaceAll("\\D+",""));
					
				    flightArray.add(new Flights(tempSplit[0], depDate, tempSplit[2], arrDate, price));
				}
				reader.close();
			}
			
			// Iterate through tree structure to find desired values 
			Iterator<Flights> it = flightArray.iterator();
			Flights current = null;
			while(it.hasNext() ) {
				current = it.next();
				//System.out.println(current.toString());
				if (inputOrigin.equals(current.origin) && inputDestination.equals(current.destination)) {
					System.out.println(current.toString());
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	} // End main
	
	public static class Flights implements Comparable<Flights> {
		private String origin;
		private Date departureDate;
		private String destination;
		private Date destinationDate;
		private int price;
		
		// Constructor
		public Flights(String o, Date dpD, String d, Date dtD, int p ) {
			this.origin = o;
			this.departureDate = dpD;
			this.destination = d;
			this.destinationDate = dtD;
			this.price = p;
		}

		public String toString() {
			// Convert date to default format
			SimpleDateFormat defaultFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			
			// Add decimal back into price
			String stringPrice = Integer.toString(price);
			String newstringPrice = stringPrice.substring(0,stringPrice.length()-2)+"."
					+stringPrice.substring(stringPrice.length()-2,stringPrice.length());
			
			// Currency symbol was not passed into this object but if international currencies are used for this, 
			// it can be passed into here as another field
			return origin + " --> " + destination +" ("+ defaultFormat.format(departureDate) + " --> " + 
				defaultFormat.format(destinationDate) + ") - $" + newstringPrice;
		} 

		public int compareTo(Flights f2) {
			// In comparing this object and another flight f2, compare price and if equal, compare destinationdate
			int priceCompare = price - f2.price;
			if (priceCompare != 0) {
				return priceCompare;
			}
			else {
				return destinationDate.compareTo(f2.destinationDate);
	    	}
		}
	} // End class Flights
} // End class searchFlights