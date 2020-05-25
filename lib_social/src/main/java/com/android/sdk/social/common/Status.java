package com.android.sdk.social.common;


import androidx.annotation.NonNull;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-07 17:42
 */
@SuppressWarnings("WeakerAccess,unused")
public class Status<T> {

    public static final int STATE_SUCCESS = 0;
    public static final int STATE_FAILED = 1;
    public static final int STATE_CANCEL = 2;
    public static final int STATE_REQUESTING = 3;

    private final T result;
    private final Throwable t;
    private final int status;

    private Status(T result, Throwable t, int status) {
        this.result = result;
        this.t = t;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public boolean isRequesting() {
        return status == STATE_REQUESTING;
    }

    public boolean isError() {
        return status == STATE_FAILED;
    }

    public boolean isSuccess() {
        return status == STATE_SUCCESS;
    }

    public boolean isCancel() {
        return status == STATE_CANCEL;
    }

    public boolean hasData() {
        return result != null;
    }

    public T getResult() {
        return result;
    }

    public T getResultOrElse(T whenNull) {
        if (result == null) {
            return whenNull;
        }
        return result;
    }

    public Throwable getError() {
        return t;
    }

    public static <T> Status<T> success(T t) {
        return new Status<>(t, null, STATE_SUCCESS);
    }

    public static <T> Status<T> success() {
        return new Status<>(null, null, STATE_SUCCESS);
    }

    public static <T> Status<T> error(Throwable throwable) {
        return new Status<>(null, throwable, STATE_FAILED);
    }

    public static <T> Status<T> loading() {
        return new Status<>(null, null, STATE_REQUESTING);
    }

    public static <T> Status<T> cancel() {
        return new Status<>(null, null, STATE_CANCEL);
    }

    @NonNull
    @Override
    public String toString() {
        return "Status{" +
                "result=" + result +
                ", t=" + t +
                ", status=" + status +
                '}';
    }

}
