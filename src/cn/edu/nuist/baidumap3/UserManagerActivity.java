package cn.edu.nuist.baidumap3;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class UserManagerActivity extends BaseActivity implements OnClickListener{
	private TextView userlist_text,update_text,map_text,delete_text,exit_text;
	private Fragment userListFragment,userUpdateFragment,userDeleteFragment;
	/* 定义一个动态数组 */
	public static ArrayList<HashMap<String, Object>> listItem=new ArrayList<HashMap<String, Object>>();
	private static ContentResolver contentResolver;
	private static String CONTENT_URI_USER="content://cn.edu.nuist.baidumap3.MySqliteContentProvider/user";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findComponentView();
		contentResolver=getContentResolver();
		getDate();
	}
	
	/* 添加一个得到数据的方法，方便使用 */
	public static ArrayList<HashMap<String, Object>> getDate() {
		listItem.clear();
		Uri uri = Uri.parse(CONTENT_URI_USER);
		Cursor cursor = contentResolver.query(uri, null, null, null, null);
		int i=0;
		while(cursor.moveToNext())
		{
			i++;
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("order", ""+i);
			map.put("id", cursor.getString(cursor.getColumnIndex("_id")));
			map.put("username", cursor.getString(cursor.getColumnIndex("username")));
			map.put("checked", false);
			listItem.add(map);
		}
		return listItem;
	}
	
	@Override
	public void findComponentView() {
		userlist_text=(TextView) findViewById(R.id.userlist_text);
		update_text=(TextView) findViewById(R.id.update_text);
		map_text=(TextView) findViewById(R.id.map_text);
		delete_text=(TextView) findViewById(R.id.delete_text);
		exit_text=(TextView) findViewById(R.id.exit_text);

		userlist_text.setOnClickListener(this);
		update_text.setOnClickListener(this);
		delete_text.setOnClickListener(this);
		map_text.setOnClickListener(this);
		exit_text.setOnClickListener(this);
		userListFragment=new UserListFragment();
		handlerSwitchUI(userlist_text,userListFragment);
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch(id){
		case R.id.userlist_text:
			if(null==userListFragment){
				userListFragment=new UserListFragment();
			}
			handlerSwitchUI(userlist_text,userListFragment);
			break;
		case R.id.update_text:
			if(null==userUpdateFragment){
				userUpdateFragment= new UserUpdateFragment();
			}
			handlerSwitchUI(update_text,userUpdateFragment);
			break;
		case R.id.delete_text:
			if(null==userDeleteFragment){
				userDeleteFragment= new UserDeleteFragment();
			}
			handlerSwitchUI(delete_text,userDeleteFragment);
			break;
		case R.id.map_text:
			handlerSwitchUI(map_text,null);
			Intent intent=new Intent(UserManagerActivity.this,MyMapActivity.class);
			UserManagerActivity.this.finish();
			break;
		case R.id.exit_text:
			handlerSwitchUI(exit_text,null);
			UserManagerActivity.this.finish();
		
			
			break;
		}
	}
	private void handlerSwitchUI(TextView tv,Fragment frg){
		userlist_text.setBackgroundResource(R.drawable.maintab_toolbar_bg);
		update_text.setBackgroundResource(R.drawable.maintab_toolbar_bg);
		delete_text.setBackgroundResource(R.drawable.maintab_toolbar_bg);
		map_text.setBackgroundResource(R.drawable.maintab_toolbar_bg);
		exit_text.setBackgroundResource(R.drawable.maintab_toolbar_bg);
		tv.setBackgroundResource(R.drawable.titlebar_lightgray_bg);
		if(frg!=null)
			getSupportFragmentManager().beginTransaction().replace(R.id.container, frg).commit();
	}
	

}
