package il.co.All4Students.homemovies.dbUtil;

import static il.co.All4Students.homemovies.dbUtil.ItemsDbConstants.DATABASE_NAME;
import static il.co.All4Students.homemovies.dbUtil.ItemsDbConstants.DATABASE_VERSION;
import static il.co.All4Students.homemovies.dbUtil.ItemsDbConstants.KEY_BODY;
import static il.co.All4Students.homemovies.dbUtil.ItemsDbConstants.KEY_COLOR;
import static il.co.All4Students.homemovies.dbUtil.ItemsDbConstants.KEY_ID;
import static il.co.All4Students.homemovies.dbUtil.ItemsDbConstants.KEY_RANK;
import static il.co.All4Students.homemovies.dbUtil.ItemsDbConstants.KEY_RT_ID;
import static il.co.All4Students.homemovies.dbUtil.ItemsDbConstants.KEY_SUBJECT;
import static il.co.All4Students.homemovies.dbUtil.ItemsDbConstants.KEY_URLLOCAL;
import static il.co.All4Students.homemovies.dbUtil.ItemsDbConstants.KEY_URLWEB;
import static il.co.All4Students.homemovies.dbUtil.ItemsDbConstants.KEY_VIEWD;
import static il.co.All4Students.homemovies.dbUtil.ItemsDbConstants.LOG_TAG;
import static il.co.All4Students.homemovies.dbUtil.ItemsDbConstants.TABLE_ITEMS;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * @author Arkadi Yoskovitz
 * 
 *         Start DbHelper
 */
public class ItemsDbHelper extends SQLiteOpenHelper {

	// Constractor
	public ItemsDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public ItemsDbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			String CREATE_ITEM_TABLE = ((StringBuilder) new StringBuilder())
					.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_ITEMS)
					.append(" ( ").append(KEY_ID)
					.append(" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ")
					.append(KEY_SUBJECT).append(" TEXT NOT NULL, ")
					.append(KEY_BODY).append(" TEXT NOT NULL, ")
					.append(KEY_URLWEB).append(" TEXT NOT NULL, ")
					.append(KEY_URLLOCAL).append(" TEXT NOT NULL, ")
					.append(KEY_RT_ID).append(" INTEGER NOT NULL, ")
					.append(KEY_RANK).append(" INTEGER NOT NULL, ")
					.append(KEY_VIEWD).append(" INTEGER NOT NULL, ")
					.append(KEY_COLOR).append(" INTEGER NOT NULL )").toString();

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
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
			// Create tables again
			onCreate(db);
		} catch (SQLiteException e) {
			Log.d(LOG_TAG, "Table upgrade: " + e.getMessage());
		}
	}

}
