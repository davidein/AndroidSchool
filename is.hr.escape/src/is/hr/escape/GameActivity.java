package is.hr.escape;

import android.app.Activity;
import android.os.Bundle;
import is.hr.escape.logic.GameLogic;

/**
 * Created with IntelliJ IDEA.
 * User: heidar
 * Date: 3/23/13
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class GameActivity extends Activity {
    private GameLogic logic;
    private DrawView drawView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logic = new GameLogic();
        setContentView(R.layout.game);

        drawView = (DrawView) findViewById(R.id.drawView);

        drawView.set_cars(logic.getCars());
    }
}