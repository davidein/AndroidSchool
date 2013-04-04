package is.hr.escape.logic;

/**
 * A POJO class representing a single action which can be performed by a car
 */
public class Action {

    private int m_id;
    private int m_offset;

    public Action( int id, int offset ) {
        m_id = id;
        m_offset = offset;
    }

    public int getId()  { return m_id; }

    public int getOffset() { return m_offset; }

    public String toString( ) {
        return String.format("(%d %d)", getId(), getOffset());
    }
}