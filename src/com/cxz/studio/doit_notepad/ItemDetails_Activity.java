package com.cxz.studio.doit_notepad;

import com.cxz.studio.utils.Utils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ItemDetails_Activity extends Activity {
	private EditText title;
	private EditText details;
	private TextView timeView;
	
	private Button cancelBtn;
	private Button okBtn;
	private Button finishBtn;
	private Button delBtn;
	private long id;	
	private int status;
	private long tmpTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_details_layout);
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(R.string.itemdetails_title);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			id = extras.getLong(ItemsContentProvider.KEY_ID);
		} else {
			finish();
		}
		
		title = (EditText)findViewById(R.id.title2_EditText);
		details = (EditText)findViewById(R.id.detail2_EditText);
		timeView = (TextView)findViewById(R.id.time2_TextView);
		
		ContentResolver cr = getContentResolver();
		Cursor result = 
			      cr.query(ContentUris.withAppendedId(ItemsContentProvider.CONTENT_URI, id),
			               null, null, null, null);
		if (result.moveToFirst()) {
			String titleStr = result.getString(result.getColumnIndex(ItemsContentProvider.KEY_TITLE));
			String detailsStr = result.getString(result.getColumnIndex(ItemsContentProvider.KEY_DETAILS));

			title.setText(titleStr);
			details.setText(detailsStr);
			status = result.getInt(result.getColumnIndex(ItemsContentProvider.KEY_STATUS));
			if (status == Constant.TODO_LIST) {
				tmpTime = result.getLong(result.getColumnIndex(ItemsContentProvider.KEY_CREATE_TIME));
			} else if (status == Constant.FINISH_LIST) {
				tmpTime = result.getLong(result.getColumnIndex(ItemsContentProvider.KEY_FINISH_TIME));
			}
			String strTime = Utils.parseLong2DateTime(tmpTime, Constant.YMDHMS);
			timeView.setText(strTime);
		}
		
		cancelBtn = (Button)findViewById(R.id.Cancel2_Button);
		okBtn = (Button)findViewById(R.id.OK2_Button);
		finishBtn = (Button)findViewById(R.id.Finish2_Button);
		delBtn = (Button)findViewById(R.id.Delete2_Button);
		
		if (Constant.FINISH_LIST == status) {
			finishBtn.setText("Redo");
		}
		
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
				
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
				
				Uri uri = ContentUris.withAppendedId(ItemsContentProvider.CONTENT_URI, id);
				cr.update(uri,
						  values,
						  null,
						  null);
				
				finish();
			}
		});
		
		
		finishBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String titleStr = title.getText().toString();
				String detailsStr = details.getText().toString();
				
				if (null == titleStr || 0 == titleStr.length()) {
					Toast.makeText(getApplicationContext(), 
								   "Please input title!", 
								   Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (null == detailsStr || 0 == detailsStr.length()) {
					Toast.makeText(getApplicationContext(), 
							   "Please input details!", 
							   Toast.LENGTH_SHORT).show();
					return;
				}
				
				ContentResolver cr = getContentResolver();
				ContentValues values = new ContentValues();
				values.put(ItemsContentProvider.KEY_TITLE, titleStr);
				values.put(ItemsContentProvider.KEY_DETAILS, detailsStr);
				values.put(ItemsContentProvider.KEY_FINISH_TIME, System.currentTimeMillis());
				status = status ^ Constant.FINISH_LIST;
				values.put(ItemsContentProvider.KEY_STATUS, status);
				
				Uri uri = ContentUris.withAppendedId(ItemsContentProvider.CONTENT_URI, id);
				cr.update(uri,
						  values,
						  null,
						  null);
				
				finish();
			}
		});
		
		
		delBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Uri uri = ContentUris.withAppendedId(ItemsContentProvider.CONTENT_URI, id);
				ContentResolver cr = getContentResolver();
				cr.delete(uri, null, null);
				
				finish();
			}
		});
	}
		
}
