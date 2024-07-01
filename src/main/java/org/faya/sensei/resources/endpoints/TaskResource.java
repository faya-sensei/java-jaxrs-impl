package org.faya.sensei.resources.endpoints;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.json.Json;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;
import jakarta.ws.rs.sse.SseEventSink;
import org.faya.sensei.middlewares.JWTAuth;
import org.faya.sensei.payloads.TaskDTO;
import org.faya.sensei.services.IService;

import java.util.Optional;

@Singleton
public class TaskResource {

    private final IService<TaskDTO> taskService;

    private final SseBroadcaster broadcaster;

    private final Sse sse;

    @Inject
    public TaskResource(final IService<TaskDTO> taskService, @Context final Sse sse) {
        this.taskService = taskService;
        this.sse = sse;
        this.broadcaster = sse.newBroadcaster();
    }

    @POST
    @JWTAuth
    public Response saveTask(final TaskDTO task) {
        try (Jsonb binder = JsonbBuilder.create()) {
            Optional<TaskDTO> taskDTO = taskService.create(task);
            taskDTO.ifPresent(dto -> broadcaster.broadcast(sse.newEventBuilder()
                    .name("task-create")
                    .mediaType(MediaType.APPLICATION_JSON_TYPE)
                    .data(String.class, binder.toJson(taskDTO.get()))
                    .build()));

            return taskDTO.isPresent()
                    ? Response.ok(binder.toJson(taskDTO.get())).build()
                    : Response.notModified().build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @PUT
    @JWTAuth
    @Path("{id: \\d+}")
    public Response updateTask(@PathParam("id") final int id, final TaskDTO task) {
        try (Jsonb binder = JsonbBuilder.create()) {
            Optional<TaskDTO> taskDTO = taskService.update(id, task);
            taskDTO.ifPresent(dto -> broadcaster.broadcast(sse.newEventBuilder()
                    .name("task-update")
                    .mediaType(MediaType.APPLICATION_JSON_TYPE)
                    .data(String.class, binder.toJson(taskDTO.get()))
                    .build()));

            return taskDTO.isPresent()
                    ? Response.ok(binder.toJson(taskDTO.get())).build()
                    : Response.notModified().build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @DELETE
    @JWTAuth
    @Path("{id: \\d+}")
    public Response deleteTask(@PathParam("id") final int id) {
        boolean success = taskService.remove(id);
        if (success)
            broadcaster.broadcast(sse.newEventBuilder()
                    .name("task-delete")
                    .mediaType(MediaType.APPLICATION_JSON_TYPE)
                    .data(String.class, Json.createObjectBuilder().add("id", id).build().toString())
                    .build());

        return success
                ? Response.status(Response.Status.OK).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void event(@Context final SseEventSink sseEventSink) {
        broadcaster.register(sseEventSink);
    }
}
