package edu.cis350.mosstalkwords;

import java.util.ArrayList;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/*
 * CategoryListActivity is to represent the category list module
 */
public class CategoryListActivity extends UserActivity {

	// The data to show
	ArrayList<Category> categoryList;

	public void startMain(String categoryName) {
		Intent activityMain = new Intent(this, MainActivity.class);
		activityMain.putExtra("startCategory", categoryName);
		startActivity(activityMain);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_list);

		new LoadCategories().execute(this);
	}

	//dyanmically get all of the categories and update the categoryList
	private class LoadCategories extends
			AsyncTask<CategoryListActivity, Category, Void> {
		@Override
		protected Void doInBackground(CategoryListActivity... params) {
			// TODO Auto-generated method stub
			ImageManager im = new ImageManager(getUserName(),
					getApplicationContext());
			categoryList = new ArrayList<Category>();

			for (String s : im.img_SDB.getAllCategories()) {
				publishProgress(new Category(-1, s));
			}
			return null;
		}

		@Override
		public void onProgressUpdate(Category... progress) {
			categoryList.add(progress[0]);
		}

		@Override
		public void onPostExecute(Void v) {
			ListView lv = (ListView) findViewById(R.id.category_list);

			lv.setAdapter(new CategoryListAdapter(CategoryListActivity.this
					.getApplicationContext(), R.layout.row_image, categoryList));
			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, final View view,
						int position, long id) {
					startMain(categoryList.get(position).getName());
					finish();
				}
			});
		}
	}
}
