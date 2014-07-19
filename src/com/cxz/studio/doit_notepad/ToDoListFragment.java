package com.cxz.studio.doit_notepad;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ToDoListFragment extends ListFragment implements LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter adapter;
	private int statusFlag;
	private OnGetStatusListener onGetStatusListener;
	  
	public interface OnGetStatusListener {
	    public int getStatus();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		adapter = new SimpleCursorAdapter(getActivity(),
			      android.R.layout.simple_list_item_1, null,
			      new String[] { ItemsContentProvider.KEY_TITLE },
			      new int[] { android.R.id.text1 }, 0);
		
	    setListAdapter(adapter);
	    getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onResume() {
		super.onResume();
		getLoaderManager().restartLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = new String[] {
			ItemsContentProvider.KEY_ID,
			ItemsContentProvider.KEY_TITLE
	    };
		
		String where = ItemsContentProvider.KEY_STATUS + " = " + statusFlag;
		String orderBy;
		if (statusFlag == Constant.TODO_LIST) {
			orderBy = ItemsContentProvider.KEY_CREATE_TIME;
		} else if (statusFlag == Constant.FINISH_LIST) {
			orderBy = ItemsContentProvider.KEY_FINISH_TIME + " desc";
		} else {
			orderBy = null;
		}
		
		CursorLoader loader = new CursorLoader(getActivity(),
											   ItemsContentProvider.CONTENT_URI, 
											   projection,
											   where,
											   null,
											   orderBy);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		adapter.swapCursor(cursor);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Intent intent = new Intent();
		intent.setClass(getActivity(), ItemDetails_Activity.class);
		intent.putExtra(ItemsContentProvider.KEY_ID, id);
		startActivity(intent);
	}
	
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	      
	    try {
	        onGetStatusListener = (OnGetStatusListener)activity;
	    } catch (ClassCastException e) {
	        throw new ClassCastException(activity.toString() + 
	            " must implement OnGetStatusListener");
	    }
	    
	    statusFlag = onGetStatusListener.getStatus();
	}
}
