package edu.cis350.mosstalkwords.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import edu.cis350.mosstalkwords.ImageManager;
import edu.cis350.mosstalkwords.ImageStatistics;

public class WordQuestTest extends AndroidTestCase {
	
	/******* Test Cases for WordQuest *******/
	// Testing getImagesForWordQuest for level 1
	public void testGetImagesForWQLevel1(){
		int[] levels = new int[4];
		ImageManager im = new ImageManager("test_user",getContext());
		SQLiteDatabase db = im.wqHandler.getReadableDatabase();
		List<ImageStatistics> imList = new ArrayList<ImageStatistics>();
		imList = im.getImagesForWordQuest(1);
		for(ImageStatistics img: imList){
			String imageName = img.getImageName();
			String getImageData = "select level from test_user_wordQuest where itemName = '" + imageName+ "'";
			Cursor cursor = db.rawQuery(getImageData, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				levels[Integer.parseInt(cursor.getString(0))-1]++;
			}
		}
		
		assertEquals(imList.size(), 20);
		assertEquals(levels[0],20);
	}
	
	public void testGetImagesForWQLevel2(){
		int[] levels = new int[4];
		ImageManager im = new ImageManager("test_user",getContext());
		SQLiteDatabase db = im.wqHandler.getReadableDatabase();
		List<ImageStatistics> imList = new ArrayList<ImageStatistics>();
		imList = im.getImagesForWordQuest(2);
		for(ImageStatistics img: imList){
			String imageName = img.getImageName();
			String getImageData = "select level from test_user_wordQuest where itemName = '" + imageName+ "'";
			Cursor cursor = db.rawQuery(getImageData, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				levels[Integer.parseInt(cursor.getString(0))-1]++;
			}
		}
		
		assertEquals(imList.size(), 20);
		assertEquals(levels[0],1);
		assertEquals(levels[1],19);
	}
	
	public void testGetImagesForWQLevel3(){
		int[] levels = new int[4];
		ImageManager im = new ImageManager("test_user",getContext());
		SQLiteDatabase db = im.wqHandler.getReadableDatabase();
		List<ImageStatistics> imList = new ArrayList<ImageStatistics>();
		imList = im.getImagesForWordQuest(3);
		for(ImageStatistics img: imList){
			String imageName = img.getImageName();
			String getImageData = "select level from test_user_wordQuest where itemName = '" + imageName+ "'";
			Cursor cursor = db.rawQuery(getImageData, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				levels[Integer.parseInt(cursor.getString(0))-1]++;
			}
		}
		
		assertEquals(imList.size(), 20);
		assertEquals(levels[0],1);
		assertEquals(levels[1],1);
		assertEquals(levels[2],18);
	}
	
	public void testGetImagesForWQLevel4(){
		int[] levels = new int[4];
		ImageManager im = new ImageManager("test_user",getContext());
		SQLiteDatabase db = im.wqHandler.getReadableDatabase();
		List<ImageStatistics> imList = new ArrayList<ImageStatistics>();
		imList = im.getImagesForWordQuest(4);
		for(ImageStatistics img: imList){
			String imageName = img.getImageName();
			String getImageData = "select level from test_user_wordQuest where itemName = '" + imageName+ "'";
			Cursor cursor = db.rawQuery(getImageData, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				levels[Integer.parseInt(cursor.getString(0))-1]++;
			}
		}
		
		assertEquals(imList.size(), 20);
		assertEquals(levels[0],1);
		assertEquals(levels[1],1);
		assertEquals(levels[2],1);
		assertEquals(levels[3],17);
	}
	public void testUpdateWeight(){
		ImageManager im = new ImageManager("test_user",getContext());
		double weight = im.wqHandler.updateWeight(1);
		assertEquals(0.8, weight);
	}
	
	public void testCalculateProgress(){
		ImageManager im = new ImageManager("test_user",getContext());
		double progress = im.wqHandler.calculateProgress(5,0);
		assertEquals(25.0,progress);
	}
	
	public void testGetPerformanceOutcome(){
		ImageManager im = new ImageManager("test_user",getContext());
		ImageStatistics imgStat = new ImageStatistics();
		
		imgStat.setSeenToday(true);
		imgStat.setSoundHints(0);
		imgStat.setWordHints(0);
		imgStat.setSolved(true);
		int result = im.wqHandler.getPerformanceOutcome(imgStat);
		assertEquals(1, result);
	}
	
	
	
