package com.android.base.utils.android.orientation;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.Surface;
import android.view.WindowManager;

abstract class AbsOrientationProvider implements OrientationProvider {


    private Listener mListener;
    private int[] worldAxisForDeviceAxisXy;
    private final WindowManager mWindowManager;
    SensorManager mSensorManager;
    private boolean mIsInvertAxle;
    private float[] mAdjustedRotationMatrix = new float[9];
    private float[] mOrientation = new float[3];


    AbsOrientationProvider(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        worldAxisForDeviceAxisXy = new int[2];
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }


    @Override
    public final void startListening(Listener listener) {
        if (listener == null) {
            return;
        }
        if (mListener == listener) {
            return;
        }
        if (mListener != null) {
            mListener = null;
            stopListening();
        }
        mListener = listener;
        onStartListening();
    }

    protected abstract void onStartListening();


    @Override
    public final void stopListening() {
        mListener = null;
        onStopListening();
    }

    protected abstract void onStopListening();


    void determineOrientation(float[] rotationMatrix) {
        setWorldAxisForDeviceAxis();
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisForDeviceAxisXy[0],
                worldAxisForDeviceAxisXy[1], mAdjustedRotationMatrix);
        // Transform rotation matrix into azimuth/pitch/roll
        SensorManager.getOrientation(mAdjustedRotationMatrix, mOrientation);
        // Convert radians to degrees
        // Convert radians to degrees
        float pitch = (float) Math.toDegrees(mOrientation[1]);//pitch  z轴与水平面的夹角 范围在-90 到 90° ，手机的任何一面垂直于水平面即为0°，z轴指向下方是为角度为正直
        float roll = (float) Math.toDegrees(mOrientation[2]);//roll  绕y轴
        float azimuth = (float) Math.toDegrees(mOrientation[0]);//roll  绕y轴
        if (mIsInvertAxle) {
            pitch = -pitch;
            roll = -roll;
            azimuth = -azimuth;
        }
        notifyOrientation(azimuth, pitch, roll);
    }


    private void setWorldAxisForDeviceAxis() {
        switch (mWindowManager.getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
            default:
                worldAxisForDeviceAxisXy[0] = SensorManager.AXIS_X;
                worldAxisForDeviceAxisXy[1] = SensorManager.AXIS_Z;
                break;
            case Surface.ROTATION_90:
                worldAxisForDeviceAxisXy[0] = SensorManager.AXIS_Z;
                worldAxisForDeviceAxisXy[1] = SensorManager.AXIS_MINUS_X;
                break;
            case Surface.ROTATION_180:
                worldAxisForDeviceAxisXy[0] = SensorManager.AXIS_MINUS_X;
                worldAxisForDeviceAxisXy[1] = SensorManager.AXIS_MINUS_Z;
                break;
            case Surface.ROTATION_270:
                worldAxisForDeviceAxisXy[0] = SensorManager.AXIS_MINUS_Z;
                worldAxisForDeviceAxisXy[1] = SensorManager.AXIS_X;
                break;
        }
    }

    private void notifyOrientation(float azimuth, float pitch, float roll) {
        if (mListener != null) {
            mListener.onOrientationChanged(azimuth, pitch, roll);
        }
    }


    @Override
    public void invertAxle(boolean isInvertAxle) {
        mIsInvertAxle = isInvertAxle;
    }
}
