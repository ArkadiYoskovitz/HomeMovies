package il.co.All4Students.homemovies.app;

import java.util.Locale;

import android.app.Application;
import android.content.res.Configuration;

/**
 * HomeProjectApplication
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
public class HomeProjectApplication extends Application {
	private ApplicationPreference mSettings;
	private Locale locale = null;

	@Override
	public void onCreate() {
		mSettings = new ApplicationPreference(this);

		Configuration config = getBaseContext().getResources()
				.getConfiguration();
		String lang = mSettings.getLanguage();

		if (!lang.equals("") && !config.locale.getLanguage().equals(lang)) {
			locale = new Locale(lang);
			Locale.setDefault(locale);
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config,
					getBaseContext().getResources().getDisplayMetrics());
		}
	}

	public ApplicationPreference getSettings() {
		return mSettings;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (locale != null) {
			newConfig.locale = locale;
			Locale.setDefault(locale);
			getBaseContext().getResources().updateConfiguration(newConfig,
					getBaseContext().getResources().getDisplayMetrics());
		}
	}
}
