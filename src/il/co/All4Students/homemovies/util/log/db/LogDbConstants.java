package il.co.All4Students.homemovies.util.log.db;

/**
 * Start LogDbConstants
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
public class LogDbConstants {
	/**
	 * DbConstants
	 * 
	 * All Static variables used in the access to the SQLiteDatabase
	 */

	// Database Version
	public static final int DATABASE_VERSION = 1;

	// Database Name
	public static final String DATABASE_NAME = "log_database.db";

	// Contacts table name
	public static final String TABLE_LOG = "log_recording";

	// Contacts Table Columns names
	public static final String KEY_ID = "_id";
	public static final String KEY_DATE = "date";
	public static final String KEY_TIME = "time";
	public static final String KEY_TAG = "tag";
	public static final String KEY_LOG = "log";

	// Helper static fields
	public static final String LOG_TAG = "LogDB";
	public static final String SELECT_HELPER = "SELECT  * FROM " + TABLE_LOG;
}
