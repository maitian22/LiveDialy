package com.laifu.liveDialy.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.laifu.liveDialy.R;
import com.laifu.liveDialy.XingchengdanDetailActivity;

public class IndexLaifuAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	ArrayList list;
	public IndexLaifuAdapter(Context context,ArrayList list) {
		this.context = context;
		inflater = inflater.from(context);
		this.list=list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.index_laifu_adapter, null);

			holder.contentView = convertView.findViewById(R.id.content_view);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.people_num = (TextView) convertView
					.findViewById(R.id.people_num);
			holder.xingcheng_num = (TextView) convertView
					.findViewById(R.id.xingcheng_num);
			holder.new_num = (TextView) convertView.findViewById(R.id.new_num);

			holder.build_view = convertView.findViewById(R.id.build_view);
			holder.icon_image = (ImageView) convertView
					.findViewById(R.id.icon_image);
			holder.build_textView = (TextView) convertView
					.findViewById(R.id.build_text);
			holder.top_line=(TextView)convertView.findViewById(R.id.top_line);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.contentView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,
						XingchengdanDetailActivity.class);
				context.startActivity(intent);
			}
		});
		if (position != 1) {
			holder.build_view.setVisibility(View.GONE);
		}else{
			holder.build_view.setVisibility(View.VISIBLE);
		}

		if(position==0){
			holder.top_line.setVisibility(View.VISIBLE);
		}else{
			holder.top_line.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	class Holder {
		View contentView;
		ImageView image;
		TextView title;
		TextView people_num;
		TextView xingcheng_num;
		TextView new_num;
		View build_view;
		ImageView icon_image;
		TextView build_textView;
		TextView top_line;
	}
}
