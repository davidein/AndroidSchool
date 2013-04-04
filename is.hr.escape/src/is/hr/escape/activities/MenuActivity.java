package is.hr.escape.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import is.hr.escape.R;
import is.hr.escape.objects.Challenge;
import is.hr.escape.objects.Level;
import is.hr.escape.helpers.SQLHelper;
import is.hr.escape.helpers.XMLHelper;

import java.util.List;
import java.util.Map;

/**
 * The menu activity is opened when the application starts. It's the main menu for the game
 */
public class MenuActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(MenuActivity.class.getSimpleName(), Activity.MODE_PRIVATE);
        if(true) {//prefs.getBoolean("firstRun", true)) {
            getBaseContext().deleteDatabase("escape");
            Map<Challenge, List<Level>> defaultChallenges = XMLHelper.loadChallengesFromAssets(getAssets());
            SQLHelper helper = new SQLHelper(getBaseContext());
            for(Challenge challenge : defaultChallenges.keySet()) {
                helper.populateChallenge(challenge, defaultChallenges.get(challenge));
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstRun", false);
        }
        setContentView(R.layout.menu);
    }

    public void buttonClick(View view) {
        Intent intent = new Intent(this, ChooseChallengeActivity.class);
        startActivity(intent);
    }

    public void quitClick(View view)
    {
        this.finish();
    }
}
