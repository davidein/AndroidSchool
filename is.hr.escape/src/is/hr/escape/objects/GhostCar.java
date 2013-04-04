package is.hr.escape.objects;

import android.graphics.*;
import is.hr.escape.objects.Car.Orientation;

/**
 * A ghost car is a car which is currently being touched by the user.
 * It's drawn in a different way than a car and knows not to move beyond given m_bounds
 */
public class GhostCar {
    private int m_col;
    private int m_row;
    private int m_id;
    private Rect m_bounds;
    private Point m_offset;
    private Orientation m_orientation;
    private Car m_car;

    public GhostCar(Car car, Point coordinates, Rect bounds, Point touchOffset) {
        m_id = car.getId();
        m_col = coordinates.x;
        m_row = coordinates.y;
        m_bounds = bounds;
        m_offset = touchOffset;
        m_orientation = car.getOrientation();
        m_car = car;
    }

    public int getCol() { return m_col; }

    public int getRow() { return m_row; }

    public int getId() { return m_id; }

    public Car getCar() { return m_car; }

    public void setPosition(Point newPos) {
        if(m_orientation == Orientation.Vertical) {
            m_row = newPos.y + m_offset.y;
            if(m_row < m_bounds.top) {
                m_row = m_bounds.top;
            } else if (m_row > m_bounds.bottom) {
                m_row = m_bounds.bottom;
            }
        } else {
            m_col = newPos.x + m_offset.x;
            if(m_col < m_bounds.left) {
                m_col = m_bounds.left;
            } else if(m_col > m_bounds.right) {
                m_col = m_bounds.right;
            }
        }
    }

    public void Draw(Canvas canvas, RectF carRect, Bitmap texture)
    {
        Paint carBorderPainter = new Paint();
        carBorderPainter.setARGB(255, 87, 13, 0);
        carBorderPainter.setStrokeWidth(1);               // set the size
        carBorderPainter.setDither(true);                    // set the dither to true
        carBorderPainter.setStyle(Paint.Style.STROKE);       // set to STOKE
        carBorderPainter.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        carBorderPainter.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
        carBorderPainter.setAntiAlias(true);

        Paint carPainter = new Paint();
        carPainter.setColor(Color.GREEN);
        carPainter.setAlpha(90);

        Paint carWoodPainter = new Paint();
        carWoodPainter.setAlpha(75);

        canvas.drawRoundRect(carRect, 15, 15, carPainter);
        canvas.drawBitmap(texture, null, carRect, carWoodPainter);
        canvas.drawRoundRect(carRect, 15, 15, carBorderPainter);
    }

}
