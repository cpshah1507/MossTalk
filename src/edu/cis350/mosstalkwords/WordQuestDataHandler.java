package edu.cis350.mosstalkwords;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WordQuestDataHandler extends SQLiteOpenHelper {
	
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "mossWords";
 
    public WordQuestDataHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // We would be getting user name here by a function call and we would be using the corresponding user table
    private  String userName;
    
    // Contacts table name
    // Table name according to user name
    private String tableName;
 
    // Contacts Table Columns names
    private final String level = "level";
    private final String itemName = "itemName";
    private final String weight = "weight";
    private final String unassistedGreaterThan24 = "unassistedGreaterThan24";
    private final String countForPenalty = "countForPenalty";
    private final String unassistedLessThan24 = "unassistedLessThan24";
    private final String assisted = "assisted";
    private final String lastTimeTrialNum = "lastTimeTrialNum";
    private final String lifeTimeTrialNum = "lifetimeTrialNum";
    private final String lastSeen = "lastSeen";
    private final String progress = "progress";
    private final String url = "url";
    
    private double progressTable[][] = new double[][]
    	{   {20,15,10,5},
    		{18,13.5,9,4.5},
    		{16,12,8,4},
    		{14,10.5,7,3.5},
    		{12,9,6,3},
    		{10,7.5,5,2.5},
    		{8,6,4,2},
    		{6,4.5,3,1.5},
    		{4,3,2,1},
    		{2,1.5,1,0.5}
    	};
    
    public void createTable(String uname) {
    	
    	userName = uname.toLowerCase();
    	tableName = userName + "_wordQuest";
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	String CREATE_WORD_QUEST_TABLE = "CREATE TABLE IF NOT EXISTS " + tableName + "("
                + level + " INTEGER," + itemName + " TEXT, "+ weight + " REAL," + unassistedGreaterThan24 + " INTEGER,"
                + countForPenalty + " INTEGER," + unassistedLessThan24 + " INTEGER," + assisted + " INTEGER," + lastTimeTrialNum + " INTEGER,"
                + lifeTimeTrialNum + " INTEGER," + lastSeen + " TEXT," + progress + " REAL," + url + " TEXT"+")";
    	
    	db.execSQL(CREATE_WORD_QUEST_TABLE);
    }
  
    public void updateWQTable(List<Image> imgList){
    	SQLiteDatabase db = this.getWritableDatabase();
    	if(imgList != null)
    	{
	    	for(Image img:imgList){
				
				String checkImageExists = "select itemName, url, level from " + tableName + " where itemName = '" + img.getWord() + "'";
	    		Cursor cursorCheckImageExists = db.rawQuery(checkImageExists, null);
	    		
	    		if(cursorCheckImageExists != null && cursorCheckImageExists.moveToFirst())
	    		{
	    			if(!(cursorCheckImageExists.getString(0).equals(img.getWord()) && cursorCheckImageExists.getString(1).equals(img.getUrl()) && Integer.parseInt(cursorCheckImageExists.getString(2)) == img.getLevel()))
	    			{
	    				String updateWQImage = "udpate " + tableName + " set url = '" + img.getUrl() + "' where itemName = '" + img.getWord() + "'";
	    				db.execSQL(updateWQImage);
	    			}
	    		}
	    		else
	    		{
	    			ContentValues values = new ContentValues();
	    			
	    			values.put(level,img.getLevel());
	    			//values.put(level,1);
	    			
					values.put(itemName,img.getWord());
			        values.put(weight, 1); 
			        values.put(unassistedGreaterThan24,0);
			        values.put(countForPenalty,0);
			        values.put(unassistedLessThan24,0);
			        values.put(assisted,0);
			        values.put(lastTimeTrialNum,0);
			        values.put(lifeTimeTrialNum,0);
			        Date d = new Date(0);
			        //d.toString()
			        values.put(lastSeen,d.toString());
			        values.put(progress,0);
			        values.put(url,img.getUrl());
			        
			        db.insert(tableName, null, values);
	    		}
			}	
    	}
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    
    public void dropTable(String tableName)
    {
    	SQLiteDatabase db = this.getReadableDatabase();
    	String DELETE_WORD_QUEST_TABLE = "DROP TABLE IF EXISTS " + tableName;
    	db.execSQL(DELETE_WORD_QUEST_TABLE);
    }
   
    public boolean updateWordQuest(List<ImageStatistics> imgStatList,int currentLevel)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	ImageStatistics imgStat = new ImageStatistics();
    	String imagesPlayed = "";
    	int lifeTimeTrialNum = 0;
    	
    	lifeTimeTrialNum = updateLifeTimeTrial(imgStatList.get(0).getImageName());
    	if(lifeTimeTrialNum == -1)
    	{
    		Log.d("Error","Error in updating Life Time Trial");
    		return false;
    	}
    	
    	for(int i=0;i<imgStatList.size();i++)
    	{
    		imgStat = imgStatList.get(i);
    		System.out.println("Image: " + imgStat.getImageName());
    		double prevProgess = 0;
    		int prevunassistedGreaterThan24 = 0;
    		int prevUnassistedLessThan24 = 0;
    		int prevAssisted = 0;
    		int prevPenalty = 0;
    		
    		double weight = 0;
    		int performanceOutcome = 0;
    		int unassistedGreaterThan24 = 0;
    		int countForPenalty = 0;
    		int unassistedLessThan24 = 0;
    		int assisted = 0;
    		int lastTimeTrialNum = 0;
    		
    		String lastSeen;
    		double progress = 0;
    		
    		if(imagesPlayed.length() == 0)
    			imagesPlayed = "'" + imgStat.getImageName() + "'";
    		else
    			imagesPlayed = imagesPlayed + ", " + "'" + imgStat.getImageName() + "'";
    		
    		/* To Decide Performance Outcome */
    		performanceOutcome = getPerformanceOutcome(imgStat);
    		/* Update Weight based on performance Outcome */
    		weight = updateWeight(performanceOutcome);
    		String getPrevData = "select progress,unassistedGreaterThan24,countForPenalty,unassistedLessThan24,assisted from " + tableName + " where itemName = '"+ imgStat.getImageName() + "'";
    		Cursor cursorPrevData = db.rawQuery(getPrevData, null);
    		
    		if(cursorPrevData != null)
    		{
    			cursorPrevData.moveToFirst();
    			prevProgess = Double.parseDouble(cursorPrevData.getString(0));
    			prevunassistedGreaterThan24 = Integer.parseInt(cursorPrevData.getString(1));
    			prevPenalty = Integer.parseInt(cursorPrevData.getString(2));
    			prevUnassistedLessThan24 = Integer.parseInt(cursorPrevData.getString(3));
    			prevAssisted = Integer.parseInt(cursorPrevData.getString(4));
    		}
    		else
    			return false;
    		
    		unassistedGreaterThan24 = prevunassistedGreaterThan24 + (performanceOutcome==0 ? 1: 0);
    		unassistedLessThan24 = prevUnassistedLessThan24 + (performanceOutcome==1 ? 1: 0);
    		assisted = prevAssisted + (performanceOutcome==1 ? 2: 0);
    		
    		lastTimeTrialNum = (lifeTimeTrialNum - 20) + (i+1);
    		
    		lastSeen = imgStat.getLastSeen().getTime().toString();
    		
    		/* For updating penalty box count */
    		if(performanceOutcome == 0 && prevPenalty < 5)
    			countForPenalty = prevPenalty + 1;
    		
    		
    		if(countForPenalty == 5)
    			weight = 0;
    		
    		progress = calculateProgress(prevProgess, performanceOutcome);
    		String updateWordQuest = "update "+ tableName +
    				" set weight = "+ weight +
    				", unassistedGreaterThan24 = " + unassistedGreaterThan24 +
    				", countForPenalty = " + countForPenalty +
    				", unassistedLessThan24 = " + unassistedLessThan24 +
    				", assisted = " + assisted +
    				", lastTimeTrialNum = " + lastTimeTrialNum +
    				", lastSeen = '" + lastSeen +
    				"', progress = " + progress + " where itemName = '" + imgStat.getImageName() + "'";
    		System.out.println(updateWordQuest);
    		db.execSQL(updateWordQuest);
    	}
    	
    	if(!updateRemainingImages(lifeTimeTrialNum, imagesPlayed, currentLevel))
    		{
    			Log.d("Error","Could not save remaining Images");
    			return false;
    		}
    	db.close();
    	return true;
    }
    
    public int getPerformanceOutcome(ImageStatistics imgStat){
    	int performanceOutcome = 0;
    	if(imgStat.isSolved())
		{
    		if(imgStat.getSoundHints() == 0 && imgStat.getWordHints() == 0 && imgStat.isSeenToday() == false)
				performanceOutcome = 0;
			else if(imgStat.getSoundHints() == 0 && imgStat.getWordHints() == 0 && imgStat.isSeenToday() == true)
				performanceOutcome = 1;
			else if(imgStat.getSoundHints() > 0 || imgStat.getWordHints() > 0)
				performanceOutcome = 2;
		}
		else
			performanceOutcome = 3;
    	
    	return performanceOutcome;
    }
    public int updateLifeTimeTrial(String imageName)
    {
    	SQLiteDatabase db = this.getWritableDatabase(); 
    	int prevLifeTimeTrialNum = 0;
    	int lifeTimeTrialNum = 0;
    	
    	String getLifeTimeTrials = "select lifeTimeTrialNum from " + tableName + " where itemName = '"+ imageName + "'";
		Cursor cursorGetLifeTime = db.rawQuery(getLifeTimeTrials, null);
		if(cursorGetLifeTime != null)
		{
			cursorGetLifeTime.moveToFirst();
			prevLifeTimeTrialNum = Integer.parseInt(cursorGetLifeTime.getString(0));
			lifeTimeTrialNum = prevLifeTimeTrialNum + 20;
		}
		else
			return -1;
		
		String setNewLifeTimeTrials = "update " + tableName + " set lifeTimeTrialNum = " + lifeTimeTrialNum;
		db.execSQL(setNewLifeTimeTrials);
		
		return lifeTimeTrialNum;
    }
   
    public boolean updateRemainingImages(int lifeTimeTrialNum,String imagesPlayed,int currentLevel)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	int remainingImagesLastTrial = 0;
    	int remainingCountForPenalty = 0;
    	String getRemainingImagesData = "select itemName, weight, lastTimeTrialNum, countForPenalty from "+ tableName + " where level = " + currentLevel + " and itemName NOT IN ("+imagesPlayed+")";
    	System.out.println(getRemainingImagesData);
    	Cursor cursorRemainingImages = db.rawQuery(getRemainingImagesData, null);
    	if(cursorRemainingImages!=null && cursorRemainingImages.getCount()!=0)
    	{
    		cursorRemainingImages.moveToFirst();
    		do
    		{
    			
    			double weight = Double.parseDouble(cursorRemainingImages.getString(1));
    			remainingImagesLastTrial = Integer.parseInt(cursorRemainingImages.getString(2));
    			remainingCountForPenalty = Integer.parseInt(cursorRemainingImages.getString(3));
    			/* Checking whether to get the image out of penalty box */
    			if((lifeTimeTrialNum - remainingImagesLastTrial >= 200) && remainingCountForPenalty == 5)
    				remainingCountForPenalty = 4;
    			
    			/* Don't increase the weight of item if it is inside penalty box */
    			if(remainingCountForPenalty != 5)
    				weight += 0.001;
    			
    			String updateOtherImages = "update "+ tableName + " set weight = " + weight + ", countForPenalty = " + remainingCountForPenalty + " where itemName = '" + cursorRemainingImages.getString(0) + "'";
    			
    			db.execSQL(updateOtherImages);
    		}while(cursorRemainingImages.moveToNext());
    		//db.close();
    		return true;
    	}
    	else
    	{
    		//db.close();
    		return false;
    	}
    	
    }
    
    public double calculateProgress(double prevProgress,int performanceOutcome) {
		if(prevProgress < 100)
		{
			double newProgress = prevProgress+progressTable[(int)(prevProgress/10)][performanceOutcome];
		
			if(newProgress <= 100)
				return (prevProgress+progressTable[(int)(prevProgress/10)][performanceOutcome]);
			else
				return 100.0;
		}
		else
			return prevProgress;
	}

	public double updateWeight(int performanceOutcome)
    {
    	double weight = 0;
    	if(performanceOutcome == 0)
    		weight = 0.6;
    	else if(performanceOutcome == 1)
    		weight = 0.8;
    	else if(performanceOutcome == 2)
    		weight = 0.9;
    	else if(performanceOutcome == 3)
    		weight = 0.95;
    	
    	return weight;
    }
    
}