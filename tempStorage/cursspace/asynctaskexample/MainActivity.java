package cursspace.asynctaskexample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

@SuppressLint("Registered")
public class MainActivity extends Activity {

	@SuppressWarnings("unused")
	private DownloadImageTask downloadTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		// Button btn = (Button)findViewById(R.id.button1);
		// btn.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (downloadTask != null) {
		// if (downloadTask.getStatus() != AsyncTask.Status.FINISHED) {
		// Log.d("doClick", "no need to start a new task");
		// return;
		// }
		// }
		//
		//
		// DownloadImageTask downloadTask = new
		// DownloadImageTask(MainActivity.this);
		// String [] imageUrl
		// ={"http://www.publicdomainpictures.net/pictures/20000/velka/butterfly-on-a-rose.jpg"};
		// downloadTask.execute(imageUrl);
		//
		// }
		// });
	}

}
