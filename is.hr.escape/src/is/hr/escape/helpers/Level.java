package is.hr.escape.helpers;

/**
 * Created with IntelliJ IDEA.
 * User: heidar
 * Date: 3/30/13
 * Time: 6:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Level {
    public final int index;
    public final String levelId;
    public final String level;

    public Level(int i, String id, String level) {
        index = i;
        levelId = id;
        this.level = level;
    }
}
