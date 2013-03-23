package is.hr.escape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    public void update() {
        //TODO: underlying game state has changed, update the display
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint linePainter = new Paint();
        linePainter.setColor(Color.RED);
        for (int irow = 1;irow<handler.getRows();irow++)
        {
            canvas.drawLine(0, getHeight()/handler.getRows()*irow, getWidth(), getHeight()/handler.getRows()*irow, linePainter );
        }
        for (int icol = 1;icol<handler.getRows();icol++)
        {
            canvas.drawLine(getWidth()/handler.getCols() * icol, 0, getWidth()/handler.getCols() * icol, getHeight(), linePainter );
        }
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
