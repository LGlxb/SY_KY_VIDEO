package com.sycode.sy_ky_video.di.module

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import com.sycode.sy_ky_video.mvp.contract.FeedContract
import com.sycode.sy_ky_video.mvp.model.FeedModel


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
@Module
//构建FeedModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class FeedModule(private val view: FeedContract.View) {
    @FragmentScope
    @Provides
    fun provideFeedView(): FeedContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideFeedModel(model: FeedModel): FeedContract.Model {
        return model
    }
}
