package lib.module

@JsName("floaty")

external object Floaty {

    /**
     * 检查悬浮窗权限
     */
    fun checkPermission(): Boolean

    /**
     * 请求悬浮窗权限
     */
    fun requestPermission()

    /**
     * 创建一个悬浮窗
     * @param layout 布局, {xml} | {View} 悬浮窗界面的XML或者View
     * @return 返回一个悬浮窗对象
     */
    fun window(layout: dynamic): FloatyWindow

    /**
     * 创建一个悬浮窗
     * 指定悬浮窗的布局，创建并显示一个原始悬浮窗，返回一个FloatyRawWindow对象。
     * 与floaty.window()函数不同的是，该悬浮窗不会增加任何额外设施（例如调整大小、位置按钮），您可以根据自己需要编写任何布局。
     * 而且，该悬浮窗支持完全全屏，可以覆盖状态栏，因此可以做护眼模式之类的应用。
     *
     * @param layout 布局, {xml} | {View} 悬浮窗界面的XML或者View
     * @return 返回一个悬浮窗对象
     */
    fun rawWindow(layout: dynamic): FloatyWindow

    /**
     * 关闭所有悬浮窗
     */
    fun closeAll(): Unit



    /**
     * 悬浮窗对象，可通过FloatyWindow.{id}获取悬浮窗界面上的元素。
     * 例如, 悬浮窗window上一个控件的id为aaa, 那么window.getById("aaa")即可获取到该控件，类似于ui。
     */
    interface FloatyWindow {
        /**
         * rawWindow 创建的悬浮窗可以使用
         * 设置悬浮窗是否可触摸，如果为true, 则悬浮窗将接收到触摸、点击等事件并且无法继续传递到悬浮窗下面；
         * 如果为false, 悬浮窗上的触摸、点击等事件将被直接传递到悬浮窗下面。
         * 处于安全考虑，被悬浮窗接收的触摸事情无法再继续传递到下层。
         * 可以用此特性来制作护眼模式脚本。
         * touchable {Boolean} 是否可触摸
         */
        fun setTouchable(touchable: Boolean): Unit


        /**
         * 设置是否允许调整悬浮窗大小
         * 如果enabled为true，则在悬浮窗左上角、右上角显示可供位置、大小调整的标示，就像控制台一样； 如果enabled为false，则隐藏上述标示。
         * @param enabled 是否允许调整
         */
        fun setAdjustEnabled(enabled: Boolean): Unit

        /**
         * 设置悬浮窗位置
         * @param x X轴坐标
         * @param y Y轴坐标
         */
        fun setPosition(x: Int, y: Int): Unit


        /**
         * 获取悬浮窗的X轴坐标
         * @return X轴坐标
         */
        fun getX(): Int

        /**
         * 获取悬浮窗的Y轴坐标
         * @return Y轴坐标
         */
        fun getY(): Int

        /**
         * 设置悬浮窗大小
         * 特别地，如果是 FloatyRawWindow对象： 值设置为-1，则为占满全屏；设置为-2则为根据悬浮窗内容大小而定。
         * @param width 宽度
         * @param height 高度
         */
        fun setSize(width: Int, height: Int): Unit

        /**
         * 获取悬浮窗的宽度
         * @return 宽度
         */
        fun getWidth(): Int

        /**
         * 获取悬浮窗的高度
         * @return 高度
         */
        fun getHeight(): Int

        /**
         * 关闭悬浮窗
         */
        fun close(): Unit

        /**
         * 使悬浮窗被关闭时自动结束脚本运行。
         */
        fun exitOnClose(): Unit
    }
}

/**
 * 获取指定id的控件
 */
fun Floaty.FloatyWindow.getById(id: String): dynamic {
    val fw = this
    return eval("fw.$id")
}