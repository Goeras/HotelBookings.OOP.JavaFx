package application;

import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.time.LocalDate;

public class LocalDatePersistenceDelegate extends PersistenceDelegate {
    
	// Override-metod för att lösa serialiseringsprocessen med LocalDate som XMLEncoder annars inte klarar av.
    @Override
    protected Expression instantiate(Object oldInstance, Encoder out) {
        LocalDate date = (LocalDate) oldInstance;
        return new Expression(oldInstance, oldInstance.getClass(), "parse", new Object[]{date.toString()}); // Parsar datumen till strängar som encodern klarar av att hantera.
    }

    // Override-metod för att jämföra om det nya objektet är en deserialiserad version av det gamla objektet.
    @Override
    protected boolean mutatesTo(Object oldInstance, Object newInstance) {
        return oldInstance.equals(newInstance);
    }
}
