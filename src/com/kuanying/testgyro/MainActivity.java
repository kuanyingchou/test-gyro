package com.kuanying.testgyro;

import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor mSensor;
	private TextView infoLabel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//] generated
		infoLabel = (TextView)findViewById(R.id.infoLabel);
		infoLabel.setText("this is a gyro test");
		
//		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
		
		Sensor.type
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
		
		infoLabel.setText("this is a gyro test: after sensor code");
	}
	
	
	
	// Create a constant to convert nanoseconds to seconds.
	private static final float NS2S = 1.0f / 1000000000.0f;
	private final float[] deltaRotationVector = new float[4];
	private float timestamp;
	private float EPSILON = 10f;
	private float[] rotationCurrent = new float[] {1, 1, 1};

	
	private void handleGyroEvent(SensorEvent event) {
		if (timestamp != 0) {
			final float dT = (event.timestamp - timestamp) * NS2S;
			// Axis of the rotation sample, not normalized yet.
			float axisX = event.values[0];
			float axisY = event.values[1];
			float axisZ = event.values[2];

			infoLabel.setText(Arrays.toString(new String[] {
					String.format("%.2f", Math.toDegrees(axisX)),
					String.format("%.2f", Math.toDegrees(axisY)),
					String.format("%.2f", Math.toDegrees(axisZ))
			}));
			
			// Calculate the angular speed of the sample
			float omegaMagnitude = FloatMath.sqrt(axisX * axisX + axisY * axisY
					+ axisZ * axisZ);

			// Normalize the rotation vector if it's big enough to get the axis
			// (that is, EPSILON should represent your maximum allowable margin
			// of error)
			if (omegaMagnitude > EPSILON) {
				axisX /= omegaMagnitude;
				axisY /= omegaMagnitude;
				axisZ /= omegaMagnitude;
			}

			
			
			// Integrate around this axis with the angular speed by the timestep
			// in order to get a delta rotation from this sample over the
			// timestep
			// We will convert this axis-angle representation of the delta
			// rotation
			// into a quaternion before turning it into the rotation matrix.
			float thetaOverTwo = omegaMagnitude * dT / 2.0f;
			float sinThetaOverTwo = FloatMath.sin(thetaOverTwo);
			float cosThetaOverTwo = FloatMath.cos(thetaOverTwo);
			deltaRotationVector[0] = sinThetaOverTwo * axisX;
			deltaRotationVector[1] = sinThetaOverTwo * axisY;
			deltaRotationVector[2] = sinThetaOverTwo * axisZ;
			deltaRotationVector[3] = cosThetaOverTwo;
		}
		timestamp = event.timestamp;
		float[] deltaRotationMatrix = new float[9];
		SensorManager.getRotationMatrixFromVector(deltaRotationMatrix,
				deltaRotationVector);
		// User code should concatenate the delta rotation we computed with the
		// current rotation
		// in order to get the updated rotation.
		rotationCurrent = Util.multiply(rotationCurrent, deltaRotationMatrix);
		
		//infoLabel.setText(Arrays.toString(deltaRotationVector));
		//infoLabel.setText(Arrays.toString(deltaRotationMatrix));
//		infoLabel.setText(Arrays.toString(rotationCurrent));
	}
	private void handleGeomagnetic(SensorEvent event) {
		infoLabel.setText(Arrays.toString(new String[] {
				String.format("%.2f", Math.toDegrees(event.values[0])),
				String.format("%.2f", Math.toDegrees(event.values[1])),
				String.format("%.2f", Math.toDegrees(event.values[2]))
		}));
	}
	
	public void onSensorChanged(SensorEvent event) {
		
		// This timestep's delta rotation to be multiplied by the current
		// rotation
		// after computing it from the gyro sample data.
		Log.d("ken", "got event: "+event);
		//handleGyroEvent(event);
		handleGeomagnetic(event);
	}
	
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mSensorManager.unregisterListener(this);
	}
}

