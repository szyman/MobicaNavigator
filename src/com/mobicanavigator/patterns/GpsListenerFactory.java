package com.mobicanavigator.patterns;

import com.mobicanavigator.activity.AugRealityActivity;
import com.mobicanavigator.activity.CompassActivity;
import com.mobicanavigator.activity.MapNavigatorActivity;
import com.mobicanavigator.activity.listeners.GpsAugRealityListener;
import com.mobicanavigator.activity.listeners.GpsCompasListener;
import com.mobicanavigator.activity.listeners.GpsListenerInf;
import com.mobicanavigator.activity.listeners.GpsMapListener;

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
