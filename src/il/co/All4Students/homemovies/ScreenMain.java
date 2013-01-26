package il.co.All4Students.homemovies;

import static il.co.All4Students.homemovies.app.AppConstants.INTENT_TARGET;
import static il.co.All4Students.homemovies.app.AppConstants.Item_Add_Local;
import static il.co.All4Students.homemovies.app.AppConstants.Item_App_Settings;
import static il.co.All4Students.homemovies.app.AppConstants.Item_Edit;
import static il.co.All4Students.homemovies.app.AppConstants.Item_Search_Web;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_SCREEN_MAIN;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_CANCEL;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_COMMIT;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_DELETE;
import static il.co.All4Students.homemovies.app.AppConstants.SortByID;
import static il.co.All4Students.homemovies.app.AppConstants.SortByRTID;
import static il.co.All4Students.homemovies.app.AppConstants.SortByRank;
import static il.co.All4Students.homemovies.app.AppConstants.SortBySubject;
import il.co.All4Students.homemovies.app.ApplicationPreference;
import il.co.All4Students.homemovies.core.Item;
import il.co.All4Students.homemovies.core.ItemCompareRTID;
import il.co.All4Students.homemovies.core.ItemCompareRank;
import il.co.All4Students.homemovies.core.ItemCompareSubject;
import il.co.All4Students.homemovies.dbUtil.ItemsHandler;
import il.co.All4Students.homemovies.uiUtil.ItemListAdapter;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.Toast;

