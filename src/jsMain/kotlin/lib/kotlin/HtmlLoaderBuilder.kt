package lib.kotlin

import lib.module.UI
import lib.module.log
import lib.packages.android
import lib.packages.ex.android.WebChromeClientListener
import lib.packages.ex.android.WebViewClientListener
import lib.packages.java
import kotlin.js.json

class HtmlLoaderBuilder(private val index: String) {

    data class PageStartedResult(val view: android.webkit.WebView, val url: String?, val favicon: dynamic)
    data class PageFinishedResult(val view: android.webkit.WebView, val url: dynamic)
    data class PageErrorResult(
        val view: android.webkit.WebView,
        val request: android.webkit.WebResourceRequest,
        val error: android.webkit.WebResourceError
    )

    data class ConsoleMessageResult(
        val message: dynamic,
        val sourceId: String,
        val lineNumber: Int,
        val messageLevel: String
    )

    /**
     * 回调结果
     * Web 调用 Autojs 端的方法产生的结果集
     * @param cmd 调用 Autojs 端中的方法的名称/ 需要在Autojs 中执行的 Js 命令
     * @param args 调用 Autojs 端中的方法的参数
     * @param result 调用 Autojs 端中的方法的结果
     * @param returnResult 将结果返回给浏览器后，负责处理该回调结果的方法，返回的数据。该数据不少必须有的
     */
    data class CallbackResult(
        val cmd: String,
        val callId: String,
        val args: dynamic,
        val result: String?,
        val returnResult: String?
    )

    private val pageStartedListenerList: MutableList<(data: PageStartedResult) -> Unit> = mutableListOf()
    private val pageFinishedListenerList: MutableList<(data: PageFinishedResult) -> Unit> = mutableListOf()
    private val pageErrorListenerList: MutableList<(data: PageErrorResult) -> Unit> = mutableListOf()
    private val consoleMessageListenerList: MutableList<(data: ConsoleMessageResult) -> Unit> = mutableListOf()
    private val callbackListenerList: MutableList<(data: CallbackResult) -> Unit> = mutableListOf()
    private val callbackResultListenerList: MutableList<(data: CallbackResult) -> Unit> = mutableListOf()


    fun onPageStarted(callback: (data: PageStartedResult) -> Unit): HtmlLoaderBuilder {
        pageStartedListenerList.add(callback)
        return this
    }

    fun onPageFinished(callback: (data: PageFinishedResult) -> Unit): HtmlLoaderBuilder {
        pageFinishedListenerList.add(callback)
        return this
    }

    fun onPageError(callback: (data: PageErrorResult) -> Unit): HtmlLoaderBuilder {
        pageErrorListenerList.add(callback)
        return this
    }

    fun onConsoleMessage(callback: (data: ConsoleMessageResult) -> Unit): HtmlLoaderBuilder {
        consoleMessageListenerList.add(callback)
        return this
    }

    fun onCallback(callback: (data: CallbackResult) -> Unit): HtmlLoaderBuilder {
        callbackListenerList.add(callback)
        return this
    }

    fun onCallbackResult(callback: (data: CallbackResult) -> Unit): HtmlLoaderBuilder {
        callbackResultListenerList.add(callback)
        return this
    }

    private fun buildWebViewClientListener(): WebViewClientListener {
        return object : WebViewClientListener {
            override fun onPageStarted(view: android.webkit.WebView, url: String?, favicon: dynamic) {
                pageStartedListenerList.forEach {
                    runCatching { it(PageStartedResult(view, url, favicon)) }.onFailure {
                        console.error("onPageStarted error: %s", it.message)
                    }
                }
            }

            override fun onPageFinished(view: android.webkit.WebView, url: dynamic) {
                pageFinishedListenerList.forEach {
                    runCatching { it(PageFinishedResult(view, url)) }.onFailure {
                        console.error("onPageFinished error: %s", it.message)
                    }
                }
            }

            override fun onReceivedError(
                view: android.webkit.WebView,
                request: android.webkit.WebResourceRequest,
                error: android.webkit.WebResourceError
            ) {
                pageErrorListenerList.forEach {
                    runCatching { it(PageErrorResult(view, request, error)) }.onFailure {
                        console.error("onPageError error: %s", it.message)
                    }
                }
            }

        }
    }

