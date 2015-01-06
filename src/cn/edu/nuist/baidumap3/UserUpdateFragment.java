package cn.edu.nuist.baidumap3;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UserUpdateFragment extends Fragment{
	private TextView reg_text;
	private Spinner province_spinner, city_spinner;
	private int[] cities = new int[] { R.array.city1, R.array.city2,
			R.array.city3 };
	private Button selectBirthday,reg_but,login_but;
	private EditText username_edit,password_edit,repassword_edit;
	private RadioButton male_radio,female_radio;
	private CheckBox playball_check,playcard_check,playgame_check;
	private int cityArrayPosition;
	
	private ContentResolver contentResolver;
	private static String CONTENT_URI_USER="content://cn.edu.nuist.baidumap3.MySqliteContentProvider/user";
	private String updateId=null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			setHasOptionsMenu(true);
			View view = inflater.inflate(R.layout.activity_reg, container,false);
			init(view);
			return view;
	}
	public void init(View view) {
		province_spinner = (Spinner) view.findViewById(R.id.province_spinner);
		city_spinner = (Spinner)view.findViewById(R.id.city_spinner);
		
		OnItemSelectedListener listener = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				cityArrayPosition = position;
				int curCities = cities[position];
				String[] strCities = getResources().getStringArray(curCities);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,android.R.id.text1,strCities);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				city_spinner.setAdapter(adapter);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		};
		province_spinner.setOnItemSelectedListener(listener);
		
		selectBirthday = (Button)view.findViewById(R.id.selectbirthday_but);
		OnClickListener listener2 = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.getId()==selectBirthday.getId())
				{
					DatePickerDialog dpd = new DatePickerDialog(getActivity(), new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear,
								int dayOfMonth) {
							selectBirthday.setText(""+year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
						}
					}, 1990, 0, 1);
					dpd.show();
				}
				else if(v.getId()==reg_but.getId())
				{
					//得到所有输入的信息
					String username=username_edit.getText().toString();
					String password=password_edit.getText().toString();
					String repassword=repassword_edit.getText().toString();
					String sex="";
					if(male_radio.isChecked())
						sex=male_radio.getText().toString();
					else 
						sex=female_radio.getText().toString();
					String interest="";
					if(playball_check.isChecked())
						interest+=playball_check.getText().toString()+",";
					if(playcard_check.isChecked())
						interest+=playcard_check.getText().toString()+",";
					if(playgame_check.isChecked())
						interest+=playgame_check.getText().toString()+",";
					String address = "";
					int provinceId=province_spinner.getSelectedItemPosition();
					String[] provinces=getResources().getStringArray(R.array.province);
					address+=provinces[provinceId];
					int cityId=city_spinner.getSelectedItemPosition();
					String[] cities_string = getResources().getStringArray(cities[cityArrayPosition]);
					address+=cities_string[cityId];
					String birthday=selectBirthday.getText().toString();
					//Toast.makeText(RegActivity.this, username+";"+password+";"+repassword+";"+sex+";"+interest+";"+address+";"+birthday, Toast.LENGTH_LONG).show();
					//向数据库中添加用户数据
					Uri uri = Uri.parse(CONTENT_URI_USER);
					 
					ContentValues values = new ContentValues();
					values.put("username", username);
					values.put("pwd", password);
					values.put("sex", sex);
					values.put("interest",interest);
					values.put("address",address);
					values.put("birthday",birthday);
					 
					int rows = contentResolver.update(uri,values,"_id=?", new String[]{updateId});
					System.out.println("::"+rows);
					if(rows>0)
					{
						UserManagerActivity.getDate();
						Toast toast = Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_LONG);
						toast.show();
					}
					else
					{
							Toast toast = Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_LONG);
							toast.show();
					}
				}
			}
		};
		selectBirthday.setOnClickListener(listener2);
		reg_but =(Button)view.findViewById(R.id.reg_but);
		reg_but.setOnClickListener(listener2);

		username_edit=(EditText)view.findViewById(R.id.username_edit);
		password_edit=(EditText)view.findViewById(R.id.password_edit);
		repassword_edit=(EditText)view.findViewById(R.id.repassword_edit);
		male_radio=(RadioButton)view.findViewById(R.id.male_radio);
		female_radio=(RadioButton)view.findViewById(R.id.female_radio);
		playball_check=(CheckBox)view.findViewById(R.id.playball_check);
		playcard_check=(CheckBox)view.findViewById(R.id.playcard_check);
		playgame_check=(CheckBox)view.findViewById(R.id.playgame_check);
		
		contentResolver=getActivity().getContentResolver();
		
		reg_but.setText("更新");
		reg_text=(TextView)view.findViewById(R.id.reg_text);
		reg_text.setText("更 新 用 户");
		login_but=(Button)view.findViewById(R.id.login_but);
		login_but.setVisibility(View.GONE);
		for(int i=0;i<UserManagerActivity.listItem.size();i++)
		{
			Log.v("list", UserManagerActivity.listItem.get(i).get("id")+""+UserManagerActivity.listItem.get(i).get("checked").toString());
			if(Boolean.parseBoolean(UserManagerActivity.listItem.get(i).get("checked").toString()))
			{
				updateId=UserManagerActivity.listItem.get(i).get("id").toString();
				break;
			}
		}
		Cursor cursor = contentResolver.query(Uri.parse(CONTENT_URI_USER), null, "_id="+updateId, null, null);
		if(cursor.moveToNext())
		{
			username_edit.setText(cursor.getString(cursor.getColumnIndex("username")));
			password_edit.setText(cursor.getString(cursor.getColumnIndex("pwd")));
			repassword_edit.setText(cursor.getString(cursor.getColumnIndex("pwd")));
			String sex = cursor.getString(cursor.getColumnIndex("sex"));
			if(sex.equals("男"))
				male_radio.setChecked(true);
			else 
				female_radio.setChecked(true);
			String interest = cursor.getString(cursor.getColumnIndex("interest"));
			String[] hobby=interest.split(",");
			
			for(int i=0;i<hobby.length;i++)
			{
				if(hobby[i].equals(playball_check.getText().toString()))
				{
					playball_check.setChecked(true);
				}
				else if(hobby[i].equals(playcard_check.getText().toString()))
				{
					playcard_check.setChecked(true);
				}
				else if(hobby[i].equals(playgame_check.getText().toString()))
				{
					playgame_check.setChecked(true);
				}
			}
			String address = cursor.getString(cursor.getColumnIndex("address"));
			String province_str = address.substring(0,2);
			String city_str=address.substring(2);
			String[] provinces=getResources().getStringArray(R.array.province);
			int province_index=0;
			for(int i=0;i<provinces.length;i++)
			{
				if(provinces[i].equals(province_str))
				{
					province_index=i;
					break;
				}
			}
			province_spinner.setSelection(province_index);
			int curCities = cities[province_index];
			String[] strCities = getResources().getStringArray(curCities);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,android.R.id.text1,strCities);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			city_spinner.setAdapter(adapter);
			int city_index=0;
			for(int i=0;i<strCities.length;i++)
			{
				if(strCities[i].equals(city_str))
				{
					city_index=i;
					break;
				}
			}
			city_spinner.setSelection(city_index);
			
			String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
			selectBirthday.setText(birthday);
		}
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.user, menu);
	}
}
