package com.superapp;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.superapp.framework.ServiceRuntime;

@Path("/super")
public class ServiceRuntimeResource {

    @Inject
    ServiceRuntime runtime;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        runtime.handleIncomingRequest();
        return "Super App Ready";
    }
}
