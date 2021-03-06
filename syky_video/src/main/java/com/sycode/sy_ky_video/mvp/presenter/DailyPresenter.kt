package com.sycode.sy_ky_video.mvp.presenter

import android.app.Application
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.integration.AppManager
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.utils.RxLifecycleUtils
import com.sycode.sy_ky_video.mvp.contract.DailyContract
import com.sycode.sy_ky_video.di.data.entity.CategoryBean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import javax.inject.Inject

/**
 *
 *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 *  ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 *  ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 *  ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 *           ░     ░ ░      ░  ░
 * Author by:淞阳coder 一个挥不动锄的小码农⛏️   Date on 03/19/2020.
 * About:性感小张在线撸码
 * Label:技术好到不靠谱，帅到不像程序员.
 * Idea:任何事都可以用搜索引擎，写代码也一样.
 * Email sy_android@sina.cn
 * Role:
 */
@FragmentScope
class DailyPresenter
@Inject
constructor(model: DailyContract.Model, rootView: DailyContract.View) :
    BasePresenter<DailyContract.Model, DailyContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler

    @Inject
    lateinit var mApplication: Application

    @Inject
    lateinit var mImageLoader: ImageLoader

    @Inject
    lateinit var mAppManager: AppManager

    /**
     * 首页分类
     */
    fun onShowCategory() {
        mModel.onShowCategory()
            .subscribeOn(Schedulers.io())
            .retryWhen(RetryWithDelay(1, 0))
            .doOnSubscribe {
                mRootView.showLoading()//显示加载框
            }
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                mRootView.hideLoading()//隐藏加载框
            }
            .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
            .subscribe(object : ErrorHandleSubscriber<List<CategoryBean>>(mErrorHandler) {
                override fun onNext(t: List<CategoryBean>) {
                    mRootView.onShowCategory(resultList = t)
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    t.message?.let { mRootView.showMessage(it) }
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy();
    }
}
