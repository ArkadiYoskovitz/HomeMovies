package il.co.All4Students.homemovies.util.log.db;

import static il.co.All4Students.homemovies.util.log.db.LogDbConstants.DATABASE_NAME;
import static il.co.All4Students.homemovies.util.log.db.LogDbConstants.DATABASE_VERSION;
import static il.co.All4Students.homemovies.util.log.db.LogDbConstants.KEY_ID;
import static il.co.All4Students.homemovies.util.log.db.LogDbConstants.KEY_LOG;
import static il.co.All4Students.homemovies.util.log.db.LogDbConstants.LOG_TAG;
import static il.co.All4Students.homemovies.util.log.db.LogDbConstants.TABLE_LOG;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Start DbHelper
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
public class LogDbHelper extends SQLiteOpenHelper {

	// Constractor
	public LogDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public LogDbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			String CREATE_ITEM_TABLE = ((StringBuilder) new StringBuilder())
					.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_LOG)
					.append(" ( ").append(KEY_ID)
					.append(" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ")
					.append(KEY_LOG).toString();

			db.execSQL(CREATE_ITEM_TABLE);

			Log.e(LOG_TAG, "Create table, table creation OK for line: "
					+ CREATE_ITEM_TABLE);
		} catch (SQLiteException ex) {
			Log.e(LOG_TAG, "Create table exception: " + ex.getMessage());
		}
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			// Drop older table if existed
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOG);
			// Create tables again
			onCreate(db);
		} catch (SQLiteException e) {
			Log.d(LOG_TAG, "Table upgrade: " + e.getMessage());
		}
	}
}