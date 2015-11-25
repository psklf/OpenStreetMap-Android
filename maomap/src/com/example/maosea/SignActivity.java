package com.example.maosea;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignActivity extends Activity implements OnClickListener {
	
	Button btnsignin; 
	Button btnsignup;
	public EditText signname;
	public EditText password;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_layout);
		
		btnsignin = (Button) findViewById(R.id.btn_signin);
		btnsignup = (Button) findViewById(R.id.btn_signup);
		
		signname = (EditText) findViewById(R.id.edit_logname);
		password = (EditText) findViewById(R.id.edit_pw);

		
		btnsignin.setOnClickListener(this);
		btnsignup.setOnClickListener(this);
		
	}
	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_signin:
			mlogin();
			break;
		case R.id.btn_signup:
			msignup();
		break;
		
		default:
			break;
		
		}
	}

	private void msignup(){
		
		BmobUser bu = new BmobUser();
		//toast(signname.getText().toString());
		
		bu.setUsername(signname.getText().toString());
		bu.setPassword(password.getText().toString());
		//bu.setEmail("sendi@163.com");
		bu.signUp(this, new SaveListener() {
		    @Override
		    public void onSuccess() {
		        // TODO Auto-generated method stub
		        toast("sign up success!");
		    }
		    @Override
		    public void onFailure(int code, String msg) {
		        // TODO Auto-generated method stub
		        toast("sign up fail:"+msg);
		    }
		});
	}

	private void mlogin() {
		// TODO Auto-generated method stub
		final BmobUser bu2 = new BmobUser();
		bu2.setUsername(signname.getText().toString());
		bu2.setPassword(password.getText().toString());
		bu2.login(this, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast(" "+bu2.getUsername() + "sign in success!");
				testGetCurrentUser();
				Intent intent = new Intent();
				intent.putExtra("username", bu2.getUsername());
				setResult(RESULT_OK, intent);
				finish();
				
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("sign in fail:" + msg);
			}
		});
	}
	
	private void testGetCurrentUser() {
		BmobUser bu = BmobUser.getCurrentUser(this, BmobUser.class);
		if (bu != null) {
			Log.i("life",":objectId = " + bu.getObjectId() + ",name = " + bu.getUsername());
		} else {
			toast("null,please sign in!");
		}

	}
	
	//toast
	public void toast(String string) {
		// TODO Auto-generated method stub		    	
    		Toast.makeText(SignActivity.this, string, Toast.LENGTH_SHORT).show();
    		Log.d("signtoast", string);			    	
	}


}
