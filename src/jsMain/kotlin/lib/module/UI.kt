package lib.module

import kotlinx.coroutines.NonDisposableHandle.parent
import lib.packages.android

/**
 *
 * @author : zimo
 * @date : 2024/07/30
 */

@JsName("ui")
external object UILib {
    // `xml` can be of type `UILike` or any other type
    fun layout(xml: dynamic) // You might want to use `Any` if you're not sure of the exact type

    // `xml` can be of type `UILike` or any other type, and `parent` can be `View` or `null`
    fun inflate(xml: dynamic, parent: Any? = definedExternally)

    // `id` is a `String`, and `findView` returns `View`
    fun findView(id: String): Any

    // `finish` is a function with no parameters and no return value
    fun finish()

    // `view` is of type `View`
    fun setContentView(view: Any)

    // `callback` is a function with no parameters and no return value
    fun run(callback: () -> Unit)

    // `callback` is a function with no parameters and no return value, `delay` is optional
    fun post(callback: () -> Unit, delay: Int? = definedExternally)

    // `color` can be `dynamic` depending on how color is represented (e.g., string, number)
    fun statusBarColor(color: dynamic)

    // `view` is of type `View`, `menu` can be `dynamic`
    fun showPopupMenu(view: Any, menu: dynamic)


    class WebView {

        @JsName("webViewClient")
        var webViewClient: dynamic

        @JsName("webChromeClient")
        var webChromeClient: dynamic

        @JsName("evaluateJavascript")
        fun evaluateJavascript(code: String, b: dynamic)

        @JsName("loadUrl")
        fun loadUrl(url: String)

        @JsName("getSettings")
        fun getSettings(): dynamic

        @JsName("jsBridge")
        val jsBridge: JsBridge
    }

    @JsName("web")
    val web: WebView
}

@JsName("createUILib")
object UI {
    init {
        val script = Engines.myEngine().source.script
        val newlineIndex1 = script.indexOf("\r\n")
        val newlineIndex2 = script.indexOf('\n')
        var firstLine = if (newlineIndex1 == -1) null else script.substring(0, newlineIndex1)
        firstLine = firstLine ?: if (newlineIndex2 == -1) "" else script.substring(0, newlineIndex2)
        if (!firstLine.contains("ui") || firstLine.length > 6) {
            throw IllegalStateException("该脚本不是ui脚本，请启用 ui 模式。在 gradle.properties 中设置 autojs.use.ui=true")
        }
    }

    fun layout(xml: dynamic) {
        UILib.layout(xml)
    }

    fun inflate(xml: dynamic, parent: Any?) {
        UILib.inflate(xml, parent)
    }

    fun inflate(xml: dynamic) {
        UILib.inflate(xml)
    }

    fun findView(id: String): Any {
        return UILib.findView(id)
    }

    fun finish() {
        UILib.finish()
    }

    fun setContentView(view: Any) {
        UILib.setContentView(view)
    }

    fun run(callback: () -> Unit) {
        UILib.run(callback)
    }

    fun post(callback: () -> Unit, delay: Int?) {
        UILib.post(callback, delay)
    }

    fun post(callback: () -> Unit) {
        UILib.post(callback)
    }

    fun statusBarColor(color: dynamic) {
        UILib.statusBarColor(color)
    }

    fun showPopupMenu(view: Any, menu: dynamic) {
        UILib.showPopupMenu(view, menu)
    }

    @JsName("UIWeb")
    val web get() = UILib.web
}
