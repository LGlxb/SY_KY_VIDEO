package com.sycode.sy_ky_video.app.weight.loadCallBack

import com.kingja.loadsir.callback.Callback
import com.sycode.sy_ky_video.R



/**
 * Description:TODO
 * Create Time:2017/9/4 10:20
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */

class ErrorCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_error
    }
}
