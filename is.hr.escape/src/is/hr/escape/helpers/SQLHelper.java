package is.hr.escape.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import is.hr.escape.objects.Challenge;
import is.hr.escape.objects.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * The SQLHelper provides helper functions to access data from the sqlite database.
 * It creates the required tables the first time the application is run and provides ways to
 * populate initial data.
 */
public class SQLHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "escape";
    private static final int DATABASE_VERSION = 2;
    private static final String CHALLENGE_TABLE_NAME = "challenge";
    private static final String LEVEL_TABLE_NAME = "level";
    private static final String CHALLENGE_TABLE_CREATE =
                "CREATE TABLE " + CHALLENGE_TABLE_NAME + " (ch_id INT PRIMARY KEY, name TEXT);";
    private static final String LEVEL_TABLE_CREATE =
                "CREATE TABLE " + LEVEL_TABLE_NAME + " (ch_id INT, l_id INT, setup TEXT, moves INT, PRIMARY KEY(ch_id, l_id), FOREIGN KEY(ch_id) REFERENCES challenge(ch_id));";

    private SQLiteDatabase m_db;

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CHALLENGE_TABLE_CREATE);
        db.execSQL(LEVEL_TABLE_CREATE);
    }

    @Override
    protected void finalize() throws Throwable {
        if (m_db != null && m_db.isOpen())
            m_db.close();

        super.finalize();
    }

    public void populateChallenge(Challenge challenge, List<Level> levelList)
    {
        m_db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("ch_id", challenge.getId());
        cv.put("name", challenge.getName());

        m_db.insert(CHALLENGE_TABLE_NAME, null, cv);

        for (Level level : levelList)
        {

            ContentValues levelcv = new ContentValues();
            levelcv.put("ch_id", challenge.getId());
            levelcv.put("l_id", level.getLevelId());
            levelcv.put("setup", level.getLevel());
            levelcv.put("moves", 0);

            m_db.insert(LEVEL_TABLE_NAME, null, levelcv);
        }
    }

    public Level getNextLevel(int challengeId, int levelId)
    {
        m_db = this.getReadableDatabase();

        Level level = null;

        Cursor cursor = m_db.query(LEVEL_TABLE_NAME, new String[] {"ch_id", "l_id, setup", "moves"}, "ch_id = ? and l_id = ?", new String[] {String.valueOf(challengeId), String.valueOf(levelId+1)}, "", "", "" );

        while (cursor.moveToNext())
        {
            int challengeIdentityColumnIndex = cursor.getColumnIndex("ch_id");
            int identityColumnIndex = cursor.getColumnIndex("l_id");
            int setupColumnIndex = cursor.getColumnIndex("setup");
            int moveColumnIndex = cursor.getColumnIndex("moves");

            int challengeIdentity = cursor.getInt(challengeIdentityColumnIndex);
            int identity = cursor.getInt(identityColumnIndex);
            String setup = cursor.getString(setupColumnIndex);
            int moves = cursor.getInt(moveColumnIndex);

            level = new Level(challengeIdentity, identity, setup, moves);
        }

        if (level == null)
        {
            Cursor secondaryCursor = m_db.query(LEVEL_TABLE_NAME, new String[] {"ch_id", "l_id, setup", "moves"}, "ch_id = ? and l_id = ?", new String[] {String.valueOf(challengeId+1), "1"}, "", "", "" );

            while (secondaryCursor.moveToNext())
            {
                int challengeIdentityColumnIndex = secondaryCursor.getColumnIndex("ch_id");
                int identityColumnIndex = secondaryCursor.getColumnIndex("l_id");
                int setupColumnIndex = secondaryCursor.getColumnIndex("setup");
                int moveColumnIndex = secondaryCursor.getColumnIndex("moves");

                int challengeIdentity = secondaryCursor.getInt(challengeIdentityColumnIndex);
                int identity = secondaryCursor.getInt(identityColumnIndex);
                String setup = secondaryCursor.getString(setupColumnIndex);
                int moves = secondaryCursor.getInt(moveColumnIndex);

                level = new Level(challengeIdentity, identity, setup, moves);
            }
        }

        return level;
    }

    public boolean saveLevel(Integer challenge, Integer level, int moves)
    {
        m_db = this.getWritableDatabase();

        Cursor cursor = m_db.query(LEVEL_TABLE_NAME, new String[] {"moves"}, "ch_id = ? and l_id = ?", new String[] {challenge.toString(), level.toString()}, "", "", "");

        int score = -1;

        while (cursor.moveToNext())
        {
            int columnIndex = cursor.getColumnIndex("moves");
            score = cursor.getInt(columnIndex);
        }

        if (score <= 0 || score > moves)
        {
            ContentValues cv = new ContentValues();
            cv.put("moves", moves);

            return m_db.update(LEVEL_TABLE_NAME, cv, "ch_id = ? and l_id = ?", new String[] {challenge.toString(), level.toString()} ) > 0 ;
        }

        return false;
    }

    public Challenge getChallenge(int challengeId)
    {
        m_db = this.getReadableDatabase();

        Cursor cursor = m_db.query(CHALLENGE_TABLE_NAME, new String[] {"ch_id, name"}, "ch_id = ?", new String[] {String.valueOf(challengeId)}, "", "", "ch_id" );

        Challenge challenge = null;

        while (cursor.moveToNext())
        {
            int identityColumnIndex = cursor.getColumnIndex("ch_id");
            int nameColumnIndex = cursor.getColumnIndex("name");

            int identity = cursor.getInt(identityColumnIndex);
            String name = cursor.getString(nameColumnIndex);

            challenge = new Challenge(identity, name);
        }

        return challenge;
    }

    public List<Challenge> getAllChallenges()
    {
        m_db = this.getReadableDatabase();

        Cursor cursor = m_db.query(CHALLENGE_TABLE_NAME, new String[] {"ch_id, name"}, "", new String[] {}, "", "", "ch_id" );

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
        m_db = this.getReadableDatabase();

        Cursor cursor = m_db.query(LEVEL_TABLE_NAME, new String[] {"l_id, setup, moves"}, "ch_id = ?", new String[] {String.valueOf(challenge.getId())}, "", "", "l_id" );

        ArrayList<Level> levelList = new ArrayList<Level>();

        while (cursor.moveToNext())
        {
            int identityColumnIndex = cursor.getColumnIndex("l_id");
            int setupColumnIndex = cursor.getColumnIndex("setup");
            int moveColumnIndex = cursor.getColumnIndex("moves");

            int identity = cursor.getInt(identityColumnIndex);
            String setup = cursor.getString(setupColumnIndex);
            int moves = cursor.getInt(moveColumnIndex);

            Level level = new Level(challenge.getId(), identity, setup, moves);
            levelList.add(level);
        }

        return  levelList;
    }

    public Level getLevel(int challengeId, int levelId)
    {
        m_db = this.getReadableDatabase();

        Cursor cursor = m_db.query(LEVEL_TABLE_NAME, new String[] {"l_id, setup, moves"}, "ch_id = ? and l_id = ?", new String[] {String.valueOf(challengeId), String.valueOf(levelId)}, "", "", "l_id" );

        Level level = null;

        while (cursor.moveToNext())
        {
            int identityColumnIndex = cursor.getColumnIndex("l_id");
            int setupColumnIndex = cursor.getColumnIndex("setup");
            int moveColumnIndex = cursor.getColumnIndex("moves");

            int identity = cursor.getInt(identityColumnIndex);
            String setup = cursor.getString(setupColumnIndex);
            int moves = cursor.getInt(moveColumnIndex);

            level = new Level(challengeId, identity, setup, moves);
        }

        return  level;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}