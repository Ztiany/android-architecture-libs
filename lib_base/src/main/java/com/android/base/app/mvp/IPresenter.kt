package com.android.base.app.mvp


interface IPresenter<V : IBaseView> : Lifecycle {

    /**
     * bind a view
     *
     * @param view V
     */
    fun bindView(view: V?)

}
