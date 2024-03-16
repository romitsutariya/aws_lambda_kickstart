package org.example;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class RequestHandler {
    public  void processRequest(Socket socket){
        try (OutputStream outputStream = socket.getOutputStream()) {
            StringBuilder response = new StringBuilder();
            String responseBody = "Hello, Romit";
            addHeader(response, responseBody.length());
            response.append(responseBody);
            outputStream.write(response.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addHeader(StringBuilder sb, int contentLength) {
        sb.append("HTTP/1.1 200 OK\n")
                .append("Content-Type: text/plain\n")
                .append("Content-Length: ").append(contentLength).append("\n")
                .append("\n");
    }
}