package com.cxz.studio.doit_notepad;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AddNewItemFragment extends Fragment {
	
	public static final int NEW_ITEM = 1;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
							 ViewGroup container,
		                     Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_item_fragment, container, false);
		final Button newItemButton  = (Button)view.findViewById(R.id.newItemButton);
		newItemButton.setText("Add New Item");
		
		newItemButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), NewItem_Activity.class);
				startActivityForResult(intent, NEW_ITEM);
			}
		});
		
		return view;
	}
}
