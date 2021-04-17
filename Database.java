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


public class Database {

    /**
     * The database file name.
     * <p>
     * Make sure the database file is in the root folder of the project if you only provide the name and extension.
     * <p>
     * Otherwise, you will need to provide an absolute path from your C: drive or a relative path from the folder this class is in.
     */
    private static String DATABASE = "test.db";

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
     * @param sql  a SQL statement that returns rows
     *             This query is written with the Statement class, typically
     *             used for static SQL SELECT statements
     */
    public static void sqlQuery(Connection conn, String sql) {
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
        System.out.println(conn);

        // display menu
    	System.out.println("Select Option:\n");
    	System.out.println("\t1. Search");
    	System.out.println("\t2. Add new records");
    	System.out.println("\t3. Order items");
    	System.out.println("\t4. Edit records");
    	System.out.println("\t5. Useful reports");

        Scanner in = new Scanner(System.in);
        int num = in.nextInt();

        try {
            switch (num) {
                case 1:
                    search(conn);
                    break;
                case 2:
                    addRecords(conn);
                    break;
                case 3:
                    break;
                case 4:
                    editRecords(conn);
                    break;
                case 5:
                    usefulReports(conn);
                    break;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    public static void search(Connection conn) throws SQLException {

        System.out.println("Select Search:\n");
        System.out.println("\t1. Search by artist");
        System.out.println("\t2. Search by track");

        Scanner in = new Scanner(System.in);
        int num = in.nextInt();
        in.nextLine();

        // create variables used for query creation
        PreparedStatement statement = null;
        ResultSet result = null;
        String query, select, from, where;

        switch (num) {

            case 1:

                // Search by artist
                System.out.println("Enter artist first name:");
                String fname = in.nextLine();
                System.out.println("Enter artist last name:");
                String lname = in.nextLine();

                select = "SELECT MEDIA.*, TRACKS.* ";
                from = "FROM MEDIA, TRACKS, PEOPLE ";
                where = "WHERE MEDIA.MediaID = TRACKS.TrackID AND TRACKS.Artist = PEOPLE.PersonID AND PEOPLE.Fname = ? AND PEOPLE.Lname = ?;";
                query = select + from + where;

                try {
                    statement = conn.prepareStatement(query);
                    statement.setString(1, fname);
                    statement.setString(2, lname);

                    result = statement.executeQuery();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;

            case 2:

                // Search by track
                System.out.println("Enter track title:");
                String track = in.nextLine();
                select = "SELECT MEDIA.*, TRACKS.* ";
                from = "FROM MEDIA, TRACKS ";
                where = "WHERE MEDIA.Title = ? AND MEDIA.MediaID = TRACKS.TrackID;";
                query = select + from + where;

                try {

                    statement = conn.prepareStatement(query);
                    statement.setString(1, track);
                    result = statement.executeQuery();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
        }

        // print result set
        try {
            ResultSetMetaData metadata = result.getMetaData();
            int column = metadata.getColumnCount();
            while (result.next()) {
                for (int i = 1; i <= column; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = result.getString(i);
                    System.out.print(columnValue + " " + metadata.getColumnName(i));
                }
                System.out.println("");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (result != null) {
                result.close();
                statement.close();
                conn.close();
            }
        }

        in.close();
    }

    public static void addRecords(Connection conn) throws SQLException {

        System.out.println("Select Add:\n");
        System.out.println("\t1. Add artist");
        System.out.println("\t2. Add AudioBook");

        Scanner in = new Scanner(System.in);
        int num = in.nextInt();
        in.nextLine();

        // create variables used for query creation
        PreparedStatement statement = null;
        ResultSet result = null;
        String query, insert, values;

        switch (num) {

            case 1:

                // Add artist
                System.out.println("Enter artist personID:");
                int personID = in.nextInt();
                in.nextLine();
                System.out.println("Enter artist first name:");
                String fname = in.nextLine();
                System.out.println("Enter artist last name:");
                String lname = in.nextLine();

                insert = "INSERT INTO PEOPLE ";
                values = "VALUES (? ? ?);";
                query = insert + values;

                try {
                    statement = conn.prepareStatement(query);
                    statement.setInt(1, personID);
                    statement.setString(2, fname);
                    statement.setString(3, lname);

                    result = statement.executeQuery();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;

            case 2:

                // Add AudioBook
                break;
        }

        // print result set
        try {
            ResultSetMetaData metadata = result.getMetaData();
            int column = metadata.getColumnCount();
            while (result.next()) {
                for (int i = 1; i <= column; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = result.getString(i);
                    System.out.print(columnValue + " " + metadata.getColumnName(i));
                }
                System.out.println("");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (result != null) {
                result.close();
                statement.close();
                conn.close();
            }
        }

        in.close();
    }

    public static void editRecords(Connection conn) throws SQLException {

    	Scanner in = new Scanner(System.in);
		PreparedStatement statement = null;
		ResultSet result = null;
		int index = 1;

		System.out.println("Enter artist first name:");
    	String Fname = in.nextLine();
		System.out.println("Enter artist last name:");
		String Lname = in.nextLine();

		String retrieveData = "SELECT * FROM PEOPLE WHERE Fname = ? and Lname = ?;";
		statement = conn.prepareStatement(retrieveData);
		statement.setString(1, Fname);
		statement.setString(2, Lname);
        result = statement.executeQuery();

        String oldPersonID = result.getString("PersonID");

		String query = "UPDATE PEOPLE SET PersonID = ?, Fname = ?, Lname = ? WHERE Fname = ? AND Lname = ?;";

		System.out.println("Enter new PersonID, or hit enter to skip:");
		String newPersonID = in.nextLine();
		System.out.println("Enter new first name, or hit enter to skip:");
		String newFname = in.nextLine();
		System.out.println("Enter new first name, or hit enter to skip:");
		String newLname = in.nextLine();

        statement = conn.prepareStatement(query);

        // Append Update
		if(!newPersonID.isEmpty()){
            statement.setString(1, newPersonID);
        } else {
            statement.setString(1, oldPersonID);
        }
        if(!newFname.isEmpty()){
            statement.setString(2, newFname);
        } else {
            statement.setString(2, Fname);
        }
        if(!newLname.isEmpty()){
            statement.setString(3, newLname);
        } else {
            statement.setString(3, Lname);
        }

        statement.setString(4, Fname);
        statement.setString(5, Lname);
        statement.executeUpdate();

	}

    public static void usefulReports(Connection conn) throws SQLException {

        System.out.println("Select report:\n");
        System.out.println("\t1. Track by ARTIST released before YEAR");
        System.out.println("\t2. Number of albums checked out by a single patron");
        System.out.println("\t3. Most popular actor in the database");
        System.out.println("\t4. Most listened to artist in the database");
        System.out.println("\t5. Patron who has checked out the most videos");

        Scanner in = new Scanner(System.in);
        int num = in.nextInt();
        in.nextLine();

        // create variables used for query creation
        PreparedStatement statement = null;
        ResultSet result = null;
        String query, select, from, where, group, order, limit;

        switch (num) {

            case 1:

                // Find the titles of all tracks by ARTIST released before YEAR
                System.out.println("Enter artist first name:");
                String fname = in.nextLine();
                System.out.println("Enter artist last name:");
                String lname = in.nextLine();
                System.out.println("Enter year:");
                int year = in.nextInt();

                select = "SELECT MEDIA.Title, CAST(STRFTIME('%Y', MEDIA.ReleaseDate) AS INT) AS Year ";
                from = "FROM MEDIA, TRACKS, PEOPLE ";
                where = "WHERE MEDIA.MediaID = TRACKS.TrackID AND TRACKS.Artist = PEOPLE.PersonID AND PEOPLE.Fname = ? AND PEOPLE.Lname = ? AND Year < ?;";
                query = select + from + where;

                try {
                    statement = conn.prepareStatement(query);
                    statement.setString(1, fname);
                    statement.setString(2, lname);
                    statement.setInt(3, year);

                    result = statement.executeQuery();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;

            case 2:

                // Find the total number of albums checked out by a single patron
                System.out.println("Enter patron email:");
                String email = in.nextLine();
                select = "SELECT COUNT(DISTINCT TRACKS.AlbumID) AS Albums ";
                from = "FROM MEDIABORROWED, MEDIA, TRACKS ";
                where = "WHERE MEDIABORROWED.CheckedOutBy = ? AND MEDIABORROWED.MediaID = MEDIA.MediaID AND MEDIA.MediaID = TRACKS.TrackID;";
                query = select + from + where;

                try {

                    statement = conn.prepareStatement(query);
                    statement.setString(1, email);
                    result = statement.executeQuery();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;

            case 3:

                // Most popular actor in the database
                select = "SELECT PEOPLE.Fname, PEOPLE.Lname, COUNT(PEOPLE.PersonID) as Occurance ";
                from = "FROM MEDIABORROWED, MEDIA, MOVIES, PEOPLE ";
                where = "WHERE MEDIABORROWED.MediaID = MEDIA.MediaID AND MEDIA.MediaID = MOVIES.MovieID AND MOVIES.Actor = PEOPLE.PersonID ";
                group = "GROUP BY PEOPLE.PersonID ";
                order = "ORDER BY Occurance DESC ";
                limit = "LIMIT 1;";
                query = select + from + where + group + order + limit;

                try {

                    statement = conn.prepareStatement(query);
                    result = statement.executeQuery();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;

            case 4:

            	// Most listened to artist in the database
				break;

            case 5:

                // Patron who has checked out the most videos
                select = "SELECT PATRON.*, COUNT(PATRON.Email) as Videos";
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
            int column = metadata.getColumnCount();
            while (result.next()) {
                for (int i = 1; i <= column; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = result.getString(i);
                    System.out.print(columnValue + " " + metadata.getColumnName(i));
                }
                System.out.println("");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (result != null) {
                result.close();
                statement.close();
                conn.close();
            }
        }

        in.close();
    }
}



