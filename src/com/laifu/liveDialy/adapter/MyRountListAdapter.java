package com.laifu.liveDialy.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MyRountListAdapter extends BaseAdapter {
	Context mContext;
	LayoutInflater inflater;
	String TAG = "MyRountListAdapter";
	String[] mFrom;
	int[] mTo;
	List<Map<String, Object>> dataList;
	int colorResId, marginResId;
	int setColorViewId, marginLeftLen;
	int layoutId;
	private CallBacks mCallBacks;
	private int mListenerId[];
	private ArrayList<ChangeImage> mArrayList = new ArrayList<ChangeImage>();

	public class ChangeImage {
		int mPosition, mViewId, mImageId;

		public ChangeImage(int position, int viewid, int imageid) {
			mPosition = position;
			mViewId = viewid;
			mImageId = imageid;
		}
	}

	public MyRountListAdapter(Context context, List<Map<String, Object>> mlist,
			int layout, String[] str, int[] ids) {
		this.mContext = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFrom = str;
		mTo = ids;
		dataList = mlist;
		layoutId = layout;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return dataList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private void setViewText(TextView v, String text) {
		v.setText(text);
		if (v.getId() == setColorViewId)
			v.setTextColor(colorResId);
		if (v.getId() == marginResId) {
			LayoutParams lp = (LayoutParams) v.getLayoutParams();
			lp.setMargins(marginLeftLen, 0, 0, 0);
			v.setLayoutParams(lp);
		}
	}

	private void setViewImage(ImageView v, String value) {
		try {
			v.setImageResource(Integer.parseInt(value));
		} catch (NumberFormatException nfe) {
			v.setImageURI(Uri.parse(value));
		}
	}

	public void setChangeImageView(int position, int resid, int imgid) {
		ChangeImage mImg = new ChangeImage(position, resid, imgid);
		for(int i = 0;i<mArrayList.size();i++){
			if(position == mArrayList.get(i).mPosition
					&&resid==mArrayList.get(i).mViewId)
				mArrayList.remove(i);
		}
		mArrayList.add(mImg);
	}

	public void setTextMarginleft(int resId, int marginleftlen) {
		marginResId = resId;
		marginLeftLen = marginleftlen;

	}

	public void setTextColor(int resId, int colorId) {
		setColorViewId = resId;
		colorResId = colorId;
	}

	private void setViewImage(ImageView v, int value, int position) {
		boolean needchange = false;
		ChangeImage mImg;
		for (int i = 0; i < mArrayList.size(); i++) {
			mImg = mArrayList.get(i);
			if ((position == mImg.mPosition) && (v.getId() == mImg.mViewId)) {
				v.setImageResource(mImg.mImageId);
				needchange = true;
				break;
			}
		}
		if(!needchange)
			v.setImageResource(value);
	}

	private void bindView(int position, View view) {
		final Map dataSet = dataList.get(position);
		if (dataSet == null) {
			return;
		}

		final String[] from = mFrom;
		final int[] to = mTo;
		final int count = to.length;

		for (int i = 0; i < count; i++) {
			final View v = view.findViewById(to[i]);
			v.setVisibility(android.view.View.VISIBLE);
			if (v != null) {
				final Object data = dataSet.get(from[i]);
				String text = data == null ? "" : data.toString();
				if (text == null) {
					text = "";
				}

				if (v instanceof Checkable) {
					if (data instanceof Boolean) {
						((Checkable) v).setChecked((Boolean) data);
					} else if (v instanceof TextView) {
						// Note: keep the instanceof TextView check at the
						// bottom of these
						// ifs since a lot of views are TextViews (e.g.
						// CheckBoxes).
						setViewText((TextView) v, text);
					} else {
						throw new IllegalStateException(v.getClass().getName()
								+ " should be bound to a Boolean, not a "
								+ (data == null ? "<unknown type>"
										: data.getClass()));
					}
				} else if (v instanceof TextView) {
					// Note: keep the instanceof TextView check at the bottom of
					// these
					// ifs since a lot of views are TextViews (e.g. CheckBoxes).
					setViewText((TextView) v, text);
				} else if (v instanceof ImageView) {
					if (data instanceof Integer) {
						setViewImage((ImageView) v, (Integer) data, position);
					} else {
						setViewImage((ImageView) v, text);
					}
				} else {
					throw new IllegalStateException(v.getClass().getName()
							+ " is not a "
							+ " view that can be bounds by this SimpleAdapter");
				}

			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v;
		if (convertView == null) {
			v = inflater.inflate(layoutId, parent, false);
		} else {
			v = convertView;
		}
		bindView(position, v);
		ItemListener mlistener = new ItemListener(position);
		if (mListenerId != null) {
			for (int i = 0; i < mListenerId.length; i++) {
				v.findViewById(mListenerId[i]).setOnClickListener(mlistener);
			}
		}
		return v;
	}

	public void setOnClickViewLisener(int[] viewid, CallBacks callbacks) {
		mCallBacks = callbacks;
		mListenerId = viewid;
	}

	public interface CallBacks {
		void onViewClicked(int positon, int viewId);
	}

	class ItemListener implements OnClickListener {
		private int m_position;

		ItemListener(int pos) {
			m_position = pos;
		}

		@Override
		public void onClick(View viewId) {
			Log.i(TAG,"m_position:"+m_position);
			mCallBacks.onViewClicked(m_position, viewId.getId());
		}
	}
}
