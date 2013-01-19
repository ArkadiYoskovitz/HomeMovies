package il.co.All4Students.homemovies.core;

import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_ITEM;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Item implements Comparable<Item>, Parcelable {
	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// Attribute
	private int _id;
	private String subject;
	private String body;
	private String urlWeb;
	private String urlLocal;
	private int rt_ID;
	private int rank;
	private boolean viewd;
	private int color;

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// Constractor
	public Item() {
		this(0, "", "", "", "", 0, 0, false, 0);
	}

	public Item(String subject) {
		this(0, subject, "", "", "", 0, 0, false, 0);
	}

	public Item(String subject, String body) {
		this(0, subject, body, "", "", 0, 0, false, 0);
	}

	public Item(String subject, String body, int color) {
		this(0, subject, body, "", "", 0, 0, false, color);
	}

	public Item(int _id, String subject, String body) {
		this(_id, subject, body, "", "", 0, 0, false, 0);
	}

	public Item(String subject, String body, String urlWeb, int rtID) {
		this(0, subject, body, urlWeb, "", rtID, 0, false, 0);
	}

	public Item(int _id, String subject, String body, String urlWeb,
			String urlLocal, int rtID, int rank, boolean watched, int color) {
		super();
		set_id(_id);
		setSubject(subject);
		setBody(body);
		setUrlWeb(urlWeb);
		setUrlLocal(urlLocal);
		setRt_ID(rtID);
		setRank(rank);
		setViewd(watched);
		setColor(color);
		printID();
	}

	public Item(Parcel in) {
		readFromParcel(in);
		printID();
	}

	public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {

		@Override
		public Item createFromParcel(Parcel in) {
			return new Item(in);
		}

		@Override
		public Item[] newArray(int size) {
			return new Item[size];
		}

	};

	// Parcel incoding and De-Coding Methods

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(_id);
		out.writeString(subject);
		out.writeString(body);
		out.writeString(urlWeb);
		out.writeString(urlLocal);
		out.writeInt(rt_ID);
		out.writeInt(rank);
		out.writeByte((byte) (viewd ? 1 : 0));
		out.writeInt(color);

	}

	public void readFromParcel(Parcel in) {
		_id = in.readInt();
		subject = in.readString();
		body = in.readString();
		urlWeb = in.readString();
		urlLocal = in.readString();
		rt_ID = in.readInt();
		rank = in.readInt();
		viewd = in.readByte() == 1;
		color = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// Get/Set Methods
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getUrlWeb() {
		return urlWeb;
	}

	public void setUrlWeb(String urlWeb) {
		this.urlWeb = urlWeb;
	}

	public String getUrlLocal() {
		return urlLocal;
	}

	public void setUrlLocal(String urlLocal) {
		this.urlLocal = urlLocal;
	}

	public int getRt_ID() {
		return rt_ID;
	}

	public void setRt_ID(int rt_ID) {
		this.rt_ID = rt_ID;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public boolean getViewd() {
		return viewd;
	}

	public void setViewd(boolean isViewd) {
		this.viewd = isViewd;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// Additional Methods

	public int getIntViewd() {
		if (viewd) {
			return 1;
		} else {
			return 0;
		}
	}

	public void setIntViewd(int in) {
		if (in == 1) {
			this.viewd = true;
		} else {
			this.viewd = false;
		}
	}

	@Override
	public int compareTo(Item another) {
		return this.get_id() - another.get_id();
	}

	@Override
	public String toString() {
		return subject;
	}

	public String toStringTwo() {
		return "Item [_id=" + _id + ", subject=" + subject + ", body=" + body
				+ ", urlWeb=" + urlWeb + ", urlLocal=" + urlLocal + ", rt_ID="
				+ rt_ID + ", rank=" + rank + ", viewd=" + viewd + ", color="
				+ color + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _id;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + color;
		result = prime * result + rank;
		result = prime * result + rt_ID;
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result
				+ ((urlLocal == null) ? 0 : urlLocal.hashCode());
		result = prime * result + ((urlWeb == null) ? 0 : urlWeb.hashCode());
		result = prime * result + (viewd ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (_id != other._id)
			return false;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (color != other.color)
			return false;
		if (rank != other.rank)
			return false;
		if (rt_ID != other.rt_ID)
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (urlLocal == null) {
			if (other.urlLocal != null)
				return false;
		} else if (!urlLocal.equals(other.urlLocal))
			return false;
		if (urlWeb == null) {
			if (other.urlWeb != null)
				return false;
		} else if (!urlWeb.equals(other.urlWeb))
			return false;
		if (viewd != other.viewd)
			return false;
		return true;
	}

	public void printID() {
		Log.d(LOG_TAG_ITEM, ((Integer) _id).toString());

	}

}
