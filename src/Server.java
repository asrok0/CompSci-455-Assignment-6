import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 11000;

        try {
            ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(ip));
            System.out.println("Server is running...");

            while (true) {
                System.out.println("Listening...");

                Socket handler = serverSocket.accept();

                InputStream in = handler.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String request = reader.readLine();
                System.out.println("Received: " + request);

                String response = request.toUpperCase();
                System.out.println("Sending: " + response);

                OutputStream out = handler.getOutputStream();
                PrintWriter writer = new PrintWriter(out, true);
                writer.println(response);

                handler.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}