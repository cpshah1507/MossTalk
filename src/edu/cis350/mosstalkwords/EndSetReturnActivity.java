package edu.cis350.mosstalkwords;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/*
 * EndSetReturnActivity simply shows a dialog that allows the user
 * to replay the set, return to main menu, or go to the next set
 */

public class EndSetReturnActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Context mContext = this;

		//dialog that shows |main menu | replay | next set|
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setCancelable(false);
		builder.setTitle("Play Again?")
				.setNeutralButton(R.string.restart,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// restart set
								Intent activityMain = new Intent(
										EndSetReturnActivity.this,
										MainActivity.class);
								Set s = EndSetReturnActivity.this.getIntent()
										.getExtras()
										.getParcelable("currentSet");
								s.resetImagesStatistics();
								activityMain.putExtra("currentSet", s);
								populateIntentModeData(activityMain);
								startActivity(activityMain);
								finish();
							}
						})
				.setPositiveButton(R.string.nextSet,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// next set
								Intent activityMain = new Intent(
										EndSetReturnActivity.this,
										MainActivity.class);
								// need to make main activity exit.

								// fix this, need to close main activity.
								freeCacheMem();

								populateIntentModeData(activityMain);
								startActivity(activityMain);

								finish();
							}
						})
				.setNegativeButton(R.string.menu,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// go to main menu
								Intent menu = new Intent(
										EndSetReturnActivity.this,
										WelcomeActivity.class);

								menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

								freeCacheMem();

								startActivity(menu);
								finish();
							}
						});

		AlertDialog alert = builder.create();// create the AlertDialog object
												// and return it
		alert.show();
	}

	// free the ImageCache's memory, 
	private void freeCacheMem() {
		Set s = EndSetReturnActivity.this.getIntent().getExtras()
				.getParcelable("currentSet");

		String[] temp = s.getWords();
		String[] del = new String[temp.length];

		for (int z = 0; z < del.length; z++)
			del[z] = temp[z];

		ImageCache.getInstance().clearCache(del);
	}

	private void populateIntentModeData(Intent i) {
		i.putExtra("mode", this.getIntent().getIntExtra("mode", -1));
		i.putExtra("wordQuestLevel",
				this.getIntent().getIntExtra("wordQuestLevel", -1));
		i.putExtra("categoryName",
				this.getIntent().getStringExtra("categoryName"));
	}
}
