package is.hr.escape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import is.hr.escape.helpers.Orientation;
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

        drawGrid(canvas);

        int baseWidth = getWidth() / handler.getCols();
        int baseHeight = getHeight() / handler.getRows();

        for (Car car : handler.getCars())
        {
            Paint carPainter = new Paint();
            carPainter.setColor(Color.BLACK);

            Rect carRect = new Rect();
            if (car.getOrientation() == Orientation.Horizontal)
            {
                carRect.set((car.getCol())*baseWidth,(car.getRow())*baseHeight, (car.getCol()+car.getLength()) * baseWidth,(car.getRow()+1)*baseHeight);
            }
            else
            {
                carRect.set((car.getCol())*baseWidth,(car.getRow())*baseHeight, (car.getCol()+1)*baseWidth,(car.getRow()+car.getLength())*baseHeight);
            }
            canvas.drawRect(carRect, carPainter);
        }

        /*
        Paint carPainter = new Paint();
        carPainter.setColor(Color.BLACK);

        Rect carRect = new Rect();
        //carRect.set(car.getRow(), car.getCol(), 1, 1);
        carRect.set(baseWidth*3,baseHeight*3, 4*baseWidth, 4*baseHeight);
        canvas.drawRect(carRect, carPainter); */
    }

    private void drawGrid(Canvas canvas)
    {
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
