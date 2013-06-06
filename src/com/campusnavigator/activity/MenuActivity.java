package com.campusnavigator.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ThreadFactory;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.campusnavigator.model.PointerModel;
import com.campusnavigator.view.MenuView;
import com.main.campusnavigator.R;

public class MenuActivity extends MainActivity implements OnClickListener, OnCheckedChangeListener {

	private View view;
	
	private PointerModel menumodel;
	private ListView officeListView;
	private Map<String, float[]> officesMap;
	private float [] officeDirection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = (MenuView)View.inflate(this, R.layout.menu_layout, null);
		setContentView(view);
		
		officesMap = new HashMap<String, float[]>();
		officeDirection = new float[2];
		float[] officeLocArray = new float[2];
		
		officeLocArray[0] = 19.449553728627507f;
		officeLocArray[1] = 51.745472398279915f;
		officesMap.put("Lodz office", officeLocArray);
		
		
		officeLocArray = new float[2];
		officeLocArray[0] = 14.556139f;
		officeLocArray[1] = 53.429152f;
		officesMap.put("Szczecin office", officeLocArray);
		

/*		officeListView = (ListView) findViewById(R.id.office_list);
		
		ArrayAdapter<String> arrayAdapter =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MenuModel.getInstance().getListOffice());
		officeListView.setAdapter(arrayAdapter);

		
		officeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub
				
				arg1.setBackgroundColor(Color.RED);
				menumodel.setSelectedOffice((String)parent.getItemAtPosition(position));
				String selected = (String)parent.getItemAtPosition(position);
				menumodel.setSelectedOffice(selected);
				Log.e("selected", (String)parent.getItemAtPosition(position));
			}
		});*/
		
		
		Button goMapButton = (Button)findViewById(R.id.go_map_button);
		goMapButton.setOnClickListener(this);
		
		Button goCompasButton = (Button)findViewById(R.id.go_compas_button);
		goCompasButton.setOnClickListener(this);
		
		Button goAugRealityButton = (Button) findViewById(R.id.go_augmented_reality);
		goAugRealityButton.setOnClickListener(this);
		
		RadioGroup officesRadioGroup = (RadioGroup) findViewById(R.id.radio_mobica_office);
		officesRadioGroup.setOnCheckedChangeListener(this);
		
		
	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}*/
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.go_map_button:
			launchActivity(MapNavigatorActivity.class, officeDirection);
			break;
		case R.id.go_compas_button:
			launchActivity(CompassActivity.class, officeDirection);
			break;
		case R.id.go_augmented_reality:
			launchActivity(AugRealityActivity.class, officeDirection);
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		RadioButton selectedRadioButton = (RadioButton) group.findViewById(group.getCheckedRadioButtonId());

			for(Entry<String, float[]> officeEntry : officesMap.entrySet()){
				if(officeEntry.getKey().equals(selectedRadioButton.getText())){
					officeDirection = officeEntry.getValue();
					break;
				}
			}
		
	}

}
