package il.co.All4Students.homemovies;

import static il.co.All4Students.homemovies.app.AppConstants.DeveloperTeam;
import static il.co.All4Students.homemovies.app.AppConstants.INTENT_TARGET;
import static il.co.All4Students.homemovies.app.AppConstants.Item_Add_Local;
import static il.co.All4Students.homemovies.app.AppConstants.Item_App_Settings;
import static il.co.All4Students.homemovies.app.AppConstants.Item_Edit;
import static il.co.All4Students.homemovies.app.AppConstants.Item_Search_Web;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_SCREEN_MAIN;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_CANCEL;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_COMMIT;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_DELETE;
import il.co.All4Students.homemovies.app.ApplicationPreference;
import il.co.All4Students.homemovies.core.Item;
import il.co.All4Students.homemovies.util.adapter.ItemListAdapter;
import il.co.All4Students.homemovies.util.app.AppUtil;
import il.co.All4Students.homemovies.util.db.ItemsHandler;
import il.co.All4Students.homemovies.util.dialog.RankDialog;
import il.co.All4Students.homemovies.util.dialog.ShareDialog;
import il.co.All4Students.homemovies.util.email.EmailUtil;
import il.co.All4Students.homemovies.util.log.util.AppLog;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.Toast;

/**
 * Main screen
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
public class ScreenMain extends Activity implements OnItemClickListener,
		OnSharedPreferenceChangeListener {
	// Attributes
	private ArrayList<Item> mItemList = new ArrayList<Item>();
	private Item mReturnedItem;
	private ListView mListView;
	private TableRow mTableRow;
	private ItemListAdapter mAdapter;

	private ApplicationPreference mSettings;

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// System Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_main);
		AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
				"Screen Main Layout was Created and loaded");
	}

	@Override
	protected void onResume() {
		super.onResume();
		AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
				"Activity Main Layout was Resumed");
		loadDateBase();
		loadScreenMainTest();
		loadScreenMainList();
		registerForContextMenu(mListView);

	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList("Main mItemList", mItemList);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mItemList = savedInstanceState.getParcelableArrayList("Main mItemList");
		loadScreenMainList();
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
				AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Edit - CANCEL");
				break;

			case RESULT_CODE_DELETE:
				AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Edit - DELETE");
				mReturnedItem = data.getExtras().getParcelable(INTENT_TARGET);
				itemHandler.deleteItem(mReturnedItem);
				mItemList.remove(mReturnedItem);
				break;

			case RESULT_CODE_COMMIT:
				AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Edit - COMMIT");
				mReturnedItem = data.getExtras().getParcelable(INTENT_TARGET);
				itemHandler.updateItem(mReturnedItem);
				lastItem = itemHandler.getLastItemId();
				mReturnedItem = itemHandler.getItem(lastItem);
				mItemList.add(mReturnedItem);
				break;

			default:
				AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Edit - default");
				break;
			}
			break;
		// Handaling the return of the Add Local button
		case Item_Add_Local:
			switch (resultCode) {

			case RESULT_CODE_CANCEL:
				AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Add_Local - CANCEL");
				break;

			case RESULT_CODE_DELETE:
				AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Add_Local - Delete");
				AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
						"No item was added, just log for now");
				break;

			case RESULT_CODE_COMMIT:
				AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Add_Local - COMMIT");
				mReturnedItem = data.getExtras().getParcelable(INTENT_TARGET);
				itemHandler.addItem(mReturnedItem);
				lastItem = itemHandler.getLastItemId();
				mReturnedItem = itemHandler.getItem(lastItem);
				mItemList.add(mReturnedItem);
				break;

			default:
				AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Add_Local - default");
				break;
			}
			break;
		// Handaling the return of the Add From Web button
		case Item_Search_Web:
			switch (resultCode) {

			case RESULT_CODE_CANCEL:
				AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Search_Web - CANCEL");
				break;

			default:
				AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Search_Web - default");
				break;
			}
			break;
		// Handaling the return of the Add From Web button
		case Item_App_Settings:
			switch (resultCode) {

			case RESULT_CODE_CANCEL:
				loadScreenMainList();
				break;

			default:
				break;
			}
			break;
		// ////////////////////////////////////////////////////////////////////////////////
		default:
			AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
					"onActivityResult - default - default");
			break;
		}

		// Handaling the screen refresh
		finish();
		startActivity(getIntent());
		AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN, "View re-loaded");

		super.onActivityResult(requestCode, resultCode, data);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Menu Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * onCreateOptionsMenu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.screen_main_menu_option, menu);
		AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
				"Activity Main Option Menue Layout was Created and loaded");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mListView = (ListView) findViewById(R.id.ScreenMainListView);
		mSettings = new ApplicationPreference(ScreenMain.this);

		switch (item.getItemId()) {
		case R.id.screenMainOptionMenuDeletAllIteams:
			AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
					"screenMainOptionMenuDeletAllIteams was pressed");
			ItemsHandler itemHandler = new ItemsHandler(this);
			itemHandler.deleteAllItems();
			mItemList.clear();
			AppUtil.sortCompareable(ScreenMain.this, mItemList);
			break;

		case R.id.screenMainOptionMenuExitSettings:
			AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
					"optionMenuExitSettings was pressed");
			System.exit(0);
			break;

		case R.id.screenMainOptionMenuSendLog:
			if (mSettings.getEnableLog()) {
				EmailUtil.sendEmail(ScreenMain.this, DeveloperTeam,
						mSettings.getEmail(), "Bug Report",
						AppLog.getAllLogs(ScreenMain.this), null);
			} else {
				Toast.makeText(ScreenMain.this, "Log is not enabled",
						Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.screenMainOptionMenuSearch:
			mTableRow = (TableRow) findViewById(R.id.ScreenMainTableRow2);
			if (mTableRow.getVisibility() == View.GONE) {
				mTableRow.setVisibility(View.VISIBLE);
				mListView.setTextFilterEnabled(true);
				final EditText ScreenMainEditText = (EditText) findViewById(R.id.ScreenMainEditText1);
				ScreenMainEditText.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence cs, int start,
							int before, int count) {
						if (count < before) {
							ScreenMain.this.mAdapter.resetData();
						}
						ScreenMain.this.mAdapter.getFilter().filter(cs);
						ScreenMain.this.mAdapter.notifyDataSetChanged();
					}

					@Override
					public void beforeTextChanged(CharSequence cs, int start,
							int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
					}
				});
			} else {
				mTableRow.setVisibility(View.GONE);
			}
			break;

		case R.id.screenMainOptionMenuSettings:
			Intent intent = new Intent(ScreenMain.this, ScreenPreferences.class);
			startActivityForResult(intent, Item_App_Settings);
			break;

		default:
			break;
		}
		mAdapter.notifyDataSetChanged();

		return super.onOptionsItemSelected(item);
	}

	/**
	 * onCreateContextMenu
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.screen_main_menu_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		mListView = (ListView) findViewById(R.id.ScreenMainListView);
		mSettings = new ApplicationPreference(ScreenMain.this);
		ItemsHandler itemHandler = new ItemsHandler(this);

		switch (item.getItemId()) {

		/*
		 * Send the choosen item to the editing screen
		 */
		case R.id.screenMainContextMenuEdit:
			AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
					"screenMainContextMenuEdit was pressed");

			Intent intent = new Intent(ScreenMain.this, ScreenEdit.class);
			intent.putExtra(INTENT_TARGET, mItemList.get((int) info.id));
			startActivityForResult(intent, Item_Edit);
			break;

		/*
		 * Share the choosen item
		 */
		case R.id.screenMainContextMenuShare:
			AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
					"screenMainContextMenuShare was pressed");

			ShareDialog shareDialog = new ShareDialog(
					mItemList.get((int) info.id), ScreenMain.this);
			shareDialog.showShareDialog();
			break;
		/*
		 * Share the choosen item
		 */
		case R.id.screenMainContextMenuRank:
			AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
					"screenMainContextMenuRank was pressed");
			RankDialog rankDialog = new RankDialog(
					mItemList.get((int) info.id), ScreenMain.this);
			rankDialog.showRankDialog();
			break;

		/*
		 * Remove the item from the DB and from the itemList
		 */
		case R.id.screenMainContextMenuDelete:
			AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
					"screenMainContextMenuDelete was pressed");
			mReturnedItem = mItemList.get((int) info.id);
			itemHandler.deleteItem(mReturnedItem);
			mItemList.remove(mReturnedItem);
			AppUtil.sortCompareable(ScreenMain.this, mItemList);
			break;

		default:
			break;
		}

		mAdapter.notifyDataSetChanged();

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
		AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
				"Activity Main - Settings Button was pressed");
		openOptionsMenu();
	}

	/**
	 * onClickAdd handels the onClick event for the Add Button, meaning it sends
	 * a command to open a new screen and that's about it
	 * 
	 * @param view
	 */
	public void onClickMainAdd(View view) {
		AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
				"Activity Main - Add Button was pressed");
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
		AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
				"Activity Main - An Item was clicked from the list");
		Intent intent = new Intent(ScreenMain.this, ScreenEdit.class);
		intent.putExtra(INTENT_TARGET, mItemList.get(position));
		startActivityForResult(intent, Item_Edit);
	}

	// /////
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		loadScreenMainList();
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

	private void loadDateBase() {
		if (mItemList.isEmpty()) {
			try {
				ItemsHandler itemHandler = new ItemsHandler(this);
				AppLog.log(ScreenMain.this, "Reading: ", "Reading all items..");
				mItemList = itemHandler.getAllItems();

				for (Item cn : mItemList) {
					StringBuilder logEntry = new StringBuilder().append("Id: ")
							.append(cn.get_id()).append(" ,Subject: ")
							.append(cn.getSubject()).append(" ,Body: ")
							.append(cn.getBody()).append(" ,UrlLocal: ")
							.append(cn.getUrlLocal()).append(" ,UrlWeb: ")
							.append(cn.getUrlWeb()).append(" ,Rt_ID: ")
							.append(cn.getRt_ID()).append(" ,Viewed: ")
							.append(cn.getViewd()).append(" ,Color: ")
							.append(cn.getColor()).append("\n\n");
					AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN,
							logEntry.toString());
				}
			} catch (Exception e) {
				AppLog.log(ScreenMain.this, LOG_TAG_SCREEN_MAIN, e.getMessage()
						.toString());
			}
		}
	}

	private void loadScreenMainList() {
		mListView = (ListView) findViewById(R.id.ScreenMainListView);
		AppUtil.sortCompareable(ScreenMain.this, mItemList);
		mAdapter = new ItemListAdapter(mItemList, ScreenMain.this);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		mListView.setOnItemClickListener(this);
	}

	public void refreshList() {
		ItemsHandler itemHandler = new ItemsHandler(ScreenMain.this);
		mItemList.clear();
		mItemList = itemHandler.getAllItems();
		ScreenMain.this.mAdapter = new ItemListAdapter(mItemList,
				ScreenMain.this);
		ScreenMain.this.mListView.setAdapter(ScreenMain.this.mAdapter);
		ScreenMain.this.mAdapter.notifyDataSetChanged();
	}
}
