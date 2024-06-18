package org.example

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.random.Random


@Serializable
data class Measurement(
    val ts: Long,
    val value: Double
)
class Sensor(private val chan: Channel<Measurement>) {
    private val timer = Timer()
    init {
        timer.scheduleAtFixedRate(0, 60000) {
            runBlocking {
                chan.send(takeMeasurement())
            }
        }
    }

    private fun takeMeasurement(): Measurement {
        val ms = Measurement(System.currentTimeMillis(), Random.nextDouble(0.0, 100.0))
        println(ms)
        return ms
    }
}