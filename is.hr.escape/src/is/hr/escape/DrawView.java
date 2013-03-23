package is.hr.escape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import is.hr.escape.state.GameState;

/**
 * User: David Einarsson
 * Date: 23.3.2013
 * Time: 10:11
 */
public class DrawView extends View {
    private GameState state;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundColor(Color.WHITE);
    }

    public void setGameState(GameState state) {
        this.state = state;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
