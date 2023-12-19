package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Guest {

	private String name;
	private String eMail;
	private String phoneNumber;
	private int bookingNumber;
	private int roomNumber;
	private int NumberOfNights;
	private List<LocalDate> dates = new ArrayList<>();

	public Guest(String name, String eMail, String phoneNumber, int bookingNumber, int roomNumber, int numberOfNights, List<LocalDate> dates)
	{
		this.name = name;
		this.eMail = eMail;
		this.phoneNumber = phoneNumber;
		this.bookingNumber = bookingNumber;
		this.roomNumber = roomNumber;
		this.NumberOfNights = numberOfNights;
		this.dates.addAll(dates);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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

	public int getBookingNumber() {
		return bookingNumber;
	}

	public void setBookingNumber(int bookingNumber) {
		this.bookingNumber = bookingNumber;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	public int getNumberOfNights() {
		return NumberOfNights;
	}

	public void setNumberOfNights(int numberOfNights) {
		NumberOfNights = numberOfNights;
	}
		
	@Override
	public String toString() {
		return    "Namn:                   " + name + "\n"
				+ "E-mail:                   " + eMail + "\n"
				+ "Telefonnummer:   " + phoneNumber + "\n"
				+ "Bokningnummer:  " + bookingNumber + "\n"
				+ "Rumsnummer:      " + roomNumber + "\n"
				+ "Antal Nätter:         " + NumberOfNights + "\n"
				+ "Incheck:                 " + dates.get(0)+"\n"
				+ "Utcheck:                " + dates.get(dates.size()-1);
	}
	
	
	
}

