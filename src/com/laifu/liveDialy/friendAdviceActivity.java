package com.laifu.liveDialy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.laifu.liveDialy.adapter.MyRountListAdapter;

public class friendAdviceActivity extends Activity implements
		Button.OnClickListener {

	private Button titleLeft, titleRight;
	private TextView title;
	private ListView mListView;

	public void InitTitle() {
		titleLeft = (Button) findViewById(R.id.left);
		titleLeft.setVisibility(android.view.View.INVISIBLE);

		title = (TextView) findViewById(R.id.title);
		title.setText("朋友推荐");

		titleRight = (Button) findViewById(R.id.right);
		titleRight.setBackgroundResource(R.drawable.gb_btn);
		titleRight.setOnClickListener(this);
	}

	public void InitMyListView() {
		mListView = (ListView) findViewById(R.id.my_nearby_list);
		MyRountListAdapter adapter = new MyRountListAdapter(this,
				GlobaleData.getMyFriendAdviceData(),
				R.layout.my_rount_list_adapter, new String[] { "leftimg",
						"lines", "title", "text0", "text1" }, new int[] {
						R.id.left_img, R.id.mlines, R.id.title, R.id.text0,
						R.id.text1 });
		adapter.setTextColor(R.id.text1,
				getResources().getColor(R.color.rount_list_introduce_color));
		mListView.setAdapter(adapter);
		mListView.setDividerHeight(0);
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
