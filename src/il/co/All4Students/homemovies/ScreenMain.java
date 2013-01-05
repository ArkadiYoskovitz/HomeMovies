package il.co.All4Students.homemovies;

import static il.co.All4Students.homemovies.app.AppConstants.INTENT_TARGET;
import static il.co.All4Students.homemovies.app.AppConstants.Item_Add_Local;
import static il.co.All4Students.homemovies.app.AppConstants.Item_Edit;
import static il.co.All4Students.homemovies.app.AppConstants.Item_Search_Web;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_MAIN;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_CANCEL;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_COMMIT;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_DELETE;
import il.co.All4Students.homemovies.app.ApplicationPreference;
import il.co.All4Students.homemovies.core.Item;
import il.co.All4Students.homemovies.dbUtil.ItemsHandler;
import il.co.All4Students.homemovies.uiUtil.ColorListAdapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Main Activity of the App handels the IO to the DB
 * 
 * @author Arkadi Yoskovitz
 * 
 */
public class ScreenMain extends Activity implements OnItemClickListener {
	// Attributes
	private ArrayList<Item> mItemList = new ArrayList<Item>();
	private Item mReturnedItem;
	private ListView mListView;
	// private ApplicationPreference mSettings;

	// System Events
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_main);
		Log.d(LOG_TAG_MAIN, "Screen Main Layout was Created and loaded");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(LOG_TAG_MAIN, "Activity Main Layout was Resumed");
		loadDateBase();
		loadScreenMainTest();
		loadScreenMainList();
		registerForContextMenu(mListView);

	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Activity sending info Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		ItemsHandler itemHandler = new ItemsHandler(this);
		int lastItem = 0;
		switch (requestCode) {
		// Handaling the return of the Edit selection
		case Item_Edit:
			switch (resultCode) {
			case RESULT_CODE_CANCEL:
				Log.d(LOG_TAG_MAIN, "onActivityResult - Item_Edit - CANCEL");
				break;

			case RESULT_CODE_DELETE:
				Log.d(LOG_TAG_MAIN, "onActivityResult - Item_Edit - DELETE");
				mReturnedItem = data.getExtras().getParcelable(INTENT_TARGET);
				itemHandler.deleteItem(mReturnedItem);
				mItemList.remove(mReturnedItem);
				break;

			case RESULT_CODE_COMMIT:
				Log.d(LOG_TAG_MAIN, "onActivityResult - Item_Edit - COMMIT");
				mReturnedItem = data.getExtras().getParcelable(INTENT_TARGET);
				itemHandler.updateItem(mReturnedItem);
				lastItem = itemHandler.getLastItemId();
				mReturnedItem = itemHandler.getItem(lastItem);
				mItemList.add(mReturnedItem);
				break;

			default:
				Log.d(LOG_TAG_MAIN, "onActivityResult - Item_Edit - default");
				break;
			}
			break;
		// Handaling the return of the Add button
		case Item_Add_Local:
			switch (resultCode) {

			case RESULT_CODE_CANCEL:
				Log.d(LOG_TAG_MAIN,
						"onActivityResult - Item_Add_Local - CANCEL");
				break;

			case RESULT_CODE_DELETE:
				Log.d(LOG_TAG_MAIN,
						"onActivityResult - Item_Add_Local - Delete");
				Log.d(LOG_TAG_MAIN, "No item was added, just log for now");
				break;

			case RESULT_CODE_COMMIT:
				Log.d(LOG_TAG_MAIN,
						"onActivityResult - Item_Add_Local - COMMIT");
				mReturnedItem = data.getExtras().getParcelable(INTENT_TARGET);
				// mReturnedItem = fixItemNumber(mReturnedItem);
				itemHandler.addItem(mReturnedItem);
				lastItem = itemHandler.getLastItemId();
				mReturnedItem = itemHandler.getItem(lastItem);
				mItemList.add(mReturnedItem);
				break;

			default:
				Log.d(LOG_TAG_MAIN,
						"onActivityResult - Item_Add_Local - default");
				break;
			}
			break;
		// ////////////////////////////////////////////////////////////////////////////////
		case Item_Search_Web:
			switch (resultCode) {

			case RESULT_CODE_CANCEL:
				Log.d(LOG_TAG_MAIN,
						"onActivityResult - Item_Search_Web - CANCEL");
				break;

			default:
				Log.d(LOG_TAG_MAIN,
						"onActivityResult - Item_Search_Web - default");
				break;
			}
			break;
		// ////////////////////////////////////////////////////////////////////////////////
		default:
			Log.d(LOG_TAG_MAIN, "onActivityResult - default - default");
			break;
		}

		// Handaling the screen refresh
		ColorListAdapter adapter = new ColorListAdapter(this, mItemList);
		adapter.notifyDataSetChanged();
		Log.d(LOG_TAG_MAIN, "View re-loaded");

		super.onActivityResult(requestCode, resultCode, data);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Menu Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * onCreateOptionsMenu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.screen_main, menu);
		Log.d(LOG_TAG_MAIN,
				"Activity Main Option Menue Layout was Created and loaded");
		return true;
	}

	/**
	 * onCreateContextMenu
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	/**
	 * onContextItemSelected
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		mListView = (ListView) findViewById(R.id.ScreenMainListView);
		ItemsHandler itemHandler = new ItemsHandler(this);
		switch (item.getItemId()) {

		/*
		 * Send the choosen item to the editing screen
		 */
		case R.id.contextMenuEdit:
			Log.d(LOG_TAG_MAIN, "contextMenuEdit was pressed");
			Intent intent = new Intent(ScreenMain.this, ScreenEdit.class);
			intent.putExtra(INTENT_TARGET, mItemList.get((int) info.id));
			startActivityForResult(intent, Item_Edit);
			break;

		/*
		 * Remove the item from the DB and from the itemList
		 */
		case R.id.contextMenuDelete:
			Log.d(LOG_TAG_MAIN, "contextMenuDelete was pressed");
			mReturnedItem = mItemList.get((int) info.id);
			itemHandler.deleteItem(mReturnedItem);
			mItemList.remove(mReturnedItem);

			ColorListAdapter adapter = new ColorListAdapter(this, mItemList);
			mListView.setAdapter(adapter);
			break;

		/*
		 * Call the item selected, get the ID, ubdate the color and return to
		 * the DB, update the itemList
		 */
		case R.id.contextMenuColorSettings:
			Log.d(LOG_TAG_MAIN, "contextMenuColorSettings was pressed");
			mReturnedItem = mItemList.get((int) info.id);
			break;

		case R.id.SubContextMenuColorRed:
			Log.d(LOG_TAG_MAIN, "SubContextMenuColorRed was pressed");
			mReturnedItem.setColor(getResources().getColor(R.color.Red));
			itemHandler.updateItem(mReturnedItem);
			Log.d(LOG_TAG_MAIN, "finised setting the color "
					+ itemHandler.getItem(mReturnedItem.get_id()).toStringTwo());
			break;

		case R.id.SubContextMenuColorGreen:
			Log.d(LOG_TAG_MAIN, "SubContextMenuColorGreen was pressed");
			mReturnedItem.setColor(getResources().getColor(R.color.Green));
			itemHandler.updateItem(mReturnedItem);
			Log.d(LOG_TAG_MAIN, "finised setting the color "
					+ itemHandler.getItem(mReturnedItem.get_id()).toStringTwo());
			break;

		case R.id.SubContextMenuColorYellow:
			Log.d(LOG_TAG_MAIN, "SubContextMenuColorYellow was pressed");
			mReturnedItem.setColor(getResources().getColor(R.color.Yellow));
			itemHandler.updateItem(mReturnedItem);
			Log.d(LOG_TAG_MAIN, "finised setting the color "
					+ itemHandler.getItem(mReturnedItem.get_id()).toStringTwo());
			break;

		case R.id.SubContextMenuColorBlue:
			Log.d(LOG_TAG_MAIN, "SubContextMenuColorBlue was pressed");
			mReturnedItem.setColor(getResources().getColor(R.color.Blue));
			itemHandler.updateItem(mReturnedItem);
			Log.d(LOG_TAG_MAIN, "finised setting the color "
					+ itemHandler.getItem(mReturnedItem.get_id()).toStringTwo());
			break;

		case R.id.SubContextMenuColorDefault:
			Log.d(LOG_TAG_MAIN, "SubContextMenuColorDefault was pressed");
			mReturnedItem.setColor(ApplicationPreference.getDefaultColor());
			itemHandler.updateItem(mReturnedItem);
			break;

		default:
			break;
		}

		ColorListAdapter adapter = new ColorListAdapter(this, mItemList);
		mListView.setAdapter(adapter);
		Log.d(LOG_TAG_MAIN, "finishe with the layout but still working on it");

		return super.onContextItemSelected(item);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// OnClick Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * onClickSettings handels the onClick event for the Settings Button, for
	 * all the android devices that don't have a physical button, although it's
	 * useable on devices with a button as well
	 * 
	 * @param view
	 */
	public void onClickSettings(View view) {
		Log.d(LOG_TAG_MAIN, "Activity Main - Settings Button was pressed");
		openOptionsMenu();
	}

	/**
	 * onClickAdd handels the onClick event for the Add Button, meaning it sends
	 * a command to open a new screen and that's about it
	 * 
	 * @param view
	 */
	public void onClickMainAdd(View view) {
		Log.d(LOG_TAG_MAIN, "Activity Main - Add Button was pressed");
		AlertDialog.Builder AddingMethod = new AlertDialog.Builder(this);
		AddingMethod.setMessage(getString(R.string.AddingDialogMsg));
		AddingMethod.setCancelable(true);
		AddingMethod.setNegativeButton(R.string.AddingDialogCancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		AddingMethod.setNeutralButton(R.string.AddingDialogWeb,
				new DialogInterface.OnClickListener() {
					// SENDIJNG THE COMMEND TO OPEN THE WEB SEARCH VIEW
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(ScreenMain.this,
								ScreenWeb.class);
						startActivityForResult(intent, Item_Search_Web);
						dialog.dismiss();
					}
				});
		AddingMethod.setPositiveButton(R.string.AddingDialogLocal,
				new DialogInterface.OnClickListener() {
					// SENDIJNG THE COMMEND TO OPEN THE ADD ITEM LOCOLY
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(ScreenMain.this,
								ScreenEdit.class);
						intent.putExtra(INTENT_TARGET, new Item());
						startActivityForResult(intent, Item_Add_Local);
						dialog.dismiss();
					}
				});
		AddingMethod.create().show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.d(LOG_TAG_MAIN, "Activity Main - An Item was clicked from the list");
		Intent intent = new Intent(ScreenMain.this, ScreenEdit.class);
		intent.putExtra(INTENT_TARGET, mItemList.get(position));
		startActivityForResult(intent, Item_Edit);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Additional Methods
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * loadScreenMainTest tests the system status, and puts up a notice if the
	 * db is empty
	 */
	private void loadScreenMainTest() {
		if (mItemList.isEmpty()) {
			AlertDialog.Builder SNotice = new AlertDialog.Builder(this);
			SNotice.setTitle(getString(R.string.systemNoticeTitle));
			SNotice.setIcon(R.drawable.ic_dialog_notice);
			SNotice.setMessage(getString(R.string.systemNoticeMsg));
			SNotice.setCancelable(true);
			SNotice.setNeutralButton(R.string.systemNoticeButton,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			SNotice.create().show();
		}
	}

	/**
	 * loadDateBase
	 */
	private void loadDateBase() {
		if (mItemList.isEmpty()) {
			try {
				ItemsHandler itemHandler = new ItemsHandler(this);
				Log.d("Reading: ", "Reading all items..");
				mItemList = itemHandler.getAllItems();

				for (Item cn : mItemList) {
					StringBuilder logEntry = new StringBuilder().append("Id: ")
							.append(cn.get_id()).append(" ,Subject: ")
							.append(cn.getSubject()).append(" ,Body: ")
							.append(cn.getBody()).append(" ,UrlLocal: ")
							.append(cn.getUrlLocal()).append(" ,UrlWeb: ")
							.append(cn.getUrlWeb()).append(" ,Rt_ID: ")
							.append(cn.getRt_ID()).append(" ,Color: ")
							.append(cn.getColor());
					Log.d(LOG_TAG_MAIN, logEntry.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * loadScreenMainList
	 */
	private void loadScreenMainList() {
		mListView = (ListView) findViewById(R.id.ScreenMainListView);
		ColorListAdapter adapter = new ColorListAdapter(this, mItemList);
		mListView.setDivider(new ColorDrawable(this.getResources().getColor(
				R.color.Crimson)));
		mListView.setDividerHeight((int) getResources().getDimension(
				R.dimen.Size2dp));
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
	}

}
