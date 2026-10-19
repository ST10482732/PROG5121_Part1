// Commit 2
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

    // Saves messages sent while the program is running
    private static ArrayList<Message> sentMessages = new ArrayList<>();

    // Keeps messages that were ignored
    private static ArrayList<Message> disregardedMessages = new ArrayList<>();

    // Holds messages saved for later
    private static ArrayList<Message> storedMessages = new ArrayList<>();

    // Total count of messages sent
    private static int totalMessages = 0;

    // ===========================================
    // CONSTRUCTOR
    // ===========================================

    public Message(String messageID, int messageNumber, String recipient, String message) {

        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.messageStatus = "Created";

        this.messageHash = createMessageHash();
    }

    // ===========================================
    // START QUICKCHAT
    // ===========================================

    public static void startQuickChat(Scanner input) {

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
        } while (numberOfMessages <= 0);

        input.nextLine();

        // Application menu loop
        boolean running = true;

        while (running) {

            System.out.println("\n=====================================");
            System.out.println("          QUICKCHAT Options:");
            System.out.println("=====================================");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.print("Choose an option: ");

            String option = input.nextLine();

            switch (option) {

                case "1":

                    sendMessages(input, numberOfMessages);

                    break;

                case "2":

                    System.out.println("\nComing Soon.");

                    break;

                case "3":

                    running = false;

                    System.out.println("\nThank you for using QuickChat.");

                    break;

                default:

                    System.out.println("\nInvalid option. Please enter 1, 2 or 3.");
            }
        }
    }

    // ===========================================
    // SEND MESSAGES
    // ===========================================

    public static void sendMessages(Scanner input, int numberOfMessages) {

        /*
         * The for loop lets the user enter messages
         * only the number of messages they chose.
         * The loop counter is the message number, starting from 0.
         */
        for (int i = 0; i < numberOfMessages; i++) {

            System.out.println("\nMESSAGE " + (i + 1));

            // MESSAGE ID

            String messageID = generateMessageID();

            System.out.println("Message ID generated: " + messageID);

            // RECIPIENT

            String recipient;

            while (true) {

                System.out.print("Enter recipient cell number: ");
                recipient = input.nextLine().trim();

                // A temporary message is used so we can reuse checkRecipientCell()
                String cellResult = new Message(messageID, i, recipient, " ").checkRecipientCell();

                System.out.println(cellResult);

                if (cellResult.equals("Cell phone number successfully captured.")) {
                    break;
                }
            }

            // MESSAGE TEXT

            String messageText;

            while (true) {

                System.out.print("Enter your message: ");
                messageText = input.nextLine();

                String lengthResult = checkMessageLength(messageText);

                System.out.println(lengthResult);

                if (lengthResult.equals("Message ready to send.")) {
                    break;
                }

                System.out.println("Please enter a message of less than 250 characters.");
            }

            // ===========================================
            // CREATE MESSAGE
            // ===========================================

            Message newMessage = new Message(
                    messageID,
                    i,
                    recipient,
                    messageText
            );

            // ===========================================
            // DISPLAY HASH
            // ===========================================

            System.out.println("Message Hash: " + newMessage.createMessageHash());

            // ===========================================
            // SEND MESSAGE
            // ===========================================

            String result = newMessage.SentMessage(input);

            System.out.println("\n" + result);

            // Ignored messages can be deleted by pressing 0
            if (newMessage.messageStatus.equals("Disregarded")) {

                String deleteChoice = input.nextLine().trim();

                if (deleteChoice.equals("0")) {
                    disregardedMessages.remove(newMessage);
                    System.out.println("Message deleted.");
                }
            }

            // ===========================================
            // DISPLAY DETAILS
            // ===========================================

            if (newMessage.messageStatus.equals("Sent")) {

                System.out.println("\n=====================================");

                System.out.println("Message ID: "
                        + newMessage.messageID);

                System.out.println("Message Hash: "
                        + newMessage.messageHash);

                System.out.println("Recipient: "
                        + newMessage.recipient);

                System.out.println("Message: "
                        + newMessage.message);

                System.out.println("=====================================");
            }
        }

        System.out.println("\nTotal messages sent: " + returnTotalMessages());
    }

    // ===========================================
    // GENERATE MESSAGE ID (random ten-digit number)
    // ===========================================

    public static String generateMessageID() {

        Random random = new Random();
        String id = "";

        for (int digit = 0; digit < 10; digit++) {
            id = id + random.nextInt(10);
        }

        return id;
    }

    // ===========================================
    // CHECK MESSAGE ID (not more than ten characters)
    // ===========================================

    public boolean checkMessageID() {

        return messageID != null && messageID.length() <= 10;
    }

    // ===========================================
    // CHECK RECIPIENT CELL
    // ===========================================

    public String checkRecipientCell() {

        /*
         * Reuses the Part 1 cell phone check:
         * +27 international code followed by 9 digits.
         * Example: +27718693002
         */
        Login login = new Login();

        if (recipient != null && login.checkCellPhoneNumber(recipient)) {

            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not "
                + "contain an international code. Please correct the number and try again.";
    }

    // ===========================================
    // CHECK MESSAGE LENGTH (250 characters maximum)
    // ===========================================

    public static String checkMessageLength(String text) {

        if (text.length() <= 250) {

            return "Message ready to send.";
        }

        int excess = text.length() - 250;

        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    // ===========================================
    // CREATE MESSAGE HASH
    // ===========================================

    public String createMessageHash() {

        String firstTwoNumbers;

        if (messageID.length() >= 2) {

            firstTwoNumbers = messageID.substring(0, 2);

        } else {

            firstTwoNumbers = messageID;
        }

        String firstWord = "";
        String lastWord = "";

        if (message != null && !message.trim().isEmpty()) {

            String cleanMessage = message.trim();

            String[] words = cleanMessage.split("\\s+");

            firstWord = words[0];

            lastWord = words[words.length - 1];

            // Remove punctuation
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

    public String SentMessage(Scanner input) {

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

                    return "Press 0 to delete the message.";

                case "3":

                    messageStatus = "Stored";
                    storedMessages.add(this);

                    if (storeMessage()) {
                        return "Message successfully stored.";
                    }

                    return "Message could not be stored.";

                default:

                    System.out.println("Invalid option. Please enter 1, 2 or 3.");
            }
        }
    }

    // ===========================================
    // PRINT ALL SENT MESSAGES
    // ===========================================

    public static String printMessages() {

        String allMessages = "";

        for (Message sent : sentMessages) {

            allMessages = allMessages
                    + "Message ID: " + sent.messageID + "\n"
                    + "Message Hash: " + sent.messageHash + "\n"
                    + "Recipient: " + sent.recipient + "\n"
                    + "Message: " + sent.message + "\n\n";
        }

        return allMessages;
    }

    // ===========================================
    // RETURN TOTAL MESSAGES SENT
    // ===========================================

    public static int returnTotalMessages() {

        return totalMessages;
    }

    // ===========================================
    // STORE MESSAGES IN A JSON FILE
    // ===========================================

    public boolean storeMessage() {

        String json = "[\n";

        for (int i = 0; i < storedMessages.size(); i++) {

            Message stored = storedMessages.get(i);

            json = json
                    + "  {\n"
                    + "    \"messageID\": \"" + escapeJson(stored.messageID) + "\",\n"
                    + "    \"messageNumber\": " + stored.messageNumber + ",\n"
                    + "    \"recipient\": \"" + escapeJson(stored.recipient) + "\",\n"
                    + "    \"message\": \"" + escapeJson(stored.message) + "\",\n"
                    + "    \"messageHash\": \"" + escapeJson(stored.messageHash) + "\"\n"
                    + "  }";

            // No comma after the last item
            if (i < storedMessages.size() - 1) {
                json = json + ",";
            }

            json = json + "\n";
        }

        json = json + "]";

        try (FileWriter writer = new FileWriter("storedMessages.json")) {

            writer.write(json);

            return true;

        } catch (IOException error) {

            System.out.println("Could not write JSON file: " + error.getMessage());

            return false;
        }
    }

    // Makes text safe to put inside a JSON string
    private static String escapeJson(String text) {

        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }
}