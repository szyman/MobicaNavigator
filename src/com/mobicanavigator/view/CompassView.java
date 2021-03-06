package com.mobicanavigator.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CompassView extends View {

	private float direction;
	private float bearing;

	public CompassView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CompassView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
				MeasureSpec.getSize(heightMeasureSpec));
	}

	@Override
	protected void onDraw(Canvas canvas) {

		int w = getMeasuredWidth();
		int h = getMeasuredHeight();
		int r;
		if (w > h) {
			r = h / 2;
		} else {
			r = w / 2;
		}

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		paint.setColor(Color.WHITE);
		
		//float bearingOfficeX = (float) (w / 2 + r * Math.sin(-bearing));
		//float bearingOfficeY = (float) (h / 2 - r * Math.cos(-bearing));

		canvas.drawCircle(w / 2, h / 2, r, paint);

		paint.setColor(Color.RED);
		canvas.drawLine(w / 2, h / 2,
				(float) (w / 2 + r * Math.sin(-direction)), 
				(float) (h / 2 - r * Math.cos(-direction)), paint);

		//float officeCircle = -direction - bearing;
		//Log.e("officeCircle", "" + officeCircle);
		
		paint.setColor(Color.BLUE);
		canvas.drawCircle((float) (w / 2 + r * Math.sin(- direction - bearing)),
				(float) (h / 2 - r * Math.cos(- direction - bearing)),
				20, paint);

	}

	public void update(float dir) {
		direction = dir;
		invalidate();
	}

	public void updateBearing(float bearing) {
		this.bearing = bearing;
		invalidate();
	}
}
