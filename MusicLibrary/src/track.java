
public class track {
	private String title;
	private String length;
	private int trackID;
	
	/**
	 * Default constructor.
	 */
	public track()
	{
		
	}
	
	/**
	 * Constructor that takes the track title and length as input.
	 * 
	 * @param title
	 * 		the title of the track
	 * @param length
	 * 		the length of the track in mm:ss format
	 */
	public track(String title, String length)
	{
		this.title = title;
		this.length = length;
		this.trackID = userProgram.ID++;
	}
	
	/**
	 * Returns the available information about a track.
	 * 
	 * @return An Array of String containing the trackID, title, and length
	 */
	public String[] getTrackInfo()
	{
		String[] info = new String[3];
		info[0] = Integer.toString(trackID);
		info[1] = title;
		info[2] = length;
		return info;
	}
}
