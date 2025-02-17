import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            // Prompt the user to enter their name
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            // Send the client's name to the server
            output.println(name);

            // Receive the welcome message from the server
            String serverMessage = input.readLine();
            System.out.println("Server: " + serverMessage);

            String userMessage;
            while (true) {
                System.out.print("You: ");
                userMessage = scanner.nextLine();

                // Send message to server
                output.println(userMessage);

                // Receive the response from server (broadcasted message)
                String serverResponse = input.readLine();
                System.out.println(serverResponse);

                // Exit if the user types "bye"
                if (userMessage.equalsIgnoreCase("bye")) {
                    System.out.println("Exiting...");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
