package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookingStage {

	private List<Room> roomChoices;
	ConfirmBox confirmBox = new ConfirmBox();
	
	// För åtkomst till getters & setters till Listorna i Main..
	private Main mainInstance;
	public BookingStage(Main mainInstance)
	{
		this.mainInstance = mainInstance;
	}

	// Metoder
	public void newBooking()
	{
		
		Stage bookingStage = new Stage();
		bookingStage.setTitle("Ny Bokning");
		bookingStage.initModality(Modality.APPLICATION_MODAL); // Avgränsar så att användare ej kan interagera med bakomvarande fönster
		
		Label label = new Label();
		label.setText("Fyll i info nedan");
		
		
		// Inchekning (DatePicker)
		DatePicker checkInPicker = new DatePicker();
		checkInPicker.setDayCellFactory( param -> new DateCell());
		checkInPicker.setPromptText("Ankomst");
		checkInPicker.setMaxWidth(100);
		
		// -utchekning (DatePicker)
		DatePicker checkOutPicker = new DatePicker();
		checkOutPicker.setDayCellFactory( param -> new DateCell());
		checkOutPicker.setPromptText("Avfärd");
		checkOutPicker.setMaxWidth(100);
		
		// Antal personer
		TextField numberOfGuestField = new TextField();
		numberOfGuestField.setPromptText("Antal Personer");
		numberOfGuestField.setMaxWidth(100);
		
		// Telefonnummer
        TextField nameField = new TextField();
        nameField.setPromptText("Namn");
        nameField.setMaxWidth(100);
		
		// Telefonnummer
        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Telefonnummer");
        phoneNumberField.setMaxWidth(100);
        
		// Mailadress
        TextField emailField = new TextField();
        emailField.setPromptText("E-mail");
        emailField.setMaxWidth(100);
        
        // Översätter DatePicker och TextField till LocalDate och String för att sedan kunna kontrollera om användaren fyllt i värden.
        
        
        Button btnSearchRoom = new Button("Hitta rum");//sök lediga/lämpliga rum, visa lista.
        btnSearchRoom.setOnAction( e -> {
        	try {
        		roomChoices = searchAvailableRoom(checkInPicker, checkOutPicker, numberOfGuestField);
        		if(!roomChoices.isEmpty()) {
        			Room room = displayOptions(roomChoices);
        			if (room != null)
        			{
        				boolean bookingComplete = bookTheRoom(checkInPicker, checkOutPicker, numberOfGuestField, nameField, phoneNumberField, emailField, room);
        				if(bookingComplete)
        				{
        					bookingStage.close();
        				}
        			}
        		}
        	} catch (NumberFormatException nfe) {
        		confirmBox.alertBox("Inga rum hittades", "Säkerställ att alla fällt är korrekt ifyllda, eller välj annat datum");
        	}

        });
        
        
        Button btnQuit = new Button("Tillbaka");
        btnQuit.setOnAction( e -> bookingStage.close());
        
        HBox hBoxTop = new HBox();
        hBoxTop.getChildren().addAll(label);
        hBoxTop.setAlignment(Pos.CENTER);
        hBoxTop.setSpacing(10);
        hBoxTop.setPadding(new Insets (10,10,10,10));
        
        VBox vBoxCentre = new VBox();
        vBoxCentre.getChildren().addAll(checkInPicker, checkOutPicker, numberOfGuestField, nameField, phoneNumberField, emailField);
        vBoxCentre.setAlignment(Pos.CENTER);
        vBoxCentre.setSpacing(10);
        vBoxCentre.setPadding(new Insets (10,10,10,10));
        
        HBox hBoxBottom = new HBox();
        hBoxBottom.getChildren().addAll(btnSearchRoom, btnQuit);
        hBoxBottom.setAlignment(Pos.CENTER);
        hBoxBottom.setSpacing(10);
        hBoxBottom.setPadding(new Insets (10,10,10,10));
        
        VBox layout = new VBox();
        layout.getChildren().addAll(hBoxTop, vBoxCentre, hBoxBottom);
        
        Scene scene = new Scene(layout, 300, 400);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        bookingStage.setScene(scene);
        bookingStage.show();
	}
	
	public void displayBookings()
	{
		
		
		Stage displayBookingStage = new Stage();
		displayBookingStage.setTitle("Ny Bokning");
		displayBookingStage.initModality(Modality.APPLICATION_MODAL); // Avgränsar så att användare ej kan interagera med bakomvarande fönster
	
		Label label = new Label();
		label.setText("Bokningar");
		
		ListView<CheckBox> bookingView = new ListView<>();
		ObservableList<CheckBox> bookings = FXCollections.observableArrayList();
		for (Guest g : mainInstance.getGuestList())
		{
			CheckBox checkBox = new CheckBox(g.toString());
			bookings.add(checkBox);
		}
		bookingView.setItems(bookings);
		bookingView.setMinWidth(384);
		
		ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(bookingView);
		
		Button btnQuit = new Button("Tillbaka");
        btnQuit.setOnAction( e -> displayBookingStage.close());
        
        Button btnDelete = new Button("Avboka");
        List<Guest> bookingsToRemove = new ArrayList<>();
        btnDelete.setOnAction( e -> {
        	for(CheckBox c : bookings)
        	{
        		if (c.isSelected()) {
                    int index = bookings.indexOf(c);
                    bookingsToRemove.add(mainInstance.guestList.get(index));
                }
        	}
        	cancelBookings(bookingsToRemove);
            displayBookingStage.close();
        	
        });
        
        Button btnSearch =  new Button("Sök");
        btnSearch.setOnAction( e -> {
        	searchBookingByNumber();
        	displayBookingStage.close();
        });
        
        
        VBox vBoxTop = new VBox();
        vBoxTop.getChildren().addAll(label);
        vBoxTop.setAlignment(Pos.CENTER);
        vBoxTop.setSpacing(10);
        vBoxTop.setPadding(new Insets(10));
        
        HBox vBoxBottom = new HBox();
        vBoxBottom.getChildren().addAll(btnSearch, btnDelete, btnQuit);
        vBoxBottom.setAlignment(Pos.CENTER);
        vBoxBottom.setSpacing(10);
        vBoxBottom.setPadding(new Insets(10));
        
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(vBoxTop);
        borderPane.setCenter(scrollPane);
        borderPane.setBottom(vBoxBottom);
        //BorderPane.setAlignment(vBoxBottom, Pos.BOTTOM_RIGHT);
        
        Scene scene = new Scene(borderPane, 400, 400);
        displayBookingStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        displayBookingStage.show();
	
	}
	
	public List<Room> searchAvailableRoom(DatePicker strCheckin, DatePicker strCheckout, TextField strNumberOfGuests)
	{
		List<Room> availableRooms =  new ArrayList<>(); // Returnera denna eller göra något annat?
		try 
		{
			LocalDate checkIn = strCheckin.getValue();
			LocalDate checkOut = strCheckout.getValue();
			int numberOfGuests = Integer.parseInt(strNumberOfGuests.getText());
			List<LocalDate> wantedDates = new ArrayList<>();
			while (!checkIn.isAfter(checkOut)) // Lägger till alla datum från in-check till ut-check i Listan.
			{
				wantedDates.add(checkIn);
				checkIn = checkIn.plusDays(1);
			}
			if(numberOfGuests <= 2)
			{
				for(Room room : mainInstance.getRoomList()) // SÖker efter alla rum..
				{
					boolean containsAnyDate = room.getDates().stream().anyMatch(wantedDates::contains); // Kollar om önskade datum finns i bokade datum för varje rum.
					if(!containsAnyDate)
					{
						availableRooms.add(room); // Lägger till rummet i listan om datumet inte redan är bokat.
					}
				}
			}
			else if(numberOfGuests <= 4)
			{
				for(Room room : mainInstance.getRoomList())
				{
					if(room instanceof DoubleRoom || room instanceof SuiteRoom) // Söker efter dubbelrum och suite..
					{
						boolean containsAnyDate = room.getDates().stream().anyMatch(wantedDates::contains); // Kollar om önskade datum finns i bokade datum för varje rum.
						if(!containsAnyDate)
						{
							availableRooms.add(room); // Lägger till rummet i listan om datumet inte redan är bokat.
						}
					}
				}
			}
			else if(numberOfGuests <= 6)
			{
				for(Room room : mainInstance.getRoomList())
				{
					if(room instanceof SuiteRoom) // söker enbart efter suite..
					{
						boolean containsAnyDate = room.getDates().stream().anyMatch(wantedDates::contains); // Kollar om önskade datum finns i bokade datum för varje rum.
						if(!containsAnyDate)
						{
							availableRooms.add(room); // Lägger till rummet i listan om datumet inte redan är bokat.
						}
					}
				}
			}
		}
		catch (NumberFormatException nfe) {
			confirmBox.alertBox("Inga rum hittades", "Kontrollera att alla fällt är ifyllda");
		}
		return availableRooms;
	}
	
	public Room displayOptions(List<Room> roomChoices)
	{
		final Room[] selectedRoom = {null};
		Stage optionStage = new Stage();
		optionStage.setTitle("Rumslista");
		
		Label label = new Label();
		label.setText("Välj rum i listan");
		
		ListView<Room> roomListView = new ListView<>(FXCollections.observableArrayList(roomChoices));
		
		Button btnChoose = new Button("Välj");
		
		btnChoose.setOnAction(event -> {
	        selectedRoom[0] = roomListView.getSelectionModel().getSelectedItem();
	        optionStage.close();
	    });
		
		//Button btnQuit = new Button("Avsluta");
		
		VBox vBoxTop = new VBox();
		vBoxTop.getChildren().addAll(label);
		vBoxTop.setAlignment(Pos.CENTER);
		
		
		HBox hBox = new HBox();
		hBox.getChildren().addAll(btnChoose);
		
		BorderPane layout = new BorderPane();
		layout.setTop(vBoxTop);
		layout.setCenter(roomListView);
		layout.setBottom(hBox);
		
		Scene scene = new Scene(layout, 300, 400);
		optionStage.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		optionStage.showAndWait();
		
		return selectedRoom[0];
	}
	
	// Boka rummet..
	public boolean bookTheRoom(DatePicker strCheckin, DatePicker strCheckout, TextField numberOfGuestField, TextField nameField, TextField phoneNumberField, TextField emailField, Room room)
	{
		boolean bookingComplete = false;;
		try {
		LocalDate checkIn = strCheckin.getValue();
		LocalDate checkOut = strCheckout.getValue();
		
		String name = nameField.getText();
		String eMail = emailField.getText();
		String phoneNumber = phoneNumberField.getText();
		int bookingNumber = createBookingNumber();
		int roomNumber = room.getRoomNumber();
		List<LocalDate> datesToBook = new ArrayList<>();
		while (!checkIn.isAfter(checkOut)) // Lägger till alla datum från in-check till ut-check i Listan.
		{
			datesToBook.add(checkIn);
			checkIn = checkIn.plusDays(1);
		}
		int numberOfNights = datesToBook.size()-1;
		
		Boolean answer = confirmBox.display("Bekräfta bokning", "Gästens namn: "+name+"\nTelefonnummer: "+phoneNumber+"\nE-Mailadress: "+eMail+"\nBokningsnummer: "+bookingNumber+"\nRumsnummer: "+roomNumber+"\nCheck-in: "+strCheckin.getValue()+"\nCheck-out: "+strCheckout.getValue());
		
		if(answer == true)
		{
		Guest guest = new Guest(name, eMail, phoneNumber, bookingNumber, roomNumber, numberOfNights, datesToBook);
		mainInstance.addGuest(guest);
		room.addDates(datesToBook);
		bookingComplete = true;
		}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Error in BookingStage.bookTheRoom()");
		}
		return bookingComplete;
		
	}
	
	public void searchBookingByNumber()
	{
		TextInputDialog dialog = new TextInputDialog();
		//dialog.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		dialog.getDialogPane().getStylesheets().add(
		        getClass().getResource("application.css").toExternalForm()
		);
	    dialog.setTitle("Sök efter bokning");
	    dialog.setHeaderText("Ange bokningsnummer:");
	    dialog.setContentText("Bokningsnummer:");

	    
	    String bookingNumber = dialog.showAndWait().orElse("");

	    if (!bookingNumber.isEmpty()) {
	    	
	    	try {
				int intBookingNumber = Integer.parseInt(bookingNumber);
				List<Guest> guestForRemoval = new ArrayList<>();
				for(Guest guest : mainInstance.getGuestList())
				{
					if(guest.getBookingNumber() == intBookingNumber)
					{
						guestForRemoval.add(guest);
					}
				}
				displayBookingDetails(guestForRemoval);
			} catch (NumberFormatException e) {
				confirmBox.alertBox("Fel inmatning", "Ange bokningsnummer med siffror.");
			}
	    }
	    else {
	    	confirmBox.alertBox("Ingen inmatning", "Ange ett bokningsnummer.");
	    }
	}
	private void displayBookingDetails(List<Guest> matchingBookings) {
	    Stage stage = new Stage();
	    stage.setTitle("Sökresultat");

	    ListView<Guest> bookingView = new ListView<>();
	    ObservableList<Guest> bookings = FXCollections.observableArrayList();
	    bookingView.setMaxHeight(145);
	    for (Guest guest : matchingBookings)
	    {
	    	bookings.add(guest);
	    }
	    bookingView.setItems(bookings);

	    Button btnDelete = new Button("Avboka");
	    btnDelete.setOnAction( e -> {
	    cancelBookings(matchingBookings);
	    stage.close();
	    });
	    
	    Button btnCancel = new Button("Avbryt");
	    btnCancel.setOnAction( e -> stage.close());
	    
	    VBox vBoxList = new VBox();
	    vBoxList.getChildren().addAll(bookingView);
	    vBoxList.setSpacing(10);
	    vBoxList.setPadding(new Insets(10));
	    vBoxList.setAlignment(Pos.CENTER);
	    
	    HBox HBoxButtons = new HBox();
	    HBoxButtons.getChildren().addAll(btnDelete, btnCancel);
	    HBoxButtons.setSpacing(10);
	    HBoxButtons.setPadding(new Insets(10));
	    HBoxButtons.setAlignment(Pos.CENTER);
	    
	    BorderPane layout = new BorderPane();
	    layout.setTop(vBoxList);
	    layout.setCenter(HBoxButtons);
	    
	    Scene scene = new Scene(layout, 300, 300);
	    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	    stage.setScene(scene);
	    stage.show();
	}
	
	
	public void cancelBookings(List<Guest> bookingsToCancel)
	{
		try {
			Boolean answer = confirmBox.display("Bekräftelse", "Avboka?");
			if (answer == true)
			{
				for(Guest guest : bookingsToCancel) {
					for(Room room : mainInstance.roomList){
						if(room.getRoomNumber() == guest.getRoomNumber())
						{
							List<Guest> removeGuestList = new ArrayList<>();
							removeGuestList.add(guest);
							room.removeGuest(removeGuestList); // Ta bort gästen ur rummets gästlista.

							List<LocalDate> removeDatesList = new ArrayList<>();
							removeDatesList.addAll(guest.getDates()); 
							room.removeDate(removeDatesList); // ta bort gästen datum från rummets datumlista.
						}
					}
				}
				mainInstance.guestList.removeAll(bookingsToCancel);
				
			}
		} catch (Exception e) {
			System.out.println("Nu small det som F_N i cancelBookings()");
			e.printStackTrace();
		}
	}

	public int createBookingNumber()
	{
		Random random = new Random();
		int bookingNumber = random.nextInt(9000)+1000;
		for(Guest guest : mainInstance.getGuestList())
		{
			if(guest.getBookingNumber() == bookingNumber)
			{
				return createBookingNumber();
			}
		}
		
		return bookingNumber;
	}
	
	public List<Room> createRooms()
	{
		List<Room> roomList = new ArrayList<>();
		Room singleRoom1 = new SingleRoom("Single Room 1", "yard view", 1, "Single");
		roomList.add(singleRoom1);
		Room singleRoom2 = new SingleRoom("Single Room 2", "yard view", 2, "Single");
		roomList.add(singleRoom2);
		Room singleRoom3 = new SingleRoom("Single Room 3", "Street view", 3, "Single");
		roomList.add(singleRoom3);
		Room singleRoom4 = new SingleRoom("Single Room 4", "Street view", 4, "Single");
		roomList.add(singleRoom4);
		Room singleRoom5 = new SingleRoom("Single Room 5", "Sea view", 5, "Single");
		roomList.add(singleRoom5);
		Room singleRoom6 = new SingleRoom("Single Room 6", "Sea view", 6, "Single");
		roomList.add(singleRoom6);
		
		Room doubleRoom1 = new DoubleRoom("Double Room 1", "yard view", 7, "Double");
		roomList.add(doubleRoom1);
		Room doubleRoom2 = new DoubleRoom("Double Room 2", "yard view", 8, "Double");
		roomList.add(doubleRoom2);
		Room doubleRoom3 = new DoubleRoom("Double Room 3", "Street view", 9, "Double");
		roomList.add(doubleRoom3);
		Room doubleRoom4 = new DoubleRoom("Double Room 4", "Sea view", 10, "Double");
		roomList.add(doubleRoom4);
		
		Room suiteRoom1 = new SuiteRoom("Suite Room 1", "Sea view", 11, "Suite");
		roomList.add(suiteRoom1);
		Room suiteRoom2 = new SuiteRoom("Suite Room 2", "Sea view", 12, "Suite");
		roomList.add(suiteRoom2);
		
		return roomList;
	}
	
}
