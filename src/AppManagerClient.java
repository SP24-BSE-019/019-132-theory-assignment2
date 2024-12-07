import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;



public class AppManagerClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
             Scanner scanner = new Scanner(System.in)) {


            System.out.println("Connected to the server.");

            while (true) {
                System.out.println("\nChoose an option:");
                System.out.println("1 - Send a message");
                System.out.println("2 - View unseen messages");
                System.out.println("3 - Display all messages");
                System.out.println("4 - Mark a message as seen");
                System.out.println("5 - Update a message");
                System.out.println("6 - Search for a message");
                System.out.println("7 - Delete a message");
                System.out.println("8 - Exit");

                int choice = scanner.nextInt();
                scanner.nextLine();
                out.println(choice);

                if (choice == 8) {
                    System.out.println("Exiting...");
                    break;
                }

                switch (choice) {
                    case 1:
                        System.out.print("Enter your name : ");
                        String sender = scanner.nextLine();
                        out.println(sender);
                        System.out.println("Enter Receiver (1-9):\n 1. Shanzay\n 2. Ahmad\n 3. Abdullah\n 4. Javeria\n 5. Arslan\n 6. Anaya\n 7. Haroon\n 8. Asad\n 9. zarnab\n ");
                        int receiverIndex = scanner.nextInt();
                        scanner.nextLine();
                        out.println(receiverIndex);
                        System.out.print("Enter message content: ");
                        String content = scanner.nextLine();
                        out.println(content);
                        System.out.println("Message sent.");
                        break;

                    case 2://unseen
                        System.out.print("Enter receiver index (1-9): ");
                        receiverIndex = scanner.nextInt();
                        out.println(receiverIndex);
                        System.out.println("Unseen messages displayed.");
                        break;

                    case 3://display
                        System.out.print("Enter receiver index (1-9): ");
                        receiverIndex = scanner.nextInt();
                        scanner.nextLine();
                        out.println(receiverIndex);
                        System.out.println("All messages displayed.");
                        break;

                    case 4://markas seen
                        System.out.print("Enter receiver index (1-9): ");
                        receiverIndex = scanner.nextInt();
                        out.println(receiverIndex);

                        System.out.print("Enter message index: ");
                        int messageIndex = scanner.nextInt();
                        scanner.nextLine();
                        out.println(messageIndex);

                        System.out.println("Message marked as seen.");
                        break;

                    case 5://update
                        System.out.print("Enter receiver index (1-9): ");
                        receiverIndex = scanner.nextInt();
                        out.println(receiverIndex);

                        System.out.print("Enter message index: ");
                        messageIndex = scanner.nextInt();
                        scanner.nextLine();
                        out.println(messageIndex);

                        System.out.print("Enter new content: ");
                        String newContent = scanner.nextLine();
                        out.println(newContent);

                        System.out.println("Message updated.");
                        break;

                    case 6://search
                        System.out.print("Enter receiver index (1-9): ");
                        receiverIndex = scanner.nextInt();
                        scanner.nextLine();
                        out.println(receiverIndex);

                        System.out.print("Enter keyword to search: ");
                        String keyword = scanner.nextLine();
                        out.println(keyword);

                        System.out.println("Search completed.");
                        break;

                    case 7://delete
                        System.out.print("Enter receiver index (1-9): ");
                        receiverIndex = scanner.nextInt();
                        out.println(receiverIndex);

                        System.out.print("Enter message index: ");
                        messageIndex = scanner.nextInt();
                        scanner.nextLine();
                        out.println(messageIndex);

                        System.out.println("Message deleted.");
                        break;

                    default:
                        System.out.println("Invalid choice. Please select a valid option.");
                        break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error connecting to the server: " + e.getMessage());
        }
    }
}


/*import java.io.*;
import java.net.*;
import java.util.Scanner;

public class AppManagerClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";  // Server IP
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the server.");

            // Start a thread to listen for messages from the server
            new Thread(new ServerMessageListener(in)).start();

            while (true) {
                // Display client menu
                System.out.println("\nChoose an option:");
                System.out.println("1 - Send a message");
                System.out.println("2 - View unseen messages");
                System.out.println("3 - Display all messages");
                System.out.println("4 - Mark a message as seen");
                System.out.println("5 - Update a message");
                System.out.println("6 - Search for a message");
                System.out.println("7 - Delete a message");
                System.out.println("8 - Exit");

                // Get user choice
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume the newline character

                // Send the choice to the server
                out.println(choice);

                // Handle user input based on choice
                switch (choice) {
                    case 1:  // Send a message
                        System.out.println("Enter sender name:");
                        String sender = scanner.nextLine();
                        System.out.println("Enter receiver index (1-based):");
                        int receiverIndex = scanner.nextInt();
                        scanner.nextLine();  // Consume the newline
                        System.out.println("Enter message content:");
                        String content = scanner.nextLine();
                        out.println(sender);          // Send sender
                        out.println(receiverIndex);   // Send receiver index
                        out.println(content);         // Send message content
                        break;

                    case 2:  // View unseen messages
                        System.out.println("Enter your user index (1-based):");
                        int userIndex = scanner.nextInt();
                        out.println(userIndex);  // Send user index for unseen messages
                        break;

                    case 3:  // Display all messages
                        System.out.println("Enter your user index (1-based):");
                        int displayIndex = scanner.nextInt();
                        out.println(displayIndex);  // Send user index to display all messages
                        break;

                    case 4:  // Mark a message as seen
                        System.out.println("Enter your user index (1-based):");
                        int markIndex = scanner.nextInt();
                        System.out.println("Enter message index to mark as seen (1-based):");
                        int messageIndex = scanner.nextInt();
                        out.println(markIndex);   // Send user index
                        out.println(messageIndex); // Send message index to mark as seen
                        break;

                    case 5:  // Update a message
                        System.out.println("Enter your user index (1-based):");
                        int updateIndex = scanner.nextInt();
                        System.out.println("Enter message index to update (1-based):");
                        int updateMessageIndex = scanner.nextInt();
                        scanner.nextLine();  // Consume newline
                        System.out.println("Enter new message content:");
                        String newContent = scanner.nextLine();
                        out.println(updateIndex);  // Send user index
                        out.println(updateMessageIndex);  // Send message index to update
                        out.println(newContent);  // Send new content to update message
                        break;

                    case 6:  // Search for a message
                        System.out.println("Enter your user index (1-based):");
                        int searchIndex = scanner.nextInt();
                        scanner.nextLine();  // Consume newline
                        System.out.println("Enter search keyword:");
                        String keyword = scanner.nextLine();
                        out.println(searchIndex);  // Send user index
                        out.println(keyword);      // Send search keyword
                        break;

                    case 7:  // Delete a message
                        System.out.println("Enter your user index (1-based):");
                        int deleteIndex = scanner.nextInt();
                        System.out.println("Enter message index to delete (1-based):");
                        int deleteMessageIndex = scanner.nextInt();
                        out.println(deleteIndex);  // Send user index
                        out.println(deleteMessageIndex); // Send message index to delete
                        break;

                    case 8:  // Exit the application
                        System.out.println("Exiting...");
                        out.println(choice);  // Send the exit choice to the server
                        return;  // Exit the loop and close the connection

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Server message listener thread to display messages from the server
    private static class ServerMessageListener implements Runnable {
        private final BufferedReader in;

        public ServerMessageListener(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            try {
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                    System.out.println("Server: " + serverMessage);
                }
            } catch (IOException e) {
                System.out.println("Error listening to server: " + e.getMessage());
            }
        }
    }
}*/
