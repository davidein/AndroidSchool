package is.hr.escape;

import is.hr.escape.logic.Action;
import is.hr.escape.objects.Car;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: heidar
 * Date: 3/23/13
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GameHandler {
    List<Car> getCars();
    List<Action> getActionsFor(Car car);
    void actionPerformed(Action action);
}
