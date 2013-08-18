package com.laifu.livecolorful.wxapi;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.ShowMessageFromWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	public static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
	public static final String APP_ID = "wx9b1de7cd8f8deb72";

	private static final int THUMB_SIZE = 150;

	// IWXAPI 是第三方app和微信通信的openapi接口
	public  IWXAPI api;

	String title, message, imageurl = null, producturl;
	Bundle bundle;
	int way=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.entry);
		LinearLayout layout2;
		layout2 = new LinearLayout(this);
		layout2.setGravity(Gravity.BOTTOM | Gravity.LEFT);
		layout2.setBackgroundDrawable(null); /*
											 * 设置成透明，必须在AndroidManifest.
											 * xml中设置android:theme=
											 * "@android:style/Theme.Translucent"
											 */
		setContentView(layout2);
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, APP_ID, true);
		api.registerApp(APP_ID);

		api.handleIntent(getIntent(), this);

		bundle=getIntent().getExtras();
		
		title = bundle.getString("title");
		message = bundle.getString("message");
		imageurl = bundle.getString("imageurl");
		producturl = bundle.getString("producturl");
		way=bundle.getInt("way");
//		imageurl = ImageAddress.getCustomWidthAndHeightImageAddress(imageurl,
//				205, 273);
		Log.i("aa", "title--" + title + "--message--" + message
				+ "--imageurl--" + imageurl + "--producturl--" + producturl);
		if(title==null){
			return;
		}
		handler.sendEmptyMessage(0);
		// shareMessage();
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			shareMessage();
		}
	};

	Bitmap bitmap;

	public void shareMessage() {
		try {
			if (!checkWx()) {

				return;
			}

			bitmap = loadImgFromUrl(imageurl);

			if (bitmap == null) {
//				Toast.makeText(this, "获取图片失败，请稍后获取!", Toast.LENGTH_SHORT).show();
				return;
			} else {
//				Toast.makeText(this, "获取图片成功!", Toast.LENGTH_SHORT).show();
			}
			share(way);
//			if (checkSharetype()) {
//				AlertDialog.Builder adb = new AlertDialog.Builder(this);
//				adb.setTitle("大麦提示");
//				adb.setMessage("亲，请选择您要分享到地方");
//				adb.setPositiveButton("分享到朋友圈",
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								dialog.dismiss();
//								share(SendMessageToWX.Req.WXSceneTimeline);
//								// api.unregisterApp();
//								// finish();
//							}
//
//						});
//				adb.setNegativeButton("分享给朋友",
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								dialog.dismiss();
//								share(0);
//							}
//
//						});
//				adb.show();
//
//			} else {
//				share(0);
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***
	 * 查看是否安装微信 没安装就提示安装
	 * 
	 * @return
	 */
	public boolean checkWx() {
		PackageManager packageManager = WXEntryActivity.this
				.getPackageManager();

		if (null == packageManager.getLaunchIntentForPackage("com.tencent.mm")) {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle("大麦提示");
			adb.setMessage("您还没有安装微信，点击确定进行安装");
			adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri
							.parse("http://weixin.qq.com/cgi-bin/download302?check=false&uin=&stype=&promote=&fr=&lang=zh_CN&ADTAG=&url=android16");
					intent.setData(content_url);
					startActivity(intent);
					dialog.dismiss();
					finish();
				}

			});
			adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					finish();
				}

			});
			adb.show();

			return false;
		} else {
			return true;
		}
	}

	byte[] arraybyte;

	public boolean checkSharetype() {
		int wxSdkVersion = api.getWXAppSupportAPI();
		if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
			return true;
		} else {
			return false;
		}
	}

	public void share(int type) {
		try {
			WXWebpageObject webpage = new WXWebpageObject();
			webpage.webpageUrl = producturl;
			WXMediaMessage msg = new WXMediaMessage(webpage);
//			if (type != 0) {
//				msg.title = message;
//			}else{
				msg.title = title;
//			}
				if(type==0){
					msg.description = message;
				}
//			msg.description = message;
			arraybyte=Bitmap2Bytes(bitmap);
			Log.i("aa", "lenth--->" + arraybyte.length + "bitmap.width-->"
					+ bitmap.getWidth());
			msg.thumbData = arraybyte;
			msg.setThumbImage(bitmap);
			// Bitmap thumb = BitmapFactory.decodeResource(getResources(),
			// R.drawable.icon);
			// msg.thumbData = Util.bmpToByteArray(thumb, true);

			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = buildTransaction("testapp");
			req.message = msg;
			if (type != 0) {
				req.scene = SendMessageToWX.Req.WXSceneTimeline;
			}
			api.sendReq(req);
			WXEntryActivity.this.finish();
			Log.i("aa", "分享完成，怎么办");
		} catch (Exception e) {
			e.printStackTrace();
			finish();
			Toast.makeText(this, "亲，程序员偷了个懒，sorry!", Toast.LENGTH_SHORT).show();
		}
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	/***
	 * 打开微信客户端
	 */
	public void openWchart() {
		api.openWXApp();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.i("aa", "onnewintent");
		bundle = intent.getExtras();
		setIntent(intent);
		api.handleIntent(intent, this);
		if(WXEntryActivity.this!=null){
			finish();
		}
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			goToGetMsg();
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		default:
			break;
		}
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		String result = "";

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "发送成功";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "发送取消";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "发送被拒绝";
			break;
		default:
			result = "发送返回";
			break;
		}

		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		WXEntryActivity.this.finish();
	}

	private void goToGetMsg() {
		// Intent intent = new Intent(this, GetFromWXActivity.class);
		// intent.putExtras(getIntent());
		// startActivity(intent);
		// finish();
	}

	private void goToShowMsg(ShowMessageFromWX.Req showReq) {
		// WXMediaMessage wxMsg = showReq.message;
		// WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
		//
		// StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
		// msg.append("description: ");
		// msg.append(wxMsg.description);
		// msg.append("\n");
		// msg.append("extInfo: ");
		// msg.append(obj.extInfo);
		// msg.append("\n");
		// msg.append("filePath: ");
		// msg.append(obj.filePath);
		//
		// Intent intent = new Intent(this, ShowFromWXActivity.class);
		// intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
		// intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
		// intent.putExtra(Constants.ShowMsgActivity.BAThumbData,
		// wxMsg.thumbData);
		// startActivity(intent);
		// finish();
	}

	public Bitmap loadImgFromUrl(String url) {
		Bitmap bitmap = null;
		if (url != null && !url.equals("")) {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream is = null;
			try {
				URL urlGetImg = new URL(url);
				URLConnection conn = urlGetImg.openConnection();
				// conn.setConnectTimeout(15 * 1000);
				conn.connect();
				is = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);
				byte[] buffer = new byte[1024];
				int length = 0;
				while ((length = is.read(buffer, 0, 1024)) != -1) {
					baos.write(buffer, 0, length);
				}
				arraybyte = baos.toByteArray();
				baos.flush();
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				try {
					if (is != null) {
						is.close();
						is = null;
					}
					if (baos != null) {
						baos.close();
						baos = null;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

		}
		return bitmap;
	}

	private byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
}