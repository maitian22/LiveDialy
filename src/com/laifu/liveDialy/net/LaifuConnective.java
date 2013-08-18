package com.laifu.liveDialy.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.laifu.liveDialy.parser.DefaultJSONData;
import com.laifu.liveDialy.tool.Constant;
import com.laifu.liveDialy.tool.Tools;
import com.laifu.liveDialy.tools.CurrentTime;

/**
 * 类名：JuMeiConnective.java 备注：联网方法
 */
public class LaifuConnective {
	/** 是否调试 */
	// private static final boolean debug = true;

	/** 网络连接超时时间，默认是15S */
	public static int TIMEOUT_TIME = 15000;

	/** 传给服务器的编码格式,默认是UTF-8 */
	public static String encoding = HTTP.UTF_8;

	public static final long cashTimeout = 30 * 60 * 1000;// 内存缓存时间最长30分钟

	public static final long cashSDTimeout = 5 * 24 * 60 * 60 * 1000;// sd卡缓存时间最长5天

	public static final String POST = "post";

	public static final String GET = "get";

	public static final String LAIFU_TOKEN = "token";

	public static final String KEY = "b5f07bc7ce6957142d7bec47cbb6aded";

	public static final String TAG = "LaifuConnective";

	private static Lock lockPostRequestAndParseByMemCache = new ReentrantLock();

	/****
	 * 
	 * @param context
	 *            上下文
	 * @param url 
	 * 			     网址参数
	 * @param map
	 *            传的参数
	 * @param jsonData
	 *            解析的对象
	 * @param list
	 * 			  存放解析对象，主要用于当取内存缓存的时候 解析对象传不出来的问题，放到list里面就可以直接获取了
	 * @param isUseCash
	 *            使用内存缓存
	 * @param isUseSDcash
	 *            使用sd卡缓存
	 * @param type
	 *            post还是get
	 * @return 1为成功 100开头服务器连接错误，200开头联网错误没网，300开头解析错误
	 * 
	 */
	public static int getServiceHttpData(Context context, String url, HashMap<String, String> map, DefaultJSONData jsonData, ArrayList<DefaultJSONData> list, boolean isUseCash,
			boolean isUseSDcash, String type) {
		int result = 0;
		if (context == null) {
			return result;
		}

		DefaultJSONData tempJson = null;

		lockPostRequestAndParseByMemCache.lock();

		SharedPreferences preferencesHTTPHEAD = context.getSharedPreferences(Constant.HTTPHEAD, Context.MODE_PRIVATE);
		String laifutoken = preferencesHTTPHEAD.getString(LAIFU_TOKEN, "");
		if (!"".equals(laifutoken)) {
			map.put(LAIFU_TOKEN, laifutoken);
		}

		String strKey = DefaultJSONDataCacheManager.getInstance().GetHashKey(url, map, preferencesHTTPHEAD.getString("token", ""));

		Log.i(TAG, "strKey-->" + strKey);
		try {
			if (isUseCash) {
				DefaultJSONData temp = DefaultJSONDataCacheManager.getInstance().LoadDefaultJSONData(strKey, cashTimeout);
				if (null != temp) {
					tempJson = temp;
					result = 1;
					list.add(tempJson);
					return result;
				}
			}

			if (isUseSDcash) {
				HttpResponseInfo responseInfo = HttpResponseCacheManager.getInstance().LoadHttpResponseInfo(strKey, cashSDTimeout);
				if (null != responseInfo) {
					String strResponse = responseInfo.getResponseData();
					result = ParseRespData(strResponse, jsonData);

					if (result == 1) {
						tempJson = jsonData;
						list.add(tempJson);
						return result;
					}
				}
			}

			StringBuffer sResponseBuffer = new StringBuffer();

			result = getFromSever(context, url, map, jsonData, sResponseBuffer, type);

			if (result == 1) {
				HttpResponseCacheManager.getInstance().SaveHttpResponseInfo(new HttpResponseInfo(strKey, sResponseBuffer.toString()));
				DefaultJSONDataCacheManager.getInstance().SaveDefaultJSONData(strKey, jsonData);
				tempJson = jsonData;
				list.add(tempJson);
				return result;
			}

		} finally {
			lockPostRequestAndParseByMemCache.unlock();
		}

		return result;
	}

