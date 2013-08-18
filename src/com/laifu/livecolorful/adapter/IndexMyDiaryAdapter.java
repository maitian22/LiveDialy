package com.laifu.livecolorful.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.laifu.livecolorful.R;
import com.laifu.livecolorful.XingchengDetailActivity;

public class IndexMyDiaryAdapter extends BaseAdapter{
	
	Context context;
	LayoutInflater inflater;
	public IndexMyDiaryAdapter(Context context){
		this.context=context;
		inflater=inflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder;
		if(convertView==null){
			holder=new Holder();
			convertView=inflater.inflate(R.layout.index_mydiary_adapter, null);
			holder.date=convertView.findViewById(R.id.time_view);
			holder.sanjiao=(ImageView)convertView.findViewById(R.id.sanjiao);
			holder.naozhong=(ImageView)convertView.findViewById(R.id.naozhong);
			holder.title=(TextView)convertView.findViewById(R.id.title);
			holder.date=(TextView)convertView.findViewById(R.id.time);
			holder.content_view=convertView.findViewById(R.id.content_view);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		holder.content_view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,XingchengDetailActivity.class);
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}
	class Holder{
		View date;
		ImageView sanjiao;
		TextView title;
		TextView time;
		ImageView naozhong;
		View content_view;
	}
}
