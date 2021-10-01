package com.android.base.architecture.mvp


interface IPresenter<V : IBaseView> : Lifecycle {

    /**
     * bind a view
     *
     * @param view V
     */
    fun bindView(view: V?)

}
