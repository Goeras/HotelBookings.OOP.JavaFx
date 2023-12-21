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

	private List<Room> roomList = new ArrayList<>();
	private List<Guest> guestList = new ArrayList<>();
	private List<Room> roomChoices;
	
	FileProcessing fileProcessing = new FileProcessing();
	ConfirmBox confirmBox = new ConfirmBox();
	
	

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
		checkInPicker.setMaxWidth(110);
		
		// -Utchekning (DatePicker)
		DatePicker checkOutPicker = new DatePicker();
		checkOutPicker.setDayCellFactory( param -> new DateCell());
		checkOutPicker.setPromptText("Avfärd");
		checkOutPicker.setMaxWidth(110);
		
		// Antal personer
		TextField numberOfGuestField = new TextField();
		numberOfGuestField.setPromptText("Antal Personer");
		numberOfGuestField.setMaxWidth(110);
		
		// Telefonnummer
        TextField nameField = new TextField();
        nameField.setPromptText("Namn");
        nameField.setMaxWidth(110);
		
		// Telefonnummer
        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Telefonnummer");
        phoneNumberField.setMaxWidth(110);
        
		// Mailadress
        TextField emailField = new TextField();
        emailField.setPromptText("E-mail");
        emailField.setMaxWidth(110);
        
        Button btnSearchRoom = new Button("Hitta rum");//Sök efter lediga/lämpliga rum, visa lista.
        btnSearchRoom.setOnAction( e -> {
        	try {
        		roomChoices = searchAvailableRoom(checkInPicker, checkOutPicker, numberOfGuestField); // Anropar metod för rumssökning med nödvändiga inparametrar.
        		if(!roomChoices.isEmpty()) {
        			Room room = displayOptions(roomChoices); // Anropar metod för att visa lista över lediga lämpliga rum att välja bland.
        			if (room != null) // OM användaren gjort sitt val så bokas rummet nedan.
        			{
        				boolean bookingComplete = bookTheRoom(checkInPicker, checkOutPicker, numberOfGuestField, nameField, phoneNumberField, emailField, room);
        				if(bookingComplete)
        				{
        					bookingStage.close(); // OM rummet bokades så stängs denna stage.
        				}
        			}
        		}
        		else {System.out.println("Roomlist is empty");}
        	} catch (NumberFormatException nfe) { // AlertBox om användaren försökt söka efter rum utan att fylla i nödvändiga uppgifter. Inparametrar specifika för just detta fall.
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
		
		// ListView med CheckBox för att visa befintliga bokningar samt ge användare möjlighet att avboka en eller flera bokningar.
		ListView<CheckBox> bookingView = new ListView<>();
		ObservableList<CheckBox> bookings = FXCollections.observableArrayList();
		for (Guest g : getGuestList()) // Lägger till varje befintlig bokning till visningslistan med checkbox.
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
        btnDelete.setOnAction( e -> { // Kontrollerar vilka CheckBoxar som är ikryssade och lägger till i en lista som sedan skickas till metod för avbokningar.
        	for(CheckBox c : bookings)
        	{
        		if (c.isSelected()) {
                    int index = bookings.indexOf(c);
                    bookingsToRemove.add(guestList.get(index));
                }
        	}
        	cancelBookings(bookingsToRemove);
            displayBookingStage.close();
        	
        });
        
        Button btnSearch =  new Button("Sök");
        btnSearch.setOnAction( e -> { // Anropar metod för att söka efter befintliga bokningar baserat på bokningsnummer.
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
        
        Scene scene = new Scene(borderPane, 400, 400);
        displayBookingStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        displayBookingStage.show();
	
	}
	
	public List<Room> searchAvailableRoom(DatePicker strCheckin, DatePicker strCheckout, TextField strNumberOfGuests)
	{
		if(roomList.isEmpty())
		{
			System.out.println("there are no created rooms");
		}
		List<Room> availableRooms =  new ArrayList<>(); // Lista för lämpliga/lediga rum som sedan returneras.
		try 
		{
			LocalDate checkIn = strCheckin.getValue();
			LocalDate checkOut = strCheckout.getValue();
			int numberOfGuests = Integer.parseInt(strNumberOfGuests.getText());
			List<LocalDate> wantedDates = new ArrayList<>();
			while (!checkIn.isAfter(checkOut)) // Lägger till alla datum från in-check och ut-check.
			{
				wantedDates.add(checkIn);
				checkIn = checkIn.plusDays(1);
			}
			if(numberOfGuests <= 2)
			{
				for(Room room : roomList) // SÖker efter alla rum..
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
				for(Room room : roomList)
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
				for(Room room : roomList)
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
			confirmBox.alertBox("Inga rum hittades", "Kontrollera att alla fällt är ifyllda"); // Öppnar en AlertBox om inga lämpliga rum hittas.
		}
		return availableRooms;
	}
	
	public Room displayOptions(List<Room> roomChoices) // Öppnar ett fönster för att visa en lista över bokningsbara rum.
	{
		final Room[] selectedRoom = {null}; // Variabeln behövde tydligen vara av typen final för att få manipuleras inuti en anonym inreklass / lambda-uttryck. Därav är variabeln en Array för att kunna ändra värdet innuti i arrayen istället för själva variabelns värde. Skulle även kunna gå att använda en AtomicReference<Room> för detta.
		Stage optionStage = new Stage();
		optionStage.setTitle("Rumslista");
		
		Label label = new Label();
		label.setText("Välj rum i listan");
		
		ListView<Room> roomListView = new ListView<>(FXCollections.observableArrayList(roomChoices)); // Visninsbar lista över möjliga rumsval.
		
		Button btnChoose = new Button("Välj");
		
		btnChoose.setOnAction(event -> {
	        selectedRoom[0] = roomListView.getSelectionModel().getSelectedItem(); // Tar användarens val av rum och lägger till i final Arrayen som kommenterades strax ovanför.
	        optionStage.close();
	    });
		
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
		
		return selectedRoom[0]; // Rerturnerar användarens val av rum.
	}
	
	// Boka rummet..
	public boolean bookTheRoom(DatePicker strCheckin, DatePicker strCheckout, TextField numberOfGuestField, TextField nameField, TextField phoneNumberField, TextField emailField, Room room)
	{
		boolean bookingComplete = false;
		try {
	    // hämtar värdet ifrån datatypen DatePicker till en LocalDate
		LocalDate checkIn = strCheckin.getValue();
		LocalDate checkOut = strCheckout.getValue();
		
		String name = nameField.getText();
		String eMail = emailField.getText();
		String phoneNumber = phoneNumberField.getText();
		int bookingNumber = createBookingNumber(); // Anropar metod och sätter värdet för bokningsnummer.
		int roomNumber = room.getRoomNumber();
		List<LocalDate> datesToBook = new ArrayList<>(); // Lista för alla datum som skall bokas.
		while (!checkIn.isAfter(checkOut)) // Lägger till alla datum från in-check till ut-check i Listan.
		{
			datesToBook.add(checkIn);
			checkIn = checkIn.plusDays(1);
		}
		int numberOfNights = datesToBook.size()-1; // Räknar ut och sätter värdet för antal nätter.
		
		// ConfirmBox med inparametrar för att bekräfta bokningen.
		Boolean answer = confirmBox.display("Bekräfta bokning", "Gästens namn: "+name+"\nTelefonnummer: "+phoneNumber+"\nE-Mailadress: "+eMail+"\nBokningsnummer: "+bookingNumber+"\nRumsnummer: "+roomNumber+"\nCheck-in: "+strCheckin.getValue()+"\nCheck-out: "+strCheckout.getValue());
		
		if(answer == true) // Om bokningen bekräftas så bokas rummet.
		{
		Guest guest = new Guest(name, eMail, phoneNumber, bookingNumber, roomNumber, numberOfNights, datesToBook); // Nytt Guest-objekt skapas upp med användarens ifyllda värden.
		guestList.add(guest); // Använder instansvaiabeln för att lägga till gäst i guestList.
		room.addDates(datesToBook);
		bookingComplete = true;
		}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Error in BookingStage.bookTheRoom()");
		}
		return bookingComplete; // Returnerar false eller true beroende på om användaren valde att bekräfta och skapa bokningen eller ej.
		
	}
	
	public void searchBookingByNumber()
	{
		TextInputDialog dialog = new TextInputDialog();
		// Hämtar CSS-designen till TextInputDialog-fönstret.
		dialog.getDialogPane().getStylesheets().add(
		        getClass().getResource("application.css").toExternalForm()
		);
	    dialog.setTitle("Sök efter bokning");
	    dialog.setHeaderText("Ange bokningsnummer:");
	    dialog.setContentText("Bokningsnummer:");


	    String bookingNumber = dialog.showAndWait().orElse(""); // .orElse sätter värdet för användares inmatning till "" om inget bokningsnummer anges.

	    if (!bookingNumber.isEmpty()) {

	    	try {
	    		int intBookingNumber = Integer.parseInt(bookingNumber); // Parsar användarens input till en int.
	    		List<Guest> guestForRemoval = new ArrayList<>();
	    		for(Guest guest : guestList)
	    		{
	    			if(guest.getBookingNumber() == intBookingNumber)
	    			{
	    				guestForRemoval.add(guest);
	    			}
	    		}
	    		if(guestForRemoval.isEmpty()) {
	    			confirmBox.alertBox("Ingen boking hittades", "Kontrollera bokningsnumret");
	    		}
	    		else {
	    			displayBookingDetails(guestForRemoval); // Anropar metod för att visa bokning för användare. 
	    		}
	    	} catch (NumberFormatException e) {
	    		confirmBox.alertBox("Fel inmatning", "Ange bokningsnummer med siffror.");
	    	}
	    }
	    else {
	    	confirmBox.alertBox("Ingen inmatning", "Ange ett bokningsnummer.");
	    }
	}
	private void displayBookingDetails(List<Guest> matchingBookings) { // Visar bokning för användaren.
	    Stage stage = new Stage();
	    stage.setTitle("Sökresultat");

	    // Visningsfönster för matchande bokningar.
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
	    cancelBookings(matchingBookings); // Anropar metod för att avboka.
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
			Boolean answer = confirmBox.display("Bekräftelse", "Avboka?"); // ConfirmBox för att bekräfta avbokningen.
			if (answer == true)
			{
				for(Guest guest : bookingsToCancel) { // För varje gäst listan för avbokning.
					for(Room room : roomList){ // För varje rum i rumsLista.
						if(room.getRoomNumber() == guest.getRoomNumber()) // Om rumsnumret för rum matchar gästens rumsnummer.
						{
							List<Guest> removeGuestList = new ArrayList<>();
							removeGuestList.add(guest);
							room.removeGuest(removeGuestList); // Ta bort gästen ur rummets gästlista.

							List<LocalDate> removeDatesList = new ArrayList<>();
							removeDatesList.addAll(guest.getDates()); 
							room.removeDate(removeDatesList); // ta bort gästens tidigare bokade datum från rummets datumlista.
						}
					}
				}
				guestList.removeAll(bookingsToCancel); // Tar bort alla avbokningar ifrån gästlistan.
				
			}
		} catch (Exception e) {
			System.out.println("Nu small det som F_N i cancelBookings()");
			e.printStackTrace();
		}
	}

	public int createBookingNumber()
	{
		Random random = new Random();
		int bookingNumber = random.nextInt(9000)+1000; // Skapar ett random bokningsnummer mellan 1000 och 9999.
		
		for(Guest guest : guestList) // Kontrollerar om bokningsnumret är unikt eller redan finns i systemet.
		{
			if(guest.getBookingNumber() == bookingNumber)
			{
				return createBookingNumber(); // om bokningsnumret redan finns så upprepas metoden tills dess att ett unikt nummer slumpats fram.
			}
		}
		
		return bookingNumber; // Returnerar ett unikt bokningsnummer.
	}
	
	public void createRooms() // Metod för att skapa upp nya rum om roomList är tom.
	{
		
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
		
	}
	
	public void serialization() // sparar ner Guest och Room objekten till xml.
	{
		fileProcessing.serializeRoom(roomList);
		fileProcessing.serializeGuest(guestList);
	}
	
	public void deSerialization() // Läser in Guest och Room objekten från xml.
	{
		roomList = fileProcessing.deserializeRoom(roomList);
		guestList = fileProcessing.deserializeGuest(guestList);
	}

	// Getters & Setters.
	public List<Room> getRoomList() {
		return roomList;
	}

	public void setRoomList(List<Room> roomList) {
		this.roomList = roomList;
	}
	
	public void addRoomList(List<Room> roomList) {
		this.roomList.addAll(roomList);
	}

	public List<Guest> getGuestList() {
		return guestList;
	}

	public void setGuestList(List<Guest> guestList) {
		this.guestList = guestList;
	}
	
	public void addGuestList(List<Guest> guestList) {
		this.guestList.addAll(guestList);
	}
	
	
	
}
