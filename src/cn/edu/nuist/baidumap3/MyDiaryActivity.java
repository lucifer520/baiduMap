package cn.edu.nuist.baidumap3;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class MyDiaryActivity extends Activity {

	private String userName;
	private String lan,lon;
	private ArrayList<String> list;
	private int catePosition;
	private static String CONTENT_URI_USER="content://cn.edu.nuist.baidumap3.MySqliteContentProvider/editor";
	private static String CONTENT_URI_CATE="content://cn.edu.nuist.baidumap3.MySqliteContentProvider/cates";
	private ContentResolver contentResolver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary);
		//得到上个界面传入的用户名
		Intent intent = getIntent();
		userName = intent.getStringExtra("userName");
		lan=intent.getStringExtra("lan");
		lon=intent.getStringExtra("lon");
		contentResolver=getContentResolver();
		list=new ArrayList<String>();
		
		//记  序号、时间、人物、地点、心情
		//对话框
		
		
		final TextView diaryId;
		final TextView diaryDay;
		final TextView diaryAddress, diaryAuthor;
		final EditText diaryContent;
		final Button diaryOk;
		final Button diaryCancel;
		final Spinner diarySpinner;
		
		diaryId = (TextView)findViewById(R.id.diaryId);
		diaryDay = (TextView)findViewById(R.id.diaryDay);
		diaryAddress = (TextView)findViewById(R.id.diaryAddress);
		diaryAuthor = (TextView)findViewById(R.id.diaryAuthor);
		diaryContent = (EditText)findViewById(R.id.diaryContent);
		diaryOk = (Button)findViewById(R.id.diaryOk);
		diaryCancel = (Button)findViewById(R.id.diaryCancel);
		diarySpinner= (Spinner)findViewById(R.id.diarySpinner);
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
		/*int lat = mMapView.getMapCenter().getLatitudeE6();
		int lon = mMapView.getMapCenter().getLongitudeE6();*/
		diaryAddress.setText(lan+","+lon);
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
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyDiaryActivity.this, android.R.layout.simple_spinner_item,android.R.id.text1,list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		diarySpinner.setAdapter(adapter);
		
		
		OnClickListener clickListner = new OnClickListener() {						
			@Override
			public void onClick(View arg0) {
				if(arg0==diaryOk)
				{
					
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
							Toast toast = Toast.makeText(MyDiaryActivity.this, "添加成功", Toast.LENGTH_LONG);
							toast.show();
							
						}
						catch(Exception ex)
						{
							Toast toast = Toast.makeText(MyDiaryActivity.this, "添加失败", Toast.LENGTH_LONG);
							toast.show();
						}
					}
					
					
				}
				else if(arg0==diaryCancel)
				{
					
				}
			}
		};
		diaryOk.setOnClickListener(clickListner);
		diaryCancel.setOnClickListener(clickListner);
		
      
	}
	

}
