package com.android.base.data

import androidx.lifecycle.MutableLiveData
import com.github.dmstocking.optional.java.util.Optional


/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-05-15 16:23
 */
class Resource<T> private constructor(
        private val error: Throwable?,
        //data or default data
        private val data: T?,
        private val status: Int
) {

    val isSuccess: Boolean
        get() = status == SUCCESS

    val isNoChange: Boolean
        get() = status == NOT_CHANGED

    val isLoading: Boolean
        get() = status == LOADING

    val isError: Boolean
        get() = status == ERROR

    fun hasData(): Boolean {
        return data != null
    }

    /**
     * 获取 State 中保存的数据，只有在 success 状态并且存在数据时下才能调用此方法，否则将抛出异常。
     *
     * @return State 中保存的数据。
     * @throws UnsupportedOperationException 非 success 状态调用此方法。
     * @throws NullPointerException          State 中没有保存数据时调用此方法。
     */
    fun data(): T {
        if (isError || isLoading || isNoChange) {
            throw UnsupportedOperationException("This method can only be called when the is state success")
        }
        if (data == null) {
            throw NullPointerException("Data is null")
        }
        return data
    }

    /**
     * 获取 State 中保存的数据，如果不存在数据则返回 defaultData 所设置的默认数据，在不同状态下获取的数据具有不同的意义：
     *
     *  * success 状态下，返回一个成功操作所产生的数据
     *  * error 状态下，返回一个默认的数据，如果存在的话
     *  * loading 状态下，返回一个默认的数据，如果存在的话
     *
     * @param defaultData 如果不存在数据则返回 defaultData 所设置的默认数据。
     * @return State 中保存的数据。
     */
    fun orElse(defaultData: T?): T? {
        return data ?: defaultData
    }

    /**
     * 获取 State 中保存的数据，在不同状态下获取的数据具有不同的意义：
     *
     *  * success 状态下，返回一个成功操作所产生的数据
     *  * error 状态下，返回一个默认的数据，如果存在的话
     *  * loading 状态下，返回一个默认的数据，如果存在的话
     *
     * @return State 中保存的数据。
     */
    fun get(): T? {
        return data
    }

    fun error(): Throwable {
        return error
                ?: throw NullPointerException("This method can only be called when the state is error")
    }

    override fun toString(): String {
        return "State{" +
                "mError=" + error +
                ", mStatus=" + status +
                ", mData=" + data +
                '}'.toString()
    }

    companion object {

        private const val LOADING = 1
        private const val ERROR = 2
        private const val SUCCESS = 3
        private const val NOT_CHANGED = 4

        fun <T> success(): Resource<T> {
            return Resource(null, null, SUCCESS)
        }

        fun <T> success(data: T?): Resource<T> {
            return Resource(null, data, SUCCESS)
        }

        fun <T> error(error: Throwable): Resource<T> {
            return error(error, null)
        }

        /**
         * 创建一个 error 状态，且设置一个默认的数据
         */
        fun <T> error(error: Throwable, defaultValue: T?): Resource<T> {
            return Resource(error, defaultValue, ERROR)
        }

        fun <T> loading(): Resource<T> {
            return loading(null)
        }

        /**
         * 创建一个 loading 状态，且设置一个默认的数据
         */
        fun <T> loading(defaultValue: T?): Resource<T> {
            return Resource(null, defaultValue, LOADING)
        }

        /**
         * 如果数据源(比如 Repository)缓存了上一次请求的数据，然后对其当前请求返回的数据，发现数据是一样的，可以使用此状态表示
         *
         * @return State
         */
        fun <T> noChange(): Resource<T> {
            return Resource(null, null, NOT_CHANGED)
        }
    }

}

/**when in loading*/
inline fun <T> Resource<T>.onLoading(onLoading: () -> Unit): Resource<T> {
    if (this.isLoading) {
        onLoading()
    }
    return this
}

/**when error occurred*/
inline fun <T> Resource<T>.onError(onError: (error: Throwable) -> Unit): Resource<T> {
    if (this.isError) {
        onError(error())
    }
    return this
}

/**when no change*/
inline fun <T> Resource<T>.onNoChange(onNoChange: () -> Unit): Resource<T> {
    if (this.isNoChange) {
        onNoChange()
    }
    return this
}

/**when succeeded*/
inline fun <T> Resource<T>.onSuccess(onSuccess: (data: T?) -> Unit): Resource<T> {
    if (this.isSuccess) {
        onSuccess(this.orElse(null))
    }
    return this
}

/**when succeeded and has data*/
inline fun <T> Resource<T>.onSuccessWithData(onSuccess: (data: T) -> Unit): Resource<T> {
    val t = this.get()
    if (this.isSuccess && t != null) {
        onSuccess(t)
    }
    return this
}

fun <T : Any?> MutableLiveData<Resource<T>>.postLoading() {
    postValue(Resource.loading())
}

fun <T : Any?> MutableLiveData<Resource<T>>.postError(error: Throwable) {
    postValue(Resource.error(error))
}

fun <T : Any?> MutableLiveData<Resource<T>>.postSuccess(t: T? = null) {
    if (t == null) {
        postValue(Resource.success())
    } else {
        postValue(Resource.success(t))
    }
}

fun <T : Any?> MutableLiveData<Resource<T>>.postOptional(optional: Optional<T>) {
    optional.ifPresentOrElse(
            {
                postValue(Resource.success(it))
            },
            {
                postValue(Resource.success())
            })
}