package cn.edu.nuist.baidumap3;

import cn.edu.nuist.baidumap3.InputMethodRelativeLayout.OnSizeChangedListenner;
import android.app.Activity;

import android.app.AlertDialog.Builder;
import android.content.ContentResolver;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class LoginActivity extends Activity implements OnSizeChangedListenner{
	private InputMethodRelativeLayout layout;  
    private LinearLayout boot ;
    private LinearLayout login_logo_layout_h ;
    private LinearLayout login_logo_layout_v ,login_container ;
    
    private Button loginButton,registerButton,OutButton;
    private static EditText username,password; 
    private ButtonListener b1=new ButtonListener();
    private Intent intent;
    private String info="";
    private String s;
    private Builder builder; 
    private ContentResolver contentResolver;
	private static String CONTENT_URI_USER="content://cn.edu.nuist.baidumap3.MySqliteContentProvider/user";
    
    
    
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		contentResolver=getContentResolver();
		//取得InputMethodRelativeLayout组件
		layout = (InputMethodRelativeLayout) this.findViewById(R.id.loginpage) ;
		//设置监听事件
        layout.setOnSizeChangedListenner(this) ;
        //取得大LOGO布局
        login_logo_layout_v = (LinearLayout) this.findViewById(R.id.login_logo_layout_v) ;
        //取得小LOGO布局
        login_logo_layout_h = (LinearLayout) this.findViewById(R.id.login_logo_layout_h) ;
      
        login_container = (LinearLayout) this.findViewById(R.id.login_container) ;
        
        //取得找回密码和新注册布局
        boot = (LinearLayout) this.findViewById(R.id.reg_and_forget_password_layout) ;
        
        loginButton=(Button)findViewById(R.id.login_btn);  //登录
        loginButton.setOnClickListener(b1); 

        

        registerButton=(Button)findViewById(R.id.reg_tv);  //注册
        registerButton.setOnClickListener(b1); 

  

      

       username=(EditText)findViewById(R.id.qqId);        //用户名
       password=(EditText)findViewById(R.id.passWord);   //密码

     

       OutButton=(Button)findViewById(R.id.forget_password_tv);        //退出登录
       OutButton.setOnClickListener(new View.OnClickListener(){
    	   public void onClick(View  v) {
    		
    		  intent=new Intent();
    		  intent.setClass(LoginActivity.this,MyMapActivity.class);
    		  LoginActivity.this.finish();
       		 
    	   } 
    		  
       });
      
	}
	
	
	public class ButtonListener implements View.OnClickListener{ 

		public boolean login(String str1,String str2){
			
			
			
			Uri uri=Uri.parse(CONTENT_URI_USER);
			Cursor cursor = contentResolver.query(uri, null, "username=? and pwd=?", new String[]{str1,str2}, null);
			if(cursor.moveToNext())
			{
				return true;
			}
			else
		
		return false;
		
     }

		@Override
		public void onClick(View v) {
			if(v==loginButton){
                   if((username.getText().toString()).equals("")||(password.getText().toString()).equals("")){
                	   Toast.makeText(getApplicationContext(),"用户名或密码不可为空",Toast.LENGTH_SHORT).show();
                	   return;
                	   }
                	if(login(username.getText().toString(),password.getText().toString())){
     
                		   s=username.getText().toString();
                           intent=new Intent();
                           intent.putExtra("userName", s);
                           Toast.makeText(LoginActivity.this,"登录成功"+info,Toast.LENGTH_SHORT).show();
                           intent.setClass(LoginActivity.this,MyMapActivity.class);
                           startActivity(intent);//  
                           LoginActivity.this.finish();
                           overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                           password.setText("");
                      }else{
                        	   Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                        	   }
             }
			else if(v==registerButton){ 
			        	   intent=new Intent();
			        	   intent.setClass(LoginActivity.this, RegActivity.class);
                	  
			                 startActivity(intent);
			}
			
		
		}
		
		
		
		
	}

		  
	
	

	/**
	 * 在Activity中实现OnSizeChangedListener，原理是设置该布局的paddingTop属性来控制子View的偏移
	 */
	public void onSizeChange(boolean flag,int w ,int h) {  
        if(flag){//键盘弹出时
        	Animation anim=AnimationUtils.loadAnimation(LoginActivity.this, R.anim.login_anim);
    		anim.setFillAfter(true);
    		login_container.startAnimation(anim);
          //  layout.setPadding(0, -10, 0, 0);   
            boot.setVisibility(View.GONE) ;
            login_logo_layout_v.setVisibility(View.GONE) ;
            login_logo_layout_h.setVisibility(View.VISIBLE) ;
        }else{ //键盘隐藏时
    		
            layout.setPadding(0, 0, 0, 0); 
            boot.setVisibility(View.VISIBLE) ;
            login_logo_layout_v.setVisibility(View.VISIBLE) ;
            login_logo_layout_h.setVisibility(View.GONE) ;
        }
    }  
 
	
}

