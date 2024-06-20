package org.example

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import javax.xml.crypto.Data
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.random.Random


@Serializable
data class Measurement(
    val ts: Long,
    val value: Double
)
class Sensor(private val chan: Channel<String>) {
    private val timer = Timer()

    /*
    constructor with a timer that pipes a new measurement
    from takeMeasurement() into the channel, provided by the station

    timer activates every 60 seconds and runs asynchronously
     */
    init {
        timer.scheduleAtFixedRate(0, 60000) {
            runBlocking {
                chan.send(takeMeasurement())
            }
        }
    }

    /*
    function that takes current time in millis
     and a random double between 0 and 100 to
     create a measurement and returns it
     */
    private fun takeMeasurement(): Measurement {
        val measure = Measurement(System.currentTimeMillis(), Random.nextDouble(0.0, 100.0))
        println(measure)
        return measure
    }
}