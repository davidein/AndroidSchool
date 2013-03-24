package is.hr.escape;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

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

    public GameOverFragment(DialogInterface.OnClickListener quitListener,
                            DialogInterface.OnClickListener nextListener) {
        this.quitListener = quitListener;
        this.nextListener = nextListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity parent = getActivity();
        LayoutInflater inflater = parent.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);

        builder.setView(inflater.inflate(R.layout.gameover, null))
                .setPositiveButton("Next puzzle", nextListener)
                .setNegativeButton("Quit", quitListener);
        return builder.create();
    }
}
