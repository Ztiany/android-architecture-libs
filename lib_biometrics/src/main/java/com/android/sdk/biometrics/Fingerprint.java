package com.android.sdk.biometrics;

import android.os.Parcel;
import android.os.Parcelable;

public final class Fingerprint implements Parcelable {

    private int mFingerId;

    Fingerprint(int fingerId) {
        mFingerId = fingerId;
    }

    private Fingerprint(Parcel in) {
        mFingerId = in.readInt();
    }

    /**
     * Gets the device-specific finger id.  Used by Settings to map a name to a specific
     * fingerprint template.
     *
     * @return device-specific id for this finger
     */
    public int getFingerId() {
        return mFingerId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mFingerId);
    }

    public static final Parcelable.Creator<Fingerprint> CREATOR = new Parcelable.Creator<Fingerprint>() {
        public Fingerprint createFromParcel(Parcel in) {
            return new Fingerprint(in);
        }

        public Fingerprint[] newArray(int size) {
            return new Fingerprint[size];
        }
    };

    @Override
    public String toString() {
        return "Fingerprint{" +
                ", mFingerId=" + mFingerId +
                '}';
    }

}