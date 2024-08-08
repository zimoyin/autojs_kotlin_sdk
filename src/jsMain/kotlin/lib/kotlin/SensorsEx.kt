package lib.kotlin

import lib.module.Sensors
import lib.module.importClass
import lib.module.setIntervalByKotlin
import lib.module.sleep
import lib.packages.android

/**
 *
 * @author : zimo
 * @date : 2024/08/08
 */

/**
 * accelerometer 加速度传感器，参数(event, ax, ay, az):
 *
 * event SensorEvent 传感器事件，用于获取传感器数据变化时的所有信息
 * ax {number} x 轴上的加速度，单位 m/s^2
 * ay {number} y 轴上的加速度，单位 m/s^2
 * az {number} z 轴上的加速度，单位 m/s^2 这里的 x 轴，y 轴，z 轴所属的坐标系统如下图(其中 z 轴垂直于设备屏幕表面):
 */
fun Sensors.registerAccelerometerSensor(
    delay: Sensors.Delay = Sensors.Delay.game,
    callback: (data: AccelerometerSensorData) -> Unit
): Sensors.SensorEventEmitter {
    return register("accelerometer", delay).apply {
        on("change") { event, ax, ay, az ->
            callback(AccelerometerSensorData(event, ax, ay, az))
        }
    }
}

data class AccelerometerSensorData(val event: Any, val ax: Double, val ay: Double, val az: Double)

/**
 * orientation 方向传感器，参数(event, azimuth, pitch, roll):
 *
 * event SensorEvent 传感器事件，用于获取传感器数据变化时的所有信息
 * azimuth {number} 方位角，从地磁指北方向线起，依顺时针方向到 y 轴之间的水平夹角，单位角度，范围 0~359
 * pitch {number} 绕 x 轴旋转的角度，当设备水平放置时该值为 0，当设备顶部翘起时该值为正数，当设备尾部翘起时该值为负数，单位角度，范围-180~180
 * roll {number} 绕 y 轴顺时针旋转的角度，单位角度，范围-90~90
 */
fun Sensors.registerOrientationSensor(
    delay: Sensors.Delay = Sensors.Delay.game,
    callback: (data: OrientationSensorData) -> Unit
): Sensors.SensorEventEmitter {
    return register("orientation", delay).apply {
        on("change") { event, azimuth, pitch, roll ->
            callback(OrientationSensorData(event, azimuth, pitch, roll))
        }
    }
}

data class OrientationSensorData(val event: Any, val azimuth: Double, val pitch: Double, val roll: Double)

/**
 * gyroscope 陀螺仪传感器，参数(event, wx, wy, wz):
 *
 * event SensorEvent 传感器事件，用于获取传感器数据变化时的所有信息
 * wx {number} 绕 x 轴的角速度，单位弧度/s
 * wy {number} 绕 y 轴的角速度，单位弧度/s
 * wz {number} 绕 z 轴的角速度，单位弧度/s
 */
fun Sensors.registerGyroscopeSensor(
    delay: Sensors.Delay = Sensors.Delay.game,
    callback: (data: GyroscopeSensorData) -> Unit
): Sensors.SensorEventEmitter {
    return register("gyroscope", delay).apply {
        on("change") { event, wx, wy, wz ->
            callback(GyroscopeSensorData(event, wx, wy, wz))
        }
    }
}

data class GyroscopeSensorData(val event: Any, val wx: Double, val wy: Double, val wz: Double)

/**
 * magnetic_field 磁场传感器，参数(event, bx, by, bz):
 *
 * event SensorEvent 传感器事件，用于获取传感器数据变化时的所有信息
 * bx {number} x 轴上的磁场强度，单位 uT
 * by {number} y 轴上的磁场强度，单位 uT
 * bz {number} z 轴上的磁场强度，单位 uT
 */
fun Sensors.registerMagneticFieldSensor(
    delay: Sensors.Delay = Sensors.Delay.game,
    callback: (data: MagneticFieldSensorData) -> Unit
): Sensors.SensorEventEmitter {
    return register("magnetic_field", delay).apply {
        on("change") { event, bx, by, bz ->
            callback(MagneticFieldSensorData(event, bx, by, bz))
        }
    }
}

data class MagneticFieldSensorData(val event: Any, val bx: Double, val by: Double, val bz: Double)

/**
 * gravity 重力传感器，参数(event, gx, gy, gz):
 *
 * event SensorEvent 传感器事件，用于获取传感器数据变化时的所有信息
 * gx {number} x 轴上的重力加速度，单位 m/s^2
 * gy {number} y 轴上的重力加速度，单位 m/s^2
 * gz {number} z 轴上的重力加速度，单位 m/s^2
 *
 */
fun Sensors.registerGravitySensor(
    delay: Sensors.Delay = Sensors.Delay.game,
    callback: (data: GravitySensorData) -> Unit
): Sensors.SensorEventEmitter {
    return register("gravity", delay).apply {
        on("change") { event, gx, gy, gz ->
            callback(GravitySensorData(event, gx, gy, gz))
        }
    }
}

data class GravitySensorData(val event: Any, val gx: Double, val gy: Double, val gz: Double)

/**
 * linear_acceleration 线性加速度传感器，参数(event, ax, ay, az):
 *
 * event SensorEvent 传感器事件，用于获取传感器数据变化时的所有信息
 * ax {number} x 轴上的线性加速度，单位 m/s^2
 * ay {number} y 轴上的线性加速度，单位 m/s^2
 * az {number} z 轴上的线性加速度，单位 m/s^2
 */
