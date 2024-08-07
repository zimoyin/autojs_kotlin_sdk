package lib.module

@JsName("auto")
external object Auto {

    /**
     * 检查无障碍服务是否已经启用，如果没有启用则跳转到无障碍服务启用界面，并等待无障碍服务启动；当无障碍服务启动后脚本会继续运行。
     */
    fun waitFor()

    /**
     * 设置自动化模式。
     * @param mode 模式，可以是 'fast' 或 'normal'
     */
    fun setMode(mode: String /* 'fast' | 'normal' */)
}
