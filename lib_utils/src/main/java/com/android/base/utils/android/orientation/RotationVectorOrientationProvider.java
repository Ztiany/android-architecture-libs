package com.android.base.utils.android.orientation;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

class RotationVectorOrientationProvider extends AbsOrientationProvider implements SensorEventListener {

    private final Sensor mSensor;
    private int mLastAccuracy;
    private float[] Q = new float[9];

    RotationVectorOrientationProvider(Context context) {
        super(context);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    @Override
    protected void onStartListening() {
        mSensorManager.registerListener(RotationVectorOrientationProvider.this, mSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onStopListening() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mSensor) {
            if (mLastAccuracy != SensorManager.SENSOR_STATUS_UNRELIABLE) {
                SensorManager.getRotationMatrixFromVector(Q, event.values);
                determineOrientation(Q);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor == mSensor) {
            if (mLastAccuracy != accuracy) {
                mLastAccuracy = accuracy;
            }
        }
    }
}
