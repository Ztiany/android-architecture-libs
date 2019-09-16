package com.android.base.data;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-05-15 16:23
 */
public class Resource<T> {

    private final Throwable mError;
    private final Status mStatus;
    private final T mData; //data or default data

    private Resource(Throwable error, T data, Status status) {
        mError = error;
        mStatus = status;
        mData = data;
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
        return new Resource<>(null, null, Status.SUCCESS);
    }

    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource<>(null, data, Status.SUCCESS);
    }

    public static <T> Resource<T> error(@NonNull Throwable error) {
        return error(error, null);
    }

    /**
     * 创建一个 error 状态，且设置一个默认的数据
     */
    public static <T> Resource<T> error(@NonNull Throwable error, T defaultValue) {
        return new Resource<>(error, defaultValue, Status.ERROR);
    }

    public static <T> Resource<T> loading() {
        return loading(null);
    }

    /**
     * 创建一个 loading 状态，且设置一个默认的数据
     */
    public static <T> Resource<T> loading(T defaultValue) {
        return new Resource<>(null, defaultValue, Status.LOADING);
    }

    /**
     * 如果数据源(比如 Repository)缓存了上一次请求的数据，然后对其当前请求返回的数据，发现数据是一样的，可以使用此状态表示
     *
     * @return Resource
     */
    public static Resource noChange() {
        return new Resource<>(null, null, Status.NOT_CHANGED);
    }

    /**
     * 获取 Resource 中保存的数据，只有在 success 状态并且存在数据时下才能调用此方法，否则将抛出异常。
     *
     * @return Resource 中保存的数据。
     * @throws UnsupportedOperationException 非 success 状态调用此方法。
     * @throws NullPointerException          Resource 中没有保存数据时调用此方法。
     */
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

    /**
     * 获取 Resource 中保存的数据，如果不存在数据则返回 defaultData 所设置的默认数据，在不同状态下获取的数据具有不同的意义：
     *
     * <pre>
     *     <li>success 状态下，返回一个成功操作所产生的数据</li>
     *     <li>error 状态下，返回一个默认的数据，如果存在的话</li>
     *     <li>loading 状态下，返回一个默认的数据，如果存在的话</li>
     * </pre>
     *
     * @param defaultData 如果不存在数据则返回 defaultData 所设置的默认数据。
     * @return Resource 中保存的数据。
     */
    @Nullable
    public T orElse(@Nullable T defaultData) {
        if (mData == null) {
            return defaultData;
        }
        return mData;
    }

    /**
     * 获取 Resource 中保存的数据，在不同状态下获取的数据具有不同的意义：
     *
     * <pre>
     *     <li>success 状态下，返回一个成功操作所产生的数据</li>
     *     <li>error 状态下，返回一个默认的数据，如果存在的话</li>
     *     <li>loading 状态下，返回一个默认的数据，如果存在的话</li>
     * </pre>
     *
     * @return Resource 中保存的数据。
     */
    @Nullable
    public T get() {
        return mData;
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
                '}';
    }

}