package is.hr.escape;

import android.app.Activity;
import android.content.Intent;
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

        LevelSelectFragment[] fragments;
        int levelsPerFragment = 9;

        public SliderPageAdapter(FragmentManager fm) {
            super(fm);
            //TODO: Load levels
            //Map from difficulty type to xml file containing those levels
            List<Pair<String, String>> challenges = new ArrayList<Pair<String, String>>();
            //Map from xml file to a map of level ids to level strings
            Map<String, Map<String, String>> levelMap = new HashMap<String, Map<String, String>>();
            challenges.add(new Pair<String, String>("easy", "easy.xml"));
            challenges.add(new Pair<String, String>("medium", "medium.xml"));
            int totalFragments = 0;

            //Dummy routine for loading levels for each xml file
            for(Pair<String, String> pair : challenges) {
                Map<String, String> level = new HashMap<String, String>();
                if(pair.first.equals("easy")) {
                    level.put("1", "(H 1 3 2), (V 0 0 2), (V 0 2 3), (H 0 5 2), (H 2 0 3), (H 4 1 2), (V 3 2 3), (V 5 3 3)");
                    level.put("2", "(H 1 3 2), (V 0 0 2), (V 0 2 3), (H 0 5 2), (H 2 0 3), (H 4 1 2), (V 3 2 3), (V 5 3 3)");
                    level.put("3", "(H 1 3 2), (V 0 0 2), (V 0 2 3), (H 0 5 2), (H 2 0 3), (H 4 1 2), (V 3 2 3), (V 5 3 3)");
                    level.put("4", "(H 1 3 2), (V 0 0 2), (V 0 2 3), (H 0 5 2), (H 2 0 3), (H 4 1 2), (V 3 2 3), (V 5 3 3)");
                    level.put("5", "(H 1 3 2), (V 0 0 2), (V 0 2 3), (H 0 5 2), (H 2 0 3), (H 4 1 2), (V 3 2 3), (V 5 3 3)");
                    level.put("6", "(H 1 3 2), (V 0 0 2), (V 0 2 3), (H 0 5 2), (H 2 0 3), (H 4 1 2), (V 3 2 3), (V 5 3 3)");
                    level.put("7", "(H 1 3 2), (V 0 0 2), (V 0 2 3), (H 0 5 2), (H 2 0 3), (H 4 1 2), (V 3 2 3), (V 5 3 3)");
                    level.put("8", "(H 1 3 2), (V 0 0 2), (V 0 2 3), (H 0 5 2), (H 2 0 3), (H 4 1 2), (V 3 2 3), (V 5 3 3)");
                    level.put("9", "(H 1 3 2), (V 0 0 2), (V 0 2 3), (H 0 5 2), (H 2 0 3), (H 4 1 2), (V 3 2 3), (V 5 3 3)");
                    level.put("10", "(H 1 3 2), (V 0 0 2), (V 0 2 3), (H 0 5 2), (H 2 0 3), (H 4 1 2), (V 3 2 3), (V 5 3 3)");
                } else {
                    level.put("1", "(H 1 3 2), (V 0 0 2), (V 0 2 3), (H 0 5 2), (H 2 0 3), (H 4 1 2), (V 3 2 3), (V 5 3 3)");
                }
                levelMap.put(pair.second, level);
                //Add fragments depending on the number of levels for this challenge
                totalFragments += level.size() / (levelsPerFragment + 1) + 1;
            }

            fragments = new LevelSelectFragment[totalFragments];
            int fIndex = 0;
            int lIndex = 1;
            LevelSelectFragment currentFragment = null;
            for(Pair<String, String> pair : challenges) {
                lIndex = 1;
                Map<String, String> levels = levelMap.get(pair.second);
                for(Map.Entry<String, String> level : levels.entrySet()) {
                    if(currentFragment == null) {
                        currentFragment = new LevelSelectFragment(pair.first);
                    }

                    currentFragment.addLevel(lIndex, level.getKey(), level.getValue());

                    if(currentFragment.getLevelCount() >= levelsPerFragment) {
                        fragments[fIndex] = currentFragment;
                        currentFragment = null;
                        fIndex++;
                    }
                    lIndex++;
                }
                if(currentFragment != null) {
                    fragments[fIndex] = currentFragment;
                    currentFragment = null;
                    fIndex++;
                }
            }
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
}