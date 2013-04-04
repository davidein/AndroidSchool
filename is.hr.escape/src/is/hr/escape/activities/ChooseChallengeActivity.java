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
 * This is the level select activity.
 * The user is presented with all puzzles for a given challenge and can slide between different challenges
 */
public class ChooseChallengeActivity extends FragmentActivity {
    /**
     * The challenges are stored in a ViewPager. Each page contains a list of all puzzles for its challenge.
     * The current page is stored both when the activity is stopped and when we start a new activity so
     * that when a user navigates back to this activity, he will return to the same challenge page.
     *
     * Note that data is loaded from database every time this activity resumes.
     */

    private ViewPager m_pager;
    private SliderPageAdapter m_adapter;
    private int m_currentPage = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);
    }

    @Override
    public void onResume() {
        super.onResume();
        m_pager = (ViewPager)findViewById(R.id.pager);
        m_adapter = new SliderPageAdapter(getLayoutInflater());
        m_pager.setAdapter(m_adapter);

        SQLHelper helper = new SQLHelper(getBaseContext());

        for(Challenge challenge : helper.getAllChallenges()) {
            List<Level> levels = helper.getChallengeLevels(challenge);

            m_adapter.addPage(challenge, levels);
        }
        m_pager.setCurrentItem(m_currentPage);
        m_adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("currentPage", m_pager.getCurrentItem());
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        m_currentPage = bundle.getInt("currentPage", 0);
    }

    /**
     * Callback for when users select a level. Starts a GameActivity on the selected level
     * @param view
     */
    public void levelClick(View view) {
        //The level is stored in the view's tag so that we can determine which level was selected
        Level level = (Level)view.getTag();
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("level", level.getLevel());
        intent.putExtra("levelId", level.getLevelId());
        intent.putExtra("challengeId", level.getChallengeId());
        m_currentPage = m_pager.getCurrentItem();
        startActivity(intent);
    }
}