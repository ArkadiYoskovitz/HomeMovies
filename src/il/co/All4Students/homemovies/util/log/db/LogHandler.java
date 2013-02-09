package il.co.All4Students.homemovies.util.log.db;

import static il.co.All4Students.homemovies.util.log.db.LogDbConstants.DATABASE_NAME;
import static il.co.All4Students.homemovies.util.log.db.LogDbConstants.DATABASE_VERSION;
import static il.co.All4Students.homemovies.util.log.db.LogDbConstants.KEY_LOG;
import static il.co.All4Students.homemovies.util.log.db.LogDbConstants.LOG_TAG;
import static il.co.All4Students.homemovies.util.log.db.LogDbConstants.SELECT_HELPER;
import static il.co.All4Students.homemovies.util.log.db.LogDbConstants.TABLE_LOG;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Start DbHandler
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
public class LogHandler {
	private LogDbHelper logDBHelper;

	public LogHandler(Context context) {
		logDBHelper = new LogDbHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	/**
	 * Adding a new item to the SQLiteDatabase
	 * 
	 * @param logString
	 */
	public void addLogItem(String logString) {
		SQLiteDatabase db = logDBHelper.getWritableDatabase();

		try {
			ContentValues values = new ContentValues();
			values.put(KEY_LOG, logString);

			// Inserting Row
			db.insert(TABLE_LOG, null, values);
		} catch (SQLException e) {
			Log.d(LOG_TAG, "Add Item: " + e.getMessage());
		} finally {
			db.close(); // Closing database connection
		}
	}

	/**
	 * Getting All logs
	 * 
	 * @return holderString.toString();
	 */
	public String getAllLogs() {
		StringBuilder holderString = new StringBuilder();
		// Select All Query
		String selectQuery = SELECT_HELPER;

		SQLiteDatabase db = logDBHelper.getReadableDatabase();
		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					holderString.append(Integer.parseInt(cursor.getString(0)))
							.append(cursor.getString(1)).append("\n\n");
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (SQLException e) {
			Log.d(LOG_TAG, "Get All Items: " + e.getMessage());
		} finally {
			db.close();
		}

		return holderString.toString();
	}

	/**
	 * Deleting all logs from the Database
	 */
	public void deleteAllLogs() {
		SQLiteDatabase db = logDBHelper.getWritableDatabase();
		try {
			String sqlDelete = "DELETE FROM " + TABLE_LOG;
			db.execSQL(sqlDelete);
		} catch (Exception e) {
			Log.d(LOG_TAG, "Delete Item: " + e.getMessage());
		} finally {
			db.close();
		}
	}
}
