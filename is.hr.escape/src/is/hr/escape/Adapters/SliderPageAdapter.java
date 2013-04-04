package is.hr.escape.Adapters;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import is.hr.escape.R;
import is.hr.escape.objects.Challenge;
import is.hr.escape.objects.Level;
import is.hr.escape.helpers.SQLHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Heiðar Þórðarson
 */

public class SliderPageAdapter extends PagerAdapter {
    private List<Item> items = new ArrayList<Item>();
    private Map<String, View> viewMap = new HashMap<String, View>();
    private LayoutInflater inflater;

    public SliderPageAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Item item = items.get(position);
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.levelselect, null);
        container.addView(root);

        GridView grid = (GridView)root.findViewById(R.id.grid);
        TextView label = (TextView)root.findViewById(R.id.difficulty);
        label.setText(item.challenge.getName());
        LevelAdapter adapter = new LevelAdapter(inflater, item.levels);
        grid.setAdapter(adapter);
        viewMap.put(item.challenge.getName(), root);
        return root;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewMap.get(object));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void addPage(Challenge challenge, List<Level> levels) {
        items.add(new Item(challenge, levels));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private class Item {
        public final Challenge challenge;
        public final List<Level> levels;

        public Item(Challenge challenge, List<Level> levels) {
            this.challenge = challenge;
            this.levels = levels;
        }
    }


    private class LevelAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<Level> levels;
        private SQLHelper sqlHelper;

        public LevelAdapter(LayoutInflater inflater, List<Level> levels) {
            this.inflater = inflater;
            this.levels = levels;
            sqlHelper = new SQLHelper(inflater.getContext());
        }

        public int getCount() {
            return levels.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout linearLayout;
            if(convertView == null) {
                linearLayout = (LinearLayout)inflater.inflate(R.layout.level, null);
            } else {
                linearLayout = (LinearLayout)convertView;
            }
            Level level = levels.get(position);

            linearLayout.setTag(level);

            LinearLayout linearLayoutItem = (LinearLayout) linearLayout.findViewById(R.id.level_item);

            TextView levelTxt = (TextView) linearLayout.findViewById(R.id.level_txt);
            TextView levelScoreTxt = (TextView) linearLayout.findViewById(R.id.levelScore_txt);

            levelTxt.setText(String.valueOf(level.getLevelId()));

            int score = level.getMoveCount();

            if (score == 0)
            {
                levelScoreTxt.setText("No score!");
            }
            else
            {
                linearLayoutItem.setBackgroundResource(R.drawable.container_level_select_complete);
                levelScoreTxt.setText(String.format("Highscore: %d", score));
            }
            return linearLayout;
        }
    }
}
