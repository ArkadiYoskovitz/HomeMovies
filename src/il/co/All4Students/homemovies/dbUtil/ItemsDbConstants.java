package il.co.All4Students.homemovies.dbUtil;

/**
 * 
 * @author Arkadi Yoskovitz
 * 
 *         Start ItemsDbConstants
 */
public class ItemsDbConstants {
	/**
	 * DbConstants
	 * 
	 * All Static variables used in the access to the SQLiteDatabase
	 */

	// Database Version
	public static final int DATABASE_VERSION = 1;

	// Database Name
	public static final String DATABASE_NAME = "items.db";

	// Contacts table name
	public static final String TABLE_ITEMS = "items";

	// Contacts Table Columns names
	public static final String KEY_ID = "_id";
	public static final String KEY_SUBJECT = "subject";
	public static final String KEY_BODY = "body";
	public static final String KEY_URLWEB = "urlWeb";
	public static final String KEY_URLLOCAL = "urlLocal";
	public static final String KEY_RT_ID = "rt_ID";
	public static final String KEY_RANK = "rank";
	public static final String KEY_VIEWD = "viewd";
	public static final String KEY_COLOR = "color";

	// Helper static fields
	public static final String LOG_TAG = "MovieDb";
	public static final String SELECT_HELPER = "SELECT  * FROM " + TABLE_ITEMS;
	public static final String WHERE_SUBJECT = "subject like ?";

}
