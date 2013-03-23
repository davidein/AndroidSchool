package is.hr.escape;

import android.app.Activity;
import android.os.Bundle;
import is.hr.escape.state.GameState;

/**
 * Created with IntelliJ IDEA.
 * User: heidar
 * Date: 3/23/13
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class GameActivity extends Activity {
    private GameState state;
    private DrawView view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        state = new GameState();
        view = (DrawView)findViewById(R.id.drawView);
        view.setGameState(state);


        setContentView(R.layout.game);
    }
}