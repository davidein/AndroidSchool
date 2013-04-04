package is.hr.escape.objects;

/**
 * A POJO class for levels in the game containing the challenge it belongs to,
 * the best move count a user has previously beaten the level in, its id and
 * the level setup string which can be used to initialize the level by the game logic.
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
