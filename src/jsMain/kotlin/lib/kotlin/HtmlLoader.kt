package lib.kotlin

import lib.module.*
// import android package
import lib.packages.android
import lib.packages.ex.android.ValueCallbackListener
import lib.packages.ex.android.WebChromeClientListener
import lib.packages.ex.android.WebViewClientListener


/**
 *
 * @author : zimo
 * @date : 2024/08/02
 */
class HtmlLoader @Deprecated("use HtmlLoader.create()") constructor(
    val index: String,
    val config: HtmlLoaderBuilder.Config,
    private val webViewClientListener: WebViewClientListener,
    private val webChromeClientListener: WebChromeClientListener,
) {

    fun start() {
        UI.layout("""<webview id="web" h="*" w="*"  />""".trimIndent())
        webViewInit(UI.web)
        UI.web.loadUrl("file://${Files.path(index.trim())}")
    }

    @JsName("webViewInit")
    private fun webViewInit(webView: UILib.WebView) {
        setConfig(webView)
        webView.webViewClient = JavaAdapterByKotlin("android.webkit.WebViewClient", webViewClientListener)
        webView.webChromeClient = JavaAdapterByKotlin("android.webkit.WebChromeClient", webChromeClientListener)
    }

    private fun setConfig(webView: UILib.WebView) {
        config.apply {
             webView.getSettings().setSupportZoom(supportZoom)
             webView.getSettings().setMediaPlaybackRequiresUserGesture(mediaPlaybackRequiresUserGesture)
             webView.getSettings().setBuiltInZoomControls(builtInZoomControls)
             webView.getSettings().setDisplayZoomControls(displayZoomControls)
             webView.getSettings().setAllowFileAccess(allowFileAccess)
             webView.getSettings().setAllowContentAccess(allowContentAccess)
             webView.getSettings().setLoadWithOverviewMode(loadWithOverviewMode)
             webView.getSettings().setSaveFormData(saveFormData)
             webView.getSettings().setTextZoom(textZoom)
//             webView.getSettings().setAcceptThirdPartyCookies(acceptThirdPartyCookies)
             webView.getSettings().setUseWideViewPort(useWideViewPort)
             webView.getSettings().setSupportMultipleWindows(supportMultipleWindows)
             webView.getSettings().setStandardFontFamily(standardFontFamily)
             webView.getSettings().setFixedFontFamily(fixedFontFamily)
             webView.getSettings().setSansSerifFontFamily(sansSerifFontFamily)
             webView.getSettings().setSerifFontFamily(serifFontFamily)
             webView.getSettings().setCursiveFontFamily(cursiveFontFamily)
             webView.getSettings().setFantasyFontFamily(fantasyFontFamily)
             webView.getSettings().setMinimumFontSize(minimumFontSize)
             webView.getSettings().setMinimumLogicalFontSize(minimumLogicalFontSize)
             webView.getSettings().setDefaultFontSize(defaultFontSize)
             webView.getSettings().setDefaultFixedFontSize(defaultFixedFontSize)
             webView.getSettings().setLoadsImagesAutomatically(loadImagesAutomatically)
             webView.getSettings().setBlockNetworkImage(blockNetworkImage)
             webView.getSettings().setBlockNetworkLoads(blockNetworkLoads)
             webView.getSettings().setJavaScriptEnabled(javaScriptEnabled)
             webView.getSettings().setAllowUniversalAccessFromFileURLs(allowUniversalAccessFromFileURLs)
             webView.getSettings().setAllowFileAccessFromFileURLs(allowFileAccessFromFileURLs)
             webView.getSettings().setGeolocationEnabled(geolocationEnabled)
             webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(javaScriptCanOpenWindowsAutomatically)
             webView.getSettings().setDefaultTextEncodingName(defaultTextEncodingName)
             webView.getSettings().setNeedInitialFocus(needInitialFocus)
             webView.getSettings().setCacheMode(cacheMode)
             webView.getSettings().setMixedContentMode(mixedContentMode)

            // 设置数据库路径和缓存路径
             webView.getSettings().databasePath = databasePath
             webView.getSettings().appCachePath = appCachePath

            // 启用或禁用数据库和缓存功能
             webView.getSettings().setDatabaseEnabled(databaseEnabled)
             webView.getSettings().setAppCacheEnabled(appCacheEnabled)
             webView.getSettings().setDomStorageEnabled(domStorageEnabled)

            // 设置用户代理字符串
            if (userAgentString != null) {
                 webView.getSettings().setUserAgentString(userAgentString)
            }
        }
    }


    companion object {

        fun create(htmlPath: String = "index.html"): HtmlLoaderBuilder {
            return HtmlLoaderBuilder(htmlPath)
        }

        /**
         * 执行本地代码
         */
        @JsName("bridgeHandler_handle")
        fun executingLocalCode(cmd: String, args: dynamic): dynamic {
            var ret: dynamic = null

            // 执行 js代码
            if (args == "[code]") {
                ret = eval(cmd)
            } else {
                // 调用方法
                /** @type {Function} */
                val func = eval(cmd)
                if (func == null || func == undefined) {
                    throw Error("浏览器调用Autojs脚本中函数失败: Autojs 脚本中没有发现 $cmd 函数")
                }

                // 判断参数类型
                ret = if (eval("Array.isArray(args)") as Boolean) {
                    func.apply(null, args)
                } else {
                    func.call(null, args)
                }
            }

            return ret
        }

        /**
         * 在浏览器执行JS
         */
        @JsName("callJs")
        fun callJs(script: String, callback: ((data: String) -> Unit)? = undefined) {
            callJavaScript(UI.web, script, callback)
        }

        /**
         * 在浏览器执行JS
         */
        @JsName("callJavaScript")
        fun callJavaScript(webViewWidget: UILib.WebView, script: String, callback: ((data: dynamic) -> Unit)? = null) {
            try {
                webViewWidget.evaluateJavascript(
                    "javascript:$script",
                    JavaAdapterByKotlin("android.webkit.ValueCallback", object : ValueCallbackListener {
                        override fun onReceiveValue(value: dynamic) {
                            if (callback != null) {
                                callback(value)
                            }
                        }
                    })
                )
            } catch (e: dynamic) {
                console.error("执行JavaScript失败", e)
            }
        }
    }
}