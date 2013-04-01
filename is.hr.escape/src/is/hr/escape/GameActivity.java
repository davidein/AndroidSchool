package is.hr.escape;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import is.hr.escape.helpers.Orientation;
import is.hr.escape.helpers.SQLHelper;
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
    private String currentLevel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logic = new GameLogic();
        setContentView(R.layout.game);

        drawView = (DrawView) findViewById(R.id.drawView);
        drawView.setGameHandler(this);

        currentLevel = getIntent().getStringExtra("level");

        setup();
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
        updateMoves();
        if(logic.isSolved()) {
            drawView.disableTouch();
            gameOver();
        }
    }

    private void gameOver() {
        int currentLevel = 1;
        SQLHelper sqlHelper = new SQLHelper(getBaseContext());

        sqlHelper.saveScore(currentLevel, logic.getMoveCount(), 1);

        DialogFragment fragment = new GameOverFragment(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Clicked quit
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Clicked next
                setup();
            }
        }, logic.getMoveCount());
        fragment.setCancelable(false);

        fragment.show(getFragmentManager(), "derp");
    }

    private void updateMoves() {
        TextView moves = (TextView)findViewById(R.id.moves);
        moves.setText(String.valueOf(logic.getMoveCount()));
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
        bundle.putInt("moveCount", logic.getMoveCount());
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
        int moveCount = bundle.getInt("moveCount", 0);
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
        logic.setMoveCount(moveCount);
        updateMoves();
    }

    private void setup() {
        logic.setup(currentLevel);
        drawView.enableTouch();
        drawView.invalidate();
    }

    public void restart(View view) {
        setup();
        updateMoves();
        drawView.invalidate();
    }
}