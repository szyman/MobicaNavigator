package com.campusnavigator.view;

import java.io.IOException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.location.Location;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

//http://www.anddev.org/multimedia-problems-f28/drawing-on-camera-preview-t4609.html

public class AugRealityView extends SurfaceView implements
		SurfaceHolder.Callback {
	private Camera camera;
	private MarkersView markersView;
	
	private float direction;
	private float bearing;

	public AugRealityView(Context context) {
		super(context);
	}

	public AugRealityView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public AugRealityView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		Camera.Parameters parameters = camera.getParameters();
		parameters.setPreviewSize(width, height);
		camera.setParameters(parameters);
		camera.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		try{
			camera = Camera.open();

		}
		catch(RuntimeException ex){
			Log.e("cameraException", ex.toString());
			
		}
		
		camera.setDisplayOrientation(90);
		try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera.stopPreview();
		camera.release();
		//camera = null;
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub

		super.onFinishInflate();

		getHolder().addCallback(this);
	}

	public MarkersView getMarkersView() {
		if(this.markersView == null)
			this.markersView = new MarkersView(this.getContext());
		return this.markersView;
	}
	
	public double getAngle(Location curLocation, Location destLocation){
		final float[] results= new float[3];
		Location.distanceBetween(curLocation.getLatitude(), curLocation.getLongitude(), destLocation.getLatitude(), destLocation.getLongitude(), results);
		
		int radius = 6371; // 6371km is the radius of the earth
	    double dLat = destLocation.getLatitude();
	    double dLon = destLocation.getLongitude();
	    double a = Math.pow(Math.sin(dLat/2),2) + Math.cos(curLocation.getLatitude())* Math.cos(destLocation.getLatitude()) * Math.pow(Math.sin(dLon/2),2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double d = radius * c;
	    
	    Log.e("bearing", results[0] + " "+ results[1] + " " + results[2]);
	    
	    
		return d;
	}
	
	public void updateDirection(float direction) {
		this.direction = direction;
		getMarkersView().callToInvalidate();
	}
	
	public void updateBearing(float bearing){
		this.bearing = bearing;
		getMarkersView().callToInvalidate();
	}

	private class MarkersView extends View {

		public MarkersView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		
		private void callToInvalidate(){	
			invalidate();
		}
		
		private float north;

		@Override
		protected void onDraw(Canvas canvas) {
			  int w = getMeasuredWidth();
			  int h = getMeasuredHeight();
			  int r;
			  if(w > h){
			   r = h/2;
			  }else{
			   r = w/2;
			  }
			  
			  Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			  paint.setStyle(Paint.Style.STROKE);
			  paint.setStrokeWidth(5);
			  paint.setColor(Color.WHITE);
			  
			  //canvas.drawCircle(w/2, h/2, r, paint);
			  
			  paint.setColor(Color.RED);
			  
			  float test1 = (float)(w/2 - r * Math.sin(-direction));
			  float test2 = (float)(h/2 - r * Math.cos(-direction));
			  
			  Log.e("test1", ""+test1);
			  Log.e("test2", ""+test2);
			  
			  float mobicaPoint = Math.round((float)(w/2 - r * Math.sin(-bearing)));
			  
				paint.setColor(Color.RED);
				//canvas.drawLine(w / 2, h / 2,
				//		(float) (w / 2 + r * Math.sin(-direction)), 
				//		(float) (h / 2 - r * Math.cos(-direction)), paint);
				if((float) (h / 2 - r * Math.cos(-direction)) <= canvas.getHeight() / 2)
				canvas.drawCircle((float) (w / 2 + r * Math.sin(-direction)),
						canvas.getHeight() / 2,
						20, paint);

				paint.setColor(Color.BLUE);
				//canvas.drawCircle((float) (w / 2 + r * Math.sin(-direction - bearing)),
				//		(float) (h / 2 - r * Math.cos(-direction - bearing)),
				//		20, paint);
				if((float) (h / 2 - r * Math.cos(-direction - bearing)) <= canvas.getHeight() / 2)
				canvas.drawCircle((float) (w / 2 + r * Math.sin(-direction - bearing)),
						canvas.getHeight() / 2,
						20, paint);
			  //Log.e("direction", Math.sin(-direction) + " " + Math.cos(-direction));
		}

	}
}
