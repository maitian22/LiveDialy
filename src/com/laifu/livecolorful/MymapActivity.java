package com.laifu.livecolorful;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.laifu.livecolorful.maptool.BMapUtil;

public class MymapActivity extends LiveBaseActivity {
	// 定位相关
		LocationClient mLocClient;
		LocationData locData = null;
		public MyLocationListenner myListener = new MyLocationListenner();
		
		//定位图层
		locationOverlay myLocationOverlay = null;
		//弹出泡泡图层
		private PopupOverlay   pop  = null;//弹出泡泡图层，浏览节点时使用
		private TextView  popupText = null;//泡泡view
		private View viewCache = null;
		
		//地图相关，使用继承MapView的MyLocationMapView目的是重写touch事件实现泡泡处理
		//如果不处理touch事件，则无需继承，直接使用MapView即可
		MyLocationMapView mMapView = null;	// 地图View
		private MapController mMapController = null;

		//UI相关
//		OnCheckedChangeListener radioButtonListener = null;
		Button requestLocButton = null;
		boolean isRequest = false;//是否手动触发请求定位
		boolean isFirstLoc = true;//是否首次定位
		
		/**
		 *  MKMapViewListener 用于处理地图事件回调
		 */
		MKMapViewListener mMapListener = null;
		
		
		int Latitude=0,Longitude=0;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
//	        setContentView(R.layout.activity_locationoverlay);
//	        CharSequence titleLable="定位功能";
//	        setTitle(titleLable);
	        requestLocButton = (Button)findViewById(R.id.button1);
	        OnClickListener btnClickListener = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//手动定位请求
					requestLocClick();
				}
			};
		    requestLocButton.setOnClickListener(btnClickListener);
		    
		    
			//地图初始化
	        mMapView = (MyLocationMapView)findViewById(R.id.bmapView);
	        mMapController = mMapView.getController();
	        
	        mMapController.setZoom(14);
	        mMapController.enableClick(true);
	        mMapView.setBuiltInZoomControls(true);
	        //创建 弹出泡泡图层
	        createPaopao();
	        
	        //定位初始化
	        mLocClient = new LocationClient( this );
	        locData = new LocationData();
	        mLocClient.registerLocationListener( myListener );
	        LocationClientOption option = new LocationClientOption();
	        option.setOpenGps(true);//打开gps
	        option.setCoorType("bd09ll");     //设置坐标类型
	        option.setScanSpan(5000);
	        mLocClient.setLocOption(option);
	        mLocClient.start();
	       
	        //定位图层初始化
			myLocationOverlay = new locationOverlay(mMapView);
			//设置定位数据
		    myLocationOverlay.setData(locData);
		    //添加定位图层
			mMapView.getOverlays().add(myLocationOverlay);
			myLocationOverlay.enableCompass();
			//修改定位数据后刷新图层生效
			mMapView.refresh();
			
			
			
			/**
	    	 *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
	    	 */
	        mMapListener = new MKMapViewListener() {
				@Override
				public void onMapMoveFinish() {
					/**
					 * 在此处理地图移动完成回调
					 * 缩放，平移等操作完成后，此回调被触发
					 */
				}
				
				@Override
				public void onClickMapPoi(MapPoi mapPoiInfo) {
					/**
					 * 在此处理底图poi点击事件
					 * 显示底图poi名称并移动至该点
					 * 设置过： mMapController.enableClick(true); 时，此回调才能被触发
					 * 
					 */
					String title = "";
					if (mapPoiInfo != null){
						title = mapPoiInfo.strText;
						Toast.makeText(MymapActivity.this,title,Toast.LENGTH_SHORT).show();
						mMapController.animateTo(mapPoiInfo.geoPt);
						
						Latitude=mapPoiInfo.geoPt.getLatitudeE6();
						Longitude=mapPoiInfo.geoPt.getLongitudeE6();
						mMapView.getCurrentMap();
					}
				}

				@Override
				public void onGetCurrentMap(Bitmap b) {
					/**
					 *  当调用过 mMapView.getCurrentMap()后，此回调会被触发
					 *  可在此保存截图至存储设备
					 */
					Intent intent=new Intent();
					intent.putExtra("Latitude", Latitude);
					intent.putExtra("Longitude", Longitude);
					intent.putExtra("bitmap", b);
					setResult(100);
				}

				@Override
				public void onMapAnimationFinish() {
					/**
					 *  地图完成带动画的操作（如: animationTo()）后，此回调被触发
					 */
				}
	            /**
	             * 在此处理地图载完成事件 
	             */
				@Override
				public void onMapLoadFinish() {
					Toast.makeText(MymapActivity.this, 
							       "地图加载完成", 
							       Toast.LENGTH_SHORT).show();
					
				}
			};
			mMapView.regMapViewListener(MyApplication.getInstance().mBMapManager, mMapListener);
	    }
	    /**
	     * 手动触发一次定位请求
	     */
	    public void requestLocClick(){
	    	isRequest = true;
	        mLocClient.requestLocation();
	        Toast.makeText(MymapActivity.this, "正在定位……", Toast.LENGTH_SHORT).show();
	    }
	    /**
	     * 修改位置图标
	     * @param marker
	     */
	    public void modifyLocationOverlayIcon(Drawable marker){
	    	//当传入marker为null时，使用默认图标绘制
	    	myLocationOverlay.setMarker(marker);
	    	//修改图层，需要刷新MapView生效
	    	mMapView.refresh();
	    }
	    /**
		 * 创建弹出泡泡图层
		 */
		public void createPaopao(){
			viewCache = getLayoutInflater().inflate(R.layout.custom_text_view, null);
	        popupText =(TextView) viewCache.findViewById(R.id.textcache);
	        //泡泡点击响应回调
	        PopupClickListener popListener = new PopupClickListener(){
				@Override
				public void onClickedPopup(int index) {
					Log.v("click", "clickapoapo");
				}
	        };
	        pop = new PopupOverlay(mMapView,popListener);
	        MyLocationMapView.pop = pop;
		}
		/**
	     * 定位SDK监听函数
	     */
	    public class MyLocationListenner implements BDLocationListener {
	    	
	        @Override
	        public void onReceiveLocation(BDLocation location) {
	            if (location == null)
	                return ;
	            
	            locData.latitude = location.getLatitude();
	            locData.longitude = location.getLongitude();
	            //如果不显示定位精度圈，将accuracy赋值为0即可
	            locData.accuracy = location.getRadius();
	            locData.direction = location.getDerect();
	            //更新定位数据
	            myLocationOverlay.setData(locData);
	            //更新图层数据执行刷新后生效
	            mMapView.refresh();
	            //是手动触发请求或首次定位时，移动到定位点
	            if (isRequest || isFirstLoc){
	            	//移动地图到定位点
	                mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
	                isRequest = false;
	            }
	            //首次定位完成
	            isFirstLoc = false;
	        }
	        
	        public void onReceivePoi(BDLocation poiLocation) {
	            if (poiLocation == null){
	                return ;
	            }
	        }
	    }
	    
	    //继承MyLocationOverlay重写dispatchTap实现点击处理
	  	public class locationOverlay extends MyLocationOverlay{

	  		public locationOverlay(MapView mapView) {
	  			super(mapView);
	  			// TODO Auto-generated constructor stub
	  		}
	  		@Override
	  		protected boolean dispatchTap() {
	  			// TODO Auto-generated method stub
	  			//处理点击事件,弹出泡泡
	  			popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText("我的位置");
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						new GeoPoint((int)(locData.latitude*1e6), (int)(locData.longitude*1e6)),
						8);
	  			return true;
	  		}
	  		
	  	}

	    @Override
	    protected void onPause() {
	        mMapView.onPause();
	        super.onPause();
	    }
	    
	    @Override
	    protected void onResume() {
	        mMapView.onResume();
	        super.onResume();
	    }
	    
	    @Override
	    protected void onDestroy() {
	    	//退出时销毁定位
	        if (mLocClient != null)
	            mLocClient.stop();
	        mMapView.destroy();
	        
	        MyApplication app = (MyApplication)this.getApplication();
			if (app.mBMapManager != null) {
				app.mBMapManager.destroy();
				app.mBMapManager = null;
			}
	        super.onDestroy();
	    }
	    
	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	    	super.onSaveInstanceState(outState);
	    	mMapView.onSaveInstanceState(outState);
	    	
	    }
	    
	    @Override
	    protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    	super.onRestoreInstanceState(savedInstanceState);
	    	mMapView.onRestoreInstanceState(savedInstanceState);
	    }
	    private Button mRightBtn;
		@Override
		public void initPages() {
			// TODO Auto-generated method stub
			mRightBtn=(Button)findViewById(R.id.right);
			mRightBtn.setBackgroundResource(R.drawable.gb_btn);
			mRightBtn.setOnClickListener(this);
		}
		@Override
		protected void onClickListener(int viewId) {
			// TODO Auto-generated method stub
			switch(viewId){
			case R.id.right:
				setResult(0);
				finish();
				break;
			}
		}
		@Override
		public int setModelId() {
			// TODO Auto-generated method stub
			return R.layout.index;
		}
		@Override
		public int setLayoutId() {
			// TODO Auto-generated method stub
			return R.layout.activity_locationoverlay;
		}
	    
	    
	    
	    
	}
	/**
	 * 继承MapView重写onTouchEvent实现泡泡处理操作
	 * @author hejin
	 *
	 */
	class MyLocationMapView extends MapView{
		static PopupOverlay   pop  = null;//弹出泡泡图层，点击图标使用
		public MyLocationMapView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		public MyLocationMapView(Context context, AttributeSet attrs){
			super(context,attrs);
		}
		public MyLocationMapView(Context context, AttributeSet attrs, int defStyle){
			super(context, attrs, defStyle);
		}
		@Override
	    public boolean onTouchEvent(MotionEvent event){
			if (!super.onTouchEvent(event)){
				//消隐泡泡
				if (pop != null && event.getAction() == MotionEvent.ACTION_UP)
					pop.hidePop();
			}
			return true;
		}
}
