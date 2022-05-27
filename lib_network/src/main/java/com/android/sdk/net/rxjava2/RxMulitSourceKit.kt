package com.android.sdk.net.rxjava2

import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.NetworkErrorException
import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.Flowable
import io.reactivex.functions.Function

/** returning true means accepting the remote data */
typealias Selector<T> = (local: T, remote: T?) -> Boolean

/** invoke when new data receiving */
typealias Consumer<T> = (newData: T?) -> Unit

data class CombinedResult<T>(
    val dataType: DataType,
    val data: T,
    val error: Throwable?
)

enum class DataType {
    Remote, Disk
}

/**
 * 1. 如果网络不可用，直接返回缓存，如果没有缓存，报错没有网络连接。
 * 2. 如果存在网络：
 *      - 如果没有缓存，则从网络获取，此时网络加载发生错误将会被忽略
 *      - 如果有缓存，则先返回缓存，然后从网络获取。
 *      - 对比缓存与网络数据，如果没有更新，则忽略。
 *      - 如果有更新，则更新缓存，并返回网络数据。
 *
 * @param remote    网络数据源。
 * @param local     本地数据源。
 * @param onNewData 当有更新时，返回新的数据，可以在这里进行存储操作。
 * @param <T>       数据类型。
 * @param selector  比较器，返回当 true 表示两者相等，如果相等，则 remote 数据将会被忽略。
 * @return 组合后的Observable
</T> *
 */
fun <T> combineRemoteAndLocal(
    remote: Flowable<Optional<T>>,
    local: Flowable<Optional<T>>,
    selector: Selector<T?>,
    onNewData: Consumer<T>
): Flowable<Optional<T>> {

    //没有网络
    if (!NetContext.get().isConnected) {
        return local.flatMap {
            if (it.isPresent) {
                Flowable.just(it)
            } else {
                Flowable.error(NetworkErrorException())
            }
        }
    }

    //有网络
    val sharedLocal = local.defaultIfEmpty(Optional.empty()).replay()
    sharedLocal.connect()

    val filteredRemote = remote.onErrorResumeNext(Function {
        sharedLocal.flatMap { localData ->
            if (!localData.isPresent) {
                Flowable.error(it)
            } else {
                Flowable.empty()
            }
        }
    }).flatMap { remoteData ->
        sharedLocal.flatMap { localData ->
            if (!localData.isPresent || selector(localData.get(), remoteData.orElse(null))) {
                onNewData(remoteData.orElse(null))
                Flowable.just(remoteData)
            } else {
                Flowable.empty()
            }
        }
    }

    return Flowable.mergeDelayError(sharedLocal.filter { obj: Optional<T> -> obj.isPresent }, filteredRemote)
}

/**
 * 该方式，始终能得到网络错误通知（注意，有可能先得到网络错误，然后返回本地缓存，这取决于网络于本地缓存哪个更快）。
 *
 * 1. 如果网络不可用，直接返回缓存，如果没有缓存，报错没有网络连接。
 * 2. 如果存在网络：
 *      - 如果没有缓存，则从网络获取，此时网络加载发生错误将会被忽略
 *      - 如果有缓存，则先返回缓存，然后从网络获取。
 *      - 对比缓存与网络数据，如果没有更新，则忽略。
 *      - 如果有更新，则更新缓存，并返回网络数据。
 *
 * @param remote    网络数据源。
 * @param local     本地数据源。
 * @param onNewData 当有更新时，返回新的数据，可以在这里进行存储操作。
 * @param <T>       数据类型。
 * @param selector  比较器，返回当 true 表示两者相等，如果相等，则 remote 数据将会被忽略。
 * @return 组合后的Observable
 *
 */
fun <T> combineRemoteAndLocalReturningError(
    remote: Flowable<Optional<T>>,
    local: Flowable<Optional<T>>,
    delayNetError: Boolean = false,
    selector: Selector<T>,
    onNewData: Consumer<T>
): Flowable<CombinedResult<T?>> {

    val sharedLocal = local.defaultIfEmpty(Optional.empty()).replay()
    sharedLocal.connect()

    //组合数据
    val complexRemote = remote.flatMap { remoteData ->
        sharedLocal.flatMap { localData ->
            if (!localData.isPresent || selector(localData.get(), remoteData.orElse(null))) {
                onNewData(remoteData.orElse(null))
                Flowable.just(remoteData)
            } else {
                Flowable.empty()
            }
        }
    }.map {
        CombinedResult<T?>(DataType.Remote, it.orElse(null), null)
    }.onErrorResumeNext(Function { throwable ->

        if (delayNetError) {
            sharedLocal.flatMap { Flowable.just(CombinedResult(DataType.Remote, null, throwable)) }
        } else {
            Flowable.just(CombinedResult(DataType.Remote, null, throwable))
        }
    })

    val mappedLocal = sharedLocal
        .filter { obj: Optional<T> -> obj.isPresent }
        .map {
            CombinedResult(DataType.Disk, it.get(), null)
        }

    return Flowable.mergeDelayError(mappedLocal, complexRemote)
}

/**refer [combineRemoteAndLocal]*/
fun <T> combineRemoteAndLocal(
    remote: Flowable<Optional<T>>,
    local: Flowable<Optional<T>>,
    onNewData: (T?) -> Unit
): Flowable<Optional<T>> {
    return combineRemoteAndLocal(remote, local, { t1, t2 -> t1 != t2 }, onNewData)
}

/**refer [combineRemoteAndLocalReturningError]*/
fun <T> combineRemoteAndLocalReturningError(
    remote: Flowable<Optional<T>>,
    local: Flowable<Optional<T>>,
    delayNetError: Boolean = false,
    onNewData: (T?) -> Unit
): Flowable<CombinedResult<T?>> {
    return combineRemoteAndLocalReturningError(remote, local, delayNetError, { t1, t2 -> t1 != t2 }, onNewData)
}