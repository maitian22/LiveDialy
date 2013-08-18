package com.laifu.livecolorful.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import com.laifu.livecolorful.R;

public class JuMeiCustomProgressDlg extends Dialog {
	private Context context = null;
	private static JuMeiCustomProgressDlg ProgressDialog = null;

	private JuMeiCustomProgressDlg(Context context) {
		super(context);
		this.context = context;
	}

	private JuMeiCustomProgressDlg(Context context, int theme) {
		super(context, theme);
	}

	public static JuMeiCustomProgressDlg createDialog(Context context) {
		
	
		if (null == context) {			
			return null;
		}
		
		ProgressDialog = new JuMeiCustomProgressDlg(context,
				R.style.jumeicustomprogressdlg);
		ProgressDialog.setContentView(R.layout.jumeicustomprogressdlg);
		ProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

		return ProgressDialog;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		if (ProgressDialog == null) {
			return;
		}

		/*
		ImageView imageView = (ImageView) ProgressDialog
				.findViewById(R.id.loadingImageView);
		
			AnimationDrawable animationDrawable = (AnimationDrawable) imageView
					.getBackground();
			animationDrawable.start();
		*/
	}

	/**
	 * 
	 * [Summary] setTitile 标题
	 * 
	 * @param strTitle
	 * @return
	 * 
	 */
	public JuMeiCustomProgressDlg setTitile(String strTitle) {
		return ProgressDialog;
	}

	/**
	 * 
	 * [Summary] setMessage 提示内容
	 * 
	 * @param strMessage
	 * @return
	 * 
	 */
	public JuMeiCustomProgressDlg setMessage(String strMessage) {

		//产品人员要求不显示加载提示,只显示白色圆环
		/*
		TextView tvMsg = (TextView) ProgressDialog
				.findViewById(R.id.id_tv_loadingmsg);

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
		*/

		return ProgressDialog;
	}
}