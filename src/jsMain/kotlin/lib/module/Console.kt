package lib.module

import lib.kotlin.isUIThread

@JsName("ConsoleLib_0")
object Console {
    init {
        if (isUIThread()) {
            console.warn("Console should not be called in the main (UI) thread")
        }
    }

    var title: String? = null
        set(value) {
            field = value
            value?.let { ConsoleLib.setTitle(it) }
        }

    /**
     * 显示控制台。这会显示一个控制台的悬浮窗(需要悬浮窗权限)。
     * 结束是否自动隐藏
     */
    fun show(autoHide: Boolean = false) {
        if (isUIThread()) {
            throw IllegalStateException("Prohibit the console from running in the main (UI) thread")
        }
        return ConsoleLib.show(autoHide)
    }

    /**
     * 隐藏控制台悬浮窗。
     */
    fun hide() {
        return ConsoleLib.hide()
    }

    /**
     * 清空控制台。
     */
    fun clear() {
        return ConsoleLib.clear()
    }


    /**
     * 打印到控制台，并带上换行符。 可以传入多个参数，第一个参数作为主要信息，其他参数作为类似于 printf(3) 中的代替值（参数都会传给 util.format()）。
     * // vararg args 对应于 TypeScript 中的 ...args
     */
    fun log(data: String, vararg args: dynamic) {
        return ConsoleLib.log(data, *args)
    }

    /**
     * 与console.log类似，但输出结果以灰色字体显示。输出优先级低于log，用于输出观察性质的信息。
     */
    fun verbose(data: String, vararg args: dynamic) {
        return ConsoleLib.verbose(data, *args)
    }

    /**
     * 与console.log类似，但输出结果以绿色字体显示。输出优先级高于log, 用于输出重要信息。
     */
    fun info(data: String, vararg args: dynamic) {
        return ConsoleLib.info(data, *args)
    }

    /**
     * 与console.log类似，但输出结果以蓝色字体显示。输出优先级高于info, 用于输出警告信息。
     */
    fun warn(data: String, vararg args: dynamic) {
        return ConsoleLib.warn(data, *args)
    }

    /**
     * 与console.log类似，但输出结果以红色字体显示。输出优先级高于warn, 用于输出错误信息。
     */
    fun error(data: String, vararg args: dynamic) {
        return ConsoleLib.error(data, *args)
    }

    /**
     * 断言。如果value为false则输出错误信息message并停止脚本运行。
     */
    fun assert(value: Boolean, message: String) {
        return ConsoleLib.assert(value, message)
    }

    /**
     * 与console.log一样输出信息，并在控制台显示输入框等待输入。按控制台的确认按钮后会将输入的字符串用eval计算后返回。
     * // 返回类型为 dynamic，表示可以是 String, Number 或 Boolean
     */
    fun input(data: String, vararg args: dynamic): dynamic {
        return ConsoleLib.input(data, *args)
    }

    /**
     * 与console.log一样输出信息，并在控制台显示输入框等待输入。按控制台的确认按钮后会将输入的字符串直接返回。
     */
    fun rawInput(data: String, vararg args: dynamic): String {
        return ConsoleLib.rawInput(data, *args)
    }

    /**
     * config {Object} 日志配置，可选的项有：
     *      file {string} 日志文件路径，将会把日志写入该文件中
     *      maxFileSize {number} 最大文件大小，单位字节，默认为512 * 1024 (512KB)
     *      rootLevel {string} 写入的日志级别，默认为"ALL"（所有日志），可以为"OFF"(关闭), "DEBUG", "INFO", "WARN", "ERROR", "FATAL"等。
     *      maxBackupSize {number} 日志备份文件最大数量，默认为5
     *      filePattern {string} 日志写入格式，参见PatternLayout
     * 设置日志保存的路径和配置。例如把日志保存到"/sdcard/1.txt":
     */
    fun setGlobalLogConfig(config: ConsoleLib.Config) {
        return ConsoleLib.setGlobalLogConfig(config)
    }


    /**
     * 设置控制台的大小，单位像素。
     */
    fun setSize(width: Int, height: Int) {
        return ConsoleLib.setSize(width, height)
    }

    /**
     * label {String} 计时器标签，可省略
     * 启动一个计时器，用以计算一个操作的持续时间。 计时器由一个唯一的 label 标识。 若label重复，则会覆盖上一个同名label的计时器。 以同名 label调用 console.timeEnd() 来停止计时器，并以毫秒为单位将持续时间输出到控制台。     */
    fun time(label: String) {
        return ConsoleLib.time(label)
    }

