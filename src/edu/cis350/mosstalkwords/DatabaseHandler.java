package edu.cis350.mosstalkwords;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "mossWords";
 
    // We would be getting user name here by a function call and we would be using the corresponding user table
    private String userName;
    
    // Table name according to user name
    private String tableName;
 
    // Contacts Table Columns names
    private final String imageName = "imageName";
    private final String category = "category";
    private final String isFavorite = "isFavorite";
    private final String attempts = "attempts";
    private final String correctAttempts = "correctAttempts";
    private final String soundHints = "soundHints";
    private final String playwordHints = "playwordHints";
    private final String noHint = "noHint";
    private final String lastSeen = "lastSeen";
    private final String difficulty = "difficulty";
    private final String url = "url";
    
    public void getTable(String uname) {
            userName = uname.toLowerCase();
            tableName = userName + "_tableName";
            SQLiteDatabase db = this.getReadableDatabase();
            String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + tableName + "("
                + imageName + " TEXT PRIMARY KEY," +category + " TEXT, "+ isFavorite + " INTEGER," + attempts + " INTEGER,"
                + correctAttempts + " INTEGER," + soundHints + " INTEGER," + playwordHints + " INTEGER," + noHint + " INTEGER,"
                + lastSeen + " TEXT," + difficulty + " REAL," + url + " TEXT)";
            
            db.execSQL(CREATE_USER_TABLE);
    }
  
    public void deleteTable(String uname){
 	   userName = uname.toLowerCase();
 	   tableName = userName + "_tableName";
 	   SQLiteDatabase db = this.getWritableDatabase();
       
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + tableName;
        db.execSQL(DROP_USER_TABLE);
    }
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public UserStimuli getUserStimuli(String imageName) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	UserStimuli us = null;
    	
    	String getUserStimuli = "select * from " + tableName + " where imageName = '" + imageName + "'";
		Cursor cursor = db.rawQuery(getUserStimuli, null);
		
        if(cursor == null)
        {
        	return null;
        }
      
        if (cursor.moveToFirst() && cursor != null) {
	    try
	    {
	        us = new UserStimuli();
	    	us.setImageName(cursor.getString(0));
	        us.setCategory(cursor.getString(1));
	        us.setIsFavorite(Integer.parseInt(cursor.getString(2)));
	        us.setAttempts(Integer.parseInt(cursor.getString(3)));
	        us.setCorrectAttempts(Integer.parseInt(cursor.getString(4)));
	        us.setSoundHints(Integer.parseInt(cursor.getString(5)));
	        us.setPlaywordHints(Integer.parseInt(cursor.getString(6)));
	        us.setNoHint(Integer.parseInt(cursor.getString(7)));
	                 
	                        Calendar cd = Calendar.getInstance();
	                        cd.setTime(new Date(cursor.getString(8)));
	        us.setLastSeen(cd);
	        us.setDifficulty(Double.parseDouble(cursor.getString(9)));
	        us.setUrl(cursor.getString(10));
	        
	        }
	        catch(Exception e)
	        {
	                Log.d("Exception in parsing",e.toString());
	        }
		}
		return us;
    }
 
    public List<UserStimuli> getFavoriteStimuli()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        List<UserStimuli> usList = new ArrayList<UserStimuli>();
        
        Cursor cursor = db.query(tableName, null, isFavorite + "=?", new String[] {"1"}, null, null, null,null);
                
        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        
	    if (cursor.moveToFirst()) {
	    do{
            try
            {
            UserStimuli us = new UserStimuli();
            
            us.setImageName(cursor.getString(0));
            us.setCategory(cursor.getString(1));
            us.setIsFavorite(Integer.parseInt(cursor.getString(2)));
            us.setAttempts(Integer.parseInt(cursor.getString(3)));
            us.setCorrectAttempts(Integer.parseInt(cursor.getString(4)));
            us.setSoundHints(Integer.parseInt(cursor.getString(5)));
            us.setPlaywordHints(Integer.parseInt(cursor.getString(6)));
            us.setNoHint(Integer.parseInt(cursor.getString(7)));
                     
                            Calendar cd = Calendar.getInstance();
                            cd.setTime(new Date(cursor.getString(8)));
            us.setLastSeen(cd);
            us.setDifficulty(Double.parseDouble(cursor.getString(9)));
            us.setUrl(cursor.getString(10));
            
            usList.add(us);}
            catch(Exception e)
            {
                    Log.d("Exception in parsing",e.toString());
            }
         } while (cursor.moveToNext());
        }
        return usList;
    }
        
        
    public void setStimuli(UserStimuli stimuli){
		SQLiteDatabase db = this.getWritableDatabase();
		//Cursor cursor = db.query(tableName, null,imageName+ "=?",new String[] {stimuli.getImageName()}, null, null, null);
		String getUserStimuli = "select * from " + tableName + " where imageName = '" + stimuli.getImageName() + "'";
		Cursor cursor = db.rawQuery(getUserStimuli, null);
		
		
		ContentValues values = new ContentValues();
	            
		values.put(imageName, stimuli.getImageName());
		values.put(category, stimuli.getCategory());
	    values.put(isFavorite, stimuli.getIsFavorite());
	    values.put(attempts, stimuli.getAttempts());
	    values.put(correctAttempts, stimuli.getCorrectAttempts());
	    values.put(soundHints, stimuli.getSoundHints());
	    values.put(playwordHints, stimuli.getPlaywordHints());
	    values.put(noHint, stimuli.getNoHint());
	    values.put(lastSeen, stimuli.getLastSeen().getTime().toString());
	    values.put(difficulty, stimuli.getDifficulty());
	    values.put(url, stimuli.getUrl());
	   
	    //Log.d("cursor rows",Integer.toString(cursor.getCount()));
	  
	    //if (cursor.getCount() == 0)
	    if(cursor == null)
	    	return;
	    
	    if(cursor.moveToFirst() && cursor != null)
	    {
	    	db.update(tableName, values, imageName+"=?", new String[] {stimuli.getImageName()});
	    }
	    else {
	    	db.insert(tableName, null, values);
	        Log.d("cursor null","adding an image");
	    }
	    db.close(); // Closing database connection
	            
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
                // TODO Auto-generated method stub
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                        // TODO Auto-generated method stub
        	}
}