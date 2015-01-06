package cn.edu.nuist.baidumap3;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.Overlay;
import com.baidu.mapapi.Projection;

public class MyMapActivity extends MapActivity{

	private BMapManager mapManager; 
	private MapView mMapView;  
	private MapController mMapController; 
	private Context context;
	private int zoomLevel=5;
	private String userName;
	private ArrayList<String> list;
	private int catePosition;
	private MKSearch mMKSearch;
	private float pX,pY;
	private int times=0;
	private ImageButton up,down,left,right,big,small,sta,traffic; 
	private ContentResolver contentResolver;
	private static String CONTENT_URI_USER="content://cn.edu.nuist.baidumap3.MySqliteContentProvider/editor";
	private static String CONTENT_URI_CATE="content://cn.edu.nuist.baidumap3.MySqliteContentProvider/cates";
	private Button query_input,nearby;
	@Override  
	public void onCreate(Bundle savedInstanceState) {  
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.map);
	    // 初始化MapActivity
	    mapManager = new BMapManager(getApplication());  
	    // init方法的第一个参数需填入申请的API Key
	    mapManager.init("A42FCA503FC147E1B03743D1F734A9B4F0000781", null);
	    super.initMapActivity(mapManager);

	    //获得MapView
        mMapView=(MapView)findViewById(R.id.mapViewId);
        //打开默认的缩放控件
        mMapView.setBuiltInZoomControls(true);   
        //获得MapController类,通过MapController实现地图的平原，缩放
        mMapController=mMapView.getController();
        //使用南京的经纬度,构造一个GeoPoint
        //(118.78333,   32.05000)
        GeoPoint gpoint=new GeoPoint((int)(32.050*1E6),(int)(118.783*1E6));   
        //调用MapController的setCenter(GeoPoint point)将地图的中心设置成南京;
        mMapController.setCenter(gpoint);
        //设置地图的缩放级别为12
        mMapController.setZoom(zoomLevel);  //3-18
        contentResolver=getContentResolver();
        list=new ArrayList<String>();
        //搜索经纬度
        query_input=(Button)findViewById(R.id.query_input); 
        nearby = (Button)findViewById(R.id.nearby);
        init();
        
