package is.hr.escape.logic;

/**
 * Created with IntelliJ IDEA.
 * User: heidar
 * Date: 3/23/13
 * Time: 11:06 AM
 * To change this template use File | Settings | File Templates.
 */
/**
 * Action class
 */
public class Action {

    private int m_id;
    private int m_offset;

    Action( int id, int offset ) {
        m_id = id;
        m_offset = offset;
    }

    public int getId()  { return m_id; }

    public int getOffset() { return m_offset; }

    public String toString( ) {
        StringBuilder sb = new StringBuilder();
        sb.append( '(' );
        sb.append( getId() );
        sb.append( ',' );
        sb.append( getOffset() );
        sb.append( ')' );
        return sb.toString();
    }
}