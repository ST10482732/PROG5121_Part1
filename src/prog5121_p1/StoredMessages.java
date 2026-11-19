/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prog5121_p1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;


/**
 *
 * @author ralik
 */
public class StoredMessages {
    
    private final String jsonFile;
    
    // Five parallel arrays to store message details for part 3
    private String[] sentMessage = new String[0];
    private String[] disregardedMessages = new String[0];
    private String[] storedMessages = new String[0];
    private String[] messageHash = new String[0];
    private String[] messageID = new String[0];
    
    // Three additional parallel arrays to store recipient, sender and status details
    private String[] recipients = new String[0];
    private String[] senders = new String[0];
    private String[] statuses = new String[0];
    
    // Stores all complete message records in a list
    private final List<MessageRecord> messages = new ArrayList<>();
    
    /**
     * List to store all complete MessageRecord objects
     */
     private static class MessageRecord {

        private String id;
        private String hash;
        private String recipient;
        private String text;
        private String status;
        private String sender;
        
        private MessageRecord(String id, String hash,
                String recipient, String text,
                String status, String sender) {

            this.id = id;
            this.hash = hash;
            this.recipient = recipient;
            this.text = text;
            this.status = status;
            this.sender = sender;
        }
     }
     
     /**
      * Default constructor that loads messages from the default JSON file
      */
      public StoredMessages() {
        this("storedMessages.json");
    }
      /**
       * Constructor that allows a custom JSON file to be used for testing
       */
       public StoredMessages(String jsonFile) {

        this.jsonFile = jsonFile;

        loadStoredMessagesFromJSON();

        populateArrays();
    }
       
       /**
        * Adds a new message to the application using "Developer" as the default sender
        */
        public void addMessage(String id, String hash,
            String recipient, String text,
            String status) {

        addMessage(id, hash, recipient, text, status, "Developer");
    }
        
        /**
         * Stores a full message record with all its details
         */
         public void addMessage(String id, String hash,
            String recipient, String text,
            String status, String sender) {

        if (id == null || hash == null || recipient == null
                || text == null || status == null || sender == null) {
            throw new IllegalArgumentException(
                    "Message details cannot be null.");
        }
        
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException(
                    "Invalid message status.");
        }

        if (findRecordByID(id) >= 0) {
            throw new IllegalArgumentException(
                    "A message with this ID already exists.");
        }
        
        messages.add(new MessageRecord(
                id, hash, recipient, text, status, sender));

        populateArrays();

