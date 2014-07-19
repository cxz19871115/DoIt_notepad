package com.cxz.studio.doit_notepad;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewItem_Activity extends Activity {
	
	EditText title;
	EditText details;
	
	Button cancelBtn;
	Button okBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_item_layout);
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(R.string.newitem_title);
		
		title = (EditText)findViewById(R.id.title_EditText);
		details = (EditText)findViewById(R.id.detail_EditText);
		
		cancelBtn = (Button)findViewById(R.id.Cancel_Button);
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		okBtn = (Button)findViewById(R.id.OK_Button);
		okBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String titleStr = title.getText().toString();
				String detailsStr = details.getText().toString();
				
				if (null == titleStr || 0 == titleStr.length()) {
					Toast.makeText(getApplicationContext(), 
								   "Please input title!", 
								   Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (titleStr.length() > Constant.TITLE_MAX_LENGTH) {
					Toast.makeText(getApplicationContext(), 
							   "Title is too long.", 
							   Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (null == detailsStr || 0 == detailsStr.length()) {
					Toast.makeText(getApplicationContext(), 
							   "Please input details!", 
							   Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (detailsStr.length() > Constant.DETAILS_MAX_LENGTH) {
					Toast.makeText(getApplicationContext(), 
							   "Details is too long.", 
							   Toast.LENGTH_SHORT).show();
					return;
				}
				
				ContentResolver cr = getContentResolver();
				ContentValues values = new ContentValues();
				values.put(ItemsContentProvider.KEY_TITLE, titleStr);
				values.put(ItemsContentProvider.KEY_DETAILS, detailsStr);
				values.put(ItemsContentProvider.KEY_STATUS, Constant.TODO_LIST);
				values.put(ItemsContentProvider.KEY_CREATE_TIME, System.currentTimeMillis());
				values.put(ItemsContentProvider.KEY_FINISH_TIME, 0);
				values.put(ItemsContentProvider.KEY_ALARM, 0);
				values.put(ItemsContentProvider.KEY_CATEGORY, -1);
				
				cr.insert(ItemsContentProvider.CONTENT_URI, values);
				
				finish();
			}
		});
	}
	
}
