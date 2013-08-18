package com.laifu.livecolorful.tool;

import java.util.ArrayList;
import java.util.HashMap;

import com.laifu.livecolorful.parser.DefaultJSONData;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;

/**
 * 
 * @author 类名：Constant.java 备注：常量类
 */
public class Constant {

	public static String URL_PREFIX_NormalValue = "http://api.laifu.fm/";

	public static String URL_PREFIX_TestValue = "http://api.sandbox.laifu.fm/";

	/** 请求的路径 */
	public static String URL_PREFIX = URL_PREFIX_TestValue;

	/** 是否为开发阶段 */
	public final static boolean IS_DEBUG = true;

	/** 版本号 */
	public final static String VER = "ANDROID V1.0";
	/** 服务器接口协议版本号,一般与客户端版本号一致 */
	public static final String CLIENTVER = "1.0";

	// 传回服务器HTTP头名字 开始
	public static final String CLIENT_OS = "platform_v"; // 手机操作系统版本

	public static final String PLATFORM = "platform";
	public static final String CLIENT = "client_v";
	public static final String MODEL = "model";
	public static final String IMSI = "imsi";
	public static final String IMEI = "imei";
	public static final String SOURCE = "source";
	public static final String CARRIER = "operator";
	public static final String CONTENT_TYPE = "content_type";
	public static final String LANGUAGE = "language";;
	public static final String SMSCENTER = "smscenter";
	public static final String HTTPHEAD = "httphead";
	public static final String JUMEI_PRODUCT = "product";

	/** 用户 hash 串 */
	public static final String ACCOUNT = "account";
	/** 用户手机屏幕分辨率 */
	public static final String RESOLUTION = "resolution";
	/** 用户昵称 */
	public static final String NICKNAME = "nickname";
	/** 从上次的请求中获得，需回传给server */
	public static final String PHPSESSID = "PHPSESSID";
	/** 服务器分发所需变量 */
	public static final String JUMEI_JHC = "JHC";

	public static final String TK = "tk";
	// 传回服务器HTTP头名字 结束

	public static final String COOKIE_VER = "cookie_ver";

	public static final String COOKIE = "Cookie";
	public static final String UID = "uid";

	public static final String CLIENT_OS_VALUE = android.os.Build.VERSION.RELEASE;
	public static final String MODEL_VALUE = android.os.Build.MODEL;
	public static String SOURCE_VALUE = "androiddefault";

	public static final boolean isUseGZIP = true;

	// Http请求头信息字段
	public static final String UDID = "udid";

	// 缓存文件的最大值
	public static final long CACHEMAXSIZE = 10 * 1024 * 1024;

	// 用户信息
	public static final String USERINFO_SHAREPREFENCE = "user_shareprefence";
	public static final String ID="id";
	public static final String BIRTHDAY="birthday";
	public static final String SEX="sex";
	public static final String USERNAME="username";
	public static final String REGESITER_TIME="regesiter_time";
	public static final String COVER_LARGE="cover_large";
	public static final String NICK="nick";
	public static final String AGE="age";
	public static final String SIGNATURE="signature";
	public static final String AVATAR_LARGE="avatar_large";
	public static final String CITY="city";
	public static final String AVATAR_SMALL="avatar_small";


	// 开关类设置配置文件名
	public static final String ALARM = "alarm";

	// 分享设置
	public static final String SharedSet_Flag = "SharedSet_flag";

	// 图片设置
	public static final String SHOWPIC_FLAG = "showpic_flag";
	public static boolean SHOWPIC_Value = true;

	// push相关
	public static final String PUSH_TITLE = "push_title";
	public static final String PUSH_MESSAGE = "push_message";
	public static final String PUSH_FLAG = "push_flag";

	public static final String IMG = "img";
	public static final String IMGURL = "imgurl";

	// sharePreference name
	public static final String HEAD_PORTRAIT_PATH = "head_protrait_path";
	public static final String PICTURE_COVER_PATH = "picture_cover_path";
	public static final String CURRENT_THIRD_ACTIVITY_PAGE = "current_third_activity_page";
	public static final String PICTURE_CAPTURE_IMAGE_NAME = "pictempName";

	public static final int HEAD_PIC_CAPTURE = 1;
	public static final int PICTURE_COVER_CAPTUIE = 2;
	public static final int RESULT_PICTURE_COVER = 3;
	public static final int PICTURE_HEAD_CAPTUIE = 4;
	public static final int RESULT_PACTURE_HEAD = 5;

	public static final String PitcturPath = Environment
			.getExternalStorageDirectory().getPath() + "/LiveColorful/Photo";
	
//	public static final String APP_ID_FOR_QQ = "100482340";
	public static final String APP_ID_FOR_QQ = "100496321";
}
