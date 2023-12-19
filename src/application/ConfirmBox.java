package application;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ConfirmBox {

	public boolean display(String title, String message)
	{
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Set custom button types (Yes and No)
        ButtonType buttonTypeYes = new ButtonType("Ja");
        ButtonType buttonTypeNo = new ButtonType("Nej");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        // Show the confirmation dialog and wait for the user's response
        alert.showAndWait();

        // Return true if the user clicked "Yes," false otherwise
        return alert.getResult() == buttonTypeYes;
	}
	
	public void alertBox(String title, String message)
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