        //接受卫星的信号
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			}
			@Override
			public void onProviderEnabled(String arg0) {
			}
			@Override
			public void onProviderDisabled(String arg0) {
			}
			@Override
			public void onLocationChanged(Location location) {
				GeoPoint curPoint = new GeoPoint((int)(location.getLatitude()*1E6),  (int)(location.getLongitude()*1E6));
				mMapController.animateTo(curPoint);
				MyOverLay curOverLay = new MyOverLay(curPoint);
				mMapView.getOverlays().add(curOverLay);
			}
		});
	}
	
	class MyOverLay extends Overlay
	{
		private GeoPoint endPoint;
		public MyOverLay(GeoPoint endPoint)
		{
			this.endPoint=endPoint;
		}
		//在指定位置上描绘图案
		@Override
		public void draw(Canvas arg0, MapView arg1, boolean arg2) {
			// TODO Auto-generated method stub
			super.draw(arg0, arg1, arg2);
			/*//将地理位置转化为xy坐标位置
			Projection projection = mMapView.getProjection();
			Point endXYPoint = new Point();
			projection.toPixels(endPoint, endXYPoint);
			
			//在x,y位置的point上描绘图标
			//1、得到图标
			Bitmap posmap=BitmapFactory.decodeResource(getResources(), R.drawable.pos);
			arg0.drawBitmap(posmap, endXYPoint.x-posmap.getWidth()/2, endXYPoint.y-posmap.getHeight(), null);
			*/
			//将地理位置转化为xy坐标位置
			Projection projection = mMapView.getProjection();
			Point endXYPoint = new Point();
			projection.toPixels(endPoint, endXYPoint);

			Paint paint=new Paint();
			paint.setColor(Color.RED);
			paint.setStrokeWidth((float)1.0);
			Paint paintx=new Paint();
			paint.setColor(Color.BLUE);
			paint.setStrokeWidth((float)1.0);
			//在x,y位置的point上描绘图标
			//1、得到图标
			Bitmap posmap=BitmapFactory.decodeResource(getResources(), R.drawable.pos);
			arg0.drawBitmap(posmap, endXYPoint.x-posmap.getWidth()/2, endXYPoint.y-posmap.getHeight(), null);
			
			
				arg0.drawLine(pX, pY,endXYPoint.x, endXYPoint.y, paint);
					
			pX=endXYPoint.x;
			pY=endXYPoint.y;
			times++;
		}	    
	}

	public void init()
	{
		//得到上个界面传入的用户名
		Intent intent = getIntent();
		userName = intent.getStringExtra("userName");
        //实例化所有的按钮
        up=(ImageButton)findViewById(R.id.up);
        down=(ImageButton)findViewById(R.id.down);
        left=(ImageButton)findViewById(R.id.left);
        right=(ImageButton)findViewById(R.id.right);
        big=(ImageButton)findViewById(R.id.big);
        small=(ImageButton)findViewById(R.id.small);
        sta=(ImageButton)findViewById(R.id.sta);
        traffic=(ImageButton)findViewById(R.id.traffic);
        
        OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				if(v==up)
				{
					int centerLatitude = mMapView.getMapCenter().getLatitudeE6();
					int newCenterLatitude = centerLatitude-mMapView.getLatitudeSpan()/4;
					GeoPoint newCenter=new GeoPoint(newCenterLatitude, mMapView.getMapCenter().getLongitudeE6());
					mMapController.setCenter(newCenter);
					//mMapController.animateTo(newCenter);
				}
				else if(v==down)
				{
					int centerLatitude = mMapView.getMapCenter().getLatitudeE6();
					int newCenterLatitude = centerLatitude-mMapView.getLatitudeSpan()/4;
					GeoPoint newCenter=new GeoPoint(newCenterLatitude, mMapView.getMapCenter().getLongitudeE6());
					mMapController.setCenter(newCenter);
				}
				else if(v==left)
				{
					int centerLongitude = mMapView.getMapCenter().getLongitudeE6();
					int newCenterLongitude = centerLongitude-mMapView.getLongitudeSpan()/4;
					GeoPoint newCenter=new GeoPoint(mMapView.getMapCenter().getLatitudeE6(),newCenterLongitude);
					mMapController.setCenter(newCenter);
				}
				else if(v==right)                                                                                                                                 
				{
					int centerLongitude = mMapView.getMapCenter().getLongitudeE6();
					int newCenterLongitude = centerLongitude+mMapView.getLongitudeSpan()/4;
					GeoPoint newCenter=new GeoPoint(mMapView.getMapCenter().getLatitudeE6(),newCenterLongitude);
					mMapController.setCenter(newCenter);
				}
				else if(v==big)
				{
					zoomLevel++;
					if(zoomLevel>18)
						zoomLevel=18;
					mMapController.setZoom(zoomLevel);
				}
				else if(v==small)
				{
					zoomLevel--;
					if(zoomLevel<3)
						zoomLevel=3;
					mMapController.setZoom(zoomLevel);
				}
				else if(v==sta)
				{
					mMapView.setTraffic(false);
					mMapView.setSatellite(true);
				}
				else if(v==traffic)
				{
					mMapView.setTraffic(true);
					mMapView.setSatellite(false);
				}
				else if(v==query_input)
				{
					Intent intent = new Intent(MyMapActivity.this,QueryAddressActivity.class);
					startActivity(intent);
				
				}
				else if(v==nearby)
				{
					Intent intent = new Intent(MyMapActivity.this,NearbyActivity.class);
					startActivity(intent);
				}
			}
		};
		
		up.setOnClickListener(listener);
		down.setOnClickListener(listener);
		left.setOnClickListener(listener);
		right.setOnClickListener(listener);
		big.setOnClickListener(listener);
		small.setOnClickListener(listener);
		sta.setOnClickListener(listener);
		traffic.setOnClickListener(listener);
		query_input.setOnClickListener(listener);
		nearby.setOnClickListener(listener);
	}
	
	
	@Override  
	protected boolean isRouteDisplayed() {  
	    return false;  
	}  

	@Override  
	protected void onDestroy() {  
	    if (mapManager != null) {  
	        mapManager.destroy();  
	        mapManager = null;  
	    }  
	    super.onDestroy();  
	}  

	@Override  
	protected void onPause() {  
	    if (mapManager != null) {  
	        mapManager.stop();  
	    }  
	    super.onPause();  
	}  

	@Override  
	protected void onResume() {  
	    if (mapManager != null) {  
	        mapManager.start();  
	    }  
	    super.onResume();  
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		OnMenuItemClickListener listener = new OnMenuItemClickListener() {			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				int itemId = arg0.getItemId();
				if(itemId==1)
				{
					/*//地址
					int lan = mMapView.getMapCenter().getLatitudeE6();
					int lon = mMapView.getMapCenter().getLongitudeE6();
					Intent intent=new Intent();
                    intent.putExtra("userName", userName);
                    intent.putExtra("lan", lan+"");
                    intent.putExtra("lon", lon+"");
                    
                    intent.setClass(MyMapActivity.this,MyDiaryActivity.class);
                    startActivity(intent);
                    MyMapActivity.this.finish();
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);*/
					//记  序号、时间、人物、地点、心情
					//对话框
					LayoutInflater inflater = getLayoutInflater();
					View diaryView = inflater.inflate(R.layout.diary, null);
					AlertDialog.Builder builder = new AlertDialog.Builder(MyMapActivity.this);
					builder.setTitle("添加日记");
					builder.setView(diaryView);
					final AlertDialog dialog = builder.create();
					dialog.show();
					
					final TextView diaryId;
					final TextView diaryDay;
					final TextView diaryAddress, diaryAuthor;
					final EditText diaryContent;
					final Button diaryOk;
					final Button diaryCancel;
					final Spinner diarySpinner;
					
					diaryId = (TextView)diaryView.findViewById(R.id.diaryId);
					diaryDay = (TextView)diaryView.findViewById(R.id.diaryDay);
					diaryAddress = (TextView)diaryView.findViewById(R.id.diaryAddress);
					diaryAuthor = (TextView)diaryView.findViewById(R.id.diaryAuthor);
					diaryContent = (EditText)diaryView.findViewById(R.id.diaryContent);
					diaryOk = (Button)diaryView.findViewById(R.id.diaryOk);
					diaryCancel = (Button)diaryView.findViewById(R.id.diaryCancel);
					diarySpinner= (Spinner)diaryView.findViewById(R.id.diarySpinner);
					//Id初使值
					final SharedPreferences sp = getSharedPreferences("diaryChina",Context.MODE_WORLD_READABLE+Context.MODE_WORLD_WRITEABLE);
					int diary_id=sp.getInt("diaryId",0);
					diaryId.setText(""+(++diary_id));
					//日期
					Calendar calendar = Calendar.getInstance();
					int year = calendar.get(Calendar.YEAR);
					int month = calendar.get(Calendar.MONTH);
					int day = calendar.get(Calendar.DATE);
					int hour = calendar.get(Calendar.HOUR);
					int minute = calendar.get(Calendar.MINUTE);
					diaryDay.setText(""+year+"-"+month+"-"+day+" "+hour+":"+minute);
					//地址
					int lat = mMapView.getMapCenter().getLatitudeE6();
					int lon = mMapView.getMapCenter().getLongitudeE6();
					diaryAddress.setText(lat+","+lon);
					//作者
					diaryAuthor.setText(userName);
					//读取分类
					Uri uri = Uri.parse(CONTENT_URI_CATE);
					Cursor cursor = contentResolver.query(uri, new String[]{"cate"},null, null, null);
					list.clear();
					int i=0;
					while(cursor.moveToNext())
					{
						
						
						list.add(cursor.getString(cursor.getColumnIndex("cate")));
						
						
						
					}
					
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyMapActivity.this, android.R.layout.simple_spinner_item,android.R.id.text1,list);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					diarySpinner.setAdapter(adapter);
					
					
					OnClickListener clickListner = new OnClickListener() {						
						@Override
						public void onClick(View arg0) {
							if(arg0==diaryOk)
							{
								//写日记								
								/*Editor editor = sp.edit();
								editor.putInt("diaryId",Integer.parseInt(diaryId.getText().toString()));
								editor.putString("diaryDay"+diaryId.getText().toString(), diaryDay.getText().toString());
								editor.putString("diaryAddress"+diaryId.getText().toString(), diaryAddress.getText().toString());
								editor.putString("diaryAuthor"+diaryId.getText().toString(), diaryAuthor.getText().toString());
								editor.putString("diaryContent"+diaryId.getText().toString(), diaryContent.getText().toString());
								editor.commit();*/
								catePosition=diarySpinner.getSelectedItemPosition();
								Uri uri = Uri.parse(CONTENT_URI_USER);
								ContentValues values = new ContentValues();
								values.put("username", "mm");
								values.put("diaryDay", diaryDay.getText().toString());
								values.put("diaryAddress", diaryAddress.getText().toString());
								values.put("diaryAuthor", diaryAuthor.getText().toString());
								values.put("diaryContent",diaryContent.getText().toString());
								values.put("cate",(String) list.get(catePosition));
								Uri newUri = contentResolver.insert(uri, values);
								System.out.println("::"+newUri.getLastPathSegment());
								String idStr = newUri.getLastPathSegment();
								if(idStr!=null&&!idStr.equals(""))
								{
									try
									{
										int idInt = Integer.parseInt(idStr);
										Toast toast = Toast.makeText(MyMapActivity.this, "添加成功", Toast.LENGTH_LONG);
										toast.show();
										dialog.dismiss();
									}
									catch(Exception ex)
									{
										Toast toast = Toast.makeText(MyMapActivity.this, "添加失败", Toast.LENGTH_LONG);
										toast.show();
									}
								}
								
								
							}
							else if(arg0==diaryCancel)
							{
								dialog.dismiss();
							}
						}
					};
					diaryOk.setOnClickListener(clickListner);
					diaryCancel.setOnClickListener(clickListner);
					
				}
				else if(itemId==2)
				{
					LayoutInflater inflater = getLayoutInflater();
					View cateView = inflater.inflate(R.layout.diarycate, null);
					AlertDialog.Builder builder = new AlertDialog.Builder(MyMapActivity.this);
					builder.setTitle("选择分类");
					builder.setView(cateView);
					final AlertDialog dialog = builder.create();
					dialog.show();
					
					
					final Spinner diarycatespinner;
					final Button diarycateOk;
					final Button diarycateCancel;
					
					diarycatespinner = (Spinner)cateView.findViewById(R.id.diarycatespinner);
					diarycateOk = (Button)cateView.findViewById(R.id.diarycateOk);
					diarycateCancel = (Button)cateView.findViewById(R.id.diarycateCancel);
					//读取分类
					Uri uri = Uri.parse(CONTENT_URI_CATE);
					Cursor cursor = contentResolver.query(uri, new String[]{"cate"},null, null, null);
					list.clear();
					int i=0;
					while(cursor.moveToNext())
					{
						
						
						list.add(cursor.getString(cursor.getColumnIndex("cate")));
						
						
						
					}
					
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyMapActivity.this, android.R.layout.simple_spinner_item,android.R.id.text1,list);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					diarycatespinner.setAdapter(adapter);
					
					OnClickListener clickListner = new OnClickListener() {						
						@Override
						public void onClick(View arg0) {
							if(arg0==diarycateOk)
							{
								
								diarycate(list.get(diarycatespinner.getSelectedItemPosition()));
								dialog.dismiss();
								
							}
							else if(arg0==diarycateCancel)
							{
								dialog.dismiss();
							}
						}
					};
					diarycateOk.setOnClickListener(clickListner);
					diarycateCancel.setOnClickListener(clickListner);
					contentResolver=getContentResolver();
					
				}
				else if(itemId==4)
				{
					Intent intent = new Intent(MyMapActivity.this,UserManagerActivity.class);
					startActivity(intent);
				}
				else if(itemId==6)
				{
					
					LayoutInflater inflater = getLayoutInflater();
					View cateView = inflater.inflate(R.layout.cate, null);
					AlertDialog.Builder builder = new AlertDialog.Builder(MyMapActivity.this);
					builder.setTitle("添加分类");
					builder.setView(cateView);
					final AlertDialog dialog = builder.create();
					dialog.show();
					
					
					final EditText cateedit;
					final Button diaryOk;
					final Button diaryCancel;
					
					cateedit = (EditText)cateView.findViewById(R.id.cateedit);
					diaryOk = (Button)cateView.findViewById(R.id.diaryOk);
					diaryCancel = (Button)cateView.findViewById(R.id.diaryCancel);
					
					OnClickListener clickListner = new OnClickListener() {						
						@Override
						public void onClick(View arg0) {
							if(arg0==diaryOk)
							{
								
								Uri uri = Uri.parse(CONTENT_URI_CATE);
								ContentValues values = new ContentValues();
								values.put("cate", cateedit.getText().toString());
								
								Uri newUri = contentResolver.insert(uri, values);
								System.out.println("::"+newUri.getLastPathSegment());
								String idStr = newUri.getLastPathSegment();
								if(idStr!=null&&!idStr.equals(""))
								{
									try
									{
										int idInt = Integer.parseInt(idStr);
										Toast toast = Toast.makeText(MyMapActivity.this, "添加成功", Toast.LENGTH_LONG);
										toast.show();
										dialog.dismiss();
									}
									catch(Exception ex)
									{
										Toast toast = Toast.makeText(MyMapActivity.this, "添加失败", Toast.LENGTH_LONG);
										toast.show();
									}
								}
								
								
							}
							else if(arg0==diaryCancel)
							{
								dialog.dismiss();
							}
						}
					};
					diaryOk.setOnClickListener(clickListner);
					diaryCancel.setOnClickListener(clickListner);
					contentResolver=getContentResolver();
				}
				return true;
			}
		};		
		//加菜单
		menu.add(1,1,1,"书写日记").setOnMenuItemClickListener(listener);
		menu.add(2,2,2,"查看日记").setOnMenuItemClickListener(listener);
		menu.add(3,3,3,"个人信息").setOnMenuItemClickListener(listener);
		menu.add(4,4,4,"用户管理").setOnMenuItemClickListener(listener);
		menu.add(5,5,5,"退出").setOnMenuItemClickListener(listener);
		menu.add(6,6,6,"添加分类").setOnMenuItemClickListener(listener);
		/*.setIcon(getResources().getDrawable(R.drawable.ic_launcher))*/
		return super.onCreateOptionsMenu(menu);
	} 
	
	private void diarycate(String cate){
		final List<Map<String,String>> data = new ArrayList<Map<String,String>>();
		Map<String,String> map = new HashMap<String, String>();
		Uri uri = Uri.parse(CONTENT_URI_USER);
		Cursor cursor = contentResolver.query(uri, new String[]{"diaryId","diaryDay","diaryAddress","diaryAuthor","diaryContent"},"cate=?", new String[]{cate}, null);
		
		int i=0;
		while(cursor.moveToNext())
		{
			
			map.put("diaryId", ""+i);
			map.put("diaryDay",cursor.getString(cursor.getColumnIndex("diaryDay")));
			map.put("diaryAddress",cursor.getString(cursor.getColumnIndex("diaryAddress")));
			map.put("diaryAuthor", cursor.getString(cursor.getColumnIndex("diaryAuthor")));
			map.put("diaryContent",cursor.getString(cursor.getColumnIndex("diaryContent")));
			data.add(map);
			
			
			
			
		}
		
			
			
		
		//对话框					 
		ListView diarylistview = new ListView(MyMapActivity.this);
		diarylistview.setAdapter(new BaseAdapter(){
			View view ;
			@Override
			public int getCount() {
				return data.size();
			}
			@Override
			public Object getItem(int arg0) {
				LayoutInflater inflater =getLayoutInflater();
				view = inflater.inflate(R.layout.diarylist,null);
				return view.getId();
			}
			@Override
			public long getItemId(int arg0) {
				return arg0;
			}
			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				LayoutInflater inflater =getLayoutInflater();
				view = inflater.inflate(R.layout.diarylist,null);
				TextView diaryIdText = (TextView)view.findViewById(R.id.diaryIdText);
				TextView diaryAddressText = (TextView)view.findViewById(R.id.diaryAddressText);
				TextView diaryDayText = (TextView)view.findViewById(R.id.diaryDayText);
				TextView diaryAuthorText = (TextView)view.findViewById(R.id.diaryAuthorText);
				TextView diaryContentText = (TextView)view.findViewById(R.id.diaryContentText);
				diaryIdText.setText(""+data.get(arg0).get("diaryId"));
				diaryDayText.setText(""+data.get(arg0).get("diaryDay"));
				diaryAddressText.setText(""+data.get(arg0).get("diaryAddress"));
				diaryAuthorText.setText(""+data.get(arg0).get("diaryAuthor"));
				diaryContentText.setText(""+data.get(arg0).get("diaryContent"));
				return view;
			}});
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(MyMapActivity.this);
		builder.setTitle("查看日记");
		builder.setView(diarylistview);					
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.cancel();
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	/**
	 * 分类按钮点击效果
	 * 
	 */
	private OnItemSelectedListener listener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			catePosition = position;
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};
	
	
	
}
