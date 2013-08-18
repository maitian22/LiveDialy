/**  
* Filename:    DefaultJSONData.java     
* Copyright:   Copyright (c)2011 
* @author:     zoutiao  
* @version:    1.0
* Create at:   2011-5-25 ����05:17:22  
*  
* Modification History:  
* Date         Author      Version     Description  
* ------------------------------------------------------------------  
* 2011-5-25    zoutiao             1.0        1.0 Version  
*/   
package com.laifu.liveDialy.parser;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 该接口提供解析json格式数据的抽象方法，类似于解析xml的{@link #DefaultHandler}
 * @author zoutiao
 *
 */
public interface DefaultJSONData {
	
	/**
	 * 解析json数组
	 *
	 */
	public abstract void parse(JSONArray array) ;
	
	/**
	 * 解析json对象
	 * 
	 */
	public abstract void parse(JSONObject object) ;
}
