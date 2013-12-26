package edu.cis350.mosstalkwords;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/*
 * BaseActivity is for login
 * Only shows up the first time or after the user resets his data.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BaseActivity extends UserActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base);
	}

	public void onStart(){
		super.onStart();
		if(isLoggedIn()){
			Intent gotoMainMenu = new Intent(this, WelcomeActivity.class);
			startActivity(gotoMainMenu);
		}
	}
		
	public void login(View v) {

		EditText n = (EditText)findViewById(R.id.baseName);
		EditText e = (EditText)findViewById(R.id.baseEmail);

		String name = n.getText().toString();
		String email = e.getText().toString();

		if(login(name, email)){
			Intent gotoMainMenu = new Intent(this, WelcomeActivity.class);
			gotoMainMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			gotoMainMenu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(gotoMainMenu);
		}
		else{
			new AlertDialog.Builder(this)
			.setTitle("Your input")
			.setMessage("Please enter a user name and a valid email address.")
			.setPositiveButton("OK", null)
			.show();
		}
	}
}
