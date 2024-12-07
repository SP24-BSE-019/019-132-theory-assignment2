import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static java.lang.System.in;
import java.time.LocalDateTime;

class Message {
    private String sender;
    private String receiver;
    private String content;
    private LocalDateTime sendingTime;
    private LocalDateTime seenTime;
    private boolean seen;

    public Message(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.sendingTime = LocalDateTime.now();
        this.seen = false;
    }

    public String getSender() {
        return sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void markAsSeen() {
        this.seen=true;
        this.seenTime=LocalDateTime.now();
    }

    public boolean isSeen() {
        return seen;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setContent(String newContent) {
        this.content =newContent;
    }


    public String toString() {
        return "Sender: " + sender + "\nReceiver: " + receiver +
                "\nContent: " + content + "\nSent: " + sendingTime +
                (seen ? "\nSeen at: " + seenTime : "\nNot yet seen");
    }
}

public class AppManagerServer {
    private static final int PORT = 1234;
    private Message[][] messages;
    private final int[] messageCounts;
    private static final int MAX_MESSAGES = 10;

    public AppManagerServer() {
        messages = new Message[10][MAX_MESSAGES];
        messageCounts = new int[10];
    }

    public void receiveMessages() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Messaging App: Server is waiting for clients...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    System.out.println("Connected to a client...");

                    while (true) {
                        String choiceLine = in.readLine();
                        if (choiceLine == null) {
                            break;
                        }

                        int choice = Integer.parseInt(choiceLine);
                        switch (choice) {
                            case 1:
                                receiveMessage(in);
                                out.println("Message received successfully.");
                                break;
                            case 2:
                                viewUnseenMessages(in);
                                out.println("Unseen messages displayed.");
                                break;
                            case 3:
                                displayAllMessages(in);
                                out.println("All messages displayed.");
                                break;
                            case 4:
                                markMessageAsSeen(in);
                                out.println("Message marked as seen.");
                                break;
                            case 5:
                                updateMessage(in);
                                out.println("Message updated.");
                                break;
                            case 6:
                                searchMessage(in);
                                out.println("Search completed.");
                                break;
                            case 7:
                                deleteMessage(in);
                                out.println("Message deleted.");
                                break;
                            default:
                                System.out.println("Invalid choice received");
                                out.println("Invalid choice.");
                                break;
                        }
                        out.flush();
                    }

                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }



    private void receiveMessage(BufferedReader in) throws IOException {
        String sender = in.readLine();
        int receiverIndex = Integer.parseInt(in.readLine());
        String content = in.readLine();

        if (messageCounts[receiverIndex] < MAX_MESSAGES) {
            messages[receiverIndex][messageCounts[receiverIndex]] = new Message(sender, "Receiver_" + receiverIndex, content);
            messageCounts[receiverIndex]++;
            System.out.println("message received from: " + sender + "\nto receiver:" + receiverIndex+ "\nmessage content:"+ content);
        } else {
            System.out.println("Receiver's message box is full.");
        }
    }

    public void viewUnseenMessages(BufferedReader in) throws IOException {
        int receiverIndex = Integer.parseInt(in.readLine());
        System.out.println("Unseen messages for Receiver_" + receiverIndex + ":");
        for (int i = 0; i < messageCounts[receiverIndex]; i++) {
            if (!messages[receiverIndex][i].isSeen()) {
                System.out.println(messages[receiverIndex][i]);
            }
        }
    }

    public void displayAllMessages(BufferedReader in) throws IOException {
        int receiverIndex = Integer.parseInt(in.readLine());
        System.out.println("All messages for Receiver_" + receiverIndex + ":");
        for (int i = 0; i < messageCounts[receiverIndex]; i++) {
            System.out.println(messages[receiverIndex][i]);
        }
    }

    public void markMessageAsSeen(BufferedReader in) throws IOException {
        int receiverIndex = Integer.parseInt(in.readLine());
        int messageIndex = Integer.parseInt(in.readLine());
        if (receiverIndex < 10 && messageIndex < messageCounts[receiverIndex]) {
            messages[receiverIndex][messageIndex].markAsSeen();
            System.out.println("Message marked as seen.");
        } else {
            System.out.println("Invalid message index.");
        }
    }

    public void updateMessage(BufferedReader in) throws IOException {
        int receiverIndex = Integer.parseInt(in.readLine());
        int messageIndex = Integer.parseInt(in.readLine());
        String newContent = in.readLine();
        if (receiverIndex < 10 && messageIndex < messageCounts[receiverIndex]) {
            messages[receiverIndex][messageIndex].setContent(newContent);
            System.out.println("Message updated.");
        } else {
            System.out.println("Invalid message index.");
        }
    }

    public void searchMessage(BufferedReader in) throws IOException {
        int receiverIndex = Integer.parseInt(in.readLine());
        String keyword = in.readLine();
        boolean found = false;
        for (int i = 0; i < messageCounts[receiverIndex]; i++) {
            if (messages[receiverIndex][i].getContent().contains(keyword)) {
                System.out.println("Message found: " + messages[receiverIndex][i]);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No messages found with the keyword: " + keyword);
        }
    }

    public void deleteMessage(BufferedReader in) throws IOException {
        int receiverIndex = Integer.parseInt(in.readLine());
        int messageIndex = Integer.parseInt(in.readLine());
        if (receiverIndex < 10 && messageIndex < messageCounts[receiverIndex]) {
            for (int i = messageIndex; i < messageCounts[receiverIndex] - 1; i++) {
                messages[receiverIndex][i] = messages[receiverIndex][i + 1];
            }
            messages[receiverIndex][--messageCounts[receiverIndex]] = null;
            System.out.println("Message deleted.");
        } else {
            System.out.println("Invalid message index.");
        }
    }

    public static void main(String[] args) {
        AppManagerServer server = new AppManagerServer();
        server.receiveMessages();
    }
}

/*import java.io.*;
import java.net.*;

public class AppManagerServer {
    private static final int PORT = 1234;
    private static final int MAX_MESSAGES = 10;
    private Message[][] messages;
    private final int[] messageCounts;

    // Constructor to initialize server-side message storage
    public AppManagerServer() {
        messages = new Message[10][MAX_MESSAGES];  // 10 users, each can have 10 messages
        messageCounts = new int[10];  // Track the number of messages for each user
    }

    public void receiveMessages() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is waiting for clients...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    System.out.println("Connected to a client...");

                    // Handle client communication
                    new Thread(new ClientHandler(clientSocket, in, out)).start();
                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    // ClientHandler to handle each connected client
    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket clientSocket, BufferedReader in, PrintWriter out) {
            this.clientSocket = clientSocket;
            this.in = in;
            this.out = out;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String choiceLine = in.readLine();
                    if (choiceLine == null) {
                        break;
                    }

                    int choice = Integer.parseInt(choiceLine);
                    switch (choice) {
                        case 1:
                            receiveMessage(in);
                            out.println("Message received successfully.");
                            break;
                        case 2:
                            viewUnseenMessages(in);
                            break;
                        case 3:
                            displayAllMessages(in);
                            break;
                        case 4:
                            markMessageAsSeen(in);
                            break;
                        case 5:
                            updateMessage(in);
                            break;
                        case 6:
                            searchMessage(in);
                            break;
                        case 7:
                            deleteMessage(in);
                            break;
                        default:
                            out.println("Invalid choice.");
                            break;
                    }
                    out.println("Server: Type your next action...");
                }

            } catch (IOException e) {
                System.out.println("Error in communication: " + e.getMessage());
            }
        }

        // Function to receive a message
        private void receiveMessage(BufferedReader in) throws IOException {
            String sender = in.readLine();
            int receiverIndex = Integer.parseInt(in.readLine()) - 1;  // Convert to 0-based index
            String content = in.readLine();

            if (messageCounts[receiverIndex] < MAX_MESSAGES) {
                messages[receiverIndex][messageCounts[receiverIndex]++] = new Message(sender, content, false);
            } else {
                out.println("Error: Message storage is full.");
            }
        }

        // Function to view unseen messages
        private void viewUnseenMessages(BufferedReader in) throws IOException {
            int receiverIndex = Integer.parseInt(in.readLine()) - 1;

            out.println("Unseen Messages for User " + (receiverIndex + 1) + ":");
            for (int i = 0; i < messageCounts[receiverIndex]; i++) {
                if (!messages[receiverIndex][i].isSeen()) {
                    out.println("Message: " + messages[receiverIndex][i].getContent());
                }
            }
        }

        // Function to display all messages
        private void displayAllMessages(BufferedReader in) throws IOException {
            int receiverIndex = Integer.parseInt(in.readLine()) - 1;

            out.println("All Messages for User " + (receiverIndex + 1) + ":");
            for (int i = 0; i < messageCounts[receiverIndex]; i++) {
                out.println("Message: " + messages[receiverIndex][i].getContent() + " (Seen: " + messages[receiverIndex][i].isSeen() + ")");
            }
        }

        // Function to mark a message as seen
        private void markMessageAsSeen(BufferedReader in) throws IOException {
            int receiverIndex = Integer.parseInt(in.readLine()) - 1;
            int messageIndex = Integer.parseInt(in.readLine()) - 1;

            if (messageIndex >= 0 && messageIndex < messageCounts[receiverIndex]) {
                messages[receiverIndex][messageIndex].setSeen(true);
                out.println("Message marked as seen.");
            } else {
                out.println("Invalid message index.");
            }
        }

        // Function to update a message
        private void updateMessage(BufferedReader in) throws IOException {
            int receiverIndex = Integer.parseInt(in.readLine()) - 1;
            int messageIndex = Integer.parseInt(in.readLine()) - 1;
            String newContent = in.readLine();

            if (messageIndex >= 0 && messageIndex < messageCounts[receiverIndex]) {
                messages[receiverIndex][messageIndex].setContent(newContent);
                out.println("Message updated.");
            } else {
                out.println("Invalid message index.");
            }
        }

        // Function to search a message
        private void searchMessage(BufferedReader in) throws IOException {
            int receiverIndex = Integer.parseInt(in.readLine()) - 1;
            String searchKeyword = in.readLine();

            out.println("Search Results:");
            for (int i = 0; i < messageCounts[receiverIndex]; i++) {
                if (messages[receiverIndex][i].getContent().contains(searchKeyword)) {
                    out.println("Message: " + messages[receiverIndex][i].getContent());
                }
            }
        }

        // Function to delete a message
        private void deleteMessage(BufferedReader in) throws IOException {
            int receiverIndex = Integer.parseInt(in.readLine()) - 1;
            int messageIndex = Integer.parseInt(in.readLine()) - 1;

            if (messageIndex >= 0 && messageIndex < messageCounts[receiverIndex]) {
                messages[receiverIndex][messageIndex] = null;
                for (int i = messageIndex; i < messageCounts[receiverIndex] - 1; i++) {
                    messages[receiverIndex][i] = messages[receiverIndex][i + 1];
                }
                messageCounts[receiverIndex]--;
                out.println("Message deleted.");
            } else {
                out.println("Invalid message index.");
            }
        }
    }

    // Message class to represent a message
    public static class Message {
        private String sender;
        private String content;
        private boolean seen;

        public Message(String sender, String content, boolean seen) {
            this.sender = sender;
            this.content = content;
            this.seen = seen;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isSeen() {
            return seen;
        }

        public void setSeen(boolean seen) {
            this.seen = seen;
        }

        @Override
        public String toString() {
            return "From: " + sender + "\nContent: " + content + "\nSeen: " + seen;
        }
    }

    public static void main(String[] args) {
        AppManagerServer server = new AppManagerServer();
        server.receiveMessages();
    }
}*/
