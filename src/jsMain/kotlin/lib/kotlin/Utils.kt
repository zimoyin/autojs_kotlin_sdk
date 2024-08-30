package lib.kotlin

import lib.module.Device.height
import lib.module.Device.width
import lib.module.Image
import lib.module.Images
import lib.module.requestScreenCapture

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

/**
 * 获取屏幕方向
 */
fun getScreenDirection(): ScreenDirection {
    return (context.getResources().getConfiguration().orientation as Int).let {
        when (it) {
            1 -> ScreenDirection.PORTRAIT
            2 -> ScreenDirection.LANDSCAPE
            else -> ScreenDirection.PORTRAIT
        }
    }
}

/**
 * 自动方向请求截图, 注意无法检测平板之类
 */
fun requestAutoScreenCapture(): Boolean {
    return when (getScreenDirection()) {
        lib.kotlin.ScreenDirection.PORTRAIT -> requestScreenCapture(false)
        lib.kotlin.ScreenDirection.LANDSCAPE -> requestScreenCapture(true)
    }
}

/**
 * 根据方向自动截取屏幕，当检测导截取的图片出现透明区域则换个方向截取
 */
fun autoCaptureScreen(): Image {
    var isLandscape = isLandscape()
    requestScreenCapture(isLandscape)
    var screen = Images.captureScreen()
    if (!checkScreenshot(screen, isLandscape)) {
        isLandscape = !isLandscape
        requestScreenCapture(isLandscape)
        screen = Images.captureScreen()
        if (!checkScreenshot(screen, isLandscape)) {
            isLandscape = !isLandscape
            requestScreenCapture(isLandscape)
            screen = Images.captureScreen()
        }
    }
    return screen
}

/**
 * 是否为横屏, 注意无法检测平板之类
 */
fun isLandscape(): Boolean {
    return getScreenDirection() == lib.kotlin.ScreenDirection.LANDSCAPE
}

/**
 * 是否为竖屏, 注意无法检测平板之类
 */
fun isPortrait(): Boolean {
    return getScreenDirection() == lib.kotlin.ScreenDirection.PORTRAIT
}

/**
 * 检测截图是否正确，是否存在透明区域。检测错误方向为横屏的时候使用了竖屏截图
 */
fun checkScreenshot(img: Image, isLandscape: Boolean = isLandscape()): Boolean {
    var isTransparent = false

    if (isLandscape) {
        // 横屏模式：检查顶部和底部区域
        for (y in 0 until height / 4) {
            if ((Images.pixel(img, width / 2, y) ushr 24) == 0) {
                isTransparent = true
                break
            }
        }
        if (!isTransparent) {
            for (y in height * 3 / 4 until height) {
                if ((Images.pixel(img, width / 2, y) ushr 24) == 0) {
                    isTransparent = true
                    break
                }
            }
        }
    } else {
        // 竖屏模式：检查左侧和右侧区域
        for (x in 0 until width / 4) {
            if ((Images.pixel(img, x, height / 2) ushr 24) == 0) {
                isTransparent = true
                break
            }
        }
        if (!isTransparent) {
            for (x in width * 3 / 4 until width) {
                if ((Images.pixel(img, x, height / 2) ushr 24) == 0) {
                    isTransparent = true
                    break
                }
            }
        }
    }

    return !isTransparent
}

fun Image.save(path: String, format: String = "png", quality: Int = 100) {
    Images.save(this, path, format, quality)
}

fun Image.close() {
    recycle()
}

enum class ScreenDirection {
    /**
     * 竖屏
     */
    PORTRAIT,

    /**
     * 横屏
     */
    LANDSCAPE;
}


fun <T> T?.onNotNull(block: (() -> Unit) = {}): T? {
    if (this != null) block()
    return this
}


fun <T> T?.onNull(block: (() -> Unit) = {}): T? {
    if (this == null) block()
    return this
}

fun <T> T?.onNullThrow(msg: String = "执行失败,获取值为 null",block: (() -> Unit) = {}): T {
    if (this == null) {
        block()
        throw ResultException(msg)
    }
    return this
}

fun Boolean.onFalse(block: (() -> Unit) = {}): Boolean {
    if (!this) block()
    return this
}

fun Boolean.onTrue(block: (() -> Unit) = {}): Boolean {
    if (this) block()
    return this
}

fun Boolean?.onFalseThrow(msg: String = "执行失败",block: (() -> Unit) = {}): Boolean {
    if (this == null || !this) {
        block()
        throw ResultException(msg)
    }
    return this
}

class ResultException(msg: String = "result is error") : Exception(msg)