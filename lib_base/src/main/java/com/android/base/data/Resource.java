package com.android.base.data;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-05-15 16:23
 */
public class Resource<T> {

    private final Throwable mError;
    private final Status mStatus;
    private final T mData; //data or default data
    private final T mDefaultData; //data or default data

    private Resource(Throwable error, T data, T defaultData, Status status) {
        mError = error;
        mStatus = status;
        mData = data;
        mDefaultData = defaultData;
    }

    public boolean isSuccess() {
        return mStatus == Status.SUCCESS;
    }

    public boolean isNoChange() {
        return mStatus == Status.NOT_CHANGED;
    }

    public boolean isLoading() {
        return mStatus == Status.LOADING;
    }

    public final boolean isError() {
        return mStatus == Status.ERROR;
    }

    public final boolean hasData() {
        return mData != null;
    }

    public static <T> Resource<T> success() {
        return new Resource<>(null, null, null, Status.SUCCESS);
    }

    public static <T> Resource<T> success(@Nullable T t) {
        return new Resource<>(null, t, null, Status.SUCCESS);
    }

    public static <T> Resource<T> error(@NonNull Throwable error) {
        return error(error, null);
    }

    public static <T> Resource<T> error(@NonNull Throwable error, T defaultValue) {
        return new Resource<>(error, null, defaultValue, Status.ERROR);
    }

    public static <T> Resource<T> loading() {
        return loading(null);
    }

    public static <T> Resource<T> loading(T defaultValue) {
        return new Resource<>(null, null, defaultValue, Status.LOADING);
    }

    /**
     * 如果数据源(比如 Repository)缓存了上一次请求的数据，然后对其当前请求返回的数据，发现数据是一样的，可以使用此状态表示
     *
     * @return Resource
     */
    public static Resource noChange() {
        return new Resource<>(null, null, null, Status.NOT_CHANGED);
    }

    @NonNull
    public T data() {
        if (isError() || isLoading() || isNoChange()) {
            throw new UnsupportedOperationException("This method can only be called when the state success");
        }
        if (mData == null) {
            throw new NullPointerException("Data is null");
        }
        return mData;
    }

    @Nullable
    public T orElse(@Nullable T elseData) {
        if (mData == null) {
            return elseData;
        }
        return mData;
    }

    @Nullable
    public T defaultData() {
        return mDefaultData;
    }

    public Throwable error() {
        return mError;
    }

    @NonNull
    @Override
    public String toString() {
        return "Resource{" +
                "mError=" + mError +
                ", mStatus=" + mStatus +
                ", mData=" + mData +
                ", mDefaultData=" + mDefaultData +
                '}';
    }

}