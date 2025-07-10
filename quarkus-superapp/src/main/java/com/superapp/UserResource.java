package com.superapp;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.superapp.framework.SuperApp;
import com.superapp.service.UserService;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    SuperApp app;

    @Inject
    UserService service;

    @POST
    public Response getUser(UserRequest request) throws Exception {
        app.OnInCommingCall();
        try {
            Optional<String> data = app.OnOutGoingCall(() -> service.fetchUser(request.user));
            if (data.isPresent()) {
                return Response.ok("{\"data\":\"" + data.get() + "\"}").build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity("not found").build();
        } catch (TimeoutException | IOException e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("error").build();
        }
    }
}
