package com.example.maosea;


import java.util.ArrayList;
import java.util.List;

//bmob
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

//import open street map

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;


import org.osmdroid.tileprovider.tilesource.TileSourceFactory; 
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase;
import org.osmdroid.util.GeoPoint;  
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapController;  
import org.osmdroid.views.MapView;  
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayManager;
import org.osmdroid.views.overlay.SimpleLocationOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
//

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadLeg;
import org.osmdroid.bonuspack.routing.RoadManager;
/***baidu location ***
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClientOption.LocationMode;
**********************/

import android.R.string;
//android sdk
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import android.location.Location;
import android.location.LocationManager;
import android.content.Context;
import android.content.Intent;

import com.example.maosea.R;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import android.app.Activity;
import android.os.Bundle;  
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity implements
	TencentLocationListener, OnClickListener {

	//osm mapview overlay object
	private IMapController mapController;  
	private MapView mapView;
	
	Drawable drawable;
	ArrayList<OverlayItem> items;
    ItemizedOverlay<OverlayItem> mLocationOverlay;
    ResourceProxy mResourceProxy;
    
    SimpleLocationOverlay simpleLocation;
    Marker nodemarker;
    Polyline roadOverlay;
    
	GeoPoint tempgp1 = new GeoPoint(0,0);
	
	//osmbonuspack
	RoadManager roadManager;
	Road road;
	
	//tencent loc
	private TencentLocationManager mLocationManager;

	
	/***baidu loc******
	public LocationClient mLocationClient;
	public MyLocationListener myListener;
	public BDLocation nowlocation ;
	LocationMode tempMode = LocationMode.Hight_Accuracy;
    String tempcoor="gcj02";
    **********************/
	
    
    public double nowlatitude;
    public double nowlongitude;
    public double templati1;
    public double templong1;
    
    public int ifcalcenter=0;
    public int ifhaveloc = 0;
	
	String username;
	//
    //btn
	Button getloc;
	Button sendmsg;
	Button getmsg;
	Button calmycenter;
	Button btnSign;
	
	////text
	EditText namesend;
	EditText nameget;
	
	TextView mLocationStatus;
	
	//other
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// innitiate Bmob SDK
		
		Bmob.initialize(this, "1f6d5faf012a0de5b3c9a1fc965b4e47");
		//Toast.makeText(this, "appid",Toast.LENGTH_LONG).show();
		
		//System.loadLibrary("locSDK5");
		/******
		 * 
		 * osm initiate
		 * 
		 * ******/         
		setContentView(R.layout.osm_layout);  
		//
		mapView = (MapView) findViewById(R.id.map); 
		mapView.setTileSource(TileSourceFactory.MAPNIK);  
		mapView.setBuiltInZoomControls(true);  
		mapController = mapView.getController();  
		mapController.setZoom(16);  
		GeoPoint point2 = new GeoPoint(30.2658604, 120.1154732);  
		mapController.setCenter(point2);
		

	    
		/***
		 * 
		 * construct btn  textview and edittext*
		 ***/
		getloc = (Button)findViewById(R.id.btnGetLocation);
		sendmsg = (Button)findViewById(R.id.btnsendmsg);
		getmsg = (Button)findViewById(R.id.btngetmsg);
		calmycenter = (Button)findViewById(R.id.btncal);
		btnSign = (Button)findViewById(R.id.btnsign);
 		
		namesend = (EditText) findViewById(R.id.edit_name);
		nameget = (EditText) findViewById(R.id.edit_get);
		
		mLocationStatus = (TextView) findViewById(R.id.text_status);
		
		
		/***tencent location initiate***/
		mLocationManager = TencentLocationManager.getInstance(this);
		//STOP LOC before change this setting
		mLocationManager.removeUpdates(null);
		// SET wgs84 
		mLocationManager.setCoordinateType(
		TencentLocationManager.COORDINATE_TYPE_WGS84);

		
		/***
		 * btn setOnClickListener
		 ****/
		sendmsg.setOnClickListener(this);
		getloc.setOnClickListener(this);
		calmycenter.setOnClickListener(this);
		btnSign.setOnClickListener(this);
		
		//initiate 
		
		
		/***baidu location initiate*********
	
		**********/
	
		
		/****the baidu get location******
		
		**********/
		
		getmsg.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				final BmobGeoPoint temppoint2 = new BmobGeoPoint();
				BmobQuery<Gps> query = new BmobQuery<Gps>();
				query.addWhereEqualTo("username", nameget.getText().toString());
				query.order("-updatedAt");
				query.findObjects(getApplicationContext(), new FindListener<Gps>(){
					
					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						toast("query failure!"+arg1);
					}
					@Override
					public void onSuccess(List<Gps> arg0) {
						// TODO Auto-generated method stub
						toast("query successful！");
						templati1 = (arg0.get(0).pointgps.getLatitude());
						templong1 = (arg0.get(0).pointgps.getLongitude());
						GeoPoint tempgp1 = new GeoPoint(templati1,templong1);
						
						//start draw!
						//Simple overlay
						if(simpleLocation != null)
							mapView.getOverlays().remove(simpleLocation);
				        simpleLocation = new SimpleLocationOverlay(MainActivity.this);  
				        simpleLocation.setEnabled(true);  
				        simpleLocation.setLocation(tempgp1);
				        //Log.d("gps", "tempgp 002  "+tempgeopoint1.getLatitude());
				        mapView.getOverlays().add(simpleLocation);
				        mapView.invalidate();	
				        //judge calculate state
				        if(ifcalcenter == 1){
				        	new Thread(calcenter).start();
				        }
					}
				});
				//toast("start draw!");
				//Log.d("gps", " again! latitude:"+templati1+"longitude:"+templong1);
				
				//tempgp1.setCoordsE6( (int) (1000000*templati1), (int)(1000000*templong1));
				//Log.i("gps", "tempgp1:"+tempgp1.getLatitude());
				
				
				
				
				
			}

			
		});
		

				  
		//***initiate myoverlay class *******/
		
}

	
	
	//*************************
	// 
	// the end of oncreate()
	//
	// * ***********************
	
	/***create JavaBean***/
	public class Gps extends BmobObject {
    private BmobGeoPoint pointgps;
    private String username;
    
    public String getusername(){
    	return username;
    }
    public void setusername(String bname){
    	this.username = bname;
    }
    public BmobGeoPoint getpointgps() {
        return pointgps;
    }
    public void setpointgps(BmobGeoPoint point) {
        this.pointgps = point;
    }
    
}
	
	
	

	/*******baidu location listener initiate******
	
	/*********baidu initaite location********

	********************/
	

	// ********* tencent view listener
	// **********tencent location callback
	@Override
	public void onLocationChanged(TencentLocation location, int error, String reason) {
		// TODO Auto-generated method stub
		String msg = null;
		if (TencentLocation.ERROR_OK == error) {
	        // 定位成功
			nowlatitude = location.getLatitude();
			nowlongitude = location.getLongitude();
			
			//show my current location
			//clear old overlays
			if(mLocationOverlay != null)
				mapView.getOverlays().remove(mLocationOverlay);
			
			GeoPoint gp3 = new GeoPoint(nowlatitude,nowlongitude); 
			//define the drawable and ArrayList<OverlayItem> items
			Drawable drawable = MainActivity.this.getResources().getDrawable(R.drawable.ic_mylocation);  
		    ArrayList<OverlayItem> items = new ArrayList<OverlayItem>(); 
			mResourceProxy = new ResourceProxyImpl(getApplicationContext());
			//define the item
			OverlayItem item = new OverlayItem("~title~", "I`m here!", gp3);
	  
	        item.setMarker(drawable);
	        //if(drawable == null)
	        //	Log.w("warn", "draw is null!");
	        
	        items.add(item);
	        
	        MainActivity.this.mLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items,
	        		new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>(){

						@Override
						public boolean onItemLongPress(int arg0,
								OverlayItem arg1) {
							// TODO Auto-generated method stub
							return false;
						}

						@Override
						public boolean onItemSingleTapUp(int arg0,
								OverlayItem arg1) {
							// TODO Auto-generated method stub
							toast("tapup!");
							return false;
						}
	        	
	        },mResourceProxy);
	        
	        mapView.getOverlays().add(mLocationOverlay);
	        if(ifhaveloc == 0){
	        	mapController.setCenter(gp3);
	        	ifhaveloc=1;
	        }
	        
			mapView.invalidate();
	        
		
			
	    } 
		else {
	        // 定位失败
			msg = "定位失败: " + reason;	
	    }
		//updateLocationStatus(msg);
	}
	
	public void stopLocation(View view) {
		mLocationManager.removeUpdates(this);
		updateLocationStatus("停止定位");
		}
	



	@Override
	public void onStatusUpdate(String arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}
	
	private void updateLocationStatus(String message) {
		mLocationStatus.append(message);
		mLocationStatus.append("\n---\n");
	}
	
	
	
	@Override
	protected void onDestroy() {
	super.onDestroy();
	/*
	* <p>
	* 重复调用 requestLocationUpdates, 将忽略之前的 request 并自动取消之前的 listener, 并使用最新的
	* request 和 listener 继续定位
	* <p>
	* 重复调用 removeUpdates, 定位停止
	*/
	// 退出 activity 前一定要停止定位!
	stopLocation(null);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.btnsendmsg:
			if(username == null){
				toast("sign in first!");
			}
			else{
				Gps mypoint = new Gps();
				
				BmobGeoPoint temppoint = new BmobGeoPoint();
				temppoint.setLatitude(nowlatitude);
				temppoint.setLongitude(nowlongitude);
				//mypoint.setusername(namesend.getText().toString());
				mypoint.setusername(username);
				mypoint.setpointgps(temppoint);
				
				/****save the gps data***/
				mypoint.save(MainActivity.this,new SaveListener(){
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						toast("add data successful！");
					}
					public void onFailure(int code, String msg) {
						// TODO Auto-generated method stub
						toast("add data failure：" + msg);
					}
				});
			}
		
		case R.id.btnGetLocation:
			//
			// create location request
			TencentLocationRequest request = TencentLocationRequest.create();
			// set interval of get location
			request.setInterval(0);
			// start 
			mLocationManager.requestLocationUpdates(request, MainActivity.this);
			//set calculate center point state
			ifcalcenter = 0;
			break;
		
		case R.id.btncal:
			//calculate the gather point
			
			//new thread
			new Thread(calcenter).start();		
			break;
		case R.id.btnsign:
			
			Intent intent = new Intent(MainActivity.this,SignActivity.class);
			startActivityForResult(intent,1);
			break;	
			
		default:
				break;
		}
	}
	
	Runnable calcenter = new Runnable(){
		//calculate the center gather point of two points
		@Override
		public void run() {
			// TODO Auto-generated method stub
			GeoPoint gp4 = new GeoPoint(nowlatitude,nowlongitude); 
			GeoPoint gp5 = new GeoPoint(templati1,templong1); 
			GeoPoint gp6 = new GeoPoint(0,0); //the middle point
			double mlength =0;
			double halflength = 0;
			int middlep = 0;
			int pointindex = 0;
			int ifarrive = 0;
			
			roadManager = new MapQuestRoadManager("Fmjtd%7Cluu82q072q%2C7x%3Do5-94y2gy");
			roadManager.addRequestOption("routeType=pedestrian");
			ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
			waypoints.add(gp4);
			waypoints.add(gp5);
			Road road = roadManager.getRoad(waypoints);
			//get all points on this road
			ArrayList<GeoPoint> mRoutepoints = new ArrayList<GeoPoint>();
			mRoutepoints = road.mRouteHigh;
			middlep = mRoutepoints.size()/2;
		    pointindex = middlep;
			Log.i("info", String.valueOf(middlep));
			//get length of my road
			halflength = road.mLength/2;
			Log.i("info", String.valueOf(halflength));
			//judge if two points is nearly the same position
			if(middlep > 1){
			
			
			//gp6 is the middle point
			gp6 = mRoutepoints.get(middlep);
			
			//calculate the middle point
			//int i1=0;
			
				//get the length of start point to current point:mlength
				for(int i=0;i<middlep;i++){
					mlength += mRoutepoints.get(i).distanceTo(mRoutepoints.get(i+1));
				}
				mlength = mlength/1000;
				Log.i("info", String.valueOf(mlength));
				//fix the  index of middle point
				
				if(mlength < halflength){					
					while(mlength < halflength){
						double templength = mRoutepoints.get(pointindex).distanceTo(mRoutepoints.get(pointindex+1));
						templength = templength/1000;
						mlength += templength;
						pointindex++;
					}
					pointindex--;
				}
				
				else{					
					while(mlength > halflength){
						double templength = mRoutepoints.get(pointindex).distanceTo(mRoutepoints.get(pointindex-1));
						templength = templength/1000;
						mlength -= templength;
						pointindex--;
					}
					pointindex++;
				}
				Log.i("info", "calculate the middle point:"+String.valueOf(pointindex));
				//draw the marker of the middle point 
				
				//clear old markers						
				if(nodemarker!=null)
					mapView.getOverlays().remove(nodemarker);
				nodemarker = new Marker(mapView);
				Drawable nodeicon = getResources().getDrawable(R.drawable.map_marker);
				
				nodemarker.setPosition(mRoutepoints.get(pointindex));
				nodemarker.setIcon(nodeicon);
				nodemarker.setTitle("gather point");
				mapView.getOverlays().add(nodemarker);
				
				//draw the road on the map view
				
				if(roadOverlay != null){				
					mapView.getOverlays().remove(roadOverlay);
				}
				roadOverlay = RoadManager.buildRoadOverlay(road, 
						getApplicationContext());
				mapView.getOverlays().add(roadOverlay);
			}
			else{
				ifarrive = 1;
			}

			
			Message msg = mhandler.obtainMessage(1);
			msg.arg1=ifarrive;
			msg.sendToTarget();
			//set the calculate center point state 1
			ifcalcenter = 1; 
		 	//Log.i("gps","latitude:"+gpcen.getLatitude()+" logitude:"+gpcen.getLongitude() );
		}
		
	};
	
	Runnable rgetloc = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
						
						
		}
	};
	
	Handler mhandler = new Handler() {
	    public void handleMessage(Message msg) {
	    	
	    	switch(msg.what){
	    	case 1:
	    		//draw route
	    		mapView.invalidate();
	    		if(msg.arg1==1){
	    			toast("Reach point!");
	    		}
	    			
	    		break;
	    	
	    	default:
	    		break;
	    	}
	    }
	};
	
	Handler mhandler2 = new Handler(){
		public void handleMessage(Message msg) {
			//mapController.setCenter((GeoPoint)msg.obj);
    		mapView.invalidate();
		}
		
	};
	
	public void toast(String string) {
		// TODO Auto-generated method stub		    	
    		Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
    		Log.d("TAG", string);			    	
	}

	@Override
	protected void onActivityResult(int requestcode,int resultcode,Intent data){
		switch(requestcode){
		case 1:
			if(resultcode == RESULT_OK){
				username = data.getStringExtra("username") ;
				
			}
			break;
		default:
				
		}
	}
	
}	 


	 



