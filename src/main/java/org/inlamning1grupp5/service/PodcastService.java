package org.inlamning1grupp5.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardOpenOption;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@Transactional(Transactional.TxType.SUPPORTS)
@ApplicationScoped
public class PodcastService {
    
    public Response getPodcastFromServer(String productId) {

        try {
            String newPath = "C:\\Users\\david\\repos\\github\\Pod grupp5\\inlamning1grupp5\\src\\main\\resources\\META-INF\\resources\\" + productId + ".mp3";
            java.nio.file.Path filePath = java.nio.file.Path.of(newPath);
            InputStream audioFile = Files.newInputStream(filePath, StandardOpenOption.READ);
            return Response.ok(audioFile).build();
        } catch (NoSuchFileException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("File not found").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