        if (status.equalsIgnoreCase("Stored")) {
            saveStoredMessagesToJSON();
        }
    }
         
         /**
          * Verifies if the given message status is one of the allowed values
          */
         private boolean isValidStatus(String status) {

        return status.equalsIgnoreCase("Sent")
                || status.equalsIgnoreCase("Stored")
                || status.equalsIgnoreCase("Disregard")
                || status.equalsIgnoreCase("Disregarded");
    }
         
         /**
          * Fills the five required arrays and extra arrays using the stored message records
          */
         public final void populateArrays() {

        List<String> sent = new ArrayList<>();
        List<String> disregarded = new ArrayList<>();
        List<String> stored = new ArrayList<>();

        List<String> hashes = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        
        List<String> recipientList = new ArrayList<>();
        List<String> senderList = new ArrayList<>();
        List<String> statusList = new ArrayList<>();

        for (MessageRecord message : messages) {

            hashes.add(message.hash);
            ids.add(message.id);

            recipientList.add(message.recipient);
            senderList.add(message.sender);
            statusList.add(message.status);
            
            if (message.status.equalsIgnoreCase("Sent")) {

                sent.add(message.text);

            } else if (message.status.equalsIgnoreCase("Disregard")
                    || message.status.equalsIgnoreCase("Disregarded")) {

                disregarded.add(message.text);

            } else if (message.status.equalsIgnoreCase("Stored")) {

                stored.add(message.text);
            }
        }
        
        sentMessage = sent.toArray(new String[0]);
        disregardedMessages = disregarded.toArray(new String[0]);
        storedMessages = stored.toArray(new String[0]);

        messageHash = hashes.toArray(new String[0]);
        messageID = ids.toArray(new String[0]);

        recipients = recipientList.toArray(new String[0]);
        senders = senderList.toArray(new String[0]);
        statuses = statusList.toArray(new String[0]);
    }
         
         /**
          * Shows the sender and recipient for each stored message
          */
         public String displaySenderAndRecipient() {

        StringBuilder result = new StringBuilder();

        for (MessageRecord message : messages) {

            if (message.status.equalsIgnoreCase("Stored")) {

                result.append("Sender: ")
                        .append(message.sender)
                        .append("\nRecipient: ")
                        .append(message.recipient)
                        .append("\n\n");
            }
        }
        
        if (result.length() == 0) {
            return "No stored messages found.";
        }

        return result.toString().trim();
    }
         
         
         /**
         * Identifies the longest message from the stored messages
         */
        public String displayLongestMessage() {

           String longest = "";

             for (MessageRecord message : messages) {

              if (message.status.equalsIgnoreCase("Stored")
                && message.text.length() > longest.length()) {

            longest = message.text;
        }
    }

    if (longest.isEmpty()) {
        return "No stored messages available.";
    }

    return longest;
  }

          
          /**
           * Looks up a message by its ID
           * 
           * A sender number can also be used as the search value to support
           * the developer-number test case from the assignment brief
           */
          public String searchMessageByID(String searchValue) {

              for (MessageRecord message : messages) {

                  if (message.id.equals(searchValue)
                           || message.sender.equals(searchValue)) {

                      return "Recipient: " + message.recipient
                              + "\nMessage: " + message.text;
            }
        }
              
         return "Message not found.";
    }
          
          
        /**
         * Returns just the message text for in unit tests
         */
        public String getMessageByID(String searchValue) {

        for (MessageRecord message : messages) {

            if (message.id.equals(searchValue)
                    || message.sender.equals(searchValue)) {

                return message.text;
            }
        }
        
        return null;
    }
        
        /**
         * Searches for all sent and stored messages belonging
         * to a particular recipient
         * 
         * Disregarded messages are excluded from the results
         * 
         * @param recipient the recipient's cellphone number
         * @return matching message texts
         */
        
        public String searchMessagesByRecipient(String recipient) {

            StringBuilder result = new StringBuilder();

            for (MessageRecord message : messages) {

                 boolean matchingRecipient =
                         message.recipient.equalsIgnoreCase(recipient);

        boolean validStatus =
                message.status.equalsIgnoreCase("Sent")
                || message.status.equalsIgnoreCase("Stored");

        if (matchingRecipient && validStatus) {

            if (result.length() > 0) {
                result.append("\n");
            }

            result.append(message.text);
        }
    }

    return result.toString();
}

        
      /**
       * Removing a message by matching its massage hash
       */
         public String deleteMessageByHash(String hash) {

        for (int i = 0; i < messages.size(); i++) {

            MessageRecord message = messages.get(i);

            if (message.hash.equals(hash)) {

                String deletedMessage = message.text;

                messages.remove(i);

                populateArrays();

                saveStoredMessagesToJSON();

                return "Message: \"" + deletedMessage
                        + "\" successfully deleted.";
            }
        }
        
        return "Message hash not found.";
    }
         
         /**
          * Shows the full report for all stored messages
          */
         public String displayMessageReport() {

        if (messages.isEmpty()) {
            return "No messages available for the report.";
        }

        StringBuilder report = new StringBuilder();

        report.append("\n========== MESSAGE REPORT ==========\n");

        for (MessageRecord message : messages) {

            report.append("\nMessage ID: ")
                    .append(message.id);

            report.append("\nMessage Hash: ")
                    .append(message.hash);

            report.append("\nSender: ")
                    .append(message.sender);

            report.append("\nRecipient: ")
                    .append(message.recipient);

            report.append("\nMessage: ")
                    .append(message.text);

            report.append("\nStatus: ")
                    .append(message.status);

            report.append("\n------------------------------------\n");
        }

        report.append("\nTotal messages: ")
                .append(messages.size());

        return report.toString();
    }

    /**
     * Finds a record using its ID
     */
    private int findRecordByID(String id) {

        for (int i = 0; i < messages.size(); i++) {

            if (messages.get(i).id.equals(id)) {
                return i;
            }
        }

        return -1;
    }
    
    /**
     * Loads the saved messages from the JSON file
     * 
     * This implementation uses Java's built-in Files, Path,
     * and regular-expression classes. No external JSON-reading
     * source or third-party library was used
     *
     * The method reads the message details and adds them
     * to the application's message collection
     */
    public final void loadStoredMessagesFromJSON() {

        Path path = Path.of(jsonFile);

        if (!Files.exists(path)) {
            return;
        }

        try {

            String json = Files.readString(
                    path, StandardCharsets.UTF_8);

            Pattern objectPattern = Pattern.compile(
                    "\\{([^{}]*)\\}");

            Matcher objectMatcher = objectPattern.matcher(json);

            while (objectMatcher.find()) {

                String object = objectMatcher.group(1);

                String id = readString(object, "messageID");
                String hash = readString(object, "messageHash");
                String recipient = readString(object, "recipient");
                String text = readString(object, "message");

                String status = readString(object, "status");
                String sender = readString(object, "sender");
                
                 // Part 2 JSON files only hold stored messages 
                if (status == null) {
                    status = "Stored";
                }

                if (sender == null) {
                    sender = "Developer";
                }

                if (id != null && hash != null
                        && recipient != null && text != null
                        && findRecordByID(id) < 0) {

                    messages.add(new MessageRecord(
                            id, hash, recipient, text,
                            status, sender));
                }
            }

        } catch (IOException error) {

            System.out.println(
                    "Could not read JSON file: " + error.getMessage());
        }
    }
    
     /**
     * Gets a string value from a JSON object and decodes escaped characters
     */
    private String readString(String object, String key) {

        Pattern pattern = Pattern.compile(
                "\"" + Pattern.quote(key)
                + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");

        Matcher matcher = pattern.matcher(object);

        if (matcher.find()) {
            return unescapeJson(matcher.group(1));
        }

        return null;
    }

    /**
     * Changes JSON escaped characters back into plain Java text
     */
    private String unescapeJson(String value) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {

            char current = value.charAt(i);

            if (current == '\\' && i + 1 < value.length()) {

                char next = value.charAt(++i);

                switch (next) {

                    case '"':
                        result.append('"');
                        break;

                    case '\\':
                        result.append('\\');
                        break;

                    case 'n':
                        result.append('\n');
                        break;

                    case 'r':
                        result.append('\r');
                        break;

                    case 't':
                        result.append('\t');
                        break;

                    case 'b':
                        result.append('\b');
                        break;

                    case 'f':
                        result.append('\f');
                        break;

                    default:
                        result.append(next);
                }

            } else {

                result.append(current);
            }
        }

        return result.toString();
    }

    /**
     * Prepares text for saving by adding JSON escape characters
     */
    private String escapeJson(String value) {

        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * Writes stored messages to the JSON file
     *
     * Only messages marked as Stored are included in this file
     */
    public void saveStoredMessagesToJSON() {

        StringBuilder json = new StringBuilder();

        json.append("[\n");

        boolean first = true;

        for (MessageRecord message : messages) {

            if (!message.status.equalsIgnoreCase("Stored")) {
                continue;
            }

            if (!first) {
                json.append(",\n");
            }

            json.append("  {\n")
                    .append("    \"messageID\": \"")
                    .append(escapeJson(message.id))
                    .append("\",\n")
                    .append("    \"messageHash\": \"")
                    .append(escapeJson(message.hash))
                    .append("\",\n")
                    .append("    \"recipient\": \"")
                    .append(escapeJson(message.recipient))
                    .append("\",\n")
                    .append("    \"message\": \"")
                    .append(escapeJson(message.text))
                    .append("\",\n")
                    .append("    \"status\": \"Stored\",\n")
                    .append("    \"sender\": \"")
                    .append(escapeJson(message.sender))
                    .append("\"\n")
                    .append("  }");

            first = false;
        }

        json.append("\n]");

        try {

            Files.writeString(
                    Path.of(jsonFile),
                    json.toString(),
                    StandardCharsets.UTF_8);

        } catch (IOException error) {

            System.out.println(
                    "Could not save JSON file: " + error.getMessage());
        }
    }

    /**
     * Shows the Stored Messages menu for part 3
     */
    public void showStoredMessagesMenu(Scanner input) {

        boolean running = true;

        while (running) {

            System.out.println("\n========== STORED MESSAGES ==========");
            System.out.println("1. Display sender and recipient");
            System.out.println("2. Display longest message");
            System.out.println("3. Search for message ID");
            System.out.println("4. Search messages by recipient");
            System.out.println("5. Delete message using hash");
            System.out.println("6. Display message report");
            System.out.println("7. Return to main menu");

            System.out.print("Choose an option: ");

            String option = input.nextLine().trim();

            switch (option) {

                case "1":
                    System.out.println(displaySenderAndRecipient());
                    break;

                case "2":
                    System.out.println(displayLongestMessage());
                    break;

                case "3":
                    System.out.print("Enter message ID: ");
                    System.out.println(
                            searchMessageByID(input.nextLine().trim()));
                    break;

                case "4":
                    System.out.print("Enter recipient number: ");
                    System.out.println(
                            searchMessagesByRecipient(
                                    input.nextLine().trim()));
                    break;

                case "5":
                    System.out.print("Enter message hash: ");
                    System.out.println(
                            deleteMessageByHash(input.nextLine().trim()));
                    break;

                case "6":
                    System.out.println(displayMessageReport());
                    break;

                case "7":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Array getters used for testing and linking with the rest of the application

    public String[] getSentMessage() {
        return sentMessage.clone();
    }

    public String[] getDisregardedMessages() {
        return disregardedMessages.clone();
    }

    public String[] getStoredMessages() {
        return storedMessages.clone();
    }

    public String[] getMessageHash() {
        return messageHash.clone();
    }

    public String[] getMessageID() {
        return messageID.clone();
    }

    public String[] getRecipients() {
        return recipients.clone();
    }

    public String[] getSenders() {
        return senders.clone();
    }

    public String[] getStatuses() {
        return statuses.clone();
    }

    public int getTotalMessages() {
        return messages.size();
    }
}

     


        
       
    

