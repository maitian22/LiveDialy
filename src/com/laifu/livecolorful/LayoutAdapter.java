package com.laifu.livecolorful;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LayoutAdapter extends BaseAdapter {
	Context mContext;
	int[] mImageIds, mStringIds, mCountNumbers;
	private View[] itemViews;

	public LayoutAdapter(Context context) {
		mContext = context;
	}

	public LayoutAdapter(Context context, int[] img, int[] strIds, int[] numbers) {
		mContext = context;
		mImageIds = img;
		mStringIds = strIds;
		mCountNumbers = numbers;
		
		itemViews = new View[mImageIds.length];  
		for (int i = 0; i < itemViews.length; i++) {
			itemViews[i] = makeItemView(mImageIds[i], mStringIds[i],mCountNumbers[i]);
		}
	}
	
	private View makeItemView(int strmImageIds, int strtext,int numbers) {  
		ImageView img;
		TextView mText,mCounts;
        LayoutInflater inflater = (LayoutInflater) mContext  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
        View itemView = inflater.inflate(R.layout.grid_discover, null);  
        
        img = (ImageView)itemView.findViewById(R.id.img);
		img.setBackgroundResource(strmImageIds);
		mText = (TextView)itemView.findViewById(R.id.text);
		mText.setText(strtext);
		mCounts = (TextView)itemView.findViewById(R.id.numbers);
		mCounts.setText(numbers+"个");
        return itemView;  
    }  
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemViews.length;
	}

	@Override
	public View getItem(int arg0) {
		// TODO Auto-generated method stub
		return itemViews[arg0];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
            return itemViews[position];  
	}

}
