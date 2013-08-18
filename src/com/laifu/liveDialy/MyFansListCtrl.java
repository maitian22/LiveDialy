package com.laifu.liveDialy;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.laifu.liveDialy.adapter.MyRountListAdapter;

public class MyFansListCtrl implements MyRountListAdapter.CallBacks,
		AdapterView.OnItemClickListener {
	public String TAG = "MyFansListCtrl";
	private RelativeLayout mRel;
	private Context mContext;
	private ListView mlist;
	private MyRountListAdapter mAdapter;

	public MyFansListCtrl(Context c, RelativeLayout ml) {
		mContext = c;
		mRel = ml;
		mlist = (ListView) mRel.findViewById(R.id.my_fans_list);
	}

	public void InitMyFansList() {
		mAdapter = new MyRountListAdapter(mContext,
				GlobaleData.getMyFansListData(), R.layout.my_fans_list,
				new String[] { "img", "name", "addicon" }, new int[] {
						R.id.img, R.id.name, R.id.add_icon });
		int[] viewid = { R.id.add_icon };
		mAdapter.setOnClickViewLisener(viewid, this);
		mlist.setAdapter(mAdapter);
		mlist.setOnItemClickListener(this);
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Log.i(TAG,"arg2:"+arg2+",arg3:"+arg3+",arg1:"+arg1.getId());
	}

	@Override
	public void onViewClicked(int position, int viewId) {
		// TODO Auto-generated method stub
		mAdapter.setChangeImageView(position, R.id.add_icon, R.drawable.arraw);
		mlist.setAdapter(mAdapter);
	}
}
