package application;
	
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Main extends Application {
	
	
	BookingStage bookingStage = new BookingStage();
	ConfirmBox confirmBox = new ConfirmBox();
	
	Button btnNewBooking;
	Button btnShowBookings;
	Button btnQuit;
	Stage mainMenu;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			bookingStage.deSerialization(); // Läser in bokningar och rum från XML
			
			if(bookingStage.getRoomList().isEmpty())
			{
				bookingStage.createRooms(); // OM roomList är tom så skapas rummen. (tex vid en första start av programmet.)
			}
			
			mainMenu = primaryStage;
			mainMenu.setTitle("Hotell Wigell");
			mainMenu.setOnCloseRequest( e -> { // event då användaren stänger programmet på krysset uppe i hörnet.
				e.consume(); // consumar användarens val att stänga programmet, anropar istället metod closeProgram()
				closeProgram(); // closeProgram öppnar en ConfirmBox, sparar sedan objekt till xml-fil innan programmet avslutas.
			});
			
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
				bookingStage.serialization(); // Sparar bokningar och rum till XML innan avslut.
				mainMenu.close();
			}
		}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
