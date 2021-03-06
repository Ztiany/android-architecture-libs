# 如果 Hilt 支持传递默认参数

>具体参考 <https://github.com/google/dagger/issues/1904>。

BaseFragment 应该是这样的：

```kotlin
open class BaseFragment @JvmOverloads constructor(
        /**provide  a  layout id*/
        @LayoutRes contentLayoutId: Int = 0,
        /**缓存 Fragment 的 View，默认为不缓存，可能在某些特点场景下才会需要用到，设置为缓存可能有未知的问题。*/
        reuseTheView: Boolean = false
) : Fragment(), LoadingView, OnBackPressListener, FragmentDelegateOwner, AutoDisposeLifecycleOwnerEx {

    private var fragmentAnimatorHelper: FragmentAnimatorHelper? = null

    private val reuseView by lazy { ReusableView(contentLayoutId, reuseTheView) }

}
```

ReusableView 应该是这样的

```kotlin
/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-03-05 12:44
 */
class ReusableView(
        @LayoutRes var contentLayoutId: Int,
        var reuseTheView: Boolean
) {

    private var layoutView: View? = null

    private var cachedView: View? = null

    private fun createFragmentLayoutCached(inflater: LayoutInflater, container: ViewGroup?): View? {
        if (cachedView == null) {
            val layout = createFragmentLayout(inflater, container)
            cachedView = layout
            return layout
        }

        cachedView?.run {
            val viewParent = parent
            if (viewParent != null && viewParent is ViewGroup) {
                viewParent.removeView(this)
            }
        }

        return cachedView
    }

    private fun createFragmentLayout(inflater: LayoutInflater, container: ViewGroup?): View? {
        return if (contentLayoutId > 0) {
            inflater.inflate(contentLayoutId, container, false)
        } else {
            null
        }
    }

    fun createView(inflater: LayoutInflater, container: ViewGroup?): View? {
        return if (reuseTheView) {
            createFragmentLayoutCached(inflater, container)
        } else {
            createFragmentLayout(inflater, container)
        }
    }

    fun isNotTheSameView(view: View): Boolean {
        if (layoutView !== view) {
            layoutView = view
            return true
        }
        return false
    }

}
```