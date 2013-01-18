package il.co.All4Students.homemovies;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Preferences screen
 * 
 * @author Arkadi Yoskovitz
 * 
 */
public class ScreenPreferences extends PreferenceActivity {

	// Attributes
	protected Method mLoadHeaders = null;
	protected Method mHasHeaders = null;

	// System Events
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle aSavedState) {
		// onBuildHeaders() will be called during super.onCreate()
		try {
			mLoadHeaders = getClass().getMethod("loadHeadersFromResource",
					int.class, List.class);
			mHasHeaders = getClass().getMethod("hasHeaders");
		} catch (NoSuchMethodException e) {
			e.getStackTrace();
		}
		super.onCreate(aSavedState);
		if (!isNewV11Prefs()) {
			addPreferencesFromResource(R.xml.screen_preferences);
		}
	}

	@Override
	public void onBuildHeaders(List<Header> aTarget) {
		try {
			mLoadHeaders.invoke(this, new Object[] { R.xml.screen_preferences,
					aTarget });
		} catch (IllegalArgumentException e) {
			e.getStackTrace();
		} catch (IllegalAccessException e) {
			e.getStackTrace();
		} catch (InvocationTargetException e) {
			e.getStackTrace();
		}
	}

	// Additional Methods
	/**
	 * Checks to see if using new v11+ way of handling PrefFragments.
	 * 
	 * @return Returns false pre-v11, else checks to see if using headers.
	 */
	public boolean isNewV11Prefs() {
		if (mHasHeaders != null && mLoadHeaders != null) {
			try {
				return (Boolean) mHasHeaders.invoke(this);
			} catch (IllegalArgumentException e) {
				e.getStackTrace();
			} catch (IllegalAccessException e) {
				e.getStackTrace();
			} catch (InvocationTargetException e) {
				e.getStackTrace();
			}
		}
		return false;
	}

}