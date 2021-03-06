package com.mobicanavigator.view;

import com.main.campusnavigator.R;
import com.mobicanavigator.model.Office;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MenuView extends LinearLayout{

	public MenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
@Override
protected void onFinishInflate() {
	// TODO Auto-generated method stub
	super.onFinishInflate();
	
	RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radio_mobica_office);
	RadioButton radioButton;
	for(Office office : Office.values()){
		radioButton = new RadioButton(getContext());
		radioButton.setText(office.toString());
		radioGroup.addView(radioButton);
	}
}
}
