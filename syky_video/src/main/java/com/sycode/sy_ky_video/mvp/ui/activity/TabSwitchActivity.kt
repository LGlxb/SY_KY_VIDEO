package com.sycode.sy_ky_video.mvp.ui.activity

import android.content.Intent
import android.os.Bundle

import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils

import com.sycode.sy_ky_video.di.component.DaggerTabSwitchComponent
import com.sycode.sy_ky_video.di.module.TabSwitchModule
import com.sycode.sy_ky_video.mvp.contract.TabSwitchContract
import com.sycode.sy_ky_video.mvp.presenter.TabSwitchPresenter

import com.sycode.sy_ky_video.R
import com.sycode.sy_ky_video.mvp.ui.base.BaseActivity


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
 * Role:分类顺序排序
 */
class TabSwitchActivity : BaseActivity<TabSwitchPresenter>(), TabSwitchContract.View {

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerTabSwitchComponent //如找不到该类,请编译一下项目
            .builder()
            .appComponent(appComponent)
            .tabSwitchModule(TabSwitchModule(this))
            .build()
            .inject(this)
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_tab_switch //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    override fun initData(savedInstanceState: Bundle?) {

    }
}
