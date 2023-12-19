package application;

public class SingleRoom extends Room{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1436583254681640923L;

	public SingleRoom() {}

	public SingleRoom(String roomName, String roomStandard, int roomNumber, String roomType) {
		this.roomName = roomName;
		this.roomStandard = roomStandard;
		this.roomNumber = roomNumber;
		this.roomType = roomType;
	}

}
