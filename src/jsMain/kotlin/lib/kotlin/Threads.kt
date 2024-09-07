package lib.kotlin

import lib.module.Thread
import lib.module.UI
import lib.module.getName

/**
 *
 * @author : zimo
 * @date : 2024/07/30
 */
fun thread(action: () -> Unit) {
    Thread.start(action)
}

fun ui(action: () -> Unit) {
    UI.run(action)
}

fun isUIThread(): Boolean {
    return Thread.getName().trim() === "main"
}

fun Thread.isUIMainThread(): Boolean {
    return Thread.getName().trim() === "main"
}