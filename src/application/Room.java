package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Room {
	
	protected String roomName;
	protected String roomStandard;
	protected int roomNumber;
	protected String roomType; //(single, double, suite)
	protected List<Guest> guests = new ArrayList<>();
	protected List<LocalDate> dates = new ArrayList<>();
	
	
	public Room() {}
	
	public Room(String roomName, String roomStandard, int roomNumber, String roomType) {
		this.roomName = roomName;
		this.roomStandard = roomStandard;
		this.roomNumber = roomNumber;
		this.roomType = roomType;
	}

	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getRoomStandard() {
		return roomStandard;
	}

	public void setRoomStandard(String roomStandard) {
		this.roomStandard = roomStandard;
	}

	public String getSize() {
		return roomType;
	}

	public void setSize(String roomType) {
		this.roomType = roomType;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	public List<Guest> getGuest() {
		return guests;
	}
	public void addGuest(List<Guest> guest) {
		this.guests.addAll(guest);
	}
	public void addSingleGuest(Guest guest)
	{
		this.guests.add(guest);
	}
	public void removeGuest(List<Guest> guest) {
		this.guests.removeAll(guest);
	}

	public List<LocalDate> getDates() {
		return dates;
	}

	public void addDates(List<LocalDate> dates) {
		this.dates.addAll(dates);
	}
	public void removeDate(List<LocalDate> dates) {
		this.dates.removeAll(dates);
	}

	@Override
	public String toString() {
		return roomName + ", " + roomStandard;
	}
	
}

