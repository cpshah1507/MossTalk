package edu.cis350.mosstalkwords;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * Adapter for the EndSetActivity grid that shows the image, score, 
 * and favorite checkbox
 */

public class ImageAdapter extends BaseAdapter {
	private Context context; // context for inflater
	private int[] results; // scores (will be used to mark them correct/incorrect
	private ArrayList<String> imageNames; // list of names of the images
	private boolean[] checked;

	private boolean[] originallyChecked;

	private View gridView;
	Set currentSet;

	public ImageAdapter(Context ctx, Set s, String userName) {
		super();
		currentSet = s;
		context = ctx;
		results = currentSet.getScores();
		imageNames = new ArrayList<String>();
		for (String i : currentSet.getWords()) {
			imageNames.add(i);
		}
		checked = new boolean[results.length];
		originallyChecked = new boolean[results.length];

		setCheckBoxes();

	}

	public boolean[] getChecked() {
		return checked;
	}

	public void setCheckBoxes() {
		int count = 0;
		for (ImageStatistics l : currentSet.getImages()) {
			if (l.getIsFavorite())
				originallyChecked[count] = true;
			count++;
		}
	}

	public void setOriginalCheckBox(CheckBox c, int pos) {
		c.setChecked(originallyChecked[pos]);
		checked[pos] = originallyChecked[pos];
	}

	public boolean[] getOriginallyChecked() {
		return originallyChecked;
	}

	public int getCount() {
		return imageNames.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imageNames.get(position);
	}

	public boolean getCheckBox(int position) {

		return true;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			gridView = li.inflate(R.layout.favorite_image, null);
			
			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.item_image_label);
			textView.setText("" + results[position]);

			// 80 is perfect for words seen today, 100 is perfect otherwise
			if (results[position] >= 80) {
				// green
				gridView.findViewById(R.id.fav_item).setBackgroundResource(
						0x01060014);
			} else if (results[position] <= 20) {// if its wrong, 20 is for attempting
				// red
				gridView.findViewById(R.id.fav_item).setBackgroundResource(
						0x01060016);
			} else { // otherwise used hints and succeeded
				// yellow
				gridView.findViewById(R.id.fav_item).setBackgroundColor(
						0xFFFFFF00);
			}

			// set image based on selected text
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.item_image);

			CheckBox c = (CheckBox) gridView.findViewById(R.id.item_checkbox);

			final int a = position;
			setOriginalCheckBox(c, a);

			c.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (buttonView.isChecked()) {
						checked[a] = true;

					} else {
						checked[a] = false;

					}
				}
			});

			Bitmap im = ImageCache.getInstance().getBitmapFromCache(
					imageNames.get(position));

			Drawable drawableBitmap = new BitmapDrawable(
					context.getResources(), im);
			imageView.setImageDrawable(drawableBitmap);

		} else {
			gridView = convertView;
		}
		return gridView;

	}

}
