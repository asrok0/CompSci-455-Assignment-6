import java.io.*;
import java.net.*;
import java.util.Scanner;

// Cryptography Stuff
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.Base64;

public class Client {

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

            // Start initiating connection/running client
            Socket socket = new Socket(ip, port);

            System.out.print("Enter message: ");
            String message1 = scanner.nextLine();

            System.out.println("Sending: " + message1);

            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);
            String encrypted = encrypt(message1, key, iv);  // Ecnrypt before sending
            writer.println(encrypted);

            System.out.println("Waiting for response...");

            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String encryptedResp = reader.readLine();
            String decrypted = decrypt(encryptedResp, key, iv); // Decrypt after receiving
            System.out.println("Received: " + decrypted);

            socket.close();
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