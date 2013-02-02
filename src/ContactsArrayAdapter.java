import il.co.All4Students.homemovies.R;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Adapter class to show the contacts
 */
public class ContactsArrayAdapter extends ArrayAdapter {

	/** Contacts list */
	private List contacts;

	/** Context */
	private Context context;

	public ContactsArrayAdapter(Context context, int textViewResourceId,
			List contacts) {
		super(context, textViewResourceId, contacts);
		this.context = context;
		this.contacts = contacts;
	}

	@Override
	public View getView(int position, View rowView, ViewGroup parent) {
		// Keeps reference to avoid future findViewById()
		rowViewHolder viewHolder;

		if (rowView == null) {
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = li.inflate(R.layout.list_row, parent, false);

			viewHolder = new rowViewHolder();
			viewHolder.rowTitle = (TextView) rowView.findViewById(R.id.rowTitle);
			viewHolder.rowRank = (RatingBar) rowView.findViewById(R.id.rowRating);
			viewHolder.rowCheckBox = (CheckBox) rowView.findViewById(R.id.rowCheckBox);
			viewHolder.rowImage = (ImageView) rowView.findViewById(R.id.rowImage);
			rowView.setTag(viewHolder);
		} else {
			viewHolder = (rowViewHolder) rowView.getTag();
		}

//		Contact contact = contacts.get(position);
//		if (contact != null) {
//			viewHolder.txName.setText(contact.getName());
//			viewHolder.txEmails.setText(contact.getEmails().toString());
//			viewHolder.txPhones.setText(contact.getNumbers().toString());
//		}
		return rowView;
	}

	static class rowViewHolder {
		LinearLayout rowThumbnail;
		ImageView rowImage;
		TextView rowTitle;
		RatingBar rowRank;
		CheckBox rowCheckBox;
	}
}