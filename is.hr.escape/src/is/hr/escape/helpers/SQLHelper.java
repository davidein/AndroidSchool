package is.hr.escape.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
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
                "CREATE TABLE " + CHALLENGE_TABLE_NAME + " (ch_id INT PRIMARY KEY, name TEXT);";
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

    public void populateChallenge(Challenge challenge, List<Level> levelList)
    {
        _db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("ch_id", challenge.id);
        cv.put("name", challenge.name);

        _db.insert(CHALLENGE_TABLE_NAME, null, cv);

        for (Level level : levelList)
        {

            ContentValues levelcv = new ContentValues();
            levelcv.put("ch_id", challenge.id);
            levelcv.put("l_id", level.levelId);
            levelcv.put("setup", level.level);

            _db.insert(LEVEL_TABLE_NAME, null, levelcv);
        }
    }

    public Level getNextLevel(int challengeId, int levelId)
    {
        _db = this.getReadableDatabase();

        Level level = null;

        Cursor cursor = _db.query(LEVEL_TABLE_NAME, new String[] {"ch_id", "l_id, setup"}, "ch_id = ? and l_id = ?", new String[] {String.valueOf(challengeId), String.valueOf(levelId+1)}, "", "", "" );

        while (cursor.moveToNext())
        {
            int challengeIdentityColumnIndex = cursor.getColumnIndex("l_id");
            int identityColumnIndex = cursor.getColumnIndex("l_id");
            int setupColumnIndex = cursor.getColumnIndex("setup");

            int challengeIdentity = cursor.getInt(challengeIdentityColumnIndex);
            int identity = cursor.getInt(identityColumnIndex);
            String setup = cursor.getString(setupColumnIndex);

            level = new Level(challengeIdentity, identity, setup);
        }

        if (level == null)
        {
            Cursor secondaryCursor = _db.query(LEVEL_TABLE_NAME, new String[] {"ch_id", "l_id, setup"}, "ch_id = ? and l_id = ?", new String[] {String.valueOf(challengeId+1), "1"}, "", "", "" );

            while (secondaryCursor.moveToNext())
            {
                int challengeIdentityColumnIndex = secondaryCursor.getColumnIndex("l_id");
                int identityColumnIndex = secondaryCursor.getColumnIndex("l_id");
                int setupColumnIndex = secondaryCursor.getColumnIndex("setup");

                int challengeIdentity = secondaryCursor.getInt(challengeIdentityColumnIndex);
                int identity = secondaryCursor.getInt(identityColumnIndex);
                String setup = secondaryCursor.getString(setupColumnIndex);

                level = new Level(challengeIdentity, identity, setup);
            }
        }

        return level;
    }

    public int getScore(int challenge, int level)
    {
        _db = this.getWritableDatabase();

        Cursor cursor = _db.query(SCORE_TABLE_NAME, new String[] {"moves"}, "ch_id = ? and l_id = ?", new String[] {String.valueOf(challenge), String.valueOf(level)}, "", "", "");

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

        Cursor cursor = _db.query(SCORE_TABLE_NAME, new String[] {"moves"}, "ch_id = ? and l_id = ?", new String[] {challenge.toString(), level.toString()}, "", "", "");

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

            return _db.update(SCORE_TABLE_NAME, cv, "ch_id = ? and l_id = ?", new String[] {challenge.toString(), level.toString()} ) > 0 ;
        }

        return false;
    }

    public List<Challenge> getAllChallenges()
    {
        _db = this.getReadableDatabase();

        Cursor cursor = _db.query(CHALLENGE_TABLE_NAME, new String[] {"ch_id, name"}, "", new String[] {}, "", "", "" );

        ArrayList<Challenge> challengeList = new ArrayList<Challenge>();

        while (cursor.moveToNext())
        {
            int identityColumnIndex = cursor.getColumnIndex("ch_id");
            int nameColumnIndex = cursor.getColumnIndex("name");

            int identity = cursor.getInt(identityColumnIndex);
            String name = cursor.getString(nameColumnIndex);

            Challenge challenge = new Challenge(identity, name);
            challengeList.add(challenge);
        }

        return challengeList;
    }

    public List<Level> getChallengeLevels(Challenge challenge)
    {
        _db = this.getReadableDatabase();

        Cursor cursor = _db.query(LEVEL_TABLE_NAME, new String[] {"l_id, setup"}, "ch_id = ?", new String[] {String.valueOf(challenge.id)}, "", "", "" );

        ArrayList<Level> levelList = new ArrayList<Level>();

        while (cursor.moveToNext())
        {
            int identityColumnIndex = cursor.getColumnIndex("l_id");
            int setupColumnIndex = cursor.getColumnIndex("setup");

            int identity = cursor.getInt(identityColumnIndex);
            String setup = cursor.getString(setupColumnIndex);

            Level level = new Level(challenge.id, identity, setup);
            levelList.add(level);
        }

        return  levelList;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}