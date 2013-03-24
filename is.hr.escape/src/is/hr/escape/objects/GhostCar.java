package is.hr.escape.objects;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import is.hr.escape.helpers.Orientation;

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
    private Point originalPosition;
    private Point offset;
    private Orientation orientation;
    private Car car;

    public GhostCar(Car car, Point coordinates, Rect bounds, Point touchOffset) {
        id = car.getId();
        x = coordinates.x;
        y = coordinates.y;
        this.bounds = bounds;
        offset = touchOffset;
        orientation = car.getOrientation();
        this.car = car;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public int getId() { return id; }

    public Car getCar() { return car; }

    public void setPosition(Point newPos) {
        if(orientation == Orientation.Vertical) {
            y = newPos.y + offset.y;
            if(y < bounds.top) {
                y = bounds.top;
            } else if (y > bounds.bottom) {
                y = bounds.bottom;
            }
        } else {
            x = newPos.x + offset.x;
            if(x < bounds.left) {
                x = bounds.left;
            } else if(x > bounds.right) {
                x = bounds.right;
            }
        }
    }

}