    private fun buildWebChromeClientListener(): WebChromeClientListener {
        return object : WebChromeClientListener {
            override fun onConsoleMessage(consoleMessage: android.webkit.ConsoleMessage) {
                val msg = consoleMessage.message()
                val sourceId = consoleMessage.sourceId().split("/")
                val sourceIdStr = sourceId.last()
                val lineNumber = consoleMessage.lineNumber()
                val msgLevel = consoleMessage.messageLevel()

                if (msg.startsWith("jsbridge://")) {

                    val uris = msg.split("/")
                    val data = JSON.parse<dynamic>(java.net.URLDecoder.decode(uris[2], "UTF-8"))
                    val cmd = data.cmd
                    val callId = data.callId
                    val args = data.args

                    var result: dynamic = null

                    try {
                        result = HtmlLoader.executingLocalCode(cmd, args)
                    } catch (e: dynamic) {
                        console.error(e)
                        result = eval("{message: e.message}")
                    }
                    if (result == undefined) result = null

                    callbackListenerList.forEach {
                        runCatching { it(CallbackResult(cmd, callId, args, result, null)) }.onFailure {
                            console.error("onCallback error: %s", it.message)
                        }
                    }
                    val callbackArgs = json("callId" to callId, "args" to result).toJsonString()
                    HtmlLoader.callJs("auto.callback($callbackArgs)") {
                        callbackResultListenerList.forEach {
                            runCatching { it(CallbackResult(cmd, callId, args, result, "success")) }.onFailure {
                                console.error("onCallbackResult error: %s", it.message)
                            }
                        }
                    }
                } else {
                    consoleMessageListenerList.forEach {
                        runCatching { it(ConsoleMessageResult(msg,sourceIdStr, lineNumber, msgLevel)) }.onFailure {
                            console.error("onConsoleMessage error: %s", it)
                        }
                    }
                }
            }
        }
    }

    fun build(): HtmlLoader {
        return HtmlLoader(index, config, buildWebViewClientListener(), buildWebChromeClientListener())
    }


    /**
     * 设置WebView是否支持使用屏幕控件或手势进行缩放，默认是true，支持缩放。
     */
    fun setConfig(callback: (Config) -> Unit): HtmlLoaderBuilder {
        callback(config)
        return this
    }

    val config = Config(
        supportZoom = true
    )

