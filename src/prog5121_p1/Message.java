// Commit 2
// Commit 3
// Commit 4
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prog5121_p1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Message {

    // ===========================================
    // VARIABLES
    // ===========================================

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;
    private String messageStatus;

    // Stores messages sent during the program session
    private static ArrayList<Message> sentMessages = new ArrayList<>();

    // Stores messages disregarded by the user
    private static ArrayList<Message> disregardedMessages = new ArrayList<>();

    // Stores messages selected for sending later
    private static ArrayList<Message> storedMessages = new ArrayList<>();

    // Total number of messages successfully sent
    private static int totalMessages = 0;

    // Keeps message numbering consistent across sending sessions
    private static int nextMessageNumber = 0;

    // ===========================================
    // CONSTRUCTOR
    // ===========================================

    public Message(String messageID, int messageNumber,
                   String recipient, String message) {

        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.messageStatus = "Created";

        this.messageHash = createMessageHash();
    }

    // ===========================================
    // GETTERS
    // ===========================================

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    // ===========================================
    // START QUICKCHAT
    // ===========================================

    public static void startQuickChat(Scanner input) {

        StoredMessages storedManager = new StoredMessages();

        startQuickChat(input, storedManager);
    }

    // Overloaded method that includes Part 3 stored-message management
    public static void startQuickChat(Scanner input,
                                      StoredMessages storedManager) {

        System.out.println("\nWelcome to QuickChat.");

        // prompts the user for how many messages they want to add
        int numberOfMessages = 0;

        do {
            System.out.print("How many messages would you like to enter? ");

            while (!input.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                input.next();
            }

            numberOfMessages = input.nextInt();

            if (numberOfMessages <= 0) {
                System.out.println("Please enter a number greater than zero.");
            }

        } while (numberOfMessages <= 0);

        input.nextLine();

        boolean running = true;

        while (running) {

            System.out.println("\n=====================================");
            System.out.println("          QUICKCHAT Options:");
            System.out.println("=====================================");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.println("4. Stored Messages");
            System.out.print("Choose an option: ");

            String option = input.nextLine().trim();

            switch (option) {

                case "1":
                    sendMessages(input, numberOfMessages, storedManager);
                    break;

                case "2":
                    System.out.println("\nRecently sent messages:");

                    String messages = printMessages();

                    if (messages.isEmpty()) {
                        System.out.println("No messages have been sent yet.");
                    } else {
                        System.out.println(messages);
                    }
                    break;

                case "3":
                    running = false;
                    System.out.println("\nThank you for using QuickChat.");
                    break;

                case "4":
                    storedManager.showStoredMessagesMenu(input);
                    break;

                default:
                    System.out.println(
                            "\nInvalid option. Please enter 1, 2, 3 or 4."
                    );
            }
        }
    }

    // ===========================================
    // SEND MESSAGES
    // ===========================================

    // Keeps the original method available for existing Part 2 tests
    public static void sendMessages(Scanner input, int numberOfMessages) {

        sendMessages(input, numberOfMessages, new StoredMessages());
    }

    // Part 3 version that also updates the StoredMessages manager
    public static void sendMessages(Scanner input, int numberOfMessages,
                                    StoredMessages storedManager) {

        for (int i = 0; i < numberOfMessages; i++) {

            // The counter does not restart when another batch is sent
            int currentMessageNumber = nextMessageNumber++;

            System.out.println(
                    "\nMESSAGE " + (currentMessageNumber + 1)
            );

            // ===========================================
            // GENERATE MESSAGE ID
            // ===========================================

            String messageID = generateMessageID();

            System.out.println("Message ID generated: " + messageID);

            // ===========================================
            // RECIPIENT
            // ===========================================

            String recipient;

            while (true) {

                System.out.print("Enter recipient cell number: ");
                recipient = input.nextLine().trim();

                Message temporaryMessage = new Message(
                        messageID,
                        currentMessageNumber,
                        recipient,
                        " "
                );

                String cellResult = temporaryMessage.checkRecipientCell();

                System.out.println(cellResult);

                if (cellResult.equals(
                        "Cell phone number successfully captured.")) {
                    break;
                }
            }

            // ===========================================
            // MESSAGE TEXT
            // ===========================================

            String messageText;

            while (true) {

                System.out.print("Enter your message: ");
                messageText = input.nextLine();

                String lengthResult = checkMessageLength(messageText);

                System.out.println(lengthResult);

                if (lengthResult.equals("Message ready to send.")) {
                    break;
                }

                System.out.println(
                        "Please enter a message of 250 characters or fewer."
                );
            }

            // ===========================================
            // CREATE MESSAGE
            // ===========================================

            Message newMessage = new Message(
                    messageID,
                    currentMessageNumber,
                    recipient,
                    messageText
            );

            System.out.println(
                    "Message Hash: " + newMessage.getMessageHash()
            );

            // ===========================================
            // SEND, DISREGARD OR STORE
            // ===========================================

            /*
             * The second argument is false because Part 3's
             * StoredMessages class manages JSON storage
             * The original SentMessage(Scanner) method remains
             * available for existing Part 2 tests
             */
            String result = newMessage.SentMessage(input, false);

            System.out.println("\n" + result);

            // ===========================================
            // PART 3: RETAIN DISREGARDED MESSAGES
            // ===========================================

            // Part 3 requires disregarded messages to remain
            // available in the disregarded messages array
            // They are therefore not automatically deleted
            boolean messageDeleted = false;

            if (newMessage.messageStatus.equals("Disregarded")) {

                String deleteChoice = "";

                while (!deleteChoice.equals("0")) {

                    System.out.print(
                            "Enter 0 to delete this disregarded message: "
                    );

                    deleteChoice = input.nextLine().trim();

                    if (!deleteChoice.equals("0")) {
                        System.out.println("Invalid choice. Enter 0.");
                    }
                }

                disregardedMessages.remove(newMessage);

                messageDeleted = true;

                System.out.println("Message deleted.");
            }

            // ===========================================
            // UPDATE PART 3 STORED MESSAGE MANAGER
            // ===========================================

            /*
             * Messages that the user deletes are not added
             * to the Part 3 manager
             */
            if (!messageDeleted && storedManager != null) {

                storedManager.addMessage(
                        newMessage.getMessageID(),
                        newMessage.getMessageHash(),
                        newMessage.getRecipient(),
                        newMessage.getMessage(),
                        newMessage.getMessageStatus(),
                        "Developer"
                );
            }

            // ===========================================
            // DISPLAY SENT MESSAGE DETAILS
            // ===========================================

            if (newMessage.messageStatus.equals("Sent")) {

                System.out.println("\n=====================================");
                System.out.println("Message ID: " + newMessage.messageID);
                System.out.println("Message Hash: " + newMessage.messageHash);
                System.out.println("Recipient: " + newMessage.recipient);
                System.out.println("Message: " + newMessage.message);
                System.out.println("=====================================");
            }
        }

        System.out.println(
                "\nTotal messages sent: " + returnTotalMessages()
        );
    }

    // ===========================================
    // GENERATE MESSAGE ID
    // ===========================================

    public static String generateMessageID() {

        Random random = new Random();

        StringBuilder id = new StringBuilder();

        for (int digit = 0; digit < 10; digit++) {
            id.append(random.nextInt(10));
        }

        return id.toString();
    }

    // ===========================================
    // CHECK MESSAGE ID
    // ===========================================

    public boolean checkMessageID() {

        return messageID != null && messageID.length() <= 10;
    }

    // ===========================================
    // CHECK RECIPIENT CELL
    // ===========================================

    public String checkRecipientCell() {

        Login login = new Login();

        if (recipient != null && login.checkCellPhoneNumber(recipient)) {

            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not "
                + "contain an international code. Please correct the number and try again.";
    }

    // ===========================================
    // CHECK MESSAGE LENGTH
    // ===========================================

    public static String checkMessageLength(String text) {

        if (text == null) {
            return "Message cannot be empty.";
        }

        if (text.length() <= 250) {
            return "Message ready to send.";
        }

        int excess = text.length() - 250;

        return "Message exceeds 250 characters by " + excess
                + "; please reduce the size.";
    }

    // ===========================================
    // CREATE MESSAGE HASH
    // ===========================================

    public String createMessageHash() {

        String firstTwoNumbers = "";

        if (messageID != null && messageID.length() >= 2) {
            firstTwoNumbers = messageID.substring(0, 2);
        } else if (messageID != null) {
            firstTwoNumbers = messageID;
        }

        String firstWord = "";
        String lastWord = "";

        if (message != null && !message.trim().isEmpty()) {

            String cleanMessage = message.trim();

            String[] words = cleanMessage.split("\\s+");

            firstWord = words[0];
            lastWord = words[words.length - 1];

            firstWord = firstWord.replaceAll("[^a-zA-Z0-9]", "");
            lastWord = lastWord.replaceAll("[^a-zA-Z0-9]", "");
        }

        return (
                firstTwoNumbers
                + ":"
                + messageNumber
                + ":"
                + firstWord
                + lastWord
        ).toUpperCase();
    }

    // ===========================================
    // SEND / DISREGARD / STORE
    // ===========================================

    // Original method retained for Part 2 compatibility
    public String SentMessage(Scanner input) {

        return SentMessage(input, true);
    }

    /*
     * Internal overloaded method
     * When saveToLegacyJson is false, Part 3's StoredMessages
     * class is responsible for saving stored messages
     */
    private String SentMessage(Scanner input, boolean saveToLegacyJson) {

        while (true) {

            System.out.println("\nChoose what to do with this message:");
            System.out.println("1. Send Message");
            System.out.println("2. Disregard Message");
            System.out.println("3. Store Message to send later");
            System.out.print("Choose an option: ");

            String choice = input.nextLine().trim();

            switch (choice) {

                case "1":

                    messageStatus = "Sent";

                    sentMessages.add(this);

                    totalMessages++;

                    return "Message successfully sent.";

                case "2":

                    messageStatus = "Disregarded";

                    disregardedMessages.add(this);
                    
                    // Preserve the original Part 2 behaviour
                    if (saveToLegacyJson) {
                    return "Press 0 to delete the message.";
                    
                     }

                     // Part 3 keeps the message in the disregarded array
                    return "Message successfully disregarded.";

                case "3":

                    messageStatus = "Stored";

                    storedMessages.add(this);

                    if (saveToLegacyJson) {

                        if (storeMessage()) {
                            return "Message successfully stored.";
                        }

                        return "Message could not be stored.";
                    }

                    return "Message successfully stored.";

                default:

                    System.out.println(
                            "Invalid option. Please enter 1, 2 or 3."
                    );
            }
        }
    }

    // ===========================================
    // PRINT ALL SENT MESSAGES
    // ===========================================

    public static String printMessages() {

        StringBuilder allMessages = new StringBuilder();

        for (Message sent : sentMessages) {

            allMessages.append("Message ID: ")
                    .append(sent.messageID)
                    .append("\n");

            allMessages.append("Message Hash: ")
                    .append(sent.messageHash)
                    .append("\n");

            allMessages.append("Recipient: ")
                    .append(sent.recipient)
                    .append("\n");

            allMessages.append("Message: ")
                    .append(sent.message)
                    .append("\n\n");
        }

        return allMessages.toString();
    }

    // ===========================================
    // RETURN TOTAL MESSAGES SENT
    // ===========================================

    public static int returnTotalMessages() {

        return totalMessages;
    }

    // ===========================================
    // STORE MESSAGES IN JSON
    // ===========================================

    /*
     * This method is retained for compatibility with the
     * original Part 2 implementation and its unit tests
     *
     * Part 3 uses StoredMessages for the main application's
     * JSON storage and retrieval
     */
    public boolean storeMessage() {

        StringBuilder json = new StringBuilder();

        json.append("[\n");

        for (int i = 0; i < storedMessages.size(); i++) {

            Message stored = storedMessages.get(i);

            json.append("  {\n")
                    .append("    \"messageID\": \"")
                    .append(escapeJson(stored.messageID))
                    .append("\",\n")
                    .append("    \"messageNumber\": ")
                    .append(stored.messageNumber)
                    .append(",\n")
                    .append("    \"recipient\": \"")
                    .append(escapeJson(stored.recipient))
                    .append("\",\n")
                    .append("    \"message\": \"")
                    .append(escapeJson(stored.message))
                    .append("\",\n")
                    .append("    \"messageHash\": \"")
                    .append(escapeJson(stored.messageHash))
                    .append("\"\n")
                    .append("  }");

            if (i < storedMessages.size() - 1) {
                json.append(",");
            }

            json.append("\n");
        }

        json.append("]");

        try (FileWriter writer = new FileWriter("storedMessages.json")) {

            writer.write(json.toString());

            return true;

        } catch (IOException error) {

            System.out.println(
                    "Could not write JSON file: " + error.getMessage()
            );

            return false;
        }
    }

    // ===========================================
    // ESCAPE JSON TEXT
    // ===========================================

    private static String escapeJson(String text) {

        if (text == null) {
            return "";
        }

        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
