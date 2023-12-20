package application;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ConfirmBox {

	public boolean display(String title, String message) // En ConfirmBox som tar strängar för titel och meddelande för att kunna använda metoden i olika sammanhang.
	{
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType buttonTypeYes = new ButtonType("Ja");
        ButtonType buttonTypeNo = new ButtonType("Nej");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        alert.showAndWait();
        
        return alert.getResult() == buttonTypeYes; // Returnerar true om användaren tryckt "JA", annars false.
	}
	
	public void alertBox(String title, String message) // En AlertBox som tar strängar för titel och meddelande för att kunna använda metoden i olika sammanhang.
	{
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        ButtonType buttonTypeOK = new ButtonType("Ok");
        
        alert.getButtonTypes().setAll(buttonTypeOK);
        alert.showAndWait();
	}
	
}
