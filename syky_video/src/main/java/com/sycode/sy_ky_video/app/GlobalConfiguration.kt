package com.sycode.sy_ky_video.app

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.jess.arms.base.delegate.AppLifecycles
import com.jess.arms.di.module.GlobalConfigModule
import com.jess.arms.http.imageloader.glide.GlideImageLoaderStrategy
import com.jess.arms.http.log.RequestInterceptor
import com.jess.arms.integration.ConfigModule
import com.sycode.sy_ky_video.BuildConfig
import com.sycode.sy_ky_video.app.api.Api
import me.jessyan.progressmanager.ProgressManager
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import java.util.concurrent.TimeUnit

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
 * Role:App 的全局配置信息在此配置, 需要将此实现类声明到 AndroidManifest 中
 */
class GlobalConfiguration : ConfigModule {
    //    public static String sDomain = Api.APP_DOMAIN;

    override fun applyOptions(context: Context, builder: GlobalConfigModule.Builder) {
        if (!BuildConfig.LOG_DEBUG) { //Release 时, 让框架不再打印 Http 请求和响应的信息
            builder.printHttpLogLevel(RequestInterceptor.Level.NONE)
        }

        builder.baseurl(Api.BASE_URL)
            .imageLoaderStrategy(GlideImageLoaderStrategy())
            //这里提供一个全局处理 Http 请求和响应结果的处理类, 可以比客户端提前一步拿到服务器返回的结果, 可以做一些操作, 比如 Token 超时后, 重新获取 Token
            .globalHttpHandler(GlobalHttpHandlerImpl(context))
            //用来处理 RxJava 中发生的所有错误, RxJava 中发生的每个错误都会回调此接口
            //RxJava 必须要使用 ErrorHandleSubscriber (默认实现 Subscriber 的 onError 方法), 此监听才生效
            .responseErrorListener(ResponseErrorListenerImpl())
            .gsonConfiguration {//这里可以自己自定义配置 Gson 的参数
                    _, gsonBuilder ->
                gsonBuilder
                    .serializeNulls()//支持序列化值为 null 的参数
                    .enableComplexMapKeySerialization()//支持将序列化 key 为 Object 的 Map, 默认只能序列化 key 为 String 的 Map
            }
            .retrofitConfiguration {//这里可以自己自定义配置 Retrofit 的参数, 甚至您可以替换框架配置好的 OkHttpClient 对象 (但是不建议这样做, 这样做您将损失框架提供的很多功能)
                    _, _ ->
            }
            .okhttpConfiguration {//这里可以自己自定义配置 Okhttp 的参数
                    _, okhttpBuilder ->
                //okhttpBuilder.sslSocketFactory(); //支持 Https, 详情请百度
                okhttpBuilder.writeTimeout(5, TimeUnit.SECONDS)//全局的读取超时时间
                okhttpBuilder.connectTimeout(10, TimeUnit.SECONDS)//全局的连接超时时间
                okhttpBuilder.readTimeout(5, TimeUnit.SECONDS)//全局的读取超时时间
                //使用一行代码监听 Retrofit／Okhttp 上传下载进度监听,以及 Glide 加载进度监听, 详细使用方法请查看 https://github.com/JessYanCoding/ProgressManager
                ProgressManager.getInstance().with(okhttpBuilder)
                //让 Retrofit 同时支持多个 BaseUrl 以及动态改变 BaseUrl, 详细使用方法请查看 https://github.com/JessYanCoding/RetrofitUrlManager
                RetrofitUrlManager.getInstance().with(okhttpBuilder)
            }
            .rxCacheConfiguration {//这里可以自己自定义配置 RxCache 的参数
                    _, rxCacheBuilder ->
                rxCacheBuilder.useExpiredDataIfLoaderNotAvailable(true)
                //想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 FastJson, 请 return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());
                //否则请 return null;
                null
            }
    }

    override fun injectAppLifecycle(context: Context, lifecycles: MutableList<AppLifecycles>) {
        //AppLifecycles 中的所有方法都会在基类 Application 的对应生命周期中被调用, 所以在对应的方法中可以扩展一些自己需要的逻辑
        //可以根据不同的逻辑添加多个实现类
        lifecycles.add(AppLifecyclesImpl())
    }

    override fun injectActivityLifecycle(
        context: Context,
        lifecycles: MutableList<Application.ActivityLifecycleCallbacks>
    ) {
        //ActivityLifecycleCallbacks 中的所有方法都会在 Activity (包括三方库) 的对应生命周期中被调用, 所以在对应的方法中可以扩展一些自己需要的逻辑
        //可以根据不同的逻辑添加多个实现类
        lifecycles.add(ActivityLifecycleCallbacksImpl())
    }

    override fun injectFragmentLifecycle(
        context: Context,
        lifecycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>
    ) {
        //FragmentLifecycleCallbacks 中的所有方法都会在 Fragment (包括三方库) 的对应生命周期中被调用, 所以在对应的方法中可以扩展一些自己需要的逻辑
        //可以根据不同的逻辑添加多个实现类
        lifecycles.add(FragmentLifecycleCallbacksImpl())
    }
}
