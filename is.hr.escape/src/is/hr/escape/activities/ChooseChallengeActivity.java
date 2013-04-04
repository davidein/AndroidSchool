package is.hr.escape.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.view.View;
import is.hr.escape.R;
import is.hr.escape.Adapters.SliderPageAdapter;
import is.hr.escape.objects.Challenge;
import is.hr.escape.objects.Level;
import is.hr.escape.helpers.SQLHelper;

import java.util.*;

/**
 * User: David Einarsson
 * Date: 24.3.2013
 * Time: 09:56
 */
public class ChooseChallengeActivity extends FragmentActivity {

    private ViewPager pager;
    private SliderPageAdapter adapter;
    private int currentPage = 0;

    private final int levelsPerFragment = 9;
    private Map<Integer, String> fragmentMap = new HashMap<Integer, String>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);
    }

    @Override
    public void onResume() {
        super.onResume();
        pager = (ViewPager)findViewById(R.id.pager);
        adapter = new SliderPageAdapter(getLayoutInflater());
        pager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {


            }
        });
        pager.setAdapter(adapter);

        SQLHelper helper = new SQLHelper(getBaseContext());

        for(Challenge challenge : helper.getAllChallenges()) {
            List<Level> levels = helper.getChallengeLevels(challenge);
            List<Level> fragmentLevels = null;
            for(Level level : levels) {
                if(fragmentLevels == null) {
                    fragmentLevels = new ArrayList<Level>();
                }

                fragmentLevels.add(level);
            }
            if(fragmentLevels != null) {
                adapter.addPage(challenge, fragmentLevels);
            }
        }
        pager.setCurrentItem(currentPage);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("currentPage", pager.getCurrentItem());
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        currentPage = bundle.getInt("currentPage", 0);
    }

    public void levelClick(View view) {
        Level level = (Level)view.getTag();
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("level", level.getLevel());
        intent.putExtra("levelId", level.getLevelId());
        intent.putExtra("challengeId", level.getChallengeId());
        currentPage = pager.getCurrentItem();
        startActivity(intent);
    }
}