package org.example;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class HttpServer {
    public static void main(String[] args) {
        int port=8080;
        try{
            ServerSocket serverSocket=new ServerSocket(port);
            System.out.println("Server is started listing on port %s".formatted(port));
            while (1==1){
                Socket clinetSocket=serverSocket.accept();
                System.out.println("Accepted connection from %s".formatted(clinetSocket.getInetAddress()));
                handleRequest(clinetSocket);
                clinetSocket.close();
            }
        }catch (IOException ioException){
               ioException.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private static void handleRequest(Socket socket) throws IOException, InterruptedException {
        try (OutputStream outputStream = socket.getOutputStream()) {
            StringBuilder response = new StringBuilder();
            String responseBody = "Hello, Romit";
            addHeader(response, responseBody.length());
            response.append(responseBody);
            TimeUnit.SECONDS.sleep(10);
            outputStream.write(response.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }
    }

    private static void addHeader(StringBuilder sb, int contentLength) {
        sb.append("HTTP/1.1 200 OK\n")
                .append("Content-Type: text/plain\n")
                .append("Content-Length: ").append(contentLength).append("\n")
                .append("\n");
    }

}
