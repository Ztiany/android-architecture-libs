package com.android.sdk.net.kit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface Selector<T> {

    /**
     * returning true means accept remote data
     */
    boolean test(@NonNull T local, @Nullable T remote);

}
