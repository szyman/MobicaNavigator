package com.mobicanavigator.activity;

import java.util.Map;
import java.util.Map.Entry;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.cmobicanavigator.model.DialogType;
import com.main.campusnavigator.R;
import com.mobicanavigator.activity.providers.DataProvider;
import com.mobicanavigator.activity.providers.DialogProvider;
import com.mobicanavigator.view.MenuView;

public class MenuActivity extends MainActivity implements OnClickListener,
		OnCheckedChangeListener {

	private View view;

	private Map<String, float[]> officesMap;
	private float[] officeDirection;
	private String officeName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = (MenuView) View.inflate(this, R.layout.menu_layout, null);
		setContentView(view);

		DataProvider dataProvider = new DataProvider(getApplicationContext());
		officesMap = dataProvider.getOfficesMap();

		Button goMapButton = (Button) findViewById(R.id.go_map_button);
		goMapButton.setOnClickListener(this);

		Button goCompasButton = (Button) findViewById(R.id.go_compas_button);
		goCompasButton.setOnClickListener(this);

		Button goAugRealityButton = (Button) findViewById(R.id.go_augmented_reality);
		goAugRealityButton.setOnClickListener(this);

		RadioGroup officesRadioGroup = (RadioGroup) findViewById(R.id.radio_mobica_office);
		officesRadioGroup.setOnCheckedChangeListener(this);

	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // TODO
	 * Auto-generated method stub return false; }
	 */

	@Override
	public void onClick(View v) {
		if (officeName != null){
			switch (v.getId()) {
			case R.id.go_map_button:
				launchActivity(MapNavigatorActivity.class, officeDirection, officeName);
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
		else
			DialogProvider.showDialog(DialogType.ERROR, this, R.string.dialog_office_set_error);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		RadioButton selectedRadioButton = (RadioButton) group
				.findViewById(group.getCheckedRadioButtonId());

		for (Entry<String, float[]> officeEntry : officesMap.entrySet()) {
			if (officeEntry.getKey().equals(selectedRadioButton.getText())) {
				officeDirection = officeEntry.getValue();
				officeName = officeEntry.getKey();
				break;
			}
		}

	}

}