fun Sensors.registerLinearAccelerationSensor(
    delay: Sensors.Delay = Sensors.Delay.game,
    callback: (data: LinearAccelerationSensorData) -> Unit
): Sensors.SensorEventEmitter {
    return register("linear_acceleration", delay).apply {
        on("change") { event, ax, ay, az ->
            callback(LinearAccelerationSensorData(event, ax, ay, az))
        }
    }
}

data class LinearAccelerationSensorData(val event: Any, val ax: Double, val ay: Double, val az: Double)

/**
 * ambient_temperature 环境温度传感器，大部分设备并不支持，参数(event, t):
 *
 * event SensorEvent 传感器事件，用于获取传感器数据变化时的所有信息
 * t {number} 环境温度，单位摄氏度。
 */
fun Sensors.registerAmbientTemperatureSensor(
    delay: Sensors.Delay = Sensors.Delay.game,
    callback: (data: AmbientTemperatureSensorData) -> Unit
): Sensors.SensorEventEmitter {
    return register("ambient_temperature", delay).apply {
        on("change") { event, t ->
            callback(AmbientTemperatureSensorData(event, t))
        }
    }
}

data class AmbientTemperatureSensorData(val event: Any, val t: Double)

/**
 * light 光线传感器，参数(event, light):
 *
 * event SensorEvent 传感器事件，用于获取传感器数据变化时的所有信息
 * light {number} 环境光强度，单位 lux
 */
fun Sensors.registerLightSensor(
    delay: Sensors.Delay = Sensors.Delay.game,
    callback: (data: LightSensorData) -> Unit
): Sensors.SensorEventEmitter {
    return register("light", delay).apply {
        on("change") { event, light ->
            callback(LightSensorData(event, light))
        }
    }
}

data class LightSensorData(val event: Any, val light: Double)

/**
 * pressure 压力传感器，参数(event, p):
 *
 * event SensorEvent 传感器事件，用于获取传感器数据变化时的所有信息
 * p {number} 大气压，单位 hPa
 */
fun Sensors.registerPressureSensor(
    delay: Sensors.Delay = Sensors.Delay.game,
    callback: (data: PressureSensorData) -> Unit
): Sensors.SensorEventEmitter {
    return register("pressure", delay).apply {
        on("change") { event, p ->
            callback(PressureSensorData(event, p))
        }
    }
}

data class PressureSensorData(val event: Any, val p: Double)

/**
 * proximity 距离传感器，参数(event, distance):
 *
 * event SensorEvent 传感器事件，用于获取传感器数据变化时的所有信息
 * distance {number} 一般指设备前置摄像头旁边的距离传感器到前方障碍物的距离，并且很多设备上这个值只有两种情况：当障碍物较近时该值为 0，当障碍物较远或在范围内没有障碍物时该值为 5
 *
 */
fun Sensors.registerProximitySensor(
    delay: Sensors.Delay = Sensors.Delay.game,
    callback: (data: ProximitySensorData) -> Unit
): Sensors.SensorEventEmitter {
    return register("proximity", delay).apply {
        on("change") { event, distance ->
            callback(ProximitySensorData(event, distance))
        }
    }
}

data class ProximitySensorData(val event: Any, val distance: Double)

/**
 * relative_humidity 湿度传感器，大部分设备并不支持，参数(event, rh):
 *
 * event SensorEvent 传感器事件，用于获取传感器数据变化时的所有信息
 * rh {number} 相对湿度，范围为 0~100（百分比）
 */
fun Sensors.registerRelativeHumiditySensor(
    delay: Sensors.Delay = Sensors.Delay.game,
    callback: (data: RelativeHumiditySensorData) -> Unit
): Sensors.SensorEventEmitter {
    return register("relative_humidity", delay).apply {
        on("change") { event, rh ->
            callback(RelativeHumiditySensorData(event, rh))
        }
    }
}

data class RelativeHumiditySensorData(val event: Any, val rh: Double)


/**
 * 务必调用 disable() 方法接收监听否则会导致监听器无法被关闭。即便是脚本结束不调用 disable() 方法，监听器也不会关闭
 */

fun Sensors.registerOrientationValueSensor(callback: (value: Int) -> Unit): OrientationValueSensorResult {
    val obj = eval(
        """
        importClass(android.content.Context);
        var context_t = context || getApplicationContext();
        new JavaAdapter(android.view.OrientationEventListener, {
            onOrientationChanged: function (orientation) {
                var id = engines.myEngine().id
                var stop = true
                if(engines.myEngine().isDestroyed()) {
                    this.disable()
                }
                for (let item of engines.all()) {
                    if(item.id == id){
                        if(item.isDestroyed()){
                            this.disable()
                        }
                        stop = false
                    }
                }
                if(stop) {
                    console.error("没有调用 OrientationValueSensorResult.disable() 方法，监听器被延迟关闭")
                    this.disable()
                }
                callback(orientation)
            }
        }, context_t);
    """.trimIndent()
    )
    return OrientationValueSensorResult(obj).apply {
        enable()
    }
}

class OrientationValueSensorResult(private val obj: dynamic) {
    var isEnabled = false
        private set
    fun disable() {
        isEnabled = false
        obj.disable()
    }

    fun enable() {
        if (isEnabled) return
        isEnabled = true
        obj.enable()
        thread {
            while (isEnabled) {
                sleep(1000)
            }
        }
    }
}

private interface OrientationEventListenerKt {
    fun onOrientationChanged(orientation: Int)
}

/**
 * 关闭传感器
 */
fun Sensors.SensorEventEmitter.close() {
    Sensors.unregister(this)
}