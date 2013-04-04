package is.hr.escape.helpers;

import android.content.res.AssetManager;
import android.util.Pair;
import is.hr.escape.objects.Challenge;
import is.hr.escape.objects.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Heiðar Þórðarson
 */
public class XMLHelper {
    private static String CHALLENGE_LIST_XML_PATH = "challengelist.xml";
    private static String CHALLENGE_TAG = "challenge";
    private static String CHALLENGE_NAME_TAG = "name";
    private static String CHALLENGE_XML_PATH_TAG = "puzzles";
    private static String PUZZLE_CHALLENGE_ID_TAG = "id";
    private static String PUZZLES_TAG = "puzzle";
    private static String PUZZLE_ID_TAG = "id";
    private static String PUZZLE_SETUP_TAG = "setup";

    private XMLHelper() {
    }

    public static Map<Challenge, List<Level>> loadChallengesFromAssets(AssetManager assets) {
        Map<Challenge, List<Level>> challenges = new HashMap<Challenge, List<Level>>();
        Map<String, String> challengeMap = getChallengeMap(assets);
        for(String challengeName : challengeMap.keySet()) {
            Pair<Integer, List<Level>> challengeIdLevelsPair = getChallenge(challengeMap.get(challengeName), assets);
            challenges.put(new Challenge(challengeIdLevelsPair.first, challengeName), challengeIdLevelsPair.second);
        }
        return challenges;
    }

    /**
     * @return A map of challenge names to their respective xml files
     */
    private static Map<String, String> getChallengeMap(AssetManager assets) {
        Map<String, String> challengeMap = new HashMap<String, String>();
        Document dom;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            dom = db.parse(assets.open(CHALLENGE_LIST_XML_PATH));
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
        Element root = dom.getDocumentElement();
        NodeList challengeList = root.getElementsByTagName(CHALLENGE_TAG);
        String name = null;
        String file = null;
        for(int i = 0; i < challengeList.getLength(); i++) {
            Node challengeNode = challengeList.item(i);
            NodeList properties = challengeNode.getChildNodes();
            for(int j = 0; j < properties.getLength(); j++) {
                Node property = properties.item(j);
                String pName = property.getNodeName();
                if(pName.equalsIgnoreCase(CHALLENGE_NAME_TAG)) {
                    name = property.getFirstChild().getNodeValue();
                } else if(pName.equalsIgnoreCase(CHALLENGE_XML_PATH_TAG)) {
                    file = property.getFirstChild().getNodeValue();
                }
            }
            if(name != null && file != null) {
                challengeMap.put(name, file);
            }
        }
        return challengeMap;
    }

    private static Pair<Integer, List<Level>> getChallenge(String file, AssetManager assets) {
        List<Level> levels = new ArrayList<Level>();
        Document dom;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            dom = db.parse(assets.open(file));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        String cId = null;

        Element root = dom.getDocumentElement();
        NodeList idList = root.getElementsByTagName(PUZZLE_CHALLENGE_ID_TAG);
        for(int i = 0; i < idList.getLength(); i++) {
            cId = idList.item(i).getFirstChild().getNodeValue();
        }
        if(cId == null) {
            throw new RuntimeException("Unable to find challenge id");
        }
        NodeList puzzleList = root.getElementsByTagName(PUZZLES_TAG);
        //Iterate through all puzzle elements
        for(int i = 0; i < puzzleList.getLength(); i++) {
            Node puzzle = puzzleList.item(i);
            String idStr = puzzle.getAttributes().getNamedItem(PUZZLE_ID_TAG).getNodeValue();
            String levelString = null;

            NodeList children = puzzle.getChildNodes();
            //Have to iterate through the children since it's parsing empty newlines between <puzzle> and <setup> as nodes
            for(int j = 0; j < children.getLength(); j++) {
                Node child = children.item(j);
                if(child.getNodeName().equalsIgnoreCase(PUZZLE_SETUP_TAG)) {
                    levelString = child.getFirstChild().getNodeValue();
                }
            }
            if(levelString != null && idStr != null) {
                Level level = new Level(Integer.valueOf(cId), Integer.valueOf(idStr), levelString, 0);
                levels.add(level);
            }
        }

        return new Pair<Integer, List<Level>>(Integer.valueOf(cId), levels);
    }
}
