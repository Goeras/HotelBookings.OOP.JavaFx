package application;

public class SuiteRoom extends Room{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3000960362229078309L;

	public SuiteRoom() {}

	public SuiteRoom(String roomName, String roomStandard, int roomNumber, String roomType) {
		this.roomName = roomName;
		this.roomStandard = roomStandard;
		this.roomNumber = roomNumber;
		this.roomType = roomType;
	}

}
