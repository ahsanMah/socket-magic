/**
 * Created by amahmood on 4/17/16.
 */

import java.net.*;
import java.io.*;
import java.util.*;

class subServer implements Runnable {
    Socket client;
    int client_id;

    subServer(Socket client_sock, int id) {

        client = client_sock;
        client_id = id;
    }

    private void clientHandler() throws Exception {

        InputStreamReader input = new InputStreamReader(client.getInputStream());
        BufferedReader inputReader = new BufferedReader(input);
        PrintStream outputStream = new PrintStream(client.getOutputStream());
        outputStream.println("Welcome, your user ID is " + client_id);
        int count = 0;
        String msg = "";

        try {
            while (true) {
                try {
                    msg = inputReader.readLine();
                    if (msg.equals("END")) {
                        outputStream.println("End communication");
                        System.out.println("Client " + client_id + " has left");
                        finalize();
                        return;                  //System.exit(-1);
                    } else {
                        System.out.println(client_id + ": " + msg);
                        count++;
                        outputStream.println(count + ": " + msg);
                    }
                } catch (NullPointerException e) {
                   // continue; //ignore this exception
                    //System.out.println("Null encountered");
                    System.out.println("Client " + client_id + " has left");
                    finalize();
                    return;
                    //drop client here
                }

            }
        } catch (IOException e) {
            System.out.println("Could not read from client");
            return;
        }
    }

    public void run() {
        try {
            clientHandler();

        } catch (Exception e) {
            System.out.println("Connection ended");
            return;
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

    private void run() throws Exception {

        server = new ServerSocket(48766);
        Random rand = new Random();
        int client_id = 0;

        while (true) {
            try {
                Socket client_socket = server.accept();
                client_id = rand.nextInt(10000);
                subServer client = new subServer(client_socket, client_id);
                Thread connection = new Thread(client);
                connection.start();
                System.out.println("Client " + client_id + " has joined the server!");
            } catch (IOException e) {
                System.out.println("Could not accept socket connection");
                continue;   //System.exit(-1);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Server server_socket = new Server();
        server_socket.run();
    }
}