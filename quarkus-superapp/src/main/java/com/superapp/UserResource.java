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

import com.superapp.framework.ServiceRuntime;
import com.superapp.service.UserService;
import com.superapp.trace.TraceContext;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    ServiceRuntime runtime;

    @Inject
    UserService service;

    @Inject
    TraceContext trace;

    @POST
    public Response getUser(UserRequest request) throws Exception {
        runtime.startTrace();
        runtime.handleIncomingRequest();
        try {
            Optional<String> data = runtime.handleOutgoingCall(() -> service.fetchUser(request.user));
            if (data.isPresent()) {
                trace.record("result: " + data.get());
                runtime.getTrace();
                return Response.ok("{\"data\":\"" + data.get() + "\"}").build();
            }
            trace.record("result: not found");
            runtime.getTrace();
            return Response.status(Response.Status.NOT_FOUND).entity("not found").build();
        } catch (TimeoutException | IOException e) {
            trace.record("error: " + e.getClass().getSimpleName());
            runtime.getTrace();
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("error").build();
        }
    }
}
