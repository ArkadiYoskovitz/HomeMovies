//import il.co.All4Students.homemovies.R;
//
//import java.net.MalformedURLException;
//import java.util.List;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class MediaItemAdapter extends ArrayAdapter<MediaItem> {
//	private final static String TAG = "MediaItemAdapter";
//	private int resourceId = 0;
//	private LayoutInflater inflater;
//	private Context context;
//	private ImageThreadLoader imageLoader = new ImageThreadLoader();
//
//	public MediaItemAdapter(Context context, int resourceId,
//			List<MediaItem> mediaItems) {
//		super(context, 0, mediaItems);
//		this.resourceId = resourceId;
//		inflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		this.context = context;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		View view;
//		TextView textTitle;
//		TextView textTimer;
//		final ImageView image;
//		view = inflater.inflate(resourceId, parent, false);
//		try {
//			textTitle = (TextView) view.findViewById(R.id.text);
//			image = (ImageView) view.findViewById(R.id.icon);
//		} catch (ClassCastException e) {
//			Log.e(TAG,
//					"Your layout must provide an image and a text view with ID's icon and text.",
//					e);
//			throw e;
//		}
//		MediaItem item = getItem(position);
//		Bitmap cachedImage = null;
//		try {
//			cachedImage = imageLoader.loadImage(item.thumbnail,
//					new ImageLoadedListener() {
//						public void imageLoaded(Bitmap imageBitmap) {
//							image.setImageBitmap(imageBitmap);
//							notifyDataSetChanged();
//						}
//					});
//		} catch (MalformedURLException e) {
//			Log.e(TAG, "Bad remote image URL: " + item.thumbnail, e);
//		}
//		textTitle.setText(item.name);
//		if (cachedImage != null) {
//			image.setImageBitmap(cachedImage);
//		}
//		return view;
//	}
//}
