import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class Client {
    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;

    public Client() {
        start();
    }

    private void start() {
        try {
            client = new Socket("127.0.0.1", 8443);
            System.out.println("Client is up and running....");

            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());

            new Thread (() -> {
                try {
                    while (true) {
                        String message = in.readUTF();
                        System.out.printf("%s\nServer: %s\n", new Date(), message);
                    }
                } catch (EOFException e) {
                    System.out.println("Connection closed. Please push the ENTER");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    close();
                }

            }).start();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                if (client.isClosed()) {
                    System.out.println("EXIT! Bye!");
                    break;
                }
                out.writeUTF(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void close() {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
