package is.hr.escape.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import is.hr.escape.helpers.Orientation;

/**
 * User: David Einarsson
 * Date: 23.3.2013
 * Time: 10:33
 */
public class Car {
    private Orientation m_orientation;
    private int m_id;
    private int m_col;
    private int m_row;
    private int m_length;

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

    public void slide( int offset ) {
        if ( getOrientation() == Orientation.Horizontal ) {
            m_col += offset;
        }
        else {
            m_row += offset;
        }
    }

    public String toString( ) {
        return String.format("(%s %d %d %d)", getOrientation() == Orientation.Horizontal ? 'H' : 'V', getCol(), getRow(), getLength());
    }

}