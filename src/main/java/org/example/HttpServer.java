package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.*;

public class HttpServer {
    public static void main(String[] args) {
        int port=8080;
        try{
            ServerSocket serverSocket=new ServerSocket(port);
            BlockingQueue<Socket> queue = new ArrayBlockingQueue<>(100);
            RequestProcessor RequestProcessor=new RequestProcessor(queue);
            new Thread(()->{
                RequestProcessor.start();
            }).start();
            System.out.println("Server is started listing on port %s".formatted(port));
            while (true) {
                queue.put(serverSocket.accept());
                System.out.println("Server has %s total request to process".formatted(queue.size()));
            }
        }catch (IOException ioException){
               ioException.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);



        }
    }

}

class RequestProcessor {
    private BlockingQueue<Socket> queue;
    private ExecutorService worker = Executors.newFixedThreadPool(10);
    private RequestHandler requestHandler=new RequestHandler();
    public RequestProcessor(BlockingQueue<Socket> queue){
        this.queue=queue;
    }
    public void start(){
        while (true) {
            Socket clientSocket = queue.poll();
            if(!Objects.isNull(clientSocket))
                 worker.submit(() -> requestHandler.processRequest(clientSocket));
        }
    }

}