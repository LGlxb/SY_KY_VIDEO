package com.sycode.sy_ky_video.di.data.entity

/**
 * Created by moment on 2018/2/1.
 */

data class ResultEntity(
    var itemList: List<ItemList>? = null,
    var count: Int = 0,
    var total: Int = 0,
    var nextPageUrl: String? = null,
    var adExist: Boolean = false,
    var updateTime: Object? = null,
    var refreshCount: Int? = 0
) {
    override fun toString(): String {
        return "Discovery(itemList=${itemList.toString()}, count=$count, total=$total, nextPageUrl=$nextPageUrl, adExist=$adExist)"
    }

    data class ItemList(
        var type: String? = null,
        var tag: String? = null,
        var id: Int = 0,
        var adIndex: Int = 0, var data: Any? = null
    ) {


        /**
         * horizontalScrollCard、textCard、briefCard、followCard、
         * videoSmallCard、squareCardCollection、videoCollectionWithBrief、DynamicInfoCard、
         * banner
         */


        override fun toString(): String {
            return "ItemList(type=$type, tag=$tag, id=$id, adIndex=$adIndex, data=$data)"
        }


    }
}
