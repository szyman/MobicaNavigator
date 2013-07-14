package com.mobicanavigator.model;

public class PointerModel {
	
	private static PointerModel instance;
	private String selectedOffice;
	


	public PointerModel() {
		instance = null;
		setSelectedOffice("");
	}
	
	public static PointerModel getInstance(){ 
		if (instance == null)
			instance = new PointerModel();
		return instance;
		
	}
	
	public String[] getListOffice(){
		return new String[] {"Mobica Lodz", "Mobica Warsaw", "Mobica Szczecin"};
	}

	
	public String getSelectedOffice() {
		return selectedOffice;
	}

	public void setSelectedOffice(String selectedOffice) {
		this.selectedOffice = selectedOffice;
	}

}
