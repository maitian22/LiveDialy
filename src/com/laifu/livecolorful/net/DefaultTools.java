/**  
 * Filename:    DefaultTools.java     
 * Copyright:   Copyright (c)2011 
 * @author:     zoutiao  
 * @version:    1.0
 * Create at:   2011-7-12 下午02:56:15  
 *  
 * Modification History:  
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2011-7-12    zoutiao             1.0        1.0 Version  
 */
package com.laifu.livecolorful.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.commons.io.filefilter.IOFileFilter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;

/**
 * 该抽象类是工具类，封装了一些常用的功能，继承该抽象类必须实现：1）设置头信息{@link #setHttpHeader(Context)}
 * 2)将头信息组合成Map数据{@link #createHttpHeaderMapData(Context)}<br>
 * <br>
 * 该类包括的功能有： 1）获取sdk版本信息{@link #getSDKVersion(Context)}; 2)获取运营商信息
 * {@link #getCarrier(Context)} 3)读取手机串号{@link #readTelephoneSerialNum(Context)}
 * ; 4)MD5加密字符串{@link #md5(String)}; 5)获取路径中的文件名
 * {@link #getFileNameFromURL(String)}; 6)根据url得到网络图片
 * {@link #getBitmapFromUrl(String)}; 7)保存文件到指定路径，后缀名修改为指定的后缀postfix
 * {@link #saveImageToSDAndChangePostfix(Bitmap, String, String, String)};
 * 8)保存文件到指定路径，后缀名不修改{@link #saveImageToSD(Bitmap, String, String)};
 * 9)从asset中得到source id{@link #getSourceIdFromAsset(Context)}; 10)判断网络是否可用
 * {@link #isAccessNetwork(Context)}; 11)获取网络类型信息
 * {@link #getAccessNetworkType(Context)}; 12)获取SD卡剩余空间{@link #getSDSize()};
 * 13)获取当前时间{@link #getNowTime()},以yyyyMMddHHmmss形式返回; 14)删除指定路径的文件
 * {@link #deleteAllFiles(String)}; 15)网络不可用提示对话框
 * {@link #netErrorToBack(Context)};
 * 
 * @author zoutiao
 * 
 *         修改： 2011-10-14 zhangchen (1) 添加获得指定文件夹方法getFileSize() (2)
 *         添加删除指定文件夹方法deleteFiles() (3) 添加如果缓存文件过大则定时清空缓存方法clearCacheFile（）
 * 
 *         2011-10-24 likuan 修改：(1) 修改网络判断代码，具体到2G，2.5G，3G，3.5G 2012-12-31
 *         wangfang 添加取得imsi的方法。
 * 
 */
public abstract class DefaultTools {
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 初始化默认协议的HTTP头信息
	 */
	public static void setDefaultHttpHeader(Context context) {

	}

	/**
	 * 设置http头信息
	 */
	public abstract void setHttpHeader(Context context);

	/**
	 * 将http头信息组合成Map数据
	 * 
	 * @return 返回封装好的头信息
	 */
	public abstract Map<String, String> createHttpHeaderMapData(Context context);

	/**
	 * 获取sdk版本信息
	 */
	public static String getSDKVersion(Context context) {
		String ver;
		String verInt = android.os.Build.VERSION.SDK;
		if ("2".equals(verInt)) {
			ver = "android1.1";
		} else if ("3".equals(verInt)) {
			ver = "android1.5";
		} else if ("4".equals(verInt)) {
			ver = "android1.6";
		} else if ("5".equals(verInt)) {
			ver = "android2.0";
		} else if ("6".equals(verInt)) {
			ver = "android2.0.1";
		} else if ("7".equals(verInt)) {
			ver = "android2.1";
		} else if ("8".equals(verInt)) {
			ver = "android2.2";
		} else if ("9".equals(verInt)) {
			ver = "android2.3.1";
		} else if ("10".equals(verInt)) {
			ver = "android2.3.3";
		} else if ("11".equals(verInt)) {
			ver = "android3.0";
		} else if ("12".equals(verInt)) {
			ver = "android3.1";
		} else {
			ver = "other";
		}
		return ver;
	}

