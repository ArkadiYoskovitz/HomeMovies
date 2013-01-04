package il.co.All4Students.homemovies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ScreenWeb extends Activity implements OnItemClickListener {
	// Attributes

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
	public void onClickWebGo(View view) {

	}

	public void onClickWebCancel(View view) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	// Additional Methods

}
