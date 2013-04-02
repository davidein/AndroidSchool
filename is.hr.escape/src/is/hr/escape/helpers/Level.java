package is.hr.escape.helpers;

/**
 * Created with IntelliJ IDEA.
 * User: heidar
 * Date: 3/30/13
 * Time: 6:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Level {
    public final int challengeId;
    public final int levelId;
    public final String level;

    public Level(int challengeId, int id, String level) {
        this.challengeId = challengeId;
        levelId = id;
        this.level = level;
    }
}
