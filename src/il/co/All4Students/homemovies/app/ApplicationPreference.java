package il.co.All4Students.homemovies.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * @author Arkadi Yoskovitz This class stores and manages all the preferences
 *         ￼￼￼￼￼￼￼￼￼ for the application
 */

public class ApplicationPreference {

	// Constants
	static final String PREF_FILE_NAME = "HomeMoviesAppPreff";

	// Application Preference Keys
	private static final String KEY_SUBJECT = "AppPreffSubject";
	private static final String KEY_EMAIL = "AppPreffEmail";
	private static final String KEY_LANGUAGE = "AppPreffLanguage";
	private static final String KEY_SORT_METHOD = "AppPreffSortMethods";
	private static final String KEY_ENABLE_COLOR = "AppPreffEnableColor";

	// Application Preference Default values
	private static final String DEFAULT_SUBJECT = "Default Topic";
	private static final String DEFAULT_EMAIL = "John.Appleseed@iCloud.com";
	private static final String DEFAULT_LANGUAGE = "EN";
	private static final int DEFAULT_SORT_METHOD = 0;
	private static final boolean DEFAULT_ENABLE_COLOR = true;

	// Attributes
	private final SharedPreferences settings;

	// Constractors
	/**
	 * @param act
	 *            The context from which to pick SharedPreferences
	 */
	public ApplicationPreference(Context act) {
		settings = act.getSharedPreferences(PREF_FILE_NAME,
				Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.commit();
	}

	/*
	 * Get / Set Methods
	 */
	public void setSubject(String subject) {
		Editor editor = settings.edit();
		editor.putString(KEY_SUBJECT, subject);
		editor.commit();
	}

	public String getSubject() {
		return settings.getString(KEY_SUBJECT, DEFAULT_SUBJECT);
	}

	public void setEmail(String email) {
		Editor editor = settings.edit();
		editor.putString(KEY_EMAIL, email);
		editor.commit();
	}

	public String getEmail() {
		return settings.getString(KEY_EMAIL, DEFAULT_EMAIL);
	}

	public void setLanguage(String language) {
		Editor editor = settings.edit();
		editor.putString(KEY_LANGUAGE, language);
		editor.commit();
	}

	public String getLanguage() {
		return settings.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE);
	}

	public void setSortMethod(String Sort) {
		Editor editor = settings.edit();
		editor.putString(KEY_SORT_METHOD, Sort);
		editor.commit();
	}

	public int getSortMethod() {
		return settings.getInt(KEY_SORT_METHOD, DEFAULT_SORT_METHOD);
	}

	public void setEnableColor(Boolean isColored) {
		Editor editor = settings.edit();
		editor.putBoolean(KEY_ENABLE_COLOR, isColored);
		editor.commit();
	}

	public boolean getEnableColor() {
		return settings.getBoolean(KEY_ENABLE_COLOR, DEFAULT_ENABLE_COLOR);
	}

	// //////////////////////////////////////////////

	public static String getDefaultSubject() {
		return DEFAULT_SUBJECT;
	}

	public static String getDefaultEmail() {
		return DEFAULT_EMAIL;
	}

	public static String getDefaultLanguage() {
		return DEFAULT_LANGUAGE;
	}

	public static int getDefaultSortMethod() {
		return DEFAULT_SORT_METHOD;
	}

	public static boolean isDefaultEnableColor() {
		return DEFAULT_ENABLE_COLOR;
	}

}
