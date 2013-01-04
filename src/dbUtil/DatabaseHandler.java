package dbUtil;

import il.co.All4Students.homemovies.core.Item;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	// Start DbConstants
	// ////////////////////////////////////////////////////////////////////////////////////////////////////

	// All Static variables

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
	public static final String KEY_COLOR = "color";

	// Helper static fields
	public static final String LOG_TAG = "MovieDb";
	public static final String SELECT_HELPER = "SELECT  * FROM " + TABLE_ITEMS;
	public static final String WHERE_SUBJECT = "subject like ?";

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	// Start DbHelper
	// ////////////////////////////////////////////////////////////////////////////////////////////////////

	// Constractor
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	// Start DbHandler
	// ////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	/**
	 * Adding a new item to the SQLiteDatabase
	 * 
	 * @param item
	 */
	public void addItem(Item item) {
		SQLiteDatabase db = this.getWritableDatabase();

		try {
			ContentValues values = new ContentValues();
			values.put(KEY_SUBJECT, item.getSubject());
			values.put(KEY_BODY, item.getBody());
			values.put(KEY_URLWEB, item.getUrlWeb());
			values.put(KEY_URLLOCAL, item.getUrlLocal());
			values.put(KEY_RT_ID, item.getRt_ID());
			values.put(KEY_COLOR, item.getColor());

			db.insert(TABLE_ITEMS, null, values);
		} catch (SQLException e) {
			Log.d(LOG_TAG, "Add Item: " + e.getMessage());
		} finally {
			db.close();
		}
	}

	/**
	 * Getting single item from SQLiteDatabase using the item ID
	 * 
	 * @param id
	 * @return item
	 */
	public Item getItem(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Item item = null;
		try {
			Cursor cursor = db
					.query(TABLE_ITEMS, new String[] { KEY_ID, KEY_SUBJECT,
							KEY_BODY, KEY_URLWEB, KEY_URLLOCAL, KEY_RT_ID,
							KEY_COLOR }, KEY_ID + "=?",
							new String[] { String.valueOf(id) }, null, null,
							null, null);
			if (cursor != null)
				cursor.moveToFirst();

			item = new Item(Integer.parseInt(cursor.getString(0)),
					cursor.getString(1), cursor.getString(2),
					cursor.getString(3), cursor.getString(4),
					Integer.parseInt(cursor.getString(5)),
					Integer.parseInt(cursor.getString(6)));

			cursor.close();
		} catch (SQLException e) {
			Log.d(LOG_TAG, "Get Item: " + e.getMessage());
		} finally {
			db.close();
		}

		return item;
	}

	/**
	 * Getting All item
	 * 
	 * method retrives all the items currantly stored in the SQLiteDatabase and
	 * places them into a List
	 * 
	 * @return itemList
	 */
	public List<Item> getAllItems() {
		List<Item> itemList = new ArrayList<Item>();
		// Select All Query
		String selectQuery = SELECT_HELPER;

		SQLiteDatabase db = this.getWritableDatabase();
		try {
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Item item = new Item();
					item.set_id(Integer.parseInt(cursor.getString(0)));
					item.setSubject(cursor.getString(1));
					item.setBody(cursor.getString(2));
					item.setUrlWeb(cursor.getString(3));
					item.setUrlLocal(cursor.getString(4));
					item.setRt_ID(Integer.parseInt(cursor.getString(5)));
					item.setColor(Integer.parseInt(cursor.getString(6)));
					// Adding item to list
					itemList.add(item);
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (SQLException e) {
			Log.d(LOG_TAG, "Get All Items: " + e.getMessage());
		} finally {
			db.close();
		}

		return itemList;
	}

	/**
	 * Updating single item
	 * 
	 * @param item
	 *            the object send to be updated in the Database
	 * @return numAffected the number of rows affected by the operation
	 */
	public int updateItem(Item item) {
		SQLiteDatabase db = this.getWritableDatabase();
		int numAffected = 0;
		try {
			ContentValues values = new ContentValues();
			values.put(KEY_SUBJECT, item.getSubject());
			values.put(KEY_BODY, item.getBody());
			values.put(KEY_URLWEB, item.getUrlWeb());
			values.put(KEY_URLLOCAL, item.getUrlLocal());
			values.put(KEY_RT_ID, item.getRt_ID());
			values.put(KEY_COLOR, item.getColor());

			// updating row
			numAffected = db.update(TABLE_ITEMS, values, KEY_ID + " = ?",
					new String[] { String.valueOf(item.get_id()) });

		} catch (SQLException e) {
			Log.d(LOG_TAG, "Update Item: " + e.getMessage());
		} finally {
			db.close();
		}
		return numAffected;
	}

	/**
	 * Deleting single item
	 * 
	 * @param item
	 *            the object to be deleted from the Database
	 */
	public void deleteItem(Item item) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			db.delete(TABLE_ITEMS, KEY_ID + " = ?",
					new String[] { String.valueOf(item.get_id()) });
		} catch (SQLException e) {
			Log.d(LOG_TAG, "Delete Item: " + e.getMessage());
		} finally {
			db.close();
		}
	}

	/**
	 * Deleting all items from the Database
	 */
	public void deleteAllItems() {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			String sqlDelete = "DELETE FROM " + TABLE_ITEMS;
			db.execSQL(sqlDelete);
		} catch (Exception e) {
			Log.d(LOG_TAG, "Delete Item: " + e.getMessage());
		} finally {
			db.close();
		}
	}

	/**
	 * Getting items Count
	 * 
	 * @return itemsCount the number of items currently found in the
	 *         SQLiteDatabase
	 */
	public int getItemsCount() {
		SQLiteDatabase db = this.getReadableDatabase();
		int itemsCount = 0;
		try {
			String countQuery = SELECT_HELPER + TABLE_ITEMS;
			Cursor cursor = db.rawQuery(countQuery, null);
			itemsCount = cursor.getCount();
			cursor.close();
		} catch (SQLException e) {
			Log.d(LOG_TAG, "Items Count: " + e.getMessage());
		} finally {
			db.close();
		}

		// return count
		return itemsCount;
	}
}
