package edu.cis350.mosstalkwords.test;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;
import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

import com.amazonaws.services.simpledb.model.ReplaceableItem;

import edu.cis350.mosstalkwords.DatabaseHandler;
import edu.cis350.mosstalkwords.Image;
import edu.cis350.mosstalkwords.Images_SDB;
import edu.cis350.mosstalkwords.UserDataHandler;
import edu.cis350.mosstalkwords.UserStimuli;
import edu.cis350.mosstalkwords.WordQuest;


public class CategoryAndFavoritesTest extends AndroidTestCase {
	List<Image> images= new ArrayList<Image>();
	List<ReplaceableItem> sampleData=new ArrayList<ReplaceableItem>();

	
	Images_SDB testImageSDB =new Images_SDB();
	UserDataHandler usd = new UserDataHandler("testUser");
	
	
	/******* Test Cases for Categories *******/
	public void testreturn20Image() {
		String category="Living";
		images=testImageSDB.returnImages(category);
		int actual=images.size();
		Assert.assertTrue(actual == 20);
	}
	
	public void testreturnImageIfLessThan20() {
		String category="Tools";
		images=testImageSDB.returnImages(category);
		int actual=images.size();
		Assert.assertTrue(actual < 20);
	}
	
	public void testCategoryNull() {
		String category=null;
		images=testImageSDB.returnImages(category);
		int actual=images.size();
		Assert.assertTrue(actual == 0);
	}
	
	public void testCategoryNotPresent() {
		String category="ggh";
		images=testImageSDB.returnImages(category);
		int actual=images.size();
		Assert.assertTrue(actual == 0);
	}
	
	/******* Test Cases for Favorites *******/
	// Case when we get 20 favorite images using getFavoriteStimuli() and check for the size of returned list
	public void testGet20FavoriteImages()
	{
		 DatabaseHandler dbHandler = new DatabaseHandler(getContext()){
			 public void getTable(String abc){
				// To override default method
			}
			public List<UserStimuli> getFavoriteStimuli()
			{
				List<UserStimuli> sampleFavoriteImages = new ArrayList<UserStimuli>();
				Calendar cd = Calendar.getInstance();
				for(int i=0;i<20;i++)
				sampleFavoriteImages.add(new UserStimuli("Ant"+i,"insect",1,0,0,0,0,0,cd,0,"testURL"));
				return sampleFavoriteImages;
			}
		};
		
		List<UserStimuli> listOfReturnedImages = new ArrayList<UserStimuli>();
		
		listOfReturnedImages = dbHandler.getFavoriteStimuli();
		
		Assert.assertEquals(20, listOfReturnedImages.size());
	}
	//Case when we get 0 favorite images using getFavoriteStimuli() and check for the size of returned list
			public void testGet0FavoriteImages()
			{
				 DatabaseHandler dbHandler = new DatabaseHandler(getContext()){
					 public void getTable(String abc){
						// To override default method
					}
					public List<UserStimuli> getFavoriteStimuli()
					{
						List<UserStimuli> sampleFavoriteImages = new ArrayList<UserStimuli>();
						return sampleFavoriteImages;
					}
				};
				
				List<UserStimuli> listOfReturnedImages = new ArrayList<UserStimuli>();
				
				listOfReturnedImages = dbHandler.getFavoriteStimuli();
				
				Assert.assertEquals(0, listOfReturnedImages.size());
			}
	
	//Case when we get 5 favorite images using getFavoriteStimulus() and check for the size of returned list
	public void testgetFavoriteStimulusForFiveImages()
	{
		UserDataHandler usd = new UserDataHandler("testUser");
		
		DatabaseHandler dbHandler = new DatabaseHandler(getContext());
		dbHandler.deleteTable("testUser");
		dbHandler.getTable("testUser");
		
		UserStimuli sampleFavoriteImage = new UserStimuli();
		Calendar cd = Calendar.getInstance();
		for(int i=0;i<5;i++)
		{
			sampleFavoriteImage = new UserStimuli("Ant"+i,"insect",1,0,0,0,0,0,cd,0,"testURL");
			dbHandler.setStimuli(sampleFavoriteImage);
		}
		List<Image> listOfReturnedImages = new ArrayList<Image>();
		listOfReturnedImages = usd.getFavoriteStimulus(getContext());
		
		Assert.assertEquals(5, listOfReturnedImages.size());
	}
	
	//Case when we get 20 favorite images using getFavoriteStimulus() and check for the size of returned list
	public void testgetFavoriteStimulusForTwentyImages()
	{
		UserDataHandler usd = new UserDataHandler("testUser");
		
		DatabaseHandler dbHandler = new DatabaseHandler(getContext());
		dbHandler.deleteTable("testUser");
		dbHandler.getTable("testUser");
		
		UserStimuli sampleFavoriteImage = new UserStimuli();
		Calendar cd = Calendar.getInstance();
		for(int i=0;i<25;i++)
		{
			sampleFavoriteImage = new UserStimuli("Ant"+i,"insect",1,0,0,0,0,0,cd,0,"testURL");
			dbHandler.setStimuli(sampleFavoriteImage);
		}
		List<Image> listOfReturnedImages = new ArrayList<Image>();
		listOfReturnedImages = usd.getFavoriteStimulus(getContext());
		
		Assert.assertEquals(20, listOfReturnedImages.size());
	}
	
	//Case when we get 0 favorite images using getFavoriteStimulus() and check for the size of returned list
	public void testgetFavoriteStimulusForZeroImages()
	{
		UserDataHandler usd = new UserDataHandler("testUser");
		
		DatabaseHandler dbHandler = new DatabaseHandler(getContext());
		dbHandler.deleteTable("testUser");
		dbHandler.getTable("testUser");
		
		List<Image> listOfReturnedImages = new ArrayList<Image>();
		listOfReturnedImages = usd.getFavoriteStimulus(getContext());
		
		Assert.assertEquals(0, listOfReturnedImages.size());
	}

	public void testGetAllCategories(){
		List<String> categories = new ArrayList<String>();
		categories = testImageSDB.getAllCategories();
		for(String str:categories){
			System.out.println("Category: " + str);
		}
		
	}
}
