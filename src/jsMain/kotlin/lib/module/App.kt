package lib.module


// 声明 app 模块的外部函数和接口
@JsName("app")
external object App {
    // SendEmailOptions 接口
    interface SendEmailOptions {
        var email: dynamic // dynamic 可以是 String 或 String[]
        var cc: dynamic // dynamic 可以是 String 或 String[]
        var bcc: dynamic // dynamic 可以是 String 或 String[]
        var subject: String?
        var text: String?
        var attachment: String?
    }


    // IntentOptions 接口
    interface IntentOptions {
        var action: String?
        var type: String?
        var data: String?
        var category: Array<String>?
        var packageName: String?
        var className: String?
        var extras: dynamic // dynamic 可以是任意对象
    }

    /**
     * uri {string} 一个代表Uri的字符串，例如"file:///sdcard/1.txt", "https://www.autojs.org"
     * return {Uri} 一个代表Uri的对象，参见android.net.Uri。
     *
     * 解析uri字符串并返回相应的Uri对象。即使Uri格式错误，该函数也会返回一个Uri对象，
     * 但之后如果访问该对象的scheme, path等值可能因解析失败而返回null。
     * 需要注意的是，在高版本Android上，由于系统限制直接在Uri暴露文件的绝对路径，
     * 因此如果uri字符串是文件file://...，返回的Uri会是诸如content://...的形式。
     */
    fun parseUri(uri: String): dynamic

    /**
     * path {string} 文件路径，例如"/sdcard/1.txt"
     * return {Uri} 一个指向该文件的Uri的对象，参见android.net.Uri。
     * 从一个文件路径创建一个uri对象。需要注意的是，在高版本Android上，由于系统限制直接在Uri暴露文件的绝对路径，因此返回的Uri会是诸如content://...的形式。
     */
    fun getUriForFile(path: String): dynamic

    /**
     * options {Object} 选项
     * 根据选项构造一个Intent，转换为对应的shell的intent命令的参数。
     */
    fun intentToShell(appName: dynamic): Unit

    /**
     * options {Object} 选项
     */
    fun startService(options: dynamic): Boolean
    fun launchApp(appName: String): Boolean
    fun launch(packageName: String): Boolean
    fun launchPackage(packageName: String): Boolean
    fun getPackageName(appName: String): String
    fun getAppName(packageName: String): String
    fun openAppSetting(packageName: String): Boolean
    fun viewFile(path: String)
    fun editFile(path: String)
    fun uninstall(packageName: String)
    fun openUrl(url: String)

    fun sendEmail(options: SendEmailOptions)

    fun intent(options: IntentOptions): dynamic
    fun startActivity(intent: dynamic)


    fun sendBroadcast(intent: dynamic)
    /**
     * name {string} 特定的广播名称，包括：
     *  inspect_layout_hierarchy 布局层次分析
     *  inspect_layout_bounds 布局范围
     * 发送以上特定名称的广播可以触发Auto.js的布局分析，方便脚本调试。这些广播在Auto.js发送才有效，在打包的脚本上运行将没有任何效果。
     */
    fun sendBroadcast(name: String)

    // 特定界面启动
    fun startActivity(name: String)
}

fun App.versionCode(): Int {
    return js("app.autojs.versionCode()")
}

fun App.versionName(): String {
    return js("app.autojs.versionName()")
}



