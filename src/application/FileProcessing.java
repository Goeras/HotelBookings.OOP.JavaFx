package application;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class FileProcessing {

	public void serializeGuest(List<Guest> guestList) {
        
        try {
            String filepath = "./bookings/bookings.xml";
            File file = new File(filepath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            XMLEncoder xmlEncoder = new XMLEncoder(fileOutputStream);

            // Specialare för att XMLEncoder ska kunna hantera LocalDate
            xmlEncoder.setPersistenceDelegate(LocalDate.class, new LocalDatePersistenceDelegate());
            xmlEncoder.writeObject(guestList);
            xmlEncoder.close();
            fileOutputStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

	
	public List<Guest> deserializeGuest(List<Guest> guestList) {
		try {
			String filepath = "./bookings/bookings.xml";
			File file = new File(filepath);

			if (file.exists()) // kollar om filen existerar.
			{
				if(file.length() > 0) 
				{
					FileInputStream fileInputStream = new FileInputStream(file);
					XMLDecoder xmlDecoder = new XMLDecoder(fileInputStream);
					@SuppressWarnings("unchecked")
					List<Guest> deserializedGuestList = (List<Guest>) xmlDecoder.readObject();
					xmlDecoder.close();
					fileInputStream.close();
					guestList.clear();
					guestList.addAll(deserializedGuestList);
				}
				else {
					System.out.println("File is empty.");
				}
			} else {
				System.out.println("File does not exist: " + filepath);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("File is probably empty.");
		}

		return guestList;
	}

	public void serializeRoom(List<Room> roomList) {
        try {
            String filepath = "./rooms/room.xml";
            File file = new File(filepath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            XMLEncoder xmlEncoder = new XMLEncoder(fileOutputStream);

            // Specialare för att XMLEncoder ska kunna hantera LocalDate
            xmlEncoder.setPersistenceDelegate(LocalDate.class, new LocalDatePersistenceDelegate());

            xmlEncoder.writeObject(roomList);
            xmlEncoder.close();
            fileOutputStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
	
	public List<Room> deserializeRoom(List<Room>roomList)
	{
		try {
            String filepath = "./rooms/room.xml";
            File file = new File(filepath);
            
            if (file.exists()) // kollar om filen existerar.
            {
            	if(file.length() > 0) 
            	{
                FileInputStream fileInputStream = new FileInputStream(file);
                XMLDecoder xmlDecoder = new XMLDecoder(fileInputStream);
                @SuppressWarnings("unchecked")
				List<Room> deserializedRoomList = (List<Room>) xmlDecoder.readObject();
                xmlDecoder.close();
                fileInputStream.close();
                roomList.clear();
                roomList.addAll(deserializedRoomList);
            	}
            	else {
            		System.out.println("File is empty.");
            	}
            } else {
                System.out.println("File does not exist: " + filepath);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
        return roomList;
	}
	
}
