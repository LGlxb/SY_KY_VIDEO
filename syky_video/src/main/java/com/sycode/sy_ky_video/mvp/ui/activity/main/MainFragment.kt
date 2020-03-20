package com.sycode.sy_ky_video.mvp.ui.activity.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.jess.arms.di.component.AppComponent
import com.sycode.sy_ky_video.R
import com.sycode.sy_ky_video.app.utils.SettingUtil
import com.sycode.sy_ky_video.di.component.DaggerMainComponent
import com.sycode.sy_ky_video.di.module.MainModule
import com.sycode.sy_ky_video.mvp.contract.MainContract
import com.sycode.sy_ky_video.mvp.presenter.MainPresenter
import com.sycode.sy_ky_video.mvp.ui.base.BaseFragment
import com.sycode.sy_ky_video.mvp.ui.fragment.DailyFragment
import com.sycode.sy_ky_video.mvp.ui.fragment.FoundFragment
import com.sycode.sy_ky_video.mvp.ui.fragment.HotFragment
import com.sycode.sy_ky_video.mvp.ui.fragment.MineFragment
import kotlinx.android.synthetic.main.fragment_main.*
import me.yokeyword.fragmentation.SupportFragment

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
class MainFragment : BaseFragment<MainPresenter>(), MainContract.View {
    private val first = 0
    private val two = 1
    private val three = 2
    private val four = 3
    private val five = 4
    private val mFragments = arrayOfNulls<SupportFragment>(5)

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
            .builder()
            .appComponent(appComponent)
            .mainModule(MainModule(this))
            .build()
            .inject(this)
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun initData(savedInstanceState: Bundle?) {
        val dailyFragment = findChildFragment(DailyFragment::class.java)
        if (dailyFragment == null) {
            mFragments[first] = DailyFragment.newInstance()//每日精选
            mFragments[two] = FoundFragment.newInstance()//发现
            mFragments[three] = HotFragment.newInstance()//热门
            mFragments[four] = MineFragment.newInstance()//我的
            loadMultipleRootFragment(
                R.id.main_frame, first, mFragments[first]
                , mFragments[two], mFragments[three], mFragments[four]
            )
        } else {
            mFragments[first] = dailyFragment
            mFragments[two] = FoundFragment.newInstance()//发现
            mFragments[three] = HotFragment.newInstance()//热门
            mFragments[four] = MineFragment.newInstance()//我的
        }
        main_bnve.run {
            enableAnimation(true)//开启或关闭点击动画(文字放大效果和图片移动效果)
            enableShiftingMode(false)//开始或关闭导航条位移模式。选中项和其他项的宽度不一样
            enableItemShiftingMode(true)//开始或关闭子菜单位移模式。除选中项，其他项的文本将会隐藏。
//            itemIconTintList = SettingUtil.getColorStateList(_mActivity)
            itemTextColor = SettingUtil.getColorStateList(_mActivity)
            setIconSize(20F, 20F)
            setTextSize(12F)
        }
    }

}
