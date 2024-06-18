package org.example

import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.random.Random


data class Measurement(
    val ts: Long,
    val value: Double
)
class Sensor {
    private val timer = Timer()

    init {
        timer.scheduleAtFixedRate(0, 60000) {
            takeMeasurement()
        }
    }

    private fun takeMeasurement(): Measurement {
        val ms = Measurement(System.currentTimeMillis(), Random.nextDouble(0.0, 100.0))
        println(ms)
        return ms
    }
    }