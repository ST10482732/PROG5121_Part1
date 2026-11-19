/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package prog5121_p1;

// Import the classes used for the particular file handling, arrays and JUnit testing
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

// Unit tests that check the StoredMessages class
public class StoredMessagesTest {
    
    
    // Objects used for testing stored messages and the temporary JSON file
    private StoredMessages storedMessages;
    private Path testFile;

    // Sample messages used across the units tests
    private static final String MESSAGE_1 = "Did you get the cake?";
    private static final String MESSAGE_2 =
            "Where are you? You are late! I have asked you to be on time.";
    private static final String MESSAGE_3 =
            "Yohoooo, I am at your gate.";
    private static final String MESSAGE_4 = "It is dinner time!";
    private static final String MESSAGE_5 =
            "Ok, I am leaving without you.";
    
    // Runs before every test to set up a new test file and test messages
    @Before
    public void setUp() throws Exception {
        
        // Make a temporary JSON file for testing
        testFile = Files.createTempFile("storedMessagesTest", ".json");
        Files.deleteIfExists(testFile);
        
        // Set up the StoredMessages object with the temporary file
        storedMessages = new StoredMessages(testFile.toString());
        
        // Add the first message as sent
        storedMessages.addMessage(
                "0000000001",
                "00:0:DIDCAKE",
                "+27834557896",
                MESSAGE_1,
                "Sent",
                "Developer"
        );

        // Add the second message as Stored
        storedMessages.addMessage(
                "0000000002",
                "00:1:WHEREON",
                "+27838884567",
                MESSAGE_2,
                "Stored",
                "Developer"
        );
 
        // Add the third message as Disregarded
        storedMessages.addMessage(
                "0000000003",
                "00:2:YOHOOGATE",
                "+27834484567",
                MESSAGE_3,
                "Disregard",
                "Developer"
        );
 
        // Add the fourth message as Sent
        storedMessages.addMessage(
                "0000000004",
                "00:3:ITTIME",
                "+27834557896",
                MESSAGE_4,
                "Sent",
                "0838884567"
        );
  
        // Add the fifth message as Stored
        storedMessages.addMessage(
                "0000000005",
                "00:4:OKYOU",
                "+27838884567",
                MESSAGE_5,
                "Stored",
                "Developer"
        );
    }

    // Runs after every test to delete the temporary JSON file
    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(testFile);
    }
 
    // Check that the sent messages array holds the right messages
    @Test
    public void testSentMessagesArray() {

        String[] expected = {MESSAGE_1, MESSAGE_4};

        assertArrayEquals(expected, storedMessages.getSentMessage());
    }

    // Check that the longest stored message is returned correctly
    @Test
    public void testDisplayLongestMessage() {

        assertEquals(
                MESSAGE_2,
                storedMessages.displayLongestMessage()
        );
    }

    // Check searching for a message by the sender's cellphone number
    @Test
    public void testSearchMessageBySenderNumber() {

        assertEquals(
                MESSAGE_4,
                storedMessages.getMessageByID("0838884567")
        );
    }

    // Check searching for a message by its message ID
    @Test
    public void testSearchMessageByID() {

        assertEquals(
                MESSAGE_4,
                storedMessages.getMessageByID("0000000004")
        );
    }

    // Check that searching with an inavlid message ID gives null
    @Test
    public void testSearchMessageByInvalidID() {

        assertNull(
                storedMessages.getMessageByID("9999999999")
        );
    }

    // Check searching for stored messages by the recepient's cellphone number
    @Test
    public void testSearchMessagesByRecipient() {

        String expected = MESSAGE_2 + "\n" + MESSAGE_5;

        assertEquals(
                expected,
                storedMessages.searchMessagesByRecipient("+27838884567")
        );
    }

    // Checking deleting a message using its message hash
    @Test
    public void testDeleteMessageByHash() {

        String expected =
                "Message: \"" + MESSAGE_2 + "\" successfully deleted.";

        assertEquals(
                expected,
                storedMessages.deleteMessageByHash("00:1:WHEREON")
        );

        // Confirm the total message count drops after delection
        assertEquals(4, storedMessages.getTotalMessages());
    }

    // Check that an invalid message hash does not delete a message
    @Test
    public void testDeleteMessageUsingInvalidHash() {

        assertEquals(
                "Message hash not found.",
                storedMessages.deleteMessageByHash("INVALID")
        );
    }

    // Check that the complete message report includes all the needed details
    @Test
    public void testDisplayMessageReport() {

        String report = storedMessages.displayMessageReport();

        assertTrue(report.contains(MESSAGE_1));
        assertTrue(report.contains(MESSAGE_4));
        assertTrue(report.contains("00:0:DIDCAKE"));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains("Developer"));
        assertTrue(report.contains("Sent"));
        assertTrue(report.contains("Stored"));
        assertTrue(report.contains("Disregard"));
    }

    // Test that the disregarded messages array contains the correct message
    @Test
    public void testDisregardedMessagesArray() {

        String[] expected = {MESSAGE_3};

        assertArrayEquals(
                expected,
                storedMessages.getDisregardedMessages()
        );
    }

    // Test that the stored messages array contains only stored messages
    @Test
    public void testStoredMessagesArray() {

        String[] expected = {MESSAGE_2, MESSAGE_5};

        assertArrayEquals(
                expected,
                storedMessages.getStoredMessages()
        );
    }

     // Test that the message hash array contains the correct hashes
    @Test
    public void testMessageHashArray() {

        String[] hashes = storedMessages.getMessageHash();

        assertEquals(5, hashes.length);
        assertEquals("00:1:WHEREON", hashes[1]);
    }

      // Test that the message ID array contains the correct IDs
    @Test
    public void testMessageIDArray() {

        String[] ids = storedMessages.getMessageID();

        assertEquals(5, ids.length);
        assertEquals("0000000004", ids[3]);
    }

      // Test that only stored messages are saved in the JSON file
    @Test
    public void testStoredMessagesSavedToJSON() throws Exception {

        String json = Files.readString(testFile);

        assertTrue(json.contains(MESSAGE_2));
        assertTrue(json.contains(MESSAGE_5));

        // Sent messages should not be included in the stored messages JSON file
        assertFalse(json.contains(MESSAGE_1));
        assertFalse(json.contains(MESSAGE_4));
    }

       // Test that stored messages can be loaded from the JSON file
    @Test
    public void testReadStoredMessagesFromJSON() {

        StoredMessages loadedMessages =
                new StoredMessages(testFile.toString());

        String[] expected = {MESSAGE_2, MESSAGE_5};

        assertArrayEquals(
                expected,
                loadedMessages.getStoredMessages()
        );
    }

    
    // Test that the sender and recipient information is displayed correctly
    @Test
    public void testDisplaySenderAndRecipient() {

        String result = storedMessages.displaySenderAndRecipient();

        assertTrue(result.contains("Developer"));
        assertTrue(result.contains("+27838884567"));
    }

        // Test that the stored messages array is updated after deleting a message
    @Test
    public void testArraysUpdateAfterDeletion() {

        storedMessages.deleteMessageByHash("00:1:WHEREON");

        assertEquals(
                1,
                storedMessages.getStoredMessages().length
        );

        assertTrue(
                Arrays.asList(storedMessages.getStoredMessages())
                        .contains(MESSAGE_5)
        );

        assertFalse(
                Arrays.asList(storedMessages.getStoredMessages())
                        .contains(MESSAGE_2)
        );
    }
}
