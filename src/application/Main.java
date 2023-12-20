package application;
	
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Main extends Application {
		
	List<Room> roomList = new ArrayList<>();
	List<Guest> guestList = new ArrayList<>();
	
	BookingStage bookingStage = new BookingStage(this);
	ConfirmBox confirmBox = new ConfirmBox();
	FileProcessing fileProcessing = new FileProcessing();
	
	Button btnNewBooking;
	Button btnShowBookings;
	Button btnQuit;
	Stage mainMenu;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			guestList = fileProcessing.deserializeGuest(guestList); // Läser in Guest-objekt från xml.
			roomList = fileProcessing.deserializeRoom(roomList); // Läser in Room-objekt från xml.
			if(roomList.isEmpty())
			{
				bookingStage.createRooms(); // OM roomList är tom så skapas rummen. (tex vid en första start av programmet.)
			}
			
			mainMenu = primaryStage;
			mainMenu.setTitle("Hotell Wigell");
			mainMenu.setOnCloseRequest( e -> {
				e.consume(); // consumar användarens val att stänga programmet, anropar istället metod closeProgram()
				closeProgram(); // closeProgram öppnar en ConfirmBox, sparar sedan objekt till xml-fil innan programmet avslutas.
			});
			
			// DatePicker, ger användaren möjlighet att välja datum i en liten "kalender"
			DatePicker datePicker = new DatePicker();
			datePicker.setDayCellFactory( param -> new DateCell());
			
			// Buttons
			btnNewBooking = new Button("Ny Bokning");
			btnShowBookings = new Button("Visa bokningar");
			btnQuit = new Button("Avsluta");
			
			btnNewBooking.setOnAction( e -> bookingStage.newBooking());
			btnShowBookings.setOnAction( e -> bookingStage.displayBookings());
			btnQuit.setOnAction( e -> closeProgram()); // Anropar metod som öppnar en ConfirmBox och sparar objekt till xml innan programmet avslutas.
			
			// Labels
			Label label = new Label();
			label.setText("HuvudMeny");
			label.setAlignment(Pos.CENTER);
			
			// Top Box
			HBox hBoxTop = new HBox();
			hBoxTop.getChildren().addAll(label);
			hBoxTop.setAlignment(Pos.CENTER);
			hBoxTop.setSpacing(20);
			hBoxTop.setPadding(new Insets (20));
			
			// Centre Box
			VBox buttons = new VBox();
			buttons.getChildren().addAll(btnNewBooking, btnShowBookings, btnQuit);
			buttons.setAlignment(Pos.CENTER);
			buttons.setSpacing(20);
			buttons.setPadding(new Insets (20));
			
			// Layout
			BorderPane layout = new BorderPane();
			layout.setTop(hBoxTop);
			layout.setCenter(buttons);
			//layout.setBottom(buttons);
			
			Scene scene = new Scene(layout,300,300);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm()); // Hämtar CSS-designen
			mainMenu.setScene(scene);
			mainMenu.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
		private void closeProgram()
		{
			Boolean answer = confirmBox.display("Bekräftelse", "Avsluta Program?");
			if (answer == true)
			{
				fileProcessing.serializeGuest(guestList); // Sparar Guest-objekt till fil.
				fileProcessing.serializeRoom(roomList); // Sparar Room-objekt till fil.
				mainMenu.close();
			}
		}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	// Getters & Setters
	public List<Room> getRoomList() {
		return roomList;
	}
	public void setRoomList(List<Room> roomList) {
		this.roomList.addAll(roomList);
	}
	public void addRoom(Room room){
		this.roomList.add(room);
	}
	public List<Guest> getGuestList() {
		return guestList;
	}
	public void setGuestList(List<Guest> guestList) {
		this.guestList.addAll(guestList);
	}
	public void addGuest(Guest guest) {
		this.guestList.add(guest);
	}
}
