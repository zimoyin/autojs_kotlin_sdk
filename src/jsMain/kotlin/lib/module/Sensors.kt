package lib.module

import lib.packages.android

@JsName("sensors")
external object Sensors {

    /**
     * 传感器事件发射器接口
     */
    @JsName("SensorEventEmitter")
    interface SensorEventEmitter {
        /**
         * 当传感器精度改变时触发的事件
         * @param eventName 事件名称
         *      * 事件: 'change' : 当传感器数据改变时触发该事件；该事件触发的最高频繁由sensors.register()指定的 delay 参数决定。
         *      * 事件: 'unsupported_sensor' : 当sensors.ignoresUnsupportedSensor被设置为true并且有不支持的传感器被注册时触发该事件。事件参数的传感器名称。
         *      * 事件: 'accuracy_change': accuracy {number} 表示传感器精度。为以下值之一:
         *             * -1 传感器未连接
         *             * 0 传感器不可读
         *             * 1 低精度
         *             * 2 中精度
         *             * 3 高精度
         * @param callback 回调函数
         */
        @JsName("on")
        fun on(eventName: String, callback: (event: android.hardware.SensorEvent, val0: Double, val1: Double, val2: Double) -> Unit): Unit

        /**
         * 当传感器精度改变时触发的事件
         * @param eventName 事件名称
         *      * 事件: 'change' : 当传感器数据改变时触发该事件；该事件触发的最高频繁由sensors.register()指定的 delay 参数决定。
         *      * 事件: 'unsupported_sensor' : 当sensors.ignoresUnsupportedSensor被设置为true并且有不支持的传感器被注册时触发该事件。事件参数的传感器名称。
         * @param callback 回调函数
         */
        @JsName("on")
        fun on(eventName: String, callback: (event: android.hardware.SensorEvent, val0: Double) -> Unit): Unit
    }

    /**
     * 当不支持传感器时触发的事件
     * @param eventName 事件名称
     * @param callback 回调函数
     */
    @JsName("on")
    fun on(eventName: String /* 'unsupported_sensor' */, callback: (sensorName: String) -> Unit): Unit

    /**
     * 注册传感器
     * @param sensorName 传感器名称
     * @param delay 传感器数据获取的延迟
     * @return 传感器事件发射器
     */
    @JsName("register")
    fun register(sensorName: String, delay: Delay? = definedExternally): SensorEventEmitter

    /**
     * 注销传感器
     * @param emitter 传感器事件发射器
     */
    @JsName("unregister")
    fun unregister(emitter: SensorEventEmitter): Unit

    /**
     * 注销所有传感器
     */
    @JsName("unregisterAll")
    fun unregisterAll(): Unit

    /**
     * 是否忽略不支持的传感器
     */
    var ignoresUnsupportedSensor: Boolean

    /**
     * 传感器数据获取延迟枚举
     */
    @JsName("delay")
    enum class Delay {
        /**
         * 默认的更新频率
         */
        normal,

        /**
         * 适合于用户界面的更新频率
         */
        ui,

        /**
         * 适合于游戏的更新频率
         */
        game,

        /**
         * 最快的更新频率
         */
        fastest
    }
}
