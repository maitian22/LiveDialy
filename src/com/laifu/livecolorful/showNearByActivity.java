package com.laifu.livecolorful;

import com.laifu.livecolorful.adapter.MyRountListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class showNearByActivity extends Activity implements
		Button.OnClickListener {
	private Button titleLeft, titleRight;
	private TextView title;
	private ListView mMyNearByList;

	public void InitTitle() {
		titleLeft = (Button) findViewById(R.id.left);
		titleLeft.setVisibility(android.view.View.INVISIBLE);

		title = (TextView) findViewById(R.id.title);
		title.setText("查看附近");

		titleRight = (Button) findViewById(R.id.right);
		titleRight.setBackgroundResource(R.drawable.gb_btn);
		titleRight.setOnClickListener(this);
	}

	public void InitMyListView() {
		mMyNearByList = (ListView) findViewById(R.id.my_nearby_list);
		MyRountListAdapter adapter = new MyRountListAdapter(this,
				GlobaleData.getMyNearByData(), R.layout.my_rount_list_adapter,
				new String[] { "leftimg", "lines", "title", "img1", "text1" },
				new int[] { R.id.left_img, R.id.mlines, R.id.title, R.id.img1,
						R.id.text1 });
		adapter.setTextColor(R.id.text1,
				this.getResources().getColor(R.color.rount_list_num_color));
		adapter.setTextMarginleft(R.id.text1, 10);
		mMyNearByList.setAdapter(adapter);
		mMyNearByList.setDividerHeight(0);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_nearby);
		InitTitle();
		InitMyListView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == titleRight) {
			finish();
		}
	}

}
