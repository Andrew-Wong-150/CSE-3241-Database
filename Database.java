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

    private static String DATABASE = "test.db";

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

    public static void displayResultSet(ResultSet result, Statement statement, Connection conn) throws SQLException {

        try {
            ResultSetMetaData metadata = result.getMetaData();
            int column = metadata.getColumnCount();
            for (int i = 1; i <= column; i++) {
                System.out.print(metadata.getColumnName(i) + "\t");
            }
            System.out.println("\n");
            while (result.next()) {
                for (int i = 1; i <= column; i++) {
                    String columnValue = result.getString(i);
                    System.out.print(columnValue + "\t");
                }
            }
            System.out.println("\n");
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (result != null) {
                result.close();
                statement.close();
            }
        }

    }

    public static int checkOrAddPerson(Connection conn, String fname, String lname, String bday) throws SQLException {

        // Create variables
        String query = null;
        PreparedStatement statement = null;
        ResultSet result;

        // Check if Person exists in PEOPLE
        query = "SELECT * FROM PEOPLE WHERE fname = ? AND lname = ?";
        statement = conn.prepareStatement(query);
        statement.setString(1, fname);
        statement.setString(2, lname);
        result = statement.executeQuery();

        // Add Person if does not exist
        if(!result.next()) {
            query = "INSERT INTO PEOPLE VALUES (NULL, ?, ?, ?)";
            statement = conn.prepareStatement(query);
            statement.setString(1, fname);
            statement.setString(2, lname);
            statement.setString(3, bday);
            statement.executeUpdate();
            System.out.println("Person Not Found. Adding to Database.");
        } else {
            System.out.println("Person Already Exists in Database.");
        }

        // Get PersonID
        query = "SELECT PersonID FROM PEOPLE WHERE fname = ? AND lname = ?;";
        statement = conn.prepareStatement(query);
        statement.setString(1, fname);
        statement.setString(2, lname);
        result = statement.executeQuery();
        result.next();
        return result.getInt(1);
    }

    public static int insertIntoMedia(Connection conn, String title, String date, String genre) throws SQLException {

        // Create variables
        String query = null;
        PreparedStatement statement = null;
        ResultSet result;

        // Insert into MEDIA
        query = "INSERT INTO MEDIA VALUES (NULL, ?, ?, ?)";
        statement = conn.prepareStatement(query);
        statement.setString(1, title);
        statement.setString(2, date);
        statement.setString(3, genre);
        statement.executeUpdate();
        System.out.println("Media Added to Database.");

        // Get MediaID
        query = "SELECT MediaID FROM MEDIA WHERE title = ? AND ReleaseDate = ? AND genre = ?;";
        statement = conn.prepareStatement(query);
        statement.setString(1, title);
        statement.setString(2, date);
        statement.setString(3, genre);
        result = statement.executeQuery();
        result.next();
        return result.getInt(1);
    }

    public static void main(String[] args) {

        Connection conn = initializeDB(DATABASE);
        System.out.println(conn);

        // display menu
        System.out.println("Select Option:\n");
        System.out.println("\t1. Search database");
        System.out.println("\t2. Add new media item to database");
        System.out.println("\t3. Order items");
        System.out.println("\t4. Activate items");
        System.out.println("\t5. Edit media");
        System.out.println("\t6. Add or Edit artist/actor/author/director");
        System.out.println("\t7. Hire employee");
        System.out.println("\t8. Edit patron");
        System.out.println("\t9. Useful reports");

        Scanner in = new Scanner(System.in);
        int num = in.nextInt();

        while(true) {

            try {
                switch (num) {
                    case 1:
                        searchItem(conn);
                        break;
                    case 2:
                        addItem(conn);
                        break;
                    case 3:
                        orderItem(conn);
                        break;
                    case 4:
                        activateItem(conn);
                        break;
                    case 5:

                        break;
                    case 6:
                        editPerson(conn);
                        break;
                }

            } catch (SQLException e) {
                e.printStackTrace();

            }
        }
    }

    public static void searchItem(Connection conn) throws SQLException {

        System.out.println("Select Search:\n");
        System.out.println("\t1. Display all artists");
        System.out.println("\t2. Display all actors");
        System.out.println("\t3. Display all directors");
        System.out.println("\t4. Display all authors");
        System.out.println("\t5. Display all tracks");
        System.out.println("\t6. Display all movies");
        System.out.println("\t7. Display all books");
        System.out.println("\t8. Search by title");


        Scanner in = new Scanner(System.in);
        int num = in.nextInt();
        in.nextLine();

        // create variables used for query creation
        PreparedStatement statement = null;
        ResultSet result = null;
        String query, select, from, where;

        switch (num) {

            case 1:

                // Display all artist
                select = "SELECT PEOPLE.* ";
                from = "FROM MEDIA, TRACKS, PEOPLE ";
                where = "WHERE MEDIA.MediaID = TRACKS.TrackID AND TRACKS.Artist = PEOPLE.PersonID;";
                query = select + from + where;
                statement = conn.prepareStatement(query);
                result = statement.executeQuery();
                break;

            case 2:

                // Display all actor
                select = "SELECT PEOPLE.* ";
                from = "FROM ACTORS, PEOPLE ";
                where = "WHERE ACTORS.ActorID = PEOPLE.PersonID;";
                query = select + from + where;
                statement = conn.prepareStatement(query);
                result = statement.executeQuery();
                break;

            case 3:

                // Display all actor
                select = "SELECT PEOPLE.* ";
                from = "FROM MEDIA, MOVIES, PEOPLE ";
                where = "WHERE MEDIA.MediaID = MOVIES.MovieID AND MOVIES.Director = PEOPLE.PersonID;";
                query = select + from + where;
                statement = conn.prepareStatement(query);
                result = statement.executeQuery();
                break;

            case 4:

                // Display all author
                select = "SELECT PEOPLE.* ";
                from = "FROM MEDIA, BOOKS, PEOPLE ";
                where = "WHERE MEDIA.MediaID = BOOKS.BookID AND BOOKS.Author = PEOPLE.PersonID;";
                query = select + from + where;
                statement = conn.prepareStatement(query);
                result = statement.executeQuery();
                break;

            case 5:

                // Display all tracks
                select = "SELECT * ";
                from = "FROM TRACKS;";
                query = select + from;
                statement = conn.prepareStatement(query);
                result = statement.executeQuery();
                break;

            case 6:

                // Display all movies
                select = "SELECT * ";
                from = "FROM MOVIES;";
                query = select + from;
                statement = conn.prepareStatement(query);
                result = statement.executeQuery();
                break;

            case 7:

                // Display all books
                select = "SELECT * ";
                from = "FROM BOOKS;";
                query = select + from;
                statement = conn.prepareStatement(query);
                result = statement.executeQuery();
                break;

            case 8:

                // Search by title
                System.out.println("Enter media title");
                String title = in.nextLine();

                select = "SELECT Media.* ";
                from = "FROM MEDIA ";
                where = "WHERE MEDIA.Title = ?;";
                query = select + from + where;
                statement = conn.prepareStatement(query);
                statement.setString(1, title);
                result = statement.executeQuery();
                break;
        }

        // print result set
        displayResultSet(result, statement, conn);

        in.close();
    }

    public static int addItem(Connection conn) throws SQLException {

        System.out.println("Select Add:\n");
        System.out.println("\t1. Add Track");
        System.out.println("\t2. Add Movie");
        System.out.println("\t3. Add Book");
        System.out.println("\t4. Add Person");

        Scanner in = new Scanner(System.in);
        int num = in.nextInt();
        in.nextLine();

        // create variables used for query creation
        PreparedStatement statement;
        String query, insert, values;
        String artistFname, artistLname, directorFname, directorLname, actorFname, actorLname, authorFname, authorLname,
                title, date, genre, length, inAlbum, albumTitle = null, bookType, bookLength = null, moreActors = "y";
        int artistPersonID, directorPersonID, actorPersonID, authorPersonID, MediaID = 0, pages, chapters;
        float rating;

        switch (num) {

            case 1:

                // Get track data
                System.out.println("Enter Track Title");
                title = in.nextLine();
                System.out.println("Enter Track Release Date (YYYY-MM-DD)");
                date = in.nextLine();
                System.out.println("Enter Track Genre");
                genre = in.nextLine();
                System.out.println("Enter Track Length (HH:MM:SS)");
                length = in.nextLine();
                System.out.println("Enter Artist First Name");
                artistFname = in.nextLine();
                System.out.println("Enter Artist Last Name");
                artistLname = in.nextLine();
                System.out.println("Is Track part of an album (y/n)?");
                inAlbum  = in.nextLine();

                if(inAlbum.equals("y")){
                    System.out.println("Enter Album Title");
                    albumTitle = in.nextLine();
                }

                // Add media
                MediaID = insertIntoMedia(conn, title, date, genre);

                // Ensure artist is in database
                artistPersonID = checkOrAddPerson(conn, artistFname, artistLname, "NULL");

                // Add track
                insert = "INSERT INTO TRACKS ";
                values = "VALUES (?, ?, ?, ?);";
                query = insert + values;

                statement = conn.prepareStatement(query);
                statement.setInt(1, MediaID);
                statement.setString(2, length);
                statement.setInt(4, artistPersonID);


                if(inAlbum.equals("y")){
                    statement.setString(3, albumTitle);
                } else {
                    statement.setString(3, "NULL");
                }

                statement.executeUpdate();
                System.out.println("Track Added to Database");

                break;

            case 2:

                // Get movie data
                System.out.println("Enter Movie Title");
                title = in.nextLine();
                System.out.println("Enter Movie Release Date (YYYY-MM-DD)");
                date = in.nextLine();
                System.out.println("Enter Movie Genre");
                genre = in.nextLine();
                System.out.println("Enter Movie Length (HH:SS:MM)");
                length = in.nextLine();
                System.out.println("Enter Movie Rating");
                rating = in.nextFloat();
                in.nextLine();
                System.out.println("Enter Director First Name");
                directorFname = in.nextLine();
                System.out.println("Enter Director Last Name");
                directorLname = in.nextLine();


                // Ensure artist is in database
                directorPersonID = checkOrAddPerson(conn, directorFname, directorLname, "NULL");

                // Add media
                MediaID = insertIntoMedia(conn, title, date, genre);

                // Add movie
                insert = "INSERT INTO MOVIES ";
                values = "VALUES (?, ?, ?, ?);";
                query = insert + values;

                statement = conn.prepareStatement(query);
                statement.setInt(1, MediaID);
                statement.setString(2, length);
                statement.setFloat(3, rating);
                statement.setInt(4, directorPersonID);
                statement.executeUpdate();
                System.out.println("Movie Added to Database\n");

                // Add actors
                while(moreActors.equals("y")){
                    System.out.println("Enter Actor First Name");
                    actorFname = in.nextLine();
                    System.out.println("Enter Actor First Name");
                    actorLname = in.nextLine();

                    actorPersonID = checkOrAddPerson(conn, actorFname, actorLname, "NULL");

                    insert = "INSERT INTO ACTORS ";
                    values = "VALUES (?, ?);";
                    query = insert + values;

                    statement = conn.prepareStatement(query);
                    statement.setInt(1, actorPersonID);
                    statement.setInt(2, MediaID);
                    statement.executeUpdate();

                    System.out.println("Add another actor (y/n)?");
                    moreActors = in.nextLine();
                }

                break;

            case 3:

                // Get Book data
                System.out.println("Enter Book Title");
                title = in.nextLine();
                System.out.println("Enter Book Release Date (YYYY-MM-DD)");
                date = in.nextLine();
                System.out.println("Enter Book Genre");
                genre = in.nextLine();
                System.out.println("Enter Author First Name");
                authorFname = in.nextLine();
                System.out.println("Enter Author Last Name");
                authorLname = in.nextLine();
                System.out.println("Enter number of chapters");
                chapters = in.nextInt();
                in.nextLine();
                System.out.println("Is the Book printed or digital (p/d)?");
                bookType = in.nextLine();

                // Ensure author is in database
                authorPersonID = checkOrAddPerson(conn, authorFname, authorLname, "NULL");

                // Add Media
                MediaID = insertIntoMedia(conn, title, date, genre);

                // Add Book
                insert = "INSERT INTO BOOKS ";
                values = "VALUES (?, ?, ?);";
                query = insert + values;
                statement = conn.prepareStatement(query);
                statement.setInt(1, MediaID);
                statement.setInt(2, authorPersonID);
                statement.setInt(3, chapters);
                statement.executeUpdate();

                // Add either Printed or AudioBook
                if(bookType.equals("p")){
                    System.out.println("Enter number of pages");
                    pages = in.nextInt();
                    in.nextLine();

                    insert = "INSERT INTO PRINTED ";
                    values = "VALUES (?, ?);";
                    query = insert + values;
                    statement = conn.prepareStatement(query);
                    statement.setInt(1, MediaID);
                    statement.setInt(2, pages);
                } else {
                    System.out.println("Enter audiobook duration (HH:MM:SS)");
                    bookLength = in.nextLine();

                    insert = "INSERT INTO AUDIOBOOK ";
                    values = "VALUES (?, ?);";
                    query = insert + values;
                    statement = conn.prepareStatement(query);
                    statement.setInt(1, MediaID);
                    statement.setString(2, bookLength);
                }

                statement.executeUpdate();
                statement.close();
                System.out.println("Book Added to Database");

                break;
        }

        in.close();
        return MediaID;
    }

    public static void orderItem(Connection conn) throws SQLException {

        Scanner in = new Scanner(System.in);

        PreparedStatement statement;
        String query, insert, values;
        String email, choice, title, date, genre, type, arrival;
        int MediaID, copies;
        float price;

        System.out.println("Enter Purchased By Email");
        email = in.nextLine();
        System.out.println("Enter number of copies");
        copies = in.nextInt();
        in.nextLine();
        System.out.println("Enter price per copy");
        price = in.nextFloat();
        in.nextLine();
        System.out.println("Enter arrival date (YYYY-MM-DD)");
        arrival = in.nextLine();
        System.out.println("Does the Media already exist in the Database (y/n)?");
        choice = in.nextLine();

        if(choice.equals("y")){
            System.out.println("Enter MediaID");
            MediaID = in.nextInt();
            in.nextLine();
        } else {
            MediaID = addItem(conn);
        }

        insert = "INSERT INTO MEDIABOUGHT ";
        values = "VALUES (?, ?, ?, ?, ?)";
        query = insert + values;

        statement = conn.prepareStatement(query);
        statement.setInt(1, MediaID);
        statement.setString(2, email);
        statement.setInt(3, copies);
        statement.setString(4, arrival);
        statement.setFloat(5, price);
        statement.executeUpdate();
        statement.close();
        System.out.println("Order placed");
    }

    public static void activateItem(Connection conn) throws SQLException {

        Scanner in = new Scanner(System.in);

        // create variables used for query creation
        PreparedStatement statement = null;
        ResultSet result = null;
        String query;
        int numActivate = 0;

        // Find Media Bought information
        System.out.println("Enter mediaID:");
        int mediaID = in.nextInt();
        in.nextLine();

        query = "SELECT * FROM MEDIABOUGHT WHERE MediaID = ?;";

        statement = conn.prepareStatement(query);
        statement.setInt(1, mediaID);

        result = statement.executeQuery();
        result.next();
        numActivate = result.getInt(3);
        System.out.println(numActivate);

        // Activate items by adding to digital or physical
        System.out.println("Activate (1) digital or (2) physical item:");
        int choice = in.nextInt();
        in.nextLine();

        switch (choice) {
            case 1:
                query = "INSERT INTO DIGITAL VALUES (?,?);";
                for (int i = 0; i < numActivate; i++) {
                    try {
                        statement = conn.prepareStatement(query);
                        statement.setInt(2, mediaID);

                        System.out.println("Enter License: ");
                        String license = in.nextLine();
                        statement.setString(1, license);
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                query = "INSERT INTO PHYSICAL VALUES (?,?,?);";
                for (int i = 0; i < numActivate; i++) {
                    try {
                        statement = conn.prepareStatement(query);
                        statement.setInt(2, mediaID);

                        System.out.println("Enter Barcode: ");
                        int barcode = in.nextInt();
                        in.nextLine();
                        System.out.println("Enter location: ");
                        String location = in.nextLine();

                        statement.setInt(1, barcode);
                        statement.setString(3, location);
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public static void editItem(Connection conn) throws SQLException {

        System.out.println("Type of item to edit:\n");
        System.out.println("\t1. Track");
        System.out.println("\t2. Movie");
        System.out.println("\t2. Book");

        Scanner in = new Scanner(System.in);
        int num = in.nextInt();
        in.nextLine();

        String fname, lname, bday;
        String query, update, set, where;
        int MediaID;
        PreparedStatement statement = null;

        switch(num) {

            case 1:

            case 2:

        }
    }

    public static void editPerson(Connection conn) throws SQLException {

        System.out.println("Select Option:\n");
        System.out.println("\t1. Add");
        System.out.println("\t2. Edit");

        Scanner in = new Scanner(System.in);
        int num = in.nextInt();
        in.nextLine();

        String fname, lname, bday;
        String query, update, set, where;
        int MediaID;
        PreparedStatement statement = null;

        switch(num) {

            case 1:
                System.out.println("Enter person first name");
                fname = in.nextLine();
                System.out.println("Enter person last name:");
                lname = in.nextLine();
                System.out.println("Enter person birthday (YYYY-MM-DD):");
                bday = in.nextLine();

                checkOrAddPerson(conn, fname, lname, bday);
                break;

            case 2:
                System.out.println("Enter person first name to update:");
                fname = in.nextLine();
                System.out.println("Enter person last name to update:");
                lname = in.nextLine();

                MediaID = checkOrAddPerson(conn, fname, lname, "NULL");
                System.out.println("Enter new first name:");
                fname = in.nextLine();
                System.out.println("Enter new last name:");
                lname = in.nextLine();
                System.out.println("Enter new bday:");
                bday = in.nextLine();

                update = "UPDATE PEOPLE ";
                set = "SET fname = ?, lname = ?, bday = ? ";
                where = "WHERE PersonID = ?";
                query = update + set + where;

                statement = conn.prepareStatement(query);
                statement.setString(1, fname);
                statement.setString(2, lname);
                statement.setString(3, bday);
                statement.setInt(4, MediaID);
                statement.executeUpdate();
                statement.close();
        }
    }

    public static void hireEmployee(Connection conn) {}

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
                select = "SELECT PEOPLE.Fname, PEOPLE.Lname, COUNT(PEOPLE.PersonID) as Occurrence ";
                from = "FROM MEDIABORROWED, MEDIA, MOVIES, PEOPLE ";
                where = "WHERE MEDIABORROWED.MediaID = MEDIA.MediaID AND MEDIA.MediaID = MOVIES.MovieID AND MOVIES.Actor = PEOPLE.PersonID ";
                group = "GROUP BY PEOPLE.PersonID ";
                order = "ORDER BY Occurrence DESC ";
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
                where = "WHERE MEDIABORROWED.CheckedOutBy = PATRON.Email AND MEDIABORROWED.MediaID = MEDIA.MediaID AND MEDIA.MediaID = MOVIES.MovieID ";
                group = "GROUP BY PATRON.Email ";
                order = "ORDER BY Videos DESC ";
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
        displayResultSet(result, statement, conn);

        in.close();
    }
}
