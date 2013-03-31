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
 * User: Heiðar Þórðarson
 * Interface to load levels. Singleton
 */
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

    /**
     * Loads the challenge list by parsing the challengelist xml file and stores the challenge names and
     * their associated xml files in memory
     */
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

    /**
     * Loads all levels for a given challenge by parsing the associated xml file and stores them in memory
     * @param challenge Name of the challenge for which to load levels
     */
    private void loadChallenge(String challenge) {
        /*
            Expects document to have the format
            <challenge>
                <id>x</id>
                <name>xxx</name>
                <puzzle id="x">
                    <setup>...</setup>
                </puzzle>
                <puzzle id="x">
                ...
            </challenge>
         */
        List<Level> levels = new ArrayList<Level>();
        String file = challengeMap.get(challenge);
        Document dom;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            dom = db.parse(assets.open(file));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        Element root = dom.getDocumentElement();
        NodeList puzzleList = root.getElementsByTagName("puzzle");
        //Iterate through all puzzle elements
        for(int i = 0; i < puzzleList.getLength(); i++) {
            Node puzzle = puzzleList.item(i);
            String id = puzzle.getAttributes().getNamedItem("id").getNodeValue();
            String levelString = null;

            NodeList children = puzzle.getChildNodes();
            //Have to iterate through the children since it's parsing empty newlines between <puzzle> and <setup> as nodes
            for(int j = 0; j < children.getLength(); j++) {
                Node child = children.item(j);
                if(child.getNodeName().equalsIgnoreCase("setup")) {
                    levelString = child.getFirstChild().getNodeValue();
                }
            }
            if(levelString != null) {
                Level level = new Level(i, id, levelString);
                levels.add(level);
            }
        }

        levelMap.put(challenge, levels);
    }

    /**
     * @return A list of all available challenges (set of levels)
     */
    public List<String> getChallenges() {
        return challenges;
    }

    /**
     * Gets all levels of a given challenge
     * @param challenge The challenge to which the levels belong
     * @return A list of levels belonging to challenge
     */
    public List<Level> getLevels(String challenge) {
        if(!levelMap.containsKey(challenge)) {
            loadChallenge(challenge);
        }
        return levelMap.get(challenge);
    }
}
