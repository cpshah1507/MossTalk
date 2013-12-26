package edu.cis350.mosstalkwords;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;

public class UserDataHandler {
	String userName;
	public UserDataHandler(String name)
	{
		userName = name;
	}
	public List<Image> getFavoriteStimulus(Context context)
	{
		List<UserStimuli> usList = new ArrayList<UserStimuli>();
		DatabaseHandler dbHandler = new DatabaseHandler(context);
		List<Image> imgList = new ArrayList<Image>();

		dbHandler.getTable(userName);

		usList = dbHandler.getFavoriteStimuli();

		Collections.shuffle(usList);
		System.out.println(usList.size());
		if(usList.size() > 0)
		{
			for(int i=0;i < 20;i++)
			{
				Image img = new Image(usList.get(i).getImageName(), usList.get(i).getCategory(), 
						"0", "0", "0", "0", usList.get(i).getUrl());
				imgList.add(img);
				if(i+1 == 20 || i+1 == usList.size())
					break;
			}
		}
		return imgList;

	}
}
