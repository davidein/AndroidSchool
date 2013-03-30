package is.hr.escape.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: heidar
 * Date: 3/30/13
 * Time: 6:24 PM
 */
//Singleton
public class LevelHelper {
    private static LevelHelper instance;

    public static LevelHelper getInstance() {
        if(instance == null) {
            instance = new LevelHelper();
        }
        return instance;
    }
    private List<String> challenges = new ArrayList<String>();
    //Maps from challenge name to challenge xml file
    private Map<String, String> challengeMap = new HashMap<String, String>();
    //Maps from challenge name to a list of its levels
    private Map<String, List<Level>> levelMap = new HashMap<String, List<Level>>();

    private LevelHelper() {
        loadChallenges();
    }

    private void loadChallenges() {
        challenges.add("easy");
        challenges.add("medium");
        challengeMap.put("easy", "easy.xml");
        challengeMap.put("medium", "medium.xml");
    }

    private void loadChallenge(String challenge) {
        String file = challengeMap.get(challenge);
        //load levels


        //Dummy loading
        List<Level> levels = new ArrayList<Level>();
        for(int i = 0; i < 11; i++) {
            levels.add(new Level(i, String.valueOf(i), "(H 1 3 2), (V 0 0 2), (V 0 2 3), (H 0 5 2), (H 2 0 3), (H 4 1 2), (V 3 2 3), (V 5 3 3)"));
        }
        levelMap.put(challenge, levels);
    }

    public List<String> getChallenges() {
        return challenges;
    }

    public List<Level> getLevels(String challenge) {
        if(!levelMap.containsKey(challenge)) {
            loadChallenge(challenge);
        }
        return levelMap.get(challenge);
    }

}
