package com.superapp;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.superapp.trace.TraceContext;

/**
 * Endpoint that returns an ASCII trace of the request flow.
 */
@Path("/trace")
@Produces(MediaType.TEXT_PLAIN)
public class TraceResource {

    @Inject
    TraceContext trace;

    @GET
    public String trace() {
        return trace.getLastDump();
    }
}
