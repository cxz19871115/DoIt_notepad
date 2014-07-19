package com.cxz.studio.doit_notepad;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;

public class Finished_List_Activity extends Activity implements
	ToDoListFragment.OnGetStatusListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finished_list_layout);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(R.string.finishedlist_title);
	}

	@Override
	public int getStatus() {
		return Constant.FINISH_LIST;
	}
}
