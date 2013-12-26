package edu.cis350.mosstalkwords;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

/*
 * Provides an means for all activites to access sharedpreferences 
 * for score, email, and user name
 */

public class UserActivity extends Activity {

	private final String NAME_KEY = "name";
	private final String EMAIL_KEY = "email";
	private final String SCORE_KEY = "totalScore";
	private final String PREFERENCE_NAME = "UserPreferences";
	private SharedPreferences userData;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userData = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
	}
	
	public boolean setUserName(String name){
		if(!validName(name))
			return false;
		
		Editor editor = userData.edit();
		editor.putString(NAME_KEY, name);
		editor.commit();
		return true;
	}
	
	public boolean setEmail(String email){
		if(!validEmailAddress(email))
			return false;
			
		Editor editor = userData.edit();
		editor.putString(EMAIL_KEY, email);
		editor.commit();
		return true;
	}
	
	public boolean setScore(int score){
		Editor editor = userData.edit();
		editor.putInt(SCORE_KEY, score);
		editor.commit();
		return true;
	} 
	
	public boolean login(String name, String email){
		if(!validName(name) || !validEmailAddress(email))
			return false;
		
		Editor editor = userData.edit();
		editor.putString(NAME_KEY, name);
		editor.putString(EMAIL_KEY, email);
		editor.putInt(SCORE_KEY, 0);
		editor.commit();
		
		return true;
	}
	
	public void logout(){
		Editor editor = userData.edit();
		editor.putString(NAME_KEY, null);
		editor.putString(EMAIL_KEY, null);
		editor.putInt(SCORE_KEY, 0);
		editor.commit();
	} 
	
	public boolean isLoggedIn(){
		return getUserName() != null && getEmail() != null;
	}
	
	public String getUserName(){
		return userData.getString(NAME_KEY, null);
	}
	
	public String getEmail(){
		return userData.getString(EMAIL_KEY, null);
	}
	
	public int getScore(){
		return userData.getInt(SCORE_KEY, -1);
	}
	
	private boolean validName(String name){
		if(name == null || name.indexOf(" ") != -1)
			return false;
		return !name.equals("");
	}
	
	private boolean validEmailAddress(String mail){
		if(mail == null)
			return false;
		return android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches();
	}
}
