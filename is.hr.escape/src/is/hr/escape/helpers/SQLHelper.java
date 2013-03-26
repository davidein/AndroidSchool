package is.hr.escape.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * User: David Einarsson
 * Date: 26.3.2013
 * Time: 17:16
 */
public class SQLHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "escape";
    private static final int DATABASE_VERSION = 2;
    private static final String SCORE_TABLE_NAME = "score";
    private static final String SCORE_TABLE_CREATE =
                "CREATE TABLE " + SCORE_TABLE_NAME + " ( level INT ,moves INT, timetaken INT);";

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCORE_TABLE_CREATE);
    }

    public int getScore(Integer level)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(SCORE_TABLE_NAME, new String[] {"moves"}, "level = ?", new String[] {level.toString()}, "", "", "");

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

    public boolean saveScore(Integer level, int moves, int timetaken)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(SCORE_TABLE_NAME, new String[] {"moves"}, "level = ?", new String[] {level.toString()}, "", "", "");

        int score = -1;

        while (cursor.moveToNext())
        {
            int columnIndex = cursor.getColumnIndex("moves");
            score = cursor.getInt(columnIndex);
        }

        if (score == -1)
        {
            ContentValues cv = new ContentValues();
            cv.put("level", level);
            cv.put("moves", moves);
            cv.put("timetaken", timetaken);

            return db.insert(SCORE_TABLE_NAME, null, cv) > 0;
        }
        else if (score > moves)
        {
            ContentValues cv = new ContentValues();
            cv.put("moves", moves);
            cv.put("timetaken", timetaken);

            return db.update(SCORE_TABLE_NAME, cv, "level = ?", new String[] {level.toString()} ) > 0 ;
        }
        return false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}