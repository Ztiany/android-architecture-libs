package com.android.base.utils.debug

import com.android.base.utils.BaseUtils
import com.android.base.utils.android.cache.SpCache
import com.android.base.utils.common.ifNonNull
import com.android.base.utils.common.ifNull
import com.android.base.utils.common.otherwise


object EnvironmentContext {

    private val spCache = SpCache(BaseUtils.getAppContext().packageName, false)

    private val envMap = HashMap<String, MutableList<Environment>>()

    private fun addEnv(category: String, env: Environment) {
        envMap[category].ifNonNull {
            add(env)
            Unit
        } otherwise {
            envMap[category] = mutableListOf<Environment>().apply { add(env) }
        }
    }

    fun startEdit(adder: EnvironmentAdder.() -> Unit) {
        adder(object : EnvironmentAdder {
            override fun add(category: String, env: Environment) {
                addEnv(category, env)
            }
        })
        endAdding()
    }

    private fun endAdding() {
        envMap.forEach { (category, list) ->
            val url = spCache.getString(category, "")
            list.find {
                url == it.url
            }.ifNull {
                spCache.putString(category, list[0].url)
            }
        }
    }

    internal fun allCategory(): Map<String, List<Environment>> {
        return envMap
    }

    internal fun select(category: String, env: Environment) {
        spCache.putString(category, env.url)
    }

    fun selected(category: String): Environment {
        val url = spCache.getString(category, "")
        return envMap[category]?.find {
            url == it.url
        } ?: throw NullPointerException("no selected Environment with category: $category")
    }

}

interface EnvironmentAdder {
    fun add(category: String, env: Environment)
}

class Environment(val name: String, val url: String)