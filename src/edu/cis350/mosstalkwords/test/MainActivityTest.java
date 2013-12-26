package edu.cis350.mosstalkwords.test;

import java.util.ArrayList;
import edu.cis350.mosstalkwords.ImageStatistics;
import edu.cis350.mosstalkwords.MainActivity;
import edu.cis350.mosstalkwords.Set;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	public MainActivityTest() {
		super(edu.cis350.mosstalkwords.MainActivity.class);
	}

	private MainActivity mA;

	protected void setUp() throws Exception {
		super.setUp();
		Intent i = new Intent();
		i.putExtra("startCategory", "Living");

		this.setActivityIntent(i);
		mA = this.getActivity();
	}

	public void testOnCreate() {
		assertNotNull(mA.getIntent());
	}

	public void testSetUserBundleStorage() {
		mA.mode = 0;
		mA.imageIndex = 0;
		mA.difficultyLevel = 0;
		mA.currentSet = new Set(new ArrayList<ImageStatistics>());
		mA.categoryName = "A";

		final Bundle bundle = new Bundle();

		getInstrumentation().runOnMainSync(new Runnable() {
			public void run() {
				mA.onSaveInstanceState(bundle);
			}
		});

		mA.mode = 1;
		mA.imageIndex = 1;
		mA.difficultyLevel = 1;
		mA.currentSet = null;
		mA.categoryName = "B";

		mA.onRestoreInstanceState(bundle);

		assertTrue(mA.mode == 0);
		assertTrue(mA.imageIndex == 0);
		assertTrue(mA.difficultyLevel == 0);
		assertTrue(mA.currentSet != null && mA.currentSet.getSize() == 0);
		assertTrue(mA.categoryName.equals("A"));
		assertTrue(mA.mode == 0);
	}
}