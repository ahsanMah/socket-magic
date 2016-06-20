/**
 * Created by amahmood on 4/17/16.
 */

import java.net.*;
import java.io.*;

class subServer implements Runnable {
    Socket client;

    subServer(Socket client) {
        this.client = client;
    }

    private void clientHandler() throws Exception {

        InputStreamReader input = new InputStreamReader(client.getInputStream());
        BufferedReader inputReader = new BufferedReader(input);
        PrintStream outputStream = new PrintStream(client.getOutputStream());
        outputStream.println("Connection established");
        int count = 0;
        String msg = "";

        try {
            while (true) {
                try {
                    msg = inputReader.readLine();
                    if (msg.equals("END")) {
                        outputStream.println("End communication");
                        System.out.println("Client has left");
                        System.exit(-1);
                    } else {
                        System.out.println("Client: " + msg);
                        count++;
                        outputStream.println(count + ": " + msg);
                    }
                } catch (NullPointerException e) {
                    continue;
                }

            }
        } catch (IOException e) {
            System.out.println("Could not read from client");
        }
    }

    public void run() {
        try {
            clientHandler();
        } catch (Exception e) {
            System.out.println("Connection ended");
        }
    }

    protected void finalize(){
        //Objects created in run method are finalized when
        //program terminates and thread exits
        try{
            System.out.println("Connection closed successfully");
            client.close();

        } catch (IOException e) {
            System.out.println("Could not close socket");
            System.exit(-1);
        }
    }

}


public class Server {
    ServerSocket server;

    private void clientHandler(ServerSocket server_sock) throws Exception {

        Socket sock = server_sock.accept();
        InputStreamReader input = new InputStreamReader(sock.getInputStream());
        BufferedReader inputReader = new BufferedReader(input);
        PrintStream outputStream = new PrintStream(sock.getOutputStream());
        outputStream.println("Communication established");
        int count = 0;
        String msg = "";
        try {
            while (!msg.equals("END")) {
                msg = inputReader.readLine();
                if (msg.equals("END")) {
                    outputStream.println("End communication");
                } else {
                    System.out.println("Client: " + msg);
                    count++;
                    outputStream.println(count + ": " + msg);
                }
            }
        } catch (NullPointerException e) {
            System.out.println("NULL entered");
            System.exit(-1);
        }
    }


        protected void finalize(){
    //Objects created in run method are finalized when
    //program terminates and thread exits
        try{
            server.close();
            System.out.println("Connection closed successfully");
        } catch (IOException e) {
            System.out.println("Could not close socket");
            System.exit(-1);
        }
    }

    private void run() throws Exception {

        server = new ServerSocket(48766);
        try {
            while (true) {
                subServer client = new subServer(server.accept());
                Thread connection = new Thread(client);
                connection.start();
            }
        } catch (IOException e) {
            System.out.println("Could not accept socket connection");
            System.exit(-1);
        }
    }

    public static void main(String[] args) throws Exception {
        Server server_socket = new Server();
        server_socket.run();
    }
}