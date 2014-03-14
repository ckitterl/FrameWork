package com.larry.layouttemplate;

import java.io.File;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.nd.ndFrame.action.Action;
import com.nd.ndFrame.layoutTemplate.IDLLayoutTemplate;
import com.nd.ndFrame.layoutTemplate.LayoutTemplate;

public class MainActivity extends Activity {
	private int[] colors = { Color.BLUE, Color.CYAN, Color.RED };
	private int count = 0;
	private static final String TAG = "LayoutTemplate";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		File file = new File(Environment.getExternalStorageDirectory(), "member.xml");
		File root = Environment.getExternalStorageDirectory();
		Log.d(TAG, file.getPath() + " : " + file.exists() + " root: " + root.getPath());

		LayoutTemplate layoutTemplate = new LayoutTemplate(this,
				new DLLayoutTemplate());
		layoutTemplate.loadLayout(file);
		View contentView = layoutTemplate.getRootView();
		setContentView(contentView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public class DLLayoutTemplate implements IDLLayoutTemplate {

		@Override
		public String valueForField(String field, String datasource) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String nameForField(String field, String datasource) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String pathForField(String field, String datasource) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Action actionForField(String field, String datasource) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public View viewForField(String field, String datasource) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
