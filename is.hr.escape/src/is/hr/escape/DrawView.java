package is.hr.escape;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import is.hr.escape.helpers.Orientation;
import is.hr.escape.logic.Action;
import is.hr.escape.objects.Car;
import is.hr.escape.objects.GhostCar;

import java.util.List;

/**
 * User: David Einarsson
 * Date: 23.3.2013
 * Time: 10:11
 */
public class DrawView extends View {
    GameHandler handler;

    private GhostCar _ghost = null;

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

        drawGrid(canvas);

        int baseWidth = getWidth() / handler.getCols();
        int baseHeight = getHeight() / handler.getRows();

        Car ghostCar = null;
        //Draw all static cars
        for (Car car : handler.getCars())
        {
            if(_ghost != null && car.getId() ==_ghost.getId()) {
                ghostCar = car;
                continue;
            }
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
        //Draw the currently held car on its own
        if(ghostCar != null) {
            Paint carPainter = new Paint();
            carPainter.setColor(Color.GREEN);

            Rect carRect = new Rect();
            if (ghostCar.getOrientation() == Orientation.Horizontal)
            {
                carRect.set(_ghost.getX(), _ghost.getY(), _ghost.getX() + (ghostCar.getLength() * baseWidth), _ghost.getY() + baseHeight);
            }
            else
            {
                carRect.set(_ghost.getX(), _ghost.getY(), _ghost.getX() + baseWidth, _ghost.getY() + (ghostCar.getLength())*baseHeight);
            }
            canvas.drawRect(carRect, carPainter);
        }
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
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int min = w < h ? w : h;
        setLayoutParams(new LinearLayout.LayoutParams(min, min));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Car selected = getCar(x, y);
                if(selected != null) {
                    List<Action> actions = handler.getActionsFor(selected);
                    Point min = new Point(selected.getCol(), selected.getRow()), max = new Point(min);

                    //Find the bounds for the car
                    for(Action action : actions) {
                        switch(selected.getOrientation()) {
                            case Horizontal:
                                int newCol = selected.getCol() + action.getOffset();
                                if(newCol < min.x) {
                                    min.x = newCol;
                                }
                                if(newCol > max.x) {
                                    max.x = newCol;
                                }
                                break;
                            case Vertical:
                                int newRow = selected.getRow() + action.getOffset();
                                if(newRow < min.y) {
                                    min.y = newRow;
                                }
                                if(newRow > max.y) {
                                    max.y = newRow;
                                }
                                break;
                        }
                    }
                    min = gridToCoordinates(min.x, min.y);
                    max = gridToCoordinates(max.x, max.y);
                    Rect bounds = new Rect(min.x, min.y, max.x, max.y);

                    Point screenPos = gridToCoordinates(selected.getCol(), selected.getRow());
                    Point offset = new Point(screenPos.x - x, screenPos.y - y);
                    _ghost = new GhostCar(selected, screenPos, bounds, offset   );
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(_ghost != null) {
                    Point newPos = new Point(x, y);
                    if(_ghost.isWithinBounds(newPos)) {

                        _ghost.setPosition(newPos);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(_ghost != null) {
                    Point newPos = coordinatesToGrid(_ghost.getX(), _ghost.getY(), true);
                    Point oldPos = new Point(_ghost.getCar().getCol(), _ghost.getCar().getRow());
                    int offset = 0;
                    switch(_ghost.getCar().getOrientation()) {
                        case Horizontal:
                            offset = newPos.x - oldPos.x;
                            break;
                        case Vertical:
                            offset = newPos.y - oldPos.y;
                            break;
                    }
                    handler.actionPerformed(new Action(_ghost.getId(), offset));
                }
                _ghost = null;
                break;
        }
        invalidate();

        return true;
    }

    /**
     * Converts screen coordinates to grid coordinates
     * @param round If set to true, grid coordinates will be rounded, otherwise simply floored
     */
    private Point coordinatesToGrid(int x, int y, boolean round) {
        int col, row;
        int columnWidth = getWidth() / handler.getCols();
        int rowHeight = getHeight() / handler.getRows();
        if(round) {
            col = Math.round(x / (float)columnWidth);
            row = Math.round(y / (float)rowHeight);
        } else {
            col = x / columnWidth;
            row = y / rowHeight;
        }
        return new Point(col, row);
    }

    /**
     * Converts grid coordinates to screen coordinates
     */
    private Point gridToCoordinates(int column, int row) {
        int x, y;
        int columnWidth = getWidth() / handler.getCols();
        int rowHeight = getHeight() / handler.getRows();

        x = column * columnWidth;
        y = row * rowHeight;

        return new Point(x, y);
    }

    /**
     * Gets the car that's under the given screen coordinates or null if no car is located there
     * @param x X screen coordinate
     * @param y Y screen coordinate
     * @return A car instance or null
     */
    private Car getCar(int x, int y) {
        Point gridPoint = coordinatesToGrid(x, y, false);

        List<Car> cars = handler.getCars();
        for(Car car : cars) {
            switch(car.getOrientation()) {
                case Horizontal:
                    if(gridPoint.y == car.getRow() && gridPoint.x >= car.getCol() && gridPoint.x < car.getCol() + car.getLength()) {
                        return car;
                    }
                    break;
                case Vertical:
                    if(gridPoint.x == car.getCol() && gridPoint.y >= car.getRow() && gridPoint.y < car.getRow() + car.getLength()) {
                        return car;
                    }
                    break;
            }
        }
        return null;
    }
}
