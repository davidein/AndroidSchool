package is.hr.escape;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import is.hr.escape.helpers.Challenge;
import is.hr.escape.helpers.Level;
import is.hr.escape.helpers.SQLHelper;
import is.hr.escape.helpers.XMLHelper;

import java.util.*;

/**
 * User: David Einarsson
 * Date: 24.3.2013
 * Time: 09:56
 */
public class ChooseChallengeActivity extends FragmentActivity {

    private ViewPager pager;
    private SliderPageAdapter adapter;

    private final int levelsPerFragment = 9;
    private Map<Integer, String> fragmentMap = new HashMap<Integer, String>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);
        pager = (ViewPager)findViewById(R.id.pager);
        adapter = new SliderPageAdapter(getLayoutInflater());
        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        SQLHelper helper = new SQLHelper(getBaseContext());

        for(Challenge challenge : helper.getAllChallenges()) {
            List<Level> levels = helper.getChallengeLevels(challenge);
            List<Level> fragmentLevels = null;
            for(Level level : levels) {
                if(fragmentLevels == null) {
                    fragmentLevels = new ArrayList<Level>();
                }

                fragmentLevels.add(level);

                if(fragmentLevels.size() >= levelsPerFragment) {
                    adapter.addPage(challenge, fragmentLevels);

                    fragmentLevels = null;
                }
            }
            if(fragmentLevels != null) {
                adapter.addPage(challenge, fragmentLevels);
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("derp", "stop");
        adapter = null;
        pager.setAdapter(null);
    }

    public void levelClick(View view) {
        Button btn = (Button)view;
        String level = (String)btn.getTag();
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }
}