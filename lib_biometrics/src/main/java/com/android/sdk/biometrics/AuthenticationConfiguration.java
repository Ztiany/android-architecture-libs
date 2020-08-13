package com.android.sdk.biometrics;

import androidx.annotation.Nullable;

public class AuthenticationConfiguration {

    boolean setInvalidatedByBiometricEnrollment = false;

    boolean validatingResult = true;

    @Nullable
    String keyName;

    private AuthenticationConfiguration() {

    }

    public void setInvalidatedByBiometricEnrollment(boolean setInvalidatedByBiometricEnrollment) {
        this.setInvalidatedByBiometricEnrollment = setInvalidatedByBiometricEnrollment;
    }

    public void setValidatingResult(boolean validatingResult) {
        this.validatingResult = validatingResult;
    }

    public void setKeyName(@Nullable String keyName) {
        this.keyName = keyName;
    }

    static AuthenticationConfiguration createDefault() {
        return new AuthenticationConfiguration();
    }

}
