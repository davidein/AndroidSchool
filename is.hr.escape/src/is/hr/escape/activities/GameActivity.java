package is.hr.escape.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import is.hr.escape.DrawView;
import is.hr.escape.GameHandler;
import is.hr.escape.fragments.GameOverFragment;
import is.hr.escape.R;
import is.hr.escape.objects.Challenge;
import is.hr.escape.objects.Level;
import is.hr.escape.helpers.SQLHelper;
import is.hr.escape.logic.Action;
import is.hr.escape.logic.GameLogic;
import is.hr.escape.objects.Car;
import is.hr.escape.objects.Car.Orientation;

import java.util.ArrayList;
import java.util.List;

/**
 * The GameActivity is where all the magic happens. It is started with a given level but when levels are
 * completed, the user can navigate to the next level as well.
 *
 * It uses a GameLogic class which handles the business logic for the game while the activity
 * controls rendering of the game state and delegates user input to the game logic.
 */
public class GameActivity extends Activity implements GameHandler {
    private GameLogic m_logic;
    private DrawView m_drawView;
    private String m_currentLevel;
    private int m_levelId;
    private int m_challengeId;

    private TextView m_levelTextView;
    private TextView m_challengeTextView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_logic = new GameLogic();
        setContentView(R.layout.game);

        m_drawView = (DrawView) findViewById(R.id.drawView);
        m_drawView.setGameHandler(this);

        m_levelTextView = (TextView) findViewById(R.id.level);
        m_challengeTextView = (TextView) findViewById(R.id.challenge);

        m_currentLevel = getIntent().getStringExtra("level");
        m_levelId = getIntent().getIntExtra("levelId", 0);
        m_challengeId = getIntent().getIntExtra("challengeId", 0);

        m_levelTextView.setText(String.format("Level %d", m_levelId));

        SQLHelper sqlHelper = new SQLHelper(getBaseContext());
        Challenge challenge = sqlHelper.getChallenge( m_challengeId);
        m_challengeTextView.setText(challenge.getName());

        setup();
    }


    /**
     * Displays the game over popup over the main screen
     */
    private void gameOver() {
        SQLHelper sqlHelper = new SQLHelper(getBaseContext());

        sqlHelper.saveLevel(m_challengeId, m_levelId, m_logic.getMoveCount());

        DialogFragment fragment = new GameOverFragment(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Clicked quit
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Clicked next
                SQLHelper sqlHelper = new SQLHelper(getBaseContext());
                Level level = sqlHelper.getNextLevel(m_challengeId, m_levelId);
                m_currentLevel = level.getLevel();
                m_levelId = level.getLevelId();
                m_challengeId = level.getChallengeId();

                m_levelTextView.setText(String.format("Level %d", m_levelId));

                Challenge challenge = sqlHelper.getChallenge( m_challengeId);
                m_challengeTextView.setText(challenge.getName());
                setup();
                updateMoves();
            }
        }, m_logic.getMoveCount());
        fragment.setCancelable(false);

        fragment.show(getFragmentManager(), "david er spes");
    }

    /**
     * Updates the current moves textbox to the value stored in the activity
     */
    private void updateMoves() {
        TextView moves = (TextView)findViewById(R.id.moves);
        moves.setText(String.valueOf(m_logic.getMoveCount()));
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        List<Car> cars = m_logic.getCars();
        bundle.putInt("moveCount", m_logic.getMoveCount());
        bundle.putInt("carCount", cars.size());
        for(int i = 0; i < cars.size(); i++) {
            bundle.putInt("carid" + i, cars.get(i).getId());
            bundle.putInt("carcol" + i, cars.get(i).getCol());
            bundle.putInt("carrow" + i, cars.get(i).getRow());
            bundle.putBoolean("carvert" + i, cars.get(i).getOrientation() == Car.Orientation.Vertical);
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
        m_logic.setup(cars);
        m_logic.setMoveCount(moveCount);
        updateMoves();
    }

    /**
     * Sets the initial positions of the game according to the current level setup
     */
    private void setup() {
        m_logic.setup(m_currentLevel);
        m_drawView.enableTouch();
        m_drawView.invalidate();
    }

    /**
     * Callback method for when the user clicks the restart button
     */
    public void restart(View view) {
        setup();
        updateMoves();
        m_drawView.invalidate();
    }

    /** Begin GameHandler Implementation **/
    public List<Car> getCars() {
        return m_logic.getCars();
    }

    public List<Action> getActionsFor(Car car) {
        List<Action> allActions =  m_logic.getActions();
        List<Action> actions = new ArrayList<Action>();

        for(Action action : allActions) {
            if(action.getId() == car.getId()) {
                actions.add(action);
            }
        }

        return actions;
    }

    public void actionPerformed(Action action) {
        m_logic.makeAction(action);
        m_drawView.invalidate();
        updateMoves();
        if(m_logic.isSolved()) {
            m_drawView.disableTouch();
            gameOver();
        }
    }

    public int getRows() {
        return m_logic.get_numRows();
    }

    public int getCols() {
        return m_logic.get_numCols();
    }

    /** End GameHandler Implementation **/
}