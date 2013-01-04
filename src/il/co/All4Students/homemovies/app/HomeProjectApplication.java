package il.co.All4Students.homemovies.app;

import android.app.Application;

public class HomeProjectApplication extends Application {
	private ApplicationPreference mSettings;

	@Override
	public void onCreate() {
		mSettings = new ApplicationPreference(this);

	}

	public ApplicationPreference getSettings() {
		return mSettings;
	}
}
