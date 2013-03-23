package is.hr.escape.objects;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: heidar
 * Date: 3/23/13
 * Time: 1:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class GhostCar {
    private int x;
    private int y;
    private int id;
    private Rect bounds;

    public GhostCar(Car car, Point coordinates, Rect bounds) {
        id = car.getId();
        x = coordinates.x;
        y = coordinates.y;
        this.bounds = bounds;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public int getId() { return id; }

    public void setPosition(Point newPos) {
        x = newPos.x;
        y = newPos.y;
    }

    public boolean isWithinBounds(Point pos) {
        Log.e("Position", String.format("%s %s", pos, bounds));
        return bounds.contains(pos.x, pos.y);
        //return pos.x >= bounds.left && pos.x <= bounds.right && pos.y >= bounds.top && pos.y <= bounds.bottom;
    }
}
