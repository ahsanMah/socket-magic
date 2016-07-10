import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    public static final Scanner input = new Scanner(System.in);

    public static void main(String args[]) throws Exception {

        Client client = new Client();
        //Client client2 = new Client();
        client.run("foo");
        //client2.run("bar");
    }

    public void run(String msg) throws Exception {
        String response;
        Socket cSocket = new Socket("localhost", 48766);
        PrintStream pStream = new PrintStream(cSocket.getOutputStream());
        InputStreamReader iReader = new InputStreamReader(cSocket.getInputStream());
        BufferedReader bReader = new BufferedReader(iReader);
        System.out.println(bReader.readLine());
        response = msg;

        while (!response.equals("END")) {
            System.out.println("Enter message: ");
            response = input.nextLine();
            pStream.println(response);
            System.out.println(bReader.readLine());
        }

        cSocket.close();

    }
}
