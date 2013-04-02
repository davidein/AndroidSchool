package is.hr.escape.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * User: David Einarsson
 * Date: 26.3.2013
 * Time: 17:16
 */
public class SQLHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "escape";
    private static final int DATABASE_VERSION = 2;
    private static final String SCORE_TABLE_NAME = "score";
    private static final String CHALLENGE_TABLE_NAME = "challenge";
    private static final String LEVEL_TABLE_NAME = "level";
    private static final String SCORE_TABLE_CREATE =
                "CREATE TABLE " + SCORE_TABLE_NAME + " (ch_id INT, l_id INT, moves INT, PRIMARY KEY(ch_id, l_id), FOREIGN KEY(ch_id) REFERENCES challenge(ch_id), FOREIGN KEY(l_id) REFERENCES level(l_id));";
    private static final String CHALLENGE_TABLE_CREATE =
                "CREATE TABLE " + CHALLENGE_TABLE_NAME + " (ch_id INT PRIMARY KEY, name TEXT, path TEXT);";
    private static final String LEVEL_TABLE_CREATE =
                "CREATE TABLE " + LEVEL_TABLE_NAME + " (ch_id INT, l_id INT, setup TEXT, PRIMARY KEY(ch_id, l_id), FOREIGN KEY(ch_id) REFERENCES challenge(ch_id));";

    private SQLiteDatabase _db;

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CHALLENGE_TABLE_CREATE);
        db.execSQL(LEVEL_TABLE_CREATE);
        db.execSQL(SCORE_TABLE_CREATE);
    }

    @Override
    protected void finalize() throws Throwable {
        if (_db != null && _db.isOpen())
            _db.close();

        super.finalize();
    }

    public int getScore(Integer level)
    {
        _db = this.getWritableDatabase();

        Cursor cursor = _db.query(SCORE_TABLE_NAME, new String[] {"moves"}, "l_id = ?", new String[] {level.toString()}, "", "", "");

        int score = -1;

        while (cursor.moveToNext())
        {
            int columnIndex = cursor.getColumnIndex("moves");
            score = cursor.getInt(columnIndex);
        }

        if (score == -1)
        {
            return 0;
        }

        return score;
    }

    public boolean saveScore(Integer challenge, Integer level, int moves)
    {
        _db = this.getWritableDatabase();

        Cursor cursor = _db.query(SCORE_TABLE_NAME, new String[] {"moves"}, "ch_id = ?, l_id = ?", new String[] {challenge.toString(), level.toString()}, "", "", "");

        int score = -1;

        while (cursor.moveToNext())
        {
            int columnIndex = cursor.getColumnIndex("moves");
            score = cursor.getInt(columnIndex);
        }

        if (score == -1)
        {
            ContentValues cv = new ContentValues();
            cv.put("ch_id", challenge);
            cv.put("l_id", level);
            cv.put("moves", moves);

            return _db.insert(SCORE_TABLE_NAME, null, cv) > 0;
        }
        else if (score > moves)
        {
            ContentValues cv = new ContentValues();
            cv.put("moves", moves);

            return _db.update(SCORE_TABLE_NAME, cv, "ch_id = ?, l_id = ?", new String[] {challenge.toString(), level.toString()} ) > 0 ;
        }

        return false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}