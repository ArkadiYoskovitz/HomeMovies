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
import static il.co.All4Students.homemovies.util.db.ItemsDbConstants.SELECT_HELPER;
import static il.co.All4Students.homemovies.util.db.ItemsDbConstants.TABLE_ITEMS;
import static il.co.All4Students.homemovies.util.db.ItemsDbConstants.WHERE_SUBJECT;
import il.co.All4Students.homemovies.core.Item;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * DbHandler
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
public class ItemsHandler {
	private ItemsDbHelper dbhelper;

	public ItemsHandler(Context context) {
		dbhelper = new ItemsDbHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	/**
	 * Adding a new item to the SQLiteDatabase
	 * 
	 * @param item
	 */
	public void addItem(Item item) {
		SQLiteDatabase db = dbhelper.getWritableDatabase();

		try {
			ContentValues values = new ContentValues();
			values.put(KEY_SUBJECT, item.getSubject());
			values.put(KEY_BODY, item.getBody());
			values.put(KEY_URLWEB, item.getUrlWeb());
			values.put(KEY_URLLOCAL, item.getUrlLocal());
			values.put(KEY_RT_ID, item.getRt_ID());
			values.put(KEY_RANK, item.getRank());
			values.put(KEY_VIEWD, item.getIntViewd());
			values.put(KEY_COLOR, item.getColor());

			// Inserting Row
			db.insert(TABLE_ITEMS, null, values);
		} catch (SQLException e) {
			Log.d(LOG_TAG, "Add Item: " + e.getMessage());
		} finally {
			db.close(); // Closing database connection
		}
	}

	/**
	 * Getting single item from SQLiteDatabase using the item ID
	 * 
	 * @param id
	 * @return item
	 */
	public Item getItem(int id) {
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Item item = null;
		try {
			Cursor cursor = db
					.query(TABLE_ITEMS, new String[] { KEY_ID, KEY_SUBJECT,
							KEY_BODY, KEY_URLWEB, KEY_URLLOCAL, KEY_RT_ID,
							KEY_RANK, KEY_VIEWD, KEY_COLOR }, KEY_ID + "=?",
							new String[] { String.valueOf(id) }, null, null,
							null, null);
			if (cursor != null)
				cursor.moveToFirst();

			item = new Item(Integer.parseInt(cursor.getString(0)),
					cursor.getString(1), cursor.getString(2),
					cursor.getString(3), cursor.getString(4),
					Integer.parseInt(cursor.getString(5)),
					Integer.parseInt(cursor.getString(6)),
					(cursor.getInt(7) == 1), Integer.parseInt(cursor
							.getString(8)));

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
	public ArrayList<Item> getAllItems() {
		ArrayList<Item> itemList = new ArrayList<Item>();
		// Select All Query
		String selectQuery = SELECT_HELPER;

		SQLiteDatabase db = dbhelper.getReadableDatabase();
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
					item.setRank(Integer.parseInt(cursor.getString(6)));
					item.setViewd(cursor.getInt(7) == 1);
					item.setColor(Integer.parseInt(cursor.getString(8)));
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
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		int numAffected = 0;
		try {
			ContentValues values = new ContentValues();
			values.put(KEY_SUBJECT, item.getSubject());
			values.put(KEY_BODY, item.getBody());
			values.put(KEY_URLWEB, item.getUrlWeb());
			values.put(KEY_URLLOCAL, item.getUrlLocal());
			values.put(KEY_RT_ID, item.getRt_ID());
			values.put(KEY_RANK, item.getRank());
			values.put(KEY_VIEWD, item.getIntViewd());
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
		SQLiteDatabase db = dbhelper.getWritableDatabase();
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
		SQLiteDatabase db = dbhelper.getWritableDatabase();
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
		SQLiteDatabase db = dbhelper.getReadableDatabase();
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

	/**
	 * Getting the Id of the last object to be stored in the SQLiteDatabase
	 * 
	 * @return lastItemID the number of items currently found in the
	 */
	public int getLastItemId() {
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		int lastItemID = 0;
		Cursor cursor = null;

		try {
			cursor = db.rawQuery(SELECT_HELPER, null);
			cursor.moveToFirst();
			do {
				lastItemID = cursor.getInt(0);
			} while (cursor.moveToNext());
		} catch (SQLException e) {
			Log.d(LOG_TAG, "Items Count: " + e.getMessage());
		} finally {
			cursor.close();
			db.close();
		}
		return lastItemID;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public ArrayList<Item> getAllLikeItems(String searchString) {

		ArrayList<Item> itemList = new ArrayList<Item>();
		Cursor cursor = null;

		SQLiteDatabase db = dbhelper.getReadableDatabase();
		try {

			cursor = db.query(TABLE_ITEMS, null, WHERE_SUBJECT,
					new String[] { searchString + "%" }, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					Item item = new Item();
					item.set_id(Integer.parseInt(cursor.getString(0)));
					item.setSubject(cursor.getString(1));
					item.setBody(cursor.getString(2));
					item.setUrlWeb(cursor.getString(3));
					item.setUrlLocal(cursor.getString(4));
					item.setRt_ID(Integer.parseInt(cursor.getString(5)));
					item.setRank(Integer.parseInt(cursor.getString(6)));
					item.setViewd((cursor.getInt(7) == 1));
					item.setColor(Integer.parseInt(cursor.getString(8)));
					itemList.add(item);
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (SQLException e) {
			Log.d(LOG_TAG, "Get All Like Items: " + e.getMessage());
		} finally {
			db.close();
		}

		return itemList;
	}
}