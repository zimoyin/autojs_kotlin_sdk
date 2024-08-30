package lib.kotlin

import lib.module.UiCollection
import lib.module.UiObject
import lib.module.selector

/**
 *
 * @author : zimo
 * @date : 2024/08/30
 */

/**
 * 以当前控件为基准创建一个选择器
 */
fun UiObject.selector(): ChildSelector {
    return ChildSelector(this)
}

/**
 * 以当前控件为基准创建一个选择器
 */
class ChildSelector(private val node: UiObject) {
    private val selector = selector()


    fun findOnce(): UiObject? {
        return node.findOne(selector)
    }

    @Deprecated("Not Applicable")
    fun findOnce(i: Int): UiObject? {
        return node.findOne(selector)
    }

    fun findOne(): UiObject? {
        return node.findOne(selector)
    }

    @Deprecated("Not Applicable")
    fun findOne(timeout: Int): UiObject? {
        return node.findOne(selector)
    }

    @ExperimentalStdlibApi
    fun untilFind(): UiCollection {
        return node.find(selector)
    }

    fun bounds(left: Int, top: Int, right: Int, bottom: Int): ChildSelector {
        selector.bounds(left, top, right, bottom)
        return this
    }

    fun boundsContains(left: Int, top: Int, right: Int, bottom: Int): ChildSelector {
        selector.boundsContains(left, top, right, bottom)
        return this
    }

    fun boundsInside(left: Int, top: Int, right: Int, bottom: Int): ChildSelector {
        selector.boundsInside(left, top, right, bottom)
        return this
    }

    fun checkable(b: Boolean): ChildSelector {
        selector.checkable(b)
        return this
    }

    fun className(str: String): ChildSelector {
        selector.className(str)
        return this
    }

    fun classNameContains(str: String): ChildSelector {
        selector.classNameContains(str)
        return this
    }

    fun classNameEndsWith(suffix: String): ChildSelector {
        selector.classNameEndsWith(suffix)
        return this
    }

    fun classNameMatches(reg: dynamic): ChildSelector {
        selector.classNameMatches(reg)
        return this
    }

    fun classNameStartsWith(prefix: String): ChildSelector {
        selector.classNameStartsWith(prefix)
        return this
    }

    fun clickable(b: Boolean): ChildSelector {
        selector.clickable(b)
        return this
    }

    fun desc(str: String): ChildSelector {
        selector.desc(str)
        return this
    }

    fun descContains(str: String): ChildSelector {
        selector.descContains(str)
        return this
    }

    fun descEndsWith(suffix: String): ChildSelector {
        selector.descEndsWith(suffix)
        return this
    }

    fun descMatches(reg: dynamic): ChildSelector {
        selector.descMatches(reg)
        return this
    }

    fun descStartsWith(prefix: String): ChildSelector {
        selector.descStartsWith(prefix)
        return this
    }

    fun drawingOrder(order: Int): ChildSelector {
        selector.drawingOrder(order)
        return this
    }

    fun editable(b: Boolean): ChildSelector {
        selector.editable(b)
        return this
    }

    fun enabled(b: Boolean): ChildSelector {
        selector.enabled(b)
        return this
    }

    fun exists(): Boolean {
        return node.find(selector).size() > 0
    }

    @ExperimentalStdlibApi
    fun filter(filter: (obj: UiObject) -> Boolean): ChildSelector {
        selector.filter(filter)
        return this
    }

    fun find(): UiCollection {
        return node.find(selector)
    }


    fun id(resId: String): ChildSelector {
        selector.id(resId)
        return this
    }

    fun idContains(str: String): ChildSelector {
        selector.idContains(str)
        return this
    }

    fun idEndsWith(suffix: String): ChildSelector {
        selector.idEndsWith(suffix)
        return this
    }

    fun idMatches(reg: dynamic): ChildSelector {
        selector.idMatches(reg)
        return this
    }

    fun idStartsWith(prefix: String): ChildSelector {
        selector.idStartsWith(prefix)
        return this
    }

    fun longClickable(b: Boolean): ChildSelector {
        selector.longClickable(b)
        return this
    }

    fun multiLine(b: Boolean): ChildSelector {
        selector.multiLine(b)
        return this
    }

    fun packageName(str: String): ChildSelector {
        selector.packageName(str)
        return this
    }

    fun packageNameContains(str: String): ChildSelector {
        selector.packageNameContains(str)
        return this
    }

    fun packageNameEndsWith(suffix: String): ChildSelector {
        selector.packageNameEndsWith(suffix)
        return this
    }

    fun packageNameMatches(reg: dynamic): ChildSelector {
        selector.packageNameMatches(reg)
        return this
    }

    fun packageNameStartsWith(prefix: String): ChildSelector {
        selector.packageNameStartsWith(prefix)
        return this
    }

    fun scrollable(b: Boolean): ChildSelector {
        selector.scrollable(b)
        return this
    }

    fun selected(b: Boolean): ChildSelector {
        selector.selected(b)
        return this
    }

    fun text(str: String): ChildSelector {
        selector.text(str)
        return this
    }

    fun textContains(str: String): ChildSelector {
        selector.textContains(str)
        return this
    }

    fun textEndsWith(suffix: String): ChildSelector {
        selector.textEndsWith(suffix)
        return this
    }

    fun textMatches(reg: dynamic): ChildSelector {
        selector.textMatches(reg)
        return this
    }

    fun textStartsWith(prefix: String): ChildSelector {
        selector.textStartsWith(prefix)
        return this
    }


    fun waitFor() {
        selector.waitFor()
    }
}