    /**
     * label {String} 计时器标签
     * 停止之前通过调用 console.time() 启动的定时器，并打印结果到控制台。 调用 console.timeEnd() 后定时器会被删除。如果不存在标签指定的定时器则会打印 NaNms。
     */
    fun timeEnd(label: String) {
        return ConsoleLib.timeEnd(label)
    }

    /**
     * data {any}
     * ...args {any}
     * 与console.log类似，同时会打印出调用这个函数所在的调用栈信息（即当前运行的文件、行数等信息）。
     */
    fun trace(data: dynamic, vararg args: dynamic) {
        return ConsoleLib.trace(data, *args)
    }

    /**
     * text {string} | {Object} 要打印到控制台的信息
     */
    fun print(text: dynamic) {
        return ConsoleLib.print(text)
    }

    /**
     * 设置标题名称，字体颜色，标题栏高度
     * title {string} 标题
     * color {string} 标题颜色，颜色值 #AARRGGBBk
     * size {number} 标题高度，字号会随高度变化，单位是dp
     */
    fun setTitle(title: String, color: String? = null, size: Int? = null) {
        this.title = title
        return if (color != null && size != null) {
            ConsoleLib.setTitle(title, color, size)
        } else if (color != null) {
            ConsoleLib.setTitle(title, color)
        } else if (size != null) {
            ConsoleLib.setTitle(title, size = size)
        } else {
            ConsoleLib.setTitle(title)
        }
    }

    /**
     * 设置控制台的位置，单位像素。
     */
    fun setPosition(x: Int, y: Int) {
        return ConsoleLib.setPosition(x, y)
    }

    /**
     * size {number} 字号大小，单位是dp或sp 20以内比较合适
     * 设置log字号大小
     */
    fun setLogSize(x: Int) {
        return ConsoleLib.setLogSize(x)
    }

    /**
     * 控制 console 是否可以输入文字
     */
    fun setCanInput(can: Boolean = true) {
        return ConsoleLib.setCanInput(can)
    }

    /**
     * 设置 console 背景色,需要在显示控制台之后才能设置，否则空指针
     * 颜色值 #AARRGGBB
     */
    fun setBackgroud(color: String) {
        return ConsoleLib.setBackgroud(color)
    }

    fun setBackground() {
        return ConsoleLib.setBackground()
    }

    /**
     * maxLines {number} 最大行数 如 10 行 设置 console 显示最大行数，默认-1，不限 ，
     * 超出行数系统会清空，从0开始显示 不限制，显示列表过长，android内存又不足，系统会回收console的引用，即console 将不显示。
     */
    fun setMaxLines(maxLines: Int) {
        return ConsoleLib.setMaxLines(maxLines)
    }
}

/**
 * 控制台模块提供了一个和Web浏览器中相似的用于调试的控制台。用于输出一些调试信息、中间结果等。 console模块中的一些函数也可以直接作为全局函数使用，例如log, print等。
 */
