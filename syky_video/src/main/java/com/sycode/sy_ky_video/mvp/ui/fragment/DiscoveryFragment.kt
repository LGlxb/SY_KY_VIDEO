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
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.sycode.sy_ky_video.R
import com.sycode.sy_ky_video.app.utils.SettingUtil
import com.sycode.sy_ky_video.app.weight.loadCallBack.EmptyCallback
import com.sycode.sy_ky_video.app.weight.loadCallBack.LoadingCallback
import com.sycode.sy_ky_video.di.component.DaggerDiscoveryComponent
import com.sycode.sy_ky_video.di.data.adapter.MyMultiTypeAdapter
import com.sycode.sy_ky_video.di.data.entity.ResultEntity
import com.sycode.sy_ky_video.di.module.DiscoveryModule
import com.sycode.sy_ky_video.mvp.contract.DiscoveryContract
import com.sycode.sy_ky_video.mvp.presenter.DiscoveryPresenter
import com.sycode.sy_ky_video.mvp.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_discovery.*
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
 * Role:发现Frag
 */
class DiscoveryFragment : BaseFragment<DiscoveryPresenter>(), DiscoveryContract.View {
    var adapter: MyMultiTypeAdapter? = null
    var isRefresh: Boolean = false
    lateinit var loadsir: LoadService<Any>

    companion object {
        fun newInstance(): DiscoveryFragment {
            val fragment = DiscoveryFragment()
            return fragment
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerDiscoveryComponent //如找不到该类,请编译一下项目
            .builder()
            .appComponent(appComponent)
            .discoveryModule(DiscoveryModule(this))
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(TAG+"+-+","DiscoveryFragment创建")
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var mRootView = inflater.inflate(R.layout.fragment_discovery, container, false)
        //绑定loadsir
        loadsir = LoadSir.getDefault().register(mRootView.swipeRefreshLayout) {
            //界面加载失败，或者没有数据时，点击重试的监听
            loadsir.showCallback(LoadingCallback::class.java)
            //首页-发现
            mPresenter?.onDiscoverySucc()
        }.apply {
            SettingUtil.setLoadingColor(_mActivity, this)
        }
        return mRootView
    }

    override fun initEvent() {
        Log.e("Fragment", "DiscoveryFragment initView()")
        swipeRefreshLayout.isEnableAutoLoadmore = true
        swipeRefreshLayout.refreshHeader = ClassicsHeader(activity)
        swipeRefreshLayout.refreshFooter = ClassicsFooter(activity)
        swipeRefreshLayout.setOnRefreshListener {
            isRefresh = true
            mPresenter?.onDiscoverySucc()
        }

    }

    override fun initData(savedInstanceState: Bundle?) {
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
        val layoutManager = LinearLayoutManager(this.activity!!)
        recyclerview.layoutManager = layoutManager
        adapter = MyMultiTypeAdapter(list, activity!!)
        recyclerview.adapter = adapter
    }


    override fun onDiscoverySucc(result: ResultEntity) {
        loadsir.showSuccess()
        val start = adapter!!.itemCount
        adapter!!.clearAll()
        adapter!!.notifyItemRangeRemoved(0, start)
        adapter!!.addAll(result.itemList as ArrayList<ResultEntity.ItemList>?)
        adapter!!.notifyItemRangeInserted(0, result.itemList!!.size)
        swipeRefreshLayout.finishRefresh()
        swipeRefreshLayout.isLoadmoreFinished = TextUtils.isEmpty(result.nextPageUrl)
    }

    override fun onDiscoveryFail(error: Throwable?) {
        Log.e(TAG + "+-+", error?.message)
        loadsir.showCallback(EmptyCallback::class.java)
        swipeRefreshLayout.isLoadmoreFinished = false
        swipeRefreshLayout.finishLoadmore()
        swipeRefreshLayout.finishRefresh()
    }

    override fun onDestroyView() {
        recyclerview!!.adapter = null
        adapter = null
        Log.e("Fragment+-+", "DiscoveryFragment onDestroy()")
        super.onDestroyView()
    }
}
