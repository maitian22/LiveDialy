/**
 * 
 */
package com.laifu.livecolorful.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.util.Log;


/**
 * @author wangfang
 * 创建日期：2012-3-13
 * 类名：CurrentTime.java
 * 备注：
 */
public class CurrentTime {
	private static long startTime;
	private static long endTime;
	private static long showTime;
	private static String title;
	/**
	 * 在想要获取程序片段之前调用这个方法（获取开始时间）
	 * @param title 为程序段起名字标记一下，容易辨认（比如什么类什么功能）
	 */
    public static void startTime(String title){
    	startTime = System.currentTimeMillis();
    	CurrentTime.title = title;
    }
    /**
     * 在想要获取程序片段之后调用这个方法（获取结束时间及输出结果）
     */
    public static void endTime(){
    	endTime = System.currentTimeMillis();
    	Log.i("currentTime",title+"程序运行时间： "+(endTime-startTime)+"ms");
    }
    
    public static void showTime(String title){
    	showTime = System.currentTimeMillis();
    	Log.i("currentTime",title+"网络请求结束，界面显示时间 ： "+(showTime-endTime)+"ms");
    }
    /**
     * 把日志输入到指定路径指定文件中，filepath是指定路径，filename是文件名
     * @param filepath 指定路径（日志保存的路径）
     * @param filename 文件名（日志保存在什么文件中，例如：log.txt）
     */
    public static void getLog(final String filepath,final String filename){
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Process mLogcatProc = null;  

				BufferedReader reader = null;  

				try {  

				        //获取logcat日志信息  （过滤信息）

				    mLogcatProc = Runtime.getRuntime().exec(new String[] { "logcat","-d","-v","time","-s","currentTime:I"});  

				    reader = new BufferedReader(new InputStreamReader(mLogcatProc.getInputStream()));  
                    
				    File files = new File(filepath);
				    if(!files.exists()){
				    	files.mkdirs();
				    }
				    
				    File file = new File(filepath+filename);
				    
				    OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file));
				    String line;  

				    while ((line = reader.readLine()) != null) {  
				            //logcat打印信息在这里可以监听到    
                          write.write(line+"\r\n");

				    }  
				    write.flush();

				} catch (Exception e) {  

				    e.printStackTrace();  

				}  


				
			}
		}).start();
    }
}

