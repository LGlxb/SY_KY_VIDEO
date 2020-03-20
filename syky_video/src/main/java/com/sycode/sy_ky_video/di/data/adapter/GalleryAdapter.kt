package com.sycode.sy_ky_video.di.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sycode.sy_ky_video.R
import com.sycode.sy_ky_video.app.utils.DensityUtil
import com.sycode.sy_ky_video.app.utils.ImageLoad


/**
 * Created by moment on 2018/2/5.
 */
class GalleryAdapter(context: Context, private val mDatas: List<String>) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mContext: Context = context

    inner class ViewHolder(arg0: View) : RecyclerView.ViewHolder(arg0) {

        internal var mImg: ImageView? = null
    }

    override fun getItemCount(): Int = mDatas.size

    /**
     * 创建ViewHolder
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = mInflater.inflate(
            R.layout.activity_recycler_item,
            viewGroup, false
        )
        val viewHolder = ViewHolder(view)

        viewHolder.mImg = view
            .findViewById(R.id.id_index_gallery_item_image)
        return viewHolder
    }

    /**
     * 设置值
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        var width = getScreenWidth(mContext) - DensityUtil.dip2px(mContext, 25f)
        var height = width * 0.6
        ImageLoad().load(
            mDatas.get(i),
            viewHolder.mImg,
            width.toDouble().toInt(),
            height.toDouble().toInt(),
            5
        )
    }

    private fun getScreenWidth(context: Context): Int {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return wm.defaultDisplay.width
    }

}