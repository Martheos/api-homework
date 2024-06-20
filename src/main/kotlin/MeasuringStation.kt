package org.example

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class MeasuringStationDto(val id: Int, val address: String, val sensorCount: Int)

@OptIn(DelicateCoroutinesApi::class)
class MeasuringStation(
    val id: Int,
    private val address: String,
) {
    private val sensors: List<Sensor> = emptyList<Sensor>().toMutableList()
    private val chan = Channel<String>()
    /*
    constructor that launches a coroutine to permanently pull measurements,
    as soon, as new ones were piped into the provided channel
     */
    init {
        GlobalScope.launch { receiveMeasurements() }
    }

    /*
    repackages the data into a DTO for serialization
    also only provides size of sensor list instead of list itself
     */
    fun toDTO(): MeasuringStationDto{
        return MeasuringStationDto(id, address, sensors.size)
    }

    /*
    adds sensor to station if station has currently less than 10 sensors attached
     */
   fun  addSensor(): Boolean{
        if (sensors.size < 10){
            val sensor = Sensor(chan)
            sensors.addLast(sensor)
            return true
        }
        else
            return false
    }

    /*
    receive function that permanently listens to its channel for new measurements
    writes new measurements into the Measurements map of the StationService
     */
    private suspend fun receiveMeasurements() {
        for (measurement in chan) {
            StationService.addMeasurement(id, measurement)
        }
    }
}