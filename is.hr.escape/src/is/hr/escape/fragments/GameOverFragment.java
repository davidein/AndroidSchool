package is.hr.escape.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import is.hr.escape.R;

/**
 * Displays the game over popup screen.
 * Clients pass in handlers to determine what should happen when users click the next level and
 * quit buttons
 */
public class GameOverFragment extends DialogFragment {
    private DialogInterface.OnClickListener m_quitListener;
    private DialogInterface.OnClickListener m_nextListener;
    private TextView m_score;
    private int m_iScore;

    public GameOverFragment(DialogInterface.OnClickListener quitListener,
                            DialogInterface.OnClickListener nextListener,
                            int score) {
        this.m_quitListener = quitListener;
        this.m_nextListener = nextListener;
        m_iScore = score;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity parent = getActivity();
        LayoutInflater inflater = parent.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);

        View view = inflater.inflate(R.layout.gameover, null);

        builder.setView(view)
                .setPositiveButton("Next puzzle", m_nextListener)
                .setNegativeButton("Quit", m_quitListener);

        m_score = (TextView) view.findViewById(R.id.score);
        m_score.setText(String.format("Moves: %d", m_iScore));

        return builder.create();
    }
}
