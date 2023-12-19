package application;

public class DoubleRoom extends Room{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2745259326930393637L;

	public DoubleRoom() {}

	public DoubleRoom(String roomName, String roomStandard, int roomNumber, String roomType) {
		this.roomName = roomName;
		this.roomStandard = roomStandard;
		this.roomNumber = roomNumber;
		this.roomType = roomType;
	}
	
}
