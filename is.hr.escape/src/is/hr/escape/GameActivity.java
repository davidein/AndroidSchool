package is.hr.escape;

import android.app.Activity;
import android.os.Bundle;
import is.hr.escape.logic.Action;
import is.hr.escape.logic.GameLogic;
import is.hr.escape.objects.Car;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: heidar
 * Date: 3/23/13
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class GameActivity extends Activity implements GameHandler {
    private GameLogic logic;
    private DrawView drawView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logic = new GameLogic();
        setContentView(R.layout.game);

        logic.setup("(H 1 3 2), (V 0 0 2), (V 0 2 3), (H 0 5 2), (H 2 0 3), (H 4 1 2), (V 3 2 3), (V 5 3 3)");

        drawView = (DrawView) findViewById(R.id.drawView);
        drawView.setGameHandler(this);
    }

    public List<Car> getCars() {
        return logic.getCars();
    }

    public List<Action> getActionsFor(Car car) {
        List<Action> allActions =  logic.getActions();
        List<Action> actions = new ArrayList<Action>();

        for(Action action : allActions) {
            if(action.getId() == car.getId()) {
                actions.add(action);
            }
        }

        return actions;
    }

    public void actionPerformed(Action action) {
        logic.makeAction(action);
        drawView.update();
    }

    public int getRows() {
        return logic.get_numRows();
    }

    public int getCols() {
        return logic.get_numCols();
    }
}