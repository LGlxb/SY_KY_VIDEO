package com.sycode.sy_ky_video.di.data.entity

import java.io.Serializable

/***
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
 * Author by:淞阳coder 一个挥不动锄的小码农⛏   Date on 2020/3/19.
 * About:性感小张在线撸码
 * Label:技术好到不靠谱，帅到不像程序员.
 * Idea:任何事都可以用搜索引擎，写代码也一样.
 * Email sy_android@sina.cn
 * Role:作者信息实体类
 */
data class AuthorInfoBean(
    val tabInfo: TabInfo,
    val pgcInfo: PgcInfo
) : Serializable {

    data class TabInfo(
        val tabList: List<TabList>,
        val defaultIdx: Int
    ) : Serializable

    data class TabList(
        val id: Int,
        val name: String,
        val apiUrl: String
    ) : Serializable


    data class PgcInfo(
        val dataType: String,
        val id: Int,
        val icon: String,
        val name: String,
        val brief: String,
        val description: String,
        val actionUrl: String,
        val area: String,
        val gender: String,
        val registDate: Int,
        val followCount: Int,
        val follow: Follow,
        val self: Boolean,
        val cover: String,
        val videoCount: Int,
        val shareCount: Int,
        val collectCount: Int,
        val shield: Shield
    ) : Serializable


    data class Follow(
        val itemType: String,
        val itemId: Int,
        val followed: Boolean
    ) : Serializable

    data class Shield(
        val itemType: String,
        val itemId: Int,
        val shielded: Boolean
    ) : Serializable
}


