package com.sycode.sy_ky_video.mvp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.jess.arms.di.component.AppComponent

import com.sycode.sy_ky_video.di.component.DaggerRecommendComponent
import com.sycode.sy_ky_video.di.module.RecommendModule
import com.sycode.sy_ky_video.mvp.contract.RecommendContract
import com.sycode.sy_ky_video.mvp.presenter.RecommendPresenter

import com.sycode.sy_ky_video.R
import com.sycode.sy_ky_video.mvp.ui.base.BaseFragment


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
 * Role:推荐Frag
 */
class RecommendFragment : BaseFragment<RecommendPresenter>(), RecommendContract.View {
    companion object {
        fun newInstance(): RecommendFragment {
            val fragment = RecommendFragment()
            return fragment
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerRecommendComponent //如找不到该类,请编译一下项目
            .builder()
            .appComponent(appComponent)
            .recommendModule(RecommendModule(this))
            .build()
            .inject(this)
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_recommend, container, false);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(TAG+"+-+","RecommendFragment创建")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initEvent() {
        super.initEvent()
    }
    override fun initData(savedInstanceState: Bundle?) {

    }

}
