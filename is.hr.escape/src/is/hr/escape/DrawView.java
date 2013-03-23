package is.hr.escape;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
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
                    _ghost = new GhostCar(selected, gridToCoordinates(selected.getCol(), selected.getRow()), bounds);
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
            case MotionEvent.ACTION_UP:
                _ghost = null;
                break;
        }

        return true;
    }

    private Point coordinatesToGrid(int x, int y) {
        int col, row;
        int columnWidth = getWidth() / handler.getCols() + 1;
        int rowHeight = getHeight() / handler.getRows() + 1;
        col = x / columnWidth;
        row = y / rowHeight;
        return new Point(col, row);
    }

    private Point gridToCoordinates(int column, int row) {
        int x, y;
        int columnWidth = getWidth() / handler.getCols() + 1;
        int rowHeight = getHeight() / handler.getRows() + 1;

        x = column * columnWidth;
        y = row * rowHeight;

        return new Point(x, y);
    }

    private Car getCar(int x, int y) {
        Point gridPoint = coordinatesToGrid(x, y);

        List<Car> cars = handler.getCars();
        for(Car car : cars) {
            switch(car.getOrientation()) {
                case Horizontal:
                    if(gridPoint.y == car.getRow() && gridPoint.x >= car.getCol() && gridPoint.x <= car.getCol() + car.getLength()) {
                        return car;
                    }
                    break;
                case Vertical:
                    if(gridPoint.x == car.getCol() && gridPoint.y >= car.getRow() && gridPoint.y <= car.getRow() + car.getLength()) {
                        return car;
                    }
                    break;
            }
        }
        return null;
    }
}
