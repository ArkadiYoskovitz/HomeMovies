package il.co.All4Students.homemovies;

import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_CANCEL;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

/**
 * Preferences screen
 * 
 * @author Arkadi Yoskovitz
 */
public class ScreenPreferences extends PreferenceActivity {

	// Attributes
	protected Method mLoadHeaders = null;
	protected Method mHasHeaders = null;

	private EditTextPreference mEmail;
	private ListPreference mLanguage;
	private ListPreference mSortMethod;
//	private ApplicationPreference mApplicationPreference = new ApplicationPreference(ScreenPreferences.this);

	// System Events
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle aSavedState) {
		super.onCreate(aSavedState);
		try {
			mLoadHeaders = getClass().getMethod("loadHeadersFromResource",
					int.class, List.class);
			mHasHeaders = getClass().getMethod("hasHeaders");
		} catch (NoSuchMethodException e) {
			e.getStackTrace();
		}
		if (!isNewV11Prefs()) {
			addPreferencesFromResource(R.xml.screen_preferences);

			this.mEmail = (EditTextPreference) findPreference("AppPreffEmail");
			this.mLanguage = (ListPreference) findPreference("AppPreffLanguage");
			this.mSortMethod = (ListPreference) findPreference("AppPreffSortMethods");
		}
	}

	@Override
	@Deprecated
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {

		if (preference == mEmail) {

		} else if (preference == mLanguage) {

		} else if (preference == mSortMethod) {

		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	public void onBackPressed() {
		Intent returnIntent = new Intent();
		setResult(RESULT_CODE_CANCEL, returnIntent);
		finish();
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