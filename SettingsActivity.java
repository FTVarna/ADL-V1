import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity{
	
	private static final String OPT_HINTS = "hints";
	private static final boolean OPT_HINTS_DEF = false;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}

	/** 
	 * Returns the current value of the hints option 
	 * @return boolean 
	 */
	public static boolean hintsEanabled(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_HINTS, OPT_HINTS_DEF);
	}
}