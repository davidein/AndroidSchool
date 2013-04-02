package is.hr.escape.helpers;

import android.content.Context;
import android.content.res.AssetManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Heiðar Þórðarson
 * Interface to load levels. Singleton
 */
public class LevelHelper {
    private static LevelHelper instance;

    public static LevelHelper getInstance(Context context) {
        if(instance == null) {
            instance = new LevelHelper(context);
        }
        return instance;
    }
    private List<Challenge> challenges = new ArrayList<Challenge>();

    //Maps from challenge name to a list of its levels
    private Map<String, List<Level>> levelMap = new HashMap<String, List<Level>>();

    private SQLHelper sqlHelper;

    private LevelHelper(Context context) {
        sqlHelper = new SQLHelper(context);
        loadChallenges();
    }

    /**
     * Loads the challenge list from databse using sqlHelper
     */
    private void loadChallenges() {
        challenges = sqlHelper.getAllChallenges();
    }

    /**
     * Loads all levels for a given challenge from database using sqlHelper
     * @param challenge Name of the challenge for which to load levels
     */
    private void loadChallenge(String challenge) {
        List<Level> levels = null;
        for(Challenge c : challenges) {
            if(c.name.equals(challenge)) {
                levels = sqlHelper.getChallengeLevels(c);
                break;
            }
        }
        if(levels != null) {
            levelMap.put(challenge, levels);
        }
    }

    /**
     * @return A list of all available challenges (set of levels)
     */
    public List<Challenge> getChallenges() {
        return challenges;
    }

    /**
     * Gets all levels of a given challenge
     * @param challenge The challenge to which the levels belong
     * @return A list of levels belonging to challenge
     */
    public List<Level> getLevels(Challenge challenge) {
        if(!levelMap.containsKey(challenge.name)) {
            loadChallenge(challenge.name);
        }
        return levelMap.get(challenge.name);
    }
}
