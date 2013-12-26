package edu.cis350.mosstalkwords.test;

import edu.cis350.mosstalkwords.Image;
import android.os.Bundle;
import android.os.Parcel;
import android.test.AndroidTestCase;

public class ImageTest extends AndroidTestCase {

	private Image img;
	
	public void setUp(){
		img = new Image("Elephant", "Living", "20", "30","1","40", "http://www.google.com/elephant.png");
	}
	
	public void testWriteToParcel() {
		// Write to Parcel 
		Bundle b = new Bundle();
		b.putParcelable("tagForImg", img);
		Parcel p = Parcel.obtain();
		b.writeToParcel(p, 0);
		// Read from Parcel 
		p.setDataPosition(0);
	    Bundle b2 = p.readBundle();
	    b2.setClassLoader(Image.class.getClassLoader());
	    Image img2 = b2.getParcelable("tagForImg");
	    
	    // Compare original and recreated
 		assertEquals(img, img2);
	}

	public void testImage() {
		assertEquals(img.getWord(), "Elephant");
		assertEquals(img.getCategory(), "Living");
		assertEquals(img.getImageability(), 20);
		assertEquals(img.getLength(), 30);
		assertEquals(img.getFrequency(), 40);
		assertEquals(img.getUrl(), "http://www.google.com/elephant.png");
	}

	public void testToggleFavourite() {
		img.setFavourite(true);
		assertTrue(img.isFavourite());
		img.toggleFavourite();
		assertFalse(img.isFavourite());
		img.toggleFavourite();
		assertTrue(img.isFavourite());
		img.toggleFavourite();
		img.toggleFavourite();
		assertTrue(img.isFavourite());
	}

	public void testEquals(){
		Image img2 = new Image("Elephant", "Living", "20", "30","1", "40", "http://www.google.com/elephant.png");
		assertEquals(img, img2);
	}

}
