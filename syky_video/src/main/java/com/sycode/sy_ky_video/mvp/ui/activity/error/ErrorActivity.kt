package com.sycode.sy_ky_video.mvp.ui.activity.error

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.jess.arms.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
import com.sycode.sy_ky_video.R
import com.sycode.sy_ky_video.app.utils.ShowUtils
import kotlinx.android.synthetic.main.activity_error.*
import kotlinx.android.synthetic.main.include_toolbar.*

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
class ErrorActivity : BaseActivity<IPresenter>() {
    override fun setupActivityComponent(appComponent: AppComponent) {
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.layout_error
    }

    override fun initData(savedInstanceState: Bundle?) {
        toolbar.run {
            setSupportActionBar(this)
            title = "发生错误"
        }

        val config = CustomActivityOnCrash.getConfigFromIntent(intent)
        error_restart.setOnClickListener {
            config?.run {
                CustomActivityOnCrash.restartApplication(this@ErrorActivity, this)
            }
        }
        error_sendError.setOnClickListener {
            //获取剪贴板管理器：
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 创建普通字符型ClipData
            val mClipData = ClipData.newPlainText(
                "Label",
                CustomActivityOnCrash.getStackTraceFromIntent(intent)
            )
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData)
            ShowUtils.showToast(this, "已复制错误日志到粘贴板")
            ShowUtils.showDialog(
                this,
                "扣　扣：330479145\n\n微　信：13113033031\n\n邮　箱：syandroid@sina.cn",
                "联系我"
            )
        }
    }
}