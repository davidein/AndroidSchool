package is.hr.escape.helpers;

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
 * Created with IntelliJ IDEA.
 * User: heidar
 * Date: 3/30/13
 * Time: 6:24 PM
 */
//Singleton
public class LevelHelper {
    private static LevelHelper instance;
    private static AssetManager assets;

    public static LevelHelper getInstance(AssetManager assets) {
        if(instance == null) {
            instance = new LevelHelper(assets);
        }
        return instance;
    }
    private List<String> challenges = new ArrayList<String>();
    //Maps from challenge name to challenge xml file
    private Map<String, String> challengeMap = new HashMap<String, String>();
    //Maps from challenge name to a list of its levels
    private Map<String, List<Level>> levelMap = new HashMap<String, List<Level>>();

    private LevelHelper(AssetManager assets) {
        this.assets = assets;
        loadChallenges();
    }

    private void loadChallenges() {
        Document dom;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            dom = db.parse(assets.open("challengelist.xml"));
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
        Element root = dom.getDocumentElement();
        NodeList challengeList = root.getElementsByTagName("challenge");
        String name = null;
        String file = null;
        for(int i = 0; i < challengeList.getLength(); i++) {
            Node challengeNode = challengeList.item(i);
            NodeList properties = challengeNode.getChildNodes();
            for(int j = 0; j < properties.getLength(); j++) {
                Node property = properties.item(j);
                String pName = property.getNodeName();
                if(pName.equalsIgnoreCase("name")) {
                    name = property.getFirstChild().getNodeValue();
                } else if(pName.equalsIgnoreCase("puzzles")) {
                    file = property.getFirstChild().getNodeValue();
                }
            }
            if(name != null && file != null) {
                challenges.add(name);
                challengeMap.put(name, file);
            }
        }
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
