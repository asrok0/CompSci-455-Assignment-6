import java.io.*;
import java.net.*;
import java.util.Scanner;

// Cryptography Stuff
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.Base64;

public class Server {

    public static void main(String[] args) {
        String ip = "127.0.0.1";    // Local IP
        int port = 11000;           // Open Port

        try {
            Scanner scanner = new Scanner(System.in);

            // Get Key and IV
            System.out.print("Enter 16-byte key: ");
            String keyStr = scanner.nextLine();

            System.out.print("Enter 16-byte IV: ");
            String ivStr = scanner.nextLine();

            SecretKey key = new SecretKeySpec(keyStr.getBytes(), "AES");
            IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());

            // Start initiating connection/running server
            ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(ip));
            System.out.println("Server is running...");

            // Server runs until interrupted or there's an error
            while (true) {
                System.out.println("Listening...");

                Socket handler = serverSocket.accept();

                InputStream in = handler.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String encryptedReq = reader.readLine();
                String request = decrypt(encryptedReq, key, iv);    // Decrypt after receiving
                System.out.println("Received: " + encryptedReq);
                System.out.println("Received (decrypted): " + request);

                String response = request.toUpperCase();
                OutputStream out = handler.getOutputStream();
                PrintWriter writer = new PrintWriter(out, true);
                String encryptedResp = encrypt(response, key, iv);  // Encrypt before sending
                writer.println(encryptedResp);
                System.out.println("Sending: " + response);
                System.out.println("Sending (encrypted): " + encryptedResp);

                handler.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Encryption Helper
    public static String encrypt(String plaintext, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }


    // Decryption Helper
    public static String decrypt(String ciphertext, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decoded = Base64.getDecoder().decode(ciphertext);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }

}