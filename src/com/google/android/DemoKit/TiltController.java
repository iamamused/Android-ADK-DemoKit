package com.google.android.DemoKit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class TiltController extends AccessoryController {

	private boolean mVertical;
	
	JoystickView mJoystickView;
	
	private SensorManager mSensorManager;

    TiltController(DemoKitActivity hostActivity) {
        super(hostActivity);
        mSensorManager = (SensorManager)mHostActivity.getSystemService(Context.SENSOR_SERVICE);
        mJoystickView = (JoystickView) findViewById(R.id.tiltView);
    }

	TiltController(DemoKitActivity hostActivity, boolean vertical) {
		super(hostActivity);
		mVertical = vertical;
		mSensorManager = (SensorManager)mHostActivity.getSystemService(Context.SENSOR_SERVICE);
		mJoystickView = (JoystickView) findViewById(R.id.tiltView);
	}


	protected void onAccesssoryAttached() {
	    
	    mSensorManager.registerListener(mSensorListener, 
	            mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), 
	            SensorManager.SENSOR_DELAY_FASTEST);
	    
	}

	protected void onAccesssoryDetached() {

	    mSensorManager.unregisterListener(mSensorListener);
	    
	}

	private final SensorEventListener mSensorListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // All values are angles in degrees.
            // values[0]: Azimuth, angle between the magnetic north direction and the y-axis, around the z-axis (0 to 359). 0=North, 90=East, 180=South, 270=West
            // values[1]: Pitch, rotation around x-axis (-180 to 180), with positive values when the z-axis moves toward the y-axis.
            // values[2]: Roll, rotation around y-axis (-90 to 90), with positive values when the x-axis moves toward the z-axis.
            // Note: This definition is different from yaw, pitch and roll used in aviation where the X axis is along the long side of the plane (tail to nose).

            float mPitch = event.values[1];
            float mRoll = event.values[2];
            
            mJoystickView.setPosition( (int)mRoll, (int)mPitch);
            
            byte mCommandTarget, v;
            
            // Use 0-255 to represent 0-180 on the servo output so that we emulate
            // the sliders in the existing app and not require changes to the sketch on the accessory.
            
            // also add a little buffer (20) so that the servos don't reach absolute 0 and 180
            int outMin = 20; // 0 = 0 degrees
            int outMax = 235; // 255 = 180 degrees
            
            //NewValue = (((OldValue - OldMin) * (NewMax - NewMin)) / (OldMax - OldMin)) + NewMin
            float servo1Angle = (((mPitch - -90) * (outMax - outMin)) / (90 - -90)) + outMin;
            float servo2Angle = (((mRoll - -90) * (outMax - outMin)) / (90 - -90)) + outMin;
            
            //Log.d("TiltController","Tilt:" + mPitch + "," + mRoll);
            
            // Send the commands...
            
            mCommandTarget = (byte) (0x10);
            v = (byte) (servo1Angle);
            mHostActivity.sendCommand(DemoKitActivity.LED_SERVO_COMMAND, mCommandTarget, v);

            mCommandTarget = (byte) (1 + 0x10);
            v = (byte) (servo2Angle);
            mHostActivity.sendCommand(DemoKitActivity.LED_SERVO_COMMAND, mCommandTarget, v);

        }
	};
	
}
