package cn.edu.nuist.baidumap3;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class RegActivity extends Activity {

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

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_reg);
		init();
	}

	public void init() {
		province_spinner = (Spinner) findViewById(R.id.province_spinner);
		city_spinner = (Spinner) findViewById(R.id.city_spinner);
		
		OnItemSelectedListener listener = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				cityArrayPosition = position;
				int curCities = cities[position];
				String[] strCities = getResources().getStringArray(curCities);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegActivity.this, android.R.layout.simple_spinner_item,android.R.id.text1,strCities);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				city_spinner.setAdapter(adapter);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		};
		province_spinner.setOnItemSelectedListener(listener);
		
		selectBirthday = (Button)findViewById(R.id.selectbirthday_but);
		OnClickListener listener2 = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.getId()==selectBirthday.getId())
				{
					DatePickerDialog dpd = new DatePickerDialog(RegActivity.this, new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear,
								int dayOfMonth) {
							selectBirthday.setText(""+year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
						}
					}, 1990, 0, 1);
					dpd.show();
				}else if(v.getId()==login_but.getId()){
					Intent intent=new Intent();
		        	   intent.setClass(RegActivity.this, LoginActivity.class);
		        	   RegActivity.this.finish();
		                 startActivity(intent);
					
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
					
					 
					Uri newUri = contentResolver.insert(uri, values);
					System.out.println("::"+newUri.getLastPathSegment());
					String idStr = newUri.getLastPathSegment();
					if(idStr!=null&&!idStr.equals(""))
					{
						try
						{
							int idInt = Integer.parseInt(idStr);
							Toast toast = Toast.makeText(RegActivity.this, "添加成功", Toast.LENGTH_LONG);
							toast.show();
						}
						catch(Exception ex)
						{
							Toast toast = Toast.makeText(RegActivity.this, "添加失败", Toast.LENGTH_LONG);
							toast.show();
						}
					}
				}
			}
		};
		selectBirthday.setOnClickListener(listener2);
		reg_but =(Button)findViewById(R.id.reg_but);
		reg_but.setOnClickListener(listener2);
		login_but =(Button)findViewById(R.id.login_but);
		login_but.setOnClickListener(listener2);
		
		username_edit=(EditText)findViewById(R.id.username_edit);
		password_edit=(EditText)findViewById(R.id.password_edit);
		repassword_edit=(EditText)findViewById(R.id.repassword_edit);
		male_radio=(RadioButton)findViewById(R.id.male_radio);
		female_radio=(RadioButton)findViewById(R.id.female_radio);
		playball_check=(CheckBox)findViewById(R.id.playball_check);
		playcard_check=(CheckBox)findViewById(R.id.playcard_check);
		playgame_check=(CheckBox)findViewById(R.id.playgame_check);
		
		contentResolver=getContentResolver();
	}
}
