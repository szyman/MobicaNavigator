package com.campusnavigator.patterns;

import com.campusnavigator.activity.AugRealityActivity;
import com.campusnavigator.activity.CompassActivity;
import com.campusnavigator.activity.MapNavigatorActivity;

public class GpsListenerFactory {

	public static GpsListenerInf createListener (Object listenerType){
		if(listenerType instanceof MapNavigatorActivity)
			return new GpsMapListener(listenerType);
		else if(listenerType instanceof CompassActivity)
			return new GpsCompasListener(listenerType);
		else if(listenerType instanceof AugRealityActivity)
			return new GpsAugRealityListener(listenerType);
		throw new IllegalArgumentException("No such Listener");
	}
	
}
