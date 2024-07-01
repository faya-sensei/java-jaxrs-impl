package org.faya.sensei.resources.endpoints;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;

public class HeartBeatResource {

    @GET
    public Response getHeartbeat() {
        JsonObject heartBeatPack = Json.createObjectBuilder()
                .add("status", "alive")
                .add("time", LocalDateTime.now().toString())
                .build();

        return Response.ok(heartBeatPack).build();
    }
}
