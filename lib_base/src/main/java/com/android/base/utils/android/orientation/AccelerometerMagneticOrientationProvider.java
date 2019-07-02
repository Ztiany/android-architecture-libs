package com.android.base.utils.android.orientation;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.android.base.utils.android.orientation.filter.MeanFilterSmoothing;


class AccelerometerMagneticOrientationProvider extends AbsOrientationProvider implements SensorEventListener {


    private float[] mAccelerationValues = new float[3];
    private float[] mMagneticValues = new float[3];

    private float[] mRotationMatrix = new float[9];
    private final Sensor mAccelerometerSensor;
    private final Sensor mMagneticFieldSensor;

    private MeanFilterSmoothing mAccelerometerFilter;
    private MeanFilterSmoothing mMagneticFieldFilter;


    AccelerometerMagneticOrientationProvider(Context context) {
        super(context);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagneticFieldSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mAccelerometerFilter = new MeanFilterSmoothing();
        mMagneticFieldFilter = new MeanFilterSmoothing();

        mAccelerometerFilter.setTimeConstant(0.1F);
        mMagneticFieldFilter.setTimeConstant(0.1F);
    }


    @Override
    protected void onStartListening() {
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mMagneticFieldSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onStopListening() {
        mSensorManager.unregisterListener(this);
        mSensorManager.unregisterListener(this, mAccelerometerSensor);
        mSensorManager.unregisterListener(this, mMagneticFieldSensor);
    }

    private boolean generateRotationMatrix() {
        if (mAccelerationValues != null && mMagneticValues != null) {
            boolean rotationMatrixGenerated;
            rotationMatrixGenerated =
                    SensorManager.getRotationMatrix(mRotationMatrix, null,
                            mAccelerationValues,
                            mMagneticValues);
            return rotationMatrixGenerated;
        }
        return false;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometerSensor) {

            System.arraycopy(event.values, 0, mAccelerationValues, 0,
                    event.values.length);
            mAccelerationValues = mAccelerometerFilter
                    .addSamples(mAccelerationValues);
        } else if (event.sensor == mMagneticFieldSensor) {

            System.arraycopy(event.values, 0, mMagneticValues, 0,
                    event.values.length);
            mMagneticValues = mMagneticFieldFilter.addSamples(mMagneticValues);
        }
        if (generateRotationMatrix()) {
            determineOrientation(mRotationMatrix);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}