package com.laifu.livecolorful.net;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.os.Environment;

import com.laifu.livecolorful.parser.DefaultJSONData;

/**
 * @author liming.jumei 创建日期：2012-4-26 类名：HttpResponseCacheManager
 *         功能:可以查询及存储HTTP请求和回应 . 根据http url 及 请求参数做为 hash key ,回应的 response 回应时间
 *         做为 hash value . 备注:处理每次都要从服务器获取JSON数据导致android 应用界面每次都重新刷新问题
 */
public class DefaultJSONDataCacheManager {

	private static DefaultJSONDataCacheManager _instance = new DefaultJSONDataCacheManager(
			50);

	/** 缓存的文件夹路径 */
	public static final String CACHE_HttpResponse_PATH = "/laifu/cache/DefaultJSONDataCacheManager/";

	/** 缓存的绝对路径 */
	public String mCachePath = "";

	/** 缓存集合的最大容量 */
	private int mCapacity = 0;

	/** 存放缓存的集合，Value 为受软引用管理的 Bitmap */
	private Map<String, DefaultJSONDataCache> mHttpCache = null;

	private DefaultJSONDataCacheManager(int capacity) {
		mCapacity = capacity;
		mHttpCache = new ConcurrentHashMap<String, DefaultJSONDataCache>(
				capacity);
		mCachePath = Environment.getExternalStorageDirectory().getPath()
				+ CACHE_HttpResponse_PATH;
	}

	public static DefaultJSONDataCacheManager getInstance() {
		return _instance;
	}
	
	public void RemoveDefaultJSONData(String strKey) {
		if (mHttpCache.containsKey(strKey)) {
			mHttpCache.remove(strKey);
		}
	}

	public void SaveDefaultJSONData(String strKey, DefaultJSONData Info) {

		DefaultJSONDataCache data = new DefaultJSONDataCache(Info);
		if (mHttpCache.containsKey(strKey)) {
			mHttpCache.remove(strKey);
			mHttpCache.put(strKey, data);
		} else {
			mHttpCache.put(strKey, data);
		}
	}


	/*
	 * returnvalue DefaultJSONData 需要的数据
	 * parm strKey hashkey 
	 * parm maxCacheTimeSecond 缓存的最大过期时间,以秒计数 超过最大的时间即返回null
	 */

	public DefaultJSONData LoadDefaultJSONData(String strKey,
			long maxCacheTimeSecond) {
		DefaultJSONData info;
		DefaultJSONDataCache data = mHttpCache.get(strKey);
		if (null != data) {
			
			long nMillsecond = Calendar.getInstance().getTime().getTime() -
					data.getDate().getTime() ;
			if ( (nMillsecond / 1000 ) < maxCacheTimeSecond + 1) {			
				info = data.getJSONData();
			} else {
				info = null;
			}
		} else {
			info = null;
		}
		return info;
	}
	
	


	public String GetHashKey(String path, Map<String, String> param , String strExtKey) {
		String strRet = "";
		if (null != path && null != param) {
			strRet = path + "|" + param.toString() + "|" + strExtKey;
		}
		return strRet;
	}

}
