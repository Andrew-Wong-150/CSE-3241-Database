import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class userProgram {
	public static int ID = 0;

	/**
	 * Prints out list of options the user can choose from.
	 */
	public static void printOptions() {
		System.out.println("\nPlease select from the following list: ");
		System.out.println("1 - Enter data for a new artist");
		System.out.println("2 - Enter data for new track / song");
		System.out.println("3 - Enter data for new media items ordered (type of media, copies purchased, price, arrival date):");
		System.out.println("4 - Retreive information about an artist");
		System.out.println("5 - Retreive information about a track");
		System.out.println("6 - Retreive information about new media items that are ordered");
		System.out.println("7 - Edit existing entries for an artist");
		System.out.println("8 - Delete an existing track");
		System.out.println("0 - Quit");
	}
	
	/**
	 * Searches through ArrayList list for a user defined name.
	 * 
	 * @param list
	 * 		the ArrayList of artists
	 * @param user
	 * 		the input stream
	 * @return a pair containing a Boolean variable and an artist
	 */
	public static Map.Entry<Boolean, artist> findArtist(ArrayList<artist> list, Scanner user)
	{
		Boolean nameFound = false;
		artist foundArtist = new artist();
		System.out.print("\nEnter the first name of the artist you would like to find: ");
		String fname = user.nextLine();
		// Scans through every artist in the ArrayList
		for (artist element : list)
		{
			String[] info = element.getArtistInfo();
			// Checks if the user inputed name matches any in the ArrayList
			// Checks for multiple occurrences of the same name
			if (fname.equals(info[1]))
			{
				System.out.println("Did you want: " + fname + " " + info[2] + "?");
				System.out.print("Type 1 for yes or 0 for no: ");
				int selection = Integer.parseInt(user.nextLine());
				if (selection == 1)
				{
					foundArtist = element;
					nameFound = true;
					break;
				}
			}
		}
		Map.Entry<Boolean, artist> result = new AbstractMap.SimpleEntry<Boolean, artist>(nameFound, foundArtist);
		return result;
	}
	
	/**
	 * Searches through ArrayList list for a user defined title.
	 * 
	 * @param list
	 * 		the ArrayList of tracks
	 * @param user
	 * 		the input stream
	 * @return a pair containing a Boolean variable and a track
	 */
	public static Map.Entry<Boolean, track> findTrack(ArrayList<track> list, Scanner user)
	{
		Boolean titleFound = false;
		track foundTrack = new track();
		System.out.print("\nEnter the title of the track you would like to find: ");
		String title = user.nextLine();
		// Scans through every track in the ArrayList
		for (track element : list)
		{
			String[] info = element.getTrackInfo();
			// Checks if the user inputed name matches any in the ArrayList
			// Checks for multiple occurrences of the same name
			if (title.equals(info[1]))
			{
				System.out.println("Did you want: " + title + " with length " + info[2]);
				System.out.print("Type 1 for yes or 0 for no: ");
				int selection = Integer.parseInt(user.nextLine());
				if (selection == 1)
				{
					foundTrack = element;
					titleFound = true;
					break;
				}
			}
		}
		Map.Entry<Boolean, track> result = new AbstractMap.SimpleEntry<Boolean, track>(titleFound, foundTrack);
		return result;
	}
	
	/**
	 * Searches through ArrayList list for a user defined name.
	 * 
	 * @param list
	 * 		the ArrayList of purchasedMedia
	 * @param user
	 * 		the input stream
	 * @return a pair containing a Boolean variables and a purchasedMedia item
	 */
	public static Map.Entry<Boolean, purchasedMedia> findNewMedia(ArrayList<purchasedMedia> list, Scanner user)
	{
		Boolean nameFound = false;
		purchasedMedia foundMedia = new purchasedMedia();
		System.out.print("\nEnter the name of the new media you would like to find: ");
		String name = user.nextLine();
		// Scans through every media item in the ArrayList
		for (purchasedMedia element : list)
		{
			String[] info = element.getMediaInfo();
			// Checks if the user inputed name matches any in the ArrayList
			// Checks for multiple occurrences of the same name
			if (name.equals(info[2]))
			{
				System.out.println("Did you want: " + name + " of type " + info[1]);
				System.out.print("Type 1 for yes or 0 for no: ");
				int selection = Integer.parseInt(user.nextLine());
				if (selection == 1)
				{
					foundMedia = element;
					nameFound = true;
					break;
				}
			}
		}
		Map.Entry<Boolean, purchasedMedia> result = new AbstractMap.SimpleEntry<Boolean, purchasedMedia>(nameFound, foundMedia);
		return result;
	}
	
	/**
	 * The main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int choice = 1;
		ArrayList<artist> artists = new ArrayList<artist>();
		ArrayList<track> tracks = new ArrayList<track>();
		ArrayList<purchasedMedia> newMedias = new ArrayList<purchasedMedia>();
		Scanner input = new Scanner(System.in);

		System.out.println("Welcome!");

		while (choice != 0)
		{
			printOptions();
			choice = Integer.parseInt(input.nextLine());
			switch (choice)
			{
				// Enter a new artist
				case 1:
					System.out.print("\nEnter the first name of the artist: ");
					String fname = input.nextLine();
					System.out.print("Enter the last name of the artist: ");
					String lname = input.nextLine();
					artist newArtist = new artist(fname, lname);
					artists.add(newArtist);
					break;
					
				// Enter a new track
				case 2:
					System.out.print("\nEnter the title of the track: ");
					String title = input.nextLine();
					System.out.print("Enter the length of the track (mm:ss format): ");
					String length = input.nextLine();	
					track newTrack = new track(title, length);
					tracks.add(newTrack);
					break;
					
				// Enter a new media item
				case 3:
					System.out.print("\nEnter the type of media purchased (movie, album, audiobook): ");
					String type = input.nextLine();
					System.out.print("Enter the name of the media purchased: ");
					String name = input.nextLine();
					System.out.print("Enter the number of copies purchased: ");
					int copies = Integer.parseInt(input.nextLine());
					System.out.print("Enter the price of the media purchased ($xx.xx format): ");
					String price = input.nextLine();
					System.out.print("Enter the arrival date of the media purchased (dd/mm/yyyy format): ");
					String arrival = input.nextLine();
					purchasedMedia media = new purchasedMedia(type, name, copies, price, arrival);
					newMedias.add(media);
					break;
					
				// Retrieve info on artist
				case 4:
					Map.Entry<Boolean, artist> artistInfo = findArtist(artists, input);
					if (artistInfo.getKey())
					{
						String[] info = artistInfo.getValue().getArtistInfo();
						System.out.println("Artist name: " + info[1] + " " + info[2]);
						System.out.println("Artist ID: " + info[0]);
					} else {
						System.out.println("\nArtist not found!\n");
					}
					break;
					
				// Retrieve info on track
				case 5:
					Map.Entry<Boolean, track> trackInfo = findTrack(tracks, input);
					if (trackInfo.getKey())
					{
						String[] info = trackInfo.getValue().getTrackInfo();
						System.out.println("Track title: " + info[1]);
						System.out.println("Track length: " + info[2]);
						System.out.println("Track ID: " + info[0]);
					} else {
						System.out.println("\nTrack not found!\n");
					}
					break;
					
				// Retrieve info on new media item
				case 6:
					Map.Entry<Boolean, purchasedMedia> mediaInfo = findNewMedia(newMedias, input);
					if (mediaInfo.getKey())
					{
						String[] info = mediaInfo.getValue().getMediaInfo();
						System.out.println("Media name: " + info[2]);
						System.out.println("Media type: " + info[1]);
						System.out.println("Media price: " + info[4]);
						System.out.println("Copies purchased: " + info[3]);
						System.out.println("Media arrival date: " + info[5]);
						System.out.println("Media ID: " + info[0]);

					} else {
						System.out.println("\nNew media not found!\n");
					}
					break;
					
				// Edit existing entries for artist
				case 7:
					Map.Entry<Boolean, artist> updateArtist = findArtist(artists, input);
					if (updateArtist.getKey())
					{
						String[] info = updateArtist.getValue().getArtistInfo();
						// Checks for the user to change the first name
						System.out.println("Would you like to change the first name? Current: " + info[1]);
						System.out.print("Type 1 for yes or 0 for no: ");
						int selection = Integer.parseInt(input.nextLine());
						if (selection == 1)
						{
							System.out.print("Enter new first name: ");
							String newName = input.nextLine();
							updateArtist.getValue().editFirstName(newName);
						}
						// Checks for the user to change the last name
						System.out.println("Would you like to change the last name? Current: " + info[2]);
						System.out.print("Type 1 for yes or 0 for no: ");
						selection = Integer.parseInt(input.nextLine());
						if (selection == 1)
						{
							System.out.print("Enter new last name: ");
							String newName = input.nextLine();
							updateArtist.getValue().editLastName(newName);
						}
					} else {
						System.out.println("\nArtist not found!\n");
					}
					break;
					
				// Delete existing track
				case 8:
					Map.Entry<Boolean, track> deleteTrack = findTrack(tracks, input);
					if (deleteTrack.getKey())
					{
						tracks.remove(deleteTrack.getValue());
						System.out.println("\nThe track has been deleted!");
					} else {
						System.out.println("\nTrack not found!\n");
					}
					break;
					
				// Exit program
				case 0:
					System.out.println("Goodbye!");
					break;
					
				default:
					System.out.println("Please enter a valid integer choice!");
					break;
			}
		}
		input.close();
	}

}
