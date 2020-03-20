package com.sycode.sy_ky_video.mvp.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jess.arms.di.component.AppComponent
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.moment.eyepetizer.event.entity.CurrentTagEvent
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.sycode.sy_ky_video.R
import com.sycode.sy_ky_video.app.event.RxBus
import com.sycode.sy_ky_video.app.utils.SettingUtil
import com.sycode.sy_ky_video.app.utils.UriUtils
import com.sycode.sy_ky_video.app.weight.loadCallBack.EmptyCallback
import com.sycode.sy_ky_video.app.weight.loadCallBack.LoadingCallback
import com.sycode.sy_ky_video.di.component.DaggerCategoryComponent
import com.sycode.sy_ky_video.di.data.adapter.MyMultiTypeAdapter
import com.sycode.sy_ky_video.di.data.entity.ResultEntity
import com.sycode.sy_ky_video.di.module.CategoryModule
import com.sycode.sy_ky_video.mvp.contract.CategoryContract
import com.sycode.sy_ky_video.mvp.presenter.CategoryPresenter
import com.sycode.sy_ky_video.mvp.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_discovery.view.*


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
 * Author by:淞阳coder 一个挥不动锄的小码农⛏️  Date on 03/19/2020.
 * About:性感小张在线撸码
 * Label:技术好到不靠谱，帅到不像程序员.
 * Idea:任何事都可以用搜索引擎，写代码也一样.
 * Email sy_android@sina.cn
 * Role:
 */
class CategoryFragment(id: String) : BaseFragment<CategoryPresenter>(), CategoryContract.View {
    var adapter: MyMultiTypeAdapter? = null
    var isRefresh: Boolean = false
    var category_id = id
    var start_num: Int = 0
    var num: Int = 10
    lateinit var loadsir: LoadService<Any>

    companion object {
        fun newInstance(): CategoryFragment {
            val fragment = CategoryFragment("")
            return fragment
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerCategoryComponent //如找不到该类,请编译一下项目
            .builder()
            .appComponent(appComponent)
            .categoryModule(CategoryModule(this))
            .build()
            .inject(this)
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var mRootView = inflater.inflate(R.layout.fragment_category, container, false)
        //绑定loadsir
        loadsir = LoadSir.getDefault().register(mRootView.swipeRefreshLayout) {
            //界面加载失败，或者没有数据时，点击重试的监听
            loadsir.showCallback(LoadingCallback::class.java)
            //首页-Category
            mPresenter?.onCategorySucc(category_id.toInt(), start_num, num)
        }.apply {
            SettingUtil.setLoadingColor(_mActivity, this)
        }
        return mRootView
    }

    override fun initEvent() {
        RxBus.default!!.toObservable(CurrentTagEvent::class.java)
            .subscribe { currentTagEvent ->
                if (currentTagEvent.tag != null && currentTagEvent.tag.equals(category_id)) {
                    if (currentTagEvent.isForceRefresh) {
                        isRefresh = true
                        swipeRefreshLayout.autoRefresh(0)
                    } else {
                        if (adapter != null && adapter!!.itemCount == 0) {
                            start_num = 0
                            num = 10
                            mPresenter?.onCategorySucc(category_id.toInt(), start_num, num)
                        }
                    }
                }
            }
    }

    override fun initData(savedInstanceState: Bundle?) {
        Log.e("Fragment", "CategoryFragment initView()")
        swipeRefreshLayout.isEnableAutoLoadmore = true
        swipeRefreshLayout.refreshHeader = ClassicsHeader(activity)
        swipeRefreshLayout.refreshFooter = ClassicsFooter(activity)
        swipeRefreshLayout.setOnRefreshListener {
            isRefresh = true
            start_num = 0
            num = 10
            mPresenter?.onCategorySucc(category_id.toInt(), start_num, num)
        }
        swipeRefreshLayout.isEnableRefresh = true

        swipeRefreshLayout.setOnLoadmoreListener {
            isRefresh = false
            mPresenter?.onCategorySucc(category_id.toInt(), start_num, num)
        }

        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //0 表示停止滑动的状态 SCROLL_STATE_IDLE
                //1表示正在滚动，用户手指在屏幕上 SCROLL_STATE_TOUCH_SCROLL
                //2表示正在滑动。用户手指已经离开屏幕 SCROLL_STATE_FLING
                when (newState) {
                    2 -> {
                        Glide.with(activity!!.applicationContext).pauseRequests()
                    }
                    0 -> {
                        Glide.with(activity!!.applicationContext).resumeRequests()
                    }
                    1 -> {
                        Glide.with(activity!!.applicationContext).resumeRequests()
                    }
                }

            }
        })
        val list = ArrayList<ResultEntity.ItemList>()
        recyclerview.layoutManager = LinearLayoutManager(this.activity!!)
        adapter = MyMultiTypeAdapter(list, activity!!)
        recyclerview.adapter = adapter
        isRefresh = false
    }

    override fun onCategorySucc(t: ResultEntity) {
        loadsir.showSuccess()
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout!!.finishLoadmore()
            swipeRefreshLayout!!.finishRefresh()
            swipeRefreshLayout!!.isLoadmoreFinished = TextUtils.isEmpty(t.nextPageUrl)
        }
        if (!TextUtils.isEmpty(t.nextPageUrl)) {
            start_num = UriUtils().parseCategoryUri(t.nextPageUrl.toString()).start
            num = UriUtils().parseCategoryUri(t.nextPageUrl.toString()).num
        }
        if (t.itemList!!.isEmpty()) return
        val start = adapter!!.itemCount
        if (isRefresh) {
            adapter!!.clearAll()
            adapter!!.notifyItemRangeRemoved(0, start)
            adapter!!.addAll(t.itemList as java.util.ArrayList<ResultEntity.ItemList>?)
            adapter!!.notifyItemRangeInserted(0, t.itemList!!.size)
            start_num = 0
            num = 10
        } else {
            adapter!!.addAll(t.itemList as java.util.ArrayList<ResultEntity.ItemList>)
            adapter!!.notifyItemRangeInserted(start, t.itemList!!.size)
        }
    }

    override fun onCategoryFail(error: Throwable?) {
        Log.e(TAG + "+-+", error?.message)
        loadsir.showCallback(EmptyCallback::class.java)
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout!!.isLoadmoreFinished = false
            swipeRefreshLayout!!.finishLoadmore()
            swipeRefreshLayout!!.finishRefresh()
        }
    }

    override fun onDestroyView() {
        recyclerview!!.adapter = null
        adapter = null
        Log.e("Fragment", "CategoryFragment onDestroy() " + category_id)
        super.onDestroyView()
    }
}
