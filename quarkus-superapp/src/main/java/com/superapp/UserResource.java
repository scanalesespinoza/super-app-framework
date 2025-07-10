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
import com.superapp.framework.CentralFlow;
import com.superapp.service.UserService;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    ServiceRuntime runtime;

    @Inject
    CentralFlow flow;

    @Inject
    UserService service;


    @POST
    public Response getUser(UserRequest request) throws Exception {
        CentralFlow.FlowResult<Optional<String>> result =
                flow.executeUser(request, () -> service.fetchUser(request.user));
        if (!result.success) {
            if ("invalid request".equals(result.reason)) {
                return Response.status(Response.Status.BAD_REQUEST).entity(result.reason).build();
            }
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(result.reason).build();
        }
        Optional<String> data = result.payload;
        if (data != null && data.isPresent()) {
            return Response.ok("{\"data\":\"" + data.get() + "\"}").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("not found").build();
    }
}
