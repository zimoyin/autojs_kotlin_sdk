package lib.kotlin

import kotlin.js.Date
import kotlin.random.Random

class UUID private constructor(private val uuid: String) {
    companion object {
        fun randomUUID(): UUID {
            val timeStamp = Date().getTime().toLong()
            val random = Random.nextInt(0, 16)
            val timeLow = timeStamp.toString(16).padStart(8, '0')
            val timeMid = (random or (random shl 4)).toString(16).padStart(4, '0')
            val timeHiAndVersion = ((random or (random shl 4)) or 0x4000).toString(16).padStart(4, '0') // version 4
            val clockSeqAndReserved = ((random or (random shl 4)) or 0x8000).toString(16).padStart(4, '0') // variant
            val node = List(6) { Random.nextInt(0, 16) }.joinToString("") { it.toString(16).padStart(2, '0') }
            
            val uuid = "$timeLow-$timeMid-$timeHiAndVersion-$clockSeqAndReserved-$node"
            return UUID(uuid)
        }
    }

    override fun toString(): String {
        return uuid
    }
}