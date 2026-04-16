import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 11000;

        try {
            Socket socket = new Socket(ip, port);

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter message: ");
            String message1 = scanner.nextLine();

            System.out.println("Sending: " + message1);

            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);
            writer.println(message1);

            System.out.println("Waiting for response...");

            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String message2 = reader.readLine();
            System.out.println("Received: " + message2);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}