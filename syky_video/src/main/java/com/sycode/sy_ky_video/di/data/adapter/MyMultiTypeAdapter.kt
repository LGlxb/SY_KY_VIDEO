package com.sycode.sy_ky_video.di.data.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sycode.sy_ky_video.app.utils.getMultiType
import com.sycode.sy_ky_video.di.data.entity.ResultEntity

/**
 * Created by moment on 2017/12/11.
 */

class MyMultiTypeAdapter(datas: ArrayList<ResultEntity.ItemList>, var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var datas: ArrayList<ResultEntity.ItemList> = datas
    private var mContext: Context = context

    enum class ITEM_TYPE(val type: String) {
        ITEM_TEXTCARD("textCard"),
        ITEM_BRIEFCARD("briefCard"),
        ITEM_DYNAMIC_INFOCARD("DynamicInfoCard"),
        ITEM_HORICONTAL_SCROLLCARD("horizontalScrollCard"),
        ITEM_FOLLOWCARD("followCard"),
        ITEM_VIDEOSMALLCARD("videoSmallCard"),
        ITEM_SQUARECARD_COLLECTION("squareCardCollection"),
        ITEM_VIDEOCOLLECTION_WITHBRIEF("videoCollectionWithBrief"),
        ITEM_BANNER("banner"),
        ITEM_BANNER2("banner2"),
        ITEM_VIDEO("video"),
        ITEM_VIDEOCOLLECTION_OFHORISCROLLCARD("videoCollectionOfHorizontalScrollCard"),
        ITEM_TEXTHEADER("textHeader"),
        ITEM_TEXTFOOTER("textFooter")
    }


    fun clearAll() = this.datas.clear()

    fun addAll(data: ArrayList<ResultEntity.ItemList>?) {
        if (data == null) {
            return
        }
        this.datas.addAll(data)
    }

    //创建新View，被LayoutManager所调用
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        createMyViewHolder(viewGroup, viewType)!!


    //将数据与界面进行绑定的操作
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) =
        bindViewHolder(mContext, datas, viewHolder, position)

    override fun getItemViewType(position: Int): Int = getMultiType(position, datas)

    //获取数据的数量
    override fun getItemCount(): Int = datas.size

}
