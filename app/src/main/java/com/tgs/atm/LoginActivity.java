package com.tgs.atm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tgs.atm.util.ConnectionDetector;
import com.tgs.atm.util.GPSTracker;

public class LoginActivity extends ActionBarActivity {

	Button btn_Login=null;
	
	
	EditText et_UserName=null;
	EditText et_Password=null;
	String strUsername="", str_pref_UserName="";
	String strPassword="", str_pref_Password;
	SharedPreferences mypref=null;
	GPSTracker gpsTracker;
	ConnectionDetector condetec;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		et_UserName=(EditText) findViewById(R.id.et_username);
		et_Password=(EditText) findViewById(R.id.et_pwd);
		btn_Login=(Button) findViewById(R.id.btn_login);
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Login");
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.iconbg));
		mypref=getSharedPreferences("Credentials", 0);
		str_pref_UserName=mypref.getString("UserName", "");
		str_pref_Password=mypref.getString("Password", "");	
		if(str_pref_UserName.equals(""))
			btn_Login.setText("Register");
		else
			btn_Login.setText("Login");
		gpsTracker=new GPSTracker(LoginActivity.this);	
		 condetec=new ConnectionDetector(LoginActivity.this);
		btn_Login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				strUsername=et_UserName.getText().toString().trim();
				strPassword=et_Password.getText().toString().trim();
				
				
			//	Log.d("Username and Password in Shared Prefs", "Username:"+str_pref_UserName+" password:"+str_pref_Password);
				
				if(strUsername.equals(""))
				{

					Toast.makeText(LoginActivity.this, "Please enter the username", Toast.LENGTH_LONG).show();
				
				}
				else
					if(strPassword.equals(""))
					{
						Toast.makeText(LoginActivity.this, "Please enter the password", Toast.LENGTH_LONG).show();
					}
					else
					{
						if(str_pref_UserName.equals(""))
						{
							if(!condetec.isConnectingToInternet())
							{	
								Toast.makeText(LoginActivity.this, "No internet connection. Your device is currently not connected to internet. Please try again.", Toast.LENGTH_LONG).show();
								return; 
							}
							Log.d("Prefs is empty", "Writing data into prefs");
							Editor e= mypref.edit();
							e.putString("UserName", strUsername);
							e.putString("Password", strPassword);
						    e.commit();
						    

							Intent mapIntent=new Intent(LoginActivity.this, MainActivity.class);

							startActivity(mapIntent);
							
						
						}
						else
						{
							if(strUsername.equalsIgnoreCase("admin") && strPassword.equalsIgnoreCase("123456"))
							{
								if(!condetec.isConnectingToInternet())
								{	
									Toast.makeText(LoginActivity.this, "No internet connection. Your device is currently not connected to internet. Please try again.", Toast.LENGTH_LONG).show();
									return; 
								}
								if(!gpsTracker.canGetLocation)
								{
									gpsTracker.showSettingsAlert();
									return;
								}
								if(gpsTracker.getLatitude()==0.0)
								{
								 gpsTracker.improveLocation();
								 return;
								}
								
								
								Intent mapIntent=new Intent(LoginActivity.this, MainActivity.class);

								startActivity(mapIntent);
							}
							else
							{
								

								Toast.makeText(LoginActivity.this, "Invalid credentials, please check and try again", Toast.LENGTH_LONG).show();
							}
						}
					}
			}
		});
		
		
	}
@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	gpsTracker.getLocation();
	Log.d("Loc data","lat:"+gpsTracker.getLatitude());
}
	
}
