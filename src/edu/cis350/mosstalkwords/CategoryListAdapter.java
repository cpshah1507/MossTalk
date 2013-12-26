package edu.cis350.mosstalkwords;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * CategoryListAdapter is used to add listener to category module
 */
public class CategoryListAdapter extends ArrayAdapter<Category> {

	private Context context;
	private int layoutResourceId;
	private ArrayList<Category> categoryList;

	public CategoryListAdapter(Context context, int layoutResourceId,
			ArrayList<Category> categoryList) {

		super(context, layoutResourceId, categoryList);
		
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.categoryList = categoryList;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		CategoryHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new CategoryHolder();

			//no icons yet.
			//holder.imgIcon = (ImageView) row.findViewById(R.id.icon);
			holder.txtTitle = (TextView) row.findViewById(R.id.name);

			row.setTag(holder);
		} else {
			holder = (CategoryHolder) row.getTag();
		}

		Category category = categoryList.get(position);
		holder.txtTitle.setText(category.getName());
		
		//no icons yet
		//holder.imgIcon.setImageResource(category.getIcon());
		
		return row;
	}

	static class CategoryHolder {
		ImageView imgIcon;
		TextView txtTitle;
	}
}
