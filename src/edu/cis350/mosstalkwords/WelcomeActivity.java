package edu.cis350.mosstalkwords;

import java.io.File;
import java.util.Date;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/*
 * WelcomeActivity is the screen that lets the user pick the module to play
 * Also allows the user to reset score and send a formatted html document
 * of information in the wordquest database in email form.
 */


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WelcomeActivity extends UserActivity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
	}
	
	public void onStart(){
		super.onStart();
		if(!isLoggedIn()){
			Intent gotoBase = new Intent(this, BaseActivity.class);
			startActivity(gotoBase);
		}
	}
		
	public void onResume(){
		super.onResume();
		TextView v = (TextView) findViewById(R.id.txtScore);
		v.setText(getScore() + " Points");
	}
	
	public void openWordQuest(View v){
		Intent activityWordQuest = new Intent(this, WordQuestActivity.class);
		startActivity(activityWordQuest);
	}
	
	public void openFavourites(View v){
		Intent activityMain= new Intent(this, MainActivity.class);
		activityMain.putExtra("startFavourites", true);
		startActivity(activityMain);
	}
	
	public void openCategories(View v){
		Intent i = new Intent(this, CategoryListActivity.class);	
		startActivity(i);
		finish();
	}
	
	public void resetUserData(View v) {
		new AlertDialog.Builder(this)
				.setTitle("Reset User Data")
				.setMessage("Are you sure you want to reset all user data? This cannot be undone.")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						logout();
						new AlertDialog.Builder(WelcomeActivity.this)
								.setTitle("Successfully reset")
								.setMessage("All user data has been successfully reset. ")
								.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
										Intent gotoBaseMenu = new Intent(WelcomeActivity.this, BaseActivity.class);
										startActivity(gotoBaseMenu);
									}
								})
								.setCancelable(false)
								.show();
					}
				})
				.setNegativeButton("No", null)
				.show();
	}
	
	public void sendDatabase(View v){
		File fileName = null;
		ImageManager im = new ImageManager(getUserName(), getApplicationContext());
		
		try {
			fileName = im.wq.generateWordQuestReport(getUserName());
			String email = getEmail();
			
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			
			emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, 
					new String[]{email});
			String subject="Wordle Report " + new Date().toString();
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
			String body= "Your report is attached below. Good Work!";
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
			emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileName));
			emailIntent.setType("vnd.android.cursor.dir/vnd.google.note");
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		} catch (Exception e) {
			new AlertDialog.Builder(this)
			.setTitle("Send Report")
			.setMessage("You cannot send the report for Word Quest until you have played it.")
			.setNeutralButton("OK", null)
			.show();
		}
	}
	
}
