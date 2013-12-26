package edu.cis350.mosstalkwords.test;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.os.Parcel;

import edu.cis350.mosstalkwords.ImageStatistics;
import edu.cis350.mosstalkwords.Set;

import junit.framework.TestCase;

public class SetTest extends TestCase {

	private ImageStatistics img1, img2, img3;
	private ArrayList<ImageStatistics> imgSet;
	private Set s;
	
	public void setUp(){
		imgSet = new ArrayList<ImageStatistics>();
		
		// int wordHints, int soundHints, int attempts
		// 80, 32, 60, 20
		img1 = new ImageStatistics("imgName1", "url1", "category1", true, 0, 0, 1, true, true, Calendar.getInstance());
		img2 = new ImageStatistics("imgName2", "url2", "category2", false, 2, 1, 4, true, true, Calendar.getInstance());
		img3 = new ImageStatistics("imgName3", "url3", "category3", true, 0, 3, 2, true, false, Calendar.getInstance());
		
		imgSet.add(img1);
		imgSet.add(img2);
		imgSet.add(img3);
		s = new Set(imgSet);
	}
	
	public void testWriteToParcel() {
		// Write to Parcel 
		Bundle b = new Bundle();
		b.putParcelable("tagForSet", s);
		Parcel p = Parcel.obtain();
		b.writeToParcel(p, 0);
		// Read from Parcel 
		p.setDataPosition(0);
	    Bundle b2 = p.readBundle();
	    b2.setClassLoader(Set.class.getClassLoader());
	    Set s2 = b2.getParcelable("tagForSet");
	    
	    // Compare original and recreated
 		assertEquals(s, s2);
	}

	public void testGetWords() {
		String[] words = s.getWords();
		assertEquals(words.length, 3);
		assertEquals(words[0], "imgName1");
		assertEquals(words[1], "imgName2");
		assertEquals(words[2], "imgName3");
	}

	public void testGet() {
		assertEquals(s.get(0), img1);
		assertEquals(s.get(1), img2);
		assertEquals(s.get(2), img3);
	}

	public void testGetWord() {
		assertEquals(s.getWord(0), "imgName1");
		assertEquals(s.getWord(1), "imgName2");
		assertEquals(s.getWord(2), "imgName3");
	}

	public void testGetSize() {
		assertEquals(s.getSize(), 3);
	}
	
	public void testGetTotalScore() {
		assertEquals(s.getTotalScore(), 172);
	}

	public void testGetScores() {
		int[] scores = s.getScores();
		assertEquals(scores[0], 80);
		assertEquals(scores[1], 32);
		assertEquals(scores[2], 60);
	}

	public void testGetStarScore() {
		assertEquals(s.getStarScore(), 3);
	}

	public void testGetCompleteness() {
		assertEquals((int)(s.getCompleteness() * 100), 57);
	}

	public void testIncWordHint() {
		s.incWordHint(1);
		assertEquals(s.get(1).getWordHints(), 3);
	}

	public void testSetSolved() {
		s.setSolved(0, false);
		assertFalse(s.get(0).isSolved());
	}

	public void testIncAttempts() {
		s.incAttempts(1);
		assertEquals(s.get(1).getAttempts(), 5);
	}

	public void testIncSoundHint() {
		s.incSoundHint(1);
		assertEquals(s.get(1).getSoundHints(), 2);
	}

	public void testSetLastSeen() {
		Calendar c = Calendar.getInstance();
		s.setLastSeen(0, c.getTimeInMillis());
		assertEquals(s.get(0).getLastSeen(), c);
	}

	public void testGetScoreIntBoolean() {
		ImageStatistics img4 = new ImageStatistics("imgName3", "url3", "category3", true, 1, 0, 1, false, true, Calendar.getInstance());
		ImageStatistics img5 = new ImageStatistics("imgName1", "url1", "category1", true, 0, 0, 1, true, false, Calendar.getInstance());
		ImageStatistics img6 = new ImageStatistics("imgName2", "url2", "category2", false, 2, 1, 4, true, false, Calendar.getInstance());
		ImageStatistics img7 = new ImageStatistics("imgName3", "url3", "category3", true, 0, 3, 2, true, true, Calendar.getInstance());
		imgSet.add(img4);
		imgSet.add(img5);
		imgSet.add(img6);
		imgSet.add(img7);
		s = new Set(imgSet);
		assertEquals(s.getScore(0, false), 80);
		assertEquals(s.getScore(1, false), 32);
		assertEquals(s.getScore(2, false), 60);
		assertEquals(s.getScore(3, false), 20);
		assertEquals(s.getScore(4, false), 100);
		assertEquals(s.getScore(5, false), 40);
		assertEquals(s.getScore(6, false), 48);
		assertEquals(s.getScore(3, true), 32);
		
	}

	public void testGetTotalScoreInt() {
		assertEquals(s.getTotalScore(2), 112);
	}

	public void testGetLongestStreak() {
		assertEquals(s.getLongestStreak(), 3);
		s.setSolved(2, false);
		assertEquals(s.getLongestStreak(), 2);
	}

	public void testEqualsObject() {
		ArrayList<ImageStatistics> imgSet1 = new ArrayList<ImageStatistics>();
		ImageStatistics img11 = new ImageStatistics("imgName1", "url1", "category1", true, 0, 0, 1, true, true, Calendar.getInstance());
		ImageStatistics img21 = new ImageStatistics("imgName2", "url2", "category2", false, 2, 1, 4, true, true, Calendar.getInstance());
		ImageStatistics img31 = new ImageStatistics("imgName3", "url3", "category3", true, 0, 3, 2, true, false, Calendar.getInstance());
		
		imgSet1.add(img11);
		imgSet1.add(img21);
		imgSet1.add(img31);
		Set s1 = new Set(imgSet1);
		assertTrue(s.equals(s1));
	}
	
}


