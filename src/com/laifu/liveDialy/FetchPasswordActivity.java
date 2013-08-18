package com.laifu.liveDialy;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FetchPasswordActivity extends LiveBaseActivity implements TextWatcher{
	
	private Button leftBtn,rightBtn;
	private TextView mTitle;
	private EditText mInputEditText;
	private Button mNextStepBtn;
	private void InitTitle(){
		leftBtn = (Button)findViewById(R.id.left);
		leftBtn.setVisibility(android.view.View.INVISIBLE);
		
		mTitle = (TextView)findViewById(R.id.title);
		mTitle.setVisibility(android.view.View.VISIBLE);
		mTitle.setText("找回密码");
		
		rightBtn = (Button)findViewById(R.id.right);
		rightBtn.setBackgroundResource(R.drawable.gb_btn);
		rightBtn.setOnClickListener(this);
		
		mInputEditText = (EditText)findViewById(R.id.input_edit_text);
		mInputEditText.addTextChangedListener(this);
		
		mNextStepBtn = (Button)findViewById(R.id.next_step_button);
		mNextStepBtn.setOnClickListener(this);
	}
	
	@Override
	public void initPages() {
		// TODO Auto-generated method stub
		InitTitle();
		InitStepButton();
	}

	@Override
	protected void onClickListener(int viewId) {
		// TODO Auto-generated method stub
		if(viewId == rightBtn.getId()){
			finish();
		}else if(viewId == mNextStepBtn.getId()){
			Intent i = new Intent(this,FetchPasswordPathActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		}
	}

	@Override
	public int setModelId() {
		// TODO Auto-generated method stub
		return R.layout.fetch_password;
	}

	@Override
	public int setLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fetch_password;
	}
	
	private void InitStepButton(){
		if(mInputEditText.getEditableText().toString().length()==0){
			mNextStepBtn.setClickable(false);
			mNextStepBtn.setBackgroundResource(R.drawable.shape_button_grey);
		}else{
			mNextStepBtn.setClickable(true);
			mNextStepBtn.setBackgroundResource(R.drawable.shape_button_blue);
		}
	}
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		InitStepButton();
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

}
