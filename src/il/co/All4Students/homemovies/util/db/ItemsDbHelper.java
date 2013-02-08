package il.co.All4Students.homemovies.util.db;

import static il.co.All4Students.homemovies.util.db.ItemsDbConstants.DATABASE_NAME;
import static il.co.All4Students.homemovies.util.db.ItemsDbConstants.DATABASE_VERSION;
import static il.co.All4Students.homemovies.util.db.ItemsDbConstants.KEY_BODY;
import static il.co.All4Students.homemovies.util.db.ItemsDbConstants.KEY_COLOR;
import static il.co.All4Students.homemovies.util.db.ItemsDbConstants.KEY_ID;
import static il.co.All4Students.homemovies.util.db.ItemsDbConstants.KEY_RANK;
import static il.co.All4Students.homemovies.util.db.ItemsDbConstants.KEY_RT_ID;
import static il.co.All4Students.homemovies.util.db.ItemsDbConstants.KEY_SUBJECT;
import static il.co.All4Students.homemovies.util.db.ItemsDbConstants.KEY_URLLOCAL;
import static il.co.All4Students.homemovies.util.db.ItemsDbConstants.KEY_URLWEB;
import static il.co.All4Students.homemovies.util.db.ItemsDbConstants.KEY_VIEWD;
import static il.co.All4Students.homemovies.util.db.ItemsDbConstants.LOG_TAG;
import static il.co.All4Students.homemovies.util.db.ItemsDbConstants.TABLE_ITEMS;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * DbHelper - A helper class to manage database creation and version management.
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
public class ItemsDbHelper extends SQLiteOpenHelper {

	// Constractor
	/**
	 * ItemsDbHelper - base constractor
	 * 
	 * @param context
	 *            - to use to open or create the database
	 */
	public ItemsDbHelper(Context context) {
		this(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * ItemsDbHelper - Create a helper object to create, open, and/or manage a
	 * database. This method always returns very quickly. The database is not
	 * actually created or opened until one of getWritableDatabase() or
	 * getReadableDatabase() is called.
	 * 
	 * @param context
	 *            - to use to open or create the database
	 * @param name
	 *            - of the database file, or null for an in-memory database
	 * @param factory
	 *            - to use for creating cursor objects, or null for the default
	 * @param version
	 *            - number of the database (starting at 1); if the database is
	 *            older, onUpgrade(SQLiteDatabase, int, int) will be used to
	 *            upgrade the database; if the database is newer,
	 *            onDowngrade(SQLiteDatabase, int, int) will be used to
	 *            downgrade the database
	 */
	public ItemsDbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	// Creating Tables
	/**
	 * onCreate - Called when the database is created for the first time. This
	 * is where the creation of tables and the initial population of the tables
	 * should happen.
	 * 
	 * @param db
	 *            - The database.
	 */
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

	/**
	 * onUpgrade - Called when the database needs to be upgraded. The
	 * implementation should use this method to drop tables, add tables, or do
	 * anything else it needs to upgrade to the new schema version.
	 * 
	 * The SQLite ALTER TABLE documentation can be found here. If you add new
	 * columns you can use ALTER TABLE to insert them into a live table. If you
	 * rename or remove columns you can use ALTER TABLE to rename the old table,
	 * then create the new table and then populate the new table with the
	 * contents of the old table.
	 * 
	 * This method executes within a transaction. If an exception is thrown, all
	 * changes will automatically be rolled back.
	 * 
	 * @param db
	 *            - The database.
	 * @param oldVersion
	 *            - The old database version.
	 * @param newVersion
	 *            - The new database version.
	 */
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
