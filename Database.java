package osu.cse3241;

import java.sql.*;
import java.util.*;


public class Database {

    private static String DATABASE = "populated.db";

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
                System.out.print(metadata.getColumnName(i) + "\t\t");
            }
            System.out.println("\n");
            while (result.next()) {
                System.out.println("");
                for (int i = 1; i <= column; i++) {
                    String columnValue = result.getString(i);
                    System.out.print(columnValue + "\t\t");
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

    public static void main(String[] args) throws SQLException {

        Connection conn = initializeDB(DATABASE);
        System.out.println(conn);

        while(true) {

            Scanner in = new Scanner(System.in);

            System.out.println("\nPress Enter to continue...");
            in.nextLine();

            // display menu
            System.out.println("\nSelect Option:\n");
            System.out.println("\t1.  Search catalog");
            System.out.println("\t2.  Add new media item to database");
            System.out.println("\t3.  Order new items");
            System.out.println("\t4.  Activate items");
            System.out.println("\t5.  Check out item");
            System.out.println("\t6.  Edit media or Delete lost item");
            System.out.println("\t7.  Add or Edit artist/actor/author/director");
            System.out.println("\t8.  Add or Remove patron");
            System.out.println("\t9.  Add or Remove employee");
            System.out.println("\t10. Add or Remove owner");
            System.out.println("\t11. Display employees/patrons/owners");
            System.out.println("\t12. Useful reports");
            int num = in.nextInt();
            in.nextLine();

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
                    checkOutOrReturnItem(conn);
                    break;
                case 6:
                    editItem(conn);
                    break;
                case 7:
                    editPerson(conn);
                    break;
                case 8:
                    editPatron(conn);
                    break;
                case 9:
                    editEmployee(conn);
                    break;
                case 10:
                    editOwner(conn);
                    break;
                case 11:
                    displayInternal(conn);
                    break;
                case 12:
                    usefulReports(conn);
                    break;
            }
        }
    }

    public static void searchItem(Connection conn) throws SQLException {

        System.out.println("Select Search:\n");
        System.out.println("\tDISPLAY ALL PEOPLE");
        System.out.println("--------------------------------------------");
        System.out.println("\t1.  Display all artists");
        System.out.println("\t2.  Display all actors");
        System.out.println("\t3.  Display all directors");
        System.out.println("\t4.  Display all authors");

        System.out.println("\n\tDISPLAY ALL MEDIA");
        System.out.println("--------------------------------------------");
        System.out.println("\t5.  Display all tracks");
        System.out.println("\t6.  Display all movies");
        System.out.println("\t7.  Display all books");
        System.out.println("\t8.  Search by title");

        System.out.println("\n\tDISPLAY ALL BY PHYSICAL VS DIGITAL");
        System.out.println("--------------------------------------------");
        System.out.println("\t9.  Display all physical by title");
        System.out.println("\t10. Display all digital by title");


        Scanner in = new Scanner(System.in);
        int num = in.nextInt();
        in.nextLine();

        // create variables used for query creation
        PreparedStatement statement = null;
        ResultSet result = null;
        String query, select = null, from = null, where = null, group = null, title;

        switch (num) {

            case 1:

                // Display all artist
                select = "SELECT PEOPLE.* ";
                from = "FROM TRACKS, PEOPLE ";
                where = "WHERE TRACKS.Artist = PEOPLE.PersonID ";
                group = "GROUP BY PEOPLE.PersonID;";
                query = select + from + where + group;
                statement = conn.prepareStatement(query);
                result = statement.executeQuery();
                break;

            case 2:

                // Display all actor
                select = "SELECT PEOPLE.* ";
                from = "FROM ACTORS, PEOPLE ";
                where = "WHERE ACTORS.ActorID = PEOPLE.PersonID ";
                group = "GROUP BY PEOPLE.PersonID;";
                query = select + from + where + group;
                statement = conn.prepareStatement(query);
                result = statement.executeQuery();
                break;

            case 3:

                // Display all actor
                select = "SELECT PEOPLE.* ";
                from = "FROM MOVIES, PEOPLE ";
                where = "WHERE MOVIES.Director = PEOPLE.PersonID ";
                group = "GROUP BY PEOPLE.PersonID;";
                query = select + from + where + group;
                statement = conn.prepareStatement(query);
                result = statement.executeQuery();
                break;

            case 4:

                // Display all author
                select = "SELECT PEOPLE.* ";
                from = "FROM BOOKS, PEOPLE ";
                where = "WHERE BOOKS.Author = PEOPLE.PersonID ";
                group = "GROUP BY PEOPLE.PersonID;";
                query = select + from + where + group;
                statement = conn.prepareStatement(query);
                result = statement.executeQuery();
                break;

            case 5:

                // Display all tracks
                select = "SELECT MEDIA.*, TRACKS.* ";
                from = "FROM MEDIA, TRACKS ";
                where = "WHERE MEDIA.MediaID = TRACKS.TrackID;";
                query = select + from + where;
                statement = conn.prepareStatement(query);
                result = statement.executeQuery();
                break;

            case 6:

                // Display all movies
                select = "SELECT MEDIA.*, MOVIES.* ";
                from = "FROM MEDIA, MOVIES ";
                where = "WHERE MEDIA.MediaID = MOVIES.MovieID;";
                query = select + from + where;
                statement = conn.prepareStatement(query);
                result = statement.executeQuery();
                break;

            case 7:

                // Display all books
                select = "SELECT MEDIA.*, BOOKS.* ";
                from = "FROM MEDIA, BOOKS ";
                where = "WHERE MEDIA.MediaID = BOOKS.BookID;";
                query = select + from + where;
                statement = conn.prepareStatement(query);
                result = statement.executeQuery();
                break;

            case 8:

                // Search by title
                System.out.println("Enter media title");
                title = in.nextLine();
                System.out.println("Enter media type (t: Track, b: Book, m: Movie)");
                String type = in.nextLine();

                if(type.equals("t")){
                    select = "SELECT Media.*, TRACKS.* ";
                    from = "FROM MEDIA, TRACKS ";
                    where = "WHERE MEDIA.Title = ?;";
                }

                if(type.equals("b")){
                    select = "SELECT Media.*, BOOKS.* ";
                    from = "FROM MEDIA, BOOKS ";
                    where = "WHERE MEDIA.Title = ?;";
                }

                if(type.equals("m")){
                    select = "SELECT Media.*, MOVIES.* ";
                    from = "FROM MEDIA, MOVIES ";
                    where = "WHERE MEDIA.Title = ?;";
                }

                query = select + from + where;
                statement = conn.prepareStatement(query);
                statement.setString(1, title);
                result = statement.executeQuery();
                break;

            case 9:

                // Display all physical by title
                System.out.println("Enter media title");
                title = in.nextLine();

                select = "SELECT PHYSICAL.* ";
                from = "FROM PHYSICAL, MEDIA ";
                where = "WHERE PHYSICAL.PhysicalID = MEDIA.MediaID AND MEDIA.Title = ?;";
                query = select + from + where;
                statement = conn.prepareStatement(query);
                statement.setString(1, title);
                result = statement.executeQuery();
                break;

            case 10:

                // Display all digital by title
                System.out.println("Enter media title");
                title = in.nextLine();

                select = "SELECT DIGITAL.* ";
                from = "FROM DIGITAL, MEDIA ";
                where = "WHERE DIGITAL.DigitalID = MEDIA.MediaID AND MEDIA.Title = ?;";
                query = select + from + where;
                statement = conn.prepareStatement(query);
                statement.setString(1, title);
                result = statement.executeQuery();
                break;
        }

        // print result set
        displayResultSet(result, statement, conn);

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
                    System.out.println("Enter Actor Last Name");
                    actorLname = in.nextLine();

                    actorPersonID = checkOrAddPerson(conn, actorFname, actorLname, "NULL");

                    insert = "INSERT INTO ACTORS ";
                    values = "VALUES (NULL, ?, ?);";
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

    public static void checkOutOrReturnItem(Connection conn) throws SQLException {

        Scanner in = new Scanner(System.in);

        // create variables used for query creation
        PreparedStatement statement = null;
        ResultSet result = null;
        String query, email, dueDate, checkOutDate, licence, type;
        int mediaID, barcode;

        System.out.println("Select action:");
        System.out.println("\t1. Checkout");
        System.out.println("\t2. Return");
        int num = in.nextInt();
        in.nextLine();

        switch(num) {

            case 1:

                // Get checkout information
                System.out.println("Enter mediaID:");
                mediaID = in.nextInt();
                in.nextLine();
                System.out.println("Enter email of patron checking out");
                email = in.nextLine();
                System.out.println("Enter check out date (YYYY-MM-DD)");
                checkOutDate = in.nextLine();
                System.out.println("Enter due date (YYYY-MM-DD)");
                dueDate = in.nextLine();
                System.out.println("Enter media type (d: Digital, p:Physical)");
                type = in.nextLine();

                query = "INSERT INTO MEDIABORROWED VALUES (NULL, ?, ?, ?, ?, FALSE, ?, ?);";
                statement = conn.prepareStatement(query);
                statement.setInt(1, mediaID);
                statement.setString(2, email);
                statement.setString(3, dueDate);
                statement.setString(4, checkOutDate);

                if (type.equals("p")) {
                    System.out.println("Enter barcode");
                    barcode = in.nextInt();
                    in.nextLine();
                    statement.setInt(5, barcode);
                    statement.setNull(6, Types.VARCHAR);
                }

                if (type.equals("d")) {
                    System.out.println("Enter licence");
                    licence = in.nextLine();
                    in.nextLine();
                    statement.setNull(5, Types.INTEGER);
                    statement.setString(6, licence);
                }

                statement.executeUpdate();
                System.out.println("Checkout Completed.");
                break;

            case 2:

                // Get return information
                System.out.println("Enter mediaID:");
                mediaID = in.nextInt();
                in.nextLine();
                System.out.println("Enter email of patron who checked out item");
                email = in.nextLine();

                query = "UPDATE MEDIABORROWED SET Returned = TRUE WHERE MediaID = ? AND CheckedOutBy = ?;";
                statement = conn.prepareStatement(query);
                statement.setInt(1, mediaID);
                statement.setString(2, email);
                statement.executeUpdate();
                System.out.println("Return Completed");
                break;
        }

    }

    public static void editItem(Connection conn) throws SQLException {

        System.out.println("Select Action:\n");
        System.out.println("\t1. Edit media");
        System.out.println("\t2. Delete lost item");

        Scanner in = new Scanner(System.in);
        int num = in.nextInt();
        in.nextLine();

        String fname, lname, bday;
        String query, type, licence, title, date, genre;
        int MediaID, barcode;
        PreparedStatement statement = null;

        switch(num) {

            case 1:

                // Edit item
                System.out.println("Enter MediaID");
                MediaID = in.nextInt();
                System.out.println("Enter new Title");
                title = in.nextLine();
                System.out.println("Enter new Release Date");
                date = in.nextLine();
                System.out.println("Enter new Genre");
                genre = in.nextLine();

                query = "UPDATE MEDIA SET Title = ?, ReleaseDate = ?, Genre = ? WHERE MediaID = ?;";
                statement = conn.prepareStatement(query);
                statement.setString(1, title);
                statement.setString(2, date);
                statement.setString(3, genre);
                statement.setInt(4, MediaID);
                statement.executeUpdate();
                System.out.println("Update Complete");
                break;

            case 2:

                // Delete item
                System.out.println("Enter media type (d: Digital, p:Physical)");
                type = in.nextLine();

                if (type.equals("p")) {
                    System.out.println("Enter barcode");
                    barcode = in.nextInt();
                    query = "DELETE FROM PHYSICAL WHERE Barcode = ?;";
                    statement = conn.prepareStatement(query);
                    statement.setInt(1,barcode);
                    statement.executeUpdate();
                }

                if (type.equals("d")) {
                    System.out.println("Enter licence");
                    licence = in.nextLine();
                    in.nextLine();
                    query = "DELETE FROM DIGITAL WHERE Licence = ?;";
                    statement = conn.prepareStatement(query);
                    statement.setString(1, licence);
                    statement.executeUpdate();
                }

                System.out.println("Delete Complete");
                break;
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
                System.out.println("Person Updated.");
        }
    }

    public static void editPatron(Connection conn) throws SQLException {

        Scanner in = new Scanner(System.in);

        System.out.println("Select action:");
        System.out.println("\t1. Add Patron");
        System.out.println("\t2. Remove Patron");

        int num = in.nextInt();
        in.nextLine();

        // create variables used for query creation
        PreparedStatement statement;
        ResultSet result;
        String query, insert, values;
        String email, address, fname, lname;
        int cardNum;

        switch(num) {

            case 1:

                System.out.println("Enter Email");
                email = in.nextLine();
                System.out.println("Enter Address");
                address = in.nextLine();
                System.out.println("Enter First Name");
                fname = in.nextLine();
                System.out.println("Enter Last Name");
                lname = in.nextLine();
                System.out.println("Enter Card Number");
                cardNum = in.nextInt();
                in.nextLine();

                insert = "INSERT INTO PATRON ";
                values = "VALUES (?, ?, ?, ?, ?);";
                query = insert + values;

                statement = conn.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, address);
                statement.setString(3, fname);
                statement.setString(4, lname);
                statement.setInt(5, cardNum);
                statement.executeUpdate();
                System.out.println("Patron Added to Database.");
                break;

            case 2:
                System.out.println("Enter Card Number");
                cardNum = in.nextInt();
                in.nextLine();
                query = "DELETE FROM PATRON WHERE CardNum = ?;";
                statement = conn.prepareStatement(query);
                statement.setInt(1, cardNum);
                statement.executeUpdate();
                System.out.println("Patron Removed.");
        }
    }

    public static void editEmployee(Connection conn) throws SQLException {

        Scanner in = new Scanner(System.in);

        System.out.println("Select action:");
        System.out.println("\t1. Add Employee");
        System.out.println("\t2. Remove Employee");

        int num = in.nextInt();
        in.nextLine();

        // create variables used for query creation
        PreparedStatement statement;
        ResultSet result;
        String query, insert, values;
        String email, address, fname, lname, hiredBy;
        float salary;
        int EmployeeID;

        switch(num) {

            case 1:
                // Get employee data
                System.out.println("Enter Hiring Manager (Owner) Email");
                hiredBy = in.nextLine();

                // Ensure boss exsists
                query = "SELECT * FROM OWNER WHERE Email = ?;";
                statement = conn.prepareStatement(query);
                statement.setString(1, hiredBy);
                result = statement.executeQuery();

                if (!result.next()) {
                    System.out.println("Owner not found. Must add owner first. Returning to menu.");
                    return;
                }

                System.out.println("Enter Salary");
                salary = in.nextFloat();
                in.nextLine();
                System.out.println("Enter Email");
                email = in.nextLine();
                System.out.println("Enter Address");
                address = in.nextLine();
                System.out.println("Enter First Name");
                fname = in.nextLine();
                System.out.println("Enter Last Name");
                lname = in.nextLine();

                insert = "INSERT INTO EMPLOYEE ";
                values = "VALUES (NULL, ?, ?, ?, ?, ?, ?);";
                query = insert + values;

                statement = conn.prepareStatement(query);
                statement.setFloat(1, salary);
                statement.setString(2, email);
                statement.setString(3, address);
                statement.setString(4, fname);
                statement.setString(5, lname);
                statement.setString(6, hiredBy);
                statement.executeUpdate();
                System.out.println("Employee Added to Database.");
                break;

            case 2:
                System.out.println("Enter EmployeeID");
                EmployeeID = in.nextInt();
                in.nextLine();
                query = "DELETE FROM EMPLOYEE WHERE EmployeeID = ?;";
                statement = conn.prepareStatement(query);
                statement.setInt(1, EmployeeID);
                statement.executeUpdate();
                System.out.println("Employee Removed.");
        }
    }

    public static void editOwner(Connection conn) throws SQLException {

        Scanner in = new Scanner(System.in);

        System.out.println("Select action:");
        System.out.println("\t1. Add Owner");
        System.out.println("\t2. Remove Owner");

        int num = in.nextInt();
        in.nextLine();

        // create variables used for query creation
        PreparedStatement statement;
        String query, insert, values;
        String email, fname, lname;

        switch(num) {

            case 1:
                // Get employee data
                System.out.println("Enter Email");
                email = in.nextLine();
                ;
                System.out.println("Enter First Name");
                fname = in.nextLine();
                System.out.println("Enter Last Name");
                lname = in.nextLine();

                insert = "INSERT INTO OWNER ";
                values = "VALUES (?, ?, ?);";
                query = insert + values;

                statement = conn.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, fname);
                statement.setString(3, lname);
                statement.executeUpdate();
                System.out.println("Owner Added to Database.");
                break;

            case 2:
                System.out.println("Enter Email");
                email = in.nextLine();
                query = "DELETE FROM OWNER WHERE Email = ?;";
                statement = conn.prepareStatement(query);
                statement.setString(1, email);
                statement.executeUpdate();
                break;
        }
    }

    public static void displayInternal(Connection conn) throws SQLException {

        Scanner in = new Scanner(System.in);

        System.out.println("Select display:");
        System.out.println("\t1. Patron");
        System.out.println("\t2. Employees");
        System.out.println("\t3. Owner");

        int num = in.nextInt();
        in.nextLine();

        // create variables used for query creation
        PreparedStatement statement = null;
        ResultSet result = null;
        String query;

        switch(num) {

            case 1:
                // Get patrons
                query = "SELECT * FROM PATRON;";
                statement = conn.prepareStatement(query);
                result = statement.executeQuery();
                break;

            case 2:
                // Get employees
                query = "SELECT * FROM EMPLOYEE;";
                statement = conn.prepareStatement(query);
                result = statement.executeQuery();
                break;

            case 3:
                // Get owner
                query = "SELECT * FROM OWNER;";
                statement = conn.prepareStatement(query);
                result = statement.executeQuery();
                break;
        }

        displayResultSet(result, statement, conn);
    }

    public static void usefulReports(Connection conn) throws SQLException {

        System.out.println("Select report:\n");
        System.out.println("\t1. Track by ARTIST released before YEAR");
        System.out.println("\t2. Patron who has checked out the most videos");
        System.out.println("\t3. Display checked out items by patron");


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

                statement = conn.prepareStatement(query);
                statement.setString(1, fname);
                statement.setString(2, lname);
                statement.setInt(3, year);
                result = statement.executeQuery();
                break;

            case 2:

                // Patron who has checked out the most videos
                select = "SELECT PATRON.*, COUNT(PATRON.Email) as Videos ";
                from = "FROM PATRON, MEDIABORROWED, MEDIA, MOVIES ";
                where = "WHERE MEDIABORROWED.CheckedOutBy = PATRON.Email AND MEDIABORROWED.MediaID = MEDIA.MediaID AND MEDIA.MediaID = MOVIES.MovieID ";
                group = "GROUP BY PATRON.Email ";
                order = "ORDER BY Videos DESC ";
                limit = "LIMIT 1;";

                query = select + from + where + group + order + limit;
                statement = conn.prepareStatement(query);
                result = statement.executeQuery();
                break;

            case 3:
                System.out.println("Enter patron email: ");
                String email = in.nextLine();
                select = "SELECT MEDIA.* ";
                from = "FROM MEDIA, MEDIABORROWED ";
                where = "WHERE MEDIABORROWED.CheckedOutBy = ? AND MEDIABORROWED.MediaID = MEDIA.MediaID;";
                query = select + from + where;
                statement = conn.prepareStatement(query);
                statement.setString(1, email);
                result = statement.executeQuery();

                if(!result.next()){
                    System.out.println("No Movies Checked Out");
                    result.beforeFirst();
                    return;
                }
                break;
        }

        // print result set
        displayResultSet(result, statement, conn);
        
    }
}