/**
 * Main Activity of the App handels the IO to the DB
 * 
 * @author Arkadi Yoskovitz
 * 
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
		Log.d(LOG_TAG_SCREEN_MAIN, "Screen Main Layout was Created and loaded");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(LOG_TAG_SCREEN_MAIN, "Activity Main Layout was Resumed");
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
				Log.d(LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Edit - CANCEL");
				break;

			case RESULT_CODE_DELETE:
				Log.d(LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Edit - DELETE");
				mReturnedItem = data.getExtras().getParcelable(INTENT_TARGET);
				itemHandler.deleteItem(mReturnedItem);
				mItemList.remove(mReturnedItem);
				break;

			case RESULT_CODE_COMMIT:
				Log.d(LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Edit - COMMIT");
				mReturnedItem = data.getExtras().getParcelable(INTENT_TARGET);
				itemHandler.updateItem(mReturnedItem);
				lastItem = itemHandler.getLastItemId();
				mReturnedItem = itemHandler.getItem(lastItem);
				mItemList.add(mReturnedItem);
				break;

			default:
				Log.d(LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Edit - default");
				break;
			}
			break;
		// Handaling the return of the Add Local button
		case Item_Add_Local:
			switch (resultCode) {

			case RESULT_CODE_CANCEL:
				Log.d(LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Add_Local - CANCEL");
				break;

			case RESULT_CODE_DELETE:
				Log.d(LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Add_Local - Delete");
				Log.d(LOG_TAG_SCREEN_MAIN,
						"No item was added, just log for now");
				break;

			case RESULT_CODE_COMMIT:
				Log.d(LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Add_Local - COMMIT");
				mReturnedItem = data.getExtras().getParcelable(INTENT_TARGET);
				itemHandler.addItem(mReturnedItem);
				lastItem = itemHandler.getLastItemId();
				mReturnedItem = itemHandler.getItem(lastItem);
				mItemList.add(mReturnedItem);
				break;

			default:
				Log.d(LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Add_Local - default");
				break;
			}
			break;
		// Handaling the return of the Add From Web button
		case Item_Search_Web:
			switch (resultCode) {

			case RESULT_CODE_CANCEL:
				Log.d(LOG_TAG_SCREEN_MAIN,
						"onActivityResult - Item_Search_Web - CANCEL");
				break;

			default:
				Log.d(LOG_TAG_SCREEN_MAIN,
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
			Log.d(LOG_TAG_SCREEN_MAIN, "onActivityResult - default - default");
			break;
		}

		// Handaling the screen refresh
		finish();
		startActivity(getIntent());
		Log.d(LOG_TAG_SCREEN_MAIN, "View re-loaded");

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
		Log.d(LOG_TAG_SCREEN_MAIN,
				"Activity Main Option Menue Layout was Created and loaded");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mListView = (ListView) findViewById(R.id.ScreenMainListView);

		switch (item.getItemId()) {
		case R.id.screenMainOptionMenuDeletAllIteams:
			Log.d(LOG_TAG_SCREEN_MAIN,
					"screenMainOptionMenuDeletAllIteams was pressed");
			ItemsHandler itemHandler = new ItemsHandler(this);
			itemHandler.deleteAllItems();
			mItemList.clear();
			sortCompareable();
			break;

		case R.id.screenMainOptionMenuExitSettings:
			Log.d(LOG_TAG_SCREEN_MAIN, "optionMenuExitSettings was pressed");
			System.exit(0);
			break;

		case R.id.screenMainOptionMenuSearch:
			mTableRow = (TableRow) findViewById(R.id.ScreenMainTableRow2);
			if (mTableRow.getVisibility() == View.GONE) {
				mTableRow.setVisibility(View.VISIBLE);
				mListView.setTextFilterEnabled(true);
				final EditText ScreenMainEditText = (EditText) findViewById(R.id.ScreenMainEditText1);
				ScreenMainEditText.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						if (count < before) {
							mAdapter.resetData();
						}
						mAdapter.getFilter().filter(
								ScreenMainEditText.getText().toString());
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
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
		ItemsHandler itemHandler = new ItemsHandler(this);
		switch (item.getItemId()) {

		/*
		 * Send the choosen item to the editing screen
		 */
		case R.id.screenMainContextMenuEdit:
			Log.d(LOG_TAG_SCREEN_MAIN, "screenMainContextMenuEdit was pressed");
			Intent intent = new Intent(ScreenMain.this, ScreenEdit.class);
			intent.putExtra(INTENT_TARGET, mItemList.get((int) info.id));
			startActivityForResult(intent, Item_Edit);
			break;

		/*
		 * Share the choosen item
		 */
		case R.id.screenMainContextMenuShare:
			Log.d(LOG_TAG_SCREEN_MAIN, "screenMainContextMenuShare was pressed");
			ShareDialog shareDialog = new ShareDialog(
					mItemList.get((int) info.id));
			shareDialog.showAlertDialog();
			break;
		/*
		 * Share the choosen item
		 */
		case R.id.screenMainContextMenuRank:
			Log.d(LOG_TAG_SCREEN_MAIN, "screenMainContextMenuRank was pressed");
			RankDialog rankDialog = new RankDialog(mItemList.get((int) info.id));
			rankDialog.showRankDialog();
			break;

		/*
		 * Remove the item from the DB and from the itemList
		 */
		case R.id.screenMainContextMenuDelete:
			Log.d(LOG_TAG_SCREEN_MAIN,
					"screenMainContextMenuDelete was pressed");
			mReturnedItem = mItemList.get((int) info.id);
			itemHandler.deleteItem(mReturnedItem);
			mItemList.remove(mReturnedItem);
			sortCompareable();
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
		Log.d(LOG_TAG_SCREEN_MAIN,
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
		Log.d(LOG_TAG_SCREEN_MAIN, "Activity Main - Add Button was pressed");
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
		Log.d(LOG_TAG_SCREEN_MAIN,
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
				Log.d("Reading: ", "Reading all items..");
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
					Log.d(LOG_TAG_SCREEN_MAIN, logEntry.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void loadScreenMainList() {
		mListView = (ListView) findViewById(R.id.ScreenMainListView);
		sortCompareable();
		mAdapter = new ItemListAdapter(mItemList, ScreenMain.this);
		mListView.setDivider(new ColorDrawable(this.getResources().getColor(
				R.color.Crimson)));
		mListView.setDividerHeight((int) getResources().getDimension(
				R.dimen.Size2dp));
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	private void sortCompareable() {
		int sortMethod = new ApplicationPreference(ScreenMain.this)
				.getSortMethod();
		if (mItemList != null) {
			switch (sortMethod) {
			case SortByID:
				Collections.sort(mItemList);
				break;

			case SortByRank:
				Collections.sort(mItemList, new ItemCompareRank());
				break;

			case SortByRTID:
				Collections.sort(mItemList, new ItemCompareRTID());
				break;

			case SortBySubject:
				Collections.sort(mItemList, new ItemCompareSubject());
				break;

			default:
				break;
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Inner Classes
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * ShareDialog The class handles the alert dialog foe the diffrent Share
	 * options that the App implements
	 * 
	 * Curentlly: - Email
	 * 
	 * in Prograsse: - FaceBook - Tweeter
	 * 
	 * @author Arkadi Yoskovitz
	 */
	private class ShareDialog {
		// Attributes
		private Item mItem;

		// Constractors
		public ShareDialog(Item item) {
			super();
			this.mItem = item;
		}

		// Additional Methods
		public void showAlertDialog() {
			LayoutInflater li = LayoutInflater.from(ScreenMain.this);

			View ShareDialogView = li.inflate(R.layout.custom_dialog_share,
					null);

			AlertDialog.Builder shareDialog = new AlertDialog.Builder(
					ScreenMain.this);
			shareDialog.setView(ShareDialogView);
			shareDialog.setTitle(ScreenMain.this.getResources().getString(
					R.string.ShareDialogTitle));
			shareDialog.setIcon(ScreenMain.this.getResources().getDrawable(
					R.drawable.ic_dialog_share));
			shareDialog.create();
			// Showing Alert Message
			final AlertDialog SDialog = shareDialog.show();

			View btnEmail = ShareDialogView
					.findViewById(R.id.customDialogShareButtonAirMail);
			btnEmail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mSettings = new ApplicationPreference(ScreenMain.this);

					String emailAddress = mSettings.getEmail().toString();

					String uriText = "mailto:" + emailAddress + "?subject="
							+ Uri.encode(mItem.getSubject()) + "&body="
							+ Uri.encode(mItem.getBody()) + "\n\n\n"
							+ Uri.encode(mItem.getUrlWeb());

					Uri uri = Uri.parse(uriText);

					Intent intent = new Intent(Intent.ACTION_SENDTO);
					intent.setData(uri);
					startActivity(Intent.createChooser(intent, "Send email"));
					SDialog.dismiss();
				}
			});

			View btnFaceBook = ShareDialogView
					.findViewById(R.id.customDialogShareButtonFaceBook);
			btnFaceBook.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(ScreenMain.this,
							"FaceBook is unavlible at this time",
							Toast.LENGTH_SHORT).show();
					SDialog.dismiss();
				}
			});

			View btnTweeter = ShareDialogView
					.findViewById(R.id.customDialogShareButtonTweeter);
			btnTweeter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(ScreenMain.this,
							"Tweeter is unavlible at this time",
							Toast.LENGTH_SHORT).show();
					SDialog.dismiss();
				}
			});

		}
	}

	private class RankDialog {
		// Attributes
		private Item mItem;
		private RatingBar mRankBar;

		// Constractors
		public RankDialog(Item item) {
			super();
			this.mItem = item;
		}

		// Additional Methods
		public void showRankDialog() {
			LayoutInflater li = LayoutInflater.from(ScreenMain.this);

			View RankDialogView = li.inflate(R.layout.custom_dialog_rank, null);

			AlertDialog.Builder rankDialog = new AlertDialog.Builder(
					ScreenMain.this);
			rankDialog.setView(RankDialogView);
			rankDialog.setTitle(ScreenMain.this.getResources().getString(
					R.string.RankDialogTitle));
			rankDialog.setTitle(ScreenMain.this.getResources().getString(
					R.string.RankDialogMsg));
			rankDialog.create();
			// Showing Alert Message
			final AlertDialog RDialog = rankDialog.show();

			mRankBar = (RatingBar) RankDialogView
					.findViewById(R.id.customDialogRankBar);
			mRankBar.setRating(((float) mItem.getRank()) / 10);
			View btnCancel = RankDialogView
					.findViewById(R.id.customDialogRankButtonCancel);
			btnCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					RDialog.dismiss();
				}
			});

			View btnCommit = RankDialogView
					.findViewById(R.id.customDialogRankButtonCommit);
			btnCommit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mItem.setRank((int) (mRankBar.getRating() * 10));
					Log.d("logtag", mItem.getRank() + "");
					ItemsHandler itemHandler = new ItemsHandler(ScreenMain.this);
					itemHandler.updateItem(mItem);
					mItemList.clear();
					mItemList = itemHandler.getAllItems();
					ScreenMain.this.mAdapter = new ItemListAdapter(mItemList,
							ScreenMain.this);
					ScreenMain.this.mListView
							.setAdapter(ScreenMain.this.mAdapter);
					ScreenMain.this.mAdapter.notifyDataSetChanged();
					RDialog.dismiss();
				}
			});
		}
	}
}
