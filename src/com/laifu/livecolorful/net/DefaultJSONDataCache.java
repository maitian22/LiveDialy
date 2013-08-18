package com.laifu.livecolorful.net;

import java.util.Calendar;
import java.util.Date;

import com.laifu.livecolorful.parser.DefaultJSONData;


/**
 * @author liming.jumei 创建日期：2012-5-11 类名：DefaultJSONDataCache
 * 功能:对  DefaultJSONData 对进包装,可以保存 DefaultJSONData 缓存的时间 ,调用者可以根据时间来判断来是否使用此缓存
 * 备注:
 */
public class DefaultJSONDataCache {
	
	private Date dtNow ;
	
	private DefaultJSONData mData ;
	
	public DefaultJSONDataCache(DefaultJSONData data)
	{
		this.dtNow = Calendar.getInstance().getTime();
		this.mData = data ;
	}
	
	
	public Date getDate()
	{
		return this.dtNow ;
	}
	
	public void setDate(Date dt)
	{
		this.dtNow = dt ;
	}
	
	public DefaultJSONData getJSONData()
	{
		return this.mData ;
	}
	
	public void setJSONData(DefaultJSONData data)
	{
		this.mData = data ;
	}
	
  
}
