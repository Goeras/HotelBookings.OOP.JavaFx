package application;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Guest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7616983092965566335L;
	private String name;
	private String eMail = "";
	private String phoneNumber;
	private int bookingNumber;
	private int roomNumber;
	private int NumberOfNights;
	private List<LocalDate> dates = new ArrayList<>();

	// Konstruktorer
	public Guest() {}

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
	
	// Getter & Setters
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEMail() {
		return eMail;
	}

	public void setEMail(String eMail) {
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

	public void setDates(List<LocalDate> dates) {
		this.dates = dates;
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
	    StringBuilder stringBuilder = new StringBuilder();

	    stringBuilder.append("Namn:                   ").append(name).append("\n")
	            .append("E-mail:                  ").append(eMail).append("\n")
	            .append("Telefonnummer:   ").append(phoneNumber).append("\n")
	            .append("Bokningnummer:  ").append(bookingNumber).append("\n")
	            .append("Rumsnummer:      ").append(roomNumber).append("\n")
	            .append("Antal Nätter:         ").append(NumberOfNights).append("\n");

	    if (!dates.isEmpty()) {
	        stringBuilder.append("Incheck:                 ").append(dates.get(0)).append("\n")
	                .append("Utcheck:                ").append(dates.get(dates.size() - 1)).append("\n");
	    } else {
	        stringBuilder.append("Incheck:                 ").append("Inget datum tillgängligt").append("\n")
	                .append("Utcheck:                ").append("Inget datum tillgängligt").append("\n");
	    }

	    return stringBuilder.toString();
	}
	
	
	
}

