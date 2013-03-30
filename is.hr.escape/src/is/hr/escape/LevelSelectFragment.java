package is.hr.escape;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import is.hr.escape.helpers.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: heidar
 * Date: 3/29/13
 * Time: 10:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class LevelSelectFragment extends Fragment {
    LevelAdapter adapter = new LevelAdapter();
    String difficulty;

    public LevelSelectFragment(String difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.levelselect, container, false);
        GridView grid = (GridView)root.findViewById(R.id.grid);
        TextView label = (TextView)root.findViewById(R.id.difficulty);
        label.setText(difficulty);
        adapter.setInflater(inflater);
        grid.setAdapter(adapter);
        return root;
    }

    public int getLevelCount() {
        return adapter.getCount();
    }

    public void addLevel(int levelIndex, String levelId, String level) {
        adapter.addLevel(new Level(levelIndex, levelId, level));
    }



    private class LevelAdapter extends BaseAdapter {
        private List<Level> levels = new ArrayList<Level>();
        private LayoutInflater inflater;

        public void setInflater(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        public void addLevel(Level level) {
            levels.add(level);
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
            Button button;
            if(convertView == null) {
                button = (Button)inflater.inflate(R.layout.level, null);
            } else {
                button = (Button)convertView;
            }
            Level level = levels.get(position);
            button.setText(String.valueOf(level.index));
            button.setTag(level.level);
            return button;
        }
    }
}
