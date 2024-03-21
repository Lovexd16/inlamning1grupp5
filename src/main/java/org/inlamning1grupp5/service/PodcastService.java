package org.inlamning1grupp5.service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.core.Response;

@Transactional(Transactional.TxType.SUPPORTS)
@ApplicationScoped
public class PodcastService {
    
    public Response getPodcastFromServer(String productId) {

        try {
            String newPath = "C:\\Users\\loveb\\OneDrive\\Dokument\\GitHub\\inlamning1grupp5\\src\\main\\resources\\META-INF\\resources\\" + productId + ".mp3";
            java.nio.file.Path filePath = java.nio.file.Path.of(newPath);
            InputStream audioFile = Files.newInputStream(filePath, StandardOpenOption.READ);
            return Response.ok(audioFile).build();
        } catch (NoSuchFileException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("File not found").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    public Response getAllPodcastsForSubscriber(@NotEmpty String username) {
        List<InputStream> allPodcasts = new ArrayList<>();
        String staticResourcePath = "C:\\Users\\loveb\\OneDrive\\Dokument\\GitHub\\inlamning1grupp5\\src\\main\\resources\\META-INF\\resources\\";
        File staticResources = new File(staticResourcePath);
        File[] staticFiles = staticResources.listFiles();
        
        for (File file : staticFiles) {
            String fileType = file.getName().substring(file.getName().length() - 3);
            
            if(fileType.equals("mp3")) {
                try {
                    java.nio.file.Path filePath = java.nio.file.Path.of(file.getAbsolutePath());
                    InputStream audioFile = Files.newInputStream(filePath, StandardOpenOption.READ);
                    allPodcasts.add(audioFile);
                } catch (Exception e) {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
                }
            };
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResult = objectMapper.writeValueAsString(allPodcasts);
            return Response.ok(jsonResult).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    public Response getAllFreePodcasts(int episodeNumber) {
        try {
            String newPath = "C:\\Users\\loveb\\OneDrive\\Dokument\\GitHub\\inlamning1grupp5\\src\\main\\resources\\META-INF\\resources\\episode" + episodeNumber + "Free.mp3";
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
