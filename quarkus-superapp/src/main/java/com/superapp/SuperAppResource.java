package com.superapp;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.superapp.framework.SuperApp;

@Path("/super")
public class SuperAppResource {

    @Inject
    SuperApp app;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        app.OnInCommingCall();
        return "Super App Ready";
    }
}
