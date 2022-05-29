package com.android.base.foundation.data


/**
 * @author Ztiany
 */
sealed class Resource<out T> {

    companion object {

        fun <T> noData(): Resource<T> {
            return NoData()
        }

        fun <T> success(data: T): Resource<T> {
            return Data(data)
        }

        fun <T> error(error: Throwable): Resource<T> {
            return Error(error)
        }

        fun <T> loading(): Resource<T> {
            return Loading
        }

    }

}

object Loading : Resource<Nothing>()

class Error(val error: Throwable) : Resource<Nothing>() {

    private var handled = false
    val isHandled: Boolean
        get() = handled

    fun markAsHandled() {
        handled = true
    }

    override fun toString(): String {
        return "Error(error=$error)"
    }

}

sealed class Success<T> : Resource<T>() {

    private var handled = false
    val isHandled: Boolean
        get() = handled

    fun markAsHandled() {
        handled = true
    }

}

class Data<T>(val value: T) : Success<T>() {

    override fun toString(): String {
        return "Data(value=$value)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Data<*>
        if (value != other.value) return false
        return true
    }

    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }

}

class NoData : Success<Nothing>()