package com.sycode.sy_ky_video.mvp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.jess.arms.di.component.AppComponent
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.moment.eyepetizer.event.entity.ChangeTabEvent
import com.moment.eyepetizer.event.entity.CurrentTagEvent
import com.moment.eyepetizer.event.entity.RefreshEvent
import com.sycode.sy_ky_video.R
import com.sycode.sy_ky_video.app.event.RxBus
import com.sycode.sy_ky_video.app.utils.ImageLoad
import com.sycode.sy_ky_video.app.utils.SettingUtil
import com.sycode.sy_ky_video.app.weight.loadCallBack.EmptyCallback
import com.sycode.sy_ky_video.app.weight.loadCallBack.LoadingCallback
import com.sycode.sy_ky_video.di.component.DaggerDailyComponent
import com.sycode.sy_ky_video.di.data.entity.CategoryBean
import com.sycode.sy_ky_video.di.module.DailyModule
import com.sycode.sy_ky_video.mvp.contract.DailyContract
import com.sycode.sy_ky_video.mvp.presenter.DailyPresenter
import com.sycode.sy_ky_video.mvp.ui.activity.SearchActivity
import com.sycode.sy_ky_video.mvp.ui.activity.TabSwitchActivity
import com.sycode.sy_ky_video.mvp.ui.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_daily.*
import kotlinx.android.synthetic.main.fragment_daily.view.*
import java.lang.ref.WeakReference

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
@Suppress("DEPRECATION")
class DailyFragment : BaseFragment<DailyPresenter>(), DailyContract.View {
    private var currentIndex = ""
    private var mTitles: ArrayList<CategoryListEntity> = ArrayList()
    private var mTitle: ArrayList<String> = ArrayList()
    private var mFragments = ArrayList<Fragment>()
    lateinit var loadsir: LoadService<Any>
    var str = arrayOf("发现", "推荐", "日报")
    var tabId: Long = 10001

    companion object {
        fun newInstance(): DailyFragment {
            val fragment = DailyFragment()
            return fragment
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerDailyComponent //如找不到该类,请编译一下项目
            .builder()
            .appComponent(appComponent)
            .dailyModule(DailyModule(this))
            .build()
            .inject(this)
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var mRootView = inflater.inflate(R.layout.fragment_daily, container, false)
        //绑定loadsir
        loadsir = LoadSir.getDefault().register(mRootView.viewpager) {
            //界面加载失败，或者没有数据时，点击重试的监听
            loadsir.showCallback(LoadingCallback::class.java)
            //首页分类
            mPresenter?.onShowCategory()
        }.apply {
            SettingUtil.setLoadingColor(_mActivity, this)
        }
        return mRootView
    }

    /**
     * 懒加载，只有该fragment获得视图时才会调用
     */
    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        loadsir.showCallback(LoadingCallback::class.java)//默认设置界面加载中
        mPresenter?.onShowCategory()
    }

    override fun initData(savedInstanceState: Bundle?) {
        iv_home.setColorFilter(resources.getColor(R.color.black))
        //分类顺序排序
        iv_home.setOnClickListener {
            startActivity(Intent(activity, TabSwitchActivity::class.java))
        }
        //搜索
        iv_search.setOnClickListener {
            startActivity(Intent(activity, SearchActivity::class.java))
            activity!!.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_in_top)
        }
    }

    override fun initEvent() {
        mPresenter?.onShowCategory()
        RxBus.default!!.toObservable(RefreshEvent::class.java)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { Toast.makeText(activity, "refresh", Toast.LENGTH_SHORT).show() }

        RxBus.default!!.toObservable(ChangeTabEvent::class.java)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result -> viewpager.setCurrentItem(result.tagIndex, true) })
    }

    class CategoryListEntity {
        var category_id: String? = null
        var name: String? = null
    }

    private fun initPager(mTitle: ArrayList<String>) {
        //vp2横向显示
        viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        //同FragmentStatePagerAdapter:承载viewpager
        viewpager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return mFragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return mFragments[position]
            }
        }
        //vp2+tablayout联动类
        TabLayoutMediator(tab_layout, viewpager) { tab, position ->
            val entity = mTitles[position]
            currentIndex = entity.category_id.toString()
            tab.text = mTitle[position]
        }.attach()
        //vp间距设置
        viewpager.setPageTransformer(object : ViewPager2.PageTransformer {
            override fun transformPage(page: View, position: Float) {
                if (position >= -1.0f && position <= 0.0f) {
                    //控制左侧滑入或者滑出的缩放比例
                    page.setScaleX(1 + position * 0.1f);
                    page.setScaleY(1 + position * 0.2f);
                } else if (position > 0.0f && position < 1.0f) {
                    //控制右侧滑入或者滑出的缩放比例
                    page.setScaleX(1 - position * 0.1f);
                    page.setScaleY(1 - position * 0.2f);
                } else {
                    //控制其他View缩放比例
                    page.setScaleX(0.9f);
                    page.setScaleY(0.8f);
                }
            }
        })
        //滑动监听
        viewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                Log.e(
                    TAG, "onPageScrolled+-+: $position--->$positionOffset--->$positionOffsetPixels"
                )
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e(TAG, "onPageSelected+-+: $position")
                val entity = mTitles[position]
                currentIndex = entity.category_id.toString()
                RxBus.default!!.post(CurrentTagEvent(currentIndex, false))
                ImageLoad().clearCache(WeakReference(activity!!.applicationContext))
                tab_layout.setScrollPosition(position, 0.0F, false);
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                Log.e(TAG, "onPageScrollStateChanged+-+: $state")
            }
        })
        //缓存个数
        viewpager.offscreenPageLimit = 3
        //设置显示的 item
        viewpager.currentItem = 0
    }

    override fun onShowCategory(resultList: List<CategoryBean>) {
        loadsir.showSuccess()
        var list = resultList.toMutableList()
        var tabs: MutableList<CategoryBean> = ArrayList<CategoryBean>() as MutableList<CategoryBean>
        for (tab in str) {
            val cate = CategoryBean(tabId, tab, "", "", "", "")
            tabId++
            tabs.add(cate)
        }
        list.addAll(0, tabs)
        for (aa in list) {
            Log.e("+-+", "" + aa.id)
        }
        for (cate in list) {
            when {
                cate.id == 10001L -> mFragments.add(DiscoveryFragment())
                cate.id == 10002L -> mFragments.add(RecommendFragment())
                cate.id == 10003L -> mFragments.add(FeedFragment())
                else -> mFragments.add(CategoryFragment(cate.id.toString()))
            }
            var category = CategoryListEntity()
            category.category_id = cate.id.toString()
            category.name = cate.name
            mTitles.add(category)
            mTitle.add(cate.name.toString())
        }
        //Pager
        initPager(mTitle)
    }

    override fun onShowFail(throwable: Throwable?) {
        Log.e(TAG + "+-+", throwable?.message)
        loadsir.showCallback(EmptyCallback::class.java)
    }

    override fun onDestroyView() {
        mFragments!!.clear()
        viewpager!!.adapter = null
        super.onDestroyView()
    }
}
