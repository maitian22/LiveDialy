package com.laifu.livecolorful.net;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date; 

/**
 * @author liming.jumei
 * 创建日期：2012-4-26
 * 类名：HttpResponseInfo.java
 * 备注：
 */
public class HttpResponseInfo  implements Serializable {
	
	private String sKey = "";    
	  
	 /** http回应包  */
    private String sResponseData = "";    
  
    /** 取得http回应包的时间  */
    private Date dtGetResponseDate = Calendar.getInstance().getTime();

    public HttpResponseInfo() {       
        dtGetResponseDate = Calendar.getInstance().getTime();
    }  
    
    public HttpResponseInfo(String strKey ,String strResponseData) {
    	this.sKey = strKey ;
        this.sResponseData = strResponseData;
        dtGetResponseDate = Calendar.getInstance().getTime();  
    }  
    
    public HttpResponseInfo(String strKey ,String strResponseData, Date date) {
    	this.sKey = strKey ;
        this.sResponseData = strResponseData;
        this.dtGetResponseDate = date ;            
    }  

    public String getResponseKey() {
        return sKey;
    }

    public String getResponseData() {
        return sResponseData;
    }

    public Date  getResponseDate()
    {
    	return dtGetResponseDate;
    }

}
