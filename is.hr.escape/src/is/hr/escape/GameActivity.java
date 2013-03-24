package is.hr.escape;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import is.hr.escape.helpers.Orientation;
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
        drawView.invalidate();
    }

    public int getRows() {
        return logic.get_numRows();
    }

    public int getCols() {
        return logic.get_numCols();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        List<Car> cars = logic.getCars();
        bundle.putInt("carCount", cars.size());
        for(int i = 0; i < cars.size(); i++) {
            bundle.putInt("carid" + i, cars.get(i).getId());
            bundle.putInt("carcol" + i, cars.get(i).getCol());
            bundle.putInt("carrow" + i, cars.get(i).getRow());
            bundle.putBoolean("carvert" + i, cars.get(i).getOrientation() == Orientation.Vertical);
            bundle.putInt("carlen" + i, cars.get(i).getLength());
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        List<Car> cars = new ArrayList<Car>();
        int count = bundle.getInt("carCount", 0);
        for(int i = 0; i < count; i++) {
            int col, row, len, id;
            Orientation o;
            id = bundle.getInt("carid" + i, 0);
            col = bundle.getInt("carcol" + i, 0);
            row = bundle.getInt("carrow" + i, 0);
            len = bundle.getInt("carlen" + i, 0);
            o = bundle.getBoolean("carvert" + i, false) ? Orientation.Vertical : Orientation.Horizontal;
            Car car = new Car(o, col, row, len);
            car.setId(id);
            cars.add(car);
        }
        logic.setup(cars);
    }
}