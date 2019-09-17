package com.android.sdk.net.kit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface Selector<T> {

    /**
     * returning true means accept remote data
     */
    boolean test(@NonNull T local, @Nullable T remote);

}
