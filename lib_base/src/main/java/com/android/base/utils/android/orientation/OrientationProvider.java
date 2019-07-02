package com.android.base.utils.android.orientation;

public interface OrientationProvider {

    void startListening(Listener listener);

    void invertAxle(boolean isInvert);

    void stopListening();

    interface Listener {
        void onOrientationChanged(float azimuth, float pitch, float roll);
    }


}