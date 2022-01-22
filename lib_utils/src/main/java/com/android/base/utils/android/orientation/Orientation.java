package com.android.base.utils.android.orientation;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.List;


/**
 * 手机旋转方向提供，参考<a href="https://github.com/KEOpenSource/AccelerationExplorer">这个项目</a>
 */
public class Orientation {

    public static OrientationProvider newInstance(Context context) {
        if (hasRotationVector(context)) {
            return new RotationVectorOrientationProvider(context);
        } else if (hasAccelerometer(context) && hasMagnetometer(context)) {
            return new AccelerometerMagneticOrientationProvider(context);
        } else {
            return new OrientationProvider() {
                @Override
                public void startListening(Listener listener) {

                }

                @Override
                public void invertAxle(boolean isInvert) {

                }

                @Override
                public void stopListening() {

                }
            };
        }
    }


    private static boolean hasMagnetometer(Context context) {
        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> listSensors = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
        return !listSensors.isEmpty();
    }

    private static boolean hasAccelerometer(Context context) {
        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> listSensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        return !listSensors.isEmpty();
    }

    private static boolean hasRotationVector(Context context) {
        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> listSensors = mSensorManager.getSensorList(Sensor.TYPE_ROTATION_VECTOR);
        return !listSensors.isEmpty();
    }
}
