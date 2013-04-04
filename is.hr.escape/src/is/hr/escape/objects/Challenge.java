package is.hr.escape.objects;

/**
 * Created with IntelliJ IDEA.
 * User: heidar
 * Date: 4/2/13
 * Time: 5:48 PM
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
