package lib.packages

/**
 *  android 包
 */
@JsName("android")
external class android {
    /**
     * android.webkit 包
     */
    @JsName("webkit")
    class webkit {
        /**
         * 对象文档： https://developer.android.google.cn/reference/android/webkit/WebViewClient
         */
        @JsName("WebViewClient")
        interface WebViewClient

        @JsName("WebChromeClient")
        interface WebChromeClient


        @JsName("ValueCallback")
        interface ValueCallback

        /**
         * 对象文档： https://developer.android.google.cn/reference/android/webkit/WebView
         */
        @JsName("WebView")
        interface WebView

        @JsName("WebResourceRequest")
        interface WebResourceRequest {
            @JsName("getUrl")
            fun getUrl(): dynamic
        }

        @JsName("WebResourceError")
        interface WebResourceError {
            @JsName("getErrorCode")
            fun getErrorCode(): Int

            @JsName("getDescription")
            fun getDescription(): String
        }

        @JsName("ConsoleMessage")
        interface ConsoleMessage {
            @JsName("message")
            fun message(): String

            @JsName("sourceId")
            fun sourceId(): String

            @JsName("lineNumber")
            fun lineNumber(): Int

            @JsName("messageLevel")
            fun messageLevel(): dynamic
        }
    }

    @JsName("graphics")
    interface graphics {
        /**
         * 对象文档: https://developer.android.google.cn/reference/android/graphics/Bitmap
         */
        @JsName("Bitmap")
        interface Bitmap
    }

    @JsName("hardware")
    class hardware{
        @JsName("SensorEvent")
        class SensorEvent{
            /**
             * accuracy {number} 表示传感器精度。
             */
            @JsName("accuracy")
            val accuracy: Int

            /**
             * 如果这是不连续后的第一个传感器事件，则设置为 true。
             */
            @JsName("firstEventAfterDiscontinuity")
            val firstEventAfterDiscontinuity: Boolean

            /**
             * 事件发生的时间（以纳秒为单位）。
             */
            @JsName("timestamp")
            val timestamp: Long
        }
    }
}