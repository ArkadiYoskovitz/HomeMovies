import il.co.All4Students.homemovies.R;
import il.co.All4Students.homemovies.core.Item;
import il.co.All4Students.homemovies.util.db.ItemsHandler;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RatingBar;

/**
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
public class RankDialog {
	// Attributes
	private Context mContext;
	private RatingBar mRankBar;
	private Item mItem;

	// Constractors
	public RankDialog(Item item, Context context) {
		super();
		this.mItem = item;
		this.mContext = context;
	}

	// Additional Methods
	public void showRankDialog() {
		LayoutInflater li = LayoutInflater.from(mContext);

		View RankDialogView = li.inflate(R.layout.custom_dialog_rank, null);

		AlertDialog.Builder rankDialog = new AlertDialog.Builder(mContext);
		rankDialog.setView(RankDialogView);
		rankDialog.setTitle(mContext.getResources().getString(R.string.RankDialogTitle));
		rankDialog.setTitle(mContext.getResources().getString(R.string.RankDialogMsg));
		rankDialog.create();
		
		// Showing Alert Message
		final AlertDialog RDialog = rankDialog.show();

		mRankBar = (RatingBar) RankDialogView.findViewById(R.id.customDialogRankBar);
		mRankBar.setRating(((float) mItem.getRank()) / 10);
		View btnCancel = RankDialogView.findViewById(R.id.customDialogRankButtonCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RDialog.dismiss();
			}
		});

		View btnCommit = RankDialogView.findViewById(R.id.customDialogRankButtonCommit);
		
		btnCommit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mItem.setRank((int) (mRankBar.getRating() * 10));
				
				Log.d("logtag", mItem.getRank() + "");
				
				ItemsHandler itemHandler = new ItemsHandler(mContext);
				itemHandler.updateItem(mItem);
				
//				mItemList.clear();
//				mItemList = itemHandler.getAllItems();
//				ScreenMain.this.mAdapter = new ItemListAdapter(mItemList,
//						ScreenMain.this);
//				ScreenMain.this.mListView.setAdapter(ScreenMain.this.mAdapter);
//				ScreenMain.this.mAdapter.notifyDataSetChanged();
				
				RDialog.dismiss();
			}
		});
	}
}