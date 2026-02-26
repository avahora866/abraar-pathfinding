package com.abraar.pathfinding.service.geographicalData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Service
public class PBFFileHandler {

    @Autowired
    private RestTemplate restTemplate;

    public void downloadFile(String city, String targetFilePath) {
        String urlString = "https://download.geofabrik.de/europe/united-kingdom/england/" + city + "-latest.osm.pbf";

        restTemplate.execute(
                urlString,
                HttpMethod.GET,
                null,
                clientHttpResponse -> {
                    if (!clientHttpResponse.getStatusCode().is2xxSuccessful()) {
                        throw new RuntimeException("Download failed with HTTP status code: " + clientHttpResponse.getStatusCode());
                    }

                    long contentLength = -1;
                    String contentLengthHeader = clientHttpResponse.getHeaders().getFirst("Content-Length");
                    if (contentLengthHeader != null) {
                        contentLength = Long.parseLong(contentLengthHeader);
                    }

                    File tempOutputFile = new File(targetFilePath + ".tmp");
                    if (tempOutputFile.getParentFile() != null) {
                        tempOutputFile.getParentFile().mkdirs();
                    }

                    long bytesRead = 0;
                    try (InputStream inputStream = clientHttpResponse.getBody();
                         FileOutputStream outputStream = new FileOutputStream(tempOutputFile)) {

                        byte[] buffer = new byte[4096];
                        int currentBytesRead;
                        while ((currentBytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, currentBytesRead);
                            bytesRead += currentBytesRead;
                        }
                    }

                    if (contentLength != -1 && bytesRead != contentLength) {
                        tempOutputFile.delete();
                        throw new RuntimeException("Download incomplete! Expected " + contentLength + " bytes but got " + bytesRead + " bytes.");
                    }

                    File finalOutputFile = new File(targetFilePath);
                    tempOutputFile.renameTo(finalOutputFile);
                    System.out.println("File downloaded and verified successfully: " + finalOutputFile.getAbsolutePath());

                    return finalOutputFile;
                });
    }
}