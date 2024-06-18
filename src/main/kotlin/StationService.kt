package org.example


object StationService {
    private val stations = mutableMapOf<Int, MeasuringStation>()
    private val measurements = mutableMapOf<Int, MutableList<Measurement>>()

    fun addStation(station: MeasuringStation): Boolean{
        if (!stations.contains(station.id)) {
            stations[station.id] = station
            return true
        } else{
            return false
        }
    }

    fun getStation(stationId: Int): MeasuringStation? {
        return stations[stationId]
    }



    fun addMeasurement(stationId: Int, measurement: Measurement){
        if (measurements.contains(stationId))
            measurements[stationId]?.add(measurement)

        else
            measurements[stationId] = mutableListOf(measurement)
    }

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


    fun addSensorToStation(stationId: Int): Boolean? {
        return stations[stationId]?.addSensor()
    }

}