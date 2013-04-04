package is.hr.escape.objects;

/**
 * A POJO class for challenges, containing a challenge's id and name
 */
public class Challenge {
    private final int m_id;
    private final String m_name;

    public Challenge(int id, String name) {
        m_id = id;
        m_name = name;
    }

    public int getId() {
        return m_id;
    }

    public String getName() {
        return m_name;
    }
}
