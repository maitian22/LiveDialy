package com.laifu.livecolorful;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.laifu.livecolorful.tool.Constant;

public abstract class ShowAlbumPicDialog extends Dialog implements
		Button.OnClickListener {
	private String TAG = "ShowAlbumPicDialog";
	private Button cancel_btn, pass_btn;
	private ImageView mShowPic;
	private Context mContext;
	private WindowManager wm;
	private Bitmap mBitmap;

	public ShowAlbumPicDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private void setParams(android.view.WindowManager.LayoutParams lay) {
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);

		Rect rect = new Rect();
		View view = getWindow().getDecorView();
		view.getWindowVisibleDisplayFrame(rect);
		lay.height = dm.heightPixels - rect.top;
		lay.width = dm.widthPixels;
	}

	void SetShowPic() {
		int width = wm.getDefaultDisplay().getWidth();
		int height = width*mBitmap.getHeight()/mBitmap.getWidth();
		mShowPic.setScaleType(ScaleType.CENTER_CROP);
		mShowPic.setImageBitmap(mBitmap);
		mShowPic.setImageBitmap(GlobaleData.zoomImage(mBitmap, width, height));
		
		/*Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(mContext.getContentResolver()
					.openInputStream(mUri));
			mShowPic.setScaleType(ScaleType.CENTER_CROP);
			mShowPic.setImageBitmap(Constant.zoomImage(bitmap, wm.getDefaultDisplay()
					.getWidth(), ));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public ShowAlbumPicDialog(Context context, Bitmap mB) {
		super(context, R.style.showpicdialog);
		mBitmap = mB;
		mContext = context;
		setContentView(R.layout.show_pic_dialog);
		wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

		cancel_btn = (Button) findViewById(R.id.canel_btn);
		cancel_btn.setOnClickListener(this);
		pass_btn = (Button) findViewById(R.id.pass_btn);
		pass_btn.setOnClickListener(this);

		mShowPic = (ImageView) findViewById(R.id.show_pic);
		SetShowPic();

		android.view.WindowManager.LayoutParams lay = getWindow()
				.getAttributes();
		setParams(lay);
	}

	abstract void onCancelClick();

	abstract void onPassClick();

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == pass_btn) {
			onPassClick();
		} else {
			onCancelClick();
		}
	}

}
