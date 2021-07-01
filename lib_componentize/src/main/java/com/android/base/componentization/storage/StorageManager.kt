package com.android.base.componentization.storage

import android.content.Context
import com.android.sdk.cache.Encipher
import com.android.sdk.cache.MMKVStorageFactoryImpl
import com.android.sdk.cache.Storage
import com.android.sdk.cache.TypeFlag
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 全局存储管理器，底层使用 mmkv，可达到内存级访问速度。
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-12-19 13:51
 */
@Singleton
class StorageManager @Inject internal constructor(
        @ApplicationContext private val context: Context
) {

    companion object {
        private const val STABLE_CACHE_ID = "app-stable-cache-id"
        private const val USER_ASSOCIATED_CACHE_ID = "app-UserAssociated-cache-id"
        private const val ALL_USER_ASSOCIATED_CACHE_ID_KEY = "all_user_associated_cache_id_key"
    }

    private val storageFactory = MMKVStorageFactoryImpl()

    private val _userAssociated: Storage = storageFactory
            .newBuilder(context)
            .storageId(USER_ASSOCIATED_CACHE_ID)
            .build()

    private val _stable: Storage = storageFactory
            .newBuilder(context)
            .storageId(STABLE_CACHE_ID)
            .build()

    private val _userAssociatedIdList by lazy {
        _stable.getEntity<MutableList<String>>(
                ALL_USER_ASSOCIATED_CACHE_ID_KEY,
                object : TypeFlag<MutableList<String>>() {}.rawType)
                ?: mutableListOf()
    }

    private val storageCache = HashMap<String, WeakReference<Storage>>()

    /**  全局默认用户相关缓存，不支持跨进程，用户退出后缓存也会被清理。 */
    fun userStorage() = _userAssociated

    /** 全局默认永久缓存，不支持跨进程，用户退出后缓存不会被清理。 */
    fun stableStorage() = _stable

    /**
     * 创建一个存储器，不支持跨进程，请确保每一次调用此方法时，相同的 [storageId] 对应相同的 [userAssociated] 和 [encipher]。
     *
     * - [storageId] 缓存文件名。
     * - [userAssociated] 如果为true，则退出登录后自动清除。
     * - [encipher] 用于数据加密。
     */
    @Synchronized
    fun newStorage(storageId: String, userAssociated: Boolean = false, encipher: Encipher? = null): Storage {

        if (userAssociated) {
            if (!_userAssociatedIdList.contains(storageId)) {
                _userAssociatedIdList.add(storageId)
                stableStorage().putEntity(ALL_USER_ASSOCIATED_CACHE_ID_KEY, _userAssociatedIdList)
            }
        }

        val weakReference = storageCache[storageId]

        if (weakReference != null) {
            val storage = weakReference.get()
            if (storage != null) {
                return storage
            }
        }

        val storage = storageFactory.newBuilder(context)
                .storageId(storageId)
                .encipher(encipher)
                .build()

        storageCache[storageId] = WeakReference(storage)

        return storage
    }

    /**仅由[AppDataSource.logout]在退出登录时调用*/
    internal fun clearUserAssociated() {
        userStorage().clearAll()

        if (_userAssociatedIdList.isEmpty()) {
            return
        }
        for (cacheId in _userAssociatedIdList) {

            if (cacheId.isEmpty()) {
                continue
            }

            /*只是清理的话，不需要考虑加密器*/
            val weakReference = storageCache[cacheId]

            if (weakReference != null) {
                val storage = weakReference.get()
                if (storage != null) {
                    storage.clearAll()
                    Timber.d("clear user associated cache：$cacheId")
                    continue
                }
            }

            storageFactory.newBuilder(context).storageId(cacheId).build()
            Timber.d("clear user associated cache：$cacheId")
        }

        _userAssociatedIdList.clear()

        stableStorage().remove(ALL_USER_ASSOCIATED_CACHE_ID_KEY)
    }

}