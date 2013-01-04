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
	// Attributes
	static final String PREF_FILE_NAME = "AppPreff";
	// Application Preference Keys
	private static final String KEY_SUBJECT = "Subject";
	private static final String KEY_EMAIL = "Email";
	private static final String KEY_COLOR = "Color";
	private static final String KEY_LANGUAGE = "Language";
	// Application Preference Default values
	private static final String DEFAULT_SUBJECT = "Default Topic";
	private static final String DEFAULT_EMAIL = "John.Appleseed@iCloud.com";
	private static final int DEFAULT_COLOR = 0xffffffff;
	private static final String DEFAULT_LANGUAGE = "EN";

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
	/**
	 * 
	 * @param subject
	 */
	public void setSubject(String subject) {
		Editor editor = settings.edit();
		editor.putString(KEY_SUBJECT, subject);
		editor.commit();
	}

	/**
	 * 
	 * @return
	 */
	public String getSubject() {
		return settings.getString(KEY_SUBJECT, DEFAULT_SUBJECT);
	}

	/**
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		Editor editor = settings.edit();
		editor.putString(KEY_EMAIL, email);
		editor.commit();
	}

	/**
	 * 
	 * @return
	 */
	public String getEmail() {
		return settings.getString(KEY_EMAIL, DEFAULT_EMAIL);
	}

	/**
	 * 
	 * @param color
	 */
	public void setColor(int color) {
		Editor editor = settings.edit();
		editor.putInt(KEY_COLOR, color);
		editor.commit();
	}

	/**
	 * 
	 * @return
	 */
	public int getColor() {
		return settings.getInt(KEY_COLOR, DEFAULT_COLOR);
	}

	/**
	 * Set the Language in the preferences
	 * 
	 * @param language
	 *            to save into preferences
	 */
	public void setLanguage(String language) {
		Editor editor = settings.edit();
		editor.putString(KEY_EMAIL, language);
		editor.commit();
	}

	/**
	 * 
	 * @return language stored in preferences
	 */
	public String getLanguage() {
		return settings.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE);
	}

	// //////////////////////////////////////////////

	public static String getDefaultSubject() {
		return DEFAULT_SUBJECT;
	}

	public static String getDefaultEmail() {
		return DEFAULT_EMAIL;
	}

	public static int getDefaultColor() {
		return DEFAULT_COLOR;
	}

	public static String getDefaultLanguage() {
		return DEFAULT_LANGUAGE;
	}

}
