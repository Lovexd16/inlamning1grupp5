package org.inlamning1grupp5.resource;

import org.inlamning1grupp5.service.PodcastService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/podcasts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PodcastResource {

    @Inject
    PodcastService podcastService;

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/get-podcast")
    public Response getPodcast(@HeaderParam("productId") String productId) {
        return podcastService.getPodcastFromServer(productId);   
    }
}
