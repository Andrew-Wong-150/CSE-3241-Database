
public class purchasedMedia {
	private String type;
	private String name;
	private int copies;
	private String price;
	private String arrival;
	private int mediaID;
	
	/**
	 * The default constructor.
	 */
	public purchasedMedia()
	{
		
	}
	
	/**
	 * Constructor that takes various elements as input.
	 * 
	 * @param type
	 * 		the type of media purchased
	 * @param name
	 * 		the name of the media purchased
	 * @param copies
	 * 		the number of copies purchased
	 * @param price
	 * 		the prices on an individual media item
	 * @param arrival
	 * 		the arrival date of the new media item
	 */
	public purchasedMedia(String type, String name, int copies, String price, String arrival)
	{
		this.type = type;
		this.name = name;
		this.copies = copies;
		this.price = price;
		this.arrival = arrival;
		this.mediaID = userProgram.ID++;
	}
	
	/**
	 * Returns the available information about a new media item.
	 * 
	 * @return Array of Strings containing the new media item's ID, type, name, copies purchased, price, and arrival date
	 */
	public String[] getMediaInfo()
	{
		String[] info = new String[6];
		info[0] = Integer.toString(mediaID);
		info[1] = type;
		info[2] = name;
		info[3] = Integer.toString(copies);
		info[4] = price;
		info[5] = arrival;
		return info;
	}
}
