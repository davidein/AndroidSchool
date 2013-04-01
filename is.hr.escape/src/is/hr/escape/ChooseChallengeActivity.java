package is.hr.escape;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import is.hr.escape.helpers.Level;
import is.hr.escape.helpers.LevelHelper;

import java.util.*;

/**
 * User: David Einarsson
 * Date: 24.3.2013
 * Time: 09:56
 */
public class ChooseChallengeActivity extends FragmentActivity {

    private ViewPager pager;
    private SliderPageAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);
        pager = (ViewPager)findViewById(R.id.pager);
        adapter = new SliderPageAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
    }

    public void levelClick(View view) {
        Button btn = (Button)view;
        String level = (String)btn.getTag();
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }

    private class SliderPageAdapter extends FragmentStatePagerAdapter {

        List<LevelSelectFragment> fragments = new ArrayList<LevelSelectFragment>();
        int levelsPerFragment = 9;

        public SliderPageAdapter(FragmentManager fm) {
            super(fm);

            int lIndex = 1;
            LevelSelectFragment currentFragment = null;

            for(String challenge : LevelHelper.getInstance(getAssets()).getChallenges()) {
                lIndex = 1;
                List<Level> levels = LevelHelper.getInstance(getAssets()).getLevels(challenge);
                for(Level level : levels) {
                    if(currentFragment == null) {
                        currentFragment = new LevelSelectFragment(challenge);
                    }

                    currentFragment.addLevel(lIndex, level.levelId, level.level);

                    if(currentFragment.getLevelCount() >= levelsPerFragment) {
                        fragments.add(currentFragment);
                        currentFragment = null;
                    }
                    lIndex++;
                }
                if(currentFragment != null) {
                    fragments.add(currentFragment);
                    currentFragment = null;
                }
            }
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}