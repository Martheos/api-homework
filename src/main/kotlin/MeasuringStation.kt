package org.example

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.Serializable

@Serializable
data class MeasuringStationDto(val id: Int, val address: String, val sensorCount: Int)

@OptIn(DelicateCoroutinesApi::class)
class MeasuringStation(
    val id: Int,
    val address: String,
) {
    private val sensors: List<Sensor> = emptyList<Sensor>().toMutableList()
    private val chan = Channel<Measurement>()
    init {
        GlobalScope.launch { receiveMeasurements() }
    }

    fun toDTO(): MeasuringStationDto{
        return MeasuringStationDto(id, address, sensors.size)
    }

   fun  addSensor(): Boolean{
        if (sensors.size < 10){
            val sensor = Sensor(chan)
            sensors.addLast(sensor)
            return true
        }
        else
            return false
    }

    private suspend fun receiveMeasurements() {
        for (measurement in chan) {
            StationService.addMeasurement(id, measurement)
        }
    }
}