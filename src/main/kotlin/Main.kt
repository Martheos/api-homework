package org.example

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.*

@Serializable
data class StationRequest(val id: Int, val address: String)
@Serializable
data class MeasurementQuery(val id: Int, val start: Long? = null, val end: Long? = null)

fun main() {
    embeddedServer(Netty, port = 8080){
        install(ContentNegotiation){
            json()
        }
        configureRouting()
    }.start(wait = true)
}

fun Application.configureRouting() {
    routing {
        route("/stations") {
            post {
                val stationRequest = call.receive<StationRequest>()
                if(StationService.addStation(MeasuringStation(stationRequest.id, stationRequest.address))){
                    call.respond(HttpStatusCode.Created, "Station with id ${stationRequest.id} created")
                }else {
                    call.respond(HttpStatusCode.BadRequest, "Station with this id already exists")
                }
            }
            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id != null) {
                    val station = StationService.getStation(id)
                    if (station != null) {
                        call.respond(station)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
            post("/{stationId}/sensor") {
                val id = call.parameters["stationId"]?.toIntOrNull()
                if (id != null) {
                    val result = StationService.addSensorToStation(id)
                    when (result) {
                        true -> call.respond(HttpStatusCode.Created, "Success")
                        false -> call.respond(HttpStatusCode.BadRequest, "Too many sensors for this station")
                        null -> call.respond(HttpStatusCode.BadRequest, "No Station with this id found")
                    }
                }
            }
            /*
            expects json in form of
            {id: Int, optional start (millis), optional end(millis)}i
            returns list of
            {ts (millis), address: String}
             */
            route("/measurements") {
                get {
                    val query = call.receive<MeasurementQuery>()
                        if (StationService.getStation(query.id) != null) {
                            val measurements = StationService.getMeasurements(query.id, query.start, query.end)
                            call.respond(measurements)
                        } else{
                            call.respond(HttpStatusCode.BadRequest, "No station with this id")
                        }
                }
            }
        }
    }
}
