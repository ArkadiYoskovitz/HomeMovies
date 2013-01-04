package il.co.All4Students.homemovies;

import il.co.All4Students.homemovies.app.ApplicationPreference;
import il.co.All4Students.homemovies.core.Item;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

/**
 * Edit screen
 * 
 * @author Arkadi Yoskovitz
 * 
 */
public class ScreenEdit extends Activity {
	// Attributes
	private Item mEditedItem;
	private ApplicationPreference mSettings;

	// System Events
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		super.onActivityResult(requestCode, resultCode, data);
	}

	// Menu Events

	// OnClick Events
	public void onClickEditSelectColor(View view) {

	}

	public void onClickEditShow(View view) {

	}

	public void onClickEditEmail(View view) {

	}

	public void onClickEditCancel(View view) {

	}

	public void onClickEditDelete(View view) {

	}

	public void onClickEditCommit(View view) {

	}
	// Additional Methods

}
