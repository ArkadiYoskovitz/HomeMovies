package il.co.All4Students.homemovies;

import il.co.All4Students.homemovies.app.ApplicationPreference;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Preferences screen
 * 
 * @author Arkadi Yoskovitz
 */
public class ScreenPreferences extends PreferenceActivity {
	// Attributes
	private ApplicationPreference mSettings;
	private String mLanguage;

	// System Events
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle aSavedState) {
		super.onCreate(aSavedState);
		addPreferencesFromResource(R.xml.screen_preferences);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSettings = new ApplicationPreference(ScreenPreferences.this);
		mLanguage = mSettings.getLanguage();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		String subject = sharedPrefs.getString("AppPreffSubject",
				"Default Topic");
		String email = sharedPrefs.getString("AppPreffEmail",
				"John.Appleseed@iCloud.com");
		String language = sharedPrefs.getString("AppPreffLanguage", "EN");
		int sortMethod = Integer.parseInt(sharedPrefs.getString(
				"AppPreffSortMethods", "0"));
		boolean isColored = sharedPrefs.getBoolean("AppPreffEnableColor", true);
		boolean isPreview = sharedPrefs.getBoolean("AppPreffEnablePreview",
				true);

		if (email.length() == 0) {
			mSettings.setEmail("John.Appleseed@iCloud.com.com");
		}

		mSettings.setSubject(subject);
		mSettings.setEmail(email);
		mSettings.setLanguage(language);
		mSettings.setSortMethod(sortMethod);
		mSettings.setEnableColor(isColored);
		mSettings.setEnablePreview(isPreview);

		if (!mLanguage.equals(mSettings.getLanguage())) {
			AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

			mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000,
					PendingIntent.getActivity(getApplication()
							.getApplicationContext(), 0,
							new Intent(getIntent()),
							Intent.FLAG_ACTIVITY_NEW_TASK));
			System.exit(2);
		}

	}
}