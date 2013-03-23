package is.hr.escape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import is.hr.escape.objects.Car;

import java.util.List;

/**
 * User: David Einarsson
 * Date: 23.3.2013
 * Time: 10:11
 */
public class DrawView extends View {
    GameHandler handler;

    private Car _movingCar = null;
    private List<Car> _carList;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundColor(Color.WHITE);
    }
    public void setGameHandler(GameHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                _movingCar = null;
                break;
        }

        return true;
    }

}
