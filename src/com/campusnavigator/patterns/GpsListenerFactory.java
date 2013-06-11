package com.campusnavigator.patterns;

import com.campusnavigator.activity.MapNavigatorActivity;

public class GpsListenerFactory {

	public static GpsListenerInf createListener (Object listenerType){
		if(listenerType instanceof MapNavigatorActivity)
			return new GpsMapListener(listenerType);
		throw new IllegalArgumentException("No such Listener");
	}
	
}
