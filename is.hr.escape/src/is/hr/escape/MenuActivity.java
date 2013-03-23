package is.hr.escape;

import android.app.Activity;
import android.os.Bundle;

/**
 * User: David Einarsson
 * Date: 23.3.2013
 * Time: 09:32
 */
public class MenuActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menu);
    }
}
