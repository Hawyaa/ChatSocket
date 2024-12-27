import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private Server server;
    private String clientName;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            // Set up input and output streams
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            // Get the client's name
            clientName = input.readLine();
            System.out.println("Client name: " + clientName);

            // Send a welcome message to the client
            output.println("Welcome " + clientName + " to the chat server!");

            String clientMessage;
            while ((clientMessage = input.readLine()) != null) {
                System.out.println(clientName + " says: " + clientMessage);

                // Broadcast the message to all other clients
                server.broadcastMessage(clientName + ": " + clientMessage, this);

                // Close connection if client sends "bye"
                if (clientMessage.equalsIgnoreCase("bye")) {
                    System.out.println(clientName + " disconnected.");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Clean up when done
                if (input != null) input.close();
                if (output != null) output.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Send a message to the client
    public void sendMessage(String message) {
        if (output != null) {
            output.println(message);
        }
    }
}
