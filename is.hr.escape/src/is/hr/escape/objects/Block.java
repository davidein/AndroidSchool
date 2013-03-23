package is.hr.escape.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * User: David Einarsson
 * Date: 23.3.2013
 * Time: 10:33
 */
public class Block {
    public Rect get_position() {
        return _position;
    }

    public void set_position(Rect _position) {
        this._position = _position;
    }

    public boolean isTouched(int x, int y)
    {
        return _position.contains(x, y);
    }

    private Rect _position;


    public void Draw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawRect( _position, paint);
    }
}
