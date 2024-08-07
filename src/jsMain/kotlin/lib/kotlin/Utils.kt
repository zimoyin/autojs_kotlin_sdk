package lib.kotlin

/**
 *
 * @author : zimo
 * @date : 2024/08/07
 */

/**
 * 获取屏幕宽度
 */
fun getDeviceWidth(): Int {
    val dm = context.getResources().getDisplayMetrics()
    val wm = context.getSystemService(context.WINDOW_SERVICE)
    wm.getDefaultDisplay().getRealMetrics(dm)
    return dm.widthPixels as Int
}

/**
 * 获取屏幕高度
 */
fun getDeviceHeight(): Int {
    val dm = context.getResources().getDisplayMetrics()
    val wm = context.getSystemService(context.WINDOW_SERVICE)
    wm.getDefaultDisplay().getRealMetrics(dm)
    return dm.heightPixels as Int
}