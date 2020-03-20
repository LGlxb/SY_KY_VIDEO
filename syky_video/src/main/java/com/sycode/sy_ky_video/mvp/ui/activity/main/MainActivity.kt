package com.sycode.sy_ky_video.mvp.ui.activity.main

import android.os.Bundle
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
import com.sycode.sy_ky_video.R
import com.sycode.sy_ky_video.app.utils.ShowUtils
import com.sycode.sy_ky_video.mvp.ui.base.BaseActivity
import com.tencent.bugly.beta.Beta
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator

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
class MainActivity : BaseActivity<IPresenter>() {

    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_main //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    override fun initData(savedInstanceState: Bundle?) {
        if (findFragment(MainFragment::class.java) == null) {
            loadRootFragment(R.id.main_framelayout, MainFragment.newInstance())
        }
        //进入首页检查更新
        Beta.checkUpgrade(false, true)
        //如果你导入了该项目并打算以该项目为基础编写自己的项目，看到了这段代码，请记得更换 Bugly Key！
//        if (BuildConfig.APPLICATION_ID != "com.sycode.sy_ky_video" && BuildConfig.BUGLY_KEY == "e364139eb3") {
//            showMessage("请更换Bugly Key！防止产生的错误信息反馈到作者账号上，具体请查看app模块中的 build.gradle文件，修改BUGLY_KEY字段值为自己在Bugly官网申请的Key")
//        }

    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        // 设置横向(和安卓4.x动画相同)
        return DefaultHorizontalAnimator()
    }

    /**
     * 启动一个其他的Fragment
     */
    fun startBrotherFragment(targetFragment: SupportFragment) {
        start(targetFragment)
    }


    var exitTime: Long = 0

    override fun onBackPressedSupport() {
        if (System.currentTimeMillis() - this.exitTime > 2000L) {
            ShowUtils.showToast(this, "再按一次退出程序")
            this.exitTime = System.currentTimeMillis()
            return
        } else {
            super.onBackPressedSupport()
        }
    }
}
