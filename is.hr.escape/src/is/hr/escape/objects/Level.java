package is.hr.escape.objects;

/**
 * Created with IntelliJ IDEA.
 * User: heidar
 * Date: 3/30/13
 * Time: 6:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Level {
    public final int m_challengeId;
    public final int m_levelId;
    public final String m_level;
    public final int m_moveCount;

    public Level(int challengeId, int id, String level, int moves) {
        m_challengeId = challengeId;
        m_levelId = id;
        m_level = level;
        m_moveCount = moves;
    }

    public int getChallengeId() {
        return m_challengeId;
    }

    public int getLevelId() {
        return m_levelId;
    }

    public String getLevel() {
        return m_level;
    }

    public int getMoveCount() {
        return m_moveCount;
    }
}
