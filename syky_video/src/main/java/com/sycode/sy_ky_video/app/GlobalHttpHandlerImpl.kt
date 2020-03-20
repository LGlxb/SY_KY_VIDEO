/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sycode.sy_ky_video.app

import android.content.Context
import android.os.Build
import com.jess.arms.http.GlobalHttpHandler
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * ================================================
 * 展示 [GlobalHttpHandler] 的用法
 *
 *
 * Created by JessYan on 04/09/2017 17:06
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class GlobalHttpHandlerImpl(val context: Context) : GlobalHttpHandler {

    /**
     * 这里可以先客户端一步拿到每一次 Http 请求的结果, 可以先解析成 Json, 再做一些操作, 如检测到 token 过期后
     * 重新请求 token, 并重新执行请求
     *
     * @param httpResult 服务器返回的结果 (已被框架自动转换为字符串)
     * @param chain      [okhttp3.Interceptor.Chain]
     * @param response   [Response]
     * @return [Response]
     */
    override fun onHttpResultResponse(
        httpResult: String?,
        chain: Interceptor.Chain,
        response: Response
    ): Response {
        //如果是登录的话，需要保存cookie
        if (chain.request().url().encodedPath().contains("user/login")) {
            if (response.headers("set-cookie").isNotEmpty()) {
                //保存Cookie做持久化操作 set-cookie可能为多个
                val cookies = response.headers("set-cookie")
//                CacheUtil.setCookie(encodeCookie(cookies))
            }
        }

        return response
    }

    /**
     * 这里可以在请求服务器之前拿到 [Request], 做一些操作比如给 [Request] 统一添加 token 或者 header 以及参数加密等操作
     *
     * @param chain   [okhttp3.Interceptor.Chain]
     * @param request [Request]
     * @return [Request]
     */
    override fun onHttpRequestBefore(chain: Interceptor.Chain, request: Request): Request {
        /* 如果需要在请求服务器之前做一些操作, 则重新构建一个做过操作的 Request 并 return, 如增加 Header、Params 等请求信息, 不做操作则直接返回参数 request*/


//        if (CacheUtil.isLogin()) {
////            chain.request().url().newBuilder().addEncodedPathSegment()
//            val cookies = CacheUtil.getCookie()
//            //如果已经登录过了，那么请求的时候可以带上cookie 参数
//            cookies?.run {
//                return chain.request().newBuilder()
//                        .addHeader("Cookie", this)
//                        /* .post(FormBody.Builder().apply {
//                             addEncoded("token","xxx")
//                         }.build())*/
//                        .build()
//            }
//        }
        val builder = request.url()
            .newBuilder()
            .addQueryParameter("Content-Type", "application/form-data; charset=UTF-8")
            .addQueryParameter("vc", "256")
            .addQueryParameter("vn", "3.14")
            .addQueryParameter("first_channel", "eyepetizer_yingyongbao_market")
            .addQueryParameter("last_channel", "eyepetizer_yingyongbao_market")
            .addQueryParameter("system_version_code", "25")
            .addQueryParameter("udid", "d2807c895f0348a180148c9dfa6f2feeac0781b5")
            .addEncodedQueryParameter("deviceModel", getMobileModel())
        return request.newBuilder()
            .method(request.method(), request.body())
            .url(builder.build())
            .build()
    }

    fun getMobileModel(): String {
        var model: String? = Build.MODEL
        model = model?.trim { it <= ' ' } ?: ""
        return model
    }
}
