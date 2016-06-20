import java.io.*;
import java.net.*;

public class Client {

    public static void main (String args[]) throws Exception{

        Client client = new Client();
        //Client client2 = new Client();
        client.run("foo");
        //client2.run("bar");
    }

    public void run(String msg) throws Exception{

        Socket cSocket = new Socket("localhost", 48766);
        PrintStream pStream = new PrintStream(cSocket.getOutputStream());
        InputStreamReader iReader = new InputStreamReader(cSocket.getInputStream());
        BufferedReader bReader = new BufferedReader(iReader);

        pStream.println(msg);
        System.out.println(bReader.readLine());
        pStream.println(msg+": seconded");
        System.out.println(bReader.readLine());
        pStream.println(msg+": garbage");
        System.out.println(bReader.readLine());
//        pStream.println("END");
//        System.out.println(bReader.readLine());
//        System.out.println(bReader.readLine());
    }


}
