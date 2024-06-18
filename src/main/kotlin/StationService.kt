package org.example


object StationService {
    private val stations = mutableMapOf<Int, MeasuringStation>()
    private val measurements = mutableMapOf<Int, MutableList<Measurement>>()

    //create a new station
    fun addStation(station: MeasuringStation): Boolean {
        if (!stations.contains(station.id)) {
            stations[station.id] = station
            return true
        } else {
            return false
        }
    }

    /*
    return station ObjectDto if object exists
    {
    id: Int
    address: String
    sensorCount: Int
    }
     */

    fun getStation(stationId: Int): MeasuringStationDto? {
        return stations[stationId]?.toDTO()
    }


    /*
    methode for Stations to commit their measurements
    stored in map<stationId, List<Measurement> for ease of access
     */
    fun addMeasurement(stationId: Int, measurement: Measurement) {
        if (measurements.contains(stationId))
            measurements[stationId]?.add(measurement)
        else
            measurements[stationId] = mutableListOf(measurement)
    }

    /*
    returns list of measurements of a provided Station
    can utilize a window
    compares the windowing values with the timestamps to filter
     */
    fun getMeasurements(stationId: Int, start: Long?, end: Long?): List<Measurement>{
        return when{
            start != null && end != null-> {
                measurements[stationId]?.filter { it.ts in start..end } ?: emptyList()
            }

            start != null -> {
                measurements[stationId]?.filter{it.ts >= start} ?: emptyList()
            }

            end != null ->{
                measurements[stationId]?.filter { it.ts <= end } ?: emptyList()
            }

            else -> measurements[stationId]?: emptyList()
        }
    }


    /*
    calls add sensor function of station
     */
    fun addSensorToStation(stationId: Int): Boolean? {
        return stations[stationId]?.addSensor()
    }

}