	private static int getFromSever(Context context, String url, HashMap<String, String> map, DefaultJSONData jsonData, StringBuffer sResponseBuffer, String type) {
		int result = 0;

		String sign = getSign(map);
		map.put("sign", sign);

		if (type.equals(POST)) {
			result = getPost(context, url, map, sResponseBuffer);
			if (result == 1) {
				result = ParseRespData(sResponseBuffer.toString(), jsonData);
			}
		} else if (type.equals(GET)) {
			result = getGet(context, url, map, sResponseBuffer);
			if (result == 1) {
				result = ParseRespData(sResponseBuffer.toString(), jsonData);
			}
		}

		return result;
	}

	public static int getPost(Context context, String url, HashMap<String, String> map, StringBuffer sResponseBuffer) {
		int nRet = 10000;

		StringBuilder sLogBuilder = new StringBuilder();
		StringBuilder sTimeLogBuilder = new StringBuilder();
		StringBuilder sExceptionLogBuilder = new StringBuilder();

		CurrentTime.startTime("请求前网络时间");
		long startTimeHTTP = System.currentTimeMillis();

		// 封装传送的数据
		ArrayList<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
		for (Map.Entry<String, String> m : map.entrySet()) {
			postData.add(new BasicNameValuePair(m.getKey(), m.getValue()));
		}

		Log.d(TAG, url);

		HttpPost httpPost = new HttpPost(url);
		// String strHeader = (new Tools()).createHttpHeaderCookData(context);
		// httpPost.setHeader(Constant.COOKIE, strHeader);
		httpPost.addHeader("User-Agent", Tools.getUseragent(context));
		if (Constant.isUseGZIP) {
			httpPost.addHeader("Accept-Encoding", "GZIP");
		}
		// 设置连接网络超时时间
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_TIME);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_TIME);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
		HttpResponse response = null;

		try {

			nRet = 20000;

			// sLogBuilder.append("--PostRequest:").append(url).append(" |param: ").append(map.toString()).append(" |request use_agent(").append(Tools.getUseragent(context)).append(")");

			Log.i(TAG, sLogBuilder.toString());

			// 对请求的数据进行UTF-8转码
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postData, encoding);
			httpPost.setEntity(entity);

			response = httpClient.execute(httpPost);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				nRet = 1;
			} else {
				nRet = 10000 + response.getStatusLine().getStatusCode();
				httpPost.abort();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			nRet = 20000 + 1;

			sExceptionLogBuilder.append(e.toString());

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			nRet = 20000 + 2;

			sExceptionLogBuilder.append(e.toString());

		} catch (java.net.UnknownHostException e) {
			e.printStackTrace();
			nRet = 20000 + 10;
			sExceptionLogBuilder.append(e.toString());
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			nRet = 20000 + 11;
			sExceptionLogBuilder.append(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			nRet = 20000 + 3;

			sExceptionLogBuilder.append(e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			nRet = 40000 + 3;

			sExceptionLogBuilder.append(e.toString());
		}

		long endTimeHTTP = System.currentTimeMillis();

		// 响应正常
		if (1 == nRet) {
			try {
				HttpEntity httpEntity = response.getEntity();
				InputStream inputStream = httpEntity.getContent();

				if (null != httpEntity.getContentEncoding() && null != httpEntity.getContentEncoding().getValue()) {
					if (httpEntity.getContentEncoding().getValue().toUpperCase().contains("GZIP")) {
						inputStream = new GZIPInputStream(inputStream);
					}
				}

				// StringBuffer buff = new StringBuffer();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				String temp = null;
				while ((temp = reader.readLine()) != null) {
					sResponseBuffer.append(temp);
				}

				if (null != inputStream) {
					inputStream.close();
				}

				Log.e(TAG, sResponseBuffer.toString() + "");

				nRet = 1;// 解析成功

			} catch (IllegalStateException e) {
				e.printStackTrace();
				nRet = 20000 + 4;

				sExceptionLogBuilder.append(e.toString());
			} catch (IOException e) {
				e.printStackTrace();
				nRet = 20000 + 5;

				sExceptionLogBuilder.append(e.toString());
			} catch (Exception e) {
				e.printStackTrace();
				nRet = 20000 + 6;

				sExceptionLogBuilder.append(e.toString());
			}
		}

		long endTimeSaveCookie = System.currentTimeMillis();

		CurrentTime.endTime();

		sLogBuilder.append("--PostRequest:").append(url).append(" |param: ").append(map.toString()).append(" |return responseValue(").append(nRet).append(") cookie(")
				.append(") response(").append(sResponseBuffer.toString()).append(") exception(").append(sExceptionLogBuilder.toString()).append(")");

		Log.i(TAG, sLogBuilder.toString());

		sTimeLogBuilder.append("--PostRequest:").append(url).append(" |param: ").append(map.toString()).append(" |return responseValue(").append(nRet).append(")")
				.append("http use(").append(endTimeHTTP - startTimeHTTP).append("ms)").append("http savecookie(").append(endTimeSaveCookie - endTimeHTTP).append("ms)");
		Log.i(TAG, sTimeLogBuilder.toString());

		return nRet;

	}

	public static int getGet(Context context, String url, HashMap<String, String> map, StringBuffer sResponseBuffer) {
		int nRet = 10000;

		StringBuilder sLogBuilder = new StringBuilder();
		StringBuilder sTimeLogBuilder = new StringBuilder();
		StringBuilder sExceptionLogBuilder = new StringBuilder();

		CurrentTime.startTime("请求前网络时间");
		long startTimeHTTP = System.currentTimeMillis();

		url += "?";

		// 封装传送的数据
		for (Map.Entry<String, String> m : map.entrySet()) {
			url += m.getKey() + "=" + m.getValue() + "&";
		}

		url = url.substring(0, url.length() - 1);

		Log.d(TAG, url);

		HttpGet httpGet = new HttpGet(url);
		// String strHeader = (new Tools()).createHttpHeaderCookData(context);
		// httpPost.setHeader(Constant.COOKIE, strHeader);
		httpGet.addHeader("User-Agent", Tools.getUseragent(context));
		if (Constant.isUseGZIP) {
			httpGet.addHeader("Accept-Encoding", "GZIP");
		}
		// 设置连接网络超时时间
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_TIME);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_TIME);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
		HttpResponse response = null;

		try {

			nRet = 20000;

			sLogBuilder.append("--PostRequest:").append(url).append(" |param: ").append(map.toString()).append(" |request use_agent(").append(Tools.getUseragent(context))
					.append(")");

			Log.i(TAG, sLogBuilder.toString());

			// 对请求的数据进行UTF-8转码
			response = httpClient.execute(httpGet);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				nRet = 1;
			} else {
				nRet = 10000 + response.getStatusLine().getStatusCode();
				httpGet.abort();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			nRet = 20000 + 1;

			sExceptionLogBuilder.append(e.toString());

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			nRet = 20000 + 2;

			sExceptionLogBuilder.append(e.toString());

		} catch (java.net.UnknownHostException e) {
			e.printStackTrace();
			nRet = 20000 + 10;
			sExceptionLogBuilder.append(e.toString());
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			nRet = 20000 + 11;
			sExceptionLogBuilder.append(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			nRet = 20000 + 3;

			sExceptionLogBuilder.append(e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			nRet = 40000 + 3;

			sExceptionLogBuilder.append(e.toString());
		}

		long endTimeHTTP = System.currentTimeMillis();

		// 响应正常
		if (1 == nRet) {
			try {
				HttpEntity httpEntity = response.getEntity();
				InputStream inputStream = httpEntity.getContent();

				if (null != httpEntity.getContentEncoding() && null != httpEntity.getContentEncoding().getValue()) {
					if (httpEntity.getContentEncoding().getValue().toUpperCase().contains("GZIP")) {
						inputStream = new GZIPInputStream(inputStream);
					}
				}

				// StringBuffer buff = new StringBuffer();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				String temp = null;
				while ((temp = reader.readLine()) != null) {
					sResponseBuffer.append(temp);
				}

				if (null != inputStream) {
					inputStream.close();
				}

				Log.e(TAG, sResponseBuffer.toString() + "");

				nRet = 1;// 解析成功

			} catch (IllegalStateException e) {
				e.printStackTrace();
				nRet = 20000 + 4;

				sExceptionLogBuilder.append(e.toString());
			} catch (IOException e) {
				e.printStackTrace();
				nRet = 20000 + 5;

				sExceptionLogBuilder.append(e.toString());
			} catch (Exception e) {
				e.printStackTrace();
				nRet = 20000 + 6;

				sExceptionLogBuilder.append(e.toString());
			}
		}

		long endTimeSaveCookie = System.currentTimeMillis();

		CurrentTime.endTime();

		sLogBuilder.append("--PostRequest:").append(url).append(" |param: ").append(map.toString()).append(" |return responseValue(").append(nRet).append(") cookie(")
				.append(") response(").append(sResponseBuffer.toString()).append(") exception(").append(sExceptionLogBuilder.toString()).append(")");

		Log.i(TAG, sLogBuilder.toString());

		sTimeLogBuilder.append("--PostRequest:").append(url).append(" |param: ").append(map.toString()).append(" |return responseValue(").append(nRet).append(")")
				.append("http use(").append(endTimeHTTP - startTimeHTTP).append("ms)").append("http savecookie(").append(endTimeSaveCookie - endTimeHTTP).append("ms)");
		Log.i(TAG, sTimeLogBuilder.toString());

		return nRet;
	}

	/****
	 * 按照key的升序排序 取值，再加上key 然后md5加密
	 * 
	 * @param map
	 * @return
	 */
	private static String getSign(HashMap<String, String> map) {

		ArrayList<String> list = new ArrayList<String>();
		Set<String> keySet = map.keySet();
		for (String ss : keySet) {
			list.add(ss);
		}
		Collections.sort(list);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			sb.append(map.get(list.get(i)));
		}

		long time = System.currentTimeMillis() / 1000;
		map.put("t", time + "");
		sb.append(time);

		sb.append(KEY);

		String sign = DefaultTools.md5(sb.toString());

		Log.i("aa", "sign-->"+sign);
		return sign;
	}

	private static int ParseRespData(String sResponse, DefaultJSONData jsonData) {
		int nRet = 30000;
		try {
			int index = sResponse.indexOf("{");
			sResponse = sResponse.substring(index);
			if (sResponse.startsWith("{")) {
				JSONObject object = new JSONObject(sResponse);
				jsonData.parse(object);
				nRet = 1;

			} else if (sResponse.startsWith("[")) {
				JSONArray object = new JSONArray(sResponse.toString());
				jsonData.parse(object);
				nRet = 1;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			nRet = 30000 + 1;
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			nRet = 30000 + 2;
		} catch (Exception e) {
			e.printStackTrace();
			nRet = 30000 + 3;
		}
		return nRet;
	}

	public static String getPromptMsg(int nResponseValue) {
		String sMsg = "";
		String sErrCode = "(错误代码：" + nResponseValue + ")";
		switch (nResponseValue) {
		case 1:
			sMsg = "获取数据成功";
			break;

		case 10400:
		case 10401:
		case 10402:
			sMsg = "错误的请求.请稍后再试";
			break;
		case 10403:
			sMsg = "您请求服务器过于频繁, 服务器暂时拒绝您访问";
			break;

		case 10404:
			sMsg = "找不到请求的接口地址";
			break;

		case 10405:
			sMsg = "您访问服务器太频繁,服务器暂时禁止您访问";
			break;

		case 10408:
		case 10503:
			sMsg = "服务器请求超时!请稍后再试";
			break;

		case 10500:
		case 10501:
		case 10502:
		case 10504:
		case 10505:
			sMsg = "对不起,服务器正在维护中!请稍后再试";
			// sMsg = "服务器暂时无法连接，请稍后再试";
			break;

		case 20001:
			sMsg = "内容的编码方式不支持";
			break;
		case 20002:
			sMsg = "客户端处理协议出错";
			break;
		case 20003:
			sMsg = "文件处理出错";
			break;
		case 20004:
			sMsg = "状态非法";
			break;
		case 20005:
			sMsg = "读取输出流时文件处理出错";
			break;
		case 20006:
			sMsg = "处理时出现错误";
			break;
		case 20007:
		case 20008:
		case 20009:

			// sMsg = "对不起,请求服务器时出现异常,获取数据失败!请您呆会儿再刷新";
			sMsg = "网络传输数据时出现了错误，请稍后再试";
			break;

		case 20010:
			// sMsg = "对不起,请求服务器时出现异常,获取数据失败!请您呆会儿再刷新";
			sMsg = "域名无法解析，请检查您网络的DNS设置";
			break;
		case 20011:
			// sMsg = "对不起,请求服务器时出现异常,获取数据失败!请您呆会儿再刷新";
			sMsg = "请求服务器超时，请检查您网络";
			break;

		case 30001:
		case 30002:
		case 30003:
		case 30004:
		case 30005:
		case 30006:
		case 30007:
		case 30008:
		case 30009:
		case 30010:
			// sMsg = "数据解析失败，请稍后再试";
			sMsg = "服务器返回数据异常，请稍后再试";
			break;

		case 90001:
		case 90002:
		case 90003:
		case 90004:
		case 90005:
		case 90006:
			sMsg = "参数错误，请稍后再试";
			break;
		default:
			// sMsg = "获取数据失败!请您呆会儿再刷新";
			sMsg = "服务器暂时无法连接，请稍后再试";

			break;
		}

		sMsg = "当前网络异常，请稍后刷新！";

		return sMsg + "\n" + sErrCode;
	}

	public static int RemoveCache(Context context, String path, Map<String, String> param) {

		int nRet = 0;

		SharedPreferences preferencesHTTPHEAD = context.getSharedPreferences(Constant.HTTPHEAD, Context.MODE_PRIVATE);
		String strMemKey = DefaultJSONDataCacheManager.getInstance().GetHashKey(path, param, preferencesHTTPHEAD.getString("token", ""));
		/* preferencesHTTPHEAD.getString(Constant.POSTCODE, "") */

		ArrayList<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
		for (Map.Entry<String, String> m : param.entrySet()) {
			postData.add(new BasicNameValuePair(m.getKey(), m.getValue()));
		}

		String strSDHashKey = HttpResponseCacheManager.getInstance().GetHashKey(path, postData.toString(), preferencesHTTPHEAD.getString("token", ""));
		/* preferencesHTTPHEAD.getString(Constant.POSTCODE, "") */

		lockPostRequestAndParseByMemCache.lock();

		try {
			DefaultJSONDataCacheManager.getInstance().RemoveDefaultJSONData(strMemKey);
			HttpResponseCacheManager.getInstance().RemoveHttpResponseInfo(strSDHashKey);

		} finally {
			lockPostRequestAndParseByMemCache.unlock();
		}

		nRet = 1;

		return nRet;
	}

}
