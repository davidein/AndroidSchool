package is.hr.escape.objects;

import android.graphics.*;

/**
 * A car is a block which can be moved either horizontally or vertically by the user.
 */
public class Car {
    private Orientation m_orientation;
    private int m_id;
    private int m_col;
    private int m_row;
    private int m_length;
    private boolean m_isghost = false;

    public Car( Orientation orientation, int col, int row, int length ) {
        m_orientation = orientation;
        m_col = col;
        m_row = row;
        m_length = length;
    }

    public Car( Car other ) {
        m_orientation = other.m_orientation;
        m_col = other.m_col;
        m_row = other.m_row;
        m_length = other.m_length;
        m_id = other.m_id;
    }

    public int getId() { return m_id; }

    public void setId(int id) { m_id = id; }

    public int getCol() { return m_col; }

    public int getRow() { return m_row; }

    public Orientation getOrientation() { return m_orientation; }

    public int getLength() { return m_length; }

    public boolean get_isGhost()
    {
        return m_isghost;
    }

    public void set_isGhost(boolean ghostCheck)
    {
        m_isghost = ghostCheck;
    }

    public void slide( int offset ) {
        if ( getOrientation() == Orientation.Horizontal ) {
            m_col += offset;
        }
        else {
            m_row += offset;
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

        Paint carWoodPainter = new Paint();
        carWoodPainter.setAlpha(75);

        Paint carBasePainter = new Paint();
        if (getId() == 0)
        {
            carBasePainter.setColor(Color.RED);
        }
        else
        {
            carBasePainter.setARGB(255, 248, 168, 45);
        }

        if (get_isGhost())
        {
            carBasePainter.setColor(Color.GREEN);
            carBasePainter.setAlpha(90);
        }

        carBasePainter.setDither(true);                    // set the dither to true
        carBasePainter.setStyle(Paint.Style.FILL);
        carBasePainter.setAntiAlias(true);

        canvas.drawRoundRect(carRect, 15, 15, carBasePainter);
        canvas.drawBitmap(texture, null, carRect, carWoodPainter);
        canvas.drawRoundRect(carRect, 15, 15, carBorderPainter);
    }

    public String toString( ) {
        return String.format("(%s %d %d %d)", getOrientation() == Orientation.Horizontal ? 'H' : 'V', getCol(), getRow(), getLength());
    }

    public enum Orientation {
        Horizontal, Vertical
    }
}