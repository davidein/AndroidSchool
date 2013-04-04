package is.hr.escape;

import is.hr.escape.logic.Action;
import is.hr.escape.objects.Car;

import java.util.List;

/**
 * A game handler interface allows the UI code to send and receive messages from the controller (Activity)
 * regarding the current state of the game
 */
public interface GameHandler {
    /**
     * @return Returns a list of all cars
     */
    List<Car> getCars();

    /**
     * @return Returns all legal actions for a given car
     */
    List<Action> getActionsFor(Car car);

    /**
     * An action was selected by the user to be performed
     */
    void actionPerformed(Action action);

    /**
     * @return Returns the number of rows used in the current puzzle
     */
    int getRows();

    /**
     * @return Returns the number of columns used in the current puzzle
     */
    int getCols();
}
