package com.cxz.studio.doit_notepad;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class DoItList_Activity extends Activity implements
	ToDoListFragment.OnGetStatusListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doitlist_layout);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(R.string.todolist_title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.do_it_list_, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		switch (item.getItemId()) {
	      case (R.id.action_about): {
	    	  Intent intent = new Intent();
	    	  intent.setClass(this, About_Activity.class);
	    	  startActivity(intent);
	    	  break;
	      }
	      case (R.id.action_finished): {
	    	  Intent intent = new Intent();
	    	  intent.setClass(this, Finished_List_Activity.class);
	    	  startActivity(intent);
	    	  break;
	      }
	      case (R.id.action_exit): {
	    	  finish();
	    	  break;
	      }
	      default: return false;
		}
		return true;
	}
	
	private long mExitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > Constant.PRESS_AGAIN_EXIT_INTERVAL) {
                    Toast.makeText(this, R.string.press_again_exit, 
                    		Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
            } else {
                    finish();
            }
            return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public int getStatus() {
		return Constant.TODO_LIST;
	}
}
