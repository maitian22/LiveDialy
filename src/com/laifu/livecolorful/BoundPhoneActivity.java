package com.laifu.livecolorful;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BoundPhoneActivity extends Activity implements TextWatcher,
		Button.OnClickListener {
	private Button mGetSecurityCode, mTitleLeft, mTitleRight;
	private TextView mTitleText, mInputSecurityCode, mTextHint;
	private EditText mInputNumber, mEditInputSecurityCode;
	private TextView mCountSeconds;
	private int mGlobalSeconds = 60;
	private boolean IsConfirmScreen = false;
	private Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			mGlobalSeconds--;
			if (mGlobalSeconds >= 0) {
				mCountSeconds.setText(mGlobalSeconds
						+ getString(R.string.security_code_hint));
				Message msg1 = new Message();
				mhandler.sendMessageDelayed(msg1, 1000);
			}
		}

	};

	private void InitTitle() {
		mTitleText = (TextView) findViewById(R.id.title);
		mTitleText.setText("绑定手机");

		mTitleLeft = (Button) findViewById(R.id.left);
		mTitleLeft.setVisibility(android.view.View.INVISIBLE);

		mTitleRight = (Button) findViewById(R.id.right);
		mTitleRight.setBackgroundResource(R.drawable.gb_btn);
		mTitleRight.setOnClickListener(this);
	}
	TextWatcher mTextWatch = new TextWatcher(){
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if(mEditInputSecurityCode.getEditableText().length()==4){
				mGetSecurityCode.setBackgroundColor(getResources().getColor(
						R.color.green));
				mGetSecurityCode.setClickable(true);
			}else{
				mGetSecurityCode.setBackgroundColor(getResources().getColor(
						R.color.button_grey));
				mGetSecurityCode.setClickable(false);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bound_phone);

		InitTitle();
		mGetSecurityCode = (Button) findViewById(R.id.security_code);
		mGetSecurityCode.setOnClickListener(this);
		mInputNumber = (EditText) findViewById(R.id.input_number);
		mInputNumber.addTextChangedListener(this);

		mInputSecurityCode = (TextView) findViewById(R.id.input_security_code);
		mEditInputSecurityCode = (EditText) findViewById(R.id.edit_input_security_code);
		mEditInputSecurityCode.addTextChangedListener(mTextWatch);

		mTextHint = (TextView) findViewById(R.id.text_introduce);
		mCountSeconds = (TextView) findViewById(R.id.count_seconds);
	}

	private void sendSmsToServer() {

	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		if (mInputNumber.getEditableText().length() == 11) {
			mGetSecurityCode.setBackgroundColor(getResources().getColor(
					R.color.green));
			mGetSecurityCode.setClickable(true);
		} else {
			mGetSecurityCode.setBackgroundColor(getResources().getColor(
					R.color.button_grey));
			mGetSecurityCode.setClickable(false);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	private void entryConfirmSecurityCodeScreen() {
		IsConfirmScreen = true;
		mInputSecurityCode.setVisibility(android.view.View.VISIBLE);
		mEditInputSecurityCode.setVisibility(android.view.View.VISIBLE);
		mGetSecurityCode.setText("确定");
		mGetSecurityCode.setBackgroundColor(getResources().getColor(
				R.color.button_grey));
		
		mTitleText.setText("验证手机");
		mTitleLeft.setBackgroundResource(R.drawable.shape_button_white);
		mTitleLeft.setText("重新验证");
		mTitleLeft.setVisibility(android.view.View.VISIBLE);
		mTitleLeft.setOnClickListener(this);

		mTextHint.setVisibility(android.view.View.INVISIBLE);
		mCountSeconds.setVisibility(android.view.View.VISIBLE);
		mCountSeconds.setText(mGlobalSeconds
				+ getString(R.string.security_code_hint));

		Message msg = new Message();
		mhandler.sendMessageDelayed(msg, 1000);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (mGetSecurityCode == v) {
			if(IsConfirmScreen){
				finish();
			}else{
				mInputNumber.setEnabled(false);
				mInputNumber.setFocusable(false);
				sendSmsToServer();
				entryConfirmSecurityCodeScreen();
			}
		} else if (mTitleRight == v) {
			finish();
		}
	}
}