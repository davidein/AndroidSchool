package is.hr.escape;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: heidar
 * Date: 3/24/13
 * Time: 11:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class GameOverFragment extends DialogFragment {
    private DialogInterface.OnClickListener quitListener;
    private DialogInterface.OnClickListener nextListener;
    private TextView _score;
    private int _iScore;

    public GameOverFragment(DialogInterface.OnClickListener quitListener,
                            DialogInterface.OnClickListener nextListener,
                            int score) {
        this.quitListener = quitListener;
        this.nextListener = nextListener;
        _iScore = score;
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
                .setPositiveButton("Next puzzle", nextListener)
                .setNegativeButton("Quit", quitListener);

        _score = (TextView) view.findViewById(R.id.score);
        _score.setText(String.format("Moves: %d", _iScore));

        return builder.create();
    }
}