	/**
	 * 获取运营商信息
	 */
	public static String getCarrier(Context context) {
		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telephony.getSubscriberId();
		if (imsi != null && !"".equals(imsi)) {
			if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
				return "ChinaMobile";
			} else if (imsi.startsWith("46001")) {
				return "ChinaUnicom";
			} else if (imsi.startsWith("46003")) {
				return "ChinaTelecom";
			} else {
				return "othes";
			}
		}

		return null;
	}

	/** 获取imsi */
	public static String getImsiInfo(Context context) {
		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telephony.getSubscriberId();
		return imsi;
	}

	/**
	 * 读取手机串号
	 */
	public static String readTelephoneSerialNum(Context con) {
		TelephonyManager telephonyManager = (TelephonyManager) con
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	/**
	 * MD5加密字符串
	 */
	public static String md5(String source) {
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			digest.update(source.getBytes());
			byte[] mess = digest.digest();
			return toHexString(mess);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return source;
	}

	public static String toHexString(byte[] b) { // byte to String
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * sha加密字符串
	 */
	public static String sha(String source) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(source.getBytes());
			byte[] mess = digest.digest();
			return toHexString(mess);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return source;
	}

	/**
	 * 获取路径中的文件名
	 */
	public static String getFileNameFromURL(String url) {
		String fileName = url.substring(url.lastIndexOf("/") + 1);
		return fileName;
	}

	/**
	 * 根据URL得到图片
	 */
	public static Bitmap getBitmapFromUrl(String urlStr) {
		try {
			URL url = new URL(urlStr);
			URLConnection connection = url.openConnection();
			connection.connect();
			InputStream inputStream = connection.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			Bitmap bitmap = BitmapFactory.decodeStream(bis);
			bis.close();
			inputStream.close();
			return bitmap;

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			//
		}

	}

	/** 获取手机的服务中心号码 */
	@SuppressWarnings("deprecation")
	public static String getServiceCenterAddress() {
		String str = "";
		SmsMessage[] messages = new SmsMessage[1];
		SmsMessage message = messages[0];
		if (null != message) {
			str = message.getServiceCenterAddress();
			if (str == null || "".equals(str)) {
				str = "";
			}

		}
		return str;
	}

	/**
	 * 保存文件到指定路径，后缀名修改为指定的后缀postfix
	 */
	public static void saveImageToSDAndChangePostfix(Bitmap bitmap,
			String path, String fileName, String postfix) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String name = fileName.substring(0, fileName.lastIndexOf("."));
		File imageFile = new File(path + name + postfix);
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bos = null;
		try {
			fileOutputStream = new FileOutputStream(imageFile);
			bos = new BufferedOutputStream(fileOutputStream);
			if (bitmap != null) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.flush();
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 保存文件到指定路径，后缀名不修改
	 */
	public static void saveImageToSD(Bitmap bitmap, String path, String fileName) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File imageFile = new File(path + fileName);
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(imageFile);
			BufferedOutputStream bos = new BufferedOutputStream(
					fileOutputStream);
			if (bitmap != null) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
				bos.flush();
				bos.close();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 从asset中得到source id
	 */
	public static String getSourceIdFromAsset(Context context) {
		AssetManager assetManager = context.getAssets();
		InputStream inputStream = null;
		ByteArrayOutputStream baos = null;
		try {
			inputStream = assetManager.open("sourid.txt");
			baos = new ByteArrayOutputStream();
			byte buf[] = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}

			return baos.toString();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * 判断网络是否可用̬
	 */
	public static boolean isAccessNetwork(Context context) {
		boolean bExistNetwork = isAccessNetwork_hard(context);
		if (!bExistNetwork) {
			bExistNetwork = isMobileConnected(context);
		}
		return bExistNetwork;
	}
	
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (null != mConnectivityManager) {
				NetworkInfo mWiFiNetworkInfo = mConnectivityManager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (mWiFiNetworkInfo != null && mWiFiNetworkInfo.isAvailable()) {
					return mWiFiNetworkInfo.isConnectedOrConnecting();
				}
			}
		}
		return false;
	}
	
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (null != mConnectivityManager) {
				NetworkInfo mMobileNetworkInfo = mConnectivityManager
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (mMobileNetworkInfo != null && mMobileNetworkInfo.isAvailable()) {
					return mMobileNetworkInfo.isConnectedOrConnecting();
				}
			}
		}
		return false;
	}
	
	/***
	 * 宽松判断网络连接
	 * @param context
	 * @return
	 */
	public static boolean isAccessNetwork_loose(Context context) {
		boolean bExistNetwork = false ;
		
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] networkInfos = connectivity.getAllNetworkInfo();
		
		for( int i = 0 ; i < networkInfos.length ; i ++ )
		{
			if( null != networkInfos[i] && networkInfos[i].isAvailable() )
			{
				bExistNetwork = true ;
				break ;
			}
		}
		
		return bExistNetwork;
	}
	
	/***
	 * 严格判断网连接
	 * @param context
	 * @return
	 */
	public static boolean isAccessNetwork_hard(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeInfo = connectivity.getActiveNetworkInfo();
		if (activeInfo != null && activeInfo.isAvailable()
				&& activeInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	/**
	 * 获取网络类型信息
	 */
	public static String getAccessNetworkType(Context context) {
		int type = 0;
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info == null) {
			return "";
		}
		type = info.getType();
		if (type == ConnectivityManager.TYPE_WIFI) {
			return "wifi";
		}
		if (type == ConnectivityManager.TYPE_MOBILE) {
			type = telephonyManager.getNetworkType();
			if (type == TelephonyManager.NETWORK_TYPE_CDMA
					|| type == TelephonyManager.NETWORK_TYPE_GPRS) {
				return "2G";
			} else if (type == TelephonyManager.NETWORK_TYPE_EDGE) {
				return "2G";
			} else if (type == TelephonyManager.NETWORK_TYPE_UMTS
					|| type == TelephonyManager.NETWORK_TYPE_EVDO_0
					|| type == TelephonyManager.NETWORK_TYPE_EVDO_A) {
				return "3G";
			} else {
				return "其它";
			}
		}

		return null;
	}

	/**
	 * 获取SD卡剩余空间，无卡则返回-1
	 */
	public static long getSDSize() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			String path = Environment.getExternalStorageDirectory().getPath();
			StatFs statFs = new StatFs(path);
			long l = statFs.getBlockSize();
			return statFs.getAvailableBlocks() * l;
		}

		return -1;
	}

	/**
	 * 获取当前时间
	 */
	public static String getNowTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(Calendar.getInstance().getTime());
	}

	/**
	 * 清理緩存
	 * 
	 * @return
	 */
	public static long clearFile(String catchFiles) {
		File file = new File(catchFiles);
		long time = 0;
		if (!file.exists()) {
			return 0;
		}
		
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (int i = 0; i<files.length; i++) {
					clearFile(files[i].getAbsolutePath());
				}
			}
			
		} else {
			long curTime = System.currentTimeMillis();
			time = file.lastModified();
			if (curTime - time > 2 * 24 * 60 * 60 * 1000) {
				file.delete();
			}
		}
		return time;
	}

	/**
	 * 判断缓存是否过期
	 * 
	 * @return
	 */
	public boolean isExpired(File file, long cacheTime) {
		if (file.exists()) {
			return (file.lastModified() + cacheTime * 1000) < System
					.currentTimeMillis();
		}
		return true;
	}

	/**
	 * 删除指定路径的文件
	 * 
	 */
	public static void deleteAllFiles(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return;
		}

		if (file.isDirectory()) {
			File[] files = file.listFiles();
			int len = 0;
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					deleteAllFiles(files[i].getPath());

				}
			}
		} else {
			file.deleteOnExit();
		}

	}

	/**
	 * 网络不可用提示对话框
	 */
	public static void netErrorToBack(final Context con)
	{
		//netErrorToBack(con, true);
		
		netErrorToToast(con);
	}
	
	
	public static void netErrorToToast(final Context con) {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(con, "来福提示:网络无法连接，请检查您的网络.", Toast.LENGTH_SHORT)
						.show();
				Looper.loop();
			}
		}.start();
	}
	
	
	public static void netErrorToBack(final Context con, final boolean bFinishCurActivity) {
		
		netErrorToToast(con);
		return ;
		/*
		AlertDialog.Builder adb = new AlertDialog.Builder(con);
		adb.setTitle("小美提示");
		adb.setMessage("网络无法连接，请检查您的网络后再试.");
		adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (bFinishCurActivity) {
					dialog.dismiss();
					((Activity) con).finish();
				} else {
					dialog.dismiss();
				}
			}
		});
		adb.show();
		*/
	}
	

	/**
	 * 获得文件夹的大小
	 * 
	 * @param cacheFile
	 *            要获得文件的大小
	 * @return 文件的大小
	 */
	protected long getFileSize(File cacheFile) {
		long size = 0;
		File flist[] = cacheFile.listFiles();
		if (flist != null) {
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size = size + getFileSize(flist[i]);
				} else {
					size = size + flist[i].length();
				}
			}
		}
		return size;
	}

	/**
	 * 删除文件夹下的所有内容
	 * 
	 * @param cacheFile
	 */
	protected void deleteFiles(File cacheFile) {
		File flist[] = cacheFile.listFiles();
		if (flist != null) {
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					deleteFiles(flist[i]);
				} else {
					flist[i].delete();
				}
			}
		}
	}


	/**
	 * 判断当前运行环境是否为模拟器
	 * 
	 * @return true 当前设备为模拟器 false 当前设备为真机
	 */
	public static boolean isEmulator() {
		return ("unknown".equals(Build.BOARD))
				&& ("generic".equals(Build.DEVICE))
				&& ("generic".equals(Build.BRAND));
	}

	/**
	 * 获取设备编号， 若获取不到，用 android_id 代替。
	 * 
	 * @param context
	 * @return null 若为模拟器或无法获取 模拟器也可能反回全0字符串
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telManager.getDeviceId();
		if (imei != null) {
			return imei;
		}
		String dv;
		if ((dv = Settings.Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID)) != null) {
			dv = md5(dv).toUpperCase();
		}
		return dv;
	}

	/**
	 * 获取 meta 中设定的值 例： <code>
	 * <meta-data android:name="keyName"
	 * android:value="0123456789abc" />
	 * </code>
	 * 
	 * @param context
	 * @param keyName
	 *            name of meta-data
	 * @return value of meta-data null 没有获取到
	 */
	public static String readMetaByKey(Context context, String keyName) {
		try {
			ApplicationInfo appi = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			Bundle bundle = appi.metaData;
			return (String) bundle.get(keyName);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 检查指定的字符串是否为空。
	 * <ul>
	 * <li>Util.isEmpty(null) = true</li>
	 * <li>Util.isEmpty("") = true</li>
	 * <li>Util.isEmpty("   ") = true</li>
	 * <li>Util.isEmpty("abc") = false</li>
	 * </ul>
	 * 
	 * @param value
	 *            待检查的字符串
	 * @return true/false
	 */
	public static boolean isEmpty(String value) {
		int strLen;
		if (value == null || (strLen = value.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(value.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static String urlGet(String url) throws IOException {
		URLConnection conn = new URL(url).openConnection();
		conn.setConnectTimeout(20000);
		InputStream input = conn.getInputStream();
		String result = inputStreamToString(input);
		return result;
	}

	public static String urlPost(String url, String encodePostParam)
			throws IOException {
		HttpURLConnection conn;
		conn = (HttpURLConnection) new URL(url).openConnection();
		return urlPost(conn, encodePostParam);
	}

	public static String urlPost(HttpURLConnection conn, String encodePostParam)
			throws IOException {
		int responseCode = -1;
		OutputStream osw = null;
		try {
			conn.setConnectTimeout(20000);
			conn.setReadTimeout(12000);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setDoOutput(true);
			byte[] bytes = encodePostParam.getBytes("UTF-8");
			conn.setRequestProperty("Content-Length",
					Integer.toString(bytes.length));
			osw = conn.getOutputStream();
			osw.write(bytes);
			osw.flush();
			responseCode = conn.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				throw new IOException("请求错误");
			} else {
				String s = inputStreamToString(conn.getInputStream());
				return s;
			}
		} finally {
			try {
				if (osw != null)
					osw.close();
			} catch (Exception ignore) {
			}
		}
	}

	/**
	 * 将inputStream 以系统默认编码转换为字符串
	 * 
	 */
	public static String inputStreamToString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line = null;
		byte arr[] = new byte[1024];
		int len = 0;
		try {
			while ((len = is.read(arr)) != -1) {
				line = new String(arr, 0, len);
				sb.append(line);
			}
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// do nothing
			}
		}
		return sb.toString();
	}

	/**
	 * 将Assists 中文件读取为字符串
	 */
	public static String getAssistFileAsString(Context context,
			String assistFile) throws IOException {
		String result = null;
		AssetManager assetManager = context.getAssets();
		result = inputStreamToString(assetManager.open(assistFile));
		return result;
	}

	/**
	 * 检查邮箱格式
	 */
	public static boolean isEmail(String emailAddress) {
		String strPattern = "^[a-zA-Z0-9][ \\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";

		// 利用了Java里面的正则表达式
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(emailAddress);
		// 当目标string与传入的string完全匹配，返回true,否则返回false
		return m.matches();
	}

	/**
	 * 检查手机格式
	 */
	public static boolean isCellPhoneNumber(String cellPhoneNumber) {
		// 手机号码验证,11位，不知道详细的手机号码段，只是验证开头必须是1和位数
		String strPattern = "^[1][\\d]{10}";
		// 利用了Java里面的正则表达式
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(cellPhoneNumber);
		// 当目标string与传入的string完全匹配，返回true,否则返回false
		return m.matches();
	}

	/*
	 * 保存文件到指定路径，后缀名不修改
	 */
	public static void saveImageToCache(Context context, Bitmap bitmap,
			String fileName) {
		File dir = new File(context.getCacheDir(), fileName);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File imageFile = new File(context.getCacheDir() + fileName);
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(imageFile));
			if (bitmap != null) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				bos.flush();
				bos.close();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void deleteFile(File file) {
		if (!file.exists()) {
			return;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File dirFile : files) {
				deleteFile(dirFile);
			}
		} else {
			if (file.exists()) {
				file.delete();
			}
		}
	}
	
	/**
	 * 开启关闭GPS定位
	 * 
	 * */
	public static boolean openOrCloseGPS(Context context) {
		boolean isOpen = false;
		LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 10,locationListener);
		getcity(locationManager, context);
//		List <Address> addList = null; 
		return isOpen;
	}
	static LocationListener locationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public static void getcity(LocationManager locationManager, Context context) {
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		String lat = Double.toHexString(location.getLatitude());
		String lon = Double.toHexString(location.getLongitude());
		Geocoder geocoder = new Geocoder(context);
		try {
			List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			System.out.println("ddddd");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
