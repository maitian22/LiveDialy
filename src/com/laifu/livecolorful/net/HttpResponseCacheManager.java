package com.laifu.livecolorful.net;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.os.Environment;
import android.os.StatFs;

/**
 * @author liming.jumei 创建日期：2012-4-26 类名：HttpResponseCacheManager
 * 功能:可以查询及存储HTTP请求和回应 . 根据http url 及 请求参数做为 hash key ,回应的 response 回应时间 做为 hash value . 
 * 备注:处理每次都要从服务器获取JSON数据导致android 应用界面每次都重新刷新问题
 */
public class HttpResponseCacheManager {	
 
	private static HttpResponseCacheManager _instance = new HttpResponseCacheManager(100);

	/** 缓存的文件夹路径 */
	public static final String CACHE_HttpResponse_PATH = "/laifu/cache/HttpResponse/";

	/** 缓存的绝对路径 */
	public String mCachePath = "";

	/** 缓存集合的最大容量 */
	private int mCapacity = 0;

	/** 存放缓存的集合，Value 为受软引用管理的 Bitmap */
	private Map<String, HttpResponseInfo> mHttpCache = null;

	private HttpResponseCacheManager(int capacity) {
		mCapacity = capacity;
		mHttpCache = new ConcurrentHashMap<String, HttpResponseInfo>(
				capacity);	
		mCachePath = Environment.getExternalStorageDirectory().getPath()
				+ CACHE_HttpResponse_PATH;
	}
	

    public static HttpResponseCacheManager getInstance(){
              return _instance ;
    }

	
	public void RemoveHttpResponseInfo(String strKey) {
		try {
			String imgName = strKey;
			if (sdCardMounted()) {
				File txtFile = new File(mCachePath + DefaultTools.md5(imgName));
				if (!txtFile.exists()) {
					return;
				} else {
					txtFile.delete();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	public void SaveHttpResponseInfo(HttpResponseInfo Info)
	{
		String strKey = Info.getResponseKey();
	
		if( mHttpCache.containsKey(strKey) ){
			mHttpCache.remove(strKey);		
			mHttpCache.put(strKey, Info);
		}else{
			mHttpCache.put(strKey, Info);
		}
		
		String strRet = saveHttpResponseInfoToSdCard(Info);	
	}

	public HttpResponseInfo LoadHttpResponseInfo(String strKey,
			long maxCacheTimeSecond) {

		HttpResponseInfo info;
		//HttpResponseInfo data = mHttpCache.get(strKey);
		HttpResponseInfo data = loadHttpResponseInfoFromSdCard(strKey);
		if (null != data) {
			long nMillsecond = Calendar.getInstance().getTime().getTime() -
					data.getResponseDate().getTime() ;
			if ( (nMillsecond / 1000 ) < maxCacheTimeSecond + 1) {
				info = new HttpResponseInfo(data.getResponseKey(),
						data.getResponseData(),
						data.getResponseDate());
			} else {
				info = null;
			}
		} else {
			info = null;
		}
		return info;
	}
	
	
	public String  GetHashKey(String strUrl ,String strPostData, String strHttpHead)
	{
		String strRet = "";
		if( null != strUrl && null != strPostData ){
		strRet = strUrl + "|" + strPostData + "|" + strHttpHead;
		}		
		return strRet;
	}
	
	
	private HttpResponseInfo loadHttpResponseInfoFromSdCard(String strKey) {
	
		HttpResponseInfo info = null ;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			String imgName = strKey;
			if (sdCardMounted()) {
				File txtFile = new File(mCachePath + DefaultTools.md5(imgName));
				if (!txtFile.exists()) {
					return null;
				}
				fis = new FileInputStream(txtFile);
				ois = new ObjectInputStream(fis);
				HttpResponseInfo data = (HttpResponseInfo) ois.readObject();				
				info = data ;
			}			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
					ois = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
			
		return info;
	}
	
	
	private String saveHttpResponseInfoToSdCard(HttpResponseInfo Info) {
		if (!sdCardMounted()) {
			return "Memory card not found";
		}
		if (getAvailableSdCardSize() < Info.getResponseData().length() * 10 ) {
			return "Not enough memory";
		}
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			File dir = new File(mCachePath);
			if (!dir.exists()) {
				// LogManager.info(mCachePath);
				if (!dir.mkdirs()) {
					// LogManager.error("Failed to create dir ! ");
				}
			}
			
			String fileName = Info.getResponseKey();			
			File txtFile = new File(dir, DefaultTools.md5(fileName));		
			if (txtFile.exists()) {
				txtFile.delete();
			}		     
			
			if (txtFile.createNewFile()) {
				fos = new FileOutputStream(txtFile);
				//fos.write(Info.getResponseData().getBytes());
	            oos = new ObjectOutputStream(fos);
	            oos.writeObject(Info);	
			
			} else {
				// LogManager.error("Failed to create file ! ");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "Save failed";
		} catch (IOException e) {
			e.printStackTrace();
			return "Save failed";
		} finally {
			try {
				if (oos != null) {
					oos.flush();
					oos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return "Save failed";
			}

			
		}
		return "Save successfully";
	}
	
	/**
	 * 将流转成字节数组
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private byte[] stream2Bytes(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = is.read(buffer, 0, 1024)) != -1) {
			baos.write(buffer, 0, length);
		}
		baos.flush();
		return baos.toByteArray();
	}

	
	
	/**
	 * 获得sd卡的可用空间
	 * 
	 * @return 可用字节数
	 */
	private long getAvailableSdCardSize() {
		String path = Environment.getExternalStorageDirectory().getPath();
		StatFs statFs = new StatFs(path);
		long blockSize = statFs.getBlockSize();
		int available = statFs.getAvailableBlocks();
		return available * blockSize;
	}

	

	/**
	 * 检查是否安装了sd卡
	 * 
	 * @return false 未安装
	 */
	private boolean sdCardMounted() {
		final String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)
				&& !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			return true;
		}
		return false;
	}


}