	// Testing updateWordQuest 
	public void testUpdateWordQuest(){
		ImageManager im = new ImageManager("test_user",getContext());
		List<ImageStatistics> imList = new ArrayList<ImageStatistics>();
		
		SQLiteDatabase db = im.wqHandler.getWritableDatabase();
		
		imList = im.getImagesForWordQuest(1);
		
		String firstImageName = imList.get(0).getImageName();
		System.out.println("Image to be debugged: " + firstImageName);
		double FirstImageWeight = 0;
		
		imList.get(0).setAttempts(1);
		imList.get(0).setSolved(true);
	
		imList.get(0).setSeenToday(false);
		imList.get(0).setSoundHints(0);
		imList.get(0).setWordHints(0);
		
		im.updateWordQuest(imList, 1);
		//imList = im.getImagesForWordQuest(1);
		String getNewImageData = "select weight from test_user_wordQuest where itemName = '" + firstImageName + "'";
		Cursor cursorNewData = db.rawQuery(getNewImageData, null);
		if(cursorNewData != null)
		{
			cursorNewData.moveToFirst();
			FirstImageWeight = Double.parseDouble(cursorNewData.getString(0));
			//System.out.println(cursorNewData.getString(1));
		}
		
		assertEquals(0.6,FirstImageWeight);
	}
	/*for(int i=1;i<=4;i++)
	{
			for(int j=1;j<=100;j++)
			{
				Date d = new Date(0);
				String insertData = "insert into test_user_wordQuest values ("+i+",'testImage"+Integer.toString(j+(i-1)*100)+"',1,0,0,0,0,0,0,'"+d.toString()+"',0,'testURL.jpg')";
				db.execSQL(insertData);
			}
	}*/
	
	// Testing Level Unlocking
	public void testGetLevelsForWordQuest(){
		ImageManager im = new ImageManager("test_user",getContext());
		List<ImageStatistics> imList = new ArrayList<ImageStatistics>();
		SQLiteDatabase db = im.wqHandler.getWritableDatabase();
		imList = im.getImagesForWordQuest(1);
		for(int i=0;i<10;i++)
		{
			String updateData = "update test_user_wordQuest set progress = 90, unassistedGreaterThan24 = 8 where itemName = '"+ imList.get(i).getImageName() +"'";
			db.execSQL(updateData);
		}
		int levels = im.getLevelsForWordQuest();
		assertEquals(2, levels);
	}
		
	// Penalty Box Testing
	public void testPenaltyBox(){
		ImageManager im = new ImageManager("test_user",getContext());
		SQLiteDatabase db = im.wqHandler.getWritableDatabase();
		
		String setNewImageData = "update test_user_wordQuest set countForPenalty = 0 , unassistedGreaterThan24 = 0 where level = 1";
		db.execSQL(setNewImageData);
		
		for(int i=0;i<5;i++)
		{
			List<ImageStatistics> imList = new ArrayList<ImageStatistics>();
			imList = im.getImagesForWordQuest(1);
			for(int j=0;j<20;j++)
			{	
				imList.get(j).setAttempts(1);
				imList.get(j).setSolved(true);
				imList.get(j).setSeenToday(false);
				imList.get(j).setSoundHints(0);
				imList.get(j).setWordHints(0);
			}
			im.updateWordQuest(imList, 1);
		}
		List<ImageStatistics> imList = new ArrayList<ImageStatistics>();
		imList = im.getImagesForWordQuest(1);
		
		Double weight = -1.0;
		int countForPenalty = -1;
		String getNewImageData = "select weight,countForPenalty from test_user_wordQuest where itemName = '" + imList.get(0).getImageName() + "'";
		Cursor cursorNewData = db.rawQuery(getNewImageData, null);
		if(cursorNewData != null)
		{
			cursorNewData.moveToFirst();
			weight = Double.parseDouble(cursorNewData.getString(0));
			countForPenalty = Integer.parseInt(cursorNewData.getString(1));
		}
		
		assertEquals(0.0, weight);
		assertEquals(5, countForPenalty);
		
	}
	
	// Penalty Box Testing
	/*public void testGetOutOfPenaltyBox(){
		ImageManager im = new ImageManager("test_user",getContext());
		SQLiteDatabase db = im.wqHandler.getWritableDatabase();
		
		String setNewImageData = "update test_user_wordQuest set weight = 0, countForPenalty = 5 , unassistedGreaterThan24 = 0 where level = 1";
		db.execSQL(setNewImageData);
		
		List<ImageStatistics> imList = new ArrayList<ImageStatistics>();
		imList = im.getImagesForWordQuest(1);
		for(int j=0;j<20;j++)
		{	
			imList.get(j).setAttempts(1);
			imList.get(j).setSolved(true);
			imList.get(j).setSeenToday(false);
			imList.get(j).setSoundHints(0);
			imList.get(j).setWordHints(0);
		}
		im.updateWordQuest(imList, 1);
		
		List<ImageStatistics> newimList = new ArrayList<ImageStatistics>();
		newimList = im.getImagesForWordQuest(1);
		
		Double weight = -1.0;
		int countForPenalty = -1;
		String getNewImageData = "select weight,countForPenalty from test_user_wordQuest where itemName = '" + newimList.get(0).getImageName() + "'";
		Cursor cursorNewData = db.rawQuery(getNewImageData, null);
		if(cursorNewData != null)
		{
			cursorNewData.moveToFirst();
			weight = Double.parseDouble(cursorNewData.getString(0));
			countForPenalty = Integer.parseInt(cursorNewData.getString(1));
		}
		
		assertEquals(0.6, weight);
		assertEquals(4, countForPenalty);
		
	}*/
		
}
