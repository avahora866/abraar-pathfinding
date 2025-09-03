package com.abraar.pathfinding.service.geographicalData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PBFFileHandler {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${file}")
    private String filePath;

    public void downloadFile(String city)
    {
        String urlString = "https://download.geofabrik.de/europe/united-kingdom/england/"+city+"-latest.osm.pbf";
        restTemplate.execute(
                urlString,
                HttpMethod.GET,
                null,
                clientHttpResponse -> {
                    // --- STEP 1: Check the Status Code ---
                    if (!clientHttpResponse.getStatusCode().is2xxSuccessful()) {
                        throw new RuntimeException("Download failed with HTTP status code: " + clientHttpResponse.getStatusCode());
                    }

                    // --- STEP 2: Get Content-Length for Verification ---
                    long contentLength = -1;
                    String contentLengthHeader = clientHttpResponse.getHeaders().getFirst("Content-Length");
                    if (contentLengthHeader != null) {
                        contentLength = Long.parseLong(contentLengthHeader);
                    }

                    // --- STEP 3: Use Temporary File Logic ---
                    File tempOutputFile = new File(filePath + ".tmp");
                    tempOutputFile.getParentFile().mkdirs();

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

                    // --- STEP 4: Verify the Download and Rename the File ---
                    if (contentLength != -1 && bytesRead != contentLength) {
                        tempOutputFile.delete(); // Delete incomplete file
                        throw new RuntimeException("Download incomplete! Expected " + contentLength + " bytes but got " + bytesRead + " bytes.");
                    }

                    File finalOutputFile = new File(filePath);
                    tempOutputFile.renameTo(finalOutputFile);
                    System.out.println("File downloaded and verified successfully: " + finalOutputFile.getAbsolutePath());

                    return finalOutputFile;
                });

    }

    public void removeFile()
    {
        Path filePath = Paths.get(this.filePath);

        try {
            Files.delete(filePath);
            System.out.println("File deleted successfully.");
        } catch (IOException e) {
            System.err.println("Failed to delete the file: " + e.getMessage());
        }
    }
}
