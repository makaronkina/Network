import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class Server {

    private ServerSocket server;
    private DataInputStream in;
    private DataOutputStream out;

    public Server() {
        start();
    }

    private void start() {
        try {
            server = new ServerSocket(8443);
            System.out.println("Server up and run....");
            System.out.println("Server info: " + server);

            System.out.println("Server is waiting for a connection....");
            Socket client = server.accept();
            System.out.println("Client accepted: " + client);

            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());

            new Thread(() -> {
                try {
                    while (true) {
                        String message = in.readUTF();
                        if (message.startsWith("-exit")) {
                            out.writeUTF("cmd EXIT: System shutdown");
                            break;
                        }
                        System.out.printf("%s\n%s: %s\n", new Date(), client.getInetAddress(), message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    close();
                }
            }).start();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                if (client.isClosed()) {
                    System.out.println("Client is failed");
                    break;
                }
                out.writeUTF(message);
            }
        } catch (IOException e) {
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
