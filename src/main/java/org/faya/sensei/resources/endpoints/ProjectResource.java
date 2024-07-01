package org.faya.sensei.resources.endpoints;

import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.faya.sensei.middlewares.JWTAuth;
import org.faya.sensei.payloads.ProjectDTO;
import org.faya.sensei.services.IService;

import java.util.Collection;
import java.util.Optional;

public class ProjectResource {

    @Inject
    private IService<ProjectDTO> projectService;

    @Path("/tasks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Class<TaskResource> getTaskResource() {
        return TaskResource.class;
    }

    @GET
    @JWTAuth
    public Response getAllProjects(final @Context SecurityContext securityContext) {
        try (Jsonb binder = JsonbBuilder.create()) {
            Collection<ProjectDTO> projectDTOs = projectService.getBy(
                    "users.name",
                    securityContext.getUserPrincipal().getName()
            );

            return Response.ok(binder.toJson(projectDTOs)).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @GET
    @JWTAuth
    @Path("{id: \\d+}")
    public Response getProject(@PathParam("id") final int id) {
        try (Jsonb binder = JsonbBuilder.create()) {
            Optional<ProjectDTO> projectDTO = projectService.get(id);

            return projectDTO.isPresent()
                    ? Response.ok(binder.toJson(projectDTO.get())).build()
                    : Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @POST
    @JWTAuth
    public Response saveProject(final ProjectDTO project) {
        try (Jsonb binder = JsonbBuilder.create()) {
            Optional<ProjectDTO> projectDTO = projectService.create(project);

            return projectDTO.isPresent()
                    ? Response.ok(binder.toJson(projectDTO.get())).build()
                    : Response.notModified().build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }
}
