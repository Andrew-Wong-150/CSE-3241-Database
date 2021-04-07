package osu.cse3241;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class Checkpoint_4 {
    
	/**
	 *  The database file name.
	 *  
	 *  Make sure the database file is in the root folder of the project if you only provide the name and extension.
	 *  
	 *  Otherwise, you will need to provide an absolute path from your C: drive or a relative path from the folder this class is in.
	 */
	private static String DATABASE = "ProjCheckpoint4.db";
	
    /**
     * Connects to the database if it exists, creates it if it does not, and returns the connection object.
     * 
     * @param databaseFileName the database file name
     * @return a connection object to the designated database
     */
    public static Connection initializeDB(String databaseFileName) {
    	/**
    	 * The "Connection String" or "Connection URL".
    	 * 
    	 * "jdbc:sqlite:" is the "subprotocol".
    	 * (If this were a SQL Server database it would be "jdbc:sqlserver:".)
    	 */
        String url = "jdbc:sqlite:" + databaseFileName;
        Connection conn = null; // If you create this variable inside the Try block it will be out of scope
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
            	// Provides some positive assurance the connection and/or creation was successful.
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("The connection to the database was successful.");
            } else {
            	// Provides some feedback in case the connection failed but did not throw an exception.
            	System.out.println("Null Connection");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("There was a problem connecting to the database.");
        }
        return conn;
    }
    
    /**
     * Queries the database and prints the results.
     * 
     * @param conn a connection object
     * @param sql a SQL statement that returns rows
     * This query is written with the Statement class, tipically 
     * used for static SQL SELECT statements
     */
    public static void sqlQuery(Connection conn, String sql){
        try {
        	Statement stmt = conn.createStatement();
        	ResultSet rs = stmt.executeQuery(sql);
        	ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();
        	for (int i = 1; i <= columnCount; i++) {
        		String value = rsmd.getColumnName(i);
        		System.out.print(value);
        		if (i < columnCount) System.out.print(",  ");
        	}
			System.out.print("\n");
        	while (rs.next()) {
        		for (int i = 1; i <= columnCount; i++) {
        			String columnValue = rs.getString(i);
            		System.out.print(columnValue);
            		if (i < columnCount) System.out.print(",  ");
        		}
    			System.out.print("\n");
        	}
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    
    public static void main(String[] args) {
    	Connection conn = initializeDB(DATABASE);
    	
    	// display menu
    	System.out.println("Select Option:");
    	System.out.println("\t.1 Search\n");
    	System.out.println("\t.2 Add new records\n");
    	System.out.println("\t.3 Order items\n");
    	System.out.println("\t.4 Edit records\n");
    	System.out.println("\t.5 Useful reports\n");
    	
    	Scanner in = new Scanner(System.in);
    	int num = in.nextInt();
    	
    	switch(num) {
    	
    	case 1:
    		//TODO: ADD FUNCTION CALL HERE
    		break;
    	case 2:
    		//TODO: ADD FUNCTION CALL HERE
    		break;
    	case 3:
    		//TODO: ADD FUNCTION CALL HERE
    		break;
    	case 4:
    		editRecords(conn);
    		break;
    	case 5:
    		usefulReports(conn);
    		break;
    	}
    }


	public static void editRecords(Connection conn) {
		
		Scanner in = new Scanner(System.in);
    	PreparedStatement statement;
    	String query = "UPDATE ARTISTS SET";
    	int result = 0;
    	
		// prompt for new data
		System.out.println("Enter new PersonID, or hit enter to keep current: ");
		String newPersonID = in.nextLine();
		System.out.println("Enter new First Name, or hit enter to keep current: ");
		String newFname = in.nextLine();
		System.out.println("Enter new Last Name, or hit enter to keep current: ");
		String newLname = in.nextLine();
		System.out.println("Enter new Role, or hit enter to keep current: ");
		String newRole = in.nextLine();
		
		// create prepared statement
		if(!newPersonID.isEmpty()) {
			query += String.format(" personID = %s,", newPersonID);
		}
		if(!newFname.isEmpty()) {
			query += String.format(" Fname = %s,", newFname);
		}
		if(!newLname.isEmpty()) {
			query += String.format(" Lname = %s,", newLname);
		}
		if(!newRole.isEmpty()) {
			query += String.format(" Role = %s,", newRole);
		}
		
		query = query.substring(0, query.length() - 1);
    	
		// prompt for search method
		System.out.println("Search by:\n");
		System.out.println("\t1. Full Name\n");
		System.out.println("\t2. PersonID\n");
		
    	int num = in.nextInt();
    	in.nextLine();
		
		switch(num) {
		
		case 1:
			
			// get first and last name to search by
			System.out.println("\nEnter Frist Name: ");
			String fname = in.nextLine();
			System.out.println("\nEnter Frist Name: ");
			String lname = in.nextLine();
			
			query += String.format(" WHERE Fname = %s, Lname = %s;", fname, lname);
			
			try {
				statement = conn.prepareStatement(query);
				result = statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			break;
			
		case 2:
			
			// get personID to search by
			System.out.println("\nEnter PersonID: ");
			String id = in.nextLine();
			
			query += String.format(" WHERE PersonID = %s;", id);
			
			try {
				statement = conn.prepareStatement(query);
				result = statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			break;
		
		}
		
		in.close();
	}
	
	public static void usefulReports(Connection conn) {
		
		System.out.println("Select report:\n");
		System.out.println("\t1. Track by ARTIST released before YEAR\n");
		System.out.println("\t2. Number of albums checked out by a single patron\n");
		System.out.println("\t3. Most popular actor in the database\n");
		System.out.println("\t4. Most listened to artist in the database\n");
		System.out.println("\t5. Patron who has checked out the most videos\n");
		
		Scanner in = new Scanner(System.in);
    	int num = in.nextInt();
    	in.nextLine();
    	
    	// create variables used for query creation
    	PreparedStatement statement;
    	String query, select, from, where, group, order, limit;
    	ResultSet result = null;
		
    	switch(num) {

    	case 1:
    		
    		// useful report 1
    		System.out.println("Enter artist:");
    		String artist = in.nextLine();
    		System.out.println("Enter year:");
    		int year = in.nextInt();
    		
    		select = "SELECT ALBUMS.* ";
    		from = "FROM ALBUMS ";
    		where  = "WHERE ALBUMS.Artist = %s AND year(ALBUMS.ReleaseDate) < %i;";
    		query = select + from + String.format(where, artist, year);
    		
    		try {
				statement = conn.prepareStatement(query);
				result = statement.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	
			break;
    	
    	case 2:
    		
    		// useful report 2
    		System.out.println("Enter patron email:");
    		String email = in.nextLine();
    		select = "SELECT COUNT(MEDIA.MediaID) ";
    		from = "FROM MEDIABORROWED, MEDIA, ALBUM ";
    		where = "WHERE MEDIABORROWED.CheckedOutBy = %s AND MEDIABORROWED.MediaID = MEDIA.MediaID AND MEDIA.MediaID = ALBUMS.AlbumID;";
    		query = select + from + String.format(where, email);
    		
			try {
				statement = conn.prepareStatement(query);
				result = statement.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	
			break;
		
    	case 5:
    		
    		// useful report 5
    		select = "SELECT PATRON.*, COUNT(PATRON.Email) as Items";
    		from = "FROM PATRON, MEDIABORROWED, MEDIA, MOVIES ";
    		where = "WHERE MEDIABORROWED.CheckedOutBy = PATRON.Email AND MEDIABORROWED.MediaID = MEDIA.MediaID AND MEDIA.MediaID = MOVIES.MOVIESID ";
    		group = "GROUP BY PATRON.Email ";
    		order = "ORDER BY Items DESC ";
    		limit = "LIMIT 1;";
    		
    		query = select + from + where + group + order + limit;
			
    		try {
				statement = conn.prepareStatement(query);
				result = statement.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	
			break;
    	}
    	
    	// print result set
    	try {
	    	ResultSetMetaData metadata = result.getMetaData();
	    	int colnum = metadata.getColumnCount();
	    	while (result.next()) {
	    	    for (int i = 1; i <= colnum; i++) {
	    	        if (i > 1) System.out.print(",  ");
	    	        String columnValue = result.getString(i);
	    	        System.out.print(columnValue + " " + metadata.getColumnName(i));
	    	    }
	    	}
	    	    System.out.println("");
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	
    	in.close();
	}
}