@Deprecated("Use Console")
@JsName("console") // 这里的 "console" 是假设的模块名
external object ConsoleLib {
    /**
     * 显示控制台。这会显示一个控制台的悬浮窗(需要悬浮窗权限)。
     * 结束是否自动隐藏
     */
    fun show(autoHide: Boolean = definedExternally)

    /**
     * 隐藏控制台悬浮窗。
     */
    fun hide()

    /**
     * 清空控制台。
     */
    fun clear()

    /**
     * 打印到控制台，并带上换行符。 可以传入多个参数，第一个参数作为主要信息，其他参数作为类似于 printf(3) 中的代替值（参数都会传给 util.format()）。
     */
    fun log(data: String, vararg args: dynamic) // vararg args 对应于 TypeScript 中的 ...args

    /**
     * 与console.log类似，但输出结果以灰色字体显示。输出优先级低于log，用于输出观察性质的信息。
     */
    fun verbose(data: String, vararg args: dynamic)

    /**
     * 与console.log类似，但输出结果以绿色字体显示。输出优先级高于log, 用于输出重要信息。
     */
    fun info(data: String, vararg args: dynamic)

    /**
     * 与console.log类似，但输出结果以蓝色字体显示。输出优先级高于info, 用于输出警告信息。
     */
    fun warn(data: String, vararg args: dynamic)

    /**
     * 与console.log类似，但输出结果以红色字体显示。输出优先级高于warn, 用于输出错误信息。
     */
    fun error(data: String, vararg args: dynamic)

    /**
     * 断言。如果value为false则输出错误信息message并停止脚本运行。
     */
    fun assert(value: Boolean, message: String)

    /**
     * 与console.log一样输出信息，并在控制台显示输入框等待输入。按控制台的确认按钮后会将输入的字符串用eval计算后返回。
     */
    fun input(data: String, vararg args: dynamic): dynamic // 返回类型为 dynamic，表示可以是 String, Number 或 Boolean

    /**
     * 与console.log一样输出信息，并在控制台显示输入框等待输入。按控制台的确认按钮后会将输入的字符串直接返回。
     */
    fun rawInput(data: String, vararg args: dynamic): String

    /**
     * config {Object} 日志配置，可选的项有：
     *      file {string} 日志文件路径，将会把日志写入该文件中
     *      maxFileSize {number} 最大文件大小，单位字节，默认为512 * 1024 (512KB)
     *      rootLevel {string} 写入的日志级别，默认为"ALL"（所有日志），可以为"OFF"(关闭), "DEBUG", "INFO", "WARN", "ERROR", "FATAL"等。
     *      maxBackupSize {number} 日志备份文件最大数量，默认为5
     *      filePattern {string} 日志写入格式，参见PatternLayout
     * 设置日志保存的路径和配置。例如把日志保存到"/sdcard/1.txt":
     */
    fun setGlobalLogConfig(config: Config)

    class Config {
        var file: String? = definedExternally
        var maxFileSize: Int? = definedExternally
        var rootLevel: String? = definedExternally
        var maxBackupSize: Int? = definedExternally
        var filePattern: String? = definedExternally
    }

    /**
     * 设置控制台的大小，单位像素。
     */
    fun setSize(width: Int, height: Int)

    /**
     * label {String} 计时器标签，可省略
     * 启动一个计时器，用以计算一个操作的持续时间。 计时器由一个唯一的 label 标识。 若label重复，则会覆盖上一个同名label的计时器。 以同名 label调用 console.timeEnd() 来停止计时器，并以毫秒为单位将持续时间输出到控制台。     */
    fun time(label: String)

    /**
     * label {String} 计时器标签
     * 停止之前通过调用 console.time() 启动的定时器，并打印结果到控制台。 调用 console.timeEnd() 后定时器会被删除。如果不存在标签指定的定时器则会打印 NaNms。
     */
    fun timeEnd(label: String)

    /**
     * data {any}
     * ...args {any}
     * 与console.log类似，同时会打印出调用这个函数所在的调用栈信息（即当前运行的文件、行数等信息）。
     */
    fun trace(data: dynamic, vararg args: dynamic)

    /**
     * text {string} | {Object} 要打印到控制台的信息
     */
    fun print(text: dynamic)

    /**
     * 设置标题名称，字体颜色，标题栏高度
     * title {string} 标题
     * color {string} 颜色值 #AARRGGBB
     * size {number} 标题高度，字号会随高度变化，单位是dp
     */
    fun setTitle(title: String = definedExternally, color: String = definedExternally, size: Int = definedExternally)

    /**
     * 设置控制台的位置，单位像素。
     */
    fun setPosition(x: Int, y: Int)

    /**
     * size {number} 字号大小，单位是dp或sp 20以内比较合适
     * 设置log字号大小
     */
    fun setLogSize(x: Int)

    /**
     * 控制 console 是否可以输入文字
     */
    fun setCanInput(can: Boolean = definedExternally)

    /**
     * 设置 console 背景色,需要在显示控制台之后才能设置，否则空指针
     * 颜色值 #AARRGGBB
     */
    fun setBackgroud(color: String)
    fun setBackground()

    /**
     * maxLines {number} 最大行数 如 10 行 设置 console 显示最大行数，默认-1，不限 ，
     * 超出行数系统会清空，从0开始显示 不限制，显示列表过长，android内存又不足，系统会回收console的引用，即console 将不显示。
     */
    fun setMaxLines(maxLines: Int)
}

///**
// * 控制台只能在工作线程中使用
// * 显示控制台。这会显示一个控制台的悬浮窗(需要悬浮窗权限)。
// * 结束是否自动隐藏
// */
//fun Console.show(autoHide: Boolean = false) {
//    if (isUIThread()) {
//        throw IllegalStateException("Prohibit the console from running in the main (UI) thread")
//    }
//    eval("console.show(autoHide)")
//}