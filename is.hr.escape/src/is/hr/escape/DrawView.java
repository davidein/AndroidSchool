package is.hr.escape;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import is.hr.escape.objects.Car.Orientation;
import is.hr.escape.logic.Action;
import is.hr.escape.objects.Car;
import is.hr.escape.objects.GhostCar;

import java.util.List;

/**
 * The DrawView renders the current state of the game.
 * It receives information about the current state of the game through a GameHandler interface.
 *
 * It handles touch events on the game view and notifies the game handler when actions are performed
 */
public class DrawView extends View {
    GameHandler m_handler;
    Bitmap m_woodTexture;
    boolean m_touchEnabled;

    private GhostCar m_ghost = null;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_touchEnabled = true;

        m_woodTexture = BitmapFactory.decodeResource(getResources(), R.drawable.woodtexture);
    }

    public void setGameHandler(GameHandler handler) {
        m_handler = handler;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBase(canvas);

        int baseWidth = getWidth() / m_handler.getCols();
        int baseHeight = getHeight() / m_handler.getRows();

        Car ghostCar = null;
        //Draw all static cars
        for (Car car : m_handler.getCars())
        {
            if(m_ghost != null && car.getId() == m_ghost.getId()) {
                ghostCar = car;
                car.set_isGhost(true);
                continue;
            }

            RectF carRect = new RectF();
            if (car.getOrientation() == Orientation.Horizontal)
            {
                carRect.set((car.getCol())*baseWidth+1,(car.getRow())*baseHeight+1, (car.getCol()+car.getLength()) * baseWidth,(car.getRow()+1)*baseHeight);
            }
            else
            {
                carRect.set((car.getCol())*baseWidth+1,(car.getRow())*baseHeight+1, (car.getCol()+1)*baseWidth,(car.getRow()+car.getLength())*baseHeight);
            }

            car.Draw(canvas, carRect, m_woodTexture);
        }
        //Draw the currently held car on its own
        if(ghostCar != null) {
            RectF carRect = new RectF();
            if (ghostCar.getOrientation() == Orientation.Horizontal)
            {
                carRect.set(m_ghost.getX(), m_ghost.getY(), m_ghost.getX() + (ghostCar.getLength() * baseWidth), m_ghost.getY() + baseHeight);
            }
            else
            {
                carRect.set(m_ghost.getX(), m_ghost.getY(), m_ghost.getX() + baseWidth, m_ghost.getY() + (ghostCar.getLength()) * baseHeight);
            }
            ghostCar.Draw(canvas, carRect, m_woodTexture);
            ghostCar.set_isGhost(false);
        }
    }


    //Draws the level background
    private void drawBase(Canvas canvas)
    {
        Paint base = new Paint();
        base.setColor(Color.GREEN);
        base.setAlpha(30);

        int baseWidth = getWidth() / m_handler.getCols();
        int baseHeight = getHeight() / m_handler.getRows();

        RectF canvasSpace = new RectF((4)*baseWidth,(3)*baseHeight, (6) * baseWidth,(4)*baseHeight);
        canvas.drawRoundRect(canvasSpace, 15, 15, base);

        Paint baseText = new Paint();
        baseText.setColor(Color.WHITE);
        baseText.setTextSize(30);

        canvas.drawText("Finish line", 4 * baseWidth + 50, 3 * baseHeight + 70, baseText);

        Paint linePainter = new Paint();
        linePainter.setColor(Color.BLACK);
        linePainter.setAlpha(200);
        for (int irow = 1;irow<m_handler.getRows();irow++)
        {
            canvas.drawLine(0, getHeight()/m_handler.getRows()*irow, getWidth(), getHeight()/m_handler.getRows()*irow, linePainter );
        }
        for (int icol = 1;icol<m_handler.getRows();icol++)
        {
            canvas.drawLine(getWidth()/m_handler.getCols() * icol, 0, getWidth()/m_handler.getCols() * icol, getHeight(), linePainter );
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
        if(!m_touchEnabled) {
            return false;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Car selected = getCar(x, y);
                if(selected != null) {
                    List<Action> actions = m_handler.getActionsFor(selected);
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
                    m_ghost = new GhostCar(selected, screenPos, bounds, offset   );
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(m_ghost != null) {
                    Point newPos = new Point(x, y);
                    m_ghost.setPosition(newPos);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                //Moving out of the screen is handled like releasing a finger
                if(m_ghost != null) {
                    Point newPos = coordinatesToGrid(m_ghost.getX(), m_ghost.getY(), true);
                    Point oldPos = new Point(m_ghost.getCar().getCol(), m_ghost.getCar().getRow());
                    int offset = 0;
                    switch(m_ghost.getCar().getOrientation()) {
                        case Horizontal:
                            offset = newPos.x - oldPos.x;
                            break;
                        case Vertical:
                            offset = newPos.y - oldPos.y;
                            break;
                    }
                    if(offset != 0) {
                        m_handler.actionPerformed(new Action(m_ghost.getId(), offset));
                    }
                }
                m_ghost = null;
                break;
        }
        invalidate();

        return true;
    }

    public void disableTouch() {
        m_touchEnabled = false;
    }

    public void enableTouch() {
        m_touchEnabled = true;
    }

    /**
     * Converts screen coordinates to grid coordinates
     * @param round If set to true, grid coordinates will be rounded, otherwise simply floored
     */
    private Point coordinatesToGrid(int x, int y, boolean round) {
        int col, row;
        int columnWidth = getWidth() / m_handler.getCols();
        int rowHeight = getHeight() / m_handler.getRows();
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
        int columnWidth = getWidth() / m_handler.getCols();
        int rowHeight = getHeight() / m_handler.getRows();

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

        List<Car> cars = m_handler.getCars();
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
