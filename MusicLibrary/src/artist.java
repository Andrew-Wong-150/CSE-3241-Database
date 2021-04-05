
public class artist {
	private String fname;
	private String lname;
	private int artistID;
	
	/**
	 * Default constructor.
	 */
	public artist()
	{
		
	}
	
	/**
	 * Constructor that takes the Strings fname and lname as input.
	 * 
	 * @param fname
	 * 		the first name of the artist
	 * @param lname
	 * 		the last name of the artist
	 */
	public artist(String fname, String lname)
	{
		this.fname = fname;
		this.lname = lname;
		this.artistID = userProgram.ID++;
	}
	
	/**
	 * Returns the available information about an artist.
	 * 
	 * @return An Array of String containing the artistID, fname, and lname
	 */
	public String[] getArtistInfo()
	{
		String[] info = new String[3];
		info[0] = Integer.toString(artistID);
		info[1] = fname;
		info[2] = lname;
		return info;
	}
	
	/**
	 * Changes the first name of the artist.
	 * 
	 * @param newName
	 * 		the new name for the artist's first name
	 */
	public void editFirstName(String newName)
	{
		fname = newName;
	}
	
	/**
	 * Changes the last name of the artist.
	 * 
	 * @param newName
	 * 		the new name for the artist's last name
	 */
	public void editLastName(String newName)
	{
		lname = newName;
	}
}