    data class Config(
        /**
         * 设置WebView是否支持使用屏幕控件或手势进行缩放，默认是true，支持缩放。
         */
        var supportZoom: Boolean = true,
        /**
         * 设置WebView是否通过手势触发播放媒体，默认是true，需要手势触发。
         */
        var mediaPlaybackRequiresUserGesture: Boolean = true,
        /**
         * 设置WebView是否使用其内置的变焦机制，该机制集合屏幕缩放控件使用，默认是false，不使用内置变焦机制。
         */
        var builtInZoomControls: Boolean = false,
        /**
         * 设置WebView使用内置缩放机制时，是否展现在屏幕缩放控件上，默认true，展现在控件上。
         */
        var displayZoomControls: Boolean = true,
        /**
         * 设置在WebView内部是否允许访问文件，默认允许访问。
         */
        var allowFileAccess: Boolean = true,
        /**
         * 设置WebView是否使用其内置的变焦机制，该机制结合屏幕缩放控件使用，默认是false，不使用内置变焦机制。
         */
        var allowContentAccess: Boolean = false,
        /**
         * 设置WebView是否使用预览模式加载界面。
         */
        var loadWithOverviewMode: Boolean = true,
        /**
         * 设置WebView是否保存表单数据，默认true，保存数据。
         */
        var saveFormData: Boolean = true,
        /**
         * 设置WebView中加载页面字体变焦百分比，默认100，整型数。
         */
        var textZoom: Int = 100,
        /**
         * 设置WebView访问第三方Cookies策略，参考CookieManager提供的方法：setShouldAcceptThirdPartyCookies。
         */
        @Deprecated("未实现该配置")
        var acceptThirdPartyCookies: Boolean = true,
        /**
         * 设置WebView是否使用viewport，当该属性被设置为false时，加载页面的宽度总是适应WebView控件宽度；当被设置为true，当前页面包含viewport属性标签，在标签中指定宽度值生效，如果页面不包含viewport标签，无法提供一个宽度值，这个时候该方法将被使用。
         */
        var useWideViewPort: Boolean = true,
        /**
         * 设置WebView是否支持多屏窗口，参考WebChromeClient#onCreateWindow，默认false，不支持。
         */
        var supportMultipleWindows: Boolean = false,
        /**
         * 设置WebView标准字体库字体，默认字体“sans-serif”。
         */
        var standardFontFamily: String = "sans-serif",
        /**
         * 设置WebView固定的字体库字体，默认“monospace”。
         */
        var fixedFontFamily: String = "monospace",
        /**
         * 设置WebView Sans SeriFontFamily字体库字体，默认“sans-serif”。
         */
        var sansSerifFontFamily: String = "sans-serif",
        /**
         * 设置WebView seri FontFamily字体库字体，默认“sans-serif”。
         */
        var serifFontFamily: String = "sans-serif",
        /**
         * 设置WebView字体库字体，默认“cursive”
         */
        var cursiveFontFamily: String = "cursive",
        /**
         * 设置WebView字体库字体，默认“fantasy”。
         */
        var fantasyFontFamily: String = "fantasy",
        /**
         * 设置WebView字体最小值，默认值8，取值1到72
         */
        var minimumFontSize: Int = 8,
        /**
         * 设置WebView逻辑上最小字体值，默认值8，取值1到72
         */
        var minimumLogicalFontSize: Int = 8,
        /**
         * 设置WebView默认值字体值，默认值16，取值1到72
         */
        var defaultFontSize: Int = 16,
        /**
         * 设置WebView默认固定的字体值，默认值16，取值1到72
         */
        var defaultFixedFontSize: Int = 16,
        /**
         * 设置WebView是否加载图片资源，默认true，自动加载图片
         */
        var loadImagesAutomatically: Boolean = true,
        /**
         * 设置WebView是否以http、https方式访问从网络加载图片资源，默认false
         */
        var blockNetworkImage: Boolean = true,
        /**
         * 设置WebView是否从网络加载资源，Application需要设置访问网络权限，否则报异常
         */
        var blockNetworkLoads: Boolean = true,
        /**
         * 设置WebView是否允许执行JavaScript脚本，默认false，不允许
         */
        var javaScriptEnabled: Boolean = true,
        /**
         * 设置WebView运行中的脚本可以是否访问任何原始起点内容，默认true
         */
        var allowUniversalAccessFromFileURLs: Boolean = true,
        /**
         * 设置WebView运行中的一个文件方案被允许访问其他文件方案中的内容，默认值true
         */
        var allowFileAccessFromFileURLs: Boolean = true,
        /**
         * 设置WebView保存地理位置信息数据路径，指定的路径Application具备写入权限
         */
        var databasePath: String = "./cache/geolocation",
        /**
         * 设置Application缓存API是否开启，默认false，设置有效的缓存路径参考
         */
        var databaseEnabled: Boolean = false,
        /**
         * 设置当前Application缓存文件路径，Application Cache API能够开启需要指定Application具备写入权限的路径
         */
        var appCachePath: String = "./cache/cache",
        /**
         * 设置是否开启数据库存储API权限，默认false，未开启，可以参考setDatabasePath(String path)
         */
        var appCacheEnabled: Boolean = false,
        /**
         * 设置是否开启DOM存储API权限，默认false，未开启，设置为true，WebView能够使用DOM storage API
         */
        var domStorageEnabled: Boolean = false,

        /**
         * 设置是否开启定位功能，默认true，开启定位
         */
        var geolocationEnabled: Boolean = true,

        /**
         * 设置脚本是否允许自动打开弹窗，默认false，不允许
         */
        var javaScriptCanOpenWindowsAutomatically: Boolean = false,

        /**
         * 设置WebView加载页面文本内容的编码，默认“UTF-8”。
         */
        var defaultTextEncodingName: String = "UTF-8",

        /**
         * 设置WebView代理字符串，如果String为null或为空，将使用系统默认值
         */
        var userAgentString: String? = null,

        /**
         * 设置WebView是否需要设置一个节点获取焦点当被回调的时候，默认true
         */
        var needInitialFocus: Boolean = true,

        /**
         * 重写缓存被使用到的方法，该方法基于Navigation Type，
         * 加载普通的页面，将会检查缓存同时重新验证是否需要加载，
         * 如果不需要重新加载，将直接从缓存读取数据，
         * 允许客户端通过指定LOAD_DEFAULT、LOAD_CACHE_ELSE_NETWORK、LOAD_NO_CACHE、LOAD_CACHE_ONLY其中之一重写该行为方法，默认值LOAD_DEFAULT
         *
         * LOAD_DEFAULT - 0: 默认加载行为，基于 HTTP 头部缓存控制决定是否从网络获取数据。
         * LOAD_CACHE_ELSE_NETWORK - 1: 只要缓存可用就从缓存加载，否则从网络加载。
         * LOAD_NO_CACHE - 2: 不使用缓存，始终从网络加载数据。
         * LOAD_CACHE_ONLY - 3: 只从缓存加载，不从网络加载。
         */
        var cacheMode: Int = 0,

        /**
         * 设置当一个安全站点企图加载来自一个不安全站点资源时WebView的行为，
         * android.os.Build.VERSION_CODES.KITKAT默认为MIXED_CONTENT_ALWAYS_ALLOW，
         * android.os.Build.VERSION_CODES#LOLLIPOP默认为MIXED_CONTENT_NEVER_ALLOW，
         * 取值其中之一：MIXED_CONTENT_NEVER_ALLOW、MIXED_CONTENT_ALWAYS_ALLOW、MIXED_CONTENT_COMPATIBILITY_MODE.
         *
         * MIXED_CONTENT_NEVER_ALLOW - 0: 不允许加载不安全的混合内容，例如，HTTPS 页面不允许加载 HTTP 资源。
         * MIXED_CONTENT_ALWAYS_ALLOW - 1: 允许加载不安全的混合内容。
         * MIXED_CONTENT_COMPATIBILITY_MODE - 2: 在兼容模式下加载混合内容。
         */
        var mixedContentMode: Int = 1,
    